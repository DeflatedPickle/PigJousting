/* Copyright (c) 2023 DeflatedPickle under the MIT license */

package com.deflatedpickle.pigjousting.mixin;

import com.deflatedpickle.pigjousting.client.AnimalSwordFeatureRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.PigEntityRenderer;
import net.minecraft.client.render.entity.model.PigEntityModel;
import net.minecraft.entity.passive.PigEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("UnusedMixin")
@Mixin(PigEntityRenderer.class)
public abstract class MixinPigRenderer
    extends MobEntityRenderer<PigEntity, PigEntityModel<PigEntity>> {
  public MixinPigRenderer(Context ctx, PigEntityModel<PigEntity> model, float shadowRadius) {
    super(ctx, model, shadowRadius);
  }

  @Inject(method = "<init>", at = @At("TAIL"))
  public void init(Context context, CallbackInfo ci) {
    addFeature(new AnimalSwordFeatureRenderer<>(context, this));
  }
}
