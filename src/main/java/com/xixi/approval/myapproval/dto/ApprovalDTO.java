package com.xixi.approval.myapproval.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author shengchengchao
 * @Description
 * @createTime 2021/4/28
 */
@Data
public class ApprovalDTO implements Serializable {

    private String related;

    private ApprovalUserDTO approvalUserDTO;

    private String rollbackReason;

    private String type;
}
