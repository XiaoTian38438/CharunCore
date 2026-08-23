/*     */ package net.minecraft.world.level.chunk;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.IdMap;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.VarInt;
/*     */ import net.minecraft.util.CrudeIncrementalIntIdentityHashBiMap;
/*     */ 
/*     */ public class HashMapPalette<T> implements Palette<T> {
/*     */   private final CrudeIncrementalIntIdentityHashBiMap<T> values;
/*     */   private final int bits;
/*     */   
/*     */   public HashMapPalette(int paramInt, List<T> paramList) {
/*  17 */     this(paramInt);
/*  18 */     Objects.requireNonNull(this.values); paramList.forEach(this.values::add);
/*     */   }
/*     */   
/*     */   public HashMapPalette(int paramInt) {
/*  22 */     this(paramInt, CrudeIncrementalIntIdentityHashBiMap.create(1 << paramInt));
/*     */   }
/*     */   
/*     */   private HashMapPalette(int paramInt, CrudeIncrementalIntIdentityHashBiMap<T> paramCrudeIncrementalIntIdentityHashBiMap) {
/*  26 */     this.bits = paramInt;
/*  27 */     this.values = paramCrudeIncrementalIntIdentityHashBiMap;
/*     */   }
/*     */   
/*     */   public static <A> Palette<A> create(int paramInt, List<A> paramList) {
/*  31 */     return new HashMapPalette<>(paramInt, paramList);
/*     */   }
/*     */ 
/*     */   
/*     */   public int idFor(T paramT, PaletteResize<T> paramPaletteResize) {
/*  36 */     int i = this.values.getId(paramT);
/*  37 */     if (i == -1) {
/*  38 */       i = this.values.add(paramT);
/*     */       
/*  40 */       if (i >= 1 << this.bits) {
/*  41 */         i = paramPaletteResize.onResize(this.bits + 1, paramT);
/*     */       }
/*     */     } 
/*  44 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean maybeHas(Predicate<T> paramPredicate) {
/*  49 */     for (byte b = 0; b < getSize(); b++) {
/*  50 */       if (paramPredicate.test((T)this.values.byId(b))) {
/*  51 */         return true;
/*     */       }
/*     */     } 
/*  54 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public T valueFor(int paramInt) {
/*  59 */     Object object = this.values.byId(paramInt);
/*  60 */     if (object == null) {
/*  61 */       throw new MissingPaletteEntryException(paramInt);
/*     */     }
/*  63 */     return (T)object;
/*     */   }
/*     */ 
/*     */   
/*     */   public void read(FriendlyByteBuf paramFriendlyByteBuf, IdMap<T> paramIdMap) {
/*  68 */     this.values.clear();
/*  69 */     int i = paramFriendlyByteBuf.readVarInt();
/*  70 */     for (byte b = 0; b < i; b++) {
/*  71 */       this.values.add(paramIdMap.byIdOrThrow(paramFriendlyByteBuf.readVarInt()));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(FriendlyByteBuf paramFriendlyByteBuf, IdMap<T> paramIdMap) {
/*  77 */     int i = getSize();
/*  78 */     paramFriendlyByteBuf.writeVarInt(i);
/*     */     
/*  80 */     for (byte b = 0; b < i; b++) {
/*  81 */       paramFriendlyByteBuf.writeVarInt(paramIdMap.getId(this.values.byId(b)));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSerializedSize(IdMap<T> paramIdMap) {
/*  87 */     int i = VarInt.getByteSize(getSize());
/*     */     
/*  89 */     for (byte b = 0; b < getSize(); b++) {
/*  90 */       i += VarInt.getByteSize(paramIdMap.getId(this.values.byId(b)));
/*     */     }
/*     */     
/*  93 */     return i;
/*     */   }
/*     */   
/*     */   public List<T> getEntries() {
/*  97 */     ArrayList<T> arrayList = new ArrayList();
/*  98 */     Objects.requireNonNull(arrayList); this.values.iterator().forEachRemaining(arrayList::add);
/*  99 */     return arrayList;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSize() {
/* 104 */     return this.values.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public Palette<T> copy() {
/* 109 */     return new HashMapPalette(this.bits, this.values.copy());
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\chunk\HashMapPalette.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */