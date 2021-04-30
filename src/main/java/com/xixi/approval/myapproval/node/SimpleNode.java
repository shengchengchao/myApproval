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


    public SimpleNode(List<String> applyUser, String type, String status,String nodeType, String name, String reason,String completeUser,Integer childrenIdx,Integer nodeIndex) {
       super(type,status,nodeType,name,reason,completeUser,nodeIndex,childrenIdx);
       this.applyUser=applyUser;
    }

}
