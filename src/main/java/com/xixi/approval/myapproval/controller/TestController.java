package com.xixi.approval.myapproval.controller;

import com.xixi.approval.myapproval.chain.MyApprovalChain;
import com.xixi.approval.myapproval.dto.ApprovalDTO;
import com.xixi.approval.myapproval.exception.ApprovalException;
import com.xixi.approval.myapproval.node.AbstractNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
     * @Description: 1
     * @Author: shengchengchao
     * @Date: 2020/4/8
     */
    @ApiOperation(value = "测试")
    @PostMapping("/testinit")
    public void testInit(){
        AbstractNode abstractNode = approvalChain.approvalChain("3");
        System.out.println(abstractNode.getNodeIdx());
    }

    /**
     * @Description: 1
     * @Author: shengchengchao
     * @Date: 2020/4/8
     */
    @ApiOperation(value = "测试2")
    @PostMapping("/testa")
    public void testInit(@RequestBody ApprovalDTO approvalDTO){
        try {
            approvalChain.approval(approvalDTO);
        } catch (ApprovalException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description: 1
     * @Author: shengchengchao
     * @Date: 2020/4/8
     */
    @ApiOperation(value = "测试3")
    @PostMapping("/testr")
    public void testroll(@RequestBody ApprovalDTO approvalDTO){
        try {
            approvalChain.rollback(approvalDTO);
        } catch (ApprovalException e) {
            e.printStackTrace();
        }
    }
}
