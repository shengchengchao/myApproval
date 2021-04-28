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
     * 关联id
     */
    protected String relateId;
    /**
     * 节点类型
     */
    protected String nodeType;
    protected String name;

    /**
     * 节点顺序
     */
    protected Integer nodeIdx;

    /**
     * 原因
     */
    protected String reason;

    protected AbstractNode nextNode;


    public AbstractNode(String type, String status, String relateId, String nodeType, String name, String reason) {
        this.type = type;
        this.status = status;
        this.relateId = relateId;
        this.nodeType = nodeType;
        this.name = name;
        this.reason = reason;
    }

    public AbstractNode() {
    }

    public void setNextNode(AbstractNode node){
        node.nodeIdx = nodeIdx+1;
        this.nextNode = node;
    }


}
