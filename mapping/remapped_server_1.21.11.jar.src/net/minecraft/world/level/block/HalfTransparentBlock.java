/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class HalfTransparentBlock extends Block {
/*  8 */   public static final MapCodec<HalfTransparentBlock> CODEC = simpleCodec(HalfTransparentBlock::new);
/*    */ 
/*    */   
/*    */   protected MapCodec<? extends HalfTransparentBlock> codec() {
/* 12 */     return CODEC;
/*    */   }
/*    */   
/*    */   protected HalfTransparentBlock(BlockBehaviour.Properties paramProperties) {
/* 16 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean skipRendering(BlockState paramBlockState1, BlockState paramBlockState2, Direction paramDirection) {
/* 21 */     if (paramBlockState2.is(this)) {
/* 22 */       return true;
/*    */     }
/* 24 */     return super.skipRendering(paramBlockState1, paramBlockState2, paramDirection);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\HalfTransparentBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */