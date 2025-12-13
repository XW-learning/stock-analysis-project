package com.example.stock.config;

import com.example.stock.model.entity.Stock;
import com.example.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 数据加载器 - 应用启动时自动加载测试数据
 * 
 * @author Toom
 * @version 1.0
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final StockRepository stockRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("检查数据库是否需要初始化...");

        // 检查数据库是否为空
        if (stockRepository.count() == 0) {
            log.info("数据库为空，开始加载测试数据...");

            // 创建测试股票 1: Apple Inc.
            Stock apple = Stock.builder()
                    .symbol("AAPL")
                    .name("Apple Inc.")
                    .exchange("NASDAQ")
                    .sector("Technology")
                    .lastUpdate(LocalDateTime.now())
                    .build();

            // 创建测试股票 2: 贵州茅台
            Stock maotai = Stock.builder()
                    .symbol("600519.SS")
                    .name("贵州茅台")
                    .exchange("SSE")
                    .sector("Consumer Goods")
                    .lastUpdate(LocalDateTime.now())
                    .build();

            // 保存到数据库
            stockRepository.save(apple);
            stockRepository.save(maotai);

            log.info("测试数据已加载");
            log.info("- AAPL (Apple Inc.) - NASDAQ");
            log.info("- 600519.SS (贵州茅台) - SSE");
        } else {
            log.info("数据库已有数据，跳过初始化。当前股票数量: {}", stockRepository.count());
        }
    }

}
