package com.anningui.modifyjs.builder.item.tool;

import com.anningui.modifyjs.builder.item.RenderHandheldItemBuilder;
import com.anningui.modifyjs.render.item.KJSClientItemExtensions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static java.util.Objects.isNull;

public class RenderSwordItem extends SwordItem {
    private final Multimap<ResourceLocation, AttributeModifier> attributes;
    private boolean modified = false;
    {defaultModifiers = ArrayListMultimap.create(defaultModifiers);}

    public RenderSwordItem(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties, Multimap<ResourceLocation, AttributeModifier> attributes) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
        this.attributes = attributes;
    }

    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot slot) {
        if (!modified) {
            modified = true;
            attributes.forEach((r, m) -> defaultModifiers.put(RegistryInfo.ATTRIBUTE.getValue(r), m));
        }
        return slot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(slot);
    }

    public static class Builder extends RenderHandheldItemBuilder {

        public Builder(ResourceLocation i) {
            super(i, 3F, -2.4F);
        }

        @Override
        public Item createObject() {
            if (mjs$isCustomRenderer && !isNull(mjs$renderByItemCallback)) {
                return new RenderSwordItem(toolTier, (int) attackDamageBaseline, speedBaseline, createItemProperties(), attributes) {
                    @Override
                    public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
                        super.initializeClient(consumer);
                        consumer.accept(new KJSClientItemExtensions(mjs$renderByItemCallback));
                    }
                };
            } else {
                return new RenderSwordItem(toolTier, (int) attackDamageBaseline, speedBaseline, createItemProperties(), attributes);
            }
        }
    }
}
