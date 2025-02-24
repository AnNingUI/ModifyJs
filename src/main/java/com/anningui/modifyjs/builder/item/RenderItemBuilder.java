package com.anningui.modifyjs.builder.item;

import com.anningui.modifyjs.callback.CustomInterface;
import com.anningui.modifyjs.render.item.KJSClientItemExtensions;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import java.util.function.Consumer;


import static com.anningui.modifyjs.ModifyJS.mjs$customRendererMap;
import static java.util.Objects.isNull;

public abstract class RenderItemBuilder extends ItemBuilder {

    public transient CustomInterface.RenderByItemCallback mjs$renderByItemCallback;
    public transient boolean mjs$isCustomRenderer = false;

    public RenderItemBuilder(ResourceLocation i) {
        super(i);
        this.mjs$renderByItemCallback = null;
        mjs$customRendererMap.put(id, false);
    }

    public RenderItemBuilder isCustomRenderer(boolean isCustomRenderer) {
        this.mjs$isCustomRenderer = isCustomRenderer;
        mjs$customRendererMap.put(id, isCustomRenderer);
        return this;
    }

    public RenderItemBuilder renderByItem(CustomInterface.RenderByItemCallback renderCallback) {
        this.mjs$renderByItemCallback = renderCallback;
        return this;
    }

    @Override
    public Item createObject() {
        if (mjs$isCustomRenderer && !isNull(mjs$renderByItemCallback)) {
            return new Item(createItemProperties()) {
                @Override
                public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
                    super.initializeClient(consumer);
                    consumer.accept(new KJSClientItemExtensions(mjs$renderByItemCallback));
                }
            };
        } else {
            return new Item(createItemProperties());
        }
    }



    public static class BasicItemBuilder extends RenderItemBuilder {
        public BasicItemBuilder(ResourceLocation i) {
            super(i);
        }
    }
}
