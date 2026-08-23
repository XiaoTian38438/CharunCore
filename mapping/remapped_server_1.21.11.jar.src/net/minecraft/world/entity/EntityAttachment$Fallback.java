/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.world.phys.Vec3;
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
/*    */ public interface Fallback
/*    */ {
/* 30 */   public static final List<Vec3> ZERO = List.of(Vec3.ZERO);
/*    */   static {
/* 32 */     AT_HEIGHT = ((paramFloat1, paramFloat2) -> List.of(new Vec3(0.0D, paramFloat2, 0.0D)));
/* 33 */     AT_CENTER = ((paramFloat1, paramFloat2) -> List.of(new Vec3(0.0D, paramFloat2 / 2.0D, 0.0D)));
/*    */   }
/*    */   
/*    */   public static final Fallback AT_FEET = (paramFloat1, paramFloat2) -> ZERO;
/*    */   public static final Fallback AT_HEIGHT;
/*    */   public static final Fallback AT_CENTER;
/*    */   
/*    */   List<Vec3> create(float paramFloat1, float paramFloat2);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\EntityAttachment$Fallback.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */