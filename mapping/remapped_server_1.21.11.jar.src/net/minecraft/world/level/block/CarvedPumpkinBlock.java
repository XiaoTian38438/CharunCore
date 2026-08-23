/*     */ package net.minecraft.world.level.block;
/*     */ import com.google.common.collect.BiMap;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.animal.golem.CopperGolem;
/*     */ import net.minecraft.world.entity.animal.golem.IronGolem;
/*     */ import net.minecraft.world.entity.animal.golem.SnowGolem;
/*     */ import net.minecraft.world.item.HoneycombItem;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockInWorld;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockPattern;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
/*     */ import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class CarvedPumpkinBlock extends HorizontalDirectionalBlock {
/*  32 */   public static final MapCodec<CarvedPumpkinBlock> CODEC = simpleCodec(CarvedPumpkinBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<? extends CarvedPumpkinBlock> codec() {
/*  36 */     return CODEC;
/*     */   }
/*     */   
/*  39 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*     */   
/*     */   private BlockPattern snowGolemBase;
/*     */   
/*     */   private BlockPattern snowGolemFull;
/*     */   
/*     */   private BlockPattern ironGolemBase;
/*     */   
/*     */   private BlockPattern ironGolemFull;
/*     */   
/*     */   private BlockPattern copperGolemBase;
/*     */   private BlockPattern copperGolemFull;
/*     */   private static final Predicate<BlockState> PUMPKINS_PREDICATE;
/*     */   
/*     */   protected CarvedPumpkinBlock(BlockBehaviour.Properties paramProperties) {
/*  54 */     super(paramProperties);
/*  55 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPlace(BlockState paramBlockState1, Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState2, boolean paramBoolean) {
/*  60 */     if (paramBlockState2.is(paramBlockState1.getBlock())) {
/*     */       return;
/*     */     }
/*  63 */     trySpawnGolem(paramLevel, paramBlockPos);
/*     */   }
/*     */   
/*     */   public boolean canSpawnGolem(LevelReader paramLevelReader, BlockPos paramBlockPos) {
/*  67 */     return (getOrCreateSnowGolemBase().find(paramLevelReader, paramBlockPos) != null || getOrCreateIronGolemBase().find(paramLevelReader, paramBlockPos) != null || getOrCreateCopperGolemBase().find(paramLevelReader, paramBlockPos) != null);
/*     */   }
/*     */   
/*     */   private void trySpawnGolem(Level paramLevel, BlockPos paramBlockPos) {
/*  71 */     BlockPattern.BlockPatternMatch blockPatternMatch1 = getOrCreateSnowGolemFull().find((LevelReader)paramLevel, paramBlockPos);
/*  72 */     if (blockPatternMatch1 != null) {
/*  73 */       SnowGolem snowGolem = (SnowGolem)EntityType.SNOW_GOLEM.create(paramLevel, EntitySpawnReason.TRIGGERED);
/*  74 */       if (snowGolem != null) {
/*  75 */         spawnGolemInWorld(paramLevel, blockPatternMatch1, (Entity)snowGolem, blockPatternMatch1.getBlock(0, 2, 0).getPos());
/*     */         return;
/*     */       } 
/*     */     } 
/*  79 */     BlockPattern.BlockPatternMatch blockPatternMatch2 = getOrCreateIronGolemFull().find((LevelReader)paramLevel, paramBlockPos);
/*  80 */     if (blockPatternMatch2 != null) {
/*  81 */       IronGolem ironGolem = (IronGolem)EntityType.IRON_GOLEM.create(paramLevel, EntitySpawnReason.TRIGGERED);
/*  82 */       if (ironGolem != null) {
/*  83 */         ironGolem.setPlayerCreated(true);
/*  84 */         spawnGolemInWorld(paramLevel, blockPatternMatch2, (Entity)ironGolem, blockPatternMatch2.getBlock(1, 2, 0).getPos());
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*  89 */     BlockPattern.BlockPatternMatch blockPatternMatch3 = getOrCreateCopperGolemFull().find((LevelReader)paramLevel, paramBlockPos);
/*  90 */     if (blockPatternMatch3 != null) {
/*  91 */       CopperGolem copperGolem = (CopperGolem)EntityType.COPPER_GOLEM.create(paramLevel, EntitySpawnReason.TRIGGERED);
/*  92 */       if (copperGolem != null) {
/*  93 */         spawnGolemInWorld(paramLevel, blockPatternMatch3, (Entity)copperGolem, blockPatternMatch3.getBlock(0, 0, 0).getPos());
/*  94 */         replaceCopperBlockWithChest(paramLevel, blockPatternMatch3);
/*  95 */         copperGolem.spawn(getWeatherStateFromPattern(blockPatternMatch3));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private WeatheringCopper.WeatherState getWeatherStateFromPattern(BlockPattern.BlockPatternMatch paramBlockPatternMatch) {
/* 101 */     BlockState blockState = paramBlockPatternMatch.getBlock(0, 1, 0).getState();
/* 102 */     Block block = blockState.getBlock();
/* 103 */     if (block instanceof WeatheringCopper) { WeatheringCopper weatheringCopper = (WeatheringCopper)block;
/* 104 */       return weatheringCopper.getAge(); }
/*     */     
/* 106 */     return ((WeatheringCopper)Optional.<Block>ofNullable((Block)((BiMap)HoneycombItem.WAX_OFF_BY_BLOCK.get()).get(blockState.getBlock()))
/* 107 */       .filter(paramBlock -> paramBlock instanceof WeatheringCopper)
/* 108 */       .map(paramBlock -> (WeatheringCopper)paramBlock)
/* 109 */       .orElse((WeatheringCopper)Blocks.COPPER_BLOCK)).getAge();
/*     */   }
/*     */   
/*     */   private static void spawnGolemInWorld(Level paramLevel, BlockPattern.BlockPatternMatch paramBlockPatternMatch, Entity paramEntity, BlockPos paramBlockPos) {
/* 113 */     clearPatternBlocks(paramLevel, paramBlockPatternMatch);
/*     */     
/* 115 */     paramEntity.snapTo(paramBlockPos.getX() + 0.5D, paramBlockPos.getY() + 0.05D, paramBlockPos.getZ() + 0.5D, 0.0F, 0.0F);
/* 116 */     paramLevel.addFreshEntity(paramEntity);
/*     */     
/* 118 */     for (ServerPlayer serverPlayer : paramLevel.getEntitiesOfClass(ServerPlayer.class, paramEntity.getBoundingBox().inflate(5.0D))) {
/* 119 */       CriteriaTriggers.SUMMONED_ENTITY.trigger(serverPlayer, paramEntity);
/*     */     }
/*     */     
/* 122 */     updatePatternBlocks(paramLevel, paramBlockPatternMatch);
/*     */   }
/*     */   
/*     */   public static void clearPatternBlocks(Level paramLevel, BlockPattern.BlockPatternMatch paramBlockPatternMatch) {
/* 126 */     for (byte b = 0; b < paramBlockPatternMatch.getWidth(); b++) {
/* 127 */       for (byte b1 = 0; b1 < paramBlockPatternMatch.getHeight(); b1++) {
/* 128 */         BlockInWorld blockInWorld = paramBlockPatternMatch.getBlock(b, b1, 0);
/* 129 */         paramLevel.setBlock(blockInWorld.getPos(), Blocks.AIR.defaultBlockState(), 2);
/* 130 */         paramLevel.levelEvent(2001, blockInWorld.getPos(), Block.getId(blockInWorld.getState()));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void updatePatternBlocks(Level paramLevel, BlockPattern.BlockPatternMatch paramBlockPatternMatch) {
/* 136 */     for (byte b = 0; b < paramBlockPatternMatch.getWidth(); b++) {
/* 137 */       for (byte b1 = 0; b1 < paramBlockPatternMatch.getHeight(); b1++) {
/* 138 */         BlockInWorld blockInWorld = paramBlockPatternMatch.getBlock(b, b1, 0);
/* 139 */         paramLevel.updateNeighborsAt(blockInWorld.getPos(), Blocks.AIR);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 146 */     return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)paramBlockPlaceContext.getHorizontalDirection().getOpposite());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 151 */     paramBuilder.add(new Property[] { (Property)FACING });
/*     */   }
/*     */   static {
/* 154 */     PUMPKINS_PREDICATE = (paramBlockState -> (paramBlockState.is(Blocks.CARVED_PUMPKIN) || paramBlockState.is(Blocks.JACK_O_LANTERN)));
/*     */   }
/*     */   private BlockPattern getOrCreateSnowGolemBase() {
/* 157 */     if (this.snowGolemBase == null) {
/* 158 */       this
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 165 */         .snowGolemBase = BlockPatternBuilder.start().aisle(new String[] { " ", "#", "#" }).where('#', BlockInWorld.hasState((Predicate)BlockStatePredicate.forBlock(Blocks.SNOW_BLOCK))).build();
/*     */     }
/*     */     
/* 168 */     return this.snowGolemBase;
/*     */   }
/*     */   
/*     */   private BlockPattern getOrCreateSnowGolemFull() {
/* 172 */     if (this.snowGolemFull == null) {
/* 173 */       this
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 181 */         .snowGolemFull = BlockPatternBuilder.start().aisle(new String[] { "^", "#", "#" }).where('^', BlockInWorld.hasState(PUMPKINS_PREDICATE)).where('#', BlockInWorld.hasState((Predicate)BlockStatePredicate.forBlock(Blocks.SNOW_BLOCK))).build();
/*     */     }
/*     */     
/* 184 */     return this.snowGolemFull;
/*     */   }
/*     */   
/*     */   private BlockPattern getOrCreateIronGolemBase() {
/* 188 */     if (this.ironGolemBase == null) {
/* 189 */       this
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 197 */         .ironGolemBase = BlockPatternBuilder.start().aisle(new String[] { "~ ~", "###", "~#~" }).where('#', BlockInWorld.hasState((Predicate)BlockStatePredicate.forBlock(Blocks.IRON_BLOCK))).where('~', BlockInWorld.hasState(BlockBehaviour.BlockStateBase::isAir)).build();
/*     */     }
/*     */     
/* 200 */     return this.ironGolemBase;
/*     */   }
/*     */   
/*     */   private BlockPattern getOrCreateIronGolemFull() {
/* 204 */     if (this.ironGolemFull == null) {
/* 205 */       this
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 214 */         .ironGolemFull = BlockPatternBuilder.start().aisle(new String[] { "~^~", "###", "~#~" }).where('^', BlockInWorld.hasState(PUMPKINS_PREDICATE)).where('#', BlockInWorld.hasState((Predicate)BlockStatePredicate.forBlock(Blocks.IRON_BLOCK))).where('~', BlockInWorld.hasState(BlockBehaviour.BlockStateBase::isAir)).build();
/*     */     }
/*     */     
/* 217 */     return this.ironGolemFull;
/*     */   }
/*     */   
/*     */   private BlockPattern getOrCreateCopperGolemBase() {
/* 221 */     if (this.copperGolemBase == null) {
/* 222 */       this
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 228 */         .copperGolemBase = BlockPatternBuilder.start().aisle(new String[] { " ", "#" }).where('#', BlockInWorld.hasState(paramBlockState -> paramBlockState.is(BlockTags.COPPER))).build();
/*     */     }
/*     */     
/* 231 */     return this.copperGolemBase;
/*     */   }
/*     */   
/*     */   private BlockPattern getOrCreateCopperGolemFull() {
/* 235 */     if (this.copperGolemFull == null) {
/* 236 */       this
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 243 */         .copperGolemFull = BlockPatternBuilder.start().aisle(new String[] { "^", "#" }).where('^', BlockInWorld.hasState(PUMPKINS_PREDICATE)).where('#', BlockInWorld.hasState(paramBlockState -> paramBlockState.is(BlockTags.COPPER))).build();
/*     */     }
/*     */     
/* 246 */     return this.copperGolemFull;
/*     */   }
/*     */   
/*     */   public void replaceCopperBlockWithChest(Level paramLevel, BlockPattern.BlockPatternMatch paramBlockPatternMatch) {
/* 250 */     BlockInWorld blockInWorld1 = paramBlockPatternMatch.getBlock(0, 1, 0);
/* 251 */     BlockInWorld blockInWorld2 = paramBlockPatternMatch.getBlock(0, 0, 0);
/* 252 */     Direction direction = (Direction)blockInWorld2.getState().getValue((Property)FACING);
/* 253 */     BlockState blockState = CopperChestBlock.getFromCopperBlock(blockInWorld1.getState().getBlock(), direction, paramLevel, blockInWorld1.getPos());
/* 254 */     paramLevel.setBlock(blockInWorld1.getPos(), blockState, 2);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\CarvedPumpkinBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */