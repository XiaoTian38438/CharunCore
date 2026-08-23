/*     */ package net.minecraft.world.entity.animal.fish;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
/*     */ import net.minecraft.world.entity.animal.Bucketable;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public abstract class AbstractFish extends WaterAnimal implements Bucketable {
/*  36 */   private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(AbstractFish.class, EntityDataSerializers.BOOLEAN);
/*     */   private static final boolean DEFAULT_FROM_BUCKET = false;
/*     */   
/*     */   public AbstractFish(EntityType<? extends AbstractFish> paramEntityType, Level paramLevel) {
/*  40 */     super((EntityType)paramEntityType, paramLevel);
/*     */     
/*  42 */     this.moveControl = new FishMoveControl(this);
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  46 */     return Mob.createMobAttributes()
/*  47 */       .add(Attributes.MAX_HEALTH, 3.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean requiresCustomPersistence() {
/*  52 */     return (super.requiresCustomPersistence() || fromBucket());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeWhenFarAway(double paramDouble) {
/*  57 */     return (!fromBucket() && !hasCustomName());
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxSpawnClusterSize() {
/*  62 */     return 8;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/*  67 */     super.defineSynchedData(paramBuilder);
/*     */     
/*  69 */     paramBuilder.define(FROM_BUCKET, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean fromBucket() {
/*  74 */     return ((Boolean)this.entityData.get(FROM_BUCKET)).booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFromBucket(boolean paramBoolean) {
/*  79 */     this.entityData.set(FROM_BUCKET, Boolean.valueOf(paramBoolean));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/*  84 */     super.addAdditionalSaveData(paramValueOutput);
/*     */     
/*  86 */     paramValueOutput.putBoolean("FromBucket", fromBucket());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/*  91 */     super.readAdditionalSaveData(paramValueInput);
/*     */     
/*  93 */     setFromBucket(paramValueInput.getBooleanOr("FromBucket", false));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  98 */     super.registerGoals();
/*     */     
/* 100 */     this.goalSelector.addGoal(0, (Goal)new PanicGoal(this, 1.25D));
/* 101 */     this.goalSelector.addGoal(2, (Goal)new AvoidEntityGoal(this, Player.class, 8.0F, 1.6D, 1.4D, EntitySelector.NO_SPECTATORS));
/* 102 */     this.goalSelector.addGoal(4, (Goal)new FishSwimGoal(this));
/*     */   }
/*     */ 
/*     */   
/*     */   protected PathNavigation createNavigation(Level paramLevel) {
/* 107 */     return (PathNavigation)new WaterBoundPathNavigation((Mob)this, paramLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void travelInWater(Vec3 paramVec3, double paramDouble1, boolean paramBoolean, double paramDouble2) {
/* 112 */     moveRelative(0.01F, paramVec3);
/* 113 */     move(MoverType.SELF, getDeltaMovement());
/*     */     
/* 115 */     setDeltaMovement(getDeltaMovement().scale(0.9D));
/* 116 */     if (getTarget() == null) {
/* 117 */       setDeltaMovement(getDeltaMovement().add(0.0D, -0.005D, 0.0D));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 123 */     if (!isInWater() && onGround() && this.verticalCollision) {
/* 124 */       setDeltaMovement(getDeltaMovement().add(((this.random
/* 125 */             .nextFloat() * 2.0F - 1.0F) * 0.05F), 0.4000000059604645D, ((this.random
/*     */             
/* 127 */             .nextFloat() * 2.0F - 1.0F) * 0.05F)));
/*     */       
/* 129 */       setOnGround(false);
/* 130 */       this.needsSync = true;
/* 131 */       makeSound(getFlopSound());
/*     */     } 
/*     */     
/* 134 */     super.aiStep();
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult mobInteract(Player paramPlayer, InteractionHand paramInteractionHand) {
/* 139 */     return Bucketable.bucketMobPickup(paramPlayer, paramInteractionHand, (LivingEntity)this).orElse(super.mobInteract(paramPlayer, paramInteractionHand));
/*     */   }
/*     */ 
/*     */   
/*     */   public void saveToBucketTag(ItemStack paramItemStack) {
/* 144 */     Bucketable.saveDefaultDataToBucketTag((Mob)this, paramItemStack);
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadFromBucketTag(CompoundTag paramCompoundTag) {
/* 149 */     Bucketable.loadDefaultDataFromBucketTag((Mob)this, paramCompoundTag);
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundEvent getPickupSound() {
/* 154 */     return SoundEvents.BUCKET_FILL_FISH;
/*     */   }
/*     */   
/*     */   private static class FishSwimGoal extends RandomSwimmingGoal {
/*     */     private final AbstractFish fish;
/*     */     
/*     */     public FishSwimGoal(AbstractFish param1AbstractFish) {
/* 161 */       super(param1AbstractFish, 1.0D, 40);
/* 162 */       this.fish = param1AbstractFish;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 167 */       return (this.fish.canRandomSwim() && super.canUse());
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean canRandomSwim() {
/* 172 */     return true;
/*     */   }
/*     */   protected abstract SoundEvent getFlopSound();
/*     */   
/*     */   private static class FishMoveControl extends MoveControl { private final AbstractFish fish;
/*     */     
/*     */     FishMoveControl(AbstractFish param1AbstractFish) {
/* 179 */       super((Mob)param1AbstractFish);
/* 180 */       this.fish = param1AbstractFish;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 185 */       if (this.fish.isEyeInFluid(FluidTags.WATER))
/*     */       {
/* 187 */         this.fish.setDeltaMovement(this.fish.getDeltaMovement().add(0.0D, 0.005D, 0.0D));
/*     */       }
/*     */       
/* 190 */       if (this.operation != MoveControl.Operation.MOVE_TO || this.fish.getNavigation().isDone()) {
/* 191 */         this.fish.setSpeed(0.0F);
/*     */         
/*     */         return;
/*     */       } 
/* 195 */       float f = (float)(this.speedModifier * this.fish.getAttributeValue(Attributes.MOVEMENT_SPEED));
/* 196 */       this.fish.setSpeed(Mth.lerp(0.125F, this.fish.getSpeed(), f));
/*     */       
/* 198 */       double d1 = this.wantedX - this.fish.getX();
/* 199 */       double d2 = this.wantedY - this.fish.getY();
/* 200 */       double d3 = this.wantedZ - this.fish.getZ();
/*     */       
/* 202 */       if (d2 != 0.0D) {
/* 203 */         double d = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
/*     */         
/* 205 */         this.fish.setDeltaMovement(this.fish.getDeltaMovement().add(0.0D, this.fish.getSpeed() * d2 / d * 0.1D, 0.0D));
/*     */       } 
/*     */       
/* 208 */       if (d1 != 0.0D || d3 != 0.0D) {
/* 209 */         float f1 = (float)(Mth.atan2(d3, d1) * 57.2957763671875D) - 90.0F;
/*     */         
/* 211 */         this.fish.setYRot(rotlerp(this.fish.getYRot(), f1, 90.0F));
/* 212 */         this.fish.yBodyRot = this.fish.getYRot();
/*     */       } 
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SoundEvent getSwimSound() {
/* 221 */     return SoundEvents.FISH_SWIM;
/*     */   }
/*     */   
/*     */   protected void playStepSound(BlockPos paramBlockPos, BlockState paramBlockState) {}
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\fish\AbstractFish.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */