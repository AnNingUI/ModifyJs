package com.anningui.modifyjs.mixin;

import com.anningui.modifyjs.callback.ItemModifyCallback;
import dev.latvian.mods.kubejs.core.ItemKJS;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.anningui.modifyjs.callback.CustomInterface.isNull;

@Mixin(Item.class)
public abstract class ItemMixin implements FeatureElement, ItemLike, IForgeItem, ItemKJS {
    //Mixins here
    @Unique
    private ItemModifyCallback mjs$callback;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(CallbackInfo ci) {
        this.mjs$callback = new ItemModifyCallback();
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void beforeUse(Level level, Player player, InteractionHand usedHand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        System.out.println("use " + kjs$getId());
        InteractionResultHolder<ItemStack> r = isNull(mjs$callback.useCallback) ?
                use(level, player, usedHand) :
                mjs$callback.useCallback.apply(level, player, usedHand);
        cir.setReturnValue(r);
        cir.cancel();
    }



    @Unique
    public ItemModifyCallback mjs$getCallback() {
        return mjs$callback;
    }



    @Shadow
    public abstract InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand);
}
