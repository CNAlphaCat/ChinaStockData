# 已实现功能

## StockService

### 1.获取单个股票的今日分时行情 
getStockMin


#### 说明介绍
获取单个股票最新交易日的分时行情

盘中请求就是当天的实时分时行情

分钟级别：每分钟一条数据

#### 调用方法

```Java
 public ArrayList<StockMin> getStockMin(String stockCode)
```


## MarketService

### 1.获取指数的行情信息-日、周、月 k线
getMarketIndex

#### 说明介绍

获取单个指数的K线行情

日，周，月K



#### 调用方法

```Java
  public List<MarketIndex> getMarketIndex(String indexCode, LocalDate startDate, KLineType kType)
```

## FutureService

### 1.获取所有期货的基本信息

#### 调用方法

```Java
 public List<FutureMarketOverview> getFuturesBaseInfo()
```

### 2.获取期货的K线数据

#### 调用方法

```Java
 public List<FutureHistory> getFutureHistory(
        String code,
        LocalDate beg,
        LocalDate end,
        EastMoneyQTKlineTypeEnum klt,
        EastMoneyQTKlineWeightingEnum fqt)
```

### 3.调用秒级期货的K线数据

#### 调用方法

```Java
public List<FutureDetail> getFutureDetails(String secId, int maxCount)
```