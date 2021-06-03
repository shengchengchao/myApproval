package com.xixi.approval.myapproval.service.impl;


import com.xixi.approval.myapproval.entity.TestEntity;
import com.xixi.approval.myapproval.mapper.TestMapper;
import com.xixi.approval.myapproval.service.TestService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author xixi
 */
@Service
public class TestServiceImpl extends ServiceImpl<TestMapper, TestEntity> implements TestService {



}
