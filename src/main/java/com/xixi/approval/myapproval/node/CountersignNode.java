package com.xixi.approval.myapproval.node;

import java.util.List;

/**
 * @author shengchengchao
 * @Description 该节点内只要有一个通过就可以
 * @createTime 2021/4/26
 */
public class CountersignNode extends AbstractNode {

    private List<SimpleNode> node;
    /**
     * 完成数
     */
    private Integer CompleteCount;
}
