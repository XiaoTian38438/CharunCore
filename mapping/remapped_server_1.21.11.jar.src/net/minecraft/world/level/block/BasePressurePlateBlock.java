/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockSetType;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public abstract class BasePressurePlateBlock extends Block {
/*  27 */   private static final VoxelShape SHAPE_PRESSED = Block.column(14.0D, 0.0D, 0.5D);
/*  28 */   private static final VoxelShape SHAPE = Block.column(14.0D, 0.0D, 1.0D);
/*  29 */   protected static final AABB TOUCH_AABB = Block.column(14.0D, 0.0D, 4.0D).toAabbs().getFirst();
/*     */   
/*     */   protected final BlockSetType type;
/*     */   
/*     */   protected BasePressurePlateBlock(BlockBehaviour.Properties paramProperties, BlockSetType paramBlockSetType) {
/*  34 */     super(paramProperties.sound(paramBlockSetType.soundType()));
/*  35 */     this.type = paramBlockSetType;
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract MapCodec<? extends BasePressurePlateBlock> codec();
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  43 */     return (getSignalForState(paramBlockState) > 0) ? SHAPE_PRESSED : SHAPE;
/*     */   }
/*     */   
/*     */   protected int getPressedTime() {
/*  47 */     return 20;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPossibleToRespawnInThis(BlockState paramBlockState) {
/*  52 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/*  57 */     if (paramDirection == Direction.DOWN && !paramBlockState1.canSurvive(paramLevelReader, paramBlockPos1)) {
/*  58 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*  60 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/*  65 */     BlockPos blockPos = paramBlockPos.below();
/*  66 */     return (canSupportRigidBlock((BlockGetter)paramLevelReader, blockPos) || canSupportCenter(paramLevelReader, blockPos, Direction.UP));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  71 */     int i = getSignalForState(paramBlockState);
/*  72 */     if (i > 0) {
/*  73 */       checkPressed((Entity)null, (Level)paramServerLevel, paramBlockPos, paramBlockState, i);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInside(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Entity paramEntity, InsideBlockEffectApplier paramInsideBlockEffectApplier, boolean paramBoolean) {
/*  79 */     if (paramLevel.isClientSide()) {
/*     */       return;
/*     */     }
/*     */     
/*  83 */     int i = getSignalForState(paramBlockState);
/*  84 */     if (i == 0) {
/*  85 */       checkPressed(paramEntity, paramLevel, paramBlockPos, paramBlockState, i);
/*     */     }
/*     */   }
/*     */   
/*     */   private void checkPressed(Entity paramEntity, Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, int paramInt) {
/*  90 */     int i = getSignalStrength(paramLevel, paramBlockPos);
/*  91 */     boolean bool1 = (paramInt > 0) ? true : false;
/*  92 */     boolean bool2 = (i > 0) ? true : false;
/*     */     
/*  94 */     if (paramInt != i) {
/*  95 */       BlockState blockState = setSignalForState(paramBlockState, i);
/*  96 */       paramLevel.setBlock(paramBlockPos, blockState, 2);
/*  97 */       updateNeighbours(paramLevel, paramBlockPos);
/*  98 */       paramLevel.setBlocksDirty(paramBlockPos, paramBlockState, blockState);
/*     */     } 
/*     */     
/* 101 */     if (!bool2 && bool1) {
/* 102 */       paramLevel.playSound(null, paramBlockPos, this.type.pressurePlateClickOff(), SoundSource.BLOCKS);
/* 103 */       paramLevel.gameEvent(paramEntity, (Holder)GameEvent.BLOCK_DEACTIVATE, paramBlockPos);
/* 104 */     } else if (bool2 && !bool1) {
/* 105 */       paramLevel.playSound(null, paramBlockPos, this.type.pressurePlateClickOn(), SoundSource.BLOCKS);
/* 106 */       paramLevel.gameEvent(paramEntity, (Holder)GameEvent.BLOCK_ACTIVATE, paramBlockPos);
/*     */     } 
/*     */     
/* 109 */     if (bool2) {
/* 110 */       paramLevel.scheduleTick(new BlockPos((Vec3i)paramBlockPos), this, getPressedTime());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, boolean paramBoolean) {
/* 116 */     if (!paramBoolean && getSignalForState(paramBlockState) > 0) {
/* 117 */       updateNeighbours((Level)paramServerLevel, paramBlockPos);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void updateNeighbours(Level paramLevel, BlockPos paramBlockPos) {
/* 122 */     paramLevel.updateNeighborsAt(paramBlockPos, this);
/* 123 */     paramLevel.updateNeighborsAt(paramBlockPos.below(), this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getSignal(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection) {
/* 128 */     return getSignalForState(paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getDirectSignal(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection) {
/* 133 */     if (paramDirection == Direction.UP) {
/* 134 */       return getSignalForState(paramBlockState);
/*     */     }
/*     */     
/* 137 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isSignalSource(BlockState paramBlockState) {
/* 142 */     return true;
/*     */   }
/*     */   
/*     */   protected static int getEntityCount(Level paramLevel, AABB paramAABB, Class<? extends Entity> paramClass) {
/* 146 */     return paramLevel.getEntitiesOfClass(paramClass, paramAABB, EntitySelector.NO_SPECTATORS.and(paramEntity -> !paramEntity.isIgnoringBlockTriggers())).size();
/*     */   }
/*     */   
/*     */   protected abstract int getSignalStrength(Level paramLevel, BlockPos paramBlockPos);
/*     */   
/*     */   protected abstract int getSignalForState(BlockState paramBlockState);
/*     */   
/*     */   protected abstract BlockState setSignalForState(BlockState paramBlockState, int paramInt);
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\BasePressurePlateBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */