package com.xixi.approval.myapproval.entity;

import java.io.Serializable;

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
@TableName("APPROVAL_LOG")
public class ApprovalLogEntity extends BaseEntity implements Serializable {

    @TableId(value="ID" ,type = IdType.ID_WORKER_STR)
    @TableField("ID")
    private String id;

    /** 回滚的原因 */
    @TableField("REASON")
    private String reason;

    /** 状态 */
    @TableField("STATUS")
    private String status;

    /** 关联id */
    @TableField("RELATE_ID")
    private String relateId;

    /** 节点索引 */
    @TableField("NODE_INDEX")
    private Integer nodeIndex;

    /** 在多节点状态下,子索引数 */
    @TableField("CHILDREN_IDX")
    private Integer childrenIdx;


}
