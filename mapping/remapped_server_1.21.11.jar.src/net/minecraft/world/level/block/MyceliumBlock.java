/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class MyceliumBlock extends SpreadingSnowyDirtBlock {
/* 11 */   public static final MapCodec<MyceliumBlock> CODEC = simpleCodec(MyceliumBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<MyceliumBlock> codec() {
/* 15 */     return CODEC;
/*    */   }
/*    */   
/*    */   public MyceliumBlock(BlockBehaviour.Properties paramProperties) {
/* 19 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public void animateTick(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 24 */     super.animateTick(paramBlockState, paramLevel, paramBlockPos, paramRandomSource);
/* 25 */     if (paramRandomSource.nextInt(10) == 0)
/* 26 */       paramLevel.addParticle((ParticleOptions)ParticleTypes.MYCELIUM, paramBlockPos.getX() + paramRandomSource.nextDouble(), paramBlockPos.getY() + 1.1D, paramBlockPos.getZ() + paramRandomSource.nextDouble(), 0.0D, 0.0D, 0.0D); 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\MyceliumBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */