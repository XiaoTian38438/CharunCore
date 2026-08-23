/*    */ package net.minecraft.server.permissions;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AlwaysPass
/*    */   implements PermissionCheck
/*    */ {
/* 21 */   public static final AlwaysPass INSTANCE = new AlwaysPass();
/*    */   
/* 23 */   public static final MapCodec<AlwaysPass> MAP_CODEC = MapCodec.unit(INSTANCE);
/*    */ 
/*    */   
/*    */   public boolean check(PermissionSet paramPermissionSet) {
/* 27 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public MapCodec<AlwaysPass> codec() {
/* 32 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\permissions\PermissionCheck$AlwaysPass.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */