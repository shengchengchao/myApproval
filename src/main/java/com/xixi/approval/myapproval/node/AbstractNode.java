package com.xixi.approval.myapproval.node;

import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author shengchengchao
 * @Description 基础的节点
 * @createTime 2021/4/26
 */
@Data
public  abstract class AbstractNode {

    /**
     * 审批类型
     */
    private String type;

    /**
     * 状态
     */
    private String status;

    /**
     * 关联id
     */
    private String relateId;
    /**
     * 节点类型
     */
    private String nodeType;

    /**
     * 节点顺序
     */
    private Integer nodeIdx;

    /**
     * 原因
     */
    private String reason;



}
