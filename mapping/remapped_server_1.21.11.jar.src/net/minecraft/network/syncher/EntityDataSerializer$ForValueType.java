/*    */ package net.minecraft.network.syncher;
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
/*    */ public interface ForValueType<T>
/*    */   extends EntityDataSerializer<T>
/*    */ {
/*    */   default T copy(T paramT) {
/* 18 */     return paramT;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\syncher\EntityDataSerializer$ForValueType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */