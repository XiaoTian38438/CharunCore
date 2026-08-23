/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class HayBlock extends RotatedPillarBlock {
/* 11 */   public static final MapCodec<HayBlock> CODEC = simpleCodec(HayBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<HayBlock> codec() {
/* 15 */     return CODEC;
/*    */   }
/*    */   
/*    */   public HayBlock(BlockBehaviour.Properties paramProperties) {
/* 19 */     super(paramProperties);
/* 20 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)AXIS, (Comparable)Direction.Axis.Y));
/*    */   }
/*    */ 
/*    */   
/*    */   public void fallOn(Level paramLevel, BlockState paramBlockState, BlockPos paramBlockPos, Entity paramEntity, double paramDouble) {
/* 25 */     paramEntity.causeFallDamage(paramDouble, 0.2F, paramLevel.damageSources().fall());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\HayBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */