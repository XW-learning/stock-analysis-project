package com.example.stock.service;

import com.example.stock.exception.StockNotFoundException;
import com.example.stock.model.dto.StockDetailDTO;
import com.example.stock.model.dto.StockSearchDTO;
import com.example.stock.model.entity.Stock;
import com.example.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 股票业务逻辑服务
 * 
 * @author Toom
 * @version 1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    /**
     * 搜索股票
     * 根据关键字模糊匹配股票代码或名称
     * 
     * @param keyword 搜索关键字
     * @return 股票搜索结果列表
     */
    @Transactional(readOnly = true)
    public List<StockSearchDTO> search(String keyword) {
        log.info("搜索股票，关键字: {}", keyword);

        if (keyword == null || keyword.trim().isEmpty()) {
            log.warn("搜索关键字为空");
            return List.of();
        }

        List<Stock> stocks = stockRepository.searchBySymbolOrName(keyword.trim());
        log.info("搜索结果数量: {}", stocks.size());

        return stocks.stream()
                .map(this::convertToSearchDTO)
                .collect(Collectors.toList());
    }

    /**
     * 获取股票详情
     * 
     * @param symbol 股票代码
     * @return 股票详情
     * @throws StockNotFoundException 股票不存在时抛出
     */
    @Transactional(readOnly = true)
    public StockDetailDTO getStockDetails(String symbol) {
        log.info("查询股票详情，代码: {}", symbol);

        if (symbol == null || symbol.trim().isEmpty()) {
            throw new IllegalArgumentException("股票代码不能为空");
        }

        Stock stock = stockRepository.findBySymbolIgnoreCase(symbol.trim())
                .orElseThrow(() -> {
                    log.error("股票不存在: {}", symbol);
                    return new StockNotFoundException(symbol);
                });

        log.info("成功查询到股票: {} - {}", stock.getSymbol(), stock.getName());
        return convertToDetailDTO(stock);
    }

    /**
     * 检查股票是否存在
     * 
     * @param symbol 股票代码
     * @return 是否存在
     */
    @Transactional(readOnly = true)
    public boolean exists(String symbol) {
        return stockRepository.existsBySymbolIgnoreCase(symbol);
    }

    /**
     * 根据行业查询股票列表
     * 
     * @param sector 行业名称
     * @return 股票列表
     */
    @Transactional(readOnly = true)
    public List<StockSearchDTO> findBySector(String sector) {
        log.info("查询行业股票，行业: {}", sector);
        
        List<Stock> stocks = stockRepository.findBySector(sector);
        
        return stocks.stream()
                .map(this::convertToSearchDTO)
                .collect(Collectors.toList());
    }

    /**
     * 转换为搜索结果 DTO
     */
    private StockSearchDTO convertToSearchDTO(Stock stock) {
        return StockSearchDTO.builder()
                .symbol(stock.getSymbol())
                .name(stock.getName())
                .exchange(stock.getExchange())
                .build();
    }

    /**
     * 转换为详情 DTO
     */
    private StockDetailDTO convertToDetailDTO(Stock stock) {
        return StockDetailDTO.builder()
                .symbol(stock.getSymbol())
                .name(stock.getName())
                .exchange(stock.getExchange())
                .sector(stock.getSector())
                .lastUpdate(stock.getLastUpdate())
                .createdAt(stock.getCreatedAt())
                .build();
    }

}
