package com.example.stock.repository;

import com.example.stock.model.entity.DailyQuote;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 日线行情数据访问接口
 * 
 * @author Toom
 * @version 1.0
 */
@Repository
public interface DailyQuoteRepository extends JpaRepository<DailyQuote, Long> {

    /**
     * 根据股票代码查询所有行情数据（按日期降序）
     * 
     * @param symbol 股票代码
     * @return 行情数据列表
     */
    List<DailyQuote> findBySymbolOrderByDateDesc(String symbol);

    /**
     * 根据股票代码查询最近 N 条行情数据
     * 
     * @param symbol 股票代码
     * @param pageable 分页参数
     * @return 行情数据列表
     */
    List<DailyQuote> findBySymbolOrderByDateDesc(String symbol, Pageable pageable);

    /**
     * 根据股票代码和日期范围查询行情数据
     * 
     * @param symbol 股票代码
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 行情数据列表
     */
    List<DailyQuote> findBySymbolAndDateBetweenOrderByDateAsc(
        String symbol, LocalDate startDate, LocalDate endDate);

    /**
     * 根据股票代码和日期查询单条行情数据
     * 
     * @param symbol 股票代码
     * @param date 交易日期
     * @return 行情数据
     */
    Optional<DailyQuote> findBySymbolAndDate(String symbol, LocalDate date);

    /**
     * 查询指定股票的最新交易日期
     * 
     * @param symbol 股票代码
     * @return 最新交易日期
     */
    @Query("SELECT MAX(dq.date) FROM DailyQuote dq WHERE dq.symbol = :symbol")
    Optional<LocalDate> findLatestDateBySymbol(@Param("symbol") String symbol);

    /**
     * 查询指定股票的最新一条行情数据
     * 
     * @param symbol 股票代码
     * @return 最新行情数据
     */
    @Query("SELECT dq FROM DailyQuote dq WHERE dq.symbol = :symbol " +
           "AND dq.date = (SELECT MAX(dq2.date) FROM DailyQuote dq2 WHERE dq2.symbol = :symbol)")
    Optional<DailyQuote> findLatestBySymbol(@Param("symbol") String symbol);

    /**
     * 批量删除指定股票的行情数据
     * 
     * @param symbol 股票代码
     */
    void deleteBySymbol(String symbol);

    /**
     * 检查指定股票和日期的行情数据是否存在
     * 
     * @param symbol 股票代码
     * @param date 交易日期
     * @return 是否存在
     */
    boolean existsBySymbolAndDate(String symbol, LocalDate date);

}
