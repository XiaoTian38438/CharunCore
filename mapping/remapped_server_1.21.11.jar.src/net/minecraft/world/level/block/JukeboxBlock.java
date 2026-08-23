/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.JukeboxPlayable;
/*     */ import net.minecraft.world.item.component.TypedEntityData;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ 
/*     */ public class JukeboxBlock extends BaseEntityBlock {
/*  31 */   public static final MapCodec<JukeboxBlock> CODEC = simpleCodec(JukeboxBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<JukeboxBlock> codec() {
/*  35 */     return CODEC;
/*     */   }
/*     */   
/*  38 */   public static final BooleanProperty HAS_RECORD = BlockStateProperties.HAS_RECORD;
/*     */   
/*     */   protected JukeboxBlock(BlockBehaviour.Properties paramProperties) {
/*  41 */     super(paramProperties);
/*  42 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)HAS_RECORD, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlacedBy(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, LivingEntity paramLivingEntity, ItemStack paramItemStack) {
/*  47 */     super.setPlacedBy(paramLevel, paramBlockPos, paramBlockState, paramLivingEntity, paramItemStack);
/*  48 */     TypedEntityData typedEntityData = (TypedEntityData)paramItemStack.get(DataComponents.BLOCK_ENTITY_DATA);
/*  49 */     if (typedEntityData != null && typedEntityData.contains("RecordItem")) {
/*  50 */       paramLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)HAS_RECORD, Boolean.valueOf(true)), 2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/*  56 */     if (((Boolean)paramBlockState.getValue((Property)HAS_RECORD)).booleanValue()) { BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos); if (blockEntity instanceof JukeboxBlockEntity) { JukeboxBlockEntity jukeboxBlockEntity = (JukeboxBlockEntity)blockEntity;
/*  57 */         jukeboxBlockEntity.popOutTheItem();
/*  58 */         return (InteractionResult)InteractionResult.SUCCESS; }
/*     */        }
/*     */     
/*  61 */     return (InteractionResult)InteractionResult.PASS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useItemOn(ItemStack paramItemStack, BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, InteractionHand paramInteractionHand, BlockHitResult paramBlockHitResult) {
/*  66 */     if (((Boolean)paramBlockState.getValue((Property)HAS_RECORD)).booleanValue()) {
/*  67 */       return (InteractionResult)InteractionResult.TRY_WITH_EMPTY_HAND;
/*     */     }
/*     */     
/*  70 */     ItemStack itemStack = paramPlayer.getItemInHand(paramInteractionHand);
/*  71 */     InteractionResult interactionResult = JukeboxPlayable.tryInsertIntoJukebox(paramLevel, paramBlockPos, itemStack, paramPlayer);
/*     */     
/*  73 */     if (!interactionResult.consumesAction()) {
/*  74 */       return (InteractionResult)InteractionResult.TRY_WITH_EMPTY_HAND;
/*     */     }
/*     */     
/*  77 */     return interactionResult;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, boolean paramBoolean) {
/*  82 */     Containers.updateNeighboursAfterDestroy(paramBlockState, (Level)paramServerLevel, paramBlockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/*  87 */     return (BlockEntity)new JukeboxBlockEntity(paramBlockPos, paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSignalSource(BlockState paramBlockState) {
/*  92 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSignal(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection) {
/*  97 */     BlockEntity blockEntity = paramBlockGetter.getBlockEntity(paramBlockPos); if (blockEntity instanceof JukeboxBlockEntity) { JukeboxBlockEntity jukeboxBlockEntity = (JukeboxBlockEntity)blockEntity; if (jukeboxBlockEntity.getSongPlayer().isPlaying())
/*  98 */         return 15;  }
/*     */     
/* 100 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean hasAnalogOutputSignal(BlockState paramBlockState) {
/* 105 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Direction paramDirection) {
/* 110 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos); if (blockEntity instanceof JukeboxBlockEntity) { JukeboxBlockEntity jukeboxBlockEntity = (JukeboxBlockEntity)blockEntity;
/* 111 */       return jukeboxBlockEntity.getComparatorOutput(); }
/*     */ 
/*     */     
/* 114 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 119 */     paramBuilder.add(new Property[] { (Property)HAS_RECORD });
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level paramLevel, BlockState paramBlockState, BlockEntityType<T> paramBlockEntityType) {
/* 124 */     if (((Boolean)paramBlockState.getValue((Property)HAS_RECORD)).booleanValue()) {
/* 125 */       return createTickerHelper(paramBlockEntityType, BlockEntityType.JUKEBOX, JukeboxBlockEntity::tick);
/*     */     }
/* 127 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\JukeboxBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */