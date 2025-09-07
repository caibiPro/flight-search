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
- **数据库**: Room + Room Paging
- **数据存储**: Preferences DataStore
- **导航**: Navigation Compose
- **状态管理**: StateFlow + SharedFlow
- **分页处理**: Paging 3

### 开发工具
- **Kotlin**: 1.9.25
- **Gradle**: 8.8.2
- **编译工具链**: Java 17

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

### 当前状态 (2025-01-09)
- ✅ 项目初始化完成
- ✅ Git分支策略设置 (main -> dev)
- ✅ 基础项目文档创建
- ✅ **核心依赖配置完成** - Room, Hilt, Navigation, DataStore, KSP, Paging 3
- ✅ **Material3主题系统实现** - 航空蓝主题 + Edge-to-Edge设计
- ✅ **MainActivity和主题测试框架** - 完整的组件测试页面
- ✅ **代码架构优化** - 模块化组件设计
- ✅ **Clean Architecture数据层完全实现** - 企业级数据层架构
- 🔄 **进行中**: 实现ViewModel和UI层
- ⏳ **下一步**: 创建FlightSearchViewModel和核心UI界面

### 已实现功能
- [x] **完整的项目基础架构** - 依赖管理、主题系统、分支策略
- [x] **完整的UI基础框架** (Compose + Material3 + 主题系统) ✅
- [x] **MainActivity和测试框架** - 包含完整的组件测试页面
- [x] **模块化UI组件架构** - 按功能拆分的组件文件
- [x] **Clean Architecture数据层** - 完整实现 ✅
  - [x] Room数据库 + 实体层 (AirportEntity, FavoriteEntity)
  - [x] DAO层 (AirportDao, FavoriteDao) 
  - [x] Repository层 (AirportRepository, FavoriteRepository)
  - [x] Domain层 (Airport, Favorite领域模型)
  - [x] 依赖注入 (完整的Hilt配置)
  - [x] Paging 3集成 (大数据量处理)
  - [x] Flow响应式编程 (shareIn性能优化)
- [ ] ViewModel层 (业务逻辑和UI状态管理)
- [ ] UI层 (Compose界面组件)
- [ ] 导航系统 (Navigation Compose)
- [ ] 数据预置 (assets/database文件)

### 技术债务和决策
- **架构选择**: 采用Clean Architecture分层，数据层已完全实现
- **UI框架**: Jetpack Compose + Material3
- **依赖注入**: 选择KSP替代kapt，提供更好的编译性能和Kotlin multiplatform支持
- **版本策略**: 保守升级 - 只修复必要的兼容性问题
  - Kotlin 1.9.25 + Compose Compiler 1.5.15 (官方兼容组合)
  - AGP 8.8.2 + compileSdk 35 (稳定环境)
  - JDK 17 工具链 (现代化基础)
  - KSP 1.9.25-1.0.20 (匹配Kotlin版本)
  - Room 2.6.1 + Room Paging (完整数据库解决方案)
  - Paging 3.3.2 (大数据量处理)
  - Hilt 2.51.1 (最新稳定版依赖注入)
- **主题系统**: Material Theme Builder生成的航空蓝主题
  - 种子色: #769CDF (航空蓝)
  - 支持动态颜色 (Android 12+)
  - 实现Edge-to-Edge设计，透明状态栏和导航栏
  - 避免使用弃用的statusBarColor API
- **代码组织**: 模块化组件架构
  - 主题测试框架：将单一大文件拆分为独立的组件文件
  - 每个组件都包含完整的Preview注解，支持浅色和深色主题预览
  - 按功能职责分离UI组件（颜色样本、输入框、按钮、卡片等）
- **数据层设计**: 企业级架构实现
  - Clean Architecture分层清晰，领域模型与数据模型分离
  - Repository模式实现，支持Flow响应式编程
  - shareIn优化策略，避免重复数据库查询
  - Paging 3集成，支持大数据量无限滚动
  - 防竞态条件设计，确保收藏操作的线程安全

### 重要技术成就
- **KSP迁移成功**: 从kapt迁移到KSP，解决了插件兼容性问题，提升构建性能
- **主题系统完善**: 实现了与动态颜色兼容的航空蓝主题，支持浅色/深色模式切换
- **Edge-to-Edge设计**: 成功实现现代Android设计规范，透明状态栏与内容颜色自适应
- **组件化架构**: 建立了可维护的模块化UI组件结构，便于后续功能开发
- **Clean Architecture数据层**: 完整实现企业级数据层架构
  - 完美的分层设计，Entity→Domain Model转换
  - 高性能Flow操作，shareIn避免重复查询
  - Paging 3无缝集成，支持大数据量处理
  - Hilt依赖注入完整配置，@Singleton + @ApplicationContext
  - 竞态条件防护，线程安全的数据操作
- **依赖配置优化**: 解决了Paging Compose和Room Paging的依赖问题

### 已解决的技术问题
1. **kotlin-kapt插件冲突**: 通过迁移到KSP解决版本兼容性问题
2. **状态栏颜色不一致**: 通过Edge-to-Edge设计和透明状态栏解决
3. **Preview与实机效果差异**: 通过动态颜色控制和主题一致性配置解决
4. **代码维护性**: 通过组件文件拆分提升代码可读性和维护性
5. **中英文混合注释**: 统一转换为英文注释，提升代码规范性
6. **依赖解析错误**: 修复androidx.paging依赖的group名称问题
7. **Room Paging集成**: 添加room-paging依赖，成功集成PagingSource功能

### 下一阶段计划
- **ViewModel层实现**: 创建FlightSearchViewModel
  - 实现搜索状态管理
  - 集成Repository数据流
  - 处理用户交互逻辑
  - 实现收藏功能的UI状态
- **核心UI组件**: 基于设计系统实现界面
  - FlightSearchScreen主界面
  - SearchBar自动完成搜索
  - AirportList机场列表显示
  - FlightRouteCard航线卡片
  - FavoriteButton收藏按钮
- **导航系统**: 设置Navigation Compose
- **数据预置**: 添加assets/database/flight_search.db机场数据

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

*该文档已于 2025-01-09 更新，记录了Clean Architecture数据层的完整实现*