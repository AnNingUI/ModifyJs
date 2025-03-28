package com.anningui.modifyjs.mod_adder.mek.custom.module;


import com.anningui.modifyjs.callback.CustomInterface;
import com.anningui.modifyjs.mod_adder.mek.MJSMekKubeJSPlugin;
import com.anningui.modifyjs.mod_adder.mek.custom.item.KubeJSUnitItemBuilder;
import com.anningui.modifyjs.mod_adder.mek.util.UnitItemSlots;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import mekanism.api.functions.TriConsumer;
import mekanism.api.gear.ICustomModule;
import mekanism.api.gear.IHUDElement;
import mekanism.api.gear.IModule;
import mekanism.api.gear.ModuleData;
import mekanism.api.gear.config.ModuleConfigItemCreator;
import mekanism.api.providers.IItemProvider;
import mekanism.api.radial.RadialData;
import mekanism.api.radial.mode.IRadialMode;
import mekanism.api.radial.mode.NestedRadialMode;
import net.minecraft.core.BlockSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import oshi.util.Memoizer;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.*;

public class KubeJSModuleDataBuilder extends AKubeJSModuleDataBuilder<KubeJSModuleData, ModuleData.ModuleDataBuilder<KubeJSModuleData>, KubeJSModuleDataBuilder>{

    public KubeJSModuleCallback moduleCallback;

    public int maxStackSize = 1;
    public Rarity rarity = Rarity.COMMON;
    public int exclusive;
    public boolean handlesModeChange = false;
    public boolean modeChangeDisabledByDefault = false;
    public boolean rendersHUD = false;
    public boolean noDisable = false;
    public boolean disabledByDefault = false;

    public UnitItemSlots.Slots slot;

    private static final Set<KubeJSModuleDataBuilder> allBuilder = new HashSet<>();

    public static Set<KubeJSModuleDataBuilder> getAllModuleDataBuilder() {
        return allBuilder;
    }

    public KubeJSModuleDataBuilder maxStackSize(int i) {
        this.maxStackSize = i;
        return this;
    }

    public KubeJSModuleDataBuilder setExclusiveByFlag(ModuleData.ExclusiveFlag... flags) {
        this.exclusive = flags.length == 0 ? ModuleData.ExclusiveFlag.ANY : ModuleData.ExclusiveFlag.getCompoundMask(flags);
        return this;
    }

    public KubeJSModuleDataBuilder rarity(Rarity r) {
        this.rarity = r;
        return this;
    }

    public KubeJSModuleDataBuilder setSlot(UnitItemSlots.Slots slot) {
        this.slot = slot;
        return this;
    }

    public KubeJSModuleDataBuilder exclusive(int i) {
        this.exclusive = i;
        return this;
    }

    public KubeJSModuleDataBuilder handlesModeChange(boolean b) {
        this.handlesModeChange = b;
        return this;
    }

    public KubeJSModuleDataBuilder modeChangeDisabledByDefault(boolean b) {
        this.modeChangeDisabledByDefault = b;
        return this;
    }

    public KubeJSModuleDataBuilder rendersHUD(boolean b) {
        this.rendersHUD = b;
        return this;
    }

    public KubeJSModuleDataBuilder noDisable(boolean b) {
        this.noDisable = b;
        return this;
    }

    public KubeJSModuleDataBuilder disabledByDefault(boolean b) {
        this.disabledByDefault = b;
        return this;
    }


    public KubeJSModuleDataBuilder(ResourceLocation i) {
        super(i);
        this.moduleCallback = new KubeJSModuleCallback();
        allBuilder.add(this);
    }

    public KubeJSModuleDataBuilder(KubeJSUnitItemBuilder b) {
        super(b.id);
        this.moduleCallback              = b.moduleCallback;
        this.maxStackSize                = b.maxModuleSize;
        this.rarity                      = b.rarity;
        this.exclusive                   = b.exclusive;
        this.handlesModeChange           = b.handlesModeChange;
        this.modeChangeDisabledByDefault = b.modeChangeDisabledByDefault;
        this.rendersHUD                  = b.rendersHUD;
        this.slot                        = b.slot;
        allBuilder.add(this);
    }

