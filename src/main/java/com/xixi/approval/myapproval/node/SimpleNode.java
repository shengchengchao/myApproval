package com.xixi.approval.myapproval.node;

import lombok.Data;

import java.util.List;

/**
 * @author shengchengchao
 * @Description
 * @createTime 2021/4/26
 */
@Data
public class SimpleNode extends AbstractNode {

    /**
     * 审批人名称
     */
    protected List<String> applyUser;


    public SimpleNode(List<String> applyUser, String type, String status, String relateId, String nodeType, String name, String reason) {
       super(type,status,relateId,nodeType,name,reason);
       this.applyUser=applyUser;
    }

}
