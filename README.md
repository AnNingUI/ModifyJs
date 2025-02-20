> Use Template By [KubeJSForgeAddonTemplate](https://github.com/CrychicTeam/KubeJSForgeAddonTemplate)
---
> Example
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