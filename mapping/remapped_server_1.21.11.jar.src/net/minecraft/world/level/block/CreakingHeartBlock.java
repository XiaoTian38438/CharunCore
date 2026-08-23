/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.ServerExplosion;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.CreakingHeartBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.CreakingHeartState;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class CreakingHeartBlock extends BaseEntityBlock {
/*  38 */   public static final MapCodec<CreakingHeartBlock> CODEC = simpleCodec(CreakingHeartBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<CreakingHeartBlock> codec() {
/*  42 */     return CODEC;
/*     */   }
/*     */   
/*  45 */   public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;
/*  46 */   public static final EnumProperty<CreakingHeartState> STATE = BlockStateProperties.CREAKING_HEART_STATE;
/*  47 */   public static final BooleanProperty NATURAL = BlockStateProperties.NATURAL;
/*     */   
/*     */   protected CreakingHeartBlock(BlockBehaviour.Properties paramProperties) {
/*  50 */     super(paramProperties);
/*  51 */     registerDefaultState((BlockState)((BlockState)((BlockState)defaultBlockState().setValue((Property)AXIS, (Comparable)Direction.Axis.Y)).setValue((Property)STATE, (Comparable)CreakingHeartState.UPROOTED)).setValue((Property)NATURAL, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/*  56 */     return (BlockEntity)new CreakingHeartBlockEntity(paramBlockPos, paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level paramLevel, BlockState paramBlockState, BlockEntityType<T> paramBlockEntityType) {
/*  61 */     if (paramLevel.isClientSide()) {
/*  62 */       return null;
/*     */     }
/*  64 */     if (paramBlockState.getValue((Property)STATE) != CreakingHeartState.UPROOTED) {
/*  65 */       return createTickerHelper(paramBlockEntityType, BlockEntityType.CREAKING_HEART, CreakingHeartBlockEntity::serverTick);
/*     */     }
/*  67 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  72 */     if (!((Boolean)paramLevel.environmentAttributes().getValue(EnvironmentAttributes.CREAKING_ACTIVE, paramBlockPos)).booleanValue()) {
/*     */       return;
/*     */     }
/*  75 */     if (paramBlockState.getValue((Property)STATE) == CreakingHeartState.UPROOTED) {
/*     */       return;
/*     */     }
/*  78 */     if (paramRandomSource.nextInt(16) == 0 && isSurroundedByLogs((LevelAccessor)paramLevel, paramBlockPos)) {
/*  79 */       paramLevel.playLocalSound(paramBlockPos.getX(), paramBlockPos.getY(), paramBlockPos.getZ(), SoundEvents.CREAKING_HEART_IDLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/*  85 */     paramScheduledTickAccess.scheduleTick(paramBlockPos1, this, 1);
/*  86 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  91 */     BlockState blockState = updateState(paramBlockState, (Level)paramServerLevel, paramBlockPos);
/*  92 */     if (blockState != paramBlockState) {
/*  93 */       paramServerLevel.setBlock(paramBlockPos, blockState, 3);
/*     */     }
/*     */   }
/*     */   
/*     */   private static BlockState updateState(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos) {
/*  98 */     boolean bool = hasRequiredLogs(paramBlockState, (LevelReader)paramLevel, paramBlockPos);
/*  99 */     boolean bool1 = (paramBlockState.getValue((Property)STATE) == CreakingHeartState.UPROOTED) ? true : false;
/* 100 */     if (bool && bool1) {
/* 101 */       return (BlockState)paramBlockState.setValue((Property)STATE, ((Boolean)paramLevel.environmentAttributes().getValue(EnvironmentAttributes.CREAKING_ACTIVE, paramBlockPos)).booleanValue() ? (Comparable)CreakingHeartState.AWAKE : (Comparable)CreakingHeartState.DORMANT);
/*     */     }
/* 103 */     return paramBlockState;
/*     */   }
/*     */   
/*     */   public static boolean hasRequiredLogs(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/* 107 */     Direction.Axis axis = (Direction.Axis)paramBlockState.getValue((Property)AXIS);
/* 108 */     for (Direction direction : axis.getDirections()) {
/* 109 */       BlockState blockState = paramLevelReader.getBlockState(paramBlockPos.relative(direction));
/* 110 */       if (!blockState.is(BlockTags.PALE_OAK_LOGS) || blockState.getValue((Property)AXIS) != axis) {
/* 111 */         return false;
/*     */       }
/*     */     } 
/* 114 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean isSurroundedByLogs(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos) {
/* 118 */     for (Direction direction : Direction.values()) {
/* 119 */       BlockPos blockPos = paramBlockPos.relative(direction);
/* 120 */       BlockState blockState = paramLevelAccessor.getBlockState(blockPos);
/* 121 */       if (!blockState.is(BlockTags.PALE_OAK_LOGS)) {
/* 122 */         return false;
/*     */       }
/*     */     } 
/* 125 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 130 */     return updateState((BlockState)defaultBlockState().setValue((Property)AXIS, (Comparable)paramBlockPlaceContext.getClickedFace().getAxis()), paramBlockPlaceContext.getLevel(), paramBlockPlaceContext.getClickedPos());
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 135 */     return RotatedPillarBlock.rotatePillar(paramBlockState, paramRotation);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 140 */     paramBuilder.add(new Property[] { (Property)AXIS, (Property)STATE, (Property)NATURAL });
/*     */   }
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, boolean paramBoolean) {
/* 145 */     Containers.updateNeighboursAfterDestroy(paramBlockState, (Level)paramServerLevel, paramBlockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onExplosionHit(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, Explosion paramExplosion, BiConsumer<ItemStack, BlockPos> paramBiConsumer) {
/* 150 */     BlockEntity blockEntity = paramServerLevel.getBlockEntity(paramBlockPos); if (blockEntity instanceof CreakingHeartBlockEntity) { CreakingHeartBlockEntity creakingHeartBlockEntity = (CreakingHeartBlockEntity)blockEntity; if (paramExplosion instanceof ServerExplosion) { ServerExplosion serverExplosion = (ServerExplosion)paramExplosion; if (paramExplosion.getBlockInteraction().shouldAffectBlocklikeEntities()) {
/* 151 */           creakingHeartBlockEntity.removeProtector(serverExplosion.getDamageSource());
/* 152 */           LivingEntity livingEntity = paramExplosion.getIndirectSourceEntity(); if (livingEntity instanceof Player) { Player player = (Player)livingEntity; if (paramExplosion.getBlockInteraction().shouldAffectBlocklikeEntities())
/* 153 */               tryAwardExperience(player, paramBlockState, (Level)paramServerLevel, paramBlockPos);  } 
/*     */         }  }
/*     */        }
/* 156 */      super.onExplosionHit(paramBlockState, paramServerLevel, paramBlockPos, paramExplosion, paramBiConsumer);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState playerWillDestroy(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, Player paramPlayer) {
/* 161 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos); if (blockEntity instanceof CreakingHeartBlockEntity) { CreakingHeartBlockEntity creakingHeartBlockEntity = (CreakingHeartBlockEntity)blockEntity;
/* 162 */       creakingHeartBlockEntity.removeProtector(paramPlayer.damageSources().playerAttack(paramPlayer));
/* 163 */       tryAwardExperience(paramPlayer, paramBlockState, paramLevel, paramBlockPos); }
/*     */     
/* 165 */     return super.playerWillDestroy(paramLevel, paramBlockPos, paramBlockState, paramPlayer);
/*     */   }
/*     */   
/*     */   private void tryAwardExperience(Player paramPlayer, BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos) {
/* 169 */     if (!paramPlayer.preventsBlockDrops() && !paramPlayer.isSpectator() && ((Boolean)paramBlockState.getValue((Property)NATURAL)).booleanValue() && paramLevel instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)paramLevel;
/* 170 */       popExperience(serverLevel, paramBlockPos, paramLevel.random.nextIntBetweenInclusive(20, 24)); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean hasAnalogOutputSignal(BlockState paramBlockState) {
/* 176 */     return true;
/*     */   }
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Direction paramDirection) {
/*     */     CreakingHeartBlockEntity creakingHeartBlockEntity;
/* 181 */     if (paramBlockState.getValue((Property)STATE) == CreakingHeartState.UPROOTED) {
/* 182 */       return 0;
/*     */     }
/* 184 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos); if (blockEntity instanceof CreakingHeartBlockEntity) { creakingHeartBlockEntity = (CreakingHeartBlockEntity)blockEntity; }
/* 185 */     else { return 0; }
/*     */     
/* 187 */     return creakingHeartBlockEntity.getAnalogOutputSignal();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\CreakingHeartBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */