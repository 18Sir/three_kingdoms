package com.three_kingdoms.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MPConfig {
    @Bean
    public MybatisPlusInterceptor mpInterceptor(){
        //创建mp的拦截器
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //添加分页的内置拦截器
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }
}
