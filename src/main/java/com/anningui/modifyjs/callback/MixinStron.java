package com.anningui.modifyjs.callback;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class MixinStron {
    public static List<RenderLayer<?, ?>> layers = new ArrayList<>();
    public static HumanoidModel<LivingEntity> innerModel = null;
    public static HumanoidModel<LivingEntity> outerModel = null;

    public static boolean isInitialized() {
        return innerModel != null && outerModel != null && !layers.isEmpty();
    }
}
