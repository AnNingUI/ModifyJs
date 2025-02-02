package com.anningui.modifyjs.kubejs;

import com.anningui.modifyjs.ModifyJs;
import dev.latvian.mods.kubejs.KubeJSPlugin;

public class ModifyJsKubeJSPlugin extends KubeJSPlugin {
    /* Basic example of a KubeJS Plugin.
       To register your own plugins, add this class and package name to "kubejs.plugins.txt" in your Resources directory.
    */

    @Override
    public void init() {
        ModifyJs.LOGGER.info("This is my KubeJS Plugin!");
        /** If you don't know how to add content, use Kube's built-in Plugin for reference.
        @see dev.latvian.mods.kubejs.BuiltinKubeJSPlugin
         */
    }
}

