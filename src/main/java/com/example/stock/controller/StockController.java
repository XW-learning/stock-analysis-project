package com.example.stock.controller;

import com.example.stock.exception.StockNotFoundException;
import com.example.stock.model.dto.StockDetailDTO;
import com.example.stock.model.dto.StockSearchDTO;
import com.example.stock.model.vo.Result;
import com.example.stock.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 股票 API 控制器
 * 
 * @author Toom
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "股票管理", description = "股票搜索与详情查询 API")
public class StockController {

    private final StockService stockService;

    /**
     * 搜索股票
     * 
     * @param q 搜索关键字（股票代码或名称）
     * @return 股票搜索结果列表
     */
    @GetMapping("/search")
    @Operation(summary = "搜索股票", description = "根据股票代码或名称模糊搜索股票")
    public Result<List<StockSearchDTO>> search(
            @Parameter(description = "搜索关键字", example = "AAPL")
            @RequestParam("q") String q) {
        
        log.info("接收到搜索请求，关键字: {}", q);

        try {
            List<StockSearchDTO> results = stockService.search(q);
            
            if (results.isEmpty()) {
                return Result.success("未找到匹配的股票", results);
            }
            
            return Result.success(results);
        } catch (Exception e) {
            log.error("搜索股票失败: {}", e.getMessage(), e);
            return Result.systemError("搜索失败: " + e.getMessage());
        }
    }

    /**
     * 获取股票详情
     * 
     * @param symbol 股票代码
     * @return 股票详情
     */
    @GetMapping("/stocks/{symbol}")
    @Operation(summary = "获取股票详情", description = "根据股票代码查询详细信息")
    public Result<StockDetailDTO> getStockDetails(
            @Parameter(description = "股票代码", example = "AAPL")
            @PathVariable("symbol") String symbol) {
        
        log.info("接收到股票详情请求，代码: {}", symbol);

        try {
            StockDetailDTO details = stockService.getStockDetails(symbol);
            return Result.success(details);
        } catch (StockNotFoundException e) {
            log.warn("股票不存在: {}", symbol);
            return Result.stockNotFound(symbol);
        } catch (IllegalArgumentException e) {
            log.warn("参数错误: {}", e.getMessage());
            return Result.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("获取股票详情失败: {}", e.getMessage(), e);
            return Result.systemError("获取股票详情失败: " + e.getMessage());
        }
    }

    /**
     * 检查股票是否存在
     * 
     * @param symbol 股票代码
     * @return 是否存在
     */
    @GetMapping("/stocks/{symbol}/exists")
    @Operation(summary = "检查股票是否存在", description = "检查指定股票代码是否存在于数据库")
    public Result<Boolean> checkExists(
            @Parameter(description = "股票代码", example = "AAPL")
            @PathVariable("symbol") String symbol) {
        
        log.info("检查股票是否存在: {}", symbol);

        try {
            boolean exists = stockService.exists(symbol);
            return Result.success(exists);
        } catch (Exception e) {
            log.error("检查股票失败: {}", e.getMessage(), e);
            return Result.systemError("检查失败: " + e.getMessage());
        }
    }

    /**
     * 根据行业查询股票
     * 
     * @param sector 行业名称
     * @return 股票列表
     */
    @GetMapping("/stocks/sector/{sector}")
    @Operation(summary = "根据行业查询股票", description = "查询指定行业的所有股票")
    public Result<List<StockSearchDTO>> getStocksBySector(
            @Parameter(description = "行业名称", example = "Technology")
            @PathVariable("sector") String sector) {
        
        log.info("根据行业查询股票: {}", sector);

        try {
            List<StockSearchDTO> stocks = stockService.findBySector(sector);
            return Result.success(stocks);
        } catch (Exception e) {
            log.error("查询行业股票失败: {}", e.getMessage(), e);
            return Result.systemError("查询失败: " + e.getMessage());
        }
    }

}
