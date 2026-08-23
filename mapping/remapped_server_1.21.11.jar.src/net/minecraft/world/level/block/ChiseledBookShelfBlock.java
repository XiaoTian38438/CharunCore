/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.List;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ 
/*     */ public class ChiseledBookShelfBlock extends BaseEntityBlock implements SelectableSlotContainer {
/*  35 */   public static final MapCodec<ChiseledBookShelfBlock> CODEC = simpleCodec(ChiseledBookShelfBlock::new);
/*     */   
/*  37 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*  38 */   public static final BooleanProperty SLOT_0_OCCUPIED = BlockStateProperties.SLOT_0_OCCUPIED;
/*  39 */   public static final BooleanProperty SLOT_1_OCCUPIED = BlockStateProperties.SLOT_1_OCCUPIED;
/*  40 */   public static final BooleanProperty SLOT_2_OCCUPIED = BlockStateProperties.SLOT_2_OCCUPIED;
/*  41 */   public static final BooleanProperty SLOT_3_OCCUPIED = BlockStateProperties.SLOT_3_OCCUPIED;
/*  42 */   public static final BooleanProperty SLOT_4_OCCUPIED = BlockStateProperties.SLOT_4_OCCUPIED;
/*  43 */   public static final BooleanProperty SLOT_5_OCCUPIED = BlockStateProperties.SLOT_5_OCCUPIED; private static final int MAX_BOOKS_IN_STORAGE = 6;
/*     */   private static final int BOOKS_PER_ROW = 3;
/*     */   
/*     */   public MapCodec<ChiseledBookShelfBlock> codec() {
/*  47 */     return CODEC;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   public static final List<BooleanProperty> SLOT_OCCUPIED_PROPERTIES = List.of(SLOT_0_OCCUPIED, SLOT_1_OCCUPIED, SLOT_2_OCCUPIED, SLOT_3_OCCUPIED, SLOT_4_OCCUPIED, SLOT_5_OCCUPIED);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRows() {
/*  64 */     return 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getColumns() {
/*  69 */     return 3;
/*     */   }
/*     */   
/*     */   public ChiseledBookShelfBlock(BlockBehaviour.Properties paramProperties) {
/*  73 */     super(paramProperties);
/*     */     
/*  75 */     BlockState blockState = (BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH);
/*     */     
/*  77 */     for (BooleanProperty booleanProperty : SLOT_OCCUPIED_PROPERTIES) {
/*  78 */       blockState = (BlockState)blockState.setValue((Property)booleanProperty, Boolean.valueOf(false));
/*     */     }
/*     */     
/*  81 */     registerDefaultState(blockState);
/*     */   }
/*     */   
/*     */   protected InteractionResult useItemOn(ItemStack paramItemStack, BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, InteractionHand paramInteractionHand, BlockHitResult paramBlockHitResult) {
/*     */     ChiseledBookShelfBlockEntity chiseledBookShelfBlockEntity;
/*  86 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos); if (blockEntity instanceof ChiseledBookShelfBlockEntity) { chiseledBookShelfBlockEntity = (ChiseledBookShelfBlockEntity)blockEntity; }
/*  87 */     else { return (InteractionResult)InteractionResult.PASS; }
/*     */ 
/*     */     
/*  90 */     if (!paramItemStack.is(ItemTags.BOOKSHELF_BOOKS)) {
/*  91 */       return (InteractionResult)InteractionResult.TRY_WITH_EMPTY_HAND;
/*     */     }
/*     */     
/*  94 */     OptionalInt optionalInt = getHitSlot(paramBlockHitResult, (Direction)paramBlockState.getValue((Property)FACING));
/*  95 */     if (optionalInt.isEmpty()) {
/*  96 */       return (InteractionResult)InteractionResult.PASS;
/*     */     }
/*     */     
/*  99 */     if (((Boolean)paramBlockState.getValue((Property)SLOT_OCCUPIED_PROPERTIES.get(optionalInt.getAsInt()))).booleanValue()) {
/* 100 */       return (InteractionResult)InteractionResult.TRY_WITH_EMPTY_HAND;
/*     */     }
/*     */     
/* 103 */     addBook(paramLevel, paramBlockPos, paramPlayer, chiseledBookShelfBlockEntity, paramItemStack, optionalInt.getAsInt());
/* 104 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/*     */     ChiseledBookShelfBlockEntity chiseledBookShelfBlockEntity;
/* 109 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos); if (blockEntity instanceof ChiseledBookShelfBlockEntity) { chiseledBookShelfBlockEntity = (ChiseledBookShelfBlockEntity)blockEntity; }
/* 110 */     else { return (InteractionResult)InteractionResult.PASS; }
/*     */ 
/*     */     
/* 113 */     OptionalInt optionalInt = getHitSlot(paramBlockHitResult, (Direction)paramBlockState.getValue((Property)FACING));
/* 114 */     if (optionalInt.isEmpty()) {
/* 115 */       return (InteractionResult)InteractionResult.PASS;
/*     */     }
/*     */     
/* 118 */     if (!((Boolean)paramBlockState.getValue((Property)SLOT_OCCUPIED_PROPERTIES.get(optionalInt.getAsInt()))).booleanValue()) {
/* 119 */       return (InteractionResult)InteractionResult.CONSUME;
/*     */     }
/*     */     
/* 122 */     removeBook(paramLevel, paramBlockPos, paramPlayer, chiseledBookShelfBlockEntity, optionalInt.getAsInt());
/* 123 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   private static void addBook(Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, ChiseledBookShelfBlockEntity paramChiseledBookShelfBlockEntity, ItemStack paramItemStack, int paramInt) {
/* 127 */     if (paramLevel.isClientSide()) {
/*     */       return;
/*     */     }
/*     */     
/* 131 */     paramPlayer.awardStat(Stats.ITEM_USED.get(paramItemStack.getItem()));
/*     */ 
/*     */ 
/*     */     
/* 135 */     SoundEvent soundEvent = paramItemStack.is(Items.ENCHANTED_BOOK) ? SoundEvents.CHISELED_BOOKSHELF_INSERT_ENCHANTED : SoundEvents.CHISELED_BOOKSHELF_INSERT;
/*     */     
/* 137 */     paramChiseledBookShelfBlockEntity.setItem(paramInt, paramItemStack.consumeAndReturn(1, (LivingEntity)paramPlayer));
/* 138 */     paramLevel.playSound(null, paramBlockPos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */   private static void removeBook(Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, ChiseledBookShelfBlockEntity paramChiseledBookShelfBlockEntity, int paramInt) {
/* 142 */     if (paramLevel.isClientSide()) {
/*     */       return;
/*     */     }
/*     */     
/* 146 */     ItemStack itemStack = paramChiseledBookShelfBlockEntity.removeItem(paramInt, 1);
/*     */ 
/*     */ 
/*     */     
/* 150 */     SoundEvent soundEvent = itemStack.is(Items.ENCHANTED_BOOK) ? SoundEvents.CHISELED_BOOKSHELF_PICKUP_ENCHANTED : SoundEvents.CHISELED_BOOKSHELF_PICKUP;
/* 151 */     paramLevel.playSound(null, paramBlockPos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */     
/* 153 */     if (!paramPlayer.getInventory().add(itemStack)) {
/* 154 */       paramPlayer.drop(itemStack, false);
/*     */     }
/*     */     
/* 157 */     paramLevel.gameEvent((Entity)paramPlayer, (Holder)GameEvent.BLOCK_CHANGE, paramBlockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 162 */     return (BlockEntity)new ChiseledBookShelfBlockEntity(paramBlockPos, paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 167 */     paramBuilder.add(new Property[] { (Property)FACING });
/* 168 */     Objects.requireNonNull(paramBuilder); SLOT_OCCUPIED_PROPERTIES.forEach(paramProperty -> paramBuilder.add(new Property[] { paramProperty }));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, boolean paramBoolean) {
/* 173 */     Containers.updateNeighboursAfterDestroy(paramBlockState, (Level)paramServerLevel, paramBlockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 178 */     return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)paramBlockPlaceContext.getHorizontalDirection().getOpposite());
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 183 */     return (BlockState)paramBlockState.setValue((Property)FACING, (Comparable)paramRotation.rotate((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 188 */     return paramBlockState.rotate(paramMirror.getRotation((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean hasAnalogOutputSignal(BlockState paramBlockState) {
/* 193 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Direction paramDirection) {
/* 198 */     if (paramLevel.isClientSide())
/*     */     {
/* 200 */       return 0;
/*     */     }
/*     */     
/* 203 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos); if (blockEntity instanceof ChiseledBookShelfBlockEntity) { ChiseledBookShelfBlockEntity chiseledBookShelfBlockEntity = (ChiseledBookShelfBlockEntity)blockEntity;
/* 204 */       return chiseledBookShelfBlockEntity.getLastInteractedSlot() + 1; }
/*     */     
/* 206 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\ChiseledBookShelfBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */