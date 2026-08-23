/*    */ package net.minecraft.world.level.redstone;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.flag.FeatureFlags;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ 
/*    */ public class ExperimentalRedstoneUtils
/*    */ {
/*    */   public static Orientation initialOrientation(Level paramLevel, Direction paramDirection1, Direction paramDirection2) {
/* 11 */     if (paramLevel.enabledFeatures().contains(FeatureFlags.REDSTONE_EXPERIMENTS)) {
/* 12 */       Orientation orientation = Orientation.random(paramLevel.random).withSideBias(Orientation.SideBias.LEFT);
/* 13 */       if (paramDirection2 != null) {
/* 14 */         orientation = orientation.withUp(paramDirection2);
/*    */       }
/* 16 */       if (paramDirection1 != null) {
/* 17 */         orientation = orientation.withFront(paramDirection1);
/*    */       }
/* 19 */       return orientation;
/*    */     } 
/* 21 */     return null;
/*    */   }
/*    */   
/*    */   public static Orientation withFront(Orientation paramOrientation, Direction paramDirection) {
/* 25 */     return (paramOrientation == null) ? null : paramOrientation.withFront(paramDirection);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\redstone\ExperimentalRedstoneUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */