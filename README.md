# CraftableEnchantments 附魔工艺

为 Slimefun 添加远古祭坛配方：合成 15 种魔法书与 40 余种原版附魔书，未改动原有机制。

## 项目定位

CraftableEnchantments 是一个 Slimefun 附属插件。它让原版附魔书变得可以合成：先用远古祭坛合成「魔法书」（气/土/火/水/雷/灵魂/迅捷/力量/弹性/灵巧/搜寻/驱魔/颠倒/幸运等系列）与「附魔书装帧」等中间材料，再用这些材料在远古祭坛上合成具体的附魔书。

## 添加了什么

- **材料组**：15 种魔法书 + 1 种附魔书装帧（魔法工作台 / 远古祭坛配方）
- **附魔书组**：40 余种原版附魔书，全部通过远古祭坛合成，注册为 `VanillaItem`（合成出的附魔书与原版附魔书完全一致，可直接使用或合并）
- **分类**：一个「附魔工艺」总分类，下辖「材料」「附魔书」两个子分类

## 修改了什么

- 未改动原有机制。不拦截任何事件、不覆写配方；不想让某本附魔书可合成时，在 `plugins/Slimefun/items.yml` 中按其 ID（前缀 `CE_`）禁用即可。

## 前置

| 插件 | 必需 |
| --- | --- |
| Slimefun4（漢化组版） | 必需（`depend: [Slimefun]`） |

## 安装

1. 服务端需 Java 21 及以上；
2. 安装 Slimefun4 后，将本插件 jar 放入 `plugins/` 目录并重启。

## 构建

```bash
./gradlew clean build
```

产物在 `build/libs/CraftableEnchantments-<版本>.jar`。Java 版本：编译目标 21。

## 兼容性

- 插件 `api-version: 1.14`；迅捷潜行附魔书仅在服务端 ≥ 1.19 时注册（代码中按 `MinecraftVersion.MINECRAFT_1_19` 门控）。

## 反馈

问题请到 [Issues](https://github.com/balugaq/CraftableEnchantments/issues) 提交，尽量附上服务端版本、Slimefun 版本与完整报错。

## 许可

见 [LICENSE](LICENSE)。
