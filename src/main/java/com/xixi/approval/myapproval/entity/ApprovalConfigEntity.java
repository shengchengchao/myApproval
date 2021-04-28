package com.xixi.approval.myapproval.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;


/**
*
 * @author xixi
 */
@Data
@TableName("APPROVAL_CONFIG")
public class ApprovalConfigEntity extends BaseEntity implements Serializable {

    @TableId(value="ID" ,type = IdType.ID_WORKER_STR)
    @TableField("ID")
    private String id;
    /**配置名称*/
    @TableField("NAME")
    private String name;

    /** 节点位置 */
    @TableField("NODE_INDEX")
    private Integer nodeIndex;

    /** 多个条件用,分割 */
    @TableField("APPROVAL_ROLE")
    private String approvalRole;

    /** 节点类型 */
    @TableField("NODE_TYPE")
    private String nodeType;

    /** 版本 */
    @TableField("VERSION")
    private Integer version;



    /** 在多节点状态下,子索引数 */
    @TableField("CHILDREN_IDX")
    private Integer childrenIdx;


}
