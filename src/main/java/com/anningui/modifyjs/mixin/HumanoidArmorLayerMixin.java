package com.anningui.modifyjs.mixin;

import com.anningui.modifyjs.builder.item.armor.RenderArmorItem;
import com.anningui.modifyjs.callback.MixinStron;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {
    public HumanoidArmorLayerMixin(RenderLayerParent<T, M> renderer) {
        super(renderer);
    }

    @Unique
    private ArmorItem mjs$armorItem;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(RenderLayerParent<T, M> renderer, A innerModel, A outerModel, ModelManager modelManager, CallbackInfo ci) {
        MixinStron.innerModel = (HumanoidModel<LivingEntity>) innerModel;
        MixinStron.outerModel = (HumanoidModel<LivingEntity>) outerModel;
    }

    @Final
    @Inject(method = "renderModel*", at = @At("HEAD"), remap = false)
    private void getItem(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, ArmorItem armorItem, Model model, boolean bl, float f, float g, float h, ResourceLocation resourceLocation, CallbackInfo ci) {
        this.mjs$armorItem = armorItem;
    }

    @ModifyArg(
            method = "renderModel(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/item/ArmorItem;Lnet/minecraft/client/model/Model;ZFFFLnet/minecraft/resources/ResourceLocation;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/Model;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V"),
            index = 7
    )
    private float modifyAlpha(float alpha) {
        if (this.mjs$armorItem instanceof RenderArmorItem) {
            RenderArmorItem.Builder builder = ((RenderArmorItem) this.mjs$armorItem).kjs$getItemBuilder();
            if (builder!= null && builder.noShowArmorModel) {
                return 0.0F;
            }
        }
        return alpha;
    }
}
