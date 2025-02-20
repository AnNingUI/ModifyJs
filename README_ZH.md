> 使用模板 [KubeJSForgeAddonTemplate](https://github.com/CrychicTeam/KubeJSForgeAddonTemplate)
---

<p align="center">
<span> 简体中文 </span> | <a href="./README.md"> English </a>
</p>

这是一个简单的模组，最开始只是为了对一些kjs社区没有怎么实现的补充，现在只是实现了一些物品的渲染，可以视作对[RenderJS](https://github.com/ch1335/RenderJS)的一些简单补充

## 示例:
```javascript
const $Axis = Java.loadClass("com.mojang.math.Axis");
StartupEvents.registry("block", event => {
    event.create("test_block")
        .item((p) => {
            p.isCustomRenderer(true)
            p.renderByItem((itemStack, itemDisplayCtx, poseStack, buffer, packedLight, packedOverlay) => {
                poseStack.pushPose()
                let time = Date.now();
                let angle = (time / 10) % 360;
                poseStack.mulPose($Axis.YP.rotationDegrees(angle));
                Client.itemRenderer.getBlockEntityRenderer().renderByItem(
                    "blue_bed",
                    itemDisplayCtx,
                    poseStack,
                    buffer,
                    packedLight,
                    packedOverlay,
                )
                poseStack.popPose()
            })
        })
})
```
---
如果你需要给普通的物品添加ItemRender，需要使用一个builder Type `mjs_item`
```javascript
StartupEvents.registry("item", e => {
    e.create("test_item", "mjs_item")
        .isCustomRenderer(true)
        .renderByItem((itemStack, itemDisplayCtx, poseStack, buffer, packedLight, packedOverlay) => {
            Client.itemRenderer.getBlockEntityRenderer().renderByItem(
                "blue_bed",
                itemDisplayCtx,
                poseStack,
                buffer,
                packedLight,
                packedOverlay,
            )
        })
})
```