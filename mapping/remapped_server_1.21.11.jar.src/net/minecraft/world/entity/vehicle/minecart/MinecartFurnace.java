/*     */ package net.minecraft.world.entity.vehicle.minecart;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.FurnaceBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class MinecartFurnace extends AbstractMinecart {
/*  28 */   private static final EntityDataAccessor<Boolean> DATA_ID_FUEL = SynchedEntityData.defineId(MinecartFurnace.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private static final int FUEL_TICKS_PER_ITEM = 3600;
/*     */   private static final int MAX_FUEL_TICKS = 32000;
/*     */   private static final short DEFAULT_FUEL = 0;
/*  33 */   private static final Vec3 DEFAULT_PUSH = Vec3.ZERO;
/*     */   
/*  35 */   private int fuel = 0;
/*  36 */   public Vec3 push = DEFAULT_PUSH;
/*     */   
/*     */   public MinecartFurnace(EntityType<? extends MinecartFurnace> paramEntityType, Level paramLevel) {
/*  39 */     super(paramEntityType, paramLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFurnace() {
/*  44 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/*  49 */     super.defineSynchedData(paramBuilder);
/*  50 */     paramBuilder.define(DATA_ID_FUEL, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  55 */     super.tick();
/*     */     
/*  57 */     if (!level().isClientSide()) {
/*  58 */       if (this.fuel > 0) {
/*  59 */         this.fuel--;
/*     */       }
/*  61 */       if (this.fuel <= 0) {
/*  62 */         this.push = Vec3.ZERO;
/*     */       }
/*  64 */       setHasFuel((this.fuel > 0));
/*     */     } 
/*     */     
/*  67 */     if (hasFuel() && this.random.nextInt(4) == 0) {
/*  68 */       level().addParticle((ParticleOptions)ParticleTypes.LARGE_SMOKE, getX(), getY() + 0.8D, getZ(), 0.0D, 0.0D, 0.0D);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected double getMaxSpeed(ServerLevel paramServerLevel) {
/*  74 */     return isInWater() ? (super.getMaxSpeed(paramServerLevel) * 0.75D) : (super.getMaxSpeed(paramServerLevel) * 0.5D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Item getDropItem() {
/*  79 */     return Items.FURNACE_MINECART;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getPickResult() {
/*  84 */     return new ItemStack((ItemLike)Items.FURNACE_MINECART);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Vec3 applyNaturalSlowdown(Vec3 paramVec3) {
/*     */     Vec3 vec3;
/*  90 */     if (this.push.lengthSqr() > 1.0E-7D) {
/*  91 */       this.push = calculateNewPushAlong(paramVec3);
/*     */ 
/*     */ 
/*     */       
/*  95 */       vec3 = paramVec3.multiply(0.8D, 0.0D, 0.8D).add(this.push);
/*     */       
/*  97 */       if (isInWater()) {
/*  98 */         vec3 = vec3.scale(0.1D);
/*     */       }
/*     */     } else {
/* 101 */       vec3 = paramVec3.multiply(0.98D, 0.0D, 0.98D);
/*     */     } 
/*     */     
/* 104 */     return super.applyNaturalSlowdown(vec3);
/*     */   }
/*     */ 
/*     */   
/*     */   private Vec3 calculateNewPushAlong(Vec3 paramVec3) {
/* 109 */     double d1 = 1.0E-4D;
/* 110 */     double d2 = 0.001D;
/* 111 */     if (this.push.horizontalDistanceSqr() > 1.0E-4D && paramVec3.horizontalDistanceSqr() > 0.001D)
/*     */     {
/* 113 */       return this.push.projectedOn(paramVec3).normalize().scale(this.push.length());
/*     */     }
/* 115 */     return this.push;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult interact(Player paramPlayer, InteractionHand paramInteractionHand) {
/* 120 */     ItemStack itemStack = paramPlayer.getItemInHand(paramInteractionHand);
/* 121 */     if (addFuel(paramPlayer.position(), itemStack)) {
/* 122 */       itemStack.consume(1, (LivingEntity)paramPlayer);
/*     */     }
/*     */     
/* 125 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   public boolean addFuel(Vec3 paramVec3, ItemStack paramItemStack) {
/* 129 */     if (paramItemStack.is(ItemTags.FURNACE_MINECART_FUEL) && this.fuel + 3600 <= 32000) {
/* 130 */       this.fuel += 3600;
/*     */     } else {
/* 132 */       return false;
/*     */     } 
/*     */     
/* 135 */     if (this.fuel > 0) {
/* 136 */       this.push = position().subtract(paramVec3).horizontal();
/*     */     }
/*     */     
/* 139 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/* 144 */     super.addAdditionalSaveData(paramValueOutput);
/* 145 */     paramValueOutput.putDouble("PushX", this.push.x);
/* 146 */     paramValueOutput.putDouble("PushZ", this.push.z);
/* 147 */     paramValueOutput.putShort("Fuel", (short)this.fuel);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/* 152 */     super.readAdditionalSaveData(paramValueInput);
/* 153 */     double d1 = paramValueInput.getDoubleOr("PushX", DEFAULT_PUSH.x);
/* 154 */     double d2 = paramValueInput.getDoubleOr("PushZ", DEFAULT_PUSH.z);
/* 155 */     this.push = new Vec3(d1, 0.0D, d2);
/* 156 */     this.fuel = paramValueInput.getShortOr("Fuel", (short)0);
/*     */   }
/*     */   
/*     */   protected boolean hasFuel() {
/* 160 */     return ((Boolean)this.entityData.get(DATA_ID_FUEL)).booleanValue();
/*     */   }
/*     */   
/*     */   protected void setHasFuel(boolean paramBoolean) {
/* 164 */     this.entityData.set(DATA_ID_FUEL, Boolean.valueOf(paramBoolean));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getDefaultDisplayBlockState() {
/* 169 */     return (BlockState)((BlockState)Blocks.FURNACE.defaultBlockState().setValue((Property)FurnaceBlock.FACING, (Comparable)Direction.NORTH)).setValue((Property)FurnaceBlock.LIT, Boolean.valueOf(hasFuel()));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\vehicle\minecart\MinecartFurnace.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */