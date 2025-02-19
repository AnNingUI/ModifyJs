> This English Readme is translated by `GPT-4o`, please understand if there is any incomprehension.

---

This is a simple mod that initially started as an addition for some features not implemented by the KJS community. It currently implements some basic item rendering, which can be considered a simple extension to[RenderJS](https://github.com/ch1335/RenderJS)

## Example:
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
If you need to add ItemRender to regular items, you should use a builder Type `mjs_item`
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