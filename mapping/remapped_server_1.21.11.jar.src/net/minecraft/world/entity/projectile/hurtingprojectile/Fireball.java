/*    */ package net.minecraft.world.entity.projectile.hurtingprojectile;
/*    */ 
/*    */ import net.minecraft.network.syncher.EntityDataAccessor;
/*    */ import net.minecraft.network.syncher.EntityDataSerializers;
/*    */ import net.minecraft.network.syncher.SynchedEntityData;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.SlotAccess;
/*    */ import net.minecraft.world.entity.projectile.ItemSupplier;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.storage.ValueInput;
/*    */ import net.minecraft.world.level.storage.ValueOutput;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public abstract class Fireball extends AbstractHurtingProjectile implements ItemSupplier {
/*    */   private static final float MIN_CAMERA_DISTANCE_SQUARED = 12.25F;
/* 20 */   private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(Fireball.class, EntityDataSerializers.ITEM_STACK);
/*    */   
/*    */   public Fireball(EntityType<? extends Fireball> paramEntityType, Level paramLevel) {
/* 23 */     super((EntityType)paramEntityType, paramLevel);
/*    */   }
/*    */   
/*    */   public Fireball(EntityType<? extends Fireball> paramEntityType, double paramDouble1, double paramDouble2, double paramDouble3, Vec3 paramVec3, Level paramLevel) {
/* 27 */     super((EntityType)paramEntityType, paramDouble1, paramDouble2, paramDouble3, paramVec3, paramLevel);
/*    */   }
/*    */   
/*    */   public Fireball(EntityType<? extends Fireball> paramEntityType, LivingEntity paramLivingEntity, Vec3 paramVec3, Level paramLevel) {
/* 31 */     super((EntityType)paramEntityType, paramLivingEntity, paramVec3, paramLevel);
/*    */   }
/*    */   
/*    */   public void setItem(ItemStack paramItemStack) {
/* 35 */     if (paramItemStack.isEmpty()) {
/* 36 */       getEntityData().set(DATA_ITEM_STACK, getDefaultItem());
/*    */     } else {
/* 38 */       getEntityData().set(DATA_ITEM_STACK, paramItemStack.copyWithCount(1));
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void playEntityOnFireExtinguishedSound() {}
/*    */ 
/*    */   
/*    */   public ItemStack getItem() {
/* 48 */     return (ItemStack)getEntityData().get(DATA_ITEM_STACK);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/* 53 */     paramBuilder.define(DATA_ITEM_STACK, getDefaultItem());
/*    */   }
/*    */ 
/*    */   
/*    */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/* 58 */     super.addAdditionalSaveData(paramValueOutput);
/* 59 */     paramValueOutput.store("Item", ItemStack.CODEC, getItem());
/*    */   }
/*    */ 
/*    */   
/*    */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/* 64 */     super.readAdditionalSaveData(paramValueInput);
/* 65 */     setItem(paramValueInput.read("Item", ItemStack.CODEC).orElse(getDefaultItem()));
/*    */   }
/*    */   
/*    */   private ItemStack getDefaultItem() {
/* 69 */     return new ItemStack((ItemLike)Items.FIRE_CHARGE);
/*    */   }
/*    */ 
/*    */   
/*    */   public SlotAccess getSlot(int paramInt) {
/* 74 */     if (paramInt == 0) {
/* 75 */       return SlotAccess.of(this::getItem, this::setItem);
/*    */     }
/* 77 */     return super.getSlot(paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldRenderAtSqrDistance(double paramDouble) {
/* 82 */     if (this.tickCount < 2 && paramDouble < 12.25D) {
/* 83 */       return false;
/*    */     }
/* 85 */     return super.shouldRenderAtSqrDistance(paramDouble);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\projectile\hurtingprojectile\Fireball.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */