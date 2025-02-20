package com.anningui.modifyjs.callback;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class CustomInterface {
    @FunctionalInterface
    public interface KQuadConsumer<T, U, V, W, R> {
        R apply(T t, U u, V v, W w);
    }

    // 自定义五个参数的回调接口
    @FunctionalInterface
    public interface KQuintConsumer<T, U, V, W, X, R> {
        R apply(T t, U u, V v, W w, X x);

    }

    // 自定义三个参数的回调接口
    @FunctionalInterface
    public interface KTriConsumer<T, U, V, R> {
        R apply(T t, U u, V v);
    }

    // 自定义六个参数的回调接口
    @FunctionalInterface
    public interface KSextConsumer<T, U, V, W, X, Y, R> {
        R apply(T t, U u, V v, W w, X x, Y y);
    }


    public static boolean isNull(Object obj) {
        return obj == null;
    }

    @FunctionalInterface
    public interface RenderByItemCallback {
        void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay);
    }
}
