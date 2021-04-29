package com.xixi.approval.myapproval.process;

import com.google.common.collect.Lists;
import com.xixi.approval.myapproval.dto.ApprovalDTO;
import com.xixi.approval.myapproval.entity.ApprovalConfigEntity;
import com.xixi.approval.myapproval.entity.ApprovalLogEntity;
import com.xixi.approval.myapproval.enums.StatusEnum;
import com.xixi.approval.myapproval.exception.ApprovalException;
import com.xixi.approval.myapproval.node.AbstractNode;
import com.xixi.approval.myapproval.service.ApprovalLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;


/**
 * @author shengchengchao
 * @Description
 * @createTime 2021/4/27
 */
@Component
public class SimpleNodeProcess extends CommonApprovalProcess {

    @Autowired
    private ApprovalLogService approvalLogService;

    /**
     * 得到第一层的节点
     *
     * @param approvalConfigEntity 审批配置
     * @param approvalLogEntity 日志
     * @return 节点
     */
    @Override
    protected AbstractNode getNode(ApprovalConfigEntity approvalConfigEntity, ApprovalLogEntity approvalLogEntity) {
        AbstractNode simpleNode = super.getSimpleNode(approvalConfigEntity, approvalLogEntity);
        return simpleNode;
    }


    @Override
    protected AbstractNode checkNode(AbstractNode currentNode, ApprovalDTO approvalDTO) throws ApprovalException {
       return super.checkNode(currentNode,approvalDTO);
    }


    @Override
    public void recordApprovalLog(AbstractNode currentNode, ApprovalDTO approvalDTO,AbstractNode node) throws ApprovalException {
        List<ApprovalLogEntity> list = new ArrayList<>();
        approvalLogService.lambdaUpdate().eq(ApprovalLogEntity::getRelateId,approvalDTO.getRelated())
                .eq(ApprovalLogEntity::getNodeIndex,currentNode.getNodeIdx()).remove();
        ApprovalLogEntity approvalLogEntity = new ApprovalLogEntity("", StatusEnum.NORMAL.getStatus(),approvalDTO.getRelated()
                ,currentNode.getNodeIdx(),0,approvalDTO.getApprovalUserDTO().getId());
        list.add(approvalLogEntity);
        AbstractNode nextNode = currentNode.getNextNode();
        if(nextNode !=null){
            AbstractNodeProcess abstractNodeProcess = processMap.get(nextNode.getNodeType());
            list.addAll(abstractNodeProcess.saveNextNodeLog(nextNode, approvalDTO));
        }
        approvalLogService.saveBatch(list);
    }

    @Override
    public void recordRollbackLog(AbstractNode currentNode, ApprovalDTO approvalDTO,AbstractNode node) throws ApprovalException  {
        approvalLogService.lambdaUpdate().eq(ApprovalLogEntity::getRelateId,approvalDTO.getRelated())
                .eq(ApprovalLogEntity::getNodeIndex,currentNode.getNodeIdx()).remove();
        ApprovalLogEntity approvalLogEntity = new ApprovalLogEntity(approvalDTO.getRollbackReason(),
                StatusEnum.ROLLBACK.getStatus(),approvalDTO.getRelated(),currentNode.getNodeIdx(),0,approvalDTO.getApprovalUserDTO().getId());

        approvalLogService.save(approvalLogEntity);
    }

    /**
     *  保存下一个节点日志
     * @param currentNode 下一个节点
     * @return 日志集合
     */
    @Override
    public List<ApprovalLogEntity> saveNextNodeLog(AbstractNode currentNode,ApprovalDTO approvalDTO) {
        ApprovalLogEntity next = new ApprovalLogEntity("", StatusEnum.READY.getStatus()
                , approvalDTO.getRelated(), currentNode.getNodeIdx(), 0,"");
        return Lists.newArrayList(next);
    }


}