    public static KubeJSModuleDataBuilder create(ResourceLocation id) {
        return new KubeJSModuleDataBuilder(id);
    }

    public static KubeJSModuleDataBuilder create(KubeJSUnitItemBuilder builder) {
        return new KubeJSModuleDataBuilder(builder);
    }

    @Override
    protected Supplier bindBuilder() {
        UnaryOperator<ModuleData.ModuleDataBuilder<KubeJSModuleData>> a = builder -> {
            var bb = builder.rarity(rarity)
                    .maxStackSize(maxStackSize);
            if (exclusive != 0) {
                bb = bb.exclusive(exclusive);
            }
            if (handlesModeChange) {
                bb = bb.handlesModeChange();
            }
            if (modeChangeDisabledByDefault) {
                bb = bb.modeChangeDisabledByDefault();
            }
            if (rendersHUD) {
                bb = bb.rendersHUD();
            }
            if (noDisable) {
                bb = bb.noDisable();
            }
            if (disabledByDefault) {
                bb = bb.disabledByDefault();
            }
            return bb;
        };
        NonNullSupplier<KubeJSModuleData> s = this::createObject;
        IItemProvider i = () -> Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(id)).asItem();
        var e = ModuleData.ModuleDataBuilder.custom(s, i);
        return () -> a.apply(e);
    }

    @Override
    public RegistryInfo getRegistryType() {
        return MJSMekKubeJSPlugin.MODULE_DATA;
    }


    public KubeJSModuleDataBuilder init(BiFunction<IModule<KubeJSModuleData>, ModuleConfigItemCreator, Void> cBInit) {
        this.moduleCallback.initCallback = cBInit;
        return this;
    }

    public KubeJSModuleDataBuilder tickServer(BiFunction<IModule<KubeJSModuleData>, Player, Void> tickServerCallback) {
        this.moduleCallback.tickServerCallback = tickServerCallback;
        return this;
    }

    public KubeJSModuleDataBuilder tickClient(BiFunction<IModule<KubeJSModuleData>, Player, Void> tickClientCallback) {
        this.moduleCallback.tickClientCallback = tickClientCallback;
        return this;
    }

    public KubeJSModuleDataBuilder addHUDStrings(
            TriConsumer<IModule<KubeJSModuleData>, Player, Consumer<Component>> addHUDStringsCallback) {
        this.moduleCallback.addHUDStringsCallback = addHUDStringsCallback;
        return this;
    }

    public KubeJSModuleDataBuilder addHUDElements(
            TriConsumer<IModule<KubeJSModuleData>, Player, Consumer<IHUDElement>> addHUDElementsCallback) {
        this.moduleCallback.addHUDElementsCallback = addHUDElementsCallback;
        return this;
    }

    public KubeJSModuleDataBuilder changeMode(
            CustomInterface.KQuintConsumer<IModule<KubeJSModuleData>, Player, ItemStack, Integer, Boolean, Void> changeModeCallback) {
        this.moduleCallback.changeModeCallback = changeModeCallback;
        return this;
    }

    public KubeJSModuleDataBuilder canChangeModeWhenDisabled(Function<IModule<KubeJSModuleData>, Boolean> canChangeModeWhenDisabledCallback) {
        this.moduleCallback.canChangeModeWhenDisabledCallback = canChangeModeWhenDisabledCallback;
        return this;
    }

    public KubeJSModuleDataBuilder canChangeRadialModeWhenDisabled(Function<IModule<KubeJSModuleData>, Boolean> canChangeRadialModeWhenDisabledCallback) {
        this.moduleCallback.canChangeRadialModeWhenDisabledCallback = canChangeRadialModeWhenDisabledCallback;
        return this;
    }

    public KubeJSModuleDataBuilder getModeScrollComponent(BiFunction<IModule<KubeJSModuleData>, ItemStack, Component> getModeScrollComponentCallback) {
        this.moduleCallback.getModeScrollComponentCallback = getModeScrollComponentCallback;
        return this;
    }

    public KubeJSModuleDataBuilder addRadialModes(CustomInterface.KTriConsumer<IModule<KubeJSModuleData>, ItemStack, Consumer<NestedRadialMode>, Void> addRadialModesCallback) {
        this.moduleCallback.addRadialModesCallback = addRadialModesCallback;
        return this;
    }

    public KubeJSModuleDataBuilder getMode(CustomInterface.KTriConsumer<IModule<KubeJSModuleData>, ItemStack, RadialData<? extends IRadialMode>, ? extends IRadialMode> getModeCallback) {
        this.moduleCallback.getModeCallback = getModeCallback;
        return this;
    }

    public KubeJSModuleDataBuilder setMode(CustomInterface.KQuintConsumer<IModule<KubeJSModuleData>, Player, ItemStack, RadialData<? extends IRadialMode>, ? extends IRadialMode, Boolean> setModeCallback) {
        this.moduleCallback.setModeCallback = setModeCallback;
        return this;
    }

    public KubeJSModuleDataBuilder onInteract(CustomInterface.KQuadConsumer<IModule<KubeJSModuleData>, Player, LivingEntity, InteractionHand, InteractionResult> onInteractCallback) {
        this.moduleCallback.onInteractCallback = onInteractCallback;
        return this;
    }

    public KubeJSModuleDataBuilder onDispense(BiFunction<IModule<KubeJSModuleData>, BlockSource, ICustomModule.ModuleDispenseResult> onDispenseCallback) {
        this.moduleCallback.onDispenseCallback = onDispenseCallback;
        return this;
    }

    public KubeJSModuleDataBuilder onAdded(BiFunction<IModule<KubeJSModuleData>, Boolean, Void> onAddedCallback) {
        this.moduleCallback.onAddedCallback = onAddedCallback;
        return this;
    }

    public KubeJSModuleDataBuilder onRemoved(BiFunction<IModule<KubeJSModuleData>, Boolean, Void> onRemovedCallback) {
        this.moduleCallback.onRemovedCallback = onRemovedCallback;
        return this;
    }

    public KubeJSModuleDataBuilder onEnabledStateChange(Function<IModule<KubeJSModuleData>, Void> onEnabledStateChangeCallback) {
        this.moduleCallback.onEnabledStateChangeCallback = onEnabledStateChangeCallback;
        return this;
    }

    public KubeJSModuleDataBuilder getDamageAbsorbInfo(BiFunction<IModule<KubeJSModuleData>, DamageSource, ICustomModule.ModuleDamageAbsorbInfo> getDamageAbsorbInfoCallback) {
        this.moduleCallback.getDamageAbsorbInfoCallback = getDamageAbsorbInfoCallback;
        return this;
    }

    public KubeJSModuleDataBuilder onItemUse(BiFunction<IModule<KubeJSModuleData>, UseOnContext, InteractionResult> onItemUseCallback) {
        this.moduleCallback.onItemUseCallback = onItemUseCallback;
        return this;
    }

    public KubeJSModuleDataBuilder canPerformAction(BiFunction<IModule<KubeJSModuleData>, ToolAction, Boolean> canPerformActionCallback) {
        this.moduleCallback.canPerformActionCallback = canPerformActionCallback;
        return this;
    }

    @Override
    public KubeJSModuleData createObject() {
        return new KubeJSModuleData(builder()) {
            @Override
            public void init(@NotNull IModule<KubeJSModuleData> module, @NotNull ModuleConfigItemCreator configItemCreator) {
                if (moduleCallback.initCallback != null) {
                    moduleCallback.initCallback.apply(module, configItemCreator);
                } else {
                    super.init(module, configItemCreator);
                }
            }

            @Override
            public void tickClient(@NotNull IModule<KubeJSModuleData> module, @NotNull Player player) {
                if (moduleCallback.tickClientCallback!= null) {
                    moduleCallback.tickClientCallback.apply(module, player);
                } else {
                    super.tickClient(module, player);
                }
            }

            @Override
            public void tickServer(@NotNull IModule<KubeJSModuleData> module, @NotNull Player player) {
                if (moduleCallback.tickServerCallback != null) {
                    moduleCallback.tickServerCallback.apply(module, player);
                } else {
                    super.tickServer(module, player);
                }
            }

            @Override
            public void addHUDElements(@NotNull IModule<KubeJSModuleData> module, @NotNull Player player, @NotNull Consumer<IHUDElement> hudElementAdder) {
                if (moduleCallback.addHUDElementsCallback!= null) {
                    moduleCallback.addHUDElementsCallback.accept(module, player, hudElementAdder);
                } else {
                    super.addHUDElements(module, player, hudElementAdder);
                }
            }

            @Override
            public void addHUDStrings(@NotNull IModule<KubeJSModuleData> module, @NotNull Player player, @NotNull Consumer<Component> hudStringAdder) {
                if (moduleCallback.addHUDStringsCallback!= null) {
                    moduleCallback.addHUDStringsCallback.accept(module, player, hudStringAdder);
                } else {
                    super.addHUDStrings(module, player, hudStringAdder);
                }
            }

            @Override
            public boolean canChangeModeWhenDisabled(@NotNull IModule<KubeJSModuleData> module) {
                if (moduleCallback.canChangeModeWhenDisabledCallback != null) {
                    return moduleCallback.canChangeModeWhenDisabledCallback.apply(module);
                } else {
                    return super.canChangeModeWhenDisabled(module);
                }
            }

            @Override
            public boolean canChangeRadialModeWhenDisabled(@NotNull IModule<KubeJSModuleData> module) {
                if (moduleCallback.canChangeRadialModeWhenDisabledCallback != null) {
                    return moduleCallback.canChangeRadialModeWhenDisabledCallback.apply(module);
                } else {
                    return super.canChangeRadialModeWhenDisabled(module);
                }
            }

            @Override
            public Component getModeScrollComponent(@NotNull IModule<KubeJSModuleData> module, @NotNull ItemStack stack) {
                if (moduleCallback.getModeScrollComponentCallback != null) {
                    return moduleCallback.getModeScrollComponentCallback.apply(module, stack);
                } else {
                    return super.getModeScrollComponent(module, stack);
                }
            }

            @Override
            public void changeMode(@NotNull IModule<KubeJSModuleData> module, @NotNull Player player, @NotNull ItemStack stack, int shift, boolean displayChangeMessage) {
                if (moduleCallback.changeModeCallback != null) {
                    moduleCallback.changeModeCallback.apply(module, player, stack, shift, displayChangeMessage);
                } else {
                    super.changeMode(module, player, stack, shift, displayChangeMessage);
                }
            }



            @Override
            public void addRadialModes(@NotNull IModule<KubeJSModuleData> module, @NotNull ItemStack stack, @NotNull Consumer<NestedRadialMode> radialModeAdder) {
                if (moduleCallback.addRadialModesCallback != null) {
                    moduleCallback.addRadialModesCallback.apply(module, stack, radialModeAdder);
                } else {
                    super.addRadialModes(module, stack, radialModeAdder);
                }
            }

            @Override
            public <MODE extends IRadialMode> @Nullable MODE getMode(@NotNull IModule<KubeJSModuleData> module, @NotNull ItemStack stack, @NotNull RadialData<MODE> radialData) {
                if (moduleCallback.getModeCallback != null) {
                    return (MODE) moduleCallback.getModeCallback.apply(module, stack, radialData);
                } else {
                    return super.getMode(module, stack, radialData);
                }
            }

            @Override
            public <MODE extends IRadialMode> boolean setMode(@NotNull IModule<KubeJSModuleData> module, @NotNull Player player, @NotNull ItemStack stack, @NotNull RadialData<MODE> radialData, @NotNull MODE mode) {
                 CustomInterface.KQuintConsumer<IModule<KubeJSModuleData>, Player, ItemStack, RadialData<MODE>, MODE, Boolean> u = (CustomInterface.KQuintConsumer) moduleCallback.setModeCallback;
                if (moduleCallback.setModeCallback != null) {
                    return u.apply(module, player, stack, radialData, mode);
                } else {
                    return super.setMode(module, player, stack, radialData, mode);
                }
            }

            @Override
            public @NotNull InteractionResult onInteract(@NotNull IModule<KubeJSModuleData> module, @NotNull Player player, @NotNull LivingEntity target, @NotNull InteractionHand hand) {
                if (moduleCallback.onInteractCallback != null) {
                    return moduleCallback.onInteractCallback.apply(module, player, target, hand);
                } else {
                    return super.onInteract(module, player, target, hand);
                }
            }

            @Override
            public @NotNull ICustomModule.ModuleDispenseResult onDispense(@NotNull IModule<KubeJSModuleData> module, @NotNull BlockSource source) {
                if (moduleCallback.onDispenseCallback != null) {
                    return moduleCallback.onDispenseCallback.apply(module, source);
                } else {
                    return super.onDispense(module, source);
                }
            }

            @Override
            public void onAdded(@NotNull IModule<KubeJSModuleData> module, boolean stackChange) {
                if (moduleCallback.onAddedCallback != null) {
                    moduleCallback.onAddedCallback.apply(module, stackChange);
                } else {
                    super.onAdded(module, stackChange);
                }
            }

            @Override
            public void onRemoved(@NotNull IModule<KubeJSModuleData> module, boolean stackChange) {
                if (moduleCallback.onRemovedCallback != null) {
                    moduleCallback.onRemovedCallback.apply(module, stackChange);
                } else {
                    super.onRemoved(module, stackChange);
                }
            }

            @Override
            public void onEnabledStateChange(@NotNull IModule<KubeJSModuleData> module) {
                if (moduleCallback.onEnabledStateChangeCallback != null) {
                    moduleCallback.onEnabledStateChangeCallback.apply(module);
                } else {
                    super.onEnabledStateChange(module);
                }
            }

            @Override
            public @NotNull ICustomModule.ModuleDamageAbsorbInfo getDamageAbsorbInfo(@NotNull IModule<KubeJSModuleData> module, @NotNull DamageSource source) {
                if (moduleCallback.getDamageAbsorbInfoCallback != null) {
                    return moduleCallback.getDamageAbsorbInfoCallback.apply(module, source);
                } else {
                    return super.getDamageAbsorbInfo(module, source);
                }
            }

            @Override
            public @NotNull InteractionResult onItemUse(@NotNull IModule<KubeJSModuleData> module, @NotNull UseOnContext context) {
                if (moduleCallback.onItemUseCallback != null) {
                    return moduleCallback.onItemUseCallback.apply(module, context);
                } else {
                    return super.onItemUse(module, context);
                }
            }

            @Override
            public boolean canPerformAction(@NotNull IModule<KubeJSModuleData> module, @NotNull ToolAction action) {
                if (moduleCallback.canPerformActionCallback != null) {
                    return moduleCallback.canPerformActionCallback.apply(module, action);
                } else {
                    return super.canPerformAction(module, action);
                }
            }
        };
    }
}


abstract class AKubeJSModuleDataBuilder<C extends ModuleData<C> & ICustomModule<C>,
        B extends ModuleData.ModuleDataBuilder<C>,
        S extends AKubeJSModuleDataBuilder<C, B, S>> extends BuilderBase<C> {
    protected AKubeJSModuleDataBuilder(ResourceLocation i) {
        super(i);
        builder = Memoizer.memoize(bindBuilder());
    }

    private final Supplier<B> builder;

    protected abstract Supplier<B> bindBuilder();
    protected final B builder() {
        return builder.get();
    }



    @SuppressWarnings("unchecked")
    protected S self() {
        return (S) this;
    }
}