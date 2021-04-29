package com.xixi.approval.myapproval.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author shengchengchao
 * @Description
 * @createTime 2021/4/28
 */
@Data
public class ApprovalUserDTO  implements Serializable {

    private String condition;

    private String id;
}
