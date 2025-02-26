package com.anningui.modifyjs.render.item;

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

/**
 * @author Flander923
 * @link <a href="https://fuwari-ald.pages.dev/posts/minecraft1_20_4/out_26-%E7%89%A9%E5%93%81%E7%89%B9%E6%AE%8A%E6%B8%B2%E6%9F%93/">...</a>
 */
public class MJSBakeModel implements BakedModel {
    private final BakedModel existingModel;

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
