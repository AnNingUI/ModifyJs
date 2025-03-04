package com.anningui.modifyjs.kubejs;

import com.anningui.modifyjs.builder.item.RenderItemBuilder;
import com.anningui.modifyjs.builder.item.RenderRecordItem;
import com.anningui.modifyjs.builder.item.armor.RenderArmorItem;
import com.anningui.modifyjs.builder.item.tool.*;
import com.anningui.modifyjs.kubejs.event.MJSModelEvents;
import com.anningui.modifyjs.kubejs.event.ModelRegisterAdditional;
import com.anningui.modifyjs.util.js_long.SwitchMap;
import com.anningui.modifyjs.util.render.MJSRenderUtils;
import com.anningui.modifyjs.util.js_long.TryCatchPipe;
import dev.architectury.platform.Platform;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;

import static dev.latvian.mods.kubejs.registry.RegistryInfo.ITEM;

public class ModifyJSKubeJSPlugin extends KubeJSPlugin {
    /* Basic example of a KubeJS Plugin.
       To register your own plugins, add this class and package name to "kubejs.plugins.txt" in your Resources directory.
    */

    @Override
    public void init() {
        super.init();
        // 使用旧版命名mjs_item来符合旧版
        ITEM.addType("mjs_item"         , RenderItemBuilder.BasicItemBuilder.class, RenderItemBuilder.BasicItemBuilder::new);

        ITEM.addType("render_basic"     , RenderItemBuilder.BasicItemBuilder.class, RenderItemBuilder.BasicItemBuilder::new);
        ITEM.addType("render_sword"     , RenderSwordItem.Builder.class, RenderSwordItem.Builder::new);
        ITEM.addType("render_pickaxe"   , RenderPickaxeItem.Builder.class, RenderPickaxeItem.Builder::new);
        ITEM.addType("render_axe"       , RenderAxeItem.Builder.class, RenderAxeItem.Builder::new);
        ITEM.addType("render_shovel"    , RenderShovelItem.Builder.class, RenderShovelItem.Builder::new);
        ITEM.addType("render_shears"    , RenderShearsItem.Builder.class, RenderShearsItem.Builder::new);
        ITEM.addType("render_hoe"       , RenderHoeItem.Builder.class, RenderHoeItem.Builder::new);
        ITEM.addType("render_helmet"    , RenderArmorItem.Helmet.class, RenderArmorItem.Helmet::new);
        ITEM.addType("render_chestplate", RenderArmorItem.Chestplate.class, RenderArmorItem.Chestplate::new);
        ITEM.addType("render_leggings"  , RenderArmorItem.Leggings.class, RenderArmorItem.Leggings::new);
        ITEM.addType("render_boots"     , RenderArmorItem.Boots.class, RenderArmorItem.Boots::new);
        ITEM.addType("render_music_disc", RenderRecordItem.Builder.class, RenderRecordItem.Builder::new);
    }

    @Override
    public void initStartup() {
        MJSModelEvents.REGISTER_ADDER.post(new ModelRegisterAdditional());
    }

    @Override
    public void registerEvents() {
        MJSModelEvents.GROUP.register();
    }

    @Override
    public void registerBindings(BindingsEvent event) {
        event.add("TryCatchPipe", TryCatchPipe.class);
        event.add("SwitchMap", SwitchMap.class);
        event.add("MJSRenderUtils", MJSRenderUtils.class);
        event.add("ModelResourceLocation", ModelResourceLocation.class);
        if (event.getType().isClient() && Platform.isModLoaded("eventjs")) {
            event.add("RenderPlayerEvent$Pre", RenderPlayerEvent.Pre.class);
            event.add("RenderPlayerEvent$Post", RenderPlayerEvent.Post.class);
            event.add("RenderLivingEvent$Pre", RenderLivingEvent.Pre.class);
            event.add("RenderLivingEvent$Post", RenderLivingEvent.Post.class);
        }
    }
}

