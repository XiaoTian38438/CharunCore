/*     */ package net.minecraft.network.syncher;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   private final SyncedDataHolder entity;
/*     */   private final SynchedEntityData.DataItem<?>[] itemsById;
/*     */   
/*     */   public Builder(SyncedDataHolder paramSyncedDataHolder) {
/* 206 */     this.entity = paramSyncedDataHolder;
/* 207 */     this.itemsById = (SynchedEntityData.DataItem<?>[])new SynchedEntityData.DataItem[SynchedEntityData.ID_REGISTRY.getCount(paramSyncedDataHolder.getClass())];
/*     */   }
/*     */   
/*     */   public <T> Builder define(EntityDataAccessor<T> paramEntityDataAccessor, T paramT) {
/* 211 */     int i = paramEntityDataAccessor.id();
/* 212 */     if (i > this.itemsById.length) {
/* 213 */       throw new IllegalArgumentException("Data value id is too big with " + i + "! (Max is " + this.itemsById.length + ")");
/*     */     }
/* 215 */     if (this.itemsById[i] != null) {
/* 216 */       throw new IllegalArgumentException("Duplicate id value for " + i + "!");
/*     */     }
/* 218 */     if (EntityDataSerializers.getSerializedId(paramEntityDataAccessor.serializer()) < 0) {
/* 219 */       throw new IllegalArgumentException("Unregistered serializer " + String.valueOf(paramEntityDataAccessor.serializer()) + " for " + i + "!");
/*     */     }
/* 221 */     this.itemsById[paramEntityDataAccessor.id()] = new SynchedEntityData.DataItem(paramEntityDataAccessor, paramT);
/* 222 */     return this;
/*     */   }
/*     */   
/*     */   public SynchedEntityData build() {
/* 226 */     for (byte b = 0; b < this.itemsById.length; b++) {
/* 227 */       if (this.itemsById[b] == null)
/*     */       {
/* 229 */         throw new IllegalStateException("Entity " + String.valueOf(this.entity.getClass()) + " has not defined synched data value " + b);
/*     */       }
/*     */     } 
/* 232 */     return new SynchedEntityData(this.entity, this.itemsById);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\syncher\SynchedEntityData$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */