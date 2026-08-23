/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.damagesource.DamageType;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ public class PanicGoal
/*     */   extends Goal
/*     */ {
/*     */   public static final int WATER_CHECK_DISTANCE_VERTICAL = 1;
/*     */   protected final PathfinderMob mob;
/*     */   protected final double speedModifier;
/*     */   protected double posX;
/*     */   protected double posY;
/*     */   protected double posZ;
/*     */   protected boolean isRunning;
/*     */   private final Function<PathfinderMob, TagKey<DamageType>> panicCausingDamageTypes;
/*     */   
/*     */   public PanicGoal(PathfinderMob paramPathfinderMob, double paramDouble) {
/*  30 */     this(paramPathfinderMob, paramDouble, DamageTypeTags.PANIC_CAUSES);
/*     */   }
/*     */   
/*     */   public PanicGoal(PathfinderMob paramPathfinderMob, double paramDouble, TagKey<DamageType> paramTagKey) {
/*  34 */     this(paramPathfinderMob, paramDouble, paramPathfinderMob -> paramTagKey);
/*     */   }
/*     */   
/*     */   public PanicGoal(PathfinderMob paramPathfinderMob, double paramDouble, Function<PathfinderMob, TagKey<DamageType>> paramFunction) {
/*  38 */     this.mob = paramPathfinderMob;
/*  39 */     this.speedModifier = paramDouble;
/*  40 */     this.panicCausingDamageTypes = paramFunction;
/*  41 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/*  46 */     if (!shouldPanic()) {
/*  47 */       return false;
/*     */     }
/*     */     
/*  50 */     if (this.mob.isOnFire()) {
/*  51 */       BlockPos blockPos = lookForWater((BlockGetter)this.mob.level(), (Entity)this.mob, 5);
/*  52 */       if (blockPos != null) {
/*  53 */         this.posX = blockPos.getX();
/*  54 */         this.posY = blockPos.getY();
/*  55 */         this.posZ = blockPos.getZ();
/*     */         
/*  57 */         return true;
/*     */       } 
/*     */     } 
/*  60 */     return findRandomPosition();
/*     */   }
/*     */   
/*     */   protected boolean shouldPanic() {
/*  64 */     return (this.mob.getLastDamageSource() != null && this.mob.getLastDamageSource().is(this.panicCausingDamageTypes.apply(this.mob)));
/*     */   }
/*     */   
/*     */   protected boolean findRandomPosition() {
/*  68 */     Vec3 vec3 = DefaultRandomPos.getPos(this.mob, 5, 4);
/*  69 */     if (vec3 == null) {
/*  70 */       return false;
/*     */     }
/*  72 */     this.posX = vec3.x;
/*  73 */     this.posY = vec3.y;
/*  74 */     this.posZ = vec3.z;
/*     */     
/*  76 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isRunning() {
/*  80 */     return this.isRunning;
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  85 */     this.mob.getNavigation().moveTo(this.posX, this.posY, this.posZ, this.speedModifier);
/*  86 */     this.isRunning = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/*  91 */     this.isRunning = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/*  96 */     return !this.mob.getNavigation().isDone();
/*     */   }
/*     */   
/*     */   protected BlockPos lookForWater(BlockGetter paramBlockGetter, Entity paramEntity, int paramInt) {
/* 100 */     BlockPos blockPos = paramEntity.blockPosition();
/* 101 */     if (!paramBlockGetter.getBlockState(blockPos).getCollisionShape(paramBlockGetter, blockPos).isEmpty()) {
/* 102 */       return null;
/*     */     }
/* 104 */     return BlockPos.findClosestMatch(paramEntity.blockPosition(), paramInt, 1, paramBlockPos -> paramBlockGetter.getFluidState(paramBlockPos).is(FluidTags.WATER)).orElse(null);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\PanicGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */