package com.xixi.approval.myapproval.chain;

import com.xixi.approval.myapproval.entity.TestEntity;
import com.xixi.approval.myapproval.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author shengchengchao
 * @Description
 * @createTime 2021/4/28
 */
@Component
public class MyApprovalChain extends AbstractApprovalChain {
    @Autowired
    private TestService testService;


    /**
     * 得到审批版本
     * @param relateId 根据关联的id
     * @return 版本
     */
    @Override
    public Integer getVersion(String relateId) {
        TestEntity one = testService.lambdaQuery().eq(TestEntity::getId, relateId).one();

        return one.getVersion();
    }

    /**
     * 加载节点处理类
     */
    @Override
    public void init() {
        super.init();
    }



}
