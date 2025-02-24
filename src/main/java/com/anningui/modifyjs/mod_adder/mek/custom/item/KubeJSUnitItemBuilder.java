package com.anningui.modifyjs.mod_adder.mek.custom.item;


import com.anningui.modifyjs.builder.item.RenderItemBuilder;
import com.anningui.modifyjs.callback.CustomInterface;
import com.anningui.modifyjs.mod_adder.mek.custom.module.KubeJSModuleCallback;
import com.anningui.modifyjs.mod_adder.mek.custom.module.KubeJSModuleData;
import com.anningui.modifyjs.mod_adder.mek.custom.module.KubeJSModuleDataBuilder;
import com.anningui.modifyjs.mod_adder.mek.util.UnitItemSlots;
import com.anningui.modifyjs.render.item.KJSClientItemExtensions;
import cpw.mods.util.Lazy;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import mekanism.api.functions.TriConsumer;
import mekanism.api.gear.ICustomModule;
import mekanism.api.gear.IHUDElement;
import mekanism.api.gear.IModule;
import mekanism.api.gear.ModuleData;
import mekanism.api.gear.config.ModuleConfigItemCreator;
import mekanism.api.providers.IModuleDataProvider;
import mekanism.api.radial.RadialData;
import mekanism.api.radial.mode.IRadialMode;
import mekanism.api.radial.mode.NestedRadialMode;
import net.minecraft.core.BlockSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ToolAction;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.anningui.modifyjs.mod_adder.mek.MJSMekKubeJSPlugin.MODULE_DATA;
import static com.anningui.modifyjs.mod_adder.mek.util.KubeJSMekUntiItemUtils.getModuleById;
import static java.util.Objects.isNull;

public class KubeJSUnitItemBuilder extends RenderItemBuilder {
    public transient UnitItemSlots.Slots slot;

    public transient KubeJSModuleCallback moduleCallback;

    public transient int exclusive;
    public transient boolean rendersHUD;

    public transient int maxModuleSize = 1;
    public transient boolean handlesModeChange;
    public transient boolean modeChangeDisabledByDefault;
    public static Map<ResourceLocation, Spec> specs = new HashMap<>();

    private IModuleDataProvider<?> moduleData;

    public KubeJSUnitItemBuilder(ResourceLocation i) {

        super(i);
        this.moduleCallback = new KubeJSModuleCallback();
    }

    public KubeJSUnitItemBuilder handlesModeChange(boolean handlesModeChange) {
        this.handlesModeChange = handlesModeChange;
        return this;
    };

    public KubeJSUnitItemBuilder modeChangeDisabledByDefault(boolean modeChangeDisabledByDefault) {
        this.modeChangeDisabledByDefault = modeChangeDisabledByDefault;
        return this;
    };

    public KubeJSUnitItemBuilder maxModuleSize(int maxModuleSize) {
        this.maxModuleSize = maxModuleSize;
        return this;
    }

    public KubeJSUnitItemBuilder init(BiFunction<IModule<KubeJSModuleData>, ModuleConfigItemCreator, Void> initCallback) {
        this.moduleCallback.initCallback = initCallback;
        return this;
    }

    public KubeJSUnitItemBuilder tickServer(BiFunction<IModule<KubeJSModuleData>, Player, Void> tickServerCallback) {
        this.moduleCallback.tickServerCallback = tickServerCallback;
        return this;
    }

    public KubeJSUnitItemBuilder tickClient(BiFunction<IModule<KubeJSModuleData>, Player, Void> tickClientCallback) {
        this.moduleCallback.tickClientCallback = tickClientCallback;
        return this;
    }

    public KubeJSUnitItemBuilder addHUDStrings(
            TriConsumer<IModule<KubeJSModuleData>, Player, Consumer<Component>> addHUDStringsCallback) {
        this.moduleCallback.addHUDStringsCallback = addHUDStringsCallback;
        return this;
    }

