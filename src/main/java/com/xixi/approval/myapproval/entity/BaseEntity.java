package com.xixi.approval.myapproval.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author shengchengchao
 * @Description
 * @createTime 2021/4/26
 */
@Data
public class BaseEntity extends Model implements Serializable {

    /**
     * 删除标志位，已删除
     */
    public static final Integer DEL_FLAG_TRUE = 1;
    /**
     * 删除标志位，未删除
     */
    public static final Integer DEL_FLAG_FALSE = 0;

    @TableField(value="CREATE_TIME",fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @TableField(value = "UPDATE_TIME",fill = FieldFill.INSERT_UPDATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @TableLogic
    @TableField(value="DEL_FLAG",fill = FieldFill.INSERT)
    private Long delFlag;

}
