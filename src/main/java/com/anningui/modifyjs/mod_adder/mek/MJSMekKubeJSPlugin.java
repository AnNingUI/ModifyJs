package com.anningui.modifyjs.mod_adder.mek;

import com.anningui.modifyjs.mod_adder.mek.custom.item.KubeJSUnitItemBuilder;
import com.anningui.modifyjs.mod_adder.mek.util.KubeJSModuleUtils;
import com.anningui.modifyjs.mod_adder.mek.util.UnitItemSlots;
import dev.architectury.platform.Platform;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import mekanism.api.MekanismAPI;
import mekanism.api.gear.ICustomModule;
import mekanism.api.gear.ModuleData;

import static dev.latvian.mods.kubejs.registry.RegistryInfo.ITEM;
import static java.util.Objects.isNull;

public class MJSMekKubeJSPlugin extends KubeJSPlugin {
    public static final RegistryInfo MODULE_DATA = Platform.isModLoaded("mekanism") ? RegistryInfo.of(MekanismAPI.MODULE_REGISTRY_NAME) : null;

    @Override
    public void init() {
        if (Platform.isModLoaded("mekanism")) {
            ITEM.addType("mek_unit", KubeJSUnitItemBuilder.class, KubeJSUnitItemBuilder::new);
        }
    }

    @Override
    public void registerBindings(BindingsEvent event) {
        if (Platform.isModLoaded("mekanism") && !isNull(MODULE_DATA)) {
            event.add("MekUnitItemSlots", UnitItemSlots.Slots.class);
            event.add("ExclusiveFlag", ModuleData.ExclusiveFlag.class);
            event.add("KJSModuleUtils", KubeJSModuleUtils.class);
            event.add("ModuleDamageAbsorbInfo", ICustomModule.ModuleDamageAbsorbInfo.class);
        }
    }
}
