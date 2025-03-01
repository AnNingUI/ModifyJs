MJSModelEvents.registerAdder(event => {
    event.register("adder/adder") // kubejs:models/adder/adder.json
})

/**
let bkM = MJSRenderUtils.getModel(new ResourceLocation(
    "kubejs","adder/adder"
))
Client.getBlockRenderer().getModelRenderer().renderModel(
    poseStack.last(),
    buffer.getBuffer($RenderType.cutoutMipped()),
    null,
    bkM,
    1,1,1,
    packedLight, $OverlayTexture.NO_OVERLAY
)
 */