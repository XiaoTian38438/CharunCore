/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.animal.Bucketable;
/*    */ import net.minecraft.world.item.component.CustomData;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ 
/*    */ public class MobBucketItem extends BucketItem {
/*    */   private final EntityType<? extends Mob> type;
/*    */   
/*    */   public MobBucketItem(EntityType<? extends Mob> paramEntityType, Fluid paramFluid, SoundEvent paramSoundEvent, Item.Properties paramProperties) {
/* 25 */     super(paramFluid, paramProperties);
/* 26 */     this.type = paramEntityType;
/* 27 */     this.emptySound = paramSoundEvent;
/*    */   }
/*    */   private final SoundEvent emptySound;
/*    */   
/*    */   public void checkExtraContent(LivingEntity paramLivingEntity, Level paramLevel, ItemStack paramItemStack, BlockPos paramBlockPos) {
/* 32 */     if (paramLevel instanceof ServerLevel) {
/* 33 */       spawn((ServerLevel)paramLevel, paramItemStack, paramBlockPos);
/* 34 */       paramLevel.gameEvent((Entity)paramLivingEntity, (Holder)GameEvent.ENTITY_PLACE, paramBlockPos);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void playEmptySound(LivingEntity paramLivingEntity, LevelAccessor paramLevelAccessor, BlockPos paramBlockPos) {
/* 40 */     paramLevelAccessor.playSound((Entity)paramLivingEntity, paramBlockPos, this.emptySound, SoundSource.NEUTRAL, 1.0F, 1.0F);
/*    */   }
/*    */   
/*    */   private void spawn(ServerLevel paramServerLevel, ItemStack paramItemStack, BlockPos paramBlockPos) {
/* 44 */     Mob mob = (Mob)this.type.create(paramServerLevel, EntityType.createDefaultStackConfig((Level)paramServerLevel, paramItemStack, null), paramBlockPos, EntitySpawnReason.BUCKET, true, false);
/*    */     
/* 46 */     if (mob instanceof Bucketable) { Bucketable bucketable = (Bucketable)mob;
/* 47 */       CustomData customData = (CustomData)paramItemStack.getOrDefault(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY);
/* 48 */       bucketable.loadFromBucketTag(customData.copyTag());
/* 49 */       bucketable.setFromBucket(true); }
/*    */ 
/*    */     
/* 52 */     if (mob != null) {
/* 53 */       paramServerLevel.addFreshEntityWithPassengers((Entity)mob);
/* 54 */       mob.playAmbientSound();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\MobBucketItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */