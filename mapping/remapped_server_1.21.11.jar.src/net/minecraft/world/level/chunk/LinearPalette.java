/*     */ package net.minecraft.world.level.chunk;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.IdMap;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.VarInt;
/*     */ import org.apache.commons.lang3.Validate;
/*     */ 
/*     */ public class LinearPalette<T>
/*     */   implements Palette<T>
/*     */ {
/*     */   private final T[] values;
/*     */   private final int bits;
/*     */   private int size;
/*     */   
/*     */   private LinearPalette(int paramInt, List<T> paramList) {
/*  18 */     this.values = (T[])new Object[1 << paramInt];
/*  19 */     this.bits = paramInt;
/*  20 */     Validate.isTrue((paramList.size() <= this.values.length), "Can't initialize LinearPalette of size %d with %d entries", new Object[] { Integer.valueOf(this.values.length), Integer.valueOf(paramList.size()) });
/*  21 */     for (byte b = 0; b < paramList.size(); b++) {
/*  22 */       this.values[b] = paramList.get(b);
/*     */     }
/*  24 */     this.size = paramList.size();
/*     */   }
/*     */   
/*     */   private LinearPalette(T[] paramArrayOfT, int paramInt1, int paramInt2) {
/*  28 */     this.values = paramArrayOfT;
/*  29 */     this.bits = paramInt1;
/*  30 */     this.size = paramInt2;
/*     */   }
/*     */   
/*     */   public static <A> Palette<A> create(int paramInt, List<A> paramList) {
/*  34 */     return new LinearPalette<>(paramInt, paramList);
/*     */   }
/*     */   
/*     */   public int idFor(T paramT, PaletteResize<T> paramPaletteResize) {
/*     */     int i;
/*  39 */     for (i = 0; i < this.size; i++) {
/*  40 */       if (this.values[i] == paramT) {
/*  41 */         return i;
/*     */       }
/*     */     } 
/*     */     
/*  45 */     i = this.size;
/*  46 */     if (i < this.values.length) {
/*  47 */       this.values[i] = paramT;
/*  48 */       this.size++;
/*  49 */       return i;
/*     */     } 
/*     */     
/*  52 */     return paramPaletteResize.onResize(this.bits + 1, paramT);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean maybeHas(Predicate<T> paramPredicate) {
/*  57 */     for (byte b = 0; b < this.size; b++) {
/*  58 */       if (paramPredicate.test(this.values[b])) {
/*  59 */         return true;
/*     */       }
/*     */     } 
/*  62 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public T valueFor(int paramInt) {
/*  67 */     if (paramInt >= 0 && paramInt < this.size) {
/*  68 */       return this.values[paramInt];
/*     */     }
/*  70 */     throw new MissingPaletteEntryException(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public void read(FriendlyByteBuf paramFriendlyByteBuf, IdMap<T> paramIdMap) {
/*  75 */     this.size = paramFriendlyByteBuf.readVarInt();
/*  76 */     for (byte b = 0; b < this.size; b++) {
/*  77 */       this.values[b] = (T)paramIdMap.byIdOrThrow(paramFriendlyByteBuf.readVarInt());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(FriendlyByteBuf paramFriendlyByteBuf, IdMap<T> paramIdMap) {
/*  83 */     paramFriendlyByteBuf.writeVarInt(this.size);
/*  84 */     for (byte b = 0; b < this.size; b++) {
/*  85 */       paramFriendlyByteBuf.writeVarInt(paramIdMap.getId(this.values[b]));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSerializedSize(IdMap<T> paramIdMap) {
/*  91 */     int i = VarInt.getByteSize(getSize());
/*     */     
/*  93 */     for (byte b = 0; b < getSize(); b++) {
/*  94 */       i += VarInt.getByteSize(paramIdMap.getId(this.values[b]));
/*     */     }
/*     */     
/*  97 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSize() {
/* 102 */     return this.size;
/*     */   }
/*     */ 
/*     */   
/*     */   public Palette<T> copy() {
/* 107 */     return new LinearPalette((T[])this.values.clone(), this.bits, this.size);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\chunk\LinearPalette.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */