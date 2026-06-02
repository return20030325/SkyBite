# SkyBite（苍穹外卖）

基于 Spring Boot 的外卖点餐平台后端服务，提供管理端和用户端双端 API。

## 技术栈

| 技术 | 说明 |
|------|------|
| Spring Boot 2.7.3 | 基础框架 |
| MyBatis | ORM |
| MySQL | 数据库 |
| Redis | 缓存 |
| JWT | 身份认证 |
| Knife4j（Swagger） | 接口文档 |
| WebSocket | 实时推送 |
| 微信支付 SDK | 微信支付 |
| 阿里云 OSS | 文件存储 |
| Apache POI | Excel 报表 |
| Spring Task | 定时任务 |

## 项目结构

```
sky-take-out/
├── sky-common/          # 公共模块（常量、枚举、异常、工具类）
├── sky-pojo/            # 实体/DTO/VO
│   ├── entity/          #   数据表实体
│   ├── dto/             #   数据传输对象
│   └── vo/              #   视图返回对象
└── sky-server/          # 服务端主模块
    ├── config/          #   配置类
    ├── controller/      #   控制器
    │   ├── admin/       #     管理端接口
    │   ├── user/        #     用户端接口
    │   └── notify/      #     支付回调
    ├── service/         #   业务接口
    │   └── impl/        #     业务实现
    ├── mapper/          #   MyBatis Mapper
    ├── task/            #   定时任务
    ├── interceptor/     #   JWT 拦截器
    ├── handler/         #   全局异常处理
    └── websocket/       #   WebSocket 服务
```

## 快速开始

### 环境要求

- JDK 8+
- MySQL 5.7+
- Redis
- Maven 3.6+

### 本地运行

```bash
# 1. 克隆项目
git clone https://github.com/return20030325/SkyBite.git
cd SkyBite

# 2. 配置环境变量（参考 .env.example 填入真实值）
#    Linux / macOS:
export SKY_DB_PASSWORD=your_password
export SKY_OSS_ENDPOINT=oss-cn-hangzhou.aliyuncs.com
export SKY_OSS_BUCKET=your_bucket
export SKY_OSS_KEY=your_access_key
export SKY_OSS_SECRET=your_access_secret
export SKY_WECHAT_APPID=your_appid
export SKY_WECHAT_SECRET=your_secret
export SKY_REDIS_PASSWORD=your_redis_password

#    Windows PowerShell:
$env:SKY_DB_PASSWORD="your_password"
# ... 同理设置其他变量

# 3. 导入数据库
#    在 MySQL 中创建数据库 sky_take_out，执行项目中的 SQL 脚本

# 4. 打包运行
mvn clean package -DskipTests
java -jar sky-server/target/sky-server-1.0-SNAPSHOT.jar
```

启动后访问接口文档：http://localhost:8080/doc.html

## 核心功能

### 管理端（/admin）

- **员工管理** — 登录/登出、新增/编辑/启禁用员工
- **分类管理** — 菜品分类的增删改查
- **菜品管理** — 菜品及口味的增删改查、起售/停售
- **套餐管理** — 套餐及关联菜品的增删改查
- **订单管理** — 订单搜索、接单/拒单、派送/完成
- **工作台** — 今日运营数据概览
- **数据报表** — 营业额/用户/订单统计与 Excel 导出

### 用户端（/user）

- 微信登录
- 分类/菜品/套餐浏览
- 购物车管理
- 下单与支付
- 订单查询、再来一单、催单

### 定时任务

- 每分钟检查并取消超时未支付订单（15 分钟）
- 每日凌晨自动完成超时派送订单

## 接口文档

启动项目后访问：
- 管理端接口：http://localhost:8080/doc.html（分组"管理端接口"）
- 用户端接口：http://localhost:8080/doc.html（分组"用户端接口"）

## 环境变量

| 变量 | 说明 |
|------|------|
| `SKY_DB_PASSWORD` | 数据库密码 |
| `SKY_OSS_ENDPOINT` | 阿里云 OSS 地域节点 |
| `SKY_OSS_BUCKET` | OSS 存储空间名称 |
| `SKY_OSS_KEY` | OSS AccessKey ID |
| `SKY_OSS_SECRET` | OSS AccessKey Secret |
| `SKY_WECHAT_APPID` | 微信小程序 AppID |
| `SKY_WECHAT_SECRET` | 微信小程序 Secret |
| `SKY_REDIS_PASSWORD` | Redis 密码（无密码留空） |

完整配置模板见 `.env.example`。

## License

MIT
