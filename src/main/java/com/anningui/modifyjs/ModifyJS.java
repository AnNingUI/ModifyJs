package com.anningui.modifyjs;

import com.anningui.modifyjs.util.js_long.SwitchMap;
import dev.architectury.platform.Platform;
import mekanism.common.integration.MekanismHooks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;


@Mod(ModifyJS.ID)
public class ModifyJS {

    // You really don't need any of the mumbo-jumbo found here in other mods. Just the ID and Logger.
    public static final String ID = "modifyjs";

    public static final Logger LOGGER = LogManager.getLogger(ID);

    public static Map<ResourceLocation, Boolean> mjs$customRendererMap = new HashMap<>();

    public static Object hooks;

    public ModifyJS() {
        if (Platform.isModLoaded("mekanism")) {
            hooks = new MekanismHooks();
        }
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ModEventBus {
        public static EquipmentSlot getArmorSlot(ArmorItem.Type type) {
            SwitchMap<ArmorItem.Type, EquipmentSlot> sm = SwitchMap.ofEnum(
                    SwitchMap.defOf((e) -> null),
                    SwitchMap.caseOf(ArmorItem.Type.HELMET    , EquipmentSlot.HEAD),
                    SwitchMap.caseOf(ArmorItem.Type.CHESTPLATE, EquipmentSlot.CHEST),
                    SwitchMap.caseOf(ArmorItem.Type.LEGGINGS  , EquipmentSlot.LEGS),
                    SwitchMap.caseOf(ArmorItem.Type.BOOTS     , EquipmentSlot.FEET)
            );
            return sm.get(type);
        }
    }
}
