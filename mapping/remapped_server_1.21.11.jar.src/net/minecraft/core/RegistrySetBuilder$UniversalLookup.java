/*     */ package net.minecraft.core;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class UniversalLookup
/*     */   extends RegistrySetBuilder.EmptyTagLookup<Object>
/*     */ {
/*  99 */   final Map<ResourceKey<Object>, Holder.Reference<Object>> holders = new HashMap<>();
/*     */   
/*     */   public UniversalLookup(HolderOwner<Object> paramHolderOwner) {
/* 102 */     super(paramHolderOwner);
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<Holder.Reference<Object>> get(ResourceKey<Object> paramResourceKey) {
/* 107 */     return Optional.of(getOrCreate(paramResourceKey));
/*     */   }
/*     */ 
/*     */   
/*     */   <T> Holder.Reference<T> getOrCreate(ResourceKey<T> paramResourceKey) {
/* 112 */     return (Holder.Reference<T>)this.holders.computeIfAbsent(paramResourceKey, paramResourceKey -> Holder.Reference.createStandAlone(this.owner, paramResourceKey));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\RegistrySetBuilder$UniversalLookup.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */