/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class AttachedStemBlock extends VegetationBlock {
/*    */   static {
/* 27 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)ResourceKey.codec(Registries.BLOCK).fieldOf("fruit").forGetter(()), (App)ResourceKey.codec(Registries.BLOCK).fieldOf("stem").forGetter(()), (App)ResourceKey.codec(Registries.ITEM).fieldOf("seed").forGetter(()), (App)propertiesCodec()).apply((Applicative)paramInstance, AttachedStemBlock::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static final MapCodec<AttachedStemBlock> CODEC;
/*    */ 
/*    */   
/*    */   public MapCodec<AttachedStemBlock> codec() {
/* 36 */     return CODEC;
/*    */   }
/*    */   
/* 39 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*    */   
/* 41 */   private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateHorizontal(Block.boxZ(4.0D, 0.0D, 10.0D, 0.0D, 10.0D));
/*    */   
/*    */   private final ResourceKey<Block> fruit;
/*    */   
/*    */   private final ResourceKey<Block> stem;
/*    */   private final ResourceKey<Item> seed;
/*    */   
/*    */   protected AttachedStemBlock(ResourceKey<Block> paramResourceKey1, ResourceKey<Block> paramResourceKey2, ResourceKey<Item> paramResourceKey, BlockBehaviour.Properties paramProperties) {
/* 49 */     super(paramProperties);
/* 50 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH));
/* 51 */     this.stem = paramResourceKey1;
/* 52 */     this.fruit = paramResourceKey2;
/* 53 */     this.seed = paramResourceKey;
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 58 */     return SHAPES.get(paramBlockState.getValue((Property)FACING));
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 63 */     if (!paramBlockState2.is(this.fruit) && paramDirection == paramBlockState1.getValue((Property)FACING)) {
/* 64 */       Optional<Block> optional = paramLevelReader.registryAccess().lookupOrThrow(Registries.BLOCK).getOptional(this.stem);
/* 65 */       if (optional.isPresent()) {
/* 66 */         return (BlockState)((Block)optional.get()).defaultBlockState().trySetValue((Property)StemBlock.AGE, Integer.valueOf(7));
/*    */       }
/*    */     } 
/* 69 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean mayPlaceOn(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 74 */     return paramBlockState.is(Blocks.FARMLAND);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ItemStack getCloneItemStack(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState, boolean paramBoolean) {
/* 79 */     return new ItemStack((ItemLike)DataFixUtils.orElse(paramLevelReader.registryAccess().lookupOrThrow(Registries.ITEM).getOptional(this.seed), this));
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 84 */     return (BlockState)paramBlockState.setValue((Property)FACING, (Comparable)paramRotation.rotate((Direction)paramBlockState.getValue((Property)FACING)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 89 */     return paramBlockState.rotate(paramMirror.getRotation((Direction)paramBlockState.getValue((Property)FACING)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 94 */     paramBuilder.add(new Property[] { (Property)FACING });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\AttachedStemBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */