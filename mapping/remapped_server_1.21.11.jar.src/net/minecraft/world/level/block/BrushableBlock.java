/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.item.FallingBlockEntity;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BrushableBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class BrushableBlock extends BaseEntityBlock implements Fallable {
/*     */   static {
/*  28 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)BuiltInRegistries.BLOCK.byNameCodec().fieldOf("turns_into").forGetter(BrushableBlock::getTurnsInto), (App)BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("brush_sound").forGetter(BrushableBlock::getBrushSound), (App)BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("brush_completed_sound").forGetter(BrushableBlock::getBrushCompletedSound), (App)propertiesCodec()).apply((Applicative)paramInstance, BrushableBlock::new));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final MapCodec<BrushableBlock> CODEC;
/*     */ 
/*     */   
/*     */   public MapCodec<BrushableBlock> codec() {
/*  37 */     return CODEC;
/*     */   }
/*     */   
/*  40 */   private static final IntegerProperty DUSTED = BlockStateProperties.DUSTED;
/*     */   
/*     */   public static final int TICK_DELAY = 2;
/*     */   private final Block turnsInto;
/*     */   private final SoundEvent brushSound;
/*     */   private final SoundEvent brushCompletedSound;
/*     */   
/*     */   public BrushableBlock(Block paramBlock, SoundEvent paramSoundEvent1, SoundEvent paramSoundEvent2, BlockBehaviour.Properties paramProperties) {
/*  48 */     super(paramProperties);
/*  49 */     this.turnsInto = paramBlock;
/*  50 */     this.brushSound = paramSoundEvent1;
/*  51 */     this.brushCompletedSound = paramSoundEvent2;
/*  52 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)DUSTED, Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/*  57 */     paramBuilder.add(new Property[] { (Property)DUSTED });
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPlace(BlockState paramBlockState1, Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState2, boolean paramBoolean) {
/*  62 */     paramLevel.scheduleTick(paramBlockPos, this, 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/*  67 */     paramScheduledTickAccess.scheduleTick(paramBlockPos1, this, 2);
/*     */     
/*  69 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  74 */     BlockEntity blockEntity = paramServerLevel.getBlockEntity(paramBlockPos); if (blockEntity instanceof BrushableBlockEntity) { BrushableBlockEntity brushableBlockEntity = (BrushableBlockEntity)blockEntity;
/*  75 */       brushableBlockEntity.checkReset(paramServerLevel); }
/*     */ 
/*     */     
/*  78 */     if (!FallingBlock.isFree(paramServerLevel.getBlockState(paramBlockPos.below())) || paramBlockPos.getY() < paramServerLevel.getMinY()) {
/*     */       return;
/*     */     }
/*     */     
/*  82 */     FallingBlockEntity fallingBlockEntity = FallingBlockEntity.fall((Level)paramServerLevel, paramBlockPos, paramBlockState);
/*  83 */     fallingBlockEntity.disableDrop();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onBrokenAfterFall(Level paramLevel, BlockPos paramBlockPos, FallingBlockEntity paramFallingBlockEntity) {
/*  92 */     Vec3 vec3 = paramFallingBlockEntity.getBoundingBox().getCenter();
/*  93 */     paramLevel.levelEvent(2001, BlockPos.containing((Position)vec3), Block.getId(paramFallingBlockEntity.getBlockState()));
/*  94 */     paramLevel.gameEvent((Entity)paramFallingBlockEntity, (Holder)GameEvent.BLOCK_DESTROY, vec3);
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  99 */     if (paramRandomSource.nextInt(16) == 0) {
/* 100 */       BlockPos blockPos = paramBlockPos.below();
/*     */       
/* 102 */       if (FallingBlock.isFree(paramLevel.getBlockState(blockPos))) {
/* 103 */         double d1 = paramBlockPos.getX() + paramRandomSource.nextDouble();
/* 104 */         double d2 = paramBlockPos.getY() - 0.05D;
/* 105 */         double d3 = paramBlockPos.getZ() + paramRandomSource.nextDouble();
/*     */         
/* 107 */         paramLevel.addParticle((ParticleOptions)new BlockParticleOption(ParticleTypes.FALLING_DUST, paramBlockState), d1, d2, d3, 0.0D, 0.0D, 0.0D);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 114 */     return (BlockEntity)new BrushableBlockEntity(paramBlockPos, paramBlockState);
/*     */   }
/*     */   
/*     */   public Block getTurnsInto() {
/* 118 */     return this.turnsInto;
/*     */   }
/*     */   
/*     */   public SoundEvent getBrushSound() {
/* 122 */     return this.brushSound;
/*     */   }
/*     */   
/*     */   public SoundEvent getBrushCompletedSound() {
/* 126 */     return this.brushCompletedSound;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\BrushableBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */