package com.xixi.approval.myapproval.process;

import com.xixi.approval.myapproval.entity.ApprovalConfigEntity;
import com.xixi.approval.myapproval.entity.ApprovalLogEntity;
import com.xixi.approval.myapproval.node.AbstractNode;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author shengchengchao
 * @Description
 * @createTime 2021/4/27
 */
@Component
public class SimpleNodeProcess extends AbstractNodeProcess {
    /**
     * 审批
     *
     * @return 审批是否通过 true为通过 false为失败，其中可以进行检验
     */
    @Override
    public Boolean approval() {
        return null;
    }

    /**
     * 空方法 在子类中实现 用于做一些自定义的操作
     */
    @Override
    public void updateNode() {

    }

    /**
     * 审批是否通过 true为通过 false为失败，其中可以进行检验
     *
     * @return 判断审批是否通过
     */
    @Override
    public Boolean rollback() {
        return null;
    }

    /**
     * 校验节点 判断节点能否被处理
     */
    @Override
    protected void checkNode() {

    }

    /**
     * 得到第一层的节点
     *
     * @param approvalConfigEntity
     * @param approvalLogEntity
     * @return
     */
    @Override
    protected AbstractNode getNode(ApprovalConfigEntity approvalConfigEntity, ApprovalLogEntity approvalLogEntity) {
        AbstractNode simpleNode = super.getSimpleNode(approvalConfigEntity, approvalLogEntity);
        return simpleNode;
    }




}
