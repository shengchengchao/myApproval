package com.xixi.approval.myapproval.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


/**
*
 * @author xixi
*/
@Data
@TableName("TEST")
public class TestEntity  extends BaseEntity  implements Serializable {

    @TableId(value="ID" ,type = IdType.ID_WORKER_STR)
    @TableField("ID")
    private String id;


    /** 版本 */
    @TableField("VERSION")
    private Integer version;
}
