package com.xixi.approval.myapproval.process;

import com.xixi.approval.myapproval.dto.ApprovalDTO;
import com.xixi.approval.myapproval.exception.ApprovalException;
import com.xixi.approval.myapproval.node.AbstractNode;

/**
 * @author shengchengchao
 * @Description
 * @createTime 2021/4/29
 */
public interface AbstractApprovalProcess  {

    /**
     *  审批
     * @param approvalDTO approvalDTO
     * @param currentNode currentNode
     * @return true
     * @throws ApprovalException ApprovalException
     */
    Boolean approval(ApprovalDTO approvalDTO, AbstractNode currentNode) throws ApprovalException;


    /**
     *  驳回
     * @param approvalDTO approvalDTO
     * @param currentNode currentNode
     * @return true
     * @throws ApprovalException ApprovalException
     */
    Boolean rollback(ApprovalDTO approvalDTO,AbstractNode currentNode) throws ApprovalException;
}
