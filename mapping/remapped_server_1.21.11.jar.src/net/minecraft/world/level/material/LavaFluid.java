/*     */ package net.minecraft.world.level.material;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.entity.InsideBlockEffectType;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.BaseFireBlock;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.LiquidBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class LavaFluid
/*     */   extends FlowingFluid
/*     */ {
/*     */   public static final float MIN_LEVEL_CUTOFF = 0.44444445F;
/*     */   
/*     */   public Fluid getFlowing() {
/*  41 */     return Fluids.FLOWING_LAVA;
/*     */   }
/*     */ 
/*     */   
/*     */   public Fluid getSource() {
/*  46 */     return Fluids.LAVA;
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getBucket() {
/*  51 */     return Items.LAVA_BUCKET;
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(Level paramLevel, BlockPos paramBlockPos, FluidState paramFluidState, RandomSource paramRandomSource) {
/*  56 */     BlockPos blockPos = paramBlockPos.above();
/*  57 */     if (paramLevel.getBlockState(blockPos).isAir() && !paramLevel.getBlockState(blockPos).isSolidRender()) {
/*  58 */       if (paramRandomSource.nextInt(100) == 0) {
/*  59 */         double d1 = paramBlockPos.getX() + paramRandomSource.nextDouble();
/*     */         
/*  61 */         double d2 = paramBlockPos.getY() + 1.0D;
/*  62 */         double d3 = paramBlockPos.getZ() + paramRandomSource.nextDouble();
/*  63 */         paramLevel.addParticle((ParticleOptions)ParticleTypes.LAVA, d1, d2, d3, 0.0D, 0.0D, 0.0D);
/*  64 */         paramLevel.playLocalSound(d1, d2, d3, SoundEvents.LAVA_POP, SoundSource.AMBIENT, 0.2F + paramRandomSource.nextFloat() * 0.2F, 0.9F + paramRandomSource.nextFloat() * 0.15F, false);
/*     */       } 
/*  66 */       if (paramRandomSource.nextInt(200) == 0) {
/*  67 */         paramLevel.playLocalSound(paramBlockPos.getX(), paramBlockPos.getY(), paramBlockPos.getZ(), SoundEvents.LAVA_AMBIENT, SoundSource.AMBIENT, 0.2F + paramRandomSource.nextFloat() * 0.2F, 0.9F + paramRandomSource.nextFloat() * 0.15F, false);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void randomTick(ServerLevel paramServerLevel, BlockPos paramBlockPos, FluidState paramFluidState, RandomSource paramRandomSource) {
/*  74 */     if (!paramServerLevel.canSpreadFireAround(paramBlockPos)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  79 */     int i = paramRandomSource.nextInt(3);
/*  80 */     if (i > 0) {
/*  81 */       BlockPos blockPos = paramBlockPos;
/*     */       
/*  83 */       for (byte b = 0; b < i; b++) {
/*  84 */         blockPos = blockPos.offset(paramRandomSource.nextInt(3) - 1, 1, paramRandomSource.nextInt(3) - 1);
/*  85 */         if (!paramServerLevel.isLoaded(blockPos)) {
/*     */           return;
/*     */         }
/*  88 */         BlockState blockState = paramServerLevel.getBlockState(blockPos);
/*  89 */         if (blockState.isAir()) {
/*  90 */           if (hasFlammableNeighbours((LevelReader)paramServerLevel, blockPos)) {
/*  91 */             paramServerLevel.setBlockAndUpdate(blockPos, BaseFireBlock.getState((BlockGetter)paramServerLevel, blockPos));
/*     */             return;
/*     */           } 
/*  94 */         } else if (blockState.blocksMotion()) {
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } else {
/*  99 */       for (byte b = 0; b < 3; b++) {
/* 100 */         BlockPos blockPos = paramBlockPos.offset(paramRandomSource.nextInt(3) - 1, 0, paramRandomSource.nextInt(3) - 1);
/* 101 */         if (!paramServerLevel.isLoaded(blockPos)) {
/*     */           return;
/*     */         }
/* 104 */         if (paramServerLevel.isEmptyBlock(blockPos.above()) && isFlammable((LevelReader)paramServerLevel, blockPos)) {
/* 105 */           paramServerLevel.setBlockAndUpdate(blockPos.above(), BaseFireBlock.getState((BlockGetter)paramServerLevel, blockPos));
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInside(Level paramLevel, BlockPos paramBlockPos, Entity paramEntity, InsideBlockEffectApplier paramInsideBlockEffectApplier) {
/* 113 */     paramInsideBlockEffectApplier.apply(InsideBlockEffectType.CLEAR_FREEZE);
/* 114 */     paramInsideBlockEffectApplier.apply(InsideBlockEffectType.LAVA_IGNITE);
/* 115 */     paramInsideBlockEffectApplier.runAfter(InsideBlockEffectType.LAVA_IGNITE, Entity::lavaHurt);
/*     */   }
/*     */   
/*     */   private boolean hasFlammableNeighbours(LevelReader paramLevelReader, BlockPos paramBlockPos) {
/* 119 */     for (Direction direction : Direction.values()) {
/* 120 */       if (isFlammable(paramLevelReader, paramBlockPos.relative(direction))) {
/* 121 */         return true;
/*     */       }
/*     */     } 
/* 124 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isFlammable(LevelReader paramLevelReader, BlockPos paramBlockPos) {
/* 128 */     if (paramLevelReader.isInsideBuildHeight(paramBlockPos.getY()) && !paramLevelReader.hasChunkAt(paramBlockPos)) {
/* 129 */       return false;
/*     */     }
/* 131 */     return paramLevelReader.getBlockState(paramBlockPos).ignitedByLava();
/*     */   }
/*     */ 
/*     */   
/*     */   public ParticleOptions getDripParticle() {
/* 136 */     return (ParticleOptions)ParticleTypes.DRIPPING_LAVA;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void beforeDestroyingBlock(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 141 */     fizz(paramLevelAccessor, paramBlockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSlopeFindDistance(LevelReader paramLevelReader) {
/* 146 */     return isFastLava(paramLevelReader) ? 4 : 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState createLegacyBlock(FluidState paramFluidState) {
/* 151 */     return (BlockState)Blocks.LAVA.defaultBlockState().setValue((Property)LiquidBlock.LEVEL, Integer.valueOf(getLegacyLevel(paramFluidState)));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSame(Fluid paramFluid) {
/* 156 */     return (paramFluid == Fluids.LAVA || paramFluid == Fluids.FLOWING_LAVA);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDropOff(LevelReader paramLevelReader) {
/* 161 */     return isFastLava(paramLevelReader) ? 1 : 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeReplacedWith(FluidState paramFluidState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Fluid paramFluid, Direction paramDirection) {
/* 166 */     return (paramFluidState.getHeight(paramBlockGetter, paramBlockPos) >= 0.44444445F && paramFluid.is(FluidTags.WATER));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTickDelay(LevelReader paramLevelReader) {
/* 171 */     return isFastLava(paramLevelReader) ? 10 : 30;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSpreadDelay(Level paramLevel, BlockPos paramBlockPos, FluidState paramFluidState1, FluidState paramFluidState2) {
/* 176 */     int i = getTickDelay((LevelReader)paramLevel);
/*     */     
/* 178 */     if (!paramFluidState1.isEmpty() && !paramFluidState2.isEmpty() && !((Boolean)paramFluidState1.getValue((Property)FALLING)).booleanValue() && !((Boolean)paramFluidState2.getValue((Property)FALLING)).booleanValue() && paramFluidState2.getHeight((BlockGetter)paramLevel, paramBlockPos) > paramFluidState1.getHeight((BlockGetter)paramLevel, paramBlockPos) && paramLevel.getRandom().nextInt(4) != 0) {
/* 179 */       i *= 4;
/*     */     }
/* 181 */     return i;
/*     */   }
/*     */   
/*     */   private void fizz(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos) {
/* 185 */     paramLevelAccessor.levelEvent(1501, paramBlockPos, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canConvertToSource(ServerLevel paramServerLevel) {
/* 190 */     return ((Boolean)paramServerLevel.getGameRules().get(GameRules.LAVA_SOURCE_CONVERSION)).booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void spreadTo(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, BlockState paramBlockState, Direction paramDirection, FluidState paramFluidState) {
/* 195 */     if (paramDirection == Direction.DOWN) {
/* 196 */       FluidState fluidState = paramLevelAccessor.getFluidState(paramBlockPos);
/* 197 */       if (is(FluidTags.LAVA) && fluidState.is(FluidTags.WATER)) {
/* 198 */         if (paramBlockState.getBlock() instanceof LiquidBlock) {
/* 199 */           paramLevelAccessor.setBlock(paramBlockPos, Blocks.STONE.defaultBlockState(), 3);
/*     */         }
/* 201 */         fizz(paramLevelAccessor, paramBlockPos);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 206 */     super.spreadTo(paramLevelAccessor, paramBlockPos, paramBlockState, paramDirection, paramFluidState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isRandomlyTicking() {
/* 211 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getExplosionResistance() {
/* 216 */     return 100.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<SoundEvent> getPickupSound() {
/* 221 */     return Optional.of(SoundEvents.BUCKET_FILL_LAVA);
/*     */   }
/*     */   
/*     */   private static boolean isFastLava(LevelReader paramLevelReader) {
/* 225 */     return ((Boolean)paramLevelReader.environmentAttributes().getDimensionValue(EnvironmentAttributes.FAST_LAVA)).booleanValue();
/*     */   }
/*     */   
/*     */   public static class Source
/*     */     extends LavaFluid {
/*     */     public int getAmount(FluidState param1FluidState) {
/* 231 */       return 8;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isSource(FluidState param1FluidState) {
/* 236 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Flowing
/*     */     extends LavaFluid {
/*     */     protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> param1Builder) {
/* 243 */       super.createFluidStateDefinition(param1Builder);
/* 244 */       param1Builder.add(new Property[] { (Property)LEVEL });
/*     */     }
/*     */ 
/*     */     
/*     */     public int getAmount(FluidState param1FluidState) {
/* 249 */       return ((Integer)param1FluidState.getValue((Property)LEVEL)).intValue();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isSource(FluidState param1FluidState) {
/* 254 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\material\LavaFluid.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */