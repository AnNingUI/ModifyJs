package com.anningui.modifyjs.mixin;

import com.anningui.modifyjs.builder.MJSItemBuilder;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.item.ItemModificationEventJS;
import dev.latvian.mods.kubejs.item.custom.BasicItemJS;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.function.Consumer;
import java.util.function.Function;

import static com.anningui.modifyjs.ModifyJS.mjs$customRendererMap;

@Mixin(ItemModificationEventJS.class)
public abstract class ItemModificationEventJSMixin extends EventJS {

    public void modifyItemBuilder(Ingredient in, Function<ItemBuilder, ItemBuilder> c) {
        modify(in, (item) -> {
            var itemBuilder = new BasicItemJS.Builder(item.kjs$getIdLocation());
            var b = c.apply(itemBuilder);
            item.kjs$setItemBuilder(b);
        });
    }

    @Shadow
    public abstract void modify(Ingredient in, Consumer<Item> c);
}
