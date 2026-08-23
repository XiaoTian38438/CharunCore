/*     */ package net.minecraft.advancements.criterion;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.entity.EntityType;
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
/* 209 */   private Optional<EntityTypePredicate> entityType = Optional.empty();
/* 210 */   private Optional<DistancePredicate> distanceToPlayer = Optional.empty();
/* 211 */   private Optional<MovementPredicate> movement = Optional.empty();
/* 212 */   private Optional<LocationPredicate> located = Optional.empty();
/* 213 */   private Optional<LocationPredicate> steppingOnLocation = Optional.empty();
/* 214 */   private Optional<LocationPredicate> movementAffectedBy = Optional.empty();
/* 215 */   private Optional<MobEffectsPredicate> effects = Optional.empty();
/* 216 */   private Optional<NbtPredicate> nbt = Optional.empty();
/* 217 */   private Optional<EntityFlagsPredicate> flags = Optional.empty();
/* 218 */   private Optional<EntityEquipmentPredicate> equipment = Optional.empty();
/* 219 */   private Optional<EntitySubPredicate> subPredicate = Optional.empty();
/* 220 */   private Optional<Integer> periodicTick = Optional.empty();
/* 221 */   private Optional<EntityPredicate> vehicle = Optional.empty();
/* 222 */   private Optional<EntityPredicate> passenger = Optional.empty();
/* 223 */   private Optional<EntityPredicate> targetedEntity = Optional.empty();
/* 224 */   private Optional<String> team = Optional.empty();
/* 225 */   private Optional<SlotsPredicate> slots = Optional.empty();
/* 226 */   private DataComponentMatchers components = DataComponentMatchers.ANY;
/*     */   
/*     */   public static Builder entity() {
/* 229 */     return new Builder();
/*     */   }
/*     */   
/*     */   public Builder of(HolderGetter<EntityType<?>> paramHolderGetter, EntityType<?> paramEntityType) {
/* 233 */     this.entityType = Optional.of(EntityTypePredicate.of(paramHolderGetter, paramEntityType));
/* 234 */     return this;
/*     */   }
/*     */   
/*     */   public Builder of(HolderGetter<EntityType<?>> paramHolderGetter, TagKey<EntityType<?>> paramTagKey) {
/* 238 */     this.entityType = Optional.of(EntityTypePredicate.of(paramHolderGetter, paramTagKey));
/* 239 */     return this;
/*     */   }
/*     */   
/*     */   public Builder entityType(EntityTypePredicate paramEntityTypePredicate) {
/* 243 */     this.entityType = Optional.of(paramEntityTypePredicate);
/* 244 */     return this;
/*     */   }
/*     */   
/*     */   public Builder distance(DistancePredicate paramDistancePredicate) {
/* 248 */     this.distanceToPlayer = Optional.of(paramDistancePredicate);
/* 249 */     return this;
/*     */   }
/*     */   
/*     */   public Builder moving(MovementPredicate paramMovementPredicate) {
/* 253 */     this.movement = Optional.of(paramMovementPredicate);
/* 254 */     return this;
/*     */   }
/*     */   
/*     */   public Builder located(LocationPredicate.Builder paramBuilder) {
/* 258 */     this.located = Optional.of(paramBuilder.build());
/* 259 */     return this;
/*     */   }
/*     */   
/*     */   public Builder steppingOn(LocationPredicate.Builder paramBuilder) {
/* 263 */     this.steppingOnLocation = Optional.of(paramBuilder.build());
/* 264 */     return this;
/*     */   }
/*     */   
/*     */   public Builder movementAffectedBy(LocationPredicate.Builder paramBuilder) {
/* 268 */     this.movementAffectedBy = Optional.of(paramBuilder.build());
/* 269 */     return this;
/*     */   }
/*     */   
/*     */   public Builder effects(MobEffectsPredicate.Builder paramBuilder) {
/* 273 */     this.effects = paramBuilder.build();
/* 274 */     return this;
/*     */   }
/*     */   
/*     */   public Builder nbt(NbtPredicate paramNbtPredicate) {
/* 278 */     this.nbt = Optional.of(paramNbtPredicate);
/* 279 */     return this;
/*     */   }
/*     */   
/*     */   public Builder flags(EntityFlagsPredicate.Builder paramBuilder) {
/* 283 */     this.flags = Optional.of(paramBuilder.build());
/* 284 */     return this;
/*     */   }
/*     */   
/*     */   public Builder equipment(EntityEquipmentPredicate.Builder paramBuilder) {
/* 288 */     this.equipment = Optional.of(paramBuilder.build());
/* 289 */     return this;
/*     */   }
/*     */   
/*     */   public Builder equipment(EntityEquipmentPredicate paramEntityEquipmentPredicate) {
/* 293 */     this.equipment = Optional.of(paramEntityEquipmentPredicate);
/* 294 */     return this;
/*     */   }
/*     */   
/*     */   public Builder subPredicate(EntitySubPredicate paramEntitySubPredicate) {
/* 298 */     this.subPredicate = Optional.of(paramEntitySubPredicate);
/* 299 */     return this;
/*     */   }
/*     */   
/*     */   public Builder periodicTick(int paramInt) {
/* 303 */     this.periodicTick = Optional.of(Integer.valueOf(paramInt));
/* 304 */     return this;
/*     */   }
/*     */   
/*     */   public Builder vehicle(Builder paramBuilder) {
/* 308 */     this.vehicle = Optional.of(paramBuilder.build());
/* 309 */     return this;
/*     */   }
/*     */   
/*     */   public Builder passenger(Builder paramBuilder) {
/* 313 */     this.passenger = Optional.of(paramBuilder.build());
/* 314 */     return this;
/*     */   }
/*     */   
/*     */   public Builder targetedEntity(Builder paramBuilder) {
/* 318 */     this.targetedEntity = Optional.of(paramBuilder.build());
/* 319 */     return this;
/*     */   }
/*     */   
/*     */   public Builder team(String paramString) {
/* 323 */     this.team = Optional.of(paramString);
/* 324 */     return this;
/*     */   }
/*     */   
/*     */   public Builder slots(SlotsPredicate paramSlotsPredicate) {
/* 328 */     this.slots = Optional.of(paramSlotsPredicate);
/* 329 */     return this;
/*     */   }
/*     */   
/*     */   public Builder components(DataComponentMatchers paramDataComponentMatchers) {
/* 333 */     this.components = paramDataComponentMatchers;
/* 334 */     return this;
/*     */   }
/*     */   
/*     */   public EntityPredicate build() {
/* 338 */     return new EntityPredicate(this.entityType, this.distanceToPlayer, this.movement, new EntityPredicate.LocationWrapper(this.located, this.steppingOnLocation, this.movementAffectedBy), this.effects, this.nbt, this.flags, this.equipment, this.subPredicate, this.periodicTick, this.vehicle, this.passenger, this.targetedEntity, this.team, this.slots, this.components);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\advancements\criterion\EntityPredicate$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */