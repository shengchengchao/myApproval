package com.xixi.approval.myapproval.process;

import com.xixi.approval.myapproval.dto.ApprovalDTO;
import com.xixi.approval.myapproval.exception.ApprovalException;
import com.xixi.approval.myapproval.node.AbstractNode;
import org.springframework.stereotype.Component;



/**
 * @author shengchengchao
 * @Description
 * @createTime 2021/4/28
 */
@Component
public abstract class CommonApprovalProcess extends AbstractNodeProcess  implements AbstractApprovalProcess ,AbstractLogProcess{



    @Override
    public Boolean approval(ApprovalDTO approvalDTO,AbstractNode currentNode) throws ApprovalException {
        //校验 并通过节点
        AbstractNode node = checkNode(currentNode, approvalDTO);
        //记录一份日志 当前节点为通过 下一个节点为审批
        recordApprovalLog(currentNode,approvalDTO,node);
        //自定义的处理 比如对于项目状态的变化
        customizeApprovalChange();
        return true;
    }

    @Override
    public Boolean rollback(ApprovalDTO approvalDTO,AbstractNode currentNode) throws ApprovalException{

        AbstractNode node = checkNode(currentNode, approvalDTO);
        recordRollbackLog(currentNode,approvalDTO,node);
        customizeRollbackChange();
        return true;
    }



    /**
     * 自定义的审批设置 子类实现
     */
    public  void customizeApprovalChange(){

    }

    /**
     * 自定义的回滚设置  子类实现
     */
    public  void customizeRollbackChange(){

    }


}
