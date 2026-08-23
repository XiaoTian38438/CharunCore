/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class SlimeBlock extends HalfTransparentBlock {
/* 13 */   public static final MapCodec<SlimeBlock> CODEC = simpleCodec(SlimeBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<SlimeBlock> codec() {
/* 17 */     return CODEC;
/*    */   }
/*    */   
/*    */   public SlimeBlock(BlockBehaviour.Properties paramProperties) {
/* 21 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public void fallOn(Level paramLevel, BlockState paramBlockState, BlockPos paramBlockPos, Entity paramEntity, double paramDouble) {
/* 26 */     if (!paramEntity.isSuppressingBounce())
/*    */     {
/* 28 */       paramEntity.causeFallDamage(paramDouble, 0.0F, paramLevel.damageSources().fall());
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateEntityMovementAfterFallOn(BlockGetter paramBlockGetter, Entity paramEntity) {
/* 34 */     if (paramEntity.isSuppressingBounce()) {
/* 35 */       super.updateEntityMovementAfterFallOn(paramBlockGetter, paramEntity);
/*    */     } else {
/* 37 */       bounceUp(paramEntity);
/*    */     } 
/*    */   }
/*    */   
/*    */   private void bounceUp(Entity paramEntity) {
/* 42 */     Vec3 vec3 = paramEntity.getDeltaMovement();
/* 43 */     if (vec3.y < 0.0D) {
/*    */       
/* 45 */       double d = (paramEntity instanceof net.minecraft.world.entity.LivingEntity) ? 1.0D : 0.8D;
/* 46 */       paramEntity.setDeltaMovement(vec3.x, -vec3.y * d, vec3.z);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void stepOn(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, Entity paramEntity) {
/* 56 */     double d = Math.abs((paramEntity.getDeltaMovement()).y);
/* 57 */     if (d < 0.1D && !paramEntity.isSteppingCarefully()) {
/* 58 */       double d1 = 0.4D + d * 0.2D;
/* 59 */       paramEntity.setDeltaMovement(paramEntity.getDeltaMovement().multiply(d1, 1.0D, d1));
/*    */     } 
/* 61 */     super.stepOn(paramLevel, paramBlockPos, paramBlockState, paramEntity);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\SlimeBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */