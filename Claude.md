# Flight Search App - 项目开发指南

## 项目概述

基于Google Android Codelab的现代化Flight Search应用，采用最新的Android开发最佳实践。

- **项目名称**: Flight Search
- **包名**: com.mingqing.flightsearch  
- **目标API**: Android 14 (API 35)
- **最低支持**: Android 7.0 (API 24)

## 技术栈

### 核心框架
- **UI**: Jetpack Compose + Material3
- **架构**: Clean Architecture + MVVM
- **依赖注入**: Hilt
- **数据库**: Room
- **数据存储**: Preferences DataStore
- **导航**: Navigation Compose
- **状态管理**: StateFlow + SharedFlow

### 开发工具
- **Kotlin**: 1.9.24
- **Gradle**: 8.8.2
- **编译工具链**: Java 11

## 项目结构

```
app/src/main/java/com/mingqing/flightsearch/
├── data/                    # 数据层
│   ├── database/           # Room数据库
│   ├── datastore/         # Preferences数据存储
│   └── repository/        # Repository实现
├── domain/                 # 领域层
│   ├── model/             # 领域模型
│   ├── repository/        # Repository接口
│   └── usecase/           # 业务用例
├── presentation/           # 表示层
│   ├── ui/                # UI组件
│   ├── viewmodel/         # ViewModel
│   └── theme/             # 主题系统
├── di/                    # 依赖注入模块
└── FlightSearchApplication.kt
```

## 核心功能需求

### 1. 机场搜索与自动完成
- 根据IATA代码或机场名称搜索
- 实时显示搜索建议
- 按乘客流量排序结果

### 2. 航班路线展示  
- 显示从选定出发机场的所有可能航线
- 按目的地机场的乘客流量排序

### 3. 收藏功能
- 保存/移除喜爱的航线
- 持久化存储用户收藏
- 在无搜索状态时显示收藏列表

### 4. 搜索状态持久化
- 保存用户的搜索文本
- 应用重启时恢复搜索状态

## 数据模型

### Airport (机场表)
```kotlin
@Entity(tableName = "airport")
data class Airport(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "iata_code") val iataCode: String,
    val name: String,
    val passengers: Int
)
```

### Favorite (收藏表)  
```kotlin
@Entity(tableName = "favorite")
data class Favorite(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "departure_code") val departureCode: String,
    @ColumnInfo(name = "destination_code") val destinationCode: String
)
```

## 开发约定

### 命名规范
- **包命名**: 小写，使用点分隔
- **类命名**: PascalCase (大驼峰)
- **函数/变量**: camelCase (小驼峰)
- **常量**: UPPER_SNAKE_CASE
- **资源文件**: snake_case

### Git工作流
- **主分支**: `main` (生产就绪代码)
- **开发分支**: `develop` (集成分支)
- **功能分支**: `feature/功能名称`
- **修复分支**: `hotfix/问题描述`

### 提交信息格式
```
type(scope): description

feat(database): add Room database setup
fix(ui): resolve search animation glitch
docs(readme): update installation instructions
```

## UI设计原则

### Material3 设计系统
- 使用动态颜色主题 (Dynamic Color)
- 完整支持深色/浅色模式
- 遵循Material3组件规范
- 保持一致的间距和typography

### 关键UI组件
- **FlightSearchScreen**: 主屏幕容器
- **SearchBar**: 自动完成搜索框
- **AirportList**: 机场建议列表
- **FlightRouteCard**: 航线卡片
- **FavoriteButton**: 收藏切换按钮

## 性能考虑

### 数据库优化
- 为搜索字段创建适当索引
- 使用Room编译时SQL验证
- 实施合理的查询限制

### UI性能
- 使用LazyColumn处理长列表
- 实施适当的状态提升
- 避免不必要的重组

## 测试策略

### 单元测试
- **Repository**: 数据层测试
- **UseCase**: 业务逻辑测试  
- **ViewModel**: UI状态测试

### UI测试
- **Compose测试**: 组件行为验证
- **端到端测试**: 完整用户流程

## 常用命令

### 构建与运行
```bash
# 构建debug版本
./gradlew assembleDebug

# 运行单元测试
./gradlew testDebugUnitTest

# 运行UI测试
./gradlew connectedDebugAndroidTest

# 代码检查
./gradlew lintDebug
```

### 数据库调试
```bash
# 使用adb查看数据库
adb shell
cd /data/data/com.mingqing.flightsearch/databases/
sqlite3 flight_database.db
```

## 外部资源

- [Android Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Material3 Design](https://m3.material.io/)  
- [Room Persistence Library](https://developer.android.com/training/data-storage/room)
- [Hilt Dependency Injection](https://developer.android.com/training/dependency-injection/hilt-android)

## 开发进度追踪

使用GitHub Issues和Projects跟踪开发进度，确保按计划交付各个功能模块。

## 开发进度跟踪

### 当前状态 (2025-08-18)
- ✅ 项目初始化完成
- ✅ Git分支策略设置 (main -> dev)
- ✅ 基础项目文档创建
- ✅ **Compose依赖配置完成** - 成功解决版本兼容性问题
- 🔄 **进行中**: 创建基本项目结构 (Clean Architecture)
- ⏳ **下一步**: 实现基础UI界面

### 已实现功能
- [x] 项目基础架构
- [x] **UI基础框架** (Compose + Material3) - 构建通过 ✅
- [ ] 数据层 (Room + Repository)
- [ ] 搜索功能
- [ ] 收藏功能
- [ ] 状态持久化

### 技术债务和决策
- **架构选择**: 采用Clean Architecture分层，逐步添加依赖
- **UI框架**: Jetpack Compose + Material3
- **版本策略**: 保守升级 - 只修复必要的兼容性问题
  - Kotlin 1.9.24 + Compose Compiler 1.5.14 (官方兼容组合)
  - AGP 8.8.2 + compileSdk 35 (稳定环境)
  - JDK 17 工具链 (现代化基础)

### 跨会话开发指南
1. 每次新会话开始前：`git pull origin dev`
2. 查看此文档的"当前状态"部分了解进度
3. 告知Claude："根据Claude.md继续开发，当前在[具体步骤]"
4. Claude会自动恢复开发上下文和todo列表

### 重要命令记录
```bash
# 构建项目
./gradlew build

# 运行应用
./gradlew installDebug

# 同步依赖变更
./gradlew --refresh-dependencies
```

---

*该文档会在重要里程碑自动更新，确保跨会话开发的连续性*