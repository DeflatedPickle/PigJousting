/* Copyright (c) 2023 DeflatedPickle under the MIT license */

package com.deflatedpickle.pigjousting.mixin;

import java.util.List;
import java.util.Objects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

@SuppressWarnings("UnusedMixin")
@Mixin(PigEntity.class)
public abstract class MixinPigEntity extends MobEntity {
  protected MixinPigEntity(EntityType<? extends MobEntity> entityType, World world) {
    super(entityType, world);
  }

  /**
   * @author
   * @reason
   */
  @Overwrite
  public static DefaultAttributeContainer.Builder createPigAttributes() {
    return MobEntity.createMobAttributes()
        .add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0)
        .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25)
        .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.0);
  }

  @Override
  public ActionResult interactAt(PlayerEntity player, Vec3d hitPos, Hand hand) {
    if (!player.getMainHandStack().isEmpty()
        && !(player.getMainHandStack().getItem() instanceof SwordItem))
      return super.interactAt(player, hitPos, hand);

    if (player.isSneaking()) {
      ItemStack stack = this.getMainHandStack();
      player.giveItemStack(stack.copy());
      stack.decrement(1);

      return ActionResult.success(world.isClient());
    } else if (this.getMainHandStack().isEmpty()) {
      ItemStack stack = player.getMainHandStack();

      this.setStackInHand(Hand.MAIN_HAND, stack.copy());
      if (!player.getAbilities().creativeMode) {
        stack.decrement(1);
      }

      return ActionResult.success(world.isClient());
    }

    return super.interactAt(player, hitPos, hand);
  }

  @Override
  public void tickMovement() {
    super.tickMovement();

    Box box =
        this.hasVehicle() && !Objects.requireNonNull(this.getVehicle()).isRemoved()
            ? this.getBoundingBox().union(this.getVehicle().getBoundingBox()).expand(1.0, 0.0, 1.0)
            : this.getBoundingBox().expand(0.5, 0.5, 0.5);
    List<Entity> list = this.world.getOtherEntities(this, box);
    for (Entity entity : list) {
      if (entity.isRemoved() || !(entity instanceof LivingEntity)) continue;
      this.collideWithEntity(entity);
    }
  }

  @Unique
  private void collideWithEntity(Entity entity) {
    Item item = this.getMainHandStack().getItem();
    if (item instanceof SwordItem && entity.isAttackable() && entity instanceof MobEntity mob) {
      this.tryAttack(mob);
    }
  }

  @Override
  protected float getDropChance(EquipmentSlot slot) {
    if (slot.getType() == EquipmentSlot.Type.HAND) {
      return 1f;
    } else {
      return super.getDropChance(slot);
    }
  }
}