    public KubeJSUnitItemBuilder addHUDElements(
            TriConsumer<IModule<KubeJSModuleData>, Player, Consumer<IHUDElement>> addHUDElementsCallback) {
        this.moduleCallback.addHUDElementsCallback = addHUDElementsCallback;
        return this;
    }

    public KubeJSUnitItemBuilder changeMode(
            CustomInterface.KQuintConsumer<IModule<KubeJSModuleData>, Player, ItemStack, Integer, Boolean, Void> changeModeCallback) {
        this.moduleCallback.changeModeCallback = changeModeCallback;
        return this;
    }

    public KubeJSUnitItemBuilder canChangeModeWhenDisabled(Function<IModule<KubeJSModuleData>, Boolean> canChangeModeWhenDisabledCallback) {
        this.moduleCallback.canChangeModeWhenDisabledCallback = canChangeModeWhenDisabledCallback;
        return this;
    }

    public KubeJSUnitItemBuilder canChangeRadialModeWhenDisabled(Function<IModule<KubeJSModuleData>, Boolean> canChangeRadialModeWhenDisabledCallback) {
        this.moduleCallback.canChangeRadialModeWhenDisabledCallback = canChangeRadialModeWhenDisabledCallback;
        return this;
    }

    public KubeJSUnitItemBuilder getModeScrollComponent(BiFunction<IModule<KubeJSModuleData>, ItemStack, Component> getModeScrollComponentCallback) {
        this.moduleCallback.getModeScrollComponentCallback = getModeScrollComponentCallback;
        return this;
    }

    public KubeJSUnitItemBuilder addRadialModes(CustomInterface.KTriConsumer<IModule<KubeJSModuleData>, ItemStack, Consumer<NestedRadialMode>, Void> addRadialModesCallback) {
        this.moduleCallback.addRadialModesCallback = addRadialModesCallback;
        return this;
    }

    public KubeJSUnitItemBuilder getMode(CustomInterface.KTriConsumer<IModule<KubeJSModuleData>, ItemStack, RadialData<? extends IRadialMode>, ? extends IRadialMode> getModeCallback) {
        this.moduleCallback.getModeCallback = getModeCallback;
        return this;
    }

    public KubeJSUnitItemBuilder setMode(CustomInterface.KQuintConsumer<IModule<KubeJSModuleData>, Player, ItemStack, RadialData<? extends IRadialMode>, ? extends IRadialMode, Boolean> setModeCallback) {
        this.moduleCallback.setModeCallback = setModeCallback;
        return this;
    }

    public KubeJSUnitItemBuilder onInteract(CustomInterface.KQuadConsumer<IModule<KubeJSModuleData>, Player, LivingEntity, InteractionHand, InteractionResult> onInteractCallback) {
        this.moduleCallback.onInteractCallback = onInteractCallback;
        return this;
    }

    public KubeJSUnitItemBuilder onDispense(BiFunction<IModule<KubeJSModuleData>, BlockSource, ICustomModule.ModuleDispenseResult> onDispenseCallback) {
        this.moduleCallback.onDispenseCallback = onDispenseCallback;
        return this;
    }

    public KubeJSUnitItemBuilder onAdded(BiFunction<IModule<KubeJSModuleData>, Boolean, Void> onAddedCallback) {
        this.moduleCallback.onAddedCallback = onAddedCallback;
        return this;
    }

    public KubeJSUnitItemBuilder onRemoved(BiFunction<IModule<KubeJSModuleData>, Boolean, Void> onRemovedCallback) {
        this.moduleCallback.onRemovedCallback = onRemovedCallback;
        return this;
    }

    public KubeJSUnitItemBuilder onEnabledStateChange(Function<IModule<KubeJSModuleData>, Void> onEnabledStateChangeCallback) {
        this.moduleCallback.onEnabledStateChangeCallback = onEnabledStateChangeCallback;
        return this;
    }

