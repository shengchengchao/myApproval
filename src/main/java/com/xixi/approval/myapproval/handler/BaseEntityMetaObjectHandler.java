package com.xixi.approval.myapproval.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author shengchengchao
 * @Description
 * @createTime 2021/4/26
 */
@Component
public class BaseEntityMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        //只有属性没有值的时候才进行填充
        if (this.getFieldValByName("createTime",metaObject) == null) {
            this.setInsertFieldValByName("createTime", new Date(), metaObject);
        }
        if (this.getFieldValByName("updateTime",metaObject) == null) {
            this.setInsertFieldValByName("updateTime", new Date(), metaObject);
        }
        if (this.getFieldValByName("delFlag",metaObject) == null) {
            this.setInsertFieldValByName("delFlag", 0L, metaObject);
        }


    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setInsertFieldValByName("updateTime", new Date(), metaObject);
    }



}
