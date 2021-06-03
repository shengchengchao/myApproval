package com.xixi.approval.myapproval;

import com.xixi.approval.myapproval.chain.MyApprovalChain;
import com.xixi.approval.myapproval.dto.ApprovalDTO;
import com.xixi.approval.myapproval.dto.ApprovalUserDTO;
import com.xixi.approval.myapproval.entity.ApprovalConfigEntity;
import com.xixi.approval.myapproval.entity.TestEntity;
import com.xixi.approval.myapproval.enums.NodeEnum;
import com.xixi.approval.myapproval.enums.OperateEnum;
import com.xixi.approval.myapproval.enums.StatusEnum;
import com.xixi.approval.myapproval.exception.ApprovalException;
import com.xixi.approval.myapproval.node.AbstractNode;
import com.xixi.approval.myapproval.node.CountersignNode;
import com.xixi.approval.myapproval.node.ParallelNode;
import com.xixi.approval.myapproval.service.ApprovalConfigService;
import com.xixi.approval.myapproval.service.TestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author shengchengchao
 * @Description
 * @createTime 2021/6/3
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyapprovalApplication.class)
public class MyapprpvalTest {

    @Autowired
    private ApprovalConfigService approvalConfigService;
    @Autowired
    private TestService testService;
    @Autowired
    private MyApprovalChain approvalChain;
    
    Integer version =1;
    String type  = "TEST";

    /**
     * 创建审批节点设置
     * @param version
     * @param type
     */
    @Test
    public void  setApprovalConfig(){
        List<ApprovalConfigEntity> list = new ArrayList<>();
        for (int i=1;i<5;i++){
            if(i==1 || i==4){
                list.add(createSimpleNode(i,version,type,NodeEnum.SIMPLE.getTypeName(),0));
            }else if (i==2){
                list.add(createParallelNode(i,version,type));
            }else {
                list.add(createCountersignNode(i,version,type));
            }
        }
        approvalConfigService.setApprovalConfig(list);
    }

    /**
     *   创建审批
     */
    @Test
    public void createApprovals(){
        TestEntity testEntity = new TestEntity();
        testEntity.setVersion(version);
        testService.save(testEntity);
        AbstractNode abstractNode = approvalChain.approvalChain(testEntity.getId());
        AbstractNode node = abstractNode;
        assertThat(node.getName()).isEqualTo("简单节点1");
        node = node.getNextNode();
        assertThat(node.getName()).isEqualTo("并行节点2");
        assertThat(((ParallelNode) node).getNode()).isNotNull();
        node = node.getNextNode();
        assertThat(((CountersignNode) node).getNode()).isNotNull();
        assertThat(((CountersignNode) node).getNode().getChildrenIdx()).isEqualTo(1);
        assertThat(((CountersignNode) node).getNode().getNextNode()).isNotNull();
        node = node.getNextNode();
        assertThat(node.getNodeType()).isEqualTo("SIMPLE");
    }





    @Test
    public void currentNode(){
        AbstractNode currentNode = approvalChain.getCurrentNode("1400326214150234114");
        currentNode.getNextNode();
    }


    /**
     * 创建简单节点
     * @param i  节点位置
     * @param version  版本
     * @return   approvalConfigEntity  节点类
     */
    public ApprovalConfigEntity createSimpleNode(Integer i,Integer version,String type,String name,Integer childrenIdx){
        ApprovalConfigEntity approvalConfigEntity = new ApprovalConfigEntity();
        approvalConfigEntity.setNodeType(NodeEnum.SIMPLE.getType());
        approvalConfigEntity.setChildrenIdx(childrenIdx);
        approvalConfigEntity.setApprovalRole(i.toString());
        approvalConfigEntity.setName(name+i);
        approvalConfigEntity.setVersion(version);
        approvalConfigEntity.setApprovalType(type);
        approvalConfigEntity.setNodeIndex(i);
        return approvalConfigEntity;
    }

    /**
     * 创建并行节点
     * @param i
     * @param version
     * @return
     */
    public ApprovalConfigEntity createParallelNode(Integer i,Integer version,String type){
        ApprovalConfigEntity approvalConfigEntity = new ApprovalConfigEntity();
        approvalConfigEntity.setNodeType(NodeEnum.PARALLEL.getType());
        approvalConfigEntity.setChildrenIdx(0);
        approvalConfigEntity.setApprovalRole(i.toString());
        approvalConfigEntity.setName(NodeEnum.PARALLEL.getTypeName()+i);
        approvalConfigEntity.setVersion(version);
        approvalConfigEntity.setNodeIndex(i);
        approvalConfigEntity.setApprovalType(type);
        List<ApprovalConfigEntity> list = new ArrayList<>();
        for (int j = 1;j<i;j++){
             list.add(createSimpleNode(i,version,type,NodeEnum.PARALLEL.getTypeName(),j));
        }
        approvalConfigEntity.setList(list);
        return approvalConfigEntity;
    }


    /**
     * 创建并发节点
     * @param i
     * @param version
     * @return
     */
    public ApprovalConfigEntity createCountersignNode(Integer i,Integer version,String type){
        ApprovalConfigEntity approvalConfigEntity = new ApprovalConfigEntity();
        approvalConfigEntity.setNodeType(NodeEnum.COUNTERSIGN.getType());
        approvalConfigEntity.setChildrenIdx(0);
        approvalConfigEntity.setApprovalRole(i.toString());
        approvalConfigEntity.setName(NodeEnum.COUNTERSIGN.getTypeName()+i);
        approvalConfigEntity.setVersion(version);
        approvalConfigEntity.setNodeIndex(i);
        approvalConfigEntity.setApprovalType(type);
        List<ApprovalConfigEntity> list = new ArrayList<>();
        for (int j = 1;j<i;j++){
            list.add(createSimpleNode(i,version,type,NodeEnum.COUNTERSIGN.getTypeName(),j));
        }
        approvalConfigEntity.setList(list);
        return approvalConfigEntity;
    }
}
