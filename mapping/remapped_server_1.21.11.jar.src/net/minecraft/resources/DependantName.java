/*   */ package net.minecraft.resources;
/*   */ 
/*   */ @FunctionalInterface
/*   */ public interface DependantName<T, V> {
/*   */   V get(ResourceKey<T> paramResourceKey);
/*   */   
/*   */   static <T, V> DependantName<T, V> fixed(V paramV) {
/* 8 */     return paramResourceKey -> paramObject;
/*   */   }
/*   */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\resources\DependantName.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */