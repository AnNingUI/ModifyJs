package com.anningui.modifyjs.mixin;

import com.anningui.modifyjs.builder.item.armor.RenderArmorItem;
import com.anningui.modifyjs.callback.MixinStore;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

import static java.util.Objects.isNull;

@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {
    public HumanoidArmorLayerMixin(RenderLayerParent<T, M> renderer) {
        super(renderer);
    }

    @Unique
    private ArmorItem mjs$armorItem;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(RenderLayerParent<T, M> renderer, A innerModel, A outerModel, ModelManager modelManager, CallbackInfo ci) {
        MixinStore.innerModel = (HumanoidModel<LivingEntity>) innerModel;
        MixinStore.outerModel = (HumanoidModel<LivingEntity>) outerModel;
    }

    @Final
    @Inject(method = "renderArmorPiece", at = @At("HEAD"))
    private void getItem(PoseStack poseStack, MultiBufferSource buffer, T livingEntity, EquipmentSlot slot, int packedLight, A model, CallbackInfo ci) {
        this.mjs$armorItem = livingEntity.getItemBySlot(slot).getItem() instanceof ArmorItem ? (ArmorItem) livingEntity.getItemBySlot(slot).getItem() : null;
    }

//    @ModifyArg(
//            method = "renderModel(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/item/ArmorItem;Lnet/minecraft/client/model/Model;ZFFFLnet/minecraft/resources/ResourceLocation;)V",
//            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/Model;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V"),
//            index = 7
//    )
//    private float modifyAlpha(float alpha) {
//        if (this.mjs$armorItem instanceof RenderArmorItem) {
//            RenderArmorItem.Builder builder = ((RenderArmorItem) this.mjs$armorItem).kjs$getItemBuilder();
//            if (builder!= null && builder.noShowArmorModel) {
//                return 0.0F;
//            }
//        }
//        return alpha;
//    }

    @Inject(method = "setPartVisibility", at = @At(value = "TAIL"))
    private void setPartVisibility(A model, EquipmentSlot slot, CallbackInfo ci) {
        if (
                !isNull(this.mjs$armorItem) &&
                this.mjs$armorItem instanceof RenderArmorItem renderArmorItem &&
                renderArmorItem.getEquipmentSlot() == slot &&
                Objects.requireNonNull(renderArmorItem.kjs$getItemBuilder()).noShowArmorModel
        ) {
            A thisModel = getArmorModel(slot);
            A armorModel = getArmorModel(this.mjs$armorItem.getEquipmentSlot());
            if (thisModel == armorModel) {
                switch (this.mjs$armorItem.getEquipmentSlot()) {
                    case HEAD:
                        model.head.visible = false;
                        model.hat.visible = false;
                        break;
                    case CHEST:
                        model.body.visible = false;
                        model.rightArm.visible = false;
                        model.leftArm.visible = false;
                        break;
                    case LEGS:
                        model.body.visible = false;
                        model.rightLeg.visible = false;
                        model.leftLeg.visible = false;
                        break;
                    case FEET:
                        model.rightLeg.visible = false;
                        model.leftLeg.visible = false;
                }
            }
        }
    }

    @Shadow public abstract A getArmorModel(EquipmentSlot slot);
}
