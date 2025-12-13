package com.example.stock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Web 股票分析平台 - 主启动类
 * 
 * @author Toom
 * @version 1.0
 */
@SpringBootApplication
@EnableCaching
@EnableJpaAuditing
public class StockApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockApplication.class, args);
        System.out.println("""
                
                ========================================
                   Web Stock Analysis Platform
                   启动成功！
                   Swagger UI: http://localhost:8080/swagger-ui.html
                ========================================
                """);
    }

}