    public KubeJSUnitItemBuilder getDamageAbsorbInfo(BiFunction<IModule<KubeJSModuleData>, DamageSource, ICustomModule.ModuleDamageAbsorbInfo> getDamageAbsorbInfoCallback) {
        this.moduleCallback.getDamageAbsorbInfoCallback = getDamageAbsorbInfoCallback;
        return this;
    }

    public KubeJSUnitItemBuilder onItemUse(BiFunction<IModule<KubeJSModuleData>, UseOnContext, InteractionResult> onItemUseCallback) {
        this.moduleCallback.onItemUseCallback = onItemUseCallback;
        return this;
    }

    public KubeJSUnitItemBuilder canPerformAction(BiFunction<IModule<KubeJSModuleData>, ToolAction, Boolean> canPerformActionCallback) {
        this.moduleCallback.canPerformActionCallback = canPerformActionCallback;
        return this;
    }

    public KubeJSUnitItemBuilder setSlot(UnitItemSlots.Slots slot) {
        this.slot = slot;
        return this;
    }

    public KubeJSUnitItemBuilder setExclusive(int exclusive) {
        this.exclusive = exclusive;
        return this;
    }

    public KubeJSUnitItemBuilder setExclusiveByFlag(ModuleData.ExclusiveFlag... flags) {
        this.exclusive = flags.length == 0 ? ModuleData.ExclusiveFlag.ANY : ModuleData.ExclusiveFlag.getCompoundMask(flags);
        return this;
    }

    public KubeJSUnitItemBuilder setRendersHUD(boolean rendersHUD) {
        this.rendersHUD = rendersHUD;
        return this;
    }

    public KubeJSUnitItemBuilder addMekaSuitModuleModelSpec(EquipmentSlot slotType, Predicate<LivingEntity> isActive) {
        if (isMekaSuit()) {
            specs.put(id, new Spec(
                    id.toString(),
                    Lazy.of(() -> {
                        if (getModuleData() != null) {
                            return getModuleData();
                        } else {
                            return getModuleById(id);
                        }
                    }),
                    slotType,
                    isActive
            ));
        }
        return this;
    }

    @HideFromJS
    public static class Spec {
        public final String name;
        public final Lazy<IModuleDataProvider<?>> moduleDataProvider;
        public final EquipmentSlot slotType;
        public final Predicate<LivingEntity> isActive;

        public Spec(String name, Lazy<IModuleDataProvider<?>> moduleDataProvider, EquipmentSlot slotType, Predicate<LivingEntity> isActive) {
            this.name = name;
            this.moduleDataProvider = moduleDataProvider;
            this.slotType = slotType;
            this.isActive = isActive;
        }
    }

    private boolean isMekaSuit() {
        return slot == UnitItemSlots.Slots.MEK_SUIT_BODY   ||
               slot == UnitItemSlots.Slots.MEK_SUIT_BOOTS  ||
               slot == UnitItemSlots.Slots.MEK_SUIT_HELMET ||
               slot == UnitItemSlots.Slots.MEK_SUIT_PANTS;
    }

    @Override
    public KubeJSUnitItem createObject() {
        if (mjs$isCustomRenderer && !isNull(mjs$renderByItemCallback)) {
            return new KubeJSUnitItem(getModuleData(), this) {
                @Override
                public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
                    super.initializeClient(consumer);
                    consumer.accept(new KJSClientItemExtensions(mjs$renderByItemCallback));
                }
            };
        } else {
            return new KubeJSUnitItem(getModuleData(), this);
        }
    }

    @Override
    public void createAdditionalObjects() {
        super.createAdditionalObjects();
        if (getModuleData() == null) {
            MODULE_DATA.addBuilder(KubeJSModuleDataBuilder.create(this));
        }
    }

    @HideFromJS
    public IModuleDataProvider<?> getModuleData() {
        return moduleData;
    }

    @Info("""
            This method is used to set a ModuleData that already exists,
            or a ModuleData that you have registered separately,
            and will not be able to set other properties.
            """)
    public void setModuleData(ResourceLocation id) {
        this.moduleData = Objects.requireNonNull(getModuleById(id)).getModuleData();
    }


}