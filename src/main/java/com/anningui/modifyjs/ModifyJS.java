package com.anningui.modifyjs;

import com.anningui.modifyjs.mixin.BlockItemBuilderMixin;
import com.anningui.modifyjs.render.MJSBakeModel;
import dev.architectury.platform.Platform;
import mekanism.api.MekanismIMC;
import mekanism.common.integration.MekanismHooks;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.registries.ForgeRegistries;


import java.util.Map;
import java.util.Objects;

import static com.anningui.modifyjs.callback.BlockItemBuilderMap.mjs$customRendererMap;
import static com.anningui.modifyjs.mod_adder.mek.custom.module.KubeJSModuleDataBuilder.getAllModuleDataBuilder;
import static com.anningui.modifyjs.mod_adder.mek.util.KubeJSMekUntiItemUtils.getModuleById;
import static java.util.Objects.isNull;

@Mod(ModifyJS.ID)
public class ModifyJS {


    // You really don't need any of the mumbo-jumbo found here in other mods. Just the ID and Logger.
    public static final String ID = "modifyjs";

    public static Object hooks;

    public ModifyJS() {
        if (Platform.isModLoaded("mekanism")) {
            hooks = new MekanismHooks();
        }
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
    public static class ModEventBus{
        @SubscribeEvent
        public static void onModelBaked(ModelEvent.ModifyBakingResult event){
            // wrench item model
            Map<ResourceLocation, BakedModel> modelRegistry = event.getModels();
            var bkMap = mjs$customRendererMap;
            for (var entry : bkMap.entrySet()) {
                var id = entry.getKey();
                var isCustomRenderer = entry.getValue();
                if (!isCustomRenderer) return;
                ModelResourceLocation location = new ModelResourceLocation(BuiltInRegistries.ITEM.getKey(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(id)).asItem()), "inventory");
                BakedModel existingModel = modelRegistry.get(location);
                if (existingModel == null || existingModel instanceof MJSBakeModel) {
                    return;
                } else {
                    MJSBakeModel obsidianWrenchBakedModel = new MJSBakeModel(existingModel);
                    event.getModels().put(location, obsidianWrenchBakedModel);
                }
            }
        }

        @SubscribeEvent
        public static void onImcQueue(InterModEnqueueEvent event) {
            if (Platform.isModLoaded("mekanism") && !isNull(hooks)) {

                ((MekanismHooks) hooks).sendIMCMessages(event);
                var a = getAllModuleDataBuilder();
                a.forEach(b -> {
                    var s  = b.slot;
                    var id = b.id;
                    var m  = getModuleById(id);
                    switch (s) {
                        case ALL:
                            MekanismIMC.addModulesToAll(m);
                            break;
                        case MEK_TOOL:
                            MekanismIMC.addMekaToolModules(m);
                            break;
                        case MEK_SUIT_HELMET:
                            MekanismIMC.addMekaSuitHelmetModules(m);
                            break;
                        case MEK_SUIT_BODY:
                            MekanismIMC.addMekaSuitBodyarmorModules(m);
                            break;
                        case MEK_SUIT_PANTS:
                            MekanismIMC.addMekaSuitPantsModules(m);
                            break;
                        case MEK_SUIT_BOOTS:
                            MekanismIMC.addMekaSuitBootsModules(m);
                    }
                });
            }
        }
    }
}
