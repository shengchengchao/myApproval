package com.xixi.approval.myapproval.enums;

/**
 * @author shengchengchao
 * @Description 节点类型枚举
 * @createTime 2021/4/27
 */
public enum  NodeEnum {
    /**简单节点*/
    SIMPLE("SIMPLE","简单节点"),
    /**并行节点*/
    PARALLEL("PARALLEL","并行节点"),
    /**会签节点*/
    COUNTERSIGN("COUNTERSIGN","会签节点"),
    ;

    private String type;
    private String typeName;


    NodeEnum(String type, String typeName) {
        this.type = type;
        this.typeName = typeName;
    }

    public String getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }
}
