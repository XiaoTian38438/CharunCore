/*    */ package net.minecraft.world.level.chunk;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.IdMap;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ 
/*    */ public class GlobalPalette<T>
/*    */   implements Palette<T> {
/*    */   private final IdMap<T> registry;
/*    */   
/*    */   public GlobalPalette(IdMap<T> paramIdMap) {
/* 12 */     this.registry = paramIdMap;
/*    */   }
/*    */ 
/*    */   
/*    */   public int idFor(T paramT, PaletteResize<T> paramPaletteResize) {
/* 17 */     int i = this.registry.getId(paramT);
/*    */     
/* 19 */     return (i == -1) ? 0 : i;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean maybeHas(Predicate<T> paramPredicate) {
/* 24 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public T valueFor(int paramInt) {
/* 29 */     Object object = this.registry.byId(paramInt);
/* 30 */     if (object == null) {
/* 31 */       throw new MissingPaletteEntryException(paramInt);
/*    */     }
/* 33 */     return (T)object;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf paramFriendlyByteBuf, IdMap<T> paramIdMap) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf paramFriendlyByteBuf, IdMap<T> paramIdMap) {}
/*    */ 
/*    */   
/*    */   public int getSerializedSize(IdMap<T> paramIdMap) {
/* 46 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getSize() {
/* 51 */     return this.registry.size();
/*    */   }
/*    */ 
/*    */   
/*    */   public Palette<T> copy() {
/* 56 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\chunk\GlobalPalette.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */