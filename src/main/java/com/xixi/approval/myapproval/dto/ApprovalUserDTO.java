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
public class ApprovalUserDTO  implements Serializable {
    @ApiModelProperty("审批条件")
    private String condition;
    @ApiModelProperty("用户id")
    private String id;
}
