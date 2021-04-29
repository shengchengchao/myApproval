package com.xixi.approval.myapproval.node;

import lombok.Data;

import java.util.List;

/**
 * @author shengchengchao
 * @Description 该节点内只要有一个通过就可以
 * @createTime 2021/4/26
 */
@Data
public class CountersignNode extends SimpleNode {

    public AbstractNode node;



    public CountersignNode(List<String> applyUser, String type, String status,String nodeType, String name, String reason,String completeUserId
            ,Integer childrenIdx,Integer nodeIndex) {
        super(applyUser,type,status,nodeType,name,reason,completeUserId,childrenIdx,nodeIndex);

    }

}
