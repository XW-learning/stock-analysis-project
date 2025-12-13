package com.example.stock.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 股票详情 DTO
 * 
 * @author Toom
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockDetailDTO {

    /**
     * 股票代码
     */
    private String symbol;

    /**
     * 股票名称
     */
    private String name;

    /**
     * 交易所
     */
    private String exchange;

    /**
     * 所属行业
     */
    private String sector;

    /**
     * 最后更新时间
     */
    private LocalDateTime lastUpdate;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

}
