package com.xixi.approval.myapproval.controller;

import com.xixi.approval.myapproval.chain.MyApprovalChain;
import com.xixi.approval.myapproval.node.AbstractNode;
import com.xixi.approval.myapproval.service.TestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shengchengchao
 * @Description
 * @createTime 2021/4/26
 */
@Api(tags="测试")
@RestController
@RequestMapping("/test")
public class TestController {


    @Autowired
    private MyApprovalChain approvalChain;
    /**
     * @Description: 字典项启用禁用
     * @Author: shengchengchao
     * @Date: 2020/4/8
     */
    @ApiOperation(value = "测试")
    @PostMapping("/testinit")
    public void testInit(){
        AbstractNode abstractNode = approvalChain.approvalChain("2");
        System.out.println(abstractNode.getNodeIdx());
    }
}
