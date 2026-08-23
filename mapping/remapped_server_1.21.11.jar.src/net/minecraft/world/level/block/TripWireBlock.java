/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
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
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class TripWireBlock extends Block {
/*     */   static {
/*  31 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)BuiltInRegistries.BLOCK.byNameCodec().fieldOf("hook").forGetter(()), (App)propertiesCodec()).apply((Applicative)paramInstance, TripWireBlock::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final MapCodec<TripWireBlock> CODEC;
/*     */   
/*     */   public MapCodec<TripWireBlock> codec() {
/*  38 */     return CODEC;
/*     */   }
/*     */   
/*  41 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*  42 */   public static final BooleanProperty ATTACHED = BlockStateProperties.ATTACHED;
/*  43 */   public static final BooleanProperty DISARMED = BlockStateProperties.DISARMED;
/*  44 */   public static final BooleanProperty NORTH = PipeBlock.NORTH;
/*  45 */   public static final BooleanProperty EAST = PipeBlock.EAST;
/*  46 */   public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
/*  47 */   public static final BooleanProperty WEST = PipeBlock.WEST;
/*     */   
/*  49 */   private static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = CrossCollisionBlock.PROPERTY_BY_DIRECTION;
/*     */   
/*  51 */   private static final VoxelShape SHAPE_ATTACHED = Block.column(16.0D, 1.0D, 2.5D);
/*  52 */   private static final VoxelShape SHAPE_NOT_ATTACHED = Block.column(16.0D, 0.0D, 8.0D);
/*     */   
/*     */   private static final int RECHECK_PERIOD = 10;
/*     */   
/*     */   private final Block hook;
/*     */   
/*     */   public TripWireBlock(Block paramBlock, BlockBehaviour.Properties paramProperties) {
/*  59 */     super(paramProperties);
/*  60 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)POWERED, Boolean.valueOf(false))).setValue((Property)ATTACHED, Boolean.valueOf(false))).setValue((Property)DISARMED, Boolean.valueOf(false))).setValue((Property)NORTH, Boolean.valueOf(false))).setValue((Property)EAST, Boolean.valueOf(false))).setValue((Property)SOUTH, Boolean.valueOf(false))).setValue((Property)WEST, Boolean.valueOf(false)));
/*  61 */     this.hook = paramBlock;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  66 */     return ((Boolean)paramBlockState.getValue((Property)ATTACHED)).booleanValue() ? SHAPE_ATTACHED : SHAPE_NOT_ATTACHED;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/*  71 */     Level level = paramBlockPlaceContext.getLevel();
/*  72 */     BlockPos blockPos = paramBlockPlaceContext.getClickedPos();
/*     */     
/*  74 */     return (BlockState)((BlockState)((BlockState)((BlockState)defaultBlockState()
/*  75 */       .setValue((Property)NORTH, Boolean.valueOf(shouldConnectTo(level.getBlockState(blockPos.north()), Direction.NORTH))))
/*  76 */       .setValue((Property)EAST, Boolean.valueOf(shouldConnectTo(level.getBlockState(blockPos.east()), Direction.EAST))))
/*  77 */       .setValue((Property)SOUTH, Boolean.valueOf(shouldConnectTo(level.getBlockState(blockPos.south()), Direction.SOUTH))))
/*  78 */       .setValue((Property)WEST, Boolean.valueOf(shouldConnectTo(level.getBlockState(blockPos.west()), Direction.WEST)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/*  83 */     if (paramDirection.getAxis().isHorizontal()) {
/*  84 */       return (BlockState)paramBlockState1.setValue((Property)PROPERTY_BY_DIRECTION.get(paramDirection), Boolean.valueOf(shouldConnectTo(paramBlockState2, paramDirection)));
/*     */     }
/*  86 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPlace(BlockState paramBlockState1, Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState2, boolean paramBoolean) {
/*  91 */     if (paramBlockState2.is(paramBlockState1.getBlock())) {
/*     */       return;
/*     */     }
/*  94 */     updateSource(paramLevel, paramBlockPos, paramBlockState1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, boolean paramBoolean) {
/*  99 */     if (!paramBoolean) {
/* 100 */       updateSource((Level)paramServerLevel, paramBlockPos, (BlockState)paramBlockState.setValue((Property)POWERED, Boolean.valueOf(true)));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState playerWillDestroy(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, Player paramPlayer) {
/* 106 */     if (!paramLevel.isClientSide() && !paramPlayer.getMainHandItem().isEmpty() && paramPlayer.getMainHandItem().is(Items.SHEARS)) {
/* 107 */       paramLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)DISARMED, Boolean.valueOf(true)), 260);
/* 108 */       paramLevel.gameEvent((Entity)paramPlayer, (Holder)GameEvent.SHEAR, paramBlockPos);
/*     */     } 
/* 110 */     return super.playerWillDestroy(paramLevel, paramBlockPos, paramBlockState, paramPlayer);
/*     */   }
/*     */   
/*     */   private void updateSource(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 114 */     for (Direction direction : new Direction[] { Direction.SOUTH, Direction.WEST }) {
/* 115 */       for (byte b = 1; b < 42; b++) {
/* 116 */         BlockPos blockPos = paramBlockPos.relative(direction, b);
/* 117 */         BlockState blockState = paramLevel.getBlockState(blockPos);
/*     */         
/* 119 */         if (blockState.is(this.hook)) {
/* 120 */           if (blockState.getValue((Property)TripWireHookBlock.FACING) == direction.getOpposite()) {
/* 121 */             TripWireHookBlock.calculateState(paramLevel, blockPos, blockState, false, true, b, paramBlockState);
/*     */           }
/*     */           break;
/*     */         } 
/* 125 */         if (!blockState.is(this)) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getEntityInsideCollisionShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Entity paramEntity) {
/* 134 */     return paramBlockState.getShape(paramBlockGetter, paramBlockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInside(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Entity paramEntity, InsideBlockEffectApplier paramInsideBlockEffectApplier, boolean paramBoolean) {
/* 139 */     if (paramLevel.isClientSide()) {
/*     */       return;
/*     */     }
/*     */     
/* 143 */     if (((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue()) {
/*     */       return;
/*     */     }
/*     */     
/* 147 */     checkPressed(paramLevel, paramBlockPos, List.of(paramEntity));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 152 */     if (!((Boolean)paramServerLevel.getBlockState(paramBlockPos).getValue((Property)POWERED)).booleanValue()) {
/*     */       return;
/*     */     }
/*     */     
/* 156 */     checkPressed((Level)paramServerLevel, paramBlockPos);
/*     */   }
/*     */   
/*     */   private void checkPressed(Level paramLevel, BlockPos paramBlockPos) {
/* 160 */     BlockState blockState = paramLevel.getBlockState(paramBlockPos);
/* 161 */     List<? extends Entity> list = paramLevel.getEntities(null, blockState.getShape((BlockGetter)paramLevel, paramBlockPos).bounds().move(paramBlockPos));
/* 162 */     checkPressed(paramLevel, paramBlockPos, list);
/*     */   }
/*     */   
/*     */   private void checkPressed(Level paramLevel, BlockPos paramBlockPos, List<? extends Entity> paramList) {
/* 166 */     BlockState blockState = paramLevel.getBlockState(paramBlockPos);
/* 167 */     boolean bool1 = ((Boolean)blockState.getValue((Property)POWERED)).booleanValue();
/* 168 */     boolean bool2 = false;
/*     */     
/* 170 */     if (!paramList.isEmpty()) {
/* 171 */       for (Entity entity : paramList) {
/* 172 */         if (!entity.isIgnoringBlockTriggers()) {
/* 173 */           bool2 = true;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/* 179 */     if (bool2 != bool1) {
/* 180 */       blockState = (BlockState)blockState.setValue((Property)POWERED, Boolean.valueOf(bool2));
/* 181 */       paramLevel.setBlock(paramBlockPos, blockState, 3);
/* 182 */       updateSource(paramLevel, paramBlockPos, blockState);
/*     */     } 
/*     */     
/* 185 */     if (bool2) {
/* 186 */       paramLevel.scheduleTick(new BlockPos((Vec3i)paramBlockPos), this, 10);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean shouldConnectTo(BlockState paramBlockState, Direction paramDirection) {
/* 191 */     if (paramBlockState.is(this.hook)) {
/* 192 */       return (paramBlockState.getValue((Property)TripWireHookBlock.FACING) == paramDirection.getOpposite());
/*     */     }
/*     */     
/* 195 */     return paramBlockState.is(this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 200 */     switch (paramRotation) {
/*     */       case LEFT_RIGHT:
/* 202 */         return (BlockState)((BlockState)((BlockState)((BlockState)paramBlockState.setValue((Property)NORTH, paramBlockState.getValue((Property)SOUTH))).setValue((Property)EAST, paramBlockState.getValue((Property)WEST))).setValue((Property)SOUTH, paramBlockState.getValue((Property)NORTH))).setValue((Property)WEST, paramBlockState.getValue((Property)EAST));
/*     */       case FRONT_BACK:
/* 204 */         return (BlockState)((BlockState)((BlockState)((BlockState)paramBlockState.setValue((Property)NORTH, paramBlockState.getValue((Property)EAST))).setValue((Property)EAST, paramBlockState.getValue((Property)SOUTH))).setValue((Property)SOUTH, paramBlockState.getValue((Property)WEST))).setValue((Property)WEST, paramBlockState.getValue((Property)NORTH));
/*     */       case null:
/* 206 */         return (BlockState)((BlockState)((BlockState)((BlockState)paramBlockState.setValue((Property)NORTH, paramBlockState.getValue((Property)WEST))).setValue((Property)EAST, paramBlockState.getValue((Property)NORTH))).setValue((Property)SOUTH, paramBlockState.getValue((Property)EAST))).setValue((Property)WEST, paramBlockState.getValue((Property)SOUTH));
/*     */     } 
/* 208 */     return paramBlockState;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 214 */     switch (paramMirror) {
/*     */       case LEFT_RIGHT:
/* 216 */         return (BlockState)((BlockState)paramBlockState.setValue((Property)NORTH, paramBlockState.getValue((Property)SOUTH))).setValue((Property)SOUTH, paramBlockState.getValue((Property)NORTH));
/*     */       case FRONT_BACK:
/* 218 */         return (BlockState)((BlockState)paramBlockState.setValue((Property)EAST, paramBlockState.getValue((Property)WEST))).setValue((Property)WEST, paramBlockState.getValue((Property)EAST));
/*     */     } 
/*     */ 
/*     */     
/* 222 */     return super.mirror(paramBlockState, paramMirror);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 227 */     paramBuilder.add(new Property[] { (Property)POWERED, (Property)ATTACHED, (Property)DISARMED, (Property)NORTH, (Property)EAST, (Property)WEST, (Property)SOUTH });
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\TripWireBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */