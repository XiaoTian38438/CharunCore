/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ 
/*    */ 
/*    */ public class SimpleExplosionDamageCalculator
/*    */   extends ExplosionDamageCalculator
/*    */ {
/*    */   private final boolean explodesBlocks;
/*    */   private final boolean damagesEntities;
/*    */   private final Optional<Float> knockbackMultiplier;
/*    */   private final Optional<HolderSet<Block>> immuneBlocks;
/*    */   
/*    */   public SimpleExplosionDamageCalculator(boolean paramBoolean1, boolean paramBoolean2, Optional<Float> paramOptional, Optional<HolderSet<Block>> paramOptional1) {
/* 21 */     this.explodesBlocks = paramBoolean1;
/* 22 */     this.damagesEntities = paramBoolean2;
/* 23 */     this.knockbackMultiplier = paramOptional;
/* 24 */     this.immuneBlocks = paramOptional1;
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<Float> getBlockExplosionResistance(Explosion paramExplosion, BlockGetter paramBlockGetter, BlockPos paramBlockPos, BlockState paramBlockState, FluidState paramFluidState) {
/* 29 */     if (this.immuneBlocks.isPresent()) {
/* 30 */       if (paramBlockState.is(this.immuneBlocks.get())) {
/* 31 */         return Optional.of(Float.valueOf(3600000.0F));
/*    */       }
/* 33 */       return Optional.empty();
/*    */     } 
/* 35 */     return super.getBlockExplosionResistance(paramExplosion, paramBlockGetter, paramBlockPos, paramBlockState, paramFluidState);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldBlockExplode(Explosion paramExplosion, BlockGetter paramBlockGetter, BlockPos paramBlockPos, BlockState paramBlockState, float paramFloat) {
/* 40 */     return this.explodesBlocks;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldDamageEntity(Explosion paramExplosion, Entity paramEntity) {
/* 45 */     return this.damagesEntities;
/*    */   }
/*    */   
/*    */   public float getKnockbackMultiplier(Entity paramEntity) {
/*    */     // Byte code:
/*    */     //   0: aload_1
/*    */     //   1: instanceof net/minecraft/world/entity/player/Player
/*    */     //   4: ifeq -> 26
/*    */     //   7: aload_1
/*    */     //   8: checkcast net/minecraft/world/entity/player/Player
/*    */     //   11: astore_3
/*    */     //   12: aload_3
/*    */     //   13: invokevirtual getAbilities : ()Lnet/minecraft/world/entity/player/Abilities;
/*    */     //   16: getfield flying : Z
/*    */     //   19: ifeq -> 26
/*    */     //   22: iconst_1
/*    */     //   23: goto -> 27
/*    */     //   26: iconst_0
/*    */     //   27: istore_2
/*    */     //   28: iload_2
/*    */     //   29: ifeq -> 36
/*    */     //   32: fconst_0
/*    */     //   33: goto -> 56
/*    */     //   36: aload_0
/*    */     //   37: getfield knockbackMultiplier : Ljava/util/Optional;
/*    */     //   40: aload_0
/*    */     //   41: aload_1
/*    */     //   42: <illegal opcode> get : (Lnet/minecraft/world/level/SimpleExplosionDamageCalculator;Lnet/minecraft/world/entity/Entity;)Ljava/util/function/Supplier;
/*    */     //   47: invokevirtual orElseGet : (Ljava/util/function/Supplier;)Ljava/lang/Object;
/*    */     //   50: checkcast java/lang/Float
/*    */     //   53: invokevirtual floatValue : ()F
/*    */     //   56: freturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #50	-> 0
/*    */     //   #51	-> 28
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\SimpleExplosionDamageCalculator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */