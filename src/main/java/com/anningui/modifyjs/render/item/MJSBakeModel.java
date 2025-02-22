package com.anningui.modifyjs.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MJSBakeModel implements BakedModel {
    private BakedModel existingModel;

    public MJSBakeModel(BakedModel existingModel) {
        this.existingModel = existingModel;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState pState, @Nullable Direction pDirection, RandomSource pRandom) {
        return this.existingModel.getQuads(pState, pDirection, pRandom);
    }


    @Override
    public boolean useAmbientOcclusion() {
        return this.existingModel.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return this.existingModel.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return this.existingModel.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return true;
    }



    @Override
    public TextureAtlasSprite getParticleIcon() {
        return this.existingModel.getParticleIcon();
    }

    @Override
    public ItemOverrides getOverrides() {
        return this.existingModel.getOverrides();
    }

    @Override
    public BakedModel applyTransform(ItemDisplayContext transformType, PoseStack poseStack, boolean applyLeftHandTransform) {
        return this;
    }
}
