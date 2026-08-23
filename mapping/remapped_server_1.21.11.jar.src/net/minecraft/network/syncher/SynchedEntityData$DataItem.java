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
/*     */ public class DataItem<T>
/*     */ {
/*     */   final EntityDataAccessor<T> accessor;
/*     */   T value;
/*     */   private final T initialValue;
/*     */   private boolean dirty;
/*     */   
/*     */   public DataItem(EntityDataAccessor<T> paramEntityDataAccessor, T paramT) {
/* 167 */     this.accessor = paramEntityDataAccessor;
/* 168 */     this.initialValue = paramT;
/* 169 */     this.value = paramT;
/*     */   }
/*     */   
/*     */   public EntityDataAccessor<T> getAccessor() {
/* 173 */     return this.accessor;
/*     */   }
/*     */   
/*     */   public void setValue(T paramT) {
/* 177 */     this.value = paramT;
/*     */   }
/*     */   
/*     */   public T getValue() {
/* 181 */     return this.value;
/*     */   }
/*     */   
/*     */   public boolean isDirty() {
/* 185 */     return this.dirty;
/*     */   }
/*     */   
/*     */   public void setDirty(boolean paramBoolean) {
/* 189 */     this.dirty = paramBoolean;
/*     */   }
/*     */   
/*     */   public boolean isSetToDefault() {
/* 193 */     return this.initialValue.equals(this.value);
/*     */   }
/*     */   
/*     */   public SynchedEntityData.DataValue<T> value() {
/* 197 */     return SynchedEntityData.DataValue.create(this.accessor, this.value);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\syncher\SynchedEntityData$DataItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */