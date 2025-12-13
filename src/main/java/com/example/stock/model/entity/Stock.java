package com.example.stock.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 股票基础信息实体类
 * 
 * @author Toom
 * @version 1.0
 */
@Entity
@Table(name = "stocks", indexes = {
    @Index(name = "idx_stock_name", columnList = "name"),
    @Index(name = "idx_stock_sector", columnList = "sector")
})
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock {

    /**
     * 股票代码（主键）
     * 例如：AAPL, MSFT
     */
    @Id
    @Column(name = "symbol", length = 20, nullable = false)
    private String symbol;

    /**
     * 股票名称
     * 例如：Apple Inc., Microsoft Corporation
     */
    @Column(name = "name", length = 200, nullable = false)
    private String name;

    /**
     * 交易所代码
     * 例如：NASDAQ, NYSE
     */
    @Column(name = "exchange", length = 50)
    private String exchange;

    /**
     * 所属行业/板块
     * 例如：Technology, Healthcare
     */
    @Column(name = "sector", length = 100)
    private String sector;

    /**
     * 最后更新时间
     */
    @Column(name = "last_update")
    @LastModifiedDate
    private LocalDateTime lastUpdate;

    /**
     * 创建时间
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

}
