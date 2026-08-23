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
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.entity.InsideBlockEffectType;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.LiquidBlock;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ 
/*     */ public abstract class WaterFluid
/*     */   extends FlowingFluid
/*     */ {
/*     */   public Fluid getFlowing() {
/*  36 */     return Fluids.FLOWING_WATER;
/*     */   }
/*     */ 
/*     */   
/*     */   public Fluid getSource() {
/*  41 */     return Fluids.WATER;
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getBucket() {
/*  46 */     return Items.WATER_BUCKET;
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(Level paramLevel, BlockPos paramBlockPos, FluidState paramFluidState, RandomSource paramRandomSource) {
/*  51 */     if (!paramFluidState.isSource() && !((Boolean)paramFluidState.getValue((Property)FALLING)).booleanValue()) {
/*  52 */       if (paramRandomSource.nextInt(64) == 0) {
/*  53 */         paramLevel.playLocalSound(paramBlockPos.getX() + 0.5D, paramBlockPos.getY() + 0.5D, paramBlockPos.getZ() + 0.5D, SoundEvents.WATER_AMBIENT, SoundSource.AMBIENT, paramRandomSource.nextFloat() * 0.25F + 0.75F, paramRandomSource.nextFloat() + 0.5F, false);
/*     */       }
/*  55 */     } else if (paramRandomSource.nextInt(10) == 0) {
/*  56 */       paramLevel.addParticle((ParticleOptions)ParticleTypes.UNDERWATER, paramBlockPos.getX() + paramRandomSource.nextDouble(), paramBlockPos.getY() + paramRandomSource.nextDouble(), paramBlockPos.getZ() + paramRandomSource.nextDouble(), 0.0D, 0.0D, 0.0D);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ParticleOptions getDripParticle() {
/*  62 */     return (ParticleOptions)ParticleTypes.DRIPPING_WATER;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canConvertToSource(ServerLevel paramServerLevel) {
/*  67 */     return ((Boolean)paramServerLevel.getGameRules().get(GameRules.WATER_SOURCE_CONVERSION)).booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void beforeDestroyingBlock(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, BlockState paramBlockState) {
/*  72 */     BlockEntity blockEntity = paramBlockState.hasBlockEntity() ? paramLevelAccessor.getBlockEntity(paramBlockPos) : null;
/*  73 */     Block.dropResources(paramBlockState, paramLevelAccessor, paramBlockPos, blockEntity);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInside(Level paramLevel, BlockPos paramBlockPos, Entity paramEntity, InsideBlockEffectApplier paramInsideBlockEffectApplier) {
/*  78 */     paramInsideBlockEffectApplier.apply(InsideBlockEffectType.EXTINGUISH);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSlopeFindDistance(LevelReader paramLevelReader) {
/*  83 */     return 4;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState createLegacyBlock(FluidState paramFluidState) {
/*  88 */     return (BlockState)Blocks.WATER.defaultBlockState().setValue((Property)LiquidBlock.LEVEL, Integer.valueOf(getLegacyLevel(paramFluidState)));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSame(Fluid paramFluid) {
/*  93 */     return (paramFluid == Fluids.WATER || paramFluid == Fluids.FLOWING_WATER);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDropOff(LevelReader paramLevelReader) {
/*  98 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTickDelay(LevelReader paramLevelReader) {
/* 103 */     return 5;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeReplacedWith(FluidState paramFluidState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Fluid paramFluid, Direction paramDirection) {
/* 108 */     return (paramDirection == Direction.DOWN && !paramFluid.is(FluidTags.WATER));
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getExplosionResistance() {
/* 113 */     return 100.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<SoundEvent> getPickupSound() {
/* 118 */     return Optional.of(SoundEvents.BUCKET_FILL);
/*     */   }
/*     */   
/*     */   public static class Source
/*     */     extends WaterFluid {
/*     */     public int getAmount(FluidState param1FluidState) {
/* 124 */       return 8;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isSource(FluidState param1FluidState) {
/* 129 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Flowing
/*     */     extends WaterFluid {
/*     */     protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> param1Builder) {
/* 136 */       super.createFluidStateDefinition(param1Builder);
/* 137 */       param1Builder.add(new Property[] { (Property)LEVEL });
/*     */     }
/*     */ 
/*     */     
/*     */     public int getAmount(FluidState param1FluidState) {
/* 142 */       return ((Integer)param1FluidState.getValue((Property)LEVEL)).intValue();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isSource(FluidState param1FluidState) {
/* 147 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\material\WaterFluid.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */