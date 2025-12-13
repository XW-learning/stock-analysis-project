package com.example.stock.exception;

/**
 * 股票不存在异常
 * 
 * @author Toom
 * @version 1.0
 */
public class StockNotFoundException extends RuntimeException {

    private final String symbol;

    public StockNotFoundException(String symbol) {
        super("Stock not found: " + symbol);
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

}
