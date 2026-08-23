/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.ToIntFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class GlowLichenBlock extends MultifaceSpreadeableBlock implements BonemealableBlock {
/* 15 */   public static final MapCodec<GlowLichenBlock> CODEC = simpleCodec(GlowLichenBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<GlowLichenBlock> codec() {
/* 19 */     return CODEC;
/*    */   }
/*    */   
/* 22 */   private final MultifaceSpreader spreader = new MultifaceSpreader(this);
/*    */   
/*    */   public GlowLichenBlock(BlockBehaviour.Properties paramProperties) {
/* 25 */     super(paramProperties);
/*    */   }
/*    */   
/*    */   public static ToIntFunction<BlockState> emission(int paramInt) {
/* 29 */     return paramBlockState -> MultifaceBlock.hasAnyFace(paramBlockState) ? paramInt : 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValidBonemealTarget(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 34 */     return Direction.stream().anyMatch(paramDirection -> this.spreader.canSpreadInAnyDirection(paramBlockState, (BlockGetter)paramLevelReader, paramBlockPos, paramDirection.getOpposite()));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBonemealSuccess(Level paramLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 39 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void performBonemeal(ServerLevel paramServerLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 44 */     this.spreader.spreadFromRandomFaceTowardRandomDirection(paramBlockState, (LevelAccessor)paramServerLevel, paramBlockPos, paramRandomSource);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean propagatesSkylightDown(BlockState paramBlockState) {
/* 49 */     return paramBlockState.getFluidState().isEmpty();
/*    */   }
/*    */ 
/*    */   
/*    */   public MultifaceSpreader getSpreader() {
/* 54 */     return this.spreader;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\GlowLichenBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */