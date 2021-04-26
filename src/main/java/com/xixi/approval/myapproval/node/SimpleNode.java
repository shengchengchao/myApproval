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
    private List<String> applyUser;
}
