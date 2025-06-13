# 🌌 Galaxy · 银河启明

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](./LICENSE)

## 介绍

个人用 Spring Boot 以及 Spring Cloud 快速启动模板，命名为 Galaxy 银河

## Galaxy 🌠 核心星系

### 📦 通用基础模块 ([galaxy-common](./galaxy-common))

| 模块名称                                                 | 功能描述                       |
|------------------------------------------------------|----------------------------|
| **[common-core](./galaxy-common/common-core)**       | 核心基础模块（基础类/常量/异常/统一响应封装）   |
| **[common-dubbo](./galaxy-common/common-dubbo)**     | Dubbo 服务框架集成模块             |
| **[common-mybatis](./galaxy-common/common-mybatis)** | MyBatis ORM 集成（含分页/二级缓存支持） |
| **[common-nacos](./galaxy-common/common-nacos)**     | Nacos 配置中心/服务发现集成          |
| **[common-redis](./galaxy-common/common-redis)**     | Redis 缓存集成模块               |
| **[common-satoken](./galaxy-common/common-satoken)** | 安全认证框架（Sa-Token）集成         |
| **[common-web](./galaxy-common/common-web)**         | Web 基础模块（工具类/异常处理/基础参数）    |

### 🚀 业务服务模块 ([galaxy-application](./galaxy-application))

| 服务模块                                       | 功能范围                                      |
| ---------------------------------------------- | --------------------------------------------- |
| **[app-system](./galaxy-application/-system)** | 核心系统服务（用户/角色/权限/菜单/字典/日志） |
