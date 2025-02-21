> This English Readme is translated by `GPT-4o`, please understand if there is any incomprehension.

---

<p align="center">
<a href="./README_ZH.md"> 简体中文 </a> | <span> English </span>
</p>

This is a simple mod that initially started as an addition for some features not implemented by the KJS community. It currently implements some basic item rendering, which can be considered a simple extension to[RenderJS](https://github.com/ch1335/RenderJS). And an Item type that can be used to create [MEK Module Item](https://wiki.aidancbrady.com/wiki/Modules)

### TODE LIST
- [x] Currently only the support for item renderByItem has been added
- [x] Add an Item type that can be used to create [MEK Module Item](https://wiki.aidancbrady.com/wiki/Modules)
- [ ] ...(still being improved)

### Attention
<span style="color: red;">
This mod overrides the <code style="color: red; font-size: 16px">createObject</code> method of <code style="color: red; font-size: 16px">BlockItemBuilder</code>, which may cause issues during block registration.</span>

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