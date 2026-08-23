/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.decoration.ItemFrame;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.SignalGetter;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.ComparatorBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.ComparatorMode;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.ticks.TickPriority;
/*     */ 
/*     */ public class ComparatorBlock extends DiodeBlock implements EntityBlock {
/*  33 */   public static final MapCodec<ComparatorBlock> CODEC = simpleCodec(ComparatorBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<ComparatorBlock> codec() {
/*  37 */     return CODEC;
/*     */   }
/*     */   
/*  40 */   public static final EnumProperty<ComparatorMode> MODE = BlockStateProperties.MODE_COMPARATOR;
/*     */   
/*     */   public ComparatorBlock(BlockBehaviour.Properties paramProperties) {
/*  43 */     super(paramProperties);
/*  44 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)POWERED, Boolean.valueOf(false))).setValue((Property)MODE, (Comparable)ComparatorMode.COMPARE));
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getDelay(BlockState paramBlockState) {
/*  49 */     return 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/*  54 */     if (paramDirection == Direction.DOWN && !canSurviveOn(paramLevelReader, paramBlockPos2, paramBlockState2)) {
/*  55 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*  57 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getOutputSignal(BlockGetter paramBlockGetter, BlockPos paramBlockPos, BlockState paramBlockState) {
/*  62 */     BlockEntity blockEntity = paramBlockGetter.getBlockEntity(paramBlockPos);
/*  63 */     if (blockEntity instanceof ComparatorBlockEntity) {
/*  64 */       return ((ComparatorBlockEntity)blockEntity).getOutputSignal();
/*     */     }
/*     */     
/*  67 */     return 0;
/*     */   }
/*     */   
/*     */   private int calculateOutputSignal(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState) {
/*  71 */     int i = getInputSignal(paramLevel, paramBlockPos, paramBlockState);
/*  72 */     if (i == 0) {
/*  73 */       return 0;
/*     */     }
/*     */     
/*  76 */     int j = getAlternateSignal((SignalGetter)paramLevel, paramBlockPos, paramBlockState);
/*  77 */     if (j > i) {
/*  78 */       return 0;
/*     */     }
/*     */     
/*  81 */     if (paramBlockState.getValue((Property)MODE) == ComparatorMode.SUBTRACT) {
/*  82 */       return i - j;
/*     */     }
/*     */     
/*  85 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean shouldTurnOn(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState) {
/*  90 */     int i = getInputSignal(paramLevel, paramBlockPos, paramBlockState);
/*  91 */     if (i == 0) {
/*  92 */       return false;
/*     */     }
/*     */     
/*  95 */     int j = getAlternateSignal((SignalGetter)paramLevel, paramBlockPos, paramBlockState);
/*  96 */     if (i > j) {
/*  97 */       return true;
/*     */     }
/*     */     
/* 100 */     return (i == j && paramBlockState.getValue((Property)MODE) == ComparatorMode.COMPARE);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getInputSignal(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 105 */     int i = super.getInputSignal(paramLevel, paramBlockPos, paramBlockState);
/*     */     
/* 107 */     Direction direction = (Direction)paramBlockState.getValue((Property)FACING);
/* 108 */     BlockPos blockPos = paramBlockPos.relative(direction);
/* 109 */     BlockState blockState = paramLevel.getBlockState(blockPos);
/*     */     
/* 111 */     if (blockState.hasAnalogOutputSignal()) {
/* 112 */       i = blockState.getAnalogOutputSignal(paramLevel, blockPos, direction.getOpposite());
/* 113 */     } else if (i < 15 && blockState.isRedstoneConductor((BlockGetter)paramLevel, blockPos)) {
/* 114 */       blockPos = blockPos.relative(direction);
/* 115 */       blockState = paramLevel.getBlockState(blockPos);
/* 116 */       ItemFrame itemFrame = getItemFrame(paramLevel, direction, blockPos);
/*     */       
/* 118 */       int j = Math.max(
/* 119 */           (itemFrame == null) ? Integer.MIN_VALUE : itemFrame.getAnalogOutput(), 
/* 120 */           blockState.hasAnalogOutputSignal() ? blockState.getAnalogOutputSignal(paramLevel, blockPos, direction.getOpposite()) : Integer.MIN_VALUE);
/*     */ 
/*     */       
/* 123 */       if (j != Integer.MIN_VALUE) {
/* 124 */         i = j;
/*     */       }
/*     */     } 
/*     */     
/* 128 */     return i;
/*     */   }
/*     */   
/*     */   private ItemFrame getItemFrame(Level paramLevel, Direction paramDirection, BlockPos paramBlockPos) {
/* 132 */     List<ItemFrame> list = paramLevel.getEntitiesOfClass(ItemFrame.class, new AABB(paramBlockPos.getX(), paramBlockPos.getY(), paramBlockPos.getZ(), (paramBlockPos.getX() + 1), (paramBlockPos.getY() + 1), (paramBlockPos.getZ() + 1)), paramItemFrame -> (paramItemFrame.getDirection() == paramDirection));
/*     */     
/* 134 */     if (list.size() == 1) {
/* 135 */       return list.get(0);
/*     */     }
/*     */     
/* 138 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/* 143 */     if (!(paramPlayer.getAbilities()).mayBuild) {
/* 144 */       return (InteractionResult)InteractionResult.PASS;
/*     */     }
/*     */     
/* 147 */     paramBlockState = (BlockState)paramBlockState.cycle((Property)MODE);
/* 148 */     float f = (paramBlockState.getValue((Property)MODE) == ComparatorMode.SUBTRACT) ? 0.55F : 0.5F;
/* 149 */     paramLevel.playSound((Entity)paramPlayer, paramBlockPos, SoundEvents.COMPARATOR_CLICK, SoundSource.BLOCKS, 0.3F, f);
/*     */     
/* 151 */     paramLevel.setBlock(paramBlockPos, paramBlockState, 2);
/* 152 */     refreshOutputState(paramLevel, paramBlockPos, paramBlockState);
/* 153 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void checkTickOnNeighbor(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 158 */     if (paramLevel.getBlockTicks().willTickThisTick(paramBlockPos, this)) {
/*     */       return;
/*     */     }
/*     */     
/* 162 */     int i = calculateOutputSignal(paramLevel, paramBlockPos, paramBlockState);
/* 163 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos);
/* 164 */     byte b = (blockEntity instanceof ComparatorBlockEntity) ? ((ComparatorBlockEntity)blockEntity).getOutputSignal() : 0;
/*     */     
/* 166 */     if (i != b || ((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue() != shouldTurnOn(paramLevel, paramBlockPos, paramBlockState)) {
/*     */       
/* 168 */       TickPriority tickPriority = shouldPrioritize((BlockGetter)paramLevel, paramBlockPos, paramBlockState) ? TickPriority.HIGH : TickPriority.NORMAL;
/* 169 */       paramLevel.scheduleTick(paramBlockPos, this, 2, tickPriority);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void refreshOutputState(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 174 */     int i = calculateOutputSignal(paramLevel, paramBlockPos, paramBlockState);
/*     */     
/* 176 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos);
/* 177 */     int j = 0;
/* 178 */     if (blockEntity instanceof ComparatorBlockEntity) { ComparatorBlockEntity comparatorBlockEntity = (ComparatorBlockEntity)blockEntity;
/* 179 */       j = comparatorBlockEntity.getOutputSignal();
/* 180 */       comparatorBlockEntity.setOutputSignal(i); }
/*     */ 
/*     */     
/* 183 */     if (j != i || paramBlockState.getValue((Property)MODE) == ComparatorMode.COMPARE) {
/* 184 */       boolean bool1 = shouldTurnOn(paramLevel, paramBlockPos, paramBlockState);
/* 185 */       boolean bool2 = ((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue();
/*     */       
/* 187 */       if (bool2 && !bool1) {
/* 188 */         paramLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)POWERED, Boolean.valueOf(false)), 2);
/* 189 */       } else if (!bool2 && bool1) {
/* 190 */         paramLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)POWERED, Boolean.valueOf(true)), 2);
/*     */       } 
/*     */       
/* 193 */       updateNeighborsInFront(paramLevel, paramBlockPos, paramBlockState);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 199 */     refreshOutputState((Level)paramServerLevel, paramBlockPos, paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean triggerEvent(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, int paramInt1, int paramInt2) {
/* 204 */     super.triggerEvent(paramBlockState, paramLevel, paramBlockPos, paramInt1, paramInt2);
/*     */     
/* 206 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos);
/* 207 */     return (blockEntity != null && blockEntity.triggerEvent(paramInt1, paramInt2));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 212 */     return (BlockEntity)new ComparatorBlockEntity(paramBlockPos, paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 217 */     paramBuilder.add(new Property[] { (Property)FACING, (Property)MODE, (Property)POWERED });
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\ComparatorBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */