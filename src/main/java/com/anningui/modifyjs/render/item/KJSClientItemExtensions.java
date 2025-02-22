package com.anningui.modifyjs.render.item;

import com.anningui.modifyjs.callback.CustomInterface;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;


public class KJSClientItemExtensions implements IClientItemExtensions {
    public CustomInterface.RenderByItemCallback mjs$renderByItemCallback;

    public KJSClientItemExtensions(CustomInterface.RenderByItemCallback renderCallback) {
        this.mjs$renderByItemCallback = renderCallback;
    }

    @Override
    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return new KJSBlockItemRenderer(mjs$renderByItemCallback);
    }

    public static class KJSBlockItemRenderer extends BlockEntityWithoutLevelRenderer {
        public CustomInterface.RenderByItemCallback mjs$renderByItemCallback;
        public KJSBlockItemRenderer(CustomInterface.RenderByItemCallback renderCallback) {
            super(null, null);
            mjs$renderByItemCallback = renderCallback;
        }

        @Override
        public void renderByItem(@NotNull ItemStack stack, @NotNull ItemDisplayContext displayContext, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
            if (mjs$renderByItemCallback != null) {
                mjs$renderByItemCallback.renderByItem(stack, displayContext, poseStack, buffer, packedLight, packedOverlay);
            } else {
                super.renderByItem(stack, displayContext, poseStack, buffer, packedLight, packedOverlay);
            }
        }
    }
}
