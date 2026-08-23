/*    */ package net.minecraft.world.entity.projectile.throwableitemprojectile;
/*    */ import net.minecraft.network.syncher.EntityDataAccessor;
/*    */ import net.minecraft.network.syncher.SynchedEntityData;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.projectile.ItemSupplier;
/*    */ import net.minecraft.world.entity.projectile.ThrowableProjectile;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.storage.ValueInput;
/*    */ import net.minecraft.world.level.storage.ValueOutput;
/*    */ 
/*    */ public abstract class ThrowableItemProjectile extends ThrowableProjectile implements ItemSupplier {
/* 17 */   private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(ThrowableItemProjectile.class, EntityDataSerializers.ITEM_STACK);
/*    */   
/*    */   public ThrowableItemProjectile(EntityType<? extends ThrowableItemProjectile> paramEntityType, Level paramLevel) {
/* 20 */     super(paramEntityType, paramLevel);
/*    */   }
/*    */   
/*    */   public ThrowableItemProjectile(EntityType<? extends ThrowableItemProjectile> paramEntityType, double paramDouble1, double paramDouble2, double paramDouble3, Level paramLevel, ItemStack paramItemStack) {
/* 24 */     super(paramEntityType, paramDouble1, paramDouble2, paramDouble3, paramLevel);
/* 25 */     setItem(paramItemStack);
/*    */   }
/*    */   
/*    */   public ThrowableItemProjectile(EntityType<? extends ThrowableItemProjectile> paramEntityType, LivingEntity paramLivingEntity, Level paramLevel, ItemStack paramItemStack) {
/* 29 */     this(paramEntityType, paramLivingEntity.getX(), paramLivingEntity.getEyeY() - 0.10000000149011612D, paramLivingEntity.getZ(), paramLevel, paramItemStack);
/* 30 */     setOwner((Entity)paramLivingEntity);
/*    */   }
/*    */   
/*    */   public void setItem(ItemStack paramItemStack) {
/* 34 */     getEntityData().set(DATA_ITEM_STACK, paramItemStack.copyWithCount(1));
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract Item getDefaultItem();
/*    */   
/*    */   public ItemStack getItem() {
/* 41 */     return (ItemStack)getEntityData().get(DATA_ITEM_STACK);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/* 46 */     paramBuilder.define(DATA_ITEM_STACK, new ItemStack((ItemLike)getDefaultItem()));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/* 51 */     super.addAdditionalSaveData(paramValueOutput);
/* 52 */     paramValueOutput.store("Item", ItemStack.CODEC, getItem());
/*    */   }
/*    */ 
/*    */   
/*    */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/* 57 */     super.readAdditionalSaveData(paramValueInput);
/* 58 */     setItem(paramValueInput.read("Item", ItemStack.CODEC).orElseGet(() -> new ItemStack((ItemLike)getDefaultItem())));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\projectile\throwableitemprojectile\ThrowableItemProjectile.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */