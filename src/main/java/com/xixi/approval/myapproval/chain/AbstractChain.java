package com.xixi.approval.myapproval.chain;

import com.xixi.approval.myapproval.node.AbstractNode;

import java.util.List;

/**
 * @author shengchengchao
 * @Description
 * @createTime 2021/4/26
 */
public abstract class AbstractChain {

    /**
     *  返回一个审批链 包含当前处理到的节点
     * @return 得到审批链
     */
    public List<AbstractNode> approvalChain(){
        //得到 配置的节点


        //查询日志 判断处理到了哪一步

        return null;
    }



}
