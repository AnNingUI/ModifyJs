package com.anningui.modifyjs.util.render;

import com.anningui.modifyjs.ModifyJS;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.logging.Logger;


@OnlyIn(Dist.CLIENT)
public class MJSRenderUtils {
    @Info("""
    /**
     * @author Flander923
     * @link <a href="https://www.bilibili.com/video/BV1t1AUe7ErD?vd_source=a6e9e72f334103d28476ce3f30850f61">...</a>
     */
    """)
    public static void renderModelLists(
            ItemRenderer itemRenderer,
            BakedModel model,
            ItemStack stack,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            int packedOverlay
    ) {
        for (BakedModel bakedModel : model.getRenderPasses(stack, true)) {
            for (RenderType renderType : model.getRenderTypes(stack, true)) {
                VertexConsumer vertexConsumer;
                if (stack.hasFoil()) {
                    vertexConsumer = ItemRenderer.getFoilBuffer(buffer, renderType, true, true);
                } else {
                    vertexConsumer = ItemRenderer.getFoilBuffer(buffer, renderType, true, false);
                }
                itemRenderer.renderModelLists(
                        bakedModel,
                        stack,
                        packedLight,
                        packedOverlay,
                        poseStack,
                        vertexConsumer
                );
            }
        }
    }

    /**
     *
     */
    @Info("""
    /**
     * @author Lat
     * @link <a href="https://github.com/FTBTeam/FTB-Jar-Mod/blob/main/src/main/java/dev/ftb/mods/ftbjarmod/block/entity/render/JarBlockEntityRenderer.java">...</a>
     */
    """)
    public static void renderFluidModel(
            FluidStack fluidStack,
            float fluidScale,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int light,
            int packedOverlay
    ) {
        Minecraft mc = Minecraft.getInstance();
        mc.getTextureManager().bindForSetup(InventoryMenu.BLOCK_ATLAS);
        VertexConsumer builder = bufferSource.getBuffer(RenderType.translucent());
        TextureAtlasSprite sprite =
                mc.getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                        .apply(IClientFluidTypeExtensions.of(fluidStack.getFluid())
                        .getStillTexture(fluidStack));
        Matrix4f m = poseStack.last().pose();
        Matrix3f n = poseStack.last().normal();
        int color = IClientFluidTypeExtensions.of(fluidStack.getFluid()).getTintColor(fluidStack);
        float r = (float) ((color >> 16) & 255) / 255.0f;
        float g = (float) ((color >> 8) & 255) / 255.0f;
        float b = (float) ((color) & 255) / 255.0f;
        float a = 1.0f;
        float s0 = 3.2f / 16;
        float s1 = 1 - s0;
        float y0 = 0.2f / 16;
        float y1 = (0.2f + 12.6f * fluidScale) / 16;
        float u0 = sprite.getU(3);
        float v0 = sprite.getV0();
        float u1 = sprite.getU(13);
        float v1 = sprite.getV(y1 * 16);
        float u0top = sprite.getU(3);
        float v0top = sprite.getV(3);
        float u1top = sprite.getU(13);
        float v1top = sprite.getV(13);
        builder.vertex(m, s0, y1, s0).color(r, g, b, a).uv(u0top, v0top).overlayCoords(packedOverlay).uv2(light).normal(n, 0, 1, 0).endVertex();
        builder.vertex(m, s0, y1, s1).color(r, g, b, a).uv(u0top, v1top).overlayCoords(packedOverlay).uv2(light).normal(n, 0, 1, 0).endVertex();
        builder.vertex(m, s1, y1, s1).color(r, g, b, a).uv(u1top, v1top).overlayCoords(packedOverlay).uv2(light).normal(n, 0, 1, 0).endVertex();
        builder.vertex(m, s1, y1, s0).color(r, g, b, a).uv(u1top, v0top).overlayCoords(packedOverlay).uv2(light).normal(n, 0, 1, 0).endVertex();

        builder.vertex(m, s0, y0, s0).color(r, g, b, a).uv(u0top, v0top).overlayCoords(packedOverlay).uv2(light).normal(n, 0, 1, 0).endVertex();
        builder.vertex(m, s1, y0, s0).color(r, g, b, a).uv(u1top, v0top).overlayCoords(packedOverlay).uv2(light).normal(n, 0, 1, 0).endVertex();
        builder.vertex(m, s1, y0, s1).color(r, g, b, a).uv(u1top, v1top).overlayCoords(packedOverlay).uv2(light).normal(n, 0, 1, 0).endVertex();
        builder.vertex(m, s0, y0, s1).color(r, g, b, a).uv(u0top, v1top).overlayCoords(packedOverlay).uv2(light).normal(n, 0, 1, 0).endVertex();

        builder.vertex(m, s0, y1, s1).color(r, g, b, a).uv(u0, v0).overlayCoords(packedOverlay).uv2(light).normal(n, 0, 1, 0).endVertex();
        builder.vertex(m, s0, y0, s1).color(r, g, b, a).uv(u0, v1).overlayCoords(packedOverlay).uv2(light).normal(n, 0, 1, 0).endVertex();
        builder.vertex(m, s1, y0, s1).color(r, g, b, a).uv(u1, v1).overlayCoords(packedOverlay).uv2(light).normal(n, 0, 1, 0).endVertex();
        builder.vertex(m, s1, y1, s1).color(r, g, b, a).uv(u1, v0).overlayCoords(packedOverlay).uv2(light).normal(n, 0, 1, 0).endVertex();

        builder.vertex(m, s0, y1, s0).color(r, g, b, a).uv(u0, v0).overlayCoords(packedOverlay).uv2(light).normal(n, 0, 1, 0).endVertex();
        builder.vertex(m, s1, y1, s0).color(r, g, b, a).uv(u1, v0).overlayCoords(packedOverlay).uv2(light).normal(n, 0, 1, 0).endVertex();
        builder.vertex(m, s1, y0, s0).color(r, g, b, a).uv(u1, v1).overlayCoords(packedOverlay).uv2(light).normal(n, 0, 1, 0).endVertex();
        builder.vertex(m, s0, y0, s0).color(r, g, b, a).uv(u0, v1).overlayCoords(packedOverlay).uv2(light).normal(n, 0, 1, 0).endVertex();

        builder.vertex(m, s0, y1, s0).color(r, g, b, a).uv(u0, v0).overlayCoords(packedOverlay).uv2(light).normal(n, 0, 1, 0).endVertex();
        builder.vertex(m, s0, y0, s0).color(r, g, b, a).uv(u0, v1).overlayCoords(packedOverlay).uv2(light).normal(n, 0, 1, 0).endVertex();
        builder.vertex(m, s0, y0, s1).color(r, g, b, a).uv(u1, v1).overlayCoords(packedOverlay).uv2(light).normal(n, 0, 1, 0).endVertex();
        builder.vertex(m, s0, y1, s1).color(r, g, b, a).uv(u1, v0).overlayCoords(packedOverlay).uv2(light).normal(n, 0, 1, 0).endVertex();

        builder.vertex(m, s1, y1, s0).color(r, g, b, a).uv(u0, v0).overlayCoords(packedOverlay).uv2(light).normal(n, 0, 1, 0).endVertex();
        builder.vertex(m, s1, y1, s1).color(r, g, b, a).uv(u1, v0).overlayCoords(packedOverlay).uv2(light).normal(n, 0, 1, 0).endVertex();
        builder.vertex(m, s1, y0, s1).color(r, g, b, a).uv(u1, v1).overlayCoords(packedOverlay).uv2(light).normal(n, 0, 1, 0).endVertex();
        builder.vertex(m, s1, y0, s0).color(r, g, b, a).uv(u0, v1).overlayCoords(packedOverlay).uv2(light).normal(n, 0, 1, 0).endVertex();
    }

}
