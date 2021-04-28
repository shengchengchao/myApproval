package com.xixi.approval.myapproval.process;

import com.xixi.approval.myapproval.entity.ApprovalConfigEntity;
import com.xixi.approval.myapproval.entity.ApprovalLogEntity;
import com.xixi.approval.myapproval.enums.StatusEnum;
import com.xixi.approval.myapproval.node.AbstractNode;
import com.xixi.approval.myapproval.node.CountersignNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author shengchengchao
 * @Description
 * @createTime 2021/4/27
 */
@Component
public class CountersignNodeProcess extends AbstractNodeProcess {
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
     * @param approvalConfigEntity 配置
     * @param approvalLogEntity 日志
     * @return AbstractNode 会签节点
     */
    @Override
    protected AbstractNode getNode(ApprovalConfigEntity approvalConfigEntity, ApprovalLogEntity approvalLogEntity) {
        String[] split = StringUtils.split(approvalConfigEntity.getApprovalRole(), ",");
        CountersignNode countersignNode = new CountersignNode(Arrays.asList(split), approvalConfigEntity.getNodeType(),
                Optional.ofNullable(approvalLogEntity.getStatus()).orElse(StatusEnum.FUTURE.getStatus()),
                approvalLogEntity.getRelateId(), approvalConfigEntity.getNodeType(), approvalConfigEntity.getName(),
                Optional.ofNullable(approvalLogEntity.getReason()).orElse(""));
        return countersignNode;
    }

    /**
     * 装配孩子节点
     * @param childHead 孩子的头节点
     * @param configList 配置的节点
     * @param logList 日志节点
     * @param index 节点
     * @param node 父节点
     */
    @Override
    protected  void packageChildren(AbstractNode childHead, List<ApprovalConfigEntity> configList, List<ApprovalLogEntity> logList, AbstractNode node) {
        AbstractNode cur =childHead;
        for (int i=0;i<configList.size();i++){
            ApprovalConfigEntity approvalConfigEntity = configList.get(i);
            Optional<ApprovalLogEntity> first = logList.stream().filter(x -> x.getChildrenIdx().equals(approvalConfigEntity.getChildrenIdx())).findFirst();

            cur.setNextNode(super.getSimpleNode(configList.get(i), first.orElseGet(ApprovalLogEntity::new)));
            cur = cur.getNextNode();
        }
        ((CountersignNode)node).setNode(childHead.getNextNode());
    }


}
