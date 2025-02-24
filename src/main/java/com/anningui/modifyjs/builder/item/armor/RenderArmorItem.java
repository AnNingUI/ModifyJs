package com.anningui.modifyjs.builder.item.armor;

import com.anningui.modifyjs.builder.item.RenderItemBuilder;
import com.anningui.modifyjs.callback.ArmorLayerContext;
import com.anningui.modifyjs.render.item.KJSClientItemExtensions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import dev.latvian.mods.kubejs.item.MutableArmorTier;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.*;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static java.util.Objects.isNull;

public class RenderArmorItem extends ArmorItem {
    private boolean modified = false;
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    private final Multimap<ResourceLocation, AttributeModifier> attributes;

    public RenderArmorItem(ArmorMaterial material, Type type, Properties properties, Multimap<ResourceLocation, AttributeModifier> attributes) {
        super(material, type, properties);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        this.defaultModifiers = ArrayListMultimap.create(builder.build());
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

    public static class Builder extends RenderItemBuilder {
        public final ArmorItem.Type armorType;
        public MutableArmorTier armorTier;

        public static Map<Builder, Consumer<ArmorLayerContext>> allArmorLayers = new HashMap<>();

        public Builder(ResourceLocation i, ArmorItem.Type t) {
            super(i);
            armorType = t;
            armorTier = new MutableArmorTier(id.toString(), ArmorMaterials.IRON);
            unstackable();
        }

        public Builder tier(ArmorMaterial t) {
            armorTier = new MutableArmorTier(t.getName(), t);
            return this;
        }

        public Builder modifyTier(Consumer<MutableArmorTier> callback) {
            callback.accept(armorTier);
            return this;
        }

        public Builder addLayerRender(Consumer<ArmorLayerContext> callback) {
            allArmorLayers.put(this, callback);
            return this;
        }

        @Override
        public Item createObject() {
            if (mjs$isCustomRenderer && !isNull(mjs$renderByItemCallback)) {
                return new RenderArmorItem(armorTier, armorType, createItemProperties(), attributes) {
                    @Override
                    public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
                        super.initializeClient(consumer);
                        consumer.accept(new KJSClientItemExtensions(mjs$renderByItemCallback) {
                            @Override
                            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                                return super.getHumanoidArmorModel(livingEntity, itemStack, equipmentSlot, original);
                            }
                        });
                    }
                };
            } else {
                return new RenderArmorItem(armorTier, armorType, createItemProperties(), attributes);
            }
        }
    }

    public static class Helmet extends Builder {
        public Helmet(ResourceLocation i) {
            super(i, ArmorItem.Type.HELMET);
        }
    }

    public static class Chestplate extends Builder {
        public Chestplate(ResourceLocation i) {
            super(i, ArmorItem.Type.CHESTPLATE);
        }

    }

    public static class Leggings extends Builder {

        public Leggings(ResourceLocation i) {
            super(i, ArmorItem.Type.LEGGINGS);
        }
    }

    public static class Boots extends Builder {
        public Boots(ResourceLocation i) {
            super(i, ArmorItem.Type.BOOTS);
        }

    }
}
