> 使用模板 [KubeJSForgeAddonTemplate](https://github.com/CrychicTeam/KubeJSForgeAddonTemplate)
---

<p align="center">
<span> 简体中文 </span> | <a href="./README.md"> English </a>
</p>

这是一个简单的模组，最开始只是为了对一些kjs社区没有怎么实现的补充，现在只是实现了一些物品的渲染，可以视作对[RenderJS](https://github.com/ch1335/RenderJS)的一些简单补充，其次是添加了[MEK Module Item](https://wiki.aidancbrady.com/wiki/Modules)的注册类型

### 目标打算
- [x] 目前只添加了对 `Item | BlockItem` 的 `renderByItem` 的支持
- [x] 添加 [MEK Module Item](https://wiki.aidancbrady.com/wiki/Modules) 的物品注册类型
- [ ] ...(还在完善中)

### 注意事项
<span style="color: red;">
这个模组重写了<code style="color: red; font-size: 16px">BlockItemBuilder</code>的<code style="color: red; font-size: 16px">createObject</code>方法, 这可能会在在方块注册期间可能引起问题</span>

## 示例
看[这里](./example/)。