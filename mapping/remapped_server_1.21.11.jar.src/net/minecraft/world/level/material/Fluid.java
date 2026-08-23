/*     */ package net.minecraft.world.level.material;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.IdMapper;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ 
/*     */ public abstract class Fluid
/*     */ {
/*  29 */   public static final IdMapper<FluidState> FLUID_STATE_REGISTRY = new IdMapper();
/*     */   
/*     */   protected final StateDefinition<Fluid, FluidState> stateDefinition;
/*     */   private FluidState defaultFluidState;
/*  33 */   private final Holder.Reference<Fluid> builtInRegistryHolder = BuiltInRegistries.FLUID.createIntrusiveHolder(this);
/*     */   
/*     */   protected Fluid() {
/*  36 */     StateDefinition.Builder<Fluid, FluidState> builder = new StateDefinition.Builder(this);
/*  37 */     createFluidStateDefinition(builder);
/*  38 */     this.stateDefinition = builder.create(Fluid::defaultFluidState, FluidState::new);
/*  39 */     registerDefaultState((FluidState)this.stateDefinition.any());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> paramBuilder) {}
/*     */   
/*     */   public StateDefinition<Fluid, FluidState> getStateDefinition() {
/*  46 */     return this.stateDefinition;
/*     */   }
/*     */   
/*     */   protected final void registerDefaultState(FluidState paramFluidState) {
/*  50 */     this.defaultFluidState = paramFluidState;
/*     */   }
/*     */   
/*     */   public final FluidState defaultFluidState() {
/*  54 */     return this.defaultFluidState;
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract Item getBucket();
/*     */ 
/*     */   
/*     */   protected void animateTick(Level paramLevel, BlockPos paramBlockPos, FluidState paramFluidState, RandomSource paramRandomSource) {}
/*     */ 
/*     */   
/*     */   protected void tick(ServerLevel paramServerLevel, BlockPos paramBlockPos, BlockState paramBlockState, FluidState paramFluidState) {}
/*     */ 
/*     */   
/*     */   protected void randomTick(ServerLevel paramServerLevel, BlockPos paramBlockPos, FluidState paramFluidState, RandomSource paramRandomSource) {}
/*     */   
/*     */   protected void entityInside(Level paramLevel, BlockPos paramBlockPos, Entity paramEntity, InsideBlockEffectApplier paramInsideBlockEffectApplier) {}
/*     */   
/*     */   protected ParticleOptions getDripParticle() {
/*  72 */     return null;
/*     */   }
/*     */   
/*     */   protected abstract boolean canBeReplacedWith(FluidState paramFluidState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Fluid paramFluid, Direction paramDirection);
/*     */   
/*     */   protected abstract Vec3 getFlow(BlockGetter paramBlockGetter, BlockPos paramBlockPos, FluidState paramFluidState);
/*     */   
/*     */   public abstract int getTickDelay(LevelReader paramLevelReader);
/*     */   
/*     */   protected boolean isRandomlyTicking() {
/*  82 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean isEmpty() {
/*  86 */     return false;
/*     */   }
/*     */   
/*     */   protected abstract float getExplosionResistance();
/*     */   
/*     */   public abstract float getHeight(FluidState paramFluidState, BlockGetter paramBlockGetter, BlockPos paramBlockPos);
/*     */   
/*     */   public abstract float getOwnHeight(FluidState paramFluidState);
/*     */   
/*     */   protected abstract BlockState createLegacyBlock(FluidState paramFluidState);
/*     */   
/*     */   public abstract boolean isSource(FluidState paramFluidState);
/*     */   
/*     */   public abstract int getAmount(FluidState paramFluidState);
/*     */   
/*     */   public boolean isSame(Fluid paramFluid) {
/* 102 */     return (paramFluid == this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public boolean is(TagKey<Fluid> paramTagKey) {
/* 110 */     return this.builtInRegistryHolder.is(paramTagKey);
/*     */   }
/*     */   
/*     */   public abstract VoxelShape getShape(FluidState paramFluidState, BlockGetter paramBlockGetter, BlockPos paramBlockPos);
/*     */   
/*     */   public AABB getAABB(FluidState paramFluidState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 116 */     if (isEmpty()) {
/* 117 */       return null;
/*     */     }
/* 119 */     float f = paramFluidState.getHeight(paramBlockGetter, paramBlockPos);
/* 120 */     return new AABB(paramBlockPos.getX(), paramBlockPos.getY(), paramBlockPos.getZ(), paramBlockPos.getX() + 1.0D, (paramBlockPos.getY() + f), paramBlockPos.getZ() + 1.0D);
/*     */   }
/*     */   
/*     */   public Optional<SoundEvent> getPickupSound() {
/* 124 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Holder.Reference<Fluid> builtInRegistryHolder() {
/* 132 */     return this.builtInRegistryHolder;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\material\Fluid.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */