package com.anningui.modifyjs.builder.item;

import dev.latvian.mods.kubejs.item.MutableToolTier;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;

import java.util.function.Consumer;

public class RenderHandheldItemBuilder extends RenderItemBuilder {
    public transient MutableToolTier toolTier;
    public transient float attackDamageBaseline;
    public transient float speedBaseline;

    public RenderHandheldItemBuilder(ResourceLocation i, float d, float s) {
        super(i);
        toolTier = new MutableToolTier(Tiers.IRON);
        attackDamageBaseline = d;
        speedBaseline = s;
        parentModel("minecraft:item/handheld");
        unstackable();
    }

    public RenderHandheldItemBuilder tier(Tier t) {
        toolTier = t instanceof MutableToolTier mtt ? mtt : new MutableToolTier(t);
        return this;
    }

    @Info("""
		Sets the base attack damage of the tool. Different tools have different baselines.
					
		For example, a sword has a baseline of 3, while an axe has a baseline of 6.
					
		The actual damage is the sum of the baseline and the attackDamageBonus from tier.
		""")
    public RenderHandheldItemBuilder attackDamageBaseline(float f) {
        attackDamageBaseline = f;
        return this;
    }

    @Info("""
		Sets the base attack speed of the tool. Different tools have different baselines.
					
		For example, a sword has a baseline of -2.4, while an axe has a baseline of -3.1.
					
		The actual speed is the sum of the baseline and the speed from tier + 4 (bare hand).
		""")
    public RenderHandheldItemBuilder speedBaseline(float f) {
        speedBaseline = f;
        return this;
    }

    @Info("Modifies the tool tier.")
    public RenderHandheldItemBuilder modifyTier(Consumer<MutableToolTier> callback) {
        callback.accept(toolTier);
        return this;
    }

    @Info("Sets the attack damage bonus of the tool.")
    public RenderHandheldItemBuilder attackDamageBonus(float f) {
        toolTier.setAttackDamageBonus(f);
        return this;
    }

    @Info("Sets the attack speed of the tool.")
    public RenderHandheldItemBuilder speed(float f) {
        toolTier.setSpeed(f);
        return this;
    }
}
