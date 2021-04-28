package com.xixi.approval.myapproval.node;

import lombok.Data;

import java.util.List;

/**
 * @author shengchengchao
 * @Description 并行的节点 该节点的内部节点只要有一个通过
 * @createTime 2021/4/26
 */
@Data
public class ParallelNode extends SimpleNode {

    /**
     * 内部节点
     */
    public AbstractNode node;
    /**
     * 完成数
     */
    public Integer completeCount;

    public ParallelNode(List<String> applyUser, String type, String status, String relateId, String nodeType, String name, String reason) {
        super(applyUser, type, status, relateId, nodeType, name, reason);
        this.completeCount=0;
    }
}
