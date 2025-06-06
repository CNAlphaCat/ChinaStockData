package cn.alphacat.chinastockdata.model.stock;

import lombok.Data;

@Data
public class StockInfo {
    private String stockCode;
    private String stockName;
    private String exchangeMarket;
}
