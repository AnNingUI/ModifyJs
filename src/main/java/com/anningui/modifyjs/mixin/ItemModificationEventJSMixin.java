package com.anningui.modifyjs.mixin;

import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.item.ItemModificationEventJS;
import dev.latvian.mods.kubejs.item.custom.BasicItemJS;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Consumer;
import java.util.function.Function;

@Mixin(ItemModificationEventJS.class)
public abstract class ItemModificationEventJSMixin extends EventJS {

    public void modifyItemBuilder(Ingredient in, Function<ItemBuilder, ItemBuilder> c) {
        modify(in, (item) -> {
            var itemBuilder = new BasicItemJS.Builder(item.kjs$getIdLocation());
            var b = c.apply(itemBuilder);
            item.kjs$setItemBuilder(b);
        });
    }

    @Shadow(remap = false)
    public abstract void modify(Ingredient in, Consumer<Item> c);
}
