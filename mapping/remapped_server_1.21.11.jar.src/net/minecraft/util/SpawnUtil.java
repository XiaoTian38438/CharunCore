/*     */ package net.minecraft.util;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class SpawnUtil
/*     */ {
/*     */   public static <T extends Mob> Optional<T> trySpawnMob(EntityType<T> paramEntityType, EntitySpawnReason paramEntitySpawnReason, ServerLevel paramServerLevel, BlockPos paramBlockPos, int paramInt1, int paramInt2, int paramInt3, Strategy paramStrategy, boolean paramBoolean) {
/*  23 */     BlockPos.MutableBlockPos mutableBlockPos = paramBlockPos.mutable();
/*  24 */     for (byte b = 0; b < paramInt1; b++) {
/*  25 */       int i = Mth.randomBetweenInclusive(paramServerLevel.random, -paramInt2, paramInt2);
/*  26 */       int j = Mth.randomBetweenInclusive(paramServerLevel.random, -paramInt2, paramInt2);
/*     */       
/*  28 */       mutableBlockPos.setWithOffset((Vec3i)paramBlockPos, i, paramInt3, j);
/*  29 */       if (paramServerLevel.getWorldBorder().isWithinBounds((BlockPos)mutableBlockPos) && moveToPossibleSpawnPosition(paramServerLevel, paramInt3, mutableBlockPos, paramStrategy))
/*     */       {
/*     */         
/*  32 */         if (!paramBoolean || paramServerLevel.noCollision(paramEntityType.getSpawnAABB(mutableBlockPos.getX() + 0.5D, mutableBlockPos.getY(), mutableBlockPos.getZ() + 0.5D))) {
/*     */ 
/*     */ 
/*     */           
/*  36 */           Mob mob = (Mob)paramEntityType.create(paramServerLevel, null, (BlockPos)mutableBlockPos, paramEntitySpawnReason, false, false);
/*  37 */           if (mob != null) {
/*  38 */             if (mob.checkSpawnRules((LevelAccessor)paramServerLevel, paramEntitySpawnReason) && mob.checkSpawnObstruction((LevelReader)paramServerLevel)) {
/*  39 */               paramServerLevel.addFreshEntityWithPassengers((Entity)mob);
/*  40 */               mob.playAmbientSound();
/*  41 */               return Optional.of((T)mob);
/*     */             } 
/*  43 */             mob.discard();
/*     */           } 
/*     */         }  } 
/*     */     } 
/*  47 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public static interface Strategy {
/*     */     @Deprecated
/*     */     public static final Strategy LEGACY_IRON_GOLEM;
/*     */     public static final Strategy ON_TOP_OF_COLLIDER;
/*     */     public static final Strategy ON_TOP_OF_COLLIDER_NO_LEAVES;
/*     */     
/*     */     boolean canSpawnOn(ServerLevel param1ServerLevel, BlockPos param1BlockPos1, BlockState param1BlockState1, BlockPos param1BlockPos2, BlockState param1BlockState2);
/*     */     
/*     */     static {
/*  59 */       LEGACY_IRON_GOLEM = ((param1ServerLevel, param1BlockPos1, param1BlockState1, param1BlockPos2, param1BlockState2) -> 
/*  60 */         (param1BlockState1.is(Blocks.COBWEB) || param1BlockState1.is(Blocks.CACTUS) || param1BlockState1.is(Blocks.GLASS_PANE) || param1BlockState1.getBlock() instanceof net.minecraft.world.level.block.StainedGlassPaneBlock || param1BlockState1.getBlock() instanceof net.minecraft.world.level.block.StainedGlassBlock || param1BlockState1.getBlock() instanceof net.minecraft.world.level.block.LeavesBlock || param1BlockState1.is(Blocks.CONDUIT) || param1BlockState1.is(Blocks.ICE) || param1BlockState1.is(Blocks.TNT) || param1BlockState1.is(Blocks.GLOWSTONE) || param1BlockState1.is(Blocks.BEACON) || param1BlockState1.is(Blocks.SEA_LANTERN) || param1BlockState1.is(Blocks.FROSTED_ICE) || param1BlockState1.is(Blocks.TINTED_GLASS) || param1BlockState1.is(Blocks.GLASS)) ? false : (
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  78 */         ((param1BlockState2.isAir() || param1BlockState2.liquid()) && (param1BlockState1.isSolid() || param1BlockState1.is(Blocks.POWDER_SNOW)))));
/*     */ 
/*     */       
/*  81 */       ON_TOP_OF_COLLIDER = ((param1ServerLevel, param1BlockPos1, param1BlockState1, param1BlockPos2, param1BlockState2) -> 
/*  82 */         (param1BlockState2.getCollisionShape((BlockGetter)param1ServerLevel, param1BlockPos2).isEmpty() && Block.isFaceFull(param1BlockState1.getCollisionShape((BlockGetter)param1ServerLevel, param1BlockPos1), Direction.UP)));
/*     */       
/*  84 */       ON_TOP_OF_COLLIDER_NO_LEAVES = ((param1ServerLevel, param1BlockPos1, param1BlockState1, param1BlockPos2, param1BlockState2) -> 
/*  85 */         (param1BlockState2.getCollisionShape((BlockGetter)param1ServerLevel, param1BlockPos2).isEmpty() && !param1BlockState1.is(BlockTags.LEAVES) && Block.isFaceFull(param1BlockState1.getCollisionShape((BlockGetter)param1ServerLevel, param1BlockPos1), Direction.UP)));
/*     */     } }
/*     */   
/*     */   private static boolean moveToPossibleSpawnPosition(ServerLevel paramServerLevel, int paramInt, BlockPos.MutableBlockPos paramMutableBlockPos, Strategy paramStrategy) {
/*  89 */     BlockPos.MutableBlockPos mutableBlockPos = (new BlockPos.MutableBlockPos()).set((Vec3i)paramMutableBlockPos);
/*  90 */     BlockState blockState = paramServerLevel.getBlockState((BlockPos)mutableBlockPos);
/*     */     
/*  92 */     for (int i = paramInt; i >= -paramInt; i--) {
/*  93 */       paramMutableBlockPos.move(Direction.DOWN);
/*  94 */       mutableBlockPos.setWithOffset((Vec3i)paramMutableBlockPos, Direction.UP);
/*     */       
/*  96 */       BlockState blockState1 = paramServerLevel.getBlockState((BlockPos)paramMutableBlockPos);
/*  97 */       if (paramStrategy.canSpawnOn(paramServerLevel, (BlockPos)paramMutableBlockPos, blockState1, (BlockPos)mutableBlockPos, blockState)) {
/*  98 */         paramMutableBlockPos.move(Direction.UP);
/*  99 */         return true;
/*     */       } 
/* 101 */       blockState = blockState1;
/*     */     } 
/* 103 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\SpawnUtil.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */