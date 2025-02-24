package com.anningui.modifyjs.mixin;

import com.anningui.modifyjs.callback.MixinStron;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {
    public HumanoidArmorLayerMixin(RenderLayerParent<T, M> renderer) {
        super(renderer);
    }

    @Final
    @Shadow
    private A innerModel;
    @Final
    @Shadow
    private A outerModel;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(RenderLayerParent<T, M> renderer, A innerModel, A outerModel, ModelManager modelManager, CallbackInfo ci) {
        MixinStron.innerModel = (HumanoidModel<LivingEntity>) innerModel;
        MixinStron.outerModel = (HumanoidModel<LivingEntity>) outerModel;
    }
}
