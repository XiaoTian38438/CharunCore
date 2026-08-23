/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class ConcretePowderBlock extends FallingBlock {
/*    */   static {
/* 19 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)BuiltInRegistries.BLOCK.byNameCodec().fieldOf("concrete").forGetter(()), (App)propertiesCodec()).apply((Applicative)paramInstance, ConcretePowderBlock::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<ConcretePowderBlock> CODEC;
/*    */   private final Block concrete;
/*    */   
/*    */   public MapCodec<ConcretePowderBlock> codec() {
/* 26 */     return CODEC;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public ConcretePowderBlock(Block paramBlock, BlockBehaviour.Properties paramProperties) {
/* 32 */     super(paramProperties);
/* 33 */     this.concrete = paramBlock;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onLand(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState1, BlockState paramBlockState2, FallingBlockEntity paramFallingBlockEntity) {
/* 38 */     if (shouldSolidify((BlockGetter)paramLevel, paramBlockPos, paramBlockState2)) {
/* 39 */       paramLevel.setBlock(paramBlockPos, this.concrete.defaultBlockState(), 3);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 45 */     Level level = paramBlockPlaceContext.getLevel();
/* 46 */     BlockPos blockPos = paramBlockPlaceContext.getClickedPos();
/* 47 */     BlockState blockState = level.getBlockState(blockPos);
/*    */     
/* 49 */     if (shouldSolidify((BlockGetter)level, blockPos, blockState)) {
/* 50 */       return this.concrete.defaultBlockState();
/*    */     }
/* 52 */     return super.getStateForPlacement(paramBlockPlaceContext);
/*    */   }
/*    */   
/*    */   private static boolean shouldSolidify(BlockGetter paramBlockGetter, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 56 */     return (canSolidify(paramBlockState) || touchesLiquid(paramBlockGetter, paramBlockPos));
/*    */   }
/*    */   
/*    */   private static boolean touchesLiquid(BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 60 */     boolean bool = false;
/* 61 */     BlockPos.MutableBlockPos mutableBlockPos = paramBlockPos.mutable();
/* 62 */     for (Direction direction : Direction.values()) {
/* 63 */       BlockState blockState = paramBlockGetter.getBlockState((BlockPos)mutableBlockPos);
/* 64 */       if (direction != Direction.DOWN || canSolidify(blockState)) {
/*    */ 
/*    */         
/* 67 */         mutableBlockPos.setWithOffset((Vec3i)paramBlockPos, direction);
/* 68 */         blockState = paramBlockGetter.getBlockState((BlockPos)mutableBlockPos);
/* 69 */         if (canSolidify(blockState) && !blockState.isFaceSturdy(paramBlockGetter, paramBlockPos, direction.getOpposite())) {
/* 70 */           bool = true; break;
/*    */         } 
/*    */       } 
/*    */     } 
/* 74 */     return bool;
/*    */   }
/*    */   
/*    */   private static boolean canSolidify(BlockState paramBlockState) {
/* 78 */     return paramBlockState.getFluidState().is(FluidTags.WATER);
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 83 */     if (touchesLiquid((BlockGetter)paramLevelReader, paramBlockPos1)) {
/* 84 */       return this.concrete.defaultBlockState();
/*    */     }
/*    */     
/* 87 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getDustColor(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 92 */     return (paramBlockState.getMapColor(paramBlockGetter, paramBlockPos)).col;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\ConcretePowderBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */