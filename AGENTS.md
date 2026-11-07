# AGENTS.md — CraftableEnchantments

## 项目概览

Slimefun 附属插件：为 15 种魔法书与 40 余种原版附魔书提供远古祭坛合成配方，未改动原有机制。构建：Gradle Kotlin DSL + shadowJar，Java 21。

## 包地图

```
io.github.schntgaispock.craftableenchantments
├── CraftableEnchantments.java     主类，继承 InfinityLib 的 AbstractAddon
└── core
    ├── setup/ItemSetup.java       所有物品注册（唯一改动热点，绝大部分配方都在这里）
    ├── slimefun/CEGroups.java     分类定义（MultiGroup MAIN + 两个 SubGroup）
    ├── slimefun/CEStacks.java     SlimefunItemStack 定义（魔法书名称与配色）
    └── theme/                     ItemTheme / ThemedItemStack 物品配色主题
```

## 启动顺序

`enable()` → bStats Metrics → `ItemSetup.setup()`（注册 MAIN 分类与全部物品）。`disable()` 仅清空 instance。

## 构建与验收

- `./gradlew clean build`；产物 `build/libs/CraftableEnchantments-<版本>.jar`。
- 编译目标：`options.release = 21`；不要在 build.gradle.kts 里加 toolchain 钉死 JDK（本机与 CI JDK 可能不同，release 已足够）。
- shadowJar：minimize + relocate（`io.github.mooy1.infinitylib`、`org.bstats` → 本包前缀）。
- `plugin.yml` 的 `version: ${version}` 由 `processResources` 的 `expand("version" to ...)` 注入；改动时不要写回 `${project.version}`（那是 Maven 占位符）。

## 决策树

- 加新附魔书 → `ItemSetup.setup()` 里复制一个 `VanillaItem` 块，配方材料复用 `CEStacks` 的魔法书 + `ENCHANTED_BOOK_BINDING`。
- 加新魔法书 → `CEStacks` 加 `MAGIC_TOME_*`（含配色）+ `ItemSetup` 加远古祭坛配方。
- 改分类图标/名称 → `CEGroups`（名称带颜色码，主题色来自 `ItemTheme`）。

## 语言

物品名内嵌中文与颜色码（`CEGroups` / `CEStacks` / `ItemTheme`），无独立语言文件；改文案需重新构建。

## 红线

- 不要把编译目标降回 17 或引入 Maven；`pom.xml` 已删除，Gradle 是唯一构建系统。
- 不要升级 spigot-api 到 1.20.5+，除非同步处理 `Enchantment.*` 旧常量改名（当前代码大量使用旧常量名）。
- 提交权归主人；agent 不 commit / push。

## 已知债务

- Slimefun4 依赖从 commit 快照（38953fe13a，JitPack 已 404）迁到 2025.1.2；新 API 下的运行时行为未经服务器实测。
- 15 个魔法书的 `getTomeName` 使用 `ChatColor.of("#…")` 自定义色，1.14 低版本客户端显示效果未验证。
