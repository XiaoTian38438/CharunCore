/*    */ package com.mojang.datafixers;
/*    */ 
/*    */ import com.google.common.reflect.TypeToken;
/*    */ import com.mojang.datafixers.optics.profunctors.Mapping;
/*    */ import com.mojang.datafixers.optics.profunctors.MonoidProfunctor;
/*    */ import com.mojang.datafixers.optics.profunctors.Monoidal;
/*    */ import com.mojang.datafixers.optics.profunctors.TraversalP;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Mu
/*    */   implements TraversalP.Mu, MonoidProfunctor.Mu, Mapping.Mu, Monoidal.Mu
/*    */ {
/* 71 */   public static final TypeToken<Mu> TYPE_TOKEN = new TypeToken<Mu>() {
/*    */     
/*    */     };
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\FunctionType$Instance$Mu.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */