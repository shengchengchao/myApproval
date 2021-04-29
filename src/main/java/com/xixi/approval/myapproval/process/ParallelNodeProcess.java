package com.xixi.approval.myapproval.process;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import com.xixi.approval.myapproval.dto.ApprovalDTO;
import com.xixi.approval.myapproval.entity.ApprovalConfigEntity;
import com.xixi.approval.myapproval.entity.ApprovalLogEntity;
import com.xixi.approval.myapproval.enums.StatusEnum;
import com.xixi.approval.myapproval.exception.ApprovalException;
import com.xixi.approval.myapproval.node.AbstractNode;
import com.xixi.approval.myapproval.node.ParallelNode;
import com.xixi.approval.myapproval.node.SimpleNode;
import com.xixi.approval.myapproval.service.ApprovalLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author shengchengchao
 * @Description
 * @createTime 2021/4/27
 */
@Component
public class ParallelNodeProcess extends CommonApprovalProcess {

    @Autowired
    private ApprovalLogService approvalLogService;
    /**
     * 得到第一层的节点
     *
     * @param approvalConfigEntity 节点
     * @param approvalLogEntity 日志
     * @return AbstractNode 节点
     */
    @Override
    protected AbstractNode getNode(ApprovalConfigEntity approvalConfigEntity, ApprovalLogEntity approvalLogEntity) {
        ParallelNode parallelNode = new ParallelNode(Lists.newArrayList(), approvalConfigEntity.getNodeType(),
                Optional.ofNullable(approvalLogEntity.getStatus()).orElse(StatusEnum.FUTURE.getStatus()),
                approvalConfigEntity.getNodeType(), approvalConfigEntity.getName(),
                Optional.ofNullable(approvalLogEntity.getReason()).orElse(""),approvalLogEntity.getUserId()
                ,approvalConfigEntity.getChildrenIdx(),approvalConfigEntity.getNodeIndex());
        return parallelNode;
    }

    /**
     * 装配孩子节点
     * @param childHead 孩子的头节点
     * @param configList 配置的节点
     * @param logList 日志节点
     * @param node 父节点
     */
    @Override
    protected  void packageChildren(AbstractNode childHead, List<ApprovalConfigEntity> configList, List<ApprovalLogEntity> logList,  AbstractNode node) {
        AbstractNode cur =childHead;
        for (int i=1;i<configList.size();i++){
            ApprovalConfigEntity approvalConfigEntity = configList.get(i);
            Optional<ApprovalLogEntity> first = logList.stream().filter(x -> x.getChildrenIdx().equals(approvalConfigEntity.getChildrenIdx())).findFirst();

            cur.setChildNextNode(super.getSimpleNode(configList.get(i), first.orElseGet(ApprovalLogEntity::new)));
            cur = cur.getNextNode();
        }
        ((ParallelNode)node).setNode(childHead.getNextNode());
    }


    @Override
    protected AbstractNode checkNode(AbstractNode currentNode, ApprovalDTO approvalDTO) throws ApprovalException {
        //这里有一点不同 作为并行节点 只有所有节点中有一个审批就可以了
        ParallelNode node = (ParallelNode) currentNode;
        AbstractNode curNode =node.getNode();
        return getApprovalIndex(curNode, approvalDTO);
    }

    /**
     * 得到能够审批的节点索引 如果无法审批 抛出异常
     * 这里 不会出现多个节点 条件相同的情况
     * @param currentNode 当前节点
     * @param approvalDTO 审批条件
     * @return index 通过的节点数
     */
    protected AbstractNode getApprovalIndex(AbstractNode curNode, ApprovalDTO approvalDTO) throws ApprovalException{
        Assert.notNull(approvalDTO.getApprovalUserDTO());
        SimpleNode cur = (SimpleNode) curNode;

        while (cur!=null){
            if(cur.getApplyUser().contains(approvalDTO.getApprovalUserDTO().getCondition()) ){
                return  cur;
            }
            cur= (SimpleNode) cur.getNextNode();
        }

        throw new ApprovalException("没有权限审批");
    }


