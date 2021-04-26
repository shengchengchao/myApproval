package com.xixi.approval.myapproval.process;

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

        if(approval()){
            //创建日志

            updateNode();
        }else {
            //抛出异常 审批失败

        }
    }

    /**
     * 创建日志
     */
    protected void createLog(){

    }

    /**
     * 审批是否通过 true为通过 false为失败，其中可以进行检验
     * @return
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

        if(rollback()){
            //创建日志

            //修改链路，进行其他操作

        }
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
