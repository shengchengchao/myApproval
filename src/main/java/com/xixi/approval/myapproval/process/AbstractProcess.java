package com.xixi.approval.myapproval.process;


import com.xixi.approval.myapproval.enums.NodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shengchengchao
 * @Description
 * @createTime 2021/4/28
 */
@Component
public abstract class AbstractProcess {


    @Autowired
    public SimpleNodeProcess simpleNodeProcess;
    @Autowired
    public ParallelNodeProcess parallelNodeProcess;
    @Autowired
    public CountersignNodeProcess countersignNodeProcess;

    public Map<String, CommonApprovalProcess> processMap;

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



}