    @Override
    public void recordApprovalLog(AbstractNode currentNode, ApprovalDTO approvalDTO,AbstractNode children) throws ApprovalException {
        //把当前节点设为通过 将其他子节点 设为跳过
        approvalLogService.lambdaUpdate().eq(ApprovalLogEntity::getRelateId,approvalDTO.getRelated())
                .eq(ApprovalLogEntity::getNodeIndex,currentNode.getNodeIdx()).remove();
        List<ApprovalLogEntity> list = new ArrayList<>();
        ParallelNode node = (ParallelNode) currentNode;
        AbstractNode childrenNode = node.getNode();
        ApprovalLogEntity log = new ApprovalLogEntity("", StatusEnum.NORMAL.getStatus(), approvalDTO.getRelated(),
                currentNode.getNodeIdx(), 0,approvalDTO.getApprovalUserDTO().getId());
        list.add(log);
        //这里统计的是 找出来一个节点 能符合条件的 就将这个节点设为通过 其他节点设置为跳过
        while (childrenNode!=null){
            ApprovalLogEntity approvalLog = new ApprovalLogEntity("",
                    childrenNode.getChildrenIdx().equals(children.getChildrenIdx()) ? StatusEnum.NORMAL.getStatus():StatusEnum.SKIP.getStatus(),
                    approvalDTO.getRelated(), childrenNode.getNodeIdx(),
                    childrenNode.getChildrenIdx()
                    ,approvalDTO.getApprovalUserDTO().getId());
            list.add(approvalLog);
            childrenNode = childrenNode.getNextNode();
        }

        AbstractNode nextNode = currentNode.getNextNode();
        if(nextNode !=null){
            AbstractNodeProcess abstractNodeProcess = processMap.get(nextNode.getNodeType());
            list.addAll(abstractNodeProcess.saveNextNodeLog(nextNode, approvalDTO));
        }
        approvalLogService.saveBatch(list);

    }

    @Override
    public void recordRollbackLog(AbstractNode currentNode, ApprovalDTO approvalDTO,AbstractNode children) throws ApprovalException {
        approvalLogService.lambdaUpdate().eq(ApprovalLogEntity::getRelateId,approvalDTO.getRelated())
                .eq(ApprovalLogEntity::getNodeIndex,currentNode.getNodeIdx()).remove();
        List<ApprovalLogEntity> list = new ArrayList<>();
        ParallelNode node = (ParallelNode) currentNode;
        ApprovalLogEntity log = new ApprovalLogEntity("",
                StatusEnum.ROLLBACK.getStatus(), approvalDTO.getRelated(), currentNode.getNodeIdx(), node.getChildrenIdx(),approvalDTO.getApprovalUserDTO().getId());
        list.add(log);

        AbstractNode childrenNode = node.getNode();
        while (childrenNode!=null){
            ApprovalLogEntity approvalLog = new ApprovalLogEntity(childrenNode.getNodeIdx().equals(node.getNodeIdx()) ?approvalDTO.getRollbackReason():"",
                    childrenNode.getNodeIdx().equals(children.getNodeIdx()) ? StatusEnum.ROLLBACK.getStatus():StatusEnum.SKIP.getStatus(),
                    approvalDTO.getRelated(), childrenNode.getNodeIdx(), childrenNode.getChildrenIdx(),approvalDTO.getApprovalUserDTO().getId());
            list.add(approvalLog);
            childrenNode = childrenNode.getNextNode();
        }
        approvalLogService.saveBatch(list);

    }

    /**
     *  保存下一个节点日志
     * @param currentNode 下一个节点
     * @return 日志集合
     */
    @Override
    public List<ApprovalLogEntity> saveNextNodeLog(AbstractNode currentNode,ApprovalDTO approvalDTO) {
        List<ApprovalLogEntity> list = new ArrayList<>();
        ParallelNode parallel = (ParallelNode) currentNode;
        ApprovalLogEntity log = new ApprovalLogEntity("", StatusEnum.READY.getStatus(),
                approvalDTO.getRelated(), parallel.getNodeIdx(), parallel.getNodeIdx(),"");
        list.add(log);
        AbstractNode childrenNode = parallel.getNode();
        while (childrenNode!=null){
            ApprovalLogEntity approvalLog = new ApprovalLogEntity("", StatusEnum.READY.getStatus(),
                    approvalDTO.getRelated(), childrenNode.getNodeIdx(), childrenNode.getChildrenIdx(),"");
            list.add(approvalLog);
            childrenNode = childrenNode.getNextNode();
        }
        return list;
    }
}
