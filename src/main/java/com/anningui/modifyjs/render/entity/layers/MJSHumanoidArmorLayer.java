package com.anningui.modifyjs.render.entity.layers;

import com.anningui.modifyjs.ModifyJS;
import com.anningui.modifyjs.builder.item.armor.RenderArmorItem;
import com.anningui.modifyjs.callback.ArmorLayerContext;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

import static com.anningui.modifyjs.builder.item.armor.RenderArmorItem.Builder.allArmorLayers;
import static com.anningui.modifyjs.builder.item.armor.RenderArmorItem.Builder.instances;

@OnlyIn(Dist.CLIENT)
public class MJSHumanoidArmorLayer <T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends HumanoidArmorLayer<T, M, A> {
    public MJSHumanoidArmorLayer(RenderLayerParent<T, M> renderer, A innerModel, A outerModel, ModelManager modelManager) {
        super(renderer, innerModel, outerModel, modelManager);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, @NotNull T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        allArmorLayers.forEach((k, v) -> {
            if (k.mjs$isCustomRenderer && shouldRenderArmor(livingEntity, k.id)) {
                v.accept(new ArmorLayerContext(
                        poseStack,
                        buffer,
                        packedLight,
                        livingEntity,
                        limbSwing,
                        limbSwingAmount,
                        partialTicks,
                        ageInTicks,
                        netHeadYaw,
                        headPitch
                ));
            }
        });
        super.render(poseStack, buffer, packedLight, livingEntity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
    }

    private boolean shouldRenderArmor(T livingEntity, ResourceLocation armorId) {
        List<ResourceLocation> ids = List.of(
                livingEntity.kjs$getHeadArmorItem().kjs$getIdLocation(),
                livingEntity.kjs$getChestArmorItem().kjs$getIdLocation(),
                livingEntity.kjs$getLegsArmorItem().kjs$getIdLocation(),
                livingEntity.kjs$getFeetArmorItem().kjs$getIdLocation()
        );
        int index = ids.indexOf(armorId);
        return index != -1;
    }
}
