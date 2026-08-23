/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ 
/*    */ public class CoralBlock extends Block {
/* 20 */   public static final MapCodec<Block> DEAD_CORAL_FIELD = BuiltInRegistries.BLOCK.byNameCodec().fieldOf("dead"); public static final MapCodec<CoralBlock> CODEC; private final Block deadBlock;
/*    */   static {
/* 22 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)DEAD_CORAL_FIELD.forGetter(()), (App)propertiesCodec()).apply((Applicative)paramInstance, CoralBlock::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CoralBlock(Block paramBlock, BlockBehaviour.Properties paramProperties) {
/* 30 */     super(paramProperties);
/* 31 */     this.deadBlock = paramBlock;
/*    */   }
/*    */ 
/*    */   
/*    */   public MapCodec<CoralBlock> codec() {
/* 36 */     return CODEC;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 41 */     if (!scanForWater((BlockGetter)paramServerLevel, paramBlockPos)) {
/* 42 */       paramServerLevel.setBlock(paramBlockPos, this.deadBlock.defaultBlockState(), 2);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 48 */     if (!scanForWater((BlockGetter)paramLevelReader, paramBlockPos1)) {
/* 49 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, this, 60 + paramRandomSource.nextInt(40));
/*    */     }
/* 51 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*    */   }
/*    */   
/*    */   protected boolean scanForWater(BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 55 */     for (Direction direction : Direction.values()) {
/* 56 */       FluidState fluidState = paramBlockGetter.getFluidState(paramBlockPos.relative(direction));
/* 57 */       if (fluidState.is(FluidTags.WATER)) {
/* 58 */         return true;
/*    */       }
/*    */     } 
/* 61 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 66 */     if (!scanForWater((BlockGetter)paramBlockPlaceContext.getLevel(), paramBlockPlaceContext.getClickedPos())) {
/* 67 */       paramBlockPlaceContext.getLevel().scheduleTick(paramBlockPlaceContext.getClickedPos(), this, 60 + paramBlockPlaceContext.getLevel().getRandom().nextInt(40));
/*    */     }
/* 69 */     return defaultBlockState();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\CoralBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */