package com.anningui.modifyjs.mixin;

import com.anningui.modifyjs.callback.CustomInterface;
import com.anningui.modifyjs.render.item.KJSClientItemExtensions;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.BlockItemBuilder;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

import static com.anningui.modifyjs.ModifyJS.mjs$customRendererMap;

@Mixin(BlockItemBuilder.class)
public abstract class BlockItemBuilderMixin extends ItemBuilder {
    @Unique
    public transient CustomInterface.RenderByItemCallback mjs$renderByItemCallback;

    @Unique
    public transient boolean mjs$isCustomRenderer;

    public BlockItemBuilderMixin(ResourceLocation i) {
        super(i);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(CallbackInfo ci) {
        this.mjs$renderByItemCallback = null;
        mjs$customRendererMap.put(id, false);
    }

//    @Inject(method = "createObject*", at = @At("RETURN"), cancellable = true, remap = false)
    /**
     * @author Anningui
     * @reason Mixin BlockItemBuilder to add renderByItem method
     */
    @Overwrite(remap=false)
    public Item createObject() {
        if (mjs$renderByItemCallback != null) {
            return new BlockItem(blockBuilder.get(), createItemProperties()) {
                @Override
                public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
                    super.initializeClient(consumer);
                    consumer.accept(new KJSClientItemExtensions(mjs$renderByItemCallback));
                }
            };
        } else {
            return new BlockItem(blockBuilder.get(), createItemProperties());
        }
    }

    @Unique
    public BlockItemBuilder renderByItem(CustomInterface.RenderByItemCallback renderCallback) {
        this.mjs$renderByItemCallback = renderCallback;
        return (BlockItemBuilder) (Object) this;
    }

    @Shadow(remap=false)
    public BlockBuilder blockBuilder;

    @Unique
    public BlockItemBuilder isCustomRenderer(boolean isCustomRenderer) {
        this.mjs$isCustomRenderer = isCustomRenderer;
        mjs$customRendererMap.put(id, isCustomRenderer);
        return (BlockItemBuilder) (Object) this;
    }
}
