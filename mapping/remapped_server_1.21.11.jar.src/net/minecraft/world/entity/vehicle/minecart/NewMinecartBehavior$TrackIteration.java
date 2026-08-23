/*    */ package net.minecraft.world.entity.vehicle.minecart;
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
/*    */ class TrackIteration
/*    */ {
/* 56 */   double movementLeft = 0.0D;
/*    */   boolean firstIteration = true;
/*    */   boolean hasGainedSlopeSpeed = false;
/*    */   boolean hasHalted = false;
/*    */   boolean hasBoosted = false;
/*    */   
/*    */   public boolean shouldIterate() {
/* 63 */     return (this.firstIteration || this.movementLeft > 9.999999747378752E-6D);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\vehicle\minecart\NewMinecartBehavior$TrackIteration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */