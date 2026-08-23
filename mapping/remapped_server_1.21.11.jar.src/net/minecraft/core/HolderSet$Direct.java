/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.tags.TagKey;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Direct<T>
/*     */   extends HolderSet.ListBacked<T>
/*     */ {
/*  78 */   static final Direct<?> EMPTY = new Direct(List.of());
/*     */   
/*     */   private final List<Holder<T>> contents;
/*     */   
/*     */   private Set<Holder<T>> contentsSet;
/*     */ 
/*     */   
/*     */   Direct(List<Holder<T>> paramList) {
/*  86 */     this.contents = paramList;
/*     */   }
/*     */ 
/*     */   
/*     */   protected List<Holder<T>> contents() {
/*  91 */     return this.contents;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBound() {
/*  96 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public Either<TagKey<T>, List<Holder<T>>> unwrap() {
/* 101 */     return Either.right(this.contents);
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<TagKey<T>> unwrapKey() {
/* 106 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Holder<T> paramHolder) {
/* 111 */     if (this.contentsSet == null) {
/* 112 */       this.contentsSet = Set.copyOf(this.contents);
/*     */     }
/* 114 */     return this.contentsSet.contains(paramHolder);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 119 */     return "DirectSet[" + String.valueOf(this.contents) + "]";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 124 */     if (this == paramObject) {
/* 125 */       return true;
/*     */     }
/* 127 */     if (paramObject instanceof Direct) { Direct direct = (Direct)paramObject; if (this.contents.equals(direct.contents)); }  return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 132 */     return this.contents.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\HolderSet$Direct.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */