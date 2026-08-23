/*     */ package net.minecraft.world.level.material;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateHolder;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public final class FluidState
/*     */   extends StateHolder<Fluid, FluidState>
/*     */ {
/*  30 */   public static final Codec<FluidState> CODEC = codec(BuiltInRegistries.FLUID.byNameCodec(), Fluid::defaultFluidState).stable();
/*     */   public static final int AMOUNT_MAX = 9;
/*     */   public static final int AMOUNT_FULL = 8;
/*     */   
/*     */   public FluidState(Fluid paramFluid, Reference2ObjectArrayMap<Property<?>, Comparable<?>> paramReference2ObjectArrayMap, MapCodec<FluidState> paramMapCodec) {
/*  35 */     super(paramFluid, paramReference2ObjectArrayMap, paramMapCodec);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Fluid getType() {
/*  42 */     return (Fluid)this.owner;
/*     */   }
/*     */   
/*     */   public boolean isSource() {
/*  46 */     return getType().isSource(this);
/*     */   }
/*     */   
/*     */   public boolean isSourceOfType(Fluid paramFluid) {
/*  50 */     return (this.owner == paramFluid && ((Fluid)this.owner).isSource(this));
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  54 */     return getType().isEmpty();
/*     */   }
/*     */   
/*     */   public float getHeight(BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/*  58 */     return getType().getHeight(this, paramBlockGetter, paramBlockPos);
/*     */   }
/*     */   
/*     */   public float getOwnHeight() {
/*  62 */     return getType().getOwnHeight(this);
/*     */   }
/*     */   
/*     */   public int getAmount() {
/*  66 */     return getType().getAmount(this);
/*     */   }
/*     */   
/*     */   public boolean shouldRenderBackwardUpFace(BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/*  70 */     for (byte b = -1; b <= 1; b++) {
/*  71 */       for (byte b1 = -1; b1 <= 1; b1++) {
/*  72 */         BlockPos blockPos = paramBlockPos.offset(b, 0, b1);
/*  73 */         FluidState fluidState = paramBlockGetter.getFluidState(blockPos);
/*  74 */         if (!fluidState.getType().isSame(getType()) && !paramBlockGetter.getBlockState(blockPos).isSolidRender()) {
/*  75 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/*  79 */     return false;
/*     */   }
/*     */   
/*     */   public void tick(ServerLevel paramServerLevel, BlockPos paramBlockPos, BlockState paramBlockState) {
/*  83 */     getType().tick(paramServerLevel, paramBlockPos, paramBlockState, this);
/*     */   }
/*     */   
/*     */   public void animateTick(Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  87 */     getType().animateTick(paramLevel, paramBlockPos, this, paramRandomSource);
/*     */   }
/*     */   
/*     */   public boolean isRandomlyTicking() {
/*  91 */     return getType().isRandomlyTicking();
/*     */   }
/*     */   
/*     */   public void randomTick(ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  95 */     getType().randomTick(paramServerLevel, paramBlockPos, this, paramRandomSource);
/*     */   }
/*     */   
/*     */   public Vec3 getFlow(BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/*  99 */     return getType().getFlow(paramBlockGetter, paramBlockPos, this);
/*     */   }
/*     */   
/*     */   public BlockState createLegacyBlock() {
/* 103 */     return getType().createLegacyBlock(this);
/*     */   }
/*     */   
/*     */   public ParticleOptions getDripParticle() {
/* 107 */     return getType().getDripParticle();
/*     */   }
/*     */   
/*     */   public boolean is(TagKey<Fluid> paramTagKey) {
/* 111 */     return getType().builtInRegistryHolder().is(paramTagKey);
/*     */   }
/*     */   
/*     */   public boolean is(HolderSet<Fluid> paramHolderSet) {
/* 115 */     return paramHolderSet.contains((Holder)getType().builtInRegistryHolder());
/*     */   }
/*     */   
/*     */   public boolean is(Fluid paramFluid) {
/* 119 */     return (getType() == paramFluid);
/*     */   }
/*     */   
/*     */   public float getExplosionResistance() {
/* 123 */     return getType().getExplosionResistance();
/*     */   }
/*     */   
/*     */   public boolean canBeReplacedWith(BlockGetter paramBlockGetter, BlockPos paramBlockPos, Fluid paramFluid, Direction paramDirection) {
/* 127 */     return getType().canBeReplacedWith(this, paramBlockGetter, paramBlockPos, paramFluid, paramDirection);
/*     */   }
/*     */   
/*     */   public VoxelShape getShape(BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 131 */     return getType().getShape(this, paramBlockGetter, paramBlockPos);
/*     */   }
/*     */   
/*     */   public AABB getAABB(BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 135 */     return getType().getAABB(this, paramBlockGetter, paramBlockPos);
/*     */   }
/*     */   
/*     */   public Holder<Fluid> holder() {
/* 139 */     return (Holder<Fluid>)((Fluid)this.owner).builtInRegistryHolder();
/*     */   }
/*     */   
/*     */   public Stream<TagKey<Fluid>> getTags() {
/* 143 */     return ((Fluid)this.owner).builtInRegistryHolder().tags();
/*     */   }
/*     */   
/*     */   public void entityInside(Level paramLevel, BlockPos paramBlockPos, Entity paramEntity, InsideBlockEffectApplier paramInsideBlockEffectApplier) {
/* 147 */     getType().entityInside(paramLevel, paramBlockPos, paramEntity, paramInsideBlockEffectApplier);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\material\FluidState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */