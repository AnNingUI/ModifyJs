package com.anningui.modifyjs;

import com.anningui.modifyjs.mixin.BlockItemBuilderMixin;
import com.anningui.modifyjs.render.MJSBakeModel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.Objects;

import static com.anningui.modifyjs.callback.BlockItemBuilderMap.mjs$customRendererMap;

@Mod(ModifyJS.ID)
public class ModifyJS {

    // You really don't need any of the mumbo-jumbo found here in other mods. Just the ID and Logger.
    public static final String ID = "modifyjs";
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
    }
}
