package com.xixi.approval.myapproval.chain;

import com.xixi.approval.myapproval.dto.ApprovalDTO;
import com.xixi.approval.myapproval.entity.ApprovalConfigEntity;
import com.xixi.approval.myapproval.entity.ApprovalLogEntity;
import com.xixi.approval.myapproval.enums.StatusEnum;
import com.xixi.approval.myapproval.exception.ApprovalException;
import com.xixi.approval.myapproval.node.AbstractNode;
import com.xixi.approval.myapproval.process.*;
import com.xixi.approval.myapproval.service.ApprovalConfigService;
import com.xixi.approval.myapproval.service.ApprovalLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author shengchengchao
 * @Description
 * @createTime 2021/4/26
 */
public abstract class AbstractApprovalChain  extends AbstractProcess {

    @Autowired
    private ApprovalConfigService approvalConfigService;
    @Autowired
    private ApprovalLogService approvalLogService;




    /**
     *  返回一个审批链 包含当前处理到的节点
     * @return 得到审批链
     */
    public AbstractNode approvalChain(String relateId){
        Integer version = getVersion(relateId);
        //得到 配置的节点
        List<ApprovalConfigEntity> config = getConfig(version);
        //查询日志 判断处理到了哪一步
        List<ApprovalLogEntity> log = getLog(relateId);

        return calculateChain(config, log);
    }

    /**
     * 计算出审批链
     * @param config 配置的节点
     * @param log 日志
     * @return AbstractNode 节点链
     */
    protected  AbstractNode calculateChain(List<ApprovalConfigEntity> config, List<ApprovalLogEntity> log){
        AbstractNode defaultHeadNode = getDefaultHeadNode();
        AbstractNode cur = defaultHeadNode;
        addBeforeNode(cur);
        //进行转换
        Map<ApprovalConfigEntity, List<ApprovalConfigEntity>> configMap = config.stream()
                .collect(Collectors.groupingBy(ApprovalConfigEntity::getNodeIndex, Collectors.toList()))
                .entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getValue().get(0), Map.Entry::getValue));
        //二次排序
        Map<ApprovalConfigEntity, List<ApprovalConfigEntity>> sortMap = new TreeMap<>((o1, o2) -> {
            if (o1.getNodeIndex().equals(o2.getNodeIndex())) {
                return o1.getChildrenIdx() - o2.getChildrenIdx();
            } else {
                return o1.getNodeIndex() - o2.getNodeIndex();
            }
        });
        sortMap.putAll(configMap);

        //装配节点
        for (Map.Entry<ApprovalConfigEntity, List<ApprovalConfigEntity>> entry : sortMap.entrySet()) {
            ApprovalConfigEntity key = entry.getKey();
            List<ApprovalLogEntity> collect = log.stream().filter(x -> x.getNodeIndex().equals(key.getNodeIndex())).sorted(Comparator.comparing(ApprovalLogEntity::getChildrenIdx)).collect(Collectors.toList());
            AbstractNodeProcess process = processMap.get(key.getNodeType());
            AbstractNode node = process.getNode(entry.getValue(), collect);
            cur.setNextNode(node);
            cur = cur.getNextNode();
        }

        addAfterNode(cur);

        return defaultHeadNode.getNextNode();
    }


    /**
     * 添加前置节点 由子类自定义实现
     * @param cur 当前节点
     */
    protected  void addBeforeNode(AbstractNode cur){ }

    /**
     * 后置节点 由子类自定义实现
     * @param cur 当前节点
     */
    protected  void addAfterNode(AbstractNode cur){ }

    /**
     * 创建一个默认的头节点
     * @return AbstractNode 头节点
     */
    protected  AbstractNode getDefaultHeadNode(){
        return simpleNodeProcess.createDefaultNode();
    }

    /**
     * 得到配置的节点
     * @param version 版本
     * @return 配置集合
     */
    public List<ApprovalConfigEntity> getConfig(Integer version){
        return approvalConfigService.lambdaQuery().eq(ApprovalConfigEntity::getVersion, version).list();
    }

    /**
     * 得到日志的节点
     * @param relateId 关联id
     * @return 日志集合
     */
    public List<ApprovalLogEntity> getLog(String relateId){
        return approvalLogService.lambdaQuery().eq(ApprovalLogEntity::getRelateId, relateId).list();
    }

    /**
     * 得到审批版本
     * @param relateId 根据关联的id
     * @return 版本
     */
    public abstract Integer getVersion(String relateId);

    /**
     * 得到当前节点 注意这里只得到父节点 不对子节点进去判断
     * @param relateId 关联id
     * @return 当前节点
     */
    public AbstractNode getCurrentNode(String relateId){
        AbstractNode abstractNode = approvalChain(relateId);
        ApprovalLogEntity currentIndex = getCurrentIndex(relateId);
        AbstractNode cur =abstractNode;
        while (cur!=null){
            if(cur.getNodeIdx().equals(currentIndex.getNodeIndex())){
                return cur;
            }
            cur = cur.getNextNode();
        }
        return abstractNode;
    }

    /**
     * 从日志中得到当前节点
     * @param relateId 关联id
     * @return 最新日志
     */
    protected  ApprovalLogEntity getCurrentIndex(String relateId){
        List<ApprovalLogEntity> list = approvalLogService.lambdaQuery().eq(ApprovalLogEntity::getRelateId, relateId)
                .and(i->i.eq(ApprovalLogEntity::getStatus, StatusEnum.READY.getStatus()).or().eq(ApprovalLogEntity::getStatus, StatusEnum.ROLLBACK.getStatus()))
                .orderByDesc(ApprovalLogEntity::getNodeIndex, ApprovalLogEntity::getChildrenIdx).list();
        return CollectionUtils.isEmpty(list) ? new ApprovalLogEntity():list.get(0);
    }

    public Boolean approval(ApprovalDTO approvalDTO) throws ApprovalException {
        AbstractNode abstractNode = getCurrentNode(approvalDTO.getRelated());
        CommonApprovalProcess commonApprovalProcess = processMap.get(abstractNode.getNodeType());
        return commonApprovalProcess.approval(approvalDTO,abstractNode);
    }


    public Boolean rollback(ApprovalDTO approvalDTO) throws ApprovalException {
        AbstractNode abstractNode = getCurrentNode(approvalDTO.getRelated());
        CommonApprovalProcess commonApprovalProcess = processMap.get(abstractNode.getNodeType());
        return commonApprovalProcess.rollback(approvalDTO,abstractNode);
    }
}
