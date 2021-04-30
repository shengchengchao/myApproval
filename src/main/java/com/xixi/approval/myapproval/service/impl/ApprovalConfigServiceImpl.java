package com.xixi.approval.myapproval.service.impl;


import com.xixi.approval.myapproval.entity.ApprovalConfigEntity;
import com.xixi.approval.myapproval.mapper.ApprovalConfigMapper;
import com.xixi.approval.myapproval.service.ApprovalConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author xixi
 */
@Service
public class ApprovalConfigServiceImpl extends ServiceImpl<ApprovalConfigMapper, ApprovalConfigEntity> implements ApprovalConfigService {

    @Autowired
    private ApprovalConfigService approvalConfigService;
    /**
     * 设置审批配置
     *
     * @param list
     */
    @Override
    public void setApprovalConfig(List<ApprovalConfigEntity> list) {
        List<ApprovalConfigEntity> res = new ArrayList<>();
        helper(res,list);
        approvalConfigService.saveBatch(list);
    }

    private void helper(List<ApprovalConfigEntity> res, List<ApprovalConfigEntity> list) {
        res.stream().forEach(each->{
            list.add(each);
            if(!CollectionUtils.isEmpty(each.getList())){
                helper(each.getList(),list);
            }
        });

    }

    /**
     * 审批展示 默认只展示 最新的审批节点的设置
     *
     * @param type
     * @return
     */
    @Override
    public List<ApprovalConfigEntity> approvalConfigList(String type) {
        List<ApprovalConfigEntity> list = approvalConfigService.lambdaQuery().eq(ApprovalConfigEntity::getApprovalType, type)
                .orderByAsc(ApprovalConfigEntity::getNodeIndex,ApprovalConfigEntity::getChildrenIdx).list();
        List<ApprovalConfigEntity> collect = list.stream().filter(x -> x.getChildrenIdx() == 0).sorted(Comparator.comparing(ApprovalConfigEntity::getNodeIndex))
                .map(each->setChildrenList(list,each)).collect(Collectors.toList());
        return collect;
    }

    /**
     * 递归
     * @param nodes
     */
    ApprovalConfigEntity setChildrenList(List<ApprovalConfigEntity> nodes,ApprovalConfigEntity rootNode){
        List<ApprovalConfigEntity> childrenList=new ArrayList<>();
        nodes.forEach(searchDictEntity -> {
            if(rootNode.getNodeIndex().equals(searchDictEntity.getNodeIndex()) && searchDictEntity.getChildrenIdx()!=0){
                childrenList.add(searchDictEntity);
            }
        });
        rootNode.setList(childrenList);
        return rootNode;
    }


}
