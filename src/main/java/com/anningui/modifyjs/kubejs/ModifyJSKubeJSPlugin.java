package com.anningui.modifyjs.kubejs;

import com.anningui.modifyjs.builder.MJSItemBuilder;
import com.anningui.modifyjs.util.js_long.TryCatchPipe;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;

import static dev.latvian.mods.kubejs.registry.RegistryInfo.ITEM;

public class ModifyJSKubeJSPlugin extends KubeJSPlugin {
    /* Basic example of a KubeJS Plugin.
       To register your own plugins, add this class and package name to "kubejs.plugins.txt" in your Resources directory.
    */

    @Override
    public void init() {
        super.init();
        ITEM.addType("mjs_item", MJSItemBuilder.class, MJSItemBuilder::new);
    }

    @Override
    public void registerBindings(BindingsEvent event) {
        event.add("TryCatchPipe", TryCatchPipe.class);
    }
}

