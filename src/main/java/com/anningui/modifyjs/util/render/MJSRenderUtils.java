package com.anningui.modifyjs.util.render;

import com.anningui.modifyjs.ModifyJS;
import com.anningui.modifyjs.util.RayTraceResultMJS;
import com.anningui.modifyjs.util.js_long.TryCatchPipe;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;


@OnlyIn(Dist.CLIENT)
public class MJSRenderUtils {
    private static final Minecraft mc = Minecraft.getInstance();

    @Info("""
    @author Flander923\n
    @link <a href="https://www.bilibili.com/video/BV1t1AUe7ErD?vd_source=a6e9e72f334103d28476ce3f30850f61">...</a>
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
    @author Lat\n
    @link <a href="https://github.com/FTBTeam/FTB-Jar-Mod/blob/main/src/main/java/dev/ftb/mods/ftbjarmod/block/entity/render/JarBlockEntityRenderer.java">...</a>
    """)
    public static void renderFluidModel(
            FluidStack fluidStack,
            float fluidScale,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int light,
            int packedOverlay
    ) {
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

    public static void renderLineIn3D(
            Vec3 start,
            Vec3 end,
            int startColor,
            int endColor,
            PoseStack poseStack,
            MultiBufferSource bufferSource
    ) {
        Matrix4f m = poseStack.last().pose();
        Matrix3f n = poseStack.last().normal();
        // 渲染线条
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.lines());
        vertexConsumer.vertex(m, (float) start.x, (float) start.y, (float) start.z)
                .color(startColor)
                .normal(n, 0, 1, 0)
                .endVertex();
        vertexConsumer.vertex(m, (float) end.x, (float) end.y, (float) end.z)
                .color(endColor)
                .normal(n, 0, 1, 0)
                .endVertex();
    }

    @Deprecated
    public static BakedModel getModel(ResourceLocation idPath) {
        return mc.getModelManager().getModel(idPath);
    }

    @Deprecated
    public static BakedModel getModel(ResourceLocation idPath, String variant) {
        return mc.getModelManager().getModel(new ModelResourceLocation(idPath, variant));
    }

    public static BakedModel getJsonModel(ResourceLocation idPath) {
        return mc.getModelManager().getModel(new ModelResourceLocation(idPath, "standalone"));
    }

    public static BakedModel getJsonModel(ResourceLocation idPath, String variant) {
        return mc.getModelManager().getModel(new ModelResourceLocation(idPath, variant));
    }

    public static <T extends LivingEntity> Optional<EntityModel<T>> getEntityModel(T entity) {
        if (entity != null) {
            var renderer = mc.getEntityRenderDispatcher().getRenderer(entity);
            try {
                return Optional.of(((LivingEntityRenderer<T, EntityModel<T>>) renderer).getModel());
            } catch (Exception e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    public static <T extends LivingEntity> Optional<HumanoidModel<T>> getHumanoidModel(T entity) {
        if (entity != null) {
            var renderer = mc.getEntityRenderDispatcher().getRenderer(entity);
            try {
                return Optional.of(((LivingEntityRenderer<T, HumanoidModel<T>>) renderer).getModel());
            } catch (Exception e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    public static <T extends LivingEntity> boolean isHumanoidModel(T entity) {
        if (entity != null) {
            var renderer = mc.getEntityRenderDispatcher().getRenderer(entity);
            return renderer instanceof LivingEntityRenderer && ((LivingEntityRenderer) renderer).getModel() instanceof HumanoidModel;
        }
        return false;
    }

    public static <T extends LivingEntity> void runOnHumanoidModel(T entity, Consumer<HumanoidModel<T>> consumer) {
        TryCatchPipe.tryCatchBBV(
                isHumanoidModel(entity),
                () -> consumer.accept(getHumanoidModel(entity).orElse(null)),
                () -> {}
        );
    }

    public static HumanoidModel<AbstractClientPlayer> getPlayerModel(AbstractClientPlayer player) {
        if (player != null) {
            var renderer = mc.getEntityRenderDispatcher().getRenderer(player);
            return ((PlayerRenderer) renderer).getModel();
        }
        return null;
    }

    public static void renderEntityLineIn3D(
            Entity entity,
            float rayLength,
            int startColor,
            int endColor,
            PoseStack poseStack,
            MultiBufferSource bufferSource
    ) {
        // 获取实体的朝向
        float yaw = entity.getYRot();
        float pitch = entity.getXRot();

        // 转换为弧度
        double pi = Math.PI;
        double yawRad = yaw * (pi / 180.0);
        double pitchRad = pitch * (pi / 180.0);

        // 计算视线方向
        double x = -Math.sin(yawRad) * Math.cos(pitchRad);
        double y = -Math.sin(pitchRad);
        double z = Math.cos(yawRad) * Math.cos(pitchRad);

        // 计算射线终点
        float endX = (float) x * rayLength;
        float endY = (float) y * rayLength;
        float endZ = (float) z * rayLength;

        // 渲染射线
        renderLineIn3D(
            new Vec3(0,0,0),
            new Vec3(endX, endY, endZ),
            startColor,
            endColor,
            poseStack,
            bufferSource
        );
    }

    @Info("""
    @param r 0~255\n
    @param g 0~255\n
    @param b 0~255\n
    @param a 0~1
    """)
    public static int rgba255ToColor(int r, int g, int b, float a) {
        int alpha = (int) (a * 255);
        // 确保 alpha 的值在有效范围内
        alpha = Math.max(0, Math.min(255, alpha));
        return ((alpha & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
    }

    public static RayTraceResultMJS getPlayerRayTrace(Player player, double distance) {
        // 获取玩家的视角向量
        Vec3 lookVec = player.getLookAngle();

        // 获取玩家当前位置
        Vec3 playerPos = new Vec3(player.getX(), player.getY(), player.getZ());

        // 计算结束位置（玩家视角指定距离的位置）
        Vec3 endPos = playerPos.add(lookVec.x * distance, lookVec.y * distance, lookVec.z * distance);

        // 创建并返回一个 RayTraceResultJS 记录对象
        return new RayTraceResultMJS(
                playerPos.x, playerPos.y, playerPos.z,
                endPos.x, endPos.y, endPos.z,
                new Vec3(playerPos.x, playerPos.y, playerPos.z),
                new Vec3(endPos.x, endPos.y, endPos.z),
                new Vec3(lookVec.x, lookVec.y, lookVec.z)
        );
    }
}
