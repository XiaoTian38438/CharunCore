/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterator;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public interface HolderSet<T>
/*     */   extends Iterable<Holder<T>>
/*     */ {
/*     */   Stream<Holder<T>> stream();
/*     */   
/*     */   int size();
/*     */   
/*     */   boolean isBound();
/*     */   
/*     */   Either<TagKey<T>, List<Holder<T>>> unwrap();
/*     */   
/*     */   Optional<Holder<T>> getRandomElement(RandomSource paramRandomSource);
/*     */   
/*     */   Holder<T> get(int paramInt);
/*     */   
/*     */   boolean contains(Holder<T> paramHolder);
/*     */   
/*     */   boolean canSerializeIn(HolderOwner<T> paramHolderOwner);
/*     */   
/*     */   Optional<TagKey<T>> unwrapKey();
/*     */   
/*     */   public static abstract class ListBacked<T>
/*     */     implements HolderSet<T> {
/*     */     protected abstract List<Holder<T>> contents();
/*     */     
/*     */     public int size() {
/*  43 */       return contents().size();
/*     */     }
/*     */ 
/*     */     
/*     */     public Spliterator<Holder<T>> spliterator() {
/*  48 */       return contents().spliterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<Holder<T>> iterator() {
/*  53 */       return contents().iterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public Stream<Holder<T>> stream() {
/*  58 */       return contents().stream();
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Holder<T>> getRandomElement(RandomSource param1RandomSource) {
/*  63 */       return Util.getRandomSafe(contents(), param1RandomSource);
/*     */     }
/*     */ 
/*     */     
/*     */     public Holder<T> get(int param1Int) {
/*  68 */       return contents().get(param1Int);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canSerializeIn(HolderOwner<T> param1HolderOwner) {
/*  73 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Direct<T> extends ListBacked<T> {
/*  78 */     static final Direct<?> EMPTY = new Direct(List.of());
/*     */     
/*     */     private final List<Holder<T>> contents;
/*     */     
/*     */     private Set<Holder<T>> contentsSet;
/*     */ 
/*     */     
/*     */     Direct(List<Holder<T>> param1List) {
/*  86 */       this.contents = param1List;
/*     */     }
/*     */ 
/*     */     
/*     */     protected List<Holder<T>> contents() {
/*  91 */       return this.contents;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isBound() {
/*  96 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public Either<TagKey<T>, List<Holder<T>>> unwrap() {
/* 101 */       return Either.right(this.contents);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<TagKey<T>> unwrapKey() {
/* 106 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Holder<T> param1Holder) {
/* 111 */       if (this.contentsSet == null) {
/* 112 */         this.contentsSet = Set.copyOf(this.contents);
/*     */       }
/* 114 */       return this.contentsSet.contains(param1Holder);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 119 */       return "DirectSet[" + String.valueOf(this.contents) + "]";
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object param1Object) {
/* 124 */       if (this == param1Object) {
/* 125 */         return true;
/*     */       }
/* 127 */       if (param1Object instanceof Direct) { Direct direct = (Direct)param1Object; if (this.contents.equals(direct.contents)); }  return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 132 */       return this.contents.hashCode();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Named<T>
/*     */     extends ListBacked<T> {
/*     */     private final HolderOwner<T> owner;
/*     */     private final TagKey<T> key;
/*     */     private List<Holder<T>> contents;
/*     */     
/*     */     Named(HolderOwner<T> param1HolderOwner, TagKey<T> param1TagKey) {
/* 143 */       this.owner = param1HolderOwner;
/* 144 */       this.key = param1TagKey;
/*     */     }
/*     */     
/*     */     void bind(List<Holder<T>> param1List) {
/* 148 */       this.contents = List.copyOf(param1List);
/*     */     }
/*     */     
/*     */     public TagKey<T> key() {
/* 152 */       return this.key;
/*     */     }
/*     */ 
/*     */     
/*     */     protected List<Holder<T>> contents() {
/* 157 */       if (this.contents == null) {
/* 158 */         throw new IllegalStateException("Trying to access unbound tag '" + String.valueOf(this.key) + "' from registry " + String.valueOf(this.owner));
/*     */       }
/* 160 */       return this.contents;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isBound() {
/* 165 */       return (this.contents != null);
/*     */     }
/*     */ 
/*     */     
/*     */     public Either<TagKey<T>, List<Holder<T>>> unwrap() {
/* 170 */       return Either.left(this.key);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<TagKey<T>> unwrapKey() {
/* 175 */       return Optional.of(this.key);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Holder<T> param1Holder) {
/* 180 */       return param1Holder.is(this.key);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 185 */       return "NamedSet(" + String.valueOf(this.key) + ")[" + String.valueOf(this.contents) + "]";
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canSerializeIn(HolderOwner<T> param1HolderOwner) {
/* 190 */       return this.owner.canSerializeIn(param1HolderOwner);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @VisibleForTesting
/*     */   static <T> Named<T> emptyNamed(HolderOwner<T> paramHolderOwner, TagKey<T> paramTagKey) {
/* 202 */     return new Named<T>(paramHolderOwner, paramTagKey)
/*     */       {
/*     */         protected List<Holder<T>> contents() {
/* 205 */           throw new UnsupportedOperationException("Tag " + String.valueOf(key()) + " can't be dereferenced during construction");
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   static <T> HolderSet<T> empty() {
/* 212 */     return (HolderSet)Direct.EMPTY;
/*     */   }
/*     */   
/*     */   @SafeVarargs
/*     */   static <T> Direct<T> direct(Holder<T>... paramVarArgs) {
/* 217 */     return new Direct<>(List.of(paramVarArgs));
/*     */   }
/*     */   
/*     */   static <T> Direct<T> direct(List<? extends Holder<T>> paramList) {
/* 221 */     return new Direct<>(List.copyOf(paramList));
/*     */   }
/*     */   
/*     */   @SafeVarargs
/*     */   static <E, T> Direct<T> direct(Function<E, Holder<T>> paramFunction, E... paramVarArgs) {
/* 226 */     return direct(Stream.<E>of(paramVarArgs).<Holder<T>>map(paramFunction).toList());
/*     */   }
/*     */   
/*     */   static <E, T> Direct<T> direct(Function<E, Holder<T>> paramFunction, Collection<E> paramCollection) {
/* 230 */     return direct(paramCollection.stream().<Holder<T>>map(paramFunction).toList());
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\HolderSet.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */