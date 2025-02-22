package com.anningui.modifyjs.adder.mc_adder.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class ParticleDisplay extends Display {
    public ParticleDisplay(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void updateRenderSubState(boolean interpolate, float partialTick) {

    }


}
