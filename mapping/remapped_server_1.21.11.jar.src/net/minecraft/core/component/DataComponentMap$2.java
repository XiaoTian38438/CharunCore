/*    */ package net.minecraft.core.component;
/*    */ 
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.Set;
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
/*    */ class null
/*    */   implements DataComponentMap
/*    */ {
/*    */   public <T> T get(DataComponentType<? extends T> paramDataComponentType) {
/* 79 */     T t = (T)overrides.get((DataComponentType)paramDataComponentType);
/* 80 */     if (t != null) {
/* 81 */       return t;
/*    */     }
/* 83 */     return prototype.get(paramDataComponentType);
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<DataComponentType<?>> keySet() {
/* 88 */     return (Set<DataComponentType<?>>)Sets.union(prototype.keySet(), overrides.keySet());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\component\DataComponentMap$2.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */