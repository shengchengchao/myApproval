package com.xixi.approval.myapproval.process;

import com.xixi.approval.myapproval.dto.ApprovalDTO;
import com.xixi.approval.myapproval.entity.ApprovalLogEntity;
import com.xixi.approval.myapproval.exception.ApprovalException;
import com.xixi.approval.myapproval.node.AbstractNode;

import java.util.List;

/**
 * @author shengchengchao
 * @Description
 * @createTime 2021/4/28
 */
public interface AbstractLogProcess   {




    /**
     * 记录审批日志
     * @param currentNode 节点
     * @param approvalDTO approvalDTO
     * @throws ApprovalException 异常
     */
    abstract void recordApprovalLog(AbstractNode currentNode,ApprovalDTO approvalDTO,AbstractNode cur) throws ApprovalException;

    /**
     * 记录回滚日志
     * @param currentNode 节点
     * @param approvalDTO approvalDTO
     * @throws ApprovalException 异常
     */
    abstract void recordRollbackLog(AbstractNode fatherNode,ApprovalDTO approvalDTO,AbstractNode cur) throws ApprovalException;

    /**
     *  保存下一个节点日志
     * @param currentNode 下一个节点
     * @param approvalDTO approvalDTO
     * @return 日志集合
     */
    abstract List<ApprovalLogEntity> saveNextNodeLog(AbstractNode currentNode, ApprovalDTO approvalDTO);
}
