package com.xixi.approval.myapproval.enums;

/**
 * @author shengchengchao
 * @Description
 * @createTime 2021/6/3
 */
public enum OperateEnum {

    /**简单节点*/
    APPROVAL("APPROVAL","审批"),
    /**并行节点*/
    ROLLBACK("ROLLBACK","回滚"),;

    private String operate;
    private String operateName;


    OperateEnum(String operate, String operateName) {
        this.operate = operate;
        this.operateName = operateName;
    }



    public String getOperate() {
        return operate;
    }

    public String getOperateName() {
        return operateName;
    }
}
