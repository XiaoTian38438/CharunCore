/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Type
/*    */ {
/* 15 */   static final Int2ObjectMap<Type> TYPES = (Int2ObjectMap<Type>)new Int2ObjectOpenHashMap();
/*    */   
/*    */   final int id;
/*    */   
/*    */   public Type(int paramInt) {
/* 20 */     this.id = paramInt;
/* 21 */     TYPES.put(paramInt, this);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\game\ClientboundGameEventPacket$Type.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */