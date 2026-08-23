/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Named<T>
/*     */   extends HolderSet.ListBacked<T>
/*     */ {
/*     */   private final HolderOwner<T> owner;
/*     */   private final TagKey<T> key;
/*     */   private List<Holder<T>> contents;
/*     */   
/*     */   Named(HolderOwner<T> paramHolderOwner, TagKey<T> paramTagKey) {
/* 143 */     this.owner = paramHolderOwner;
/* 144 */     this.key = paramTagKey;
/*     */   }
/*     */   
/*     */   void bind(List<Holder<T>> paramList) {
/* 148 */     this.contents = List.copyOf(paramList);
/*     */   }
/*     */   
/*     */   public TagKey<T> key() {
/* 152 */     return this.key;
/*     */   }
/*     */ 
/*     */   
/*     */   protected List<Holder<T>> contents() {
/* 157 */     if (this.contents == null) {
/* 158 */       throw new IllegalStateException("Trying to access unbound tag '" + String.valueOf(this.key) + "' from registry " + String.valueOf(this.owner));
/*     */     }
/* 160 */     return this.contents;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBound() {
/* 165 */     return (this.contents != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public Either<TagKey<T>, List<Holder<T>>> unwrap() {
/* 170 */     return Either.left(this.key);
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<TagKey<T>> unwrapKey() {
/* 175 */     return Optional.of(this.key);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Holder<T> paramHolder) {
/* 180 */     return paramHolder.is(this.key);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 185 */     return "NamedSet(" + String.valueOf(this.key) + ")[" + String.valueOf(this.contents) + "]";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canSerializeIn(HolderOwner<T> paramHolderOwner) {
/* 190 */     return this.owner.canSerializeIn(paramHolderOwner);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\HolderSet$Named.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */