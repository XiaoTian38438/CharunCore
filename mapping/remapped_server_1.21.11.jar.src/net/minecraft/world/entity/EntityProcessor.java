/*   */ package net.minecraft.world.entity;
/*   */ 
/*   */ @FunctionalInterface
/*   */ public interface EntityProcessor
/*   */ {
/*   */   static {
/* 7 */     NOP = (paramEntity -> paramEntity);
/*   */   }
/*   */   
/*   */   public static final EntityProcessor NOP;
/*   */   
/*   */   Entity process(Entity paramEntity);
/*   */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\EntityProcessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */