package com.xixi.approval.myapproval.node;

import lombok.Builder;
import lombok.Data;


/**
 * @author shengchengchao
 * @Description 基础的节点
 * @createTime 2021/4/26
 */
@Data
public  class AbstractNode {

    /**
     * 审批类型
     */
    protected String type;

    /**
     * 状态
     */
    protected String status;


    /**
     * 节点类型
     */
    protected String nodeType;
    protected String name;

    protected String completeUser;
    /**
     * 节点顺序
     */
    protected Integer nodeIdx;

    /**
     * 原因
     */
    protected String reason;

    protected AbstractNode nextNode;

    /**
     * 子节点顺序
     */
    protected Integer childrenIdx;


    public AbstractNode(String type, String status, String nodeType, String name, String reason,String completeUser,Integer nodeIdx,Integer childrenIdx) {
        this.type = type;
        this.status = status;
        this.nodeType = nodeType;
        this.name = name;
        this.reason = reason;
        this.completeUser = completeUser;
        this.nodeIdx = nodeIdx;
        this.childrenIdx = childrenIdx;
    }

    public AbstractNode() {
    }

    public void setNextNode(AbstractNode node){
        node.nodeIdx = nodeIdx+1;
        this.nextNode = node;
    }


    public void setChildNextNode(AbstractNode node){
        node.childrenIdx = childrenIdx+1;
        this.nextNode = node;
    }
}
