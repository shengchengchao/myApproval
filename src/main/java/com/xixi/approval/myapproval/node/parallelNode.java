package com.xixi.approval.myapproval.node;

import lombok.Data;

import java.util.List;

/**
 * @author shengchengchao
 * @Description 并行的节点 该节点的内部节点只要有一个通过
 * @createTime 2021/4/26
 */
@Data
public class parallelNode extends AbstractNode {

    /**
     * 内部节点
     */
    private List<SimpleNode> node;
    /**
     * 完成数
     */
    private Integer CompleteCount;
}