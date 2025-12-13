# 项目需求说明书（PRD）

## 项目名称

Web 股票分析工具（Web Stock Analysis Platform）

## 版本信息

* 版本号：v1.1
* 状态：Implementation-Ready（可实施）
* 日期：2023-10-27
* 作者：Toom（架构师 / PM）

---

## 0. 文档变更说明（Changelog）

| 版本   | 变更内容                                     |
|------|------------------------------------------|
| v1.1 | 补充用户故事、页面结构、API 字段级定义、缓存与错误规范，使文档可直接指导开发 |
| v1.0 | 初始功能、架构与技术栈定义                            |

---

## 1. 项目概述（Overview）

### 1.1 背景

当前普通投资者缺乏一个既轻量又具备智能分析能力的股票工具。现有产品要么过于复杂（专业交易软件），要么缺乏深度（简单行情网页）。本项目旨在通过 Web 技术结合 AI 能力，填补这一空白。

### 1.2 项目定位

本项目为一个 **只读型、分析导向** 的 Web 股票分析平台，不涉及真实交易功能，不构成任何投资建议。

### 1.3 目标（Objectives）

* MVP 目标：构建一个支持股票搜索、专业 K 线展示、AI 行情解读的 Web 平台
* 核心性能指标：

    * 页面加载时间 < 2s
    * API 响应时间 < 300ms（缓存命中）
    * AI 分析生成时间 < 5s

### 1.4 适用范围（Scope）

**包含：**

* 股票搜索
* 基础行情数据展示
* 日线级 K 线图
* 技术指标计算
* AI 智能分析简报

**不包含（MVP 阶段）：**

* 实时 Tick 数据
* 用户注册 / 登录体系
* 真实交易或下单功能
* 复杂策略回测

---

## 2. 用户角色与用户故事（User Roles & Stories）

### 2.1 用户角色

* 普通投资者：需要直观的行情展示与 AI 解读
* 技术分析爱好者：需要流畅的图表与技术指标支持

### 2.2 用户故事（User Stories）

US-01：
作为一名普通投资者，
我希望通过输入股票代码或名称快速搜索股票，
以便查看其走势和 AI 分析结果。

US-02：
作为一名技术分析用户，
我希望在 K 线图上切换 MA 指标，
以便辅助判断趋势强弱。

---

## 3. 页面结构定义（Page Definition）

### P-01 股票搜索页

* 股票搜索输入框
* 搜索建议列表（Symbol + Name）
* 点击后跳转股票详情页

### P-02 股票详情页

* 基础信息区（当前价、涨跌幅、市值、行业）
* K 线图区域（日线）
* 技术指标控制区（MA5 / MA10 / MA20 显示切换）
* AI 智能分析结果面板

---

## 4. 功能需求（Functional Requirements）

### 4.1 模块一：行情与数据（Market Data）

| ID   | 功能点      | 优先级 | 描述                                  | 验收标准                     |
|------|----------|-----|-------------------------------------|--------------------------|
| F-01 | 股票搜索     | P0  | 支持按代码或名称模糊搜索                        | 输入 "AAP" 能联想出 "AAPL"     |
| F-02 | 基础信息展示   | P0  | 展示当前价、涨跌幅、市值、行业                     | 数据与 Yahoo / Tushare 保持一致 |
| F-03 | 交互式 K 线图 | P0  | 基于 TradingView / Lightweight Charts | 正确渲染 OHLCV，无明显卡顿         |
| F-04 | 技术指标     | P1  | 支持 MA、成交量等指标                        | MA5 / MA10 / MA20 可切换    |

### 4.2 模块二：AI 智能分析（AI Analysis）

| ID   | 功能点     | 优先级 | 描述                 | 验收标准          |
|------|---------|-----|--------------------|---------------|
| F-05 | 技术面摘要   | P0  | 后端计算 RSI、均线金叉等关键指标 | 能识别突破、多头排列    |
| F-06 | AI 走势点评 | P0  | 调用 LLM 生成约 200 字分析 | 包含趋势、支撑/压力、风险 |
| F-07 | 分析缓存    | P1  | AI 分析结果缓存 1 小时     | 1 小时内重复请求返回缓存 |

---

## 5. 技术架构与数据设计（Architecture & Data）

### 5.1 技术栈（Tech Stack）

* 前端：Vue 3、Vite、Pinia、Tailwind CSS、Element Plus、Lightweight Charts
* 后端：Java、Spring Boot 3.x、JDK 17+
* 数据库：PostgreSQL 15+
* 缓存：Redis 7+
* AI 服务：OpenAI API / Gemini API（后端 Proxy 调用）
* 数据源：Yahoo Finance API

