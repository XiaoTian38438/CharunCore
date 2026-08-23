/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.particles.ItemParticleOption;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class RemoveBlockGoal
/*     */   extends MoveToBlockGoal {
/*     */   private final Block blockToRemove;
/*     */   private final Mob removerMob;
/*     */   
/*     */   public RemoveBlockGoal(Block paramBlock, PathfinderMob paramPathfinderMob, double paramDouble, int paramInt) {
/*  32 */     super(paramPathfinderMob, paramDouble, 24, paramInt);
/*  33 */     this.blockToRemove = paramBlock;
/*  34 */     this.removerMob = (Mob)paramPathfinderMob;
/*     */   }
/*     */   private int ticksSinceReachedGoal; private static final int WAIT_AFTER_BLOCK_FOUND = 20;
/*     */   
/*     */   public boolean canUse() {
/*  39 */     if (!((Boolean)getServerLevel((Entity)this.removerMob).getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue()) {
/*  40 */       return false;
/*     */     }
/*     */     
/*  43 */     if (this.nextStartTick > 0) {
/*  44 */       this.nextStartTick--;
/*  45 */       return false;
/*     */     } 
/*     */     
/*  48 */     if (findNearestBlock()) {
/*     */       
/*  50 */       this.nextStartTick = reducedTickDelay(20);
/*  51 */       return true;
/*     */     } 
/*  53 */     this.nextStartTick = nextStartTick(this.mob);
/*  54 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {
/*  60 */     super.stop();
/*  61 */     this.removerMob.fallDistance = 1.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  66 */     super.start();
/*  67 */     this.ticksSinceReachedGoal = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void playDestroyProgressSound(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos) {}
/*     */ 
/*     */   
/*     */   public void playBreakSound(Level paramLevel, BlockPos paramBlockPos) {}
/*     */ 
/*     */   
/*     */   public void tick() {
/*  78 */     super.tick();
/*  79 */     Level level = this.removerMob.level();
/*  80 */     BlockPos blockPos1 = this.removerMob.blockPosition();
/*     */     
/*  82 */     BlockPos blockPos2 = getPosWithBlock(blockPos1, (BlockGetter)level);
/*     */     
/*  84 */     RandomSource randomSource = this.removerMob.getRandom();
/*  85 */     if (isReachedTarget() && blockPos2 != null) {
/*  86 */       if (this.ticksSinceReachedGoal > 0) {
/*  87 */         Vec3 vec3 = this.removerMob.getDeltaMovement();
/*  88 */         this.removerMob.setDeltaMovement(vec3.x, 0.3D, vec3.z);
/*     */         
/*  90 */         if (!level.isClientSide()) {
/*  91 */           double d = 0.08D;
/*  92 */           ((ServerLevel)level).sendParticles((ParticleOptions)new ItemParticleOption(ParticleTypes.ITEM, new ItemStack((ItemLike)Items.EGG)), blockPos2
/*     */               
/*  94 */               .getX() + 0.5D, blockPos2
/*  95 */               .getY() + 0.7D, blockPos2
/*  96 */               .getZ() + 0.5D, 3, (randomSource
/*     */               
/*  98 */               .nextFloat() - 0.5D) * 0.08D, (randomSource
/*  99 */               .nextFloat() - 0.5D) * 0.08D, (randomSource
/* 100 */               .nextFloat() - 0.5D) * 0.08D, 0.15000000596046448D);
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 106 */       if (this.ticksSinceReachedGoal % 2 == 0) {
/* 107 */         Vec3 vec3 = this.removerMob.getDeltaMovement();
/* 108 */         this.removerMob.setDeltaMovement(vec3.x, -0.3D, vec3.z);
/*     */         
/* 110 */         if (this.ticksSinceReachedGoal % 6 == 0) {
/* 111 */           playDestroyProgressSound((LevelAccessor)level, this.blockPos);
/*     */         }
/*     */       } 
/*     */       
/* 115 */       if (this.ticksSinceReachedGoal > 60) {
/* 116 */         level.removeBlock(blockPos2, false);
/* 117 */         if (!level.isClientSide()) {
/* 118 */           for (byte b = 0; b < 20; b++) {
/* 119 */             double d1 = randomSource.nextGaussian() * 0.02D;
/* 120 */             double d2 = randomSource.nextGaussian() * 0.02D;
/* 121 */             double d3 = randomSource.nextGaussian() * 0.02D;
/* 122 */             ((ServerLevel)level).sendParticles((ParticleOptions)ParticleTypes.POOF, blockPos2.getX() + 0.5D, blockPos2.getY(), blockPos2.getZ() + 0.5D, 1, d1, d2, d3, 0.15000000596046448D);
/*     */           } 
/* 124 */           playBreakSound(level, blockPos2);
/*     */         } 
/*     */       } 
/* 127 */       this.ticksSinceReachedGoal++;
/*     */     } 
/*     */   }
/*     */   
/*     */   private BlockPos getPosWithBlock(BlockPos paramBlockPos, BlockGetter paramBlockGetter) {
/* 132 */     if (paramBlockGetter.getBlockState(paramBlockPos).is(this.blockToRemove)) {
/* 133 */       return paramBlockPos;
/*     */     }
/* 135 */     BlockPos[] arrayOfBlockPos = { paramBlockPos.below(), paramBlockPos.west(), paramBlockPos.east(), paramBlockPos.north(), paramBlockPos.south(), paramBlockPos.below().below() };
/* 136 */     for (BlockPos blockPos : arrayOfBlockPos) {
/* 137 */       if (paramBlockGetter.getBlockState(blockPos).is(this.blockToRemove)) {
/* 138 */         return blockPos;
/*     */       }
/*     */     } 
/* 141 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isValidTarget(LevelReader paramLevelReader, BlockPos paramBlockPos) {
/* 146 */     ChunkAccess chunkAccess = paramLevelReader.getChunk(SectionPos.blockToSectionCoord(paramBlockPos.getX()), SectionPos.blockToSectionCoord(paramBlockPos.getZ()), ChunkStatus.FULL, false);
/* 147 */     if (chunkAccess != null) {
/* 148 */       return (chunkAccess.getBlockState(paramBlockPos).is(this.blockToRemove) && chunkAccess.getBlockState(paramBlockPos.above()).isAir() && chunkAccess.getBlockState(paramBlockPos.above(2)).isAir());
/*     */     }
/* 150 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\RemoveBlockGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */