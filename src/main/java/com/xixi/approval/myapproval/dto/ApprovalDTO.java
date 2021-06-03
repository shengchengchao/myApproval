package com.xixi.approval.myapproval.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author shengchengchao
 * @Description
 * @createTime 2021/4/28
 */
@Data
public class ApprovalDTO implements Serializable {
    @ApiModelProperty("关联id")
    private String related;
    @ApiModelProperty("审批用户信息")
    private ApprovalUserDTO approvalUserDTO;
    @ApiModelProperty("驳回原因")
    private String rollbackReason;
    @ApiModelProperty("类型")
    private String type;
}
