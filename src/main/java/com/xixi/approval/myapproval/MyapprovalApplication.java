package com.xixi.approval.myapproval;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * @author xixi
 */
@SpringBootApplication
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.xixi.approval.myapproval.*"})
@MapperScan({"com.xixi.approval.myapproval.mapper"})
public class MyapprovalApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyapprovalApplication.class, args);
    }

}
