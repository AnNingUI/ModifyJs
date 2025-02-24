package com.anningui.modifyjs;

import com.anningui.modifyjs.callback.MixinStron;
import com.anningui.modifyjs.mixin.LivingEntityRendererMixin;
import com.anningui.modifyjs.mod_adder.mek.custom.module.KubeJSModuleDataBuilder;
import com.anningui.modifyjs.mod_adder.mek.util.UnitItemSlots;
import com.anningui.modifyjs.render.entity.layers.MJSHumanoidArmorLayer;
import com.anningui.modifyjs.render.item.MJSBakeModel;
import dev.architectury.platform.Platform;
import mekanism.api.MekanismIMC;
import mekanism.api.gear.IModuleHelper;
import mekanism.common.integration.MekanismHooks;
import mekanism.common.registries.MekanismModules;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;


import java.util.*;

import static com.anningui.modifyjs.mod_adder.mek.custom.item.KubeJSUnitItemBuilder.specs;
import static com.anningui.modifyjs.mod_adder.mek.custom.module.KubeJSModuleDataBuilder.getAllModuleDataBuilder;
import static com.anningui.modifyjs.mod_adder.mek.util.KubeJSMekUntiItemUtils.getModuleById;
import static java.util.Objects.isNull;

@Mod(ModifyJS.ID)
public class ModifyJS {

    // You really don't need any of the mumbo-jumbo found here in other mods. Just the ID and Logger.
    public static final String ID = "modifyjs";

    public static Map<ResourceLocation, Boolean> mjs$customRendererMap = new HashMap<>();

    public static Object hooks;



    public ModifyJS() {
        if (Platform.isModLoaded("mekanism")) {
            hooks = new MekanismHooks();
        }
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
    public static class ModEventBus{
        @SubscribeEvent
        public static void init(FMLClientSetupEvent event) {
            if (Platform.isModLoaded("mekanism") && !isNull(specs)) {

                IModuleHelper moduleHelper = IModuleHelper.INSTANCE;
                specs.forEach((key, s) -> moduleHelper.addMekaSuitModuleModelSpec(
                        s.name,
                        s.moduleDataProvider.get(),
                        s.slotType,
                        s.isActive
                ));
            }
        }

        @SubscribeEvent
        public static void onRendererRegistry(EntityRenderersEvent.AddLayers event) {
            for (String skinName : event.getSkins()) {
                addCustomLayers(EntityType.PLAYER, (PlayerRenderer) event.getSkin(skinName), event.getContext().getModelManager());
            }
            //Add our own custom armor layer to everything that has an armor layer
            //Note: This includes any modded mobs that have vanilla's BipedArmorLayer added to them
            for (Map.Entry<EntityType<?>, EntityRenderer<?>> entry : Minecraft.getInstance().getEntityRenderDispatcher().renderers.entrySet()) {
                EntityRenderer<?> renderer = entry.getValue();
                if (renderer instanceof LivingEntityRenderer) {
                    EntityType<?> entityType = entry.getKey();
                    //noinspection unchecked,rawtypes
                    addCustomLayers(entityType, event.getRenderer((EntityType) entityType), event.getContext().getModelManager());
                }
            }
        }

        private static <T extends LivingEntity, M extends HumanoidModel<T>> void addCustomLayers(EntityType<?> type, @Nullable LivingEntityRenderer<T, M> renderer, ModelManager modelManager) {
            if (renderer == null || !MixinStron.isInitialized()
            ) {
                return;
            }
            HumanoidArmorLayer<T, M, ?> bipedArmorLayer = null;
            for (RenderLayer<T, M> layerRenderer : (List<RenderLayer<T, M>>) (Object) MixinStron.layers) {
                //Validate against the layer render being null, as it seems like some mods do stupid things and add in null layers
                if (layerRenderer != null) {
                    //Only allow an exact class match, so we don't add to modded entities that only have a modded extended armor or elytra layer
                    Class<?> layerClass = layerRenderer.getClass();
                    if (layerClass == HumanoidArmorLayer.class) {
                        bipedArmorLayer = (HumanoidArmorLayer<T, M, ?>) layerRenderer;
                    }
                    if (!isNull(bipedArmorLayer)) {
                        break;
                    }
                }
            }
            if (bipedArmorLayer != null) {
                renderer.addLayer(
                        new MJSHumanoidArmorLayer<>(
                                renderer,
                                (HumanoidModel<T>) MixinStron.innerModel,
                                (HumanoidModel<T>) MixinStron.outerModel,
                                modelManager
                        )
                );
            }
        }

        @SubscribeEvent
        public static void onModelBaked(ModelEvent.ModifyBakingResult event){
            // wrench item model
            Map<ResourceLocation, BakedModel> modelRegistry = event.getModels();
            var map2set = mjs$customRendererMap.entrySet();
            map2set.forEach(entry -> {
                var id = entry.getKey();
                var isCustomRenderer = entry.getValue();
                if (!isCustomRenderer) return;
                ModelResourceLocation location = new ModelResourceLocation(BuiltInRegistries.ITEM.getKey(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(id)).asItem()), "inventory");
                BakedModel existingModel = modelRegistry.get(location);
                if (!(existingModel == null || existingModel instanceof MJSBakeModel)) {
                    MJSBakeModel obsidianWrenchBakedModel = new MJSBakeModel(existingModel);
                    event.getModels().put(location, obsidianWrenchBakedModel);
                }
            });
        }

        @SubscribeEvent
        public static void onImcQueue(InterModEnqueueEvent event) {
            if (Platform.isModLoaded("mekanism") && !isNull(hooks)) {

                ((MekanismHooks) hooks).sendIMCMessages(event);
                Set<KubeJSModuleDataBuilder> a = getAllModuleDataBuilder();
                a.forEach(b -> {
                    UnitItemSlots.Slots s  = b.slot;
                    ResourceLocation id = b.id;
                    var m  = getModuleById(id);
                    switch (s) {
                        case ALL -> MekanismIMC.addModulesToAll(m);
                        case MEK_TOOL -> MekanismIMC.addMekaToolModules(m);
                        case MEK_SUIT_HELMET -> MekanismIMC.addMekaSuitHelmetModules(m);
                        case MEK_SUIT_BODY -> MekanismIMC.addMekaSuitBodyarmorModules(m);
                        case MEK_SUIT_PANTS -> MekanismIMC.addMekaSuitPantsModules(m);
                        case MEK_SUIT_BOOTS -> MekanismIMC.addMekaSuitBootsModules(m);
                    }
                });
            }
        }
    }
}
