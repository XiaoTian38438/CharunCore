/*     */ package net.minecraft.world.level.block.piston;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.SignalGetter;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.PistonType;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.material.PushReaction;
/*     */ import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class PistonBaseBlock extends DirectionalBlock {
/*     */   static {
/*  47 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)Codec.BOOL.fieldOf("sticky").forGetter(()), (App)propertiesCodec()).apply((Applicative)paramInstance, PistonBaseBlock::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final MapCodec<PistonBaseBlock> CODEC;
/*     */   
/*     */   public MapCodec<PistonBaseBlock> codec() {
/*  54 */     return CODEC;
/*     */   }
/*     */   
/*  57 */   public static final BooleanProperty EXTENDED = BlockStateProperties.EXTENDED;
/*     */   
/*     */   public static final int TRIGGER_EXTEND = 0;
/*     */   
/*     */   public static final int TRIGGER_CONTRACT = 1;
/*     */   public static final int TRIGGER_DROP = 2;
/*     */   public static final int PLATFORM_THICKNESS = 4;
/*  64 */   private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateAll(Block.boxZ(16.0D, 4.0D, 16.0D));
/*     */   
/*     */   private final boolean isSticky;
/*     */   
/*     */   public PistonBaseBlock(boolean paramBoolean, BlockBehaviour.Properties paramProperties) {
/*  69 */     super(paramProperties);
/*  70 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)EXTENDED, Boolean.valueOf(false)));
/*  71 */     this.isSticky = paramBoolean;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  76 */     if (((Boolean)paramBlockState.getValue((Property)EXTENDED)).booleanValue()) {
/*  77 */       return SHAPES.get(paramBlockState.getValue((Property)FACING));
/*     */     }
/*  79 */     return Shapes.block();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlacedBy(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, LivingEntity paramLivingEntity, ItemStack paramItemStack) {
/*  84 */     if (!paramLevel.isClientSide()) {
/*  85 */       checkIfExtend(paramLevel, paramBlockPos, paramBlockState);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void neighborChanged(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Block paramBlock, Orientation paramOrientation, boolean paramBoolean) {
/*  91 */     if (!paramLevel.isClientSide()) {
/*  92 */       checkIfExtend(paramLevel, paramBlockPos, paramBlockState);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPlace(BlockState paramBlockState1, Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState2, boolean paramBoolean) {
/*  98 */     if (paramBlockState2.is(paramBlockState1.getBlock())) {
/*     */       return;
/*     */     }
/* 101 */     if (!paramLevel.isClientSide() && paramLevel.getBlockEntity(paramBlockPos) == null) {
/* 102 */       checkIfExtend(paramLevel, paramBlockPos, paramBlockState1);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 108 */     return (BlockState)((BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)paramBlockPlaceContext.getNearestLookingDirection().getOpposite())).setValue((Property)EXTENDED, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   private void checkIfExtend(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 112 */     Direction direction = (Direction)paramBlockState.getValue((Property)FACING);
/*     */     
/* 114 */     boolean bool = getNeighborSignal((SignalGetter)paramLevel, paramBlockPos, direction);
/*     */     
/* 116 */     if (bool && !((Boolean)paramBlockState.getValue((Property)EXTENDED)).booleanValue()) {
/* 117 */       if ((new PistonStructureResolver(paramLevel, paramBlockPos, direction, true)).resolve()) {
/* 118 */         paramLevel.blockEvent(paramBlockPos, (Block)this, 0, direction.get3DDataValue());
/*     */       }
/* 120 */     } else if (!bool && ((Boolean)paramBlockState.getValue((Property)EXTENDED)).booleanValue()) {
/* 121 */       BlockPos blockPos = paramBlockPos.relative(direction, 2);
/* 122 */       BlockState blockState = paramLevel.getBlockState(blockPos);
/*     */       
/* 124 */       byte b = 1;
/* 125 */       if (blockState.is(Blocks.MOVING_PISTON) && blockState.getValue((Property)FACING) == direction) {
/* 126 */         BlockEntity blockEntity = paramLevel.getBlockEntity(blockPos);
/*     */         
/* 128 */         if (blockEntity instanceof PistonMovingBlockEntity) { PistonMovingBlockEntity pistonMovingBlockEntity = (PistonMovingBlockEntity)blockEntity;
/* 129 */           if (pistonMovingBlockEntity.isExtending() && (pistonMovingBlockEntity.getProgress(0.0F) < 0.5F || paramLevel.getGameTime() == pistonMovingBlockEntity.getLastTicked() || ((ServerLevel)paramLevel).isHandlingTick())) {
/* 130 */             b = 2;
/*     */           } }
/*     */       
/*     */       } 
/*     */       
/* 135 */       paramLevel.blockEvent(paramBlockPos, (Block)this, b, direction.get3DDataValue());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean getNeighborSignal(SignalGetter paramSignalGetter, BlockPos paramBlockPos, Direction paramDirection) {
/* 146 */     for (Direction direction : Direction.values()) {
/* 147 */       if (direction != paramDirection && paramSignalGetter.hasSignal(paramBlockPos.relative(direction), direction)) {
/* 148 */         return true;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 153 */     if (paramSignalGetter.hasSignal(paramBlockPos, Direction.DOWN)) {
/* 154 */       return true;
/*     */     }
/*     */     
/* 157 */     BlockPos blockPos = paramBlockPos.above();
/* 158 */     for (Direction direction : Direction.values()) {
/* 159 */       if (direction != Direction.DOWN && paramSignalGetter.hasSignal(blockPos.relative(direction), direction)) {
/* 160 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 164 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean triggerEvent(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, int paramInt1, int paramInt2) {
/* 169 */     Direction direction = (Direction)paramBlockState.getValue((Property)FACING);
/* 170 */     BlockState blockState = (BlockState)paramBlockState.setValue((Property)EXTENDED, Boolean.valueOf(true));
/* 171 */     if (!paramLevel.isClientSide()) {
/* 172 */       boolean bool = getNeighborSignal((SignalGetter)paramLevel, paramBlockPos, direction);
/*     */       
/* 174 */       if (bool && (paramInt1 == 1 || paramInt1 == 2)) {
/*     */         
/* 176 */         paramLevel.setBlock(paramBlockPos, blockState, 2);
/* 177 */         return false;
/* 178 */       }  if (!bool && paramInt1 == 0) {
/* 179 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 183 */     if (paramInt1 == 0) {
/* 184 */       if (moveBlocks(paramLevel, paramBlockPos, direction, true)) {
/* 185 */         paramLevel.setBlock(paramBlockPos, blockState, 67);
/* 186 */         paramLevel.playSound(null, paramBlockPos, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 0.5F, paramLevel.random.nextFloat() * 0.25F + 0.6F);
/* 187 */         paramLevel.gameEvent((Holder)GameEvent.BLOCK_ACTIVATE, paramBlockPos, GameEvent.Context.of(blockState));
/*     */       } else {
/* 189 */         return false;
/*     */       } 
/* 191 */     } else if (paramInt1 == 1 || paramInt1 == 2) {
/* 192 */       BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos.relative(direction));
/* 193 */       if (blockEntity instanceof PistonMovingBlockEntity) {
/* 194 */         ((PistonMovingBlockEntity)blockEntity).finalTick();
/*     */       }
/*     */       
/* 197 */       BlockState blockState1 = (BlockState)((BlockState)Blocks.MOVING_PISTON.defaultBlockState().setValue((Property)MovingPistonBlock.FACING, (Comparable)direction)).setValue((Property)MovingPistonBlock.TYPE, this.isSticky ? (Comparable)PistonType.STICKY : (Comparable)PistonType.DEFAULT);
/* 198 */       paramLevel.setBlock(paramBlockPos, blockState1, 276);
/* 199 */       paramLevel.setBlockEntity(MovingPistonBlock.newMovingBlockEntity(paramBlockPos, blockState1, (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)Direction.from3DDataValue(paramInt2 & 0x7)), direction, false, true));
/*     */       
/* 201 */       paramLevel.updateNeighborsAt(paramBlockPos, blockState1.getBlock());
/* 202 */       blockState1.updateNeighbourShapes((LevelAccessor)paramLevel, paramBlockPos, 2);
/*     */ 
/*     */       
/* 205 */       if (this.isSticky) {
/* 206 */         BlockPos blockPos = paramBlockPos.offset(direction.getStepX() * 2, direction.getStepY() * 2, direction.getStepZ() * 2);
/* 207 */         BlockState blockState2 = paramLevel.getBlockState(blockPos);
/* 208 */         boolean bool = false;
/*     */         
/* 210 */         if (blockState2.is(Blocks.MOVING_PISTON)) {
/*     */ 
/*     */           
/* 213 */           BlockEntity blockEntity1 = paramLevel.getBlockEntity(blockPos);
/* 214 */           if (blockEntity1 instanceof PistonMovingBlockEntity) { PistonMovingBlockEntity pistonMovingBlockEntity = (PistonMovingBlockEntity)blockEntity1;
/* 215 */             if (pistonMovingBlockEntity.getDirection() == direction && pistonMovingBlockEntity.isExtending()) {
/*     */               
/* 217 */               pistonMovingBlockEntity.finalTick();
/* 218 */               bool = true;
/*     */             }  }
/*     */         
/*     */         } 
/*     */         
/* 223 */         if (!bool) {
/* 224 */           if (paramInt1 == 1 && !blockState2.isAir() && isPushable(blockState2, paramLevel, blockPos, direction.getOpposite(), false, direction) && (blockState2.getPistonPushReaction() == PushReaction.NORMAL || blockState2.is(Blocks.PISTON) || blockState2.is(Blocks.STICKY_PISTON))) {
/* 225 */             moveBlocks(paramLevel, paramBlockPos, direction, false);
/*     */           } else {
/* 227 */             paramLevel.removeBlock(paramBlockPos.relative(direction), false);
/*     */           } 
/*     */         }
/*     */       } else {
/* 231 */         paramLevel.removeBlock(paramBlockPos.relative(direction), false);
/*     */       } 
/*     */       
/* 234 */       paramLevel.playSound(null, paramBlockPos, SoundEvents.PISTON_CONTRACT, SoundSource.BLOCKS, 0.5F, paramLevel.random.nextFloat() * 0.15F + 0.6F);
/* 235 */       paramLevel.gameEvent((Holder)GameEvent.BLOCK_DEACTIVATE, paramBlockPos, GameEvent.Context.of(blockState1));
/*     */     } 
/* 237 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean isPushable(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Direction paramDirection1, boolean paramBoolean, Direction paramDirection2) {
/* 241 */     if (paramBlockPos.getY() < paramLevel.getMinY() || paramBlockPos.getY() > paramLevel.getMaxY() || !paramLevel.getWorldBorder().isWithinBounds(paramBlockPos)) {
/* 242 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 246 */     if (paramBlockState.isAir()) {
/* 247 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 251 */     if (paramBlockState.is(Blocks.OBSIDIAN) || paramBlockState.is(Blocks.CRYING_OBSIDIAN) || paramBlockState.is(Blocks.RESPAWN_ANCHOR) || paramBlockState.is(Blocks.REINFORCED_DEEPSLATE)) {
/* 252 */       return false;
/*     */     }
/*     */     
/* 255 */     if (paramDirection1 == Direction.DOWN && paramBlockPos.getY() == paramLevel.getMinY()) {
/* 256 */       return false;
/*     */     }
/*     */     
/* 259 */     if (paramDirection1 == Direction.UP && paramBlockPos.getY() == paramLevel.getMaxY()) {
/* 260 */       return false;
/*     */     }
/*     */     
/* 263 */     if (paramBlockState.is(Blocks.PISTON) || paramBlockState.is(Blocks.STICKY_PISTON)) {
/*     */       
/* 265 */       if (((Boolean)paramBlockState.getValue((Property)EXTENDED)).booleanValue()) {
/* 266 */         return false;
/*     */       }
/*     */     } else {
/* 269 */       if (paramBlockState.getDestroySpeed((BlockGetter)paramLevel, paramBlockPos) == -1.0F) {
/* 270 */         return false;
/*     */       }
/*     */       
/* 273 */       switch (paramBlockState.getPistonPushReaction()) {
/*     */         case BLOCK:
/* 275 */           return false;
/*     */         case DESTROY:
/* 277 */           return paramBoolean;
/*     */         case PUSH_ONLY:
/* 279 */           return (paramDirection1 == paramDirection2);
/*     */       } 
/*     */ 
/*     */     
/*     */     } 
/* 284 */     return !paramBlockState.hasBlockEntity();
/*     */   }
/*     */   
/*     */   private boolean moveBlocks(Level paramLevel, BlockPos paramBlockPos, Direction paramDirection, boolean paramBoolean) {
/* 288 */     BlockPos blockPos = paramBlockPos.relative(paramDirection);
/* 289 */     if (!paramBoolean && paramLevel.getBlockState(blockPos).is(Blocks.PISTON_HEAD))
/*     */     {
/* 291 */       paramLevel.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 276);
/*     */     }
/*     */     
/* 294 */     PistonStructureResolver pistonStructureResolver = new PistonStructureResolver(paramLevel, paramBlockPos, paramDirection, paramBoolean);
/* 295 */     if (!pistonStructureResolver.resolve()) {
/* 296 */       return false;
/*     */     }
/*     */     
/* 299 */     HashMap<BlockPos, BlockState> hashMap = Maps.newHashMap();
/* 300 */     List<BlockPos> list1 = pistonStructureResolver.getToPush();
/* 301 */     ArrayList<BlockState> arrayList = Lists.newArrayList();
/* 302 */     for (BlockPos blockPos1 : list1) {
/* 303 */       BlockState blockState1 = paramLevel.getBlockState(blockPos1);
/* 304 */       arrayList.add(blockState1);
/* 305 */       hashMap.put(blockPos1, blockState1);
/*     */     } 
/* 307 */     List<BlockPos> list2 = pistonStructureResolver.getToDestroy();
/*     */     
/* 309 */     BlockState[] arrayOfBlockState = new BlockState[list1.size() + list2.size()];
/* 310 */     Direction direction = paramBoolean ? paramDirection : paramDirection.getOpposite();
/*     */     
/* 312 */     byte b = 0;
/*     */     int i;
/* 314 */     for (i = list2.size() - 1; i >= 0; i--) {
/* 315 */       BlockPos blockPos1 = list2.get(i);
/*     */       
/* 317 */       BlockState blockState1 = paramLevel.getBlockState(blockPos1);
/*     */       
/* 319 */       BlockEntity blockEntity = blockState1.hasBlockEntity() ? paramLevel.getBlockEntity(blockPos1) : null;
/*     */       
/* 321 */       dropResources(blockState1, (LevelAccessor)paramLevel, blockPos1, blockEntity);
/* 322 */       if (!blockState1.is(BlockTags.FIRE) && paramLevel.isClientSide()) {
/* 323 */         paramLevel.levelEvent(2001, blockPos1, getId(blockState1));
/*     */       }
/* 325 */       paramLevel.setBlock(blockPos1, Blocks.AIR.defaultBlockState(), 18);
/* 326 */       paramLevel.gameEvent((Holder)GameEvent.BLOCK_DESTROY, blockPos1, GameEvent.Context.of(blockState1));
/*     */       
/* 328 */       arrayOfBlockState[b++] = blockState1;
/*     */     } 
/*     */ 
/*     */     
/* 332 */     for (i = list1.size() - 1; i >= 0; i--) {
/* 333 */       BlockPos blockPos1 = list1.get(i);
/* 334 */       BlockState blockState1 = paramLevel.getBlockState(blockPos1);
/*     */       
/* 336 */       blockPos1 = blockPos1.relative(direction);
/*     */       
/* 338 */       hashMap.remove(blockPos1);
/*     */       
/* 340 */       BlockState blockState2 = (BlockState)Blocks.MOVING_PISTON.defaultBlockState().setValue((Property)FACING, (Comparable)paramDirection);
/* 341 */       paramLevel.setBlock(blockPos1, blockState2, 324);
/* 342 */       paramLevel.setBlockEntity(MovingPistonBlock.newMovingBlockEntity(blockPos1, blockState2, arrayList.get(i), paramDirection, paramBoolean, false));
/*     */       
/* 344 */       arrayOfBlockState[b++] = blockState1;
/*     */     } 
/*     */     
/* 347 */     if (paramBoolean) {
/* 348 */       PistonType pistonType = this.isSticky ? PistonType.STICKY : PistonType.DEFAULT;
/* 349 */       BlockState blockState1 = (BlockState)((BlockState)Blocks.PISTON_HEAD.defaultBlockState().setValue((Property)PistonHeadBlock.FACING, (Comparable)paramDirection)).setValue((Property)PistonHeadBlock.TYPE, (Comparable)pistonType);
/*     */ 
/*     */ 
/*     */       
/* 353 */       BlockState blockState2 = (BlockState)((BlockState)Blocks.MOVING_PISTON.defaultBlockState().setValue((Property)MovingPistonBlock.FACING, (Comparable)paramDirection)).setValue((Property)MovingPistonBlock.TYPE, this.isSticky ? (Comparable)PistonType.STICKY : (Comparable)PistonType.DEFAULT);
/*     */       
/* 355 */       hashMap.remove(blockPos);
/*     */       
/* 357 */       paramLevel.setBlock(blockPos, blockState2, 324);
/* 358 */       paramLevel.setBlockEntity(MovingPistonBlock.newMovingBlockEntity(blockPos, blockState2, blockState1, paramDirection, true, true));
/*     */     } 
/*     */     
/* 361 */     BlockState blockState = Blocks.AIR.defaultBlockState();
/* 362 */     for (BlockPos blockPos1 : hashMap.keySet()) {
/* 363 */       paramLevel.setBlock(blockPos1, blockState, 82);
/*     */     }
/*     */     
/* 366 */     for (Map.Entry<BlockPos, BlockState> entry : hashMap.entrySet()) {
/* 367 */       BlockPos blockPos1 = (BlockPos)entry.getKey();
/* 368 */       BlockState blockState1 = (BlockState)entry.getValue();
/* 369 */       blockState1.updateIndirectNeighbourShapes((LevelAccessor)paramLevel, blockPos1, 2);
/* 370 */       blockState.updateNeighbourShapes((LevelAccessor)paramLevel, blockPos1, 2);
/* 371 */       blockState.updateIndirectNeighbourShapes((LevelAccessor)paramLevel, blockPos1, 2);
/*     */     } 
/*     */     
/* 374 */     Orientation orientation = ExperimentalRedstoneUtils.initialOrientation(paramLevel, pistonStructureResolver.getPushDirection(), null);
/* 375 */     b = 0;
/*     */     int j;
/* 377 */     for (j = list2.size() - 1; j >= 0; j--) {
/* 378 */       BlockState blockState1 = arrayOfBlockState[b++];
/* 379 */       BlockPos blockPos1 = list2.get(j);
/* 380 */       if (paramLevel instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)paramLevel;
/* 381 */         blockState1.affectNeighborsAfterRemoval(serverLevel, blockPos1, false); }
/*     */       
/* 383 */       blockState1.updateIndirectNeighbourShapes((LevelAccessor)paramLevel, blockPos1, 2);
/* 384 */       paramLevel.updateNeighborsAt(blockPos1, blockState1.getBlock(), orientation);
/*     */     } 
/*     */ 
/*     */     
/* 388 */     for (j = list1.size() - 1; j >= 0; j--) {
/* 389 */       paramLevel.updateNeighborsAt(list1.get(j), arrayOfBlockState[b++].getBlock(), orientation);
/*     */     }
/*     */     
/* 392 */     if (paramBoolean) {
/* 393 */       paramLevel.updateNeighborsAt(blockPos, Blocks.PISTON_HEAD, orientation);
/*     */     }
/*     */     
/* 396 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 401 */     return (BlockState)paramBlockState.setValue((Property)FACING, (Comparable)paramRotation.rotate((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 406 */     return paramBlockState.rotate(paramMirror.getRotation((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 411 */     paramBuilder.add(new Property[] { (Property)FACING, (Property)EXTENDED });
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean useShapeForLightOcclusion(BlockState paramBlockState) {
/* 416 */     return ((Boolean)paramBlockState.getValue((Property)EXTENDED)).booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/* 421 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\piston\PistonBaseBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */