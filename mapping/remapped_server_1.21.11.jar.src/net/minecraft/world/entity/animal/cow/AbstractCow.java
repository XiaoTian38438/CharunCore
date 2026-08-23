/*     */ package net.minecraft.world.entity.animal.cow;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FollowParentGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.TemptGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ItemUtils;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public abstract class AbstractCow extends Animal {
/*  32 */   private static final EntityDimensions BABY_DIMENSIONS = EntityType.COW.getDimensions().scale(0.5F).withEyeHeight(0.665F);
/*     */   
/*     */   public AbstractCow(EntityType<? extends AbstractCow> paramEntityType, Level paramLevel) {
/*  35 */     super(paramEntityType, paramLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  40 */     this.goalSelector.addGoal(0, (Goal)new FloatGoal((Mob)this));
/*  41 */     this.goalSelector.addGoal(1, (Goal)new PanicGoal((PathfinderMob)this, 2.0D));
/*  42 */     this.goalSelector.addGoal(2, (Goal)new BreedGoal(this, 1.0D));
/*  43 */     this.goalSelector.addGoal(3, (Goal)new TemptGoal((PathfinderMob)this, 1.25D, paramItemStack -> paramItemStack.is(ItemTags.COW_FOOD), false));
/*  44 */     this.goalSelector.addGoal(4, (Goal)new FollowParentGoal(this, 1.25D));
/*  45 */     this.goalSelector.addGoal(5, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 1.0D));
/*  46 */     this.goalSelector.addGoal(6, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 6.0F));
/*  47 */     this.goalSelector.addGoal(7, (Goal)new RandomLookAroundGoal((Mob)this));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFood(ItemStack paramItemStack) {
/*  52 */     return paramItemStack.is(ItemTags.COW_FOOD);
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  56 */     return Animal.createAnimalAttributes()
/*  57 */       .add(Attributes.MAX_HEALTH, 10.0D)
/*  58 */       .add(Attributes.MOVEMENT_SPEED, 0.20000000298023224D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/*  63 */     return SoundEvents.COW_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource paramDamageSource) {
/*  68 */     return SoundEvents.COW_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/*  73 */     return SoundEvents.COW_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos paramBlockPos, BlockState paramBlockState) {
/*  78 */     playSound(SoundEvents.COW_STEP, 0.15F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getSoundVolume() {
/*  83 */     return 0.4F;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player paramPlayer, InteractionHand paramInteractionHand) {
/*  88 */     ItemStack itemStack = paramPlayer.getItemInHand(paramInteractionHand);
/*  89 */     if (itemStack.is(Items.BUCKET) && !isBaby()) {
/*  90 */       paramPlayer.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
/*  91 */       ItemStack itemStack1 = ItemUtils.createFilledResult(itemStack, paramPlayer, Items.MILK_BUCKET.getDefaultInstance());
/*  92 */       paramPlayer.setItemInHand(paramInteractionHand, itemStack1);
/*  93 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/*  95 */     return super.mobInteract(paramPlayer, paramInteractionHand);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose paramPose) {
/* 100 */     return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(paramPose);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\cow\AbstractCow.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */