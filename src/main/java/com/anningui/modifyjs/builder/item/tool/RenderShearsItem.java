package com.anningui.modifyjs.builder.item.tool;

import com.anningui.modifyjs.builder.item.RenderItemBuilder;
import com.anningui.modifyjs.render.item.KJSClientItemExtensions;
import dev.architectury.platform.Platform;
import dev.latvian.mods.kubejs.item.custom.ShearsItemBuilder;
import net.minecraft.core.dispenser.ShearsDispenseItemBehavior;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static java.util.Objects.isNull;

public class RenderShearsItem extends ShearsItem {
    public final Builder builder;

    public RenderShearsItem(Builder builder) {
        super(builder.createItemProperties());
        this.builder = builder;
    }

    @Override
    public float getDestroySpeed(ItemStack itemStack, BlockState blockState) {
        if (blockState.is(BlockTags.LEAVES)) {
            return 15F;
        } else if (blockState.is(Blocks.COBWEB)) {
            return builder.speedBaseline * 3F;
        } else if (blockState.is(Blocks.VINE) || blockState.is(Blocks.GLOW_LICHEN)) {
            return builder.speedBaseline / 2.5F;
        } else if (blockState.is(BlockTags.WOOL)) {
            return builder.speedBaseline;
        } else {
            return super.getDestroySpeed(itemStack, blockState);
        }
    }

    public static class Builder extends RenderItemBuilder {
        public static boolean isCustomShears(ItemStack stack) {
            return stack.getItem() instanceof ShearsItemBuilder.ShearsItemKJS;
        }

        public static final ResourceLocation TAG = new ResourceLocation(Platform.isForge() ? "forge:shears" : "c:shears");

        public transient float speedBaseline;

        public Builder(ResourceLocation i) {
            super(i);
            speedBaseline(5f);
            parentModel("minecraft:item/handheld");
            unstackable();
            tag(TAG);
        }

        public Builder speedBaseline(float f) {
            speedBaseline = f;
            return this;
        }

        @Override
        public Item createObject() {
            if (mjs$isCustomRenderer && !isNull(mjs$renderByItemCallback)) {
                var item = new RenderShearsItem(this) {
                    @Override
                    public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
                        super.initializeClient(consumer);
                        consumer.accept(new KJSClientItemExtensions(mjs$renderByItemCallback));
                    }
                };
                DispenserBlock.registerBehavior(item, new ShearsDispenseItemBehavior());
                return item;
            } else {
                var item = new RenderShearsItem(this);
                DispenserBlock.registerBehavior(item, new ShearsDispenseItemBehavior());
                return item;
            }
        }
    }
}
