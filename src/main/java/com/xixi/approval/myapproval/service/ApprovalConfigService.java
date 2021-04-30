package com.xixi.approval.myapproval.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.xixi.approval.myapproval.entity.ApprovalConfigEntity;

import java.util.List;

/**
 * @author xixi
 */
public interface ApprovalConfigService extends IService<ApprovalConfigEntity> {

    /**
     * 设置审批配置 注意 这里存在一个问题就是 如果当前审批中存在没有完成的审批的话 依旧会使用旧版本去审批
     * @param list
     */
    void setApprovalConfig(List<ApprovalConfigEntity> list);

    /**
     * 审批展示 默认只展示 最新的审批节点的设置
     * @param type
     * @return
     */
    List<ApprovalConfigEntity> approvalConfigList(String type);
}
