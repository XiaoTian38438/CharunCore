/*     */ package net.minecraft.world.level.block;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class TripWireHookBlock extends Block {
/*  36 */   public static final MapCodec<TripWireHookBlock> CODEC = simpleCodec(TripWireHookBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<TripWireHookBlock> codec() {
/*  40 */     return CODEC;
/*     */   }
/*     */   
/*  43 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*  44 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*  45 */   public static final BooleanProperty ATTACHED = BlockStateProperties.ATTACHED;
/*     */   
/*     */   protected static final int WIRE_DIST_MIN = 1;
/*     */   
/*     */   protected static final int WIRE_DIST_MAX = 42;
/*     */   private static final int RECHECK_PERIOD = 10;
/*  51 */   private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateHorizontal(Block.boxZ(6.0D, 0.0D, 10.0D, 10.0D, 16.0D));
/*     */   
/*     */   public TripWireHookBlock(BlockBehaviour.Properties paramProperties) {
/*  54 */     super(paramProperties);
/*  55 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)POWERED, Boolean.valueOf(false))).setValue((Property)ATTACHED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  60 */     return SHAPES.get(paramBlockState.getValue((Property)FACING));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/*  65 */     Direction direction = (Direction)paramBlockState.getValue((Property)FACING);
/*  66 */     BlockPos blockPos = paramBlockPos.relative(direction.getOpposite());
/*  67 */     BlockState blockState = paramLevelReader.getBlockState(blockPos);
/*  68 */     return (direction.getAxis().isHorizontal() && blockState.isFaceSturdy((BlockGetter)paramLevelReader, blockPos, direction));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/*  73 */     if (paramDirection.getOpposite() == paramBlockState1.getValue((Property)FACING) && !paramBlockState1.canSurvive(paramLevelReader, paramBlockPos1)) {
/*  74 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*  76 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/*  81 */     BlockState blockState = (BlockState)((BlockState)defaultBlockState().setValue((Property)POWERED, Boolean.valueOf(false))).setValue((Property)ATTACHED, Boolean.valueOf(false));
/*     */     
/*  83 */     Level level = paramBlockPlaceContext.getLevel();
/*  84 */     BlockPos blockPos = paramBlockPlaceContext.getClickedPos();
/*     */     
/*  86 */     Direction[] arrayOfDirection = paramBlockPlaceContext.getNearestLookingDirections();
/*  87 */     for (Direction direction : arrayOfDirection) {
/*  88 */       if (direction.getAxis().isHorizontal()) {
/*     */ 
/*     */ 
/*     */         
/*  92 */         Direction direction1 = direction.getOpposite();
/*     */         
/*  94 */         blockState = (BlockState)blockState.setValue((Property)FACING, (Comparable)direction1);
/*  95 */         if (blockState.canSurvive((LevelReader)level, blockPos)) {
/*  96 */           return blockState;
/*     */         }
/*     */       } 
/*     */     } 
/* 100 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlacedBy(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, LivingEntity paramLivingEntity, ItemStack paramItemStack) {
/* 105 */     calculateState(paramLevel, paramBlockPos, paramBlockState, false, false, -1, (BlockState)null);
/*     */   }
/*     */   
/*     */   public static void calculateState(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState1, boolean paramBoolean1, boolean paramBoolean2, int paramInt, BlockState paramBlockState2) {
/* 109 */     Optional<Direction> optional = paramBlockState1.getOptionalValue((Property)FACING);
/* 110 */     if (!optional.isPresent()) {
/*     */       return;
/*     */     }
/*     */     
/* 114 */     Direction direction = optional.get();
/* 115 */     int i = ((Boolean)paramBlockState1.getOptionalValue((Property)ATTACHED).orElse(Boolean.valueOf(false))).booleanValue();
/* 116 */     boolean bool = ((Boolean)paramBlockState1.getOptionalValue((Property)POWERED).orElse(Boolean.valueOf(false))).booleanValue();
/*     */     
/* 118 */     Block block = paramBlockState1.getBlock();
/* 119 */     int j = !paramBoolean1 ? 1 : 0;
/* 120 */     int k = 0;
/* 121 */     byte b1 = 0;
/*     */     
/* 123 */     BlockState[] arrayOfBlockState = new BlockState[42];
/* 124 */     for (byte b2 = 1; b2 < 42; b2++) {
/* 125 */       BlockPos blockPos = paramBlockPos.relative(direction, b2);
/* 126 */       BlockState blockState1 = paramLevel.getBlockState(blockPos);
/*     */       
/* 128 */       if (blockState1.is(Blocks.TRIPWIRE_HOOK)) {
/* 129 */         if (blockState1.getValue((Property)FACING) == direction.getOpposite()) {
/* 130 */           b1 = b2;
/*     */         }
/*     */         break;
/*     */       } 
/* 134 */       if (blockState1.is(Blocks.TRIPWIRE) || b2 == paramInt) {
/* 135 */         if (b2 == paramInt) {
/* 136 */           blockState1 = (BlockState)MoreObjects.firstNonNull(paramBlockState2, blockState1);
/*     */         }
/* 138 */         int m = !((Boolean)blockState1.getValue((Property)TripWireBlock.DISARMED)).booleanValue() ? 1 : 0;
/* 139 */         boolean bool1 = ((Boolean)blockState1.getValue((Property)TripWireBlock.POWERED)).booleanValue();
/* 140 */         k |= (m && bool1) ? 1 : 0;
/*     */         
/* 142 */         arrayOfBlockState[b2] = blockState1;
/*     */         
/* 144 */         if (b2 == paramInt) {
/* 145 */           paramLevel.scheduleTick(paramBlockPos, block, 10);
/* 146 */           j &= m;
/*     */         } 
/*     */       } else {
/* 149 */         arrayOfBlockState[b2] = null;
/* 150 */         j = 0;
/*     */       } 
/*     */     } 
/*     */     
/* 154 */     j &= (b1 > 1) ? 1 : 0;
/* 155 */     k &= j;
/* 156 */     BlockState blockState = (BlockState)((BlockState)block.defaultBlockState().trySetValue((Property)ATTACHED, Boolean.valueOf(j))).trySetValue((Property)POWERED, Boolean.valueOf(k));
/*     */     
/* 158 */     if (b1 > 0) {
/* 159 */       BlockPos blockPos = paramBlockPos.relative(direction, b1);
/* 160 */       Direction direction1 = direction.getOpposite();
/* 161 */       paramLevel.setBlock(blockPos, (BlockState)blockState.setValue((Property)FACING, (Comparable)direction1), 3);
/* 162 */       notifyNeighbors(block, paramLevel, blockPos, direction1);
/*     */       
/* 164 */       emitState(paramLevel, blockPos, j, k, i, bool);
/*     */     } 
/*     */     
/* 167 */     emitState(paramLevel, paramBlockPos, j, k, i, bool);
/*     */     
/* 169 */     if (!paramBoolean1) {
/* 170 */       paramLevel.setBlock(paramBlockPos, (BlockState)blockState.setValue((Property)FACING, (Comparable)direction), 3);
/* 171 */       if (paramBoolean2) {
/* 172 */         notifyNeighbors(block, paramLevel, paramBlockPos, direction);
/*     */       }
/*     */     } 
/*     */     
/* 176 */     if (i != j) {
/* 177 */       for (byte b = 1; b < b1; b++) {
/* 178 */         BlockPos blockPos = paramBlockPos.relative(direction, b);
/* 179 */         BlockState blockState1 = arrayOfBlockState[b];
/* 180 */         if (blockState1 != null) {
/*     */ 
/*     */ 
/*     */           
/* 184 */           BlockState blockState2 = paramLevel.getBlockState(blockPos);
/* 185 */           if (blockState2.is(Blocks.TRIPWIRE) || blockState2.is(Blocks.TRIPWIRE_HOOK)) {
/* 186 */             paramLevel.setBlock(blockPos, (BlockState)blockState1.trySetValue((Property)ATTACHED, Boolean.valueOf(j)), 3);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 194 */     calculateState((Level)paramServerLevel, paramBlockPos, paramBlockState, false, true, -1, (BlockState)null);
/*     */   }
/*     */   
/*     */   private static void emitState(Level paramLevel, BlockPos paramBlockPos, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4) {
/* 198 */     if (paramBoolean2 && !paramBoolean4) {
/* 199 */       paramLevel.playSound(null, paramBlockPos, SoundEvents.TRIPWIRE_CLICK_ON, SoundSource.BLOCKS, 0.4F, 0.6F);
/* 200 */       paramLevel.gameEvent(null, (Holder)GameEvent.BLOCK_ACTIVATE, paramBlockPos);
/* 201 */     } else if (!paramBoolean2 && paramBoolean4) {
/* 202 */       paramLevel.playSound(null, paramBlockPos, SoundEvents.TRIPWIRE_CLICK_OFF, SoundSource.BLOCKS, 0.4F, 0.5F);
/* 203 */       paramLevel.gameEvent(null, (Holder)GameEvent.BLOCK_DEACTIVATE, paramBlockPos);
/* 204 */     } else if (paramBoolean1 && !paramBoolean3) {
/* 205 */       paramLevel.playSound(null, paramBlockPos, SoundEvents.TRIPWIRE_ATTACH, SoundSource.BLOCKS, 0.4F, 0.7F);
/* 206 */       paramLevel.gameEvent(null, (Holder)GameEvent.BLOCK_ATTACH, paramBlockPos);
/* 207 */     } else if (!paramBoolean1 && paramBoolean3) {
/* 208 */       paramLevel.playSound(null, paramBlockPos, SoundEvents.TRIPWIRE_DETACH, SoundSource.BLOCKS, 0.4F, 1.2F / (paramLevel.random.nextFloat() * 0.2F + 0.9F));
/* 209 */       paramLevel.gameEvent(null, (Holder)GameEvent.BLOCK_DETACH, paramBlockPos);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void notifyNeighbors(Block paramBlock, Level paramLevel, BlockPos paramBlockPos, Direction paramDirection) {
/* 214 */     Direction direction = paramDirection.getOpposite();
/* 215 */     Orientation orientation = ExperimentalRedstoneUtils.initialOrientation(paramLevel, direction, Direction.UP);
/* 216 */     paramLevel.updateNeighborsAt(paramBlockPos, paramBlock, orientation);
/* 217 */     paramLevel.updateNeighborsAt(paramBlockPos.relative(direction), paramBlock, orientation);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, boolean paramBoolean) {
/* 222 */     if (paramBoolean) {
/*     */       return;
/*     */     }
/* 225 */     boolean bool1 = ((Boolean)paramBlockState.getValue((Property)ATTACHED)).booleanValue();
/* 226 */     boolean bool2 = ((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue();
/*     */     
/* 228 */     if (bool1 || bool2) {
/* 229 */       calculateState((Level)paramServerLevel, paramBlockPos, paramBlockState, true, false, -1, (BlockState)null);
/*     */     }
/*     */     
/* 232 */     if (bool2) {
/* 233 */       notifyNeighbors(this, (Level)paramServerLevel, paramBlockPos, (Direction)paramBlockState.getValue((Property)FACING));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getSignal(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection) {
/* 239 */     return ((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue() ? 15 : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getDirectSignal(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection) {
/* 244 */     if (!((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue()) {
/* 245 */       return 0;
/*     */     }
/*     */     
/* 248 */     if (paramBlockState.getValue((Property)FACING) == paramDirection) {
/* 249 */       return 15;
/*     */     }
/*     */     
/* 252 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isSignalSource(BlockState paramBlockState) {
/* 257 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 262 */     return (BlockState)paramBlockState.setValue((Property)FACING, (Comparable)paramRotation.rotate((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 267 */     return paramBlockState.rotate(paramMirror.getRotation((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 272 */     paramBuilder.add(new Property[] { (Property)FACING, (Property)POWERED, (Property)ATTACHED });
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\TripWireHookBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */