package com.xixi.approval.myapproval.enums;

/**
 * @author shengchengchao
 * @Description
 * @createTime 2021/4/26
 */
public enum StatusEnum {
    /**
     *  可以进行审批操作的简单节点是Ready状态
     */
    READY("READY","审批"),
    /**
     *  审批完成
     */
    NORMAL("COMPLETE","审批完成"),
    /**
     * 现在还没有走到的节点状态
     */
    FUTURE("FUTURE","待审批"),
    /**
     * 只有复杂节点有该状态，表示在等待子节点审批
     */
    WAITING("WAITING","子节点待审批"),
    /**
     * 回滚
     */
    ROLLBACK("ROLLBACK","驳回"),
    /**
     *   只有复杂节点有该状态，在并发节点下，一个子节点过了审批，其他字节点为跳过
     */
    SKIP("SKIP","跳过")
    ;

    private String status;
    private String statusCn;

    public String getStatus() {
        return status;
    }

    public String getStatusCn() {
        return statusCn;
    }

    StatusEnum(String status, String statusCn) {
        this.status = status;
        this.statusCn = statusCn;
    }
}
