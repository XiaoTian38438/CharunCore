/*     */ package net.minecraft.world.item.equipment;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Builder
/*     */ {
/*     */   private final EquipmentSlot slot;
/* 165 */   private Holder<SoundEvent> equipSound = SoundEvents.ARMOR_EQUIP_GENERIC;
/* 166 */   private Optional<ResourceKey<EquipmentAsset>> assetId = Optional.empty();
/* 167 */   private Optional<Identifier> cameraOverlay = Optional.empty();
/* 168 */   private Optional<HolderSet<EntityType<?>>> allowedEntities = Optional.empty();
/*     */   private boolean dispensable = true;
/*     */   private boolean swappable = true;
/*     */   private boolean damageOnHurt = true;
/*     */   private boolean equipOnInteract;
/*     */   private boolean canBeSheared;
/* 174 */   private Holder<SoundEvent> shearingSound = BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.SHEARS_SNIP);
/*     */   
/*     */   Builder(EquipmentSlot paramEquipmentSlot) {
/* 177 */     this.slot = paramEquipmentSlot;
/*     */   }
/*     */   
/*     */   public Builder setEquipSound(Holder<SoundEvent> paramHolder) {
/* 181 */     this.equipSound = paramHolder;
/* 182 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setAsset(ResourceKey<EquipmentAsset> paramResourceKey) {
/* 186 */     this.assetId = Optional.of(paramResourceKey);
/* 187 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setCameraOverlay(Identifier paramIdentifier) {
/* 191 */     this.cameraOverlay = Optional.of(paramIdentifier);
/* 192 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setAllowedEntities(EntityType<?>... paramVarArgs) {
/* 196 */     return setAllowedEntities((HolderSet<EntityType<?>>)HolderSet.direct(EntityType::builtInRegistryHolder, (Object[])paramVarArgs));
/*     */   }
/*     */   
/*     */   public Builder setAllowedEntities(HolderSet<EntityType<?>> paramHolderSet) {
/* 200 */     this.allowedEntities = Optional.of(paramHolderSet);
/* 201 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setDispensable(boolean paramBoolean) {
/* 205 */     this.dispensable = paramBoolean;
/* 206 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setSwappable(boolean paramBoolean) {
/* 210 */     this.swappable = paramBoolean;
/* 211 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setDamageOnHurt(boolean paramBoolean) {
/* 215 */     this.damageOnHurt = paramBoolean;
/* 216 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setEquipOnInteract(boolean paramBoolean) {
/* 220 */     this.equipOnInteract = paramBoolean;
/* 221 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setCanBeSheared(boolean paramBoolean) {
/* 225 */     this.canBeSheared = paramBoolean;
/* 226 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setShearingSound(Holder<SoundEvent> paramHolder) {
/* 230 */     this.shearingSound = paramHolder;
/* 231 */     return this;
/*     */   }
/*     */   
/*     */   public Equippable build() {
/* 235 */     return new Equippable(this.slot, this.equipSound, this.assetId, this.cameraOverlay, this.allowedEntities, this.dispensable, this.swappable, this.damageOnHurt, this.equipOnInteract, this.canBeSheared, this.shearingSound);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\equipment\Equippable$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */