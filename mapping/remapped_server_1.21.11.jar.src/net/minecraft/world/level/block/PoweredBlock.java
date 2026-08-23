/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class PoweredBlock extends Block {
/* 11 */   public static final MapCodec<PoweredBlock> CODEC = simpleCodec(PoweredBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<PoweredBlock> codec() {
/* 15 */     return CODEC;
/*    */   }
/*    */   
/*    */   public PoweredBlock(BlockBehaviour.Properties paramProperties) {
/* 19 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isSignalSource(BlockState paramBlockState) {
/* 24 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getSignal(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection) {
/* 29 */     return 15;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\PoweredBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */