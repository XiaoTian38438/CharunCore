/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.HashSet;
/*    */ import java.util.function.ToIntFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.gamerules.GameRules;
/*    */ 
/*    */ class WeavingMobEffect
/*    */   extends MobEffect
/*    */ {
/*    */   private final ToIntFunction<RandomSource> maxCobwebs;
/*    */   
/*    */   protected WeavingMobEffect(MobEffectCategory paramMobEffectCategory, int paramInt, ToIntFunction<RandomSource> paramToIntFunction) {
/* 24 */     super(paramMobEffectCategory, paramInt, (ParticleOptions)ParticleTypes.ITEM_COBWEB);
/* 25 */     this.maxCobwebs = paramToIntFunction;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onMobRemoved(ServerLevel paramServerLevel, LivingEntity paramLivingEntity, int paramInt, Entity.RemovalReason paramRemovalReason) {
/* 30 */     if (paramRemovalReason == Entity.RemovalReason.KILLED && (
/* 31 */       paramLivingEntity instanceof net.minecraft.world.entity.player.Player || ((Boolean)paramServerLevel.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue())) {
/* 32 */       spawnCobwebsRandomlyAround(paramServerLevel, paramLivingEntity.getRandom(), paramLivingEntity.blockPosition());
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   private void spawnCobwebsRandomlyAround(ServerLevel paramServerLevel, RandomSource paramRandomSource, BlockPos paramBlockPos) {
/* 38 */     HashSet<BlockPos> hashSet = Sets.newHashSet();
/* 39 */     int i = this.maxCobwebs.applyAsInt(paramRandomSource);
/* 40 */     for (BlockPos blockPos1 : BlockPos.randomInCube(paramRandomSource, 15, paramBlockPos, 1)) {
/* 41 */       BlockPos blockPos2 = blockPos1.below();
/*    */       
/* 43 */       if (hashSet.contains(blockPos1)) {
/*    */         continue;
/*    */       }
/*    */       
/* 47 */       if (paramServerLevel.getBlockState(blockPos1).canBeReplaced() && paramServerLevel.getBlockState(blockPos2).isFaceSturdy((BlockGetter)paramServerLevel, blockPos2, Direction.UP)) {
/* 48 */         hashSet.add(blockPos1.immutable());
/* 49 */         if (hashSet.size() >= i) {
/*    */           break;
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 55 */     for (BlockPos blockPos : hashSet) {
/* 56 */       paramServerLevel.setBlock(blockPos, Blocks.COBWEB.defaultBlockState(), 3);
/* 57 */       paramServerLevel.levelEvent(3018, blockPos, 0);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\effect\WeavingMobEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */