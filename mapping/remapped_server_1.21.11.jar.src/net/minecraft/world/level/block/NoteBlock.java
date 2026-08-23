/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.SkullBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ 
/*     */ public class NoteBlock extends Block {
/*  37 */   public static final MapCodec<NoteBlock> CODEC = simpleCodec(NoteBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<NoteBlock> codec() {
/*  41 */     return CODEC;
/*     */   }
/*     */   
/*  44 */   public static final EnumProperty<NoteBlockInstrument> INSTRUMENT = BlockStateProperties.NOTEBLOCK_INSTRUMENT;
/*  45 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*  46 */   public static final IntegerProperty NOTE = BlockStateProperties.NOTE;
/*     */   public static final int NOTE_VOLUME = 3;
/*     */   
/*     */   public NoteBlock(BlockBehaviour.Properties paramProperties) {
/*  50 */     super(paramProperties);
/*  51 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)INSTRUMENT, (Comparable)NoteBlockInstrument.HARP)).setValue((Property)NOTE, Integer.valueOf(0))).setValue((Property)POWERED, Boolean.valueOf(false)));
/*     */   }
/*     */   
/*     */   private BlockState setInstrument(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState) {
/*  55 */     NoteBlockInstrument noteBlockInstrument1 = paramLevelReader.getBlockState(paramBlockPos.above()).instrument();
/*  56 */     if (noteBlockInstrument1.worksAboveNoteBlock()) {
/*  57 */       return (BlockState)paramBlockState.setValue((Property)INSTRUMENT, (Comparable)noteBlockInstrument1);
/*     */     }
/*     */     
/*  60 */     NoteBlockInstrument noteBlockInstrument2 = paramLevelReader.getBlockState(paramBlockPos.below()).instrument();
/*  61 */     NoteBlockInstrument noteBlockInstrument3 = noteBlockInstrument2.worksAboveNoteBlock() ? NoteBlockInstrument.HARP : noteBlockInstrument2;
/*  62 */     return (BlockState)paramBlockState.setValue((Property)INSTRUMENT, (Comparable)noteBlockInstrument3);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/*  67 */     return setInstrument((LevelReader)paramBlockPlaceContext.getLevel(), paramBlockPlaceContext.getClickedPos(), defaultBlockState());
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/*  72 */     boolean bool = (paramDirection.getAxis() == Direction.Axis.Y) ? true : false;
/*     */     
/*  74 */     if (bool) {
/*  75 */       return setInstrument(paramLevelReader, paramBlockPos1, paramBlockState1);
/*     */     }
/*  77 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void neighborChanged(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Block paramBlock, Orientation paramOrientation, boolean paramBoolean) {
/*  82 */     boolean bool = paramLevel.hasNeighborSignal(paramBlockPos);
/*     */     
/*  84 */     if (bool != ((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue()) {
/*  85 */       if (bool) {
/*  86 */         playNote((Entity)null, paramBlockState, paramLevel, paramBlockPos);
/*     */       }
/*  88 */       paramLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)POWERED, Boolean.valueOf(bool)), 3);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void playNote(Entity paramEntity, BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos) {
/*  93 */     if (((NoteBlockInstrument)paramBlockState.getValue((Property)INSTRUMENT)).worksAboveNoteBlock() || paramLevel.getBlockState(paramBlockPos.above()).isAir()) {
/*  94 */       paramLevel.blockEvent(paramBlockPos, this, 0, 0);
/*  95 */       paramLevel.gameEvent(paramEntity, (Holder)GameEvent.NOTE_BLOCK_PLAY, paramBlockPos);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useItemOn(ItemStack paramItemStack, BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, InteractionHand paramInteractionHand, BlockHitResult paramBlockHitResult) {
/* 101 */     if (paramItemStack.is(ItemTags.NOTE_BLOCK_TOP_INSTRUMENTS) && paramBlockHitResult
/* 102 */       .getDirection() == Direction.UP)
/*     */     {
/* 104 */       return (InteractionResult)InteractionResult.PASS;
/*     */     }
/*     */     
/* 107 */     return super.useItemOn(paramItemStack, paramBlockState, paramLevel, paramBlockPos, paramPlayer, paramInteractionHand, paramBlockHitResult);
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/* 112 */     if (!paramLevel.isClientSide()) {
/* 113 */       paramBlockState = (BlockState)paramBlockState.cycle((Property)NOTE);
/* 114 */       paramLevel.setBlock(paramBlockPos, paramBlockState, 3);
/* 115 */       playNote((Entity)paramPlayer, paramBlockState, paramLevel, paramBlockPos);
/* 116 */       paramPlayer.awardStat(Stats.TUNE_NOTEBLOCK);
/*     */     } 
/* 118 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void attack(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer) {
/* 123 */     if (paramLevel.isClientSide()) {
/*     */       return;
/*     */     }
/*     */     
/* 127 */     playNote((Entity)paramPlayer, paramBlockState, paramLevel, paramBlockPos);
/* 128 */     paramPlayer.awardStat(Stats.PLAY_NOTEBLOCK);
/*     */   }
/*     */   
/*     */   public static float getPitchFromNote(int paramInt) {
/* 132 */     return (float)Math.pow(2.0D, (paramInt - 12) / 12.0D);
/*     */   }
/*     */   
/*     */   protected boolean triggerEvent(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, int paramInt1, int paramInt2) {
/*     */     float f;
/*     */     Holder holder;
/* 138 */     NoteBlockInstrument noteBlockInstrument = (NoteBlockInstrument)paramBlockState.getValue((Property)INSTRUMENT);
/* 139 */     if (noteBlockInstrument.isTunable()) {
/* 140 */       int i = ((Integer)paramBlockState.getValue((Property)NOTE)).intValue();
/* 141 */       f = getPitchFromNote(i);
/* 142 */       paramLevel.addParticle((ParticleOptions)ParticleTypes.NOTE, paramBlockPos.getX() + 0.5D, paramBlockPos.getY() + 1.2D, paramBlockPos.getZ() + 0.5D, i / 24.0D, 0.0D, 0.0D);
/*     */     } else {
/* 144 */       f = 1.0F;
/*     */     } 
/*     */ 
/*     */     
/* 148 */     if (noteBlockInstrument.hasCustomSound()) {
/* 149 */       Identifier identifier = getCustomSoundId(paramLevel, paramBlockPos);
/* 150 */       if (identifier == null) {
/* 151 */         return false;
/*     */       }
/* 153 */       holder = Holder.direct(SoundEvent.createVariableRangeEvent(identifier));
/*     */     } else {
/* 155 */       holder = noteBlockInstrument.getSoundEvent();
/*     */     } 
/* 157 */     paramLevel.playSeededSound(null, paramBlockPos.getX() + 0.5D, paramBlockPos.getY() + 0.5D, paramBlockPos.getZ() + 0.5D, holder, SoundSource.RECORDS, 3.0F, f, paramLevel.random.nextLong());
/* 158 */     return true;
/*     */   }
/*     */   
/*     */   private Identifier getCustomSoundId(Level paramLevel, BlockPos paramBlockPos) {
/* 162 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos.above()); if (blockEntity instanceof SkullBlockEntity) { SkullBlockEntity skullBlockEntity = (SkullBlockEntity)blockEntity;
/* 163 */       return skullBlockEntity.getNoteBlockSound(); }
/*     */     
/* 165 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 170 */     paramBuilder.add(new Property[] { (Property)INSTRUMENT, (Property)POWERED, (Property)NOTE });
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\NoteBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */