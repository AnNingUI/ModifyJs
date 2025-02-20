package com.anningui.modifyjs.callback;

import com.anningui.modifyjs.callback.CustomInterface.KTriConsumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public class ItemModifyCallback {
    public KTriConsumer<Level, Player, InteractionHand, InteractionResultHolder<ItemStack>> useCallback;

    public ItemModifyCallback() {
        this.useCallback = null;
    }

    public ItemModifyCallback use(
            KTriConsumer<Level, Player, InteractionHand, InteractionResultHolder<ItemStack>> useCallback
    ) {
        this.useCallback = useCallback;
        return this;
    }
}
