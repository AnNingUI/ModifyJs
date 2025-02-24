package com.anningui.modifyjs.builder.item;

import com.anningui.modifyjs.render.item.KJSClientItemExtensions;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.item.custom.RecordItemJS;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.RecordItem;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static java.util.Objects.isNull;

public class RenderRecordItem extends RecordItem {
    public static class Builder extends RenderItemBuilder {
        public transient ResourceLocation song;
        public transient SoundEvent songSoundEvent;
        public transient int length;
        public transient int analogOutput;

        public Builder(ResourceLocation i) {
            super(i);
            song = new ResourceLocation("minecraft:music_disc.11");
            length = 71;
            analogOutput = 1;
            maxStackSize(1);
            rarity(Rarity.RARE);
        }

        @Info(value = """
			Sets the song that will play when this record is played.
			""",
                params = {
                        @Param(name = "s", value = "The location of sound event."),
                        @Param(name = "seconds", value = "The length of the song in seconds.")
                })
        public Builder song(ResourceLocation s, int seconds) {
            song = s;
            length = seconds;
            songSoundEvent = null;
            return this;
        }

        @Info("Sets the redstone output of the jukebox when this record is played.")
        public Builder analogOutput(int o) {
            analogOutput = o;
            return this;
        }

        @Override
        public Item createObject() {
            if (mjs$isCustomRenderer && !isNull(mjs$renderByItemCallback)) {
                return new RenderRecordItem(this, analogOutput, SoundEvents.ITEM_PICKUP, createItemProperties()) {
                    @Override
                    public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
                        super.initializeClient(consumer);
                        consumer.accept(new KJSClientItemExtensions(mjs$renderByItemCallback));
                    }
                };
            } else {
                return new RenderRecordItem(this, analogOutput, SoundEvents.ITEM_PICKUP, createItemProperties());
            }
        }

        public SoundEvent getSoundEvent() {
            if (songSoundEvent == null) {
                songSoundEvent = RegistryInfo.SOUND_EVENT.getValue(song);

                if (songSoundEvent == null || songSoundEvent == SoundEvents.ITEM_PICKUP) {
                    songSoundEvent = SoundEvents.ITEM_PICKUP;
                }
            }

            return songSoundEvent;
        }
    }

    private final Builder builder;

    public RenderRecordItem(Builder b, int analogOutput, SoundEvent song, Item.Properties properties) {
        super(analogOutput, song, properties, b.length);
        builder = b;
    }

    @Override
    public SoundEvent getSound() {
        return builder.getSoundEvent();
    }

    @Override
    public int getLengthInTicks() {
        return builder.length * 20;
    }
}
