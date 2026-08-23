/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class TintedGlassBlock extends TransparentBlock {
/*  8 */   public static final MapCodec<TintedGlassBlock> CODEC = simpleCodec(TintedGlassBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<TintedGlassBlock> codec() {
/* 12 */     return CODEC;
/*    */   }
/*    */   public TintedGlassBlock(BlockBehaviour.Properties paramProperties) {
/* 15 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean propagatesSkylightDown(BlockState paramBlockState) {
/* 20 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getLightBlock(BlockState paramBlockState) {
/* 25 */     return 15;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\TintedGlassBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */