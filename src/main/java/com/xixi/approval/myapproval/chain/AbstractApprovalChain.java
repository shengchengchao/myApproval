package com.xixi.approval.myapproval.chain;

import cn.hutool.core.collection.CollectionUtil;
import com.xixi.approval.myapproval.entity.ApprovalConfigEntity;
import com.xixi.approval.myapproval.entity.ApprovalLogEntity;
import com.xixi.approval.myapproval.enums.NodeEnum;
import com.xixi.approval.myapproval.node.AbstractNode;
import com.xixi.approval.myapproval.process.AbstractNodeProcess;
import com.xixi.approval.myapproval.process.CountersignNodeProcess;
import com.xixi.approval.myapproval.process.ParallelNodeProcess;
import com.xixi.approval.myapproval.process.SimpleNodeProcess;
import com.xixi.approval.myapproval.service.ApprovalConfigService;
import com.xixi.approval.myapproval.service.ApprovalLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author shengchengchao
 * @Description
 * @createTime 2021/4/26
 */
public abstract class AbstractApprovalChain {

    @Autowired
    private ApprovalConfigService approvalConfigService;
    @Autowired
    private ApprovalLogService approvalLogService;

    @Autowired
    private SimpleNodeProcess simpleNodeProcess;
    @Autowired
    private ParallelNodeProcess parallelNodeProcess;
    @Autowired
    private CountersignNodeProcess countersignNodeProcess;

    Map<String, AbstractNodeProcess> processMap;

    /**
     * 初始化 策略模式
     */
    @PostConstruct
    public void init(){
        processMap = new HashMap<>(16);
        processMap.put(NodeEnum.SIMPLE.getType(),simpleNodeProcess);
        processMap.put(NodeEnum.PARALLEL.getType(),parallelNodeProcess);
        processMap.put(NodeEnum.COUNTERSIGN.getType(),countersignNodeProcess);
    }

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

        AbstractNode abstractNodes = calculateChain(config, log);

        return abstractNodes;
    }

    /**
     * 计算出审批链
     * @param config 配置的节点
     * @param log 日志
     * @return
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
            List<ApprovalLogEntity> collect = log.stream().filter(x -> x.getNodeIndex().equals(key.getNodeIndex())).collect(Collectors.toList());
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
     * @param cur
     */
    protected  void addBeforeNode(AbstractNode cur){ }

    /**
     * 后置节点 由子类自定义实现
     * @param cur
     */
    protected  void addAfterNode(AbstractNode cur){ }

    /**
     * 创建一个默认的头节点
     * @return
     */
    protected  AbstractNode getDefaultHeadNode(){
        return simpleNodeProcess.createDefaultNode();
    }

    /**
     * 得到配置的节点
     * @param version
     * @return
     */
    public List<ApprovalConfigEntity> getConfig(Integer version){
        return approvalConfigService.lambdaQuery()
                .eq(ApprovalConfigEntity::getVersion, version).list();
    }

    /**
     * 得到日志的节点
     * @param relateId
     * @return
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


}
