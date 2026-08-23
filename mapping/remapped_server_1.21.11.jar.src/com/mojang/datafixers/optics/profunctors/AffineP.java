/*    */ package com.mojang.datafixers.optics.profunctors;
/*    */ 
/*    */ import com.google.common.reflect.TypeToken;
/*    */ 
/*    */ public interface AffineP<P extends com.mojang.datafixers.kinds.K2, Mu extends AffineP.Mu>
/*    */   extends Cartesian<P, Mu>, Cocartesian<P, Mu>
/*    */ {
/*    */   public static interface Mu
/*    */     extends Cartesian.Mu, Cocartesian.Mu {
/* 10 */     public static final TypeToken<Mu> TYPE_TOKEN = new TypeToken<Mu>() {
/*    */       
/*    */       };
/*    */   }
/*    */   
/*    */   class null extends TypeToken<Mu> {}
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\profunctors\AffineP.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */