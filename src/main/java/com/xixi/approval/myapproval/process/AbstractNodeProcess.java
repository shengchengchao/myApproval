package com.xixi.approval.myapproval.process;

import com.xixi.approval.myapproval.entity.ApprovalConfigEntity;
import com.xixi.approval.myapproval.entity.ApprovalLogEntity;
import com.xixi.approval.myapproval.enums.StatusEnum;
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
public abstract class AbstractNodeProcess {


    /**
     *  这里 通用的处理就是  写一份日志 修改表中节点顺序
     */
    public void approvalNode(){
        //校验node
        checkNode();
        approval();
        createLog();
        updateNode();

    }

    /**
     * 创建日志
     */
    protected void createLog(){
        ApprovalLogEntity approvalLogEntity = new ApprovalLogEntity();


    }

    /**
     * 审批
     * @return 审批是否通过 true为通过 false为失败，其中可以进行检验
     */
    public abstract Boolean approval();


    /**
     * 空方法 在子类中实现 用于做一些自定义的操作
     */
    public abstract void updateNode();

    /**
     * 驳回节点 这里需要做的就是 将节点回滚 那么就是记录一条日志 同时将节点回滚一位 ，状态设置为驳回
     */
    public  void rollbackNode(){

        checkNode();
        rollback();
        createLog();
        updateNode();
    }

    /**
     * 审批是否通过 true为通过 false为失败，其中可以进行检验
     * @return 判断审批是否通过
     */
    public abstract Boolean rollback();

    /**
     * 校验节点 判断节点能否被处理
     */
    protected abstract void checkNode();

    public  AbstractNode createDefaultNode(){
        AbstractNode abstractNode = new AbstractNode();
        abstractNode.setNodeIdx(0);
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
        AbstractNode childHeadNode = createDefaultNode();
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
                approvalLogEntity.getRelateId(), approvalConfigEntity.getNodeType(), approvalConfigEntity.getName(),
                Optional.ofNullable(approvalLogEntity.getReason()).orElse(""));
        return simpleNode;
    }
}
