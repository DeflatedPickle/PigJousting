/* Copyright (c) 2023 DeflatedPickle under the MIT license */

package com.deflatedpickle.pigjousting.client

import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.LivingEntityRenderer
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.render.model.json.ModelTransformation.Mode.FIXED
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.LivingEntity
import net.minecraft.item.SwordItem
import net.minecraft.util.Hand.MAIN_HAND
import net.minecraft.util.math.Vec3f

class AnimalSwordFeatureRenderer<T : LivingEntity, M : EntityModel<T>>(
    context: EntityRendererFactory.Context,
    entityRenderer: LivingEntityRenderer<T, M>,
) : FeatureRenderer<T, M>(entityRenderer) {
    override fun render(
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        entity: T,
        limbAngle: Float,
        limbDistance: Float,
        tickDelta: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {
        val stack = entity.getStackInHand(MAIN_HAND)

        if (stack.item is SwordItem) {
            matrices.push()
            matrices.translate(0.35, 0.8, -0.5)
            matrices.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(90f))
            matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(55f))

            MinecraftClient.getInstance()
                .itemRenderer
                .renderItem(
                    entity,
                    stack,
                    FIXED,
                    false,
                    matrices,
                    vertexConsumers,
                    entity.world,
                    light,
                    OverlayTexture.DEFAULT_UV,
                    0
                )

            matrices.pop()
        }
    }
}
