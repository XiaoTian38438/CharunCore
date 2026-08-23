/*    */ package com.mojang.datafixers.types.templates;
/*    */ 
/*    */ import com.mojang.serialization.DynamicOps;
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
/*    */ public interface HookFunction
/*    */ {
/* 27 */   public static final HookFunction IDENTITY = new HookFunction()
/*    */     {
/*    */       public <T> T apply(DynamicOps<T> param2DynamicOps, T param2T) {
/* 30 */         return param2T;
/*    */       }
/*    */     };
/*    */   
/*    */   <T> T apply(DynamicOps<T> paramDynamicOps, T paramT);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\types\templates\Hook$HookFunction.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */