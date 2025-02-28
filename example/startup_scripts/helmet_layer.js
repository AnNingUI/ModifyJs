// const $Axis = Java.loadClass("com.mojang.math.Axis");
// const $ItemDisplayContext = Java.loadClass("net.minecraft.world.item.ItemDisplayContext");
StartupEvents.registry("item", e => {
    e.create("test_item", "render_helmet")
        .isCustomRenderer(true)
        .renderByItem((itemStack, itemDisplayCtx, poseStack, buffer, packedLight, packedOverlay) => {
            // ...
        }).addLayerRender((ctx) => {
            global.testRender(ctx)
        })
})
/**
 *
 * @param {Internal.ArmorLayerContext} ctx
 */
global.testRender = (ctx) => {
    let { poseStack, packedLight, livingEntity, buffer, headPitch, netHeadYaw, partialTicks } = ctx;
    // headPitch up down 90[down] ~ -90[up] netHeadYaw left right -50[left] ~ 50[right]
    let player = /** @type { Internal.Player } */(livingEntity);
    let sPos = [
        0,
        0.5,
        0
    ]
    {
        let isSneaking = player.isCrouching();
        let isFallFlying = player.isFallFlying();
        let fallFlyingAmount = player.getFallFlyingTicks();
        let isSwimming = player.isSwimming();
        let swimmingAmount = player.getSwimAmount(partialTicks);

        poseStack.pushPose();
        if (isSneaking) {
            poseStack.translate(0,0.2617994, - Math.sin(0.2617994 * KMath.PI / 180))
        }
        // yew
        poseStack.mulPose($Axis.YP.rotationDegrees(netHeadYaw));
        // pitch
        if (isFallFlying && fallFlyingAmount > 4 || isSwimming && swimmingAmount > 0.2) {
            poseStack.mulPose($Axis.XP.rotationDegrees(-45));
        } else {
            poseStack.mulPose($Axis.XP.rotationDegrees(headPitch));
        }
        poseStack.mulPose($Axis.ZP.rotationDegrees(180));
        poseStack.mulPose($Axis.YP.rotationDegrees(180));
        poseStack.scale(1.1, 1.1, 1.1)
        poseStack.translate(
            sPos[0],
            sPos[1],
            sPos[2]
        )

        Client.itemRenderer.renderStatic(
            Item.of("creeper_head"),
            $ItemDisplayContext.HEAD,
            packedLight,
            $OverlayTexture.NO_OVERLAY,
            poseStack,
            buffer,
            Client.level,
            Client.player.getId()
        )

        poseStack.popPose();
    }
}