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
import com.xixi.approval.myapproval.node.CountersignNode;
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
public class CountersignNodeProcess extends CommonApprovalProcess {

    @Autowired
    private ApprovalLogService approvalLogService;
    /**
     * 得到第一层的节点
     *
     * @param approvalConfigEntity 配置
     * @param approvalLogEntity 日志
     * @return AbstractNode 会签节点
     */
    protected AbstractNode getNode(ApprovalConfigEntity approvalConfigEntity, ApprovalLogEntity approvalLogEntity) {
        CountersignNode countersignNode = new CountersignNode(Lists.newArrayList(), approvalConfigEntity.getNodeType(),
                Optional.ofNullable(approvalLogEntity.getStatus()).orElse(StatusEnum.FUTURE.getStatus()), approvalConfigEntity.getNodeType(),
                approvalConfigEntity.getName(), Optional.ofNullable(approvalLogEntity.getReason()).orElse(""),
                approvalLogEntity.getUserId(),approvalConfigEntity.getChildrenIdx(),approvalConfigEntity.getNodeIndex());
        return countersignNode;
    }

    /**
     * 装配孩子节点
     * @param childHead 孩子的头节点
     * @param configList 配置的节点
     * @param logList 日志节点
     * @param index 节点
     * @param node 父节点
     */
    @Override
    protected  void packageChildren(AbstractNode childHead, List<ApprovalConfigEntity> configList, List<ApprovalLogEntity> logList, AbstractNode node) {
        AbstractNode cur =childHead;
        for (int i=1;i<configList.size();i++){
            ApprovalConfigEntity approvalConfigEntity = configList.get(i);
            Optional<ApprovalLogEntity> first = logList.stream().filter(x -> x.getChildrenIdx().equals(approvalConfigEntity.getChildrenIdx())).findFirst();

            cur.setChildNextNode(super.getSimpleNode(configList.get(i), first.orElseGet(ApprovalLogEntity::new)));
            cur = cur.getNextNode();
        }
        ((CountersignNode)node).setNode(childHead.getNextNode());
    }


    @Override
    protected AbstractNode checkNode(AbstractNode currentNode, ApprovalDTO approvalDTO) throws ApprovalException {
        CountersignNode fatherNode = (CountersignNode) currentNode;
        AbstractNode curNode = fatherNode.getNode();
        return getApprovalIndex(curNode,approvalDTO);
    }

    protected AbstractNode getApprovalIndex(AbstractNode curNode, ApprovalDTO approvalDTO) throws ApprovalException{
        Assert.notNull(approvalDTO.getApprovalUserDTO());
        SimpleNode cur = (SimpleNode) curNode;
        while (cur!=null){
            if(StatusEnum.READY.getStatus().equals(cur.getStatus()) || StatusEnum.ROLLBACK.getStatus().equals(cur.getStatus()) ){
                super.checkNode(cur,approvalDTO);
                return cur;
            }
            cur= (SimpleNode) cur.getNextNode();
        }
        throw new ApprovalException("没有权限审批");
    }

    @Override
    public void recordApprovalLog(AbstractNode currentNode, ApprovalDTO approvalDTO,AbstractNode node) throws ApprovalException {
        Boolean flag = false;
        List<ApprovalLogEntity> list = new ArrayList<>();
        CountersignNode father = (CountersignNode) currentNode;
        AbstractNode childrenNode = father.getNode();
        approvalLogService.lambdaUpdate().eq(ApprovalLogEntity::getRelateId,approvalDTO.getRelated())
                .eq(ApprovalLogEntity::getNodeIndex,currentNode.getNodeIdx()).remove();
        //如果当前节点没有下一个节点的话 头节点要变成审批完成
        ApprovalLogEntity log = new ApprovalLogEntity("", ObjectUtil.isNull(node.getNextNode())?StatusEnum.NORMAL.getStatus():StatusEnum.READY.getStatus(), approvalDTO.getRelated(),currentNode.getNodeIdx(),
                currentNode.getChildrenIdx(),approvalDTO.getApprovalUserDTO().getId(),approvalDTO.getType());
        list.add(log);
        if(ObjectUtil.isNull(node.getNextNode()))  {
            flag = true;
        }

        while (childrenNode!=null){
            String status = StatusEnum.FUTURE.getStatus();
            if(childrenNode.getChildrenIdx()<=node.getChildrenIdx()){
                status=StatusEnum.NORMAL.getStatus();
            }else if(childrenNode.getChildrenIdx()==node.getChildrenIdx()+1){
                status=StatusEnum.READY.getStatus();
            }
            ApprovalLogEntity approvalLog = new ApprovalLogEntity("", status, approvalDTO.getRelated(),currentNode.getNodeIdx(),
                    childrenNode.getChildrenIdx(),approvalDTO.getApprovalUserDTO().getId(),approvalDTO.getType());
            list.add(approvalLog);
            childrenNode=childrenNode.getNextNode();
        }

        AbstractNode nextNode = currentNode.getNextNode();
        if(nextNode !=null && flag){
            AbstractNodeProcess abstractNodeProcess = processMap.get(nextNode.getNodeType());
            list.addAll(abstractNodeProcess.saveNextNodeLog(nextNode, approvalDTO));
        }
        approvalLogService.saveBatch(list);
    }

    @Override
    public void recordRollbackLog(AbstractNode currentNode, ApprovalDTO approvalDTO,AbstractNode node) throws ApprovalException {

        approvalLogService.lambdaUpdate().eq(ApprovalLogEntity::getRelateId,approvalDTO.getRelated())
                .eq(ApprovalLogEntity::getNodeIndex,currentNode.getNodeIdx())
                .and(i->i.eq(ApprovalLogEntity::getChildrenIdx,node.getChildrenIdx()).or().eq(ApprovalLogEntity::getChildrenIdx,0))
                .remove();
        ApprovalLogEntity approvalLog = new ApprovalLogEntity(approvalDTO.getRollbackReason(), StatusEnum.ROLLBACK.getStatus()
                , approvalDTO.getRelated(), currentNode.getNodeIdx(), node.getChildrenIdx(),approvalDTO.getApprovalUserDTO().getId(),approvalDTO.getType());
        ApprovalLogEntity fatherLog = new ApprovalLogEntity("", StatusEnum.ROLLBACK.getStatus()
                , approvalDTO.getRelated(), currentNode.getNodeIdx(), 0,approvalDTO.getApprovalUserDTO().getId(),approvalDTO.getType());
        List<ApprovalLogEntity> list = Lists.newArrayList(fatherLog, approvalLog);
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
        ApprovalLogEntity log = new ApprovalLogEntity("",StatusEnum.READY.getStatus()
                , approvalDTO.getRelated(), currentNode.getNodeIdx(), currentNode.getChildrenIdx(),"",approvalDTO.getType());
        list.add(log);

        boolean flag =true;
        AbstractNode node = ((CountersignNode) currentNode).getNode();
        while (node!=null){
            ApprovalLogEntity approvalLog = new ApprovalLogEntity("",flag? StatusEnum.READY.getStatus(): StatusEnum.FUTURE.getStatus()
                    , approvalDTO.getRelated(), currentNode.getNodeIdx(), node.getChildrenIdx(),"",approvalDTO.getType());
            list.add(approvalLog);
            node=node.getNextNode();
            flag=false;
        }
        return list;
    }
}
