package com.anningui.modifyjs.builder.item.armor;

import com.anningui.modifyjs.builder.item.RenderItemBuilder;
import com.anningui.modifyjs.callback.ArmorLayerContext;
import com.anningui.modifyjs.render.item.KJSClientItemExtensions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.item.MutableArmorTier;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.*;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static java.util.Objects.isNull;

public class RenderArmorItem extends ArmorItem {
    private final Multimap<ResourceLocation, AttributeModifier> attributes;
    private final Builder builder;
    public RenderArmorItem(ArmorMaterial material, Type type, Properties properties, Multimap<ResourceLocation, AttributeModifier> attributes, Builder builder) {
        super(material, type, properties);
        this.attributes = attributes;
        this.builder = builder;
    }
    private boolean modified = false;
    {defaultModifiers = ArrayListMultimap.create(defaultModifiers);}

    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot equipmentSlot) {
        if (!modified) {
            modified = true;
            attributes.forEach((r, m) -> defaultModifiers.put(RegistryInfo.ATTRIBUTE.getValue(r), m));
        }
        return super.getDefaultAttributeModifiers(equipmentSlot);
    }

    @Override
    public @Nullable Builder kjs$getItemBuilder() {
        return builder;
    }

    public static class Builder extends RenderItemBuilder {
        @HideFromJS
        public static Map<ResourceLocation, Builder> instances = new HashMap<>();
        public final ArmorItem.Type armorType;
        public MutableArmorTier armorTier;
        @HideFromJS
        public boolean noShowArmorModel = false;

        public static Map<Builder, Consumer<ArmorLayerContext>> allArmorLayers = new HashMap<>();

        public Builder(ResourceLocation i, ArmorItem.Type t) {
            super(i);
            armorType = t;
            armorTier = new MutableArmorTier(id.toString(), ArmorMaterials.IRON);
            unstackable();
            instances.put(i, this);
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

        public Builder noDefaultRender() {
            this.noShowArmorModel = true;
            return this;
        }

        @Override
        public Item createObject() {
            if (mjs$isCustomRenderer && !isNull(mjs$renderByItemCallback)) {
                return new RenderArmorItem(armorTier, armorType, createItemProperties(), attributes, this) {

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
                return new RenderArmorItem(armorTier, armorType, createItemProperties(), attributes, this);
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
