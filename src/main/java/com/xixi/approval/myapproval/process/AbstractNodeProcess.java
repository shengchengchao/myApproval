package com.xixi.approval.myapproval.process;

import cn.hutool.core.lang.Assert;
import com.xixi.approval.myapproval.dto.ApprovalDTO;
import com.xixi.approval.myapproval.entity.ApprovalConfigEntity;
import com.xixi.approval.myapproval.entity.ApprovalLogEntity;
import com.xixi.approval.myapproval.enums.StatusEnum;
import com.xixi.approval.myapproval.exception.ApprovalException;
import com.xixi.approval.myapproval.node.AbstractNode;
import com.xixi.approval.myapproval.node.SimpleNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author shengchengchao
 * @Description
 * @createTime 2021/4/26
 */
@Component
public abstract class AbstractNodeProcess extends AbstractProcess implements AbstractLogProcess {




    public  AbstractNode createDefaultNode(){
        AbstractNode abstractNode = new AbstractNode();
        abstractNode.setNodeIdx(0);
        return abstractNode;
    }

    public  AbstractNode createDefaultChildNode(){
        AbstractNode abstractNode = new AbstractNode();
        abstractNode.setChildrenIdx(0);
        return abstractNode;
    }

    /**
     * 根据 配置信息与日志 得到节点 并进行配置
     * @param configList 配置
     * @param logList 日志
     * @return AbstractNode 得到状态的节点
     */
    public  AbstractNode getNode(List<ApprovalConfigEntity> configList,List<ApprovalLogEntity> logList){
        if(CollectionUtils.isEmpty(configList)){
            throw new RuntimeException("配置数量不符");
        }

        AbstractNode node = getNode(configList.get(0),CollectionUtils.isEmpty(logList)? new ApprovalLogEntity() : logList.get(0)  );
        AbstractNode childHeadNode = createDefaultChildNode();
        packageChildren(childHeadNode,configList,logList,node);
        return node;
    }

    /**
     * 得到第一层的节点
     * @param approvalConfigEntity 配置
     * @param approvalLogEntity 日志
     * @return AbstractNode 节点
     */
    protected abstract AbstractNode getNode(ApprovalConfigEntity approvalConfigEntity,ApprovalLogEntity approvalLogEntity);

    /**
     * 装配孩子节点 交给子类实现
     * @param childHead 孩子的头节点
     * @param configList 配置的节点
     * @param logList 日志节点
     * @param node 父节点
     */
    protected  void packageChildren(AbstractNode childHead,List<ApprovalConfigEntity> configList,List<ApprovalLogEntity> logList,AbstractNode node){
    }


    protected AbstractNode getSimpleNode(ApprovalConfigEntity approvalConfigEntity, ApprovalLogEntity approvalLogEntity){
        String[] split = StringUtils.split(approvalConfigEntity.getApprovalRole(), ",");
        SimpleNode simpleNode = new SimpleNode(Arrays.asList(split), approvalConfigEntity.getNodeType(),
                Optional.ofNullable(approvalLogEntity.getStatus()).orElse(StatusEnum.FUTURE.getStatus()),
               approvalConfigEntity.getNodeType(), approvalConfigEntity.getName(),
                Optional.ofNullable(approvalLogEntity.getReason()).orElse(""),approvalLogEntity.getUserId(),approvalConfigEntity.getChildrenIdx()
                ,approvalConfigEntity.getNodeIndex());
        return simpleNode;
    }

    /**
     * 校验节点
     * @param currentNode 当前节点
     * @param approvalDTO 审批类
     * @return   AbstractNode 节点
     * @throws ApprovalException 异常
     */
    protected  AbstractNode checkNode(AbstractNode currentNode,ApprovalDTO approvalDTO) throws ApprovalException{
        SimpleNode node = (SimpleNode) currentNode;
        if(CollectionUtils.isEmpty(node.getApplyUser())){
            return node;
        }
        Assert.notNull(approvalDTO.getApprovalUserDTO());
        if(!node.getApplyUser().contains(approvalDTO.getApprovalUserDTO().getCondition())){
            throw  new ApprovalException("没有权限审批");
        }
        return node;
    }
}
