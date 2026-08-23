/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.SegmentedAnglePrecision;
/*    */ 
/*    */ public class RotationSegment
/*    */ {
/*  9 */   private static final SegmentedAnglePrecision SEGMENTED_ANGLE16 = new SegmentedAnglePrecision(4);
/*    */   
/* 11 */   private static final int MAX_SEGMENT_INDEX = SEGMENTED_ANGLE16.getMask();
/*    */   
/*    */   private static final int NORTH_0 = 0;
/*    */   private static final int EAST_90 = 4;
/*    */   private static final int SOUTH_180 = 8;
/*    */   private static final int WEST_270 = 12;
/*    */   
/*    */   public static int getMaxSegmentIndex() {
/* 19 */     return MAX_SEGMENT_INDEX;
/*    */   }
/*    */   
/*    */   public static int convertToSegment(Direction paramDirection) {
/* 23 */     return SEGMENTED_ANGLE16.fromDirection(paramDirection);
/*    */   }
/*    */   
/*    */   public static int convertToSegment(float paramFloat) {
/* 27 */     return SEGMENTED_ANGLE16.fromDegrees(paramFloat);
/*    */   }
/*    */   
/*    */   public static Optional<Direction> convertToDirection(int paramInt) {
/* 31 */     switch (paramInt) { case 0: 
/*    */       case 4: 
/*    */       case 8:
/*    */       
/*    */       case 12:
/* 36 */        }  Direction direction = null;
/*    */ 
/*    */     
/* 39 */     return Optional.ofNullable(direction);
/*    */   }
/*    */   
/*    */   public static float convertToDegrees(int paramInt) {
/* 43 */     return SEGMENTED_ANGLE16.toDegrees(paramInt);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\state\properties\RotationSegment.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */