package com.example.stock.repository;

import com.example.stock.model.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 股票基础信息数据访问接口
 *
 * @author Toom
 * @version 1.0
 */
@Repository
public interface StockRepository extends JpaRepository<Stock, String> {

    /**
     * 根据股票代码查询（忽略大小写）
     *
     * @param symbol 股票代码
     * @return 股票实体
     */
    Optional<Stock> findBySymbolIgnoreCase(String symbol);

    /**
     * 根据股票名称模糊查询（忽略大小写）
     * 用于搜索功能
     *
     * @param name 股票名称关键字
     * @return 股票列表
     */
    List<Stock> findByNameContainingIgnoreCase(String name);

    /**
     * 根据股票代码或名称模糊查询（组合搜索）
     *
     * @param keyword 查询关键字，可用于匹配股票代码或名称
     * @return 股票列表
     */
    @Query("SELECT s FROM Stock s WHERE " +
            "LOWER(s.symbol) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Stock> searchBySymbolOrName(@Param("keyword") String keyword);

    /**
     * 根据行业查询股票列表
     *
     * @param sector 行业名称
     * @return 股票列表
     */
    List<Stock> findBySector(String sector);

    /**
     * 检查股票代码是否存在
     *
     * @param symbol 股票代码
     * @return 是否存在
     */
    boolean existsBySymbolIgnoreCase(String symbol);

}
