package com.anningui.modifyjs.kubejs.event;

import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.event.StartupEventJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ModelEvent;

import java.util.HashSet;
import java.util.Set;

public class ModelRegisterAdditional extends StartupEventJS {
    @HideFromJS
    public static Set<ResourceLocation> modelsAdderSet = new HashSet<>();
    @HideFromJS
    public ModelRegisterAdditional() {

    }
    @HideFromJS
    public static void onEvent(ModelEvent.RegisterAdditional event) {
        MJSModelEvents.REGISTER_ADDER.post(new ModelRegisterAdditional());
        modelsAdderSet.forEach(event::register);
    }
    public void register(String modelNamespace,String modelPath) {
        ResourceLocation model = new ResourceLocation(modelNamespace, modelPath);
        modelsAdderSet.add(model);
    }

    public void register(String modelPath) {
        ResourceLocation model = new ResourceLocation(KubeJS.MOD_ID, modelPath);
        modelsAdderSet.add(model);
    }
}
