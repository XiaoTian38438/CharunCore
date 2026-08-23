/*    */ package net.minecraft.world.level.chunk;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.IdMap;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.VarInt;
/*    */ import org.apache.commons.lang3.Validate;
/*    */ 
/*    */ public class SingleValuePalette<T>
/*    */   implements Palette<T>
/*    */ {
/*    */   private T value;
/*    */   
/*    */   public SingleValuePalette(List<T> paramList) {
/* 16 */     if (!paramList.isEmpty()) {
/* 17 */       Validate.isTrue((paramList.size() <= 1), "Can't initialize SingleValuePalette with %d values.", paramList.size());
/* 18 */       this.value = paramList.getFirst();
/*    */     } 
/*    */   }
/*    */   
/*    */   public static <A> Palette<A> create(int paramInt, List<A> paramList) {
/* 23 */     return new SingleValuePalette<>(paramList);
/*    */   }
/*    */ 
/*    */   
/*    */   public int idFor(T paramT, PaletteResize<T> paramPaletteResize) {
/* 28 */     if (this.value == null || this.value == paramT) {
/* 29 */       this.value = paramT;
/* 30 */       return 0;
/*    */     } 
/* 32 */     return paramPaletteResize.onResize(1, paramT);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean maybeHas(Predicate<T> paramPredicate) {
/* 37 */     if (this.value == null) {
/* 38 */       throw new IllegalStateException("Use of an uninitialized palette");
/*    */     }
/* 40 */     return paramPredicate.test(this.value);
/*    */   }
/*    */ 
/*    */   
/*    */   public T valueFor(int paramInt) {
/* 45 */     if (this.value == null || paramInt != 0) {
/* 46 */       throw new IllegalStateException("Missing Palette entry for id " + paramInt + ".");
/*    */     }
/* 48 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf paramFriendlyByteBuf, IdMap<T> paramIdMap) {
/* 53 */     this.value = (T)paramIdMap.byIdOrThrow(paramFriendlyByteBuf.readVarInt());
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf paramFriendlyByteBuf, IdMap<T> paramIdMap) {
/* 58 */     if (this.value == null) {
/* 59 */       throw new IllegalStateException("Use of an uninitialized palette");
/*    */     }
/* 61 */     paramFriendlyByteBuf.writeVarInt(paramIdMap.getId(this.value));
/*    */   }
/*    */ 
/*    */   
/*    */   public int getSerializedSize(IdMap<T> paramIdMap) {
/* 66 */     if (this.value == null) {
/* 67 */       throw new IllegalStateException("Use of an uninitialized palette");
/*    */     }
/* 69 */     return VarInt.getByteSize(paramIdMap.getId(this.value));
/*    */   }
/*    */ 
/*    */   
/*    */   public int getSize() {
/* 74 */     return 1;
/*    */   }
/*    */ 
/*    */   
/*    */   public Palette<T> copy() {
/* 79 */     if (this.value == null) {
/* 80 */       throw new IllegalStateException("Use of an uninitialized palette");
/*    */     }
/* 82 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\chunk\SingleValuePalette.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */