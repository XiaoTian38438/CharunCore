/*    */ package net.minecraft.world.attribute;
/*    */ 
/*    */ import net.minecraft.world.level.Level;
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
/*    */ public interface WeatherAccess
/*    */ {
/*    */   static WeatherAccess from(final Level level) {
/* 67 */     return new WeatherAccess()
/*    */       {
/*    */         public float rainLevel() {
/* 70 */           return level.getRainLevel(1.0F);
/*    */         }
/*    */ 
/*    */         
/*    */         public float thunderLevel() {
/* 75 */           return level.getThunderLevel(1.0F);
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   float rainLevel();
/*    */   
/*    */   float thunderLevel();
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\attribute\WeatherAttributes$WeatherAccess.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */