# 初学者项目环境搭建记录（Aider + Spring Boot + Docker）

> 本文档用于**个人回顾**，记录从零开始搭建后端开发环境的全过程，包括遇到的问题与解决方案。

---

## 一、背景说明

我是一个**初学者**，目标是开发一个基于 **Spring Boot + PostgreSQL + Redis** 的 Web 项目，并使用 **Aider（AI 编程助手）** 辅助开发。

在此之前，我的电脑上：

* ❌ 没有完整的 Java 后端开发环境
* ❌ 不清楚数据库、Redis、Docker 的作用
* ❌ 不熟悉 Aider 的使用方式

因此需要**一步一步搭建环境，并记录每一步在做什么**。

---

## 二、Aider 的安装与使用

### 2.1 什么是 Aider

Aider 是一个 **命令行 AI 编程助手**，可以：

* 直接修改项目代码
* 理解整个项目结构
* 自动生成 / 修改文件

它不是 IDE，而是**在终端中运行的工具**。

---

### 2.2 为什么要使用 venv（虚拟环境）

Aider 是用 **Python** 写的。

使用 `venv` 的好处：

* 不污染系统 Python
* 不影响其他项目
* 所有依赖只属于当前项目

---

### 2.3 安装与启动 Aider

```bash
# 创建虚拟环境
python -m venv venv

# 激活虚拟环境（Windows PowerShell）
.\venv\Scripts\activate

# 安装 aider
pip install aider-chat

# 启动 aider
aider
```

启动后会看到：

* Aider 版本信息
* 当前 Git 仓库状态

---

### 2.4 启动 Aider 时遇到的问题

#### 问题 1：是否加入 .gitignore

```text
Add .aider* to .gitignore (recommended)?
```

**选择：Yes**

原因：

* Aider 会生成临时文件
* 不需要提交到 Git

---

#### 问题 2：模型警告（context window）

```text
Unknown context window size and costs
```

**选择：D（Don't ask again）**

解释：

* 不影响使用
* 只是模型提示

---

## 三、为什么要用 Docker

### 3.1 不用 Docker 会怎样？

如果不用 Docker：

* 需要手动安装 PostgreSQL
* 需要配置环境变量
* 版本不一致容易出问题

---

### 3.2 Docker 是什么（通俗解释）

Docker = **软件的“盒子”**

* PostgreSQL 一个盒子
* Redis 一个盒子
* 不装在电脑上
* 用完可以删

**Docker ≠ 虚拟机**（更轻量）

---

### 3.3 安装 Docker（Windows 11）

#### 正确选择

下载：

* **Docker Desktop for Windows (x86_64)** ✅

安装完成后，验证：

```bash
docker --version
```

成功输出：

```text
Docker version 29.1.2
```

---

## 四、docker-compose 的作用

### 4.1 docker-compose 是什么

`docker-compose.yml` 用来：

* 一次性启动多个服务
* 自动创建网络
* 自动映射端口

例如：

* PostgreSQL
* Redis

---

### 4.2 docker-compose.yml 中做了什么

#### PostgreSQL

```yaml
POSTGRES_USER: stock_user
POSTGRES_PASSWORD: stock_password
POSTGRES_DB: stock_db
```

含义：

* 创建数据库用户
* 设置密码
* 自动创建数据库

#### Redis

```yaml
ports:
  - "6379:6379"
```

含义：

* Redis 启动在容器中
* 本机可以通过 6379 访问

---

### 4.3 遇到的问题：docker-compose 命令不存在

```text
docker-compose : The term 'docker-compose' is not recognized
```

#### 原因

新版 Docker：

* 不再单独提供 docker-compose.exe
* 已集成到 docker

#### 正确命令

```bash
docker compose up -d
```

---

## 五、Redis 是做什么用的

### 5.1 Redis 的角色（一句话）

Redis = **超快的内存缓存**

---

### 5.2 为什么项目要用 Redis

在项目中 Redis 用来：

* 缓存股票 K 线数据
* 缓存 AI 分析结果
* 减少数据库和外部 API 压力

举例：

> 第一次请求：查数据库 / API（慢）
> 第二次请求：查 Redis（快）

---

## 六、Spring Boot 中的 application.yml 配置

### 6.1 application.yml 是什么

它是：

* Spring Boot 的**总配置文件**
* 告诉程序：

    * 数据库在哪
    * 用什么账号
    * Redis 怎么连

---

### 6.2 数据库配置为什么要改

Docker 中 PostgreSQL 使用的是：

```text
username: stock_user
password: stock_password
database: stock_db
```

如果 Spring Boot 还用：

```text
postgres / postgres
```

→ ❌ 连不上数据库

---

### 6.3 我修改了什么

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/stock_db
    username: stock_user
    password: stock_password
```

这一步的作用是：

> 让 Spring Boot 能连上 Docker 里的数据库

---

### 6.4 JPA 配置的含义

```yaml
jpa:
  hibernate:
    ddl-auto: update
  show-sql: true
```

解释：

* `ddl-auto: update`

    * 实体类 → 自动建表
* `show-sql: true`

    * 控制台打印 SQL

非常适合**初学阶段**

---

## 七、整个流程总结（一句话版）

1. 用 venv 安装并隔离 Aider
2. 用 Aider 辅助生成 / 修改代码
3. 用 Docker 运行 PostgreSQL 和 Redis
4. 用 docker-compose 管理多个服务
5. 在 application.yml 中对齐配置
6. Spring Boot 成功连接数据库和缓存

---

## 八、当前状态

* ✅ Aider 正常运行
* ✅ Docker 安装完成
* ✅ PostgreSQL 运行中
* ✅ Redis 运行中
* ✅ Spring Boot 可正常连接

接下来可以开始：

* 写实体类
* 写接口
* 写业务逻辑

---

> 本文档将持续更新，用于长期回顾 🚀
