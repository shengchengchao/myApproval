package com.xixi.approval.myapproval.process;

import com.xixi.approval.myapproval.entity.ApprovalLogEntity;

import java.util.Date;

/**
 * @author shengchengchao
 * @Description
 * @createTime 2021/4/26
 */
public abstract class AbstractProcess {


    /**
     *  这里 通用的处理就是  写一份日志 修改表中节点顺序
     */
    public void approvalNode(){
        //校验node
        checkNode();
        approval();
        createLog();
        updateNode();

    }

    /**
     * 创建日志
     */
    protected void createLog(){
        ApprovalLogEntity approvalLogEntity = new ApprovalLogEntity();


    }

    /**
     * 审批
     * @return 审批是否通过 true为通过 false为失败，其中可以进行检验
     */
    public abstract Boolean approval();


    /**
     * 空方法 在子类中实现 用于做一些自定义的操作
     */
    public abstract void updateNode();

    /**
     * 驳回节点 这里需要做的就是 将节点回滚 那么就是记录一条日志 同时将节点回滚一位 ，状态设置为驳回
     */
    public  void rollbackNode(){

        checkNode();
        rollback();
        createLog();
        updateNode();
    }

    /**
     * 审批是否通过 true为通过 false为失败，其中可以进行检验
     * @return 判断审批是否通过
     */
    public abstract Boolean rollback();

    /**
     * 校验节点 判断节点能否被处理
     */
    protected abstract void checkNode();
}