### 5.2 数据库设计（Database Schema）

**stocks（股票基础表）**

* symbol（PK）
* name
* sector
* last_update

**daily_quotes（日线行情表）**

* id（PK）
* symbol（FK）
* date
* open / high / low / close
* volume

Unique(symbol, date)

---

## 6. 接口规范（API Contract）- v1.1 Updated

### Base URL

`/api/v1`

### 6.1 通用响应包装（Response Wrapper）

所有接口必须返回统一 JSON 结构：

```json
{
  "code": 200,
  "msg": "success",
  "data": {}
}
```

* `code`：200 成功 / 400 参数错误 / 500 系统错误
* `msg`：调试或提示信息
* `data`：实际业务数据

---

### 6.2 核心接口定义

#### A. 搜索股票

* **路径**：`GET /search`

* **参数**：

    * `q`（String，必填）：股票代码或名称关键字

* **业务逻辑**：

    * 查询 `stocks` 表
    * 匹配 `symbol` 或 `name`

* **响应示例**：

```json
{
  "code": 200,
  "data": [
    { "symbol": "AAPL", "name": "Apple Inc.", "exchange": "NASDAQ" },
    { "symbol": "MSFT", "name": "Microsoft", "exchange": "NASDAQ" }
  ]
}
```

### 6.3 业务逻辑目录结构
src/main/java/com/example/stock
├── config/             # Redis, Cors, Swagger 配置
├── controller/         # StockController, AnalysisController
├── model/
│   ├── entity/         # Stock, DailyQuote (JPA Entities)
│   ├── dto/            # API Request/Response DTOs
│   └── vo/             # View Objects
├── repository/         # JPA Repositories
├── service/            # 核心业务逻辑
│   ├── impl/
│   ├── StockService.java
│   ├── MarketDataService.java (对接 Yahoo)
│   └── AIService.java
└── utils/              # 工具类 (DateUtil, Calculator)

---

#### B. 获取 K 线数据（适配 Lightweight Charts）

* **路径**：`GET /stocks/{symbol}/candles`

* **参数**：

    * `period`：`1d`（默认） / `1w`
    * `limit`：返回条数（默认 200）

* **业务逻辑**：

    1. 查询 Redis：`market:candles:{symbol}:{period}`
    2. 命中缓存直接返回
    3. 未命中：查询 DB
    4. DB 数据过期则调用 Yahoo Finance API
    5. 更新 DB 与 Redis 后返回

* **响应示例（数组压缩格式）**：

```json
{
  "code": 200,
  "data": [
    [1672531200, 120.5, 122.0, 119.8, 121.2, 5000000],
    [1672617600, 121.2, 123.5, 121.0, 123.0, 6000000]
  ]
}
```

> 数组格式定义：`[timestamp, open, high, low, close, volume]`

---

#### C. 获取 / 触发 AI 分析报告

* **路径**：`GET /stocks/{symbol}/analysis`

* **业务逻辑**：

    1. 查询 Redis：`ai:report:{symbol}`（TTL 1 小时）
    2. 若存在缓存直接返回
    3. 若不存在：

        * 计算技术指标（RSI、MA 等）
        * 组装 Prompt
        * 调用 LLM
        * 写入 Redis
        * 返回结果

* **响应示例**：

```json
{
  "code": 200,
  "data": {
    "summary": "看涨",
    "content": "该股票近期突破 20 日均线，成交量放大，MACD 显示金叉...",
    "support_price": 120.5,
    "resistance_price": 135.0,
    "updated_at": "2023-10-27 10:00:00"
  }
}
```

---

## 7. 缓存、降级与错误规范

### 7.1 缓存策略

* 行情数据缓存：TTL 5 分钟
* AI 分析缓存：TTL 60 分钟

### 7.2 降级策略

* 外部 API 超限：返回缓存数据
* AI 超时：返回提示信息，不阻塞页面

### 7.3 错误码规范

| Code | 含义      |
|------|---------|
| 0    | 成功      |
| 1001 | 股票不存在   |
| 2001 | 外部数据源异常 |
| 3001 | AI 服务超时 |

---

## 8. 开发计划（Roadmap）

* Week 1：基础架构与数据库、接口骨架
* Week 2：行情数据接入与 K 线图渲染
* Week 3：AI 分析集成、UI 优化与测试

---

## 9. MVP 验收标准（Definition of Done）

* 搜索功能可用
* 股票详情页可正常加载
* K 线图渲染流畅
* AI 分析 5 秒内返回
* 外部服务异常时系统不崩溃
