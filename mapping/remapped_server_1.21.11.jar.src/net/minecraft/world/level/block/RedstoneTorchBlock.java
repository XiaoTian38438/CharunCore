/*     */ package net.minecraft.world.level.block;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.DustParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ 
/*     */ public class RedstoneTorchBlock extends BaseTorchBlock {
/*  26 */   public static final MapCodec<RedstoneTorchBlock> CODEC = simpleCodec(RedstoneTorchBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<? extends RedstoneTorchBlock> codec() {
/*  30 */     return CODEC;
/*     */   }
/*     */   
/*  33 */   public static final BooleanProperty LIT = BlockStateProperties.LIT;
/*     */ 
/*     */   
/*  36 */   private static final Map<BlockGetter, List<Toggle>> RECENT_TOGGLES = new WeakHashMap<>();
/*     */   
/*     */   public static final int RECENT_TOGGLE_TIMER = 60;
/*     */   public static final int MAX_RECENT_TOGGLES = 8;
/*     */   public static final int RESTART_DELAY = 160;
/*     */   private static final int TOGGLE_DELAY = 2;
/*     */   
/*     */   protected RedstoneTorchBlock(BlockBehaviour.Properties paramProperties) {
/*  44 */     super(paramProperties);
/*  45 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)LIT, Boolean.valueOf(true)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPlace(BlockState paramBlockState1, Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState2, boolean paramBoolean) {
/*  50 */     notifyNeighbors(paramLevel, paramBlockPos, paramBlockState1);
/*     */   }
/*     */   
/*     */   private void notifyNeighbors(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState) {
/*  54 */     Orientation orientation = randomOrientation(paramLevel, paramBlockState);
/*  55 */     for (Direction direction : Direction.values()) {
/*  56 */       paramLevel.updateNeighborsAt(paramBlockPos.relative(direction), this, ExperimentalRedstoneUtils.withFront(orientation, direction));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, boolean paramBoolean) {
/*  62 */     if (!paramBoolean) {
/*  63 */       notifyNeighbors((Level)paramServerLevel, paramBlockPos, paramBlockState);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getSignal(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection) {
/*  69 */     if (((Boolean)paramBlockState.getValue((Property)LIT)).booleanValue() && Direction.UP != paramDirection) {
/*  70 */       return 15;
/*     */     }
/*     */     
/*  73 */     return 0;
/*     */   }
/*     */   
/*     */   protected boolean hasNeighborSignal(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState) {
/*  77 */     return paramLevel.hasSignal(paramBlockPos.below(), Direction.DOWN);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  82 */     boolean bool = hasNeighborSignal((Level)paramServerLevel, paramBlockPos, paramBlockState);
/*     */     
/*  84 */     List list = RECENT_TOGGLES.get(paramServerLevel);
/*  85 */     while (list != null && !list.isEmpty() && paramServerLevel.getGameTime() - ((Toggle)list.get(0)).when > 60L) {
/*  86 */       list.remove(0);
/*     */     }
/*     */     
/*  89 */     if (((Boolean)paramBlockState.getValue((Property)LIT)).booleanValue()) {
/*  90 */       if (bool) {
/*  91 */         paramServerLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)LIT, Boolean.valueOf(false)), 3);
/*     */         
/*  93 */         if (isToggledTooFrequently((Level)paramServerLevel, paramBlockPos, true)) {
/*  94 */           paramServerLevel.levelEvent(1502, paramBlockPos, 0);
/*  95 */           paramServerLevel.scheduleTick(paramBlockPos, paramServerLevel.getBlockState(paramBlockPos).getBlock(), 160);
/*     */         }
/*     */       
/*     */       } 
/*  99 */     } else if (!bool && !isToggledTooFrequently((Level)paramServerLevel, paramBlockPos, false)) {
/* 100 */       paramServerLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)LIT, Boolean.valueOf(true)), 3);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void neighborChanged(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Block paramBlock, Orientation paramOrientation, boolean paramBoolean) {
/* 107 */     if (((Boolean)paramBlockState.getValue((Property)LIT)).booleanValue() == hasNeighborSignal(paramLevel, paramBlockPos, paramBlockState) && !paramLevel.getBlockTicks().willTickThisTick(paramBlockPos, this)) {
/* 108 */       paramLevel.scheduleTick(paramBlockPos, this, 2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getDirectSignal(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection) {
/* 114 */     if (paramDirection == Direction.DOWN) {
/* 115 */       return paramBlockState.getSignal(paramBlockGetter, paramBlockPos, paramDirection);
/*     */     }
/* 117 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isSignalSource(BlockState paramBlockState) {
/* 122 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 127 */     if (!((Boolean)paramBlockState.getValue((Property)LIT)).booleanValue()) {
/*     */       return;
/*     */     }
/*     */     
/* 131 */     double d1 = paramBlockPos.getX() + 0.5D + (paramRandomSource.nextDouble() - 0.5D) * 0.2D;
/* 132 */     double d2 = paramBlockPos.getY() + 0.7D + (paramRandomSource.nextDouble() - 0.5D) * 0.2D;
/* 133 */     double d3 = paramBlockPos.getZ() + 0.5D + (paramRandomSource.nextDouble() - 0.5D) * 0.2D;
/*     */     
/* 135 */     paramLevel.addParticle((ParticleOptions)DustParticleOptions.REDSTONE, d1, d2, d3, 0.0D, 0.0D, 0.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 140 */     paramBuilder.add(new Property[] { (Property)LIT });
/*     */   }
/*     */   
/*     */   public static class Toggle {
/*     */     final BlockPos pos;
/*     */     final long when;
/*     */     
/*     */     public Toggle(BlockPos param1BlockPos, long param1Long) {
/* 148 */       this.pos = param1BlockPos;
/* 149 */       this.when = param1Long;
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean isToggledTooFrequently(Level paramLevel, BlockPos paramBlockPos, boolean paramBoolean) {
/* 154 */     List<Toggle> list = RECENT_TOGGLES.computeIfAbsent(paramLevel, paramBlockGetter -> Lists.newArrayList());
/*     */     
/* 156 */     if (paramBoolean) {
/* 157 */       list.add(new Toggle(paramBlockPos.immutable(), paramLevel.getGameTime()));
/*     */     }
/*     */     
/* 160 */     byte b = 0;
/* 161 */     for (Toggle toggle : list) {
/*     */       
/* 163 */       b++;
/* 164 */       if (toggle.pos.equals(paramBlockPos) && b >= 8) {
/* 165 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 169 */     return false;
/*     */   }
/*     */   
/*     */   protected Orientation randomOrientation(Level paramLevel, BlockState paramBlockState) {
/* 173 */     return ExperimentalRedstoneUtils.initialOrientation(paramLevel, null, Direction.UP);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\RedstoneTorchBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */