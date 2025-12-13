package com.example.stock.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 日线行情数据实体类
 * 
 * @author Toom
 * @version 1.0
 */
@Entity
@Table(name = "daily_quotes", 
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_symbol_date", columnNames = {"symbol", "date"})
    },
    indexes = {
        @Index(name = "idx_daily_quote_symbol", columnList = "symbol"),
        @Index(name = "idx_daily_quote_date", columnList = "date")
    }
)
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyQuote {

    /**
     * 自增主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 股票代码（外键）
     */
    @Column(name = "symbol", length = 20, nullable = false)
    private String symbol;

    /**
     * 关联股票实体（多对一）
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "symbol", referencedColumnName = "symbol", 
                insertable = false, updatable = false)
    private Stock stock;

    /**
     * 交易日期
     */
    @Column(name = "date", nullable = false)
    private LocalDate date;

    /**
     * 开盘价
     */
    @Column(name = "open", precision = 10, scale = 2, nullable = false)
    private BigDecimal open;

    /**
     * 最高价
     */
    @Column(name = "high", precision = 10, scale = 2, nullable = false)
    private BigDecimal high;

    /**
     * 最低价
     */
    @Column(name = "low", precision = 10, scale = 2, nullable = false)
    private BigDecimal low;

    /**
     * 收盘价
     */
    @Column(name = "close", precision = 10, scale = 2, nullable = false)
    private BigDecimal close;

    /**
     * 成交量
     */
    @Column(name = "volume", nullable = false)
    private Long volume;

    /**
     * 调整后收盘价（复权价）
     */
    @Column(name = "adj_close", precision = 10, scale = 2)
    private BigDecimal adjClose;

    /**
     * 创建时间
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

}
