/*   */ package net.minecraft.world.entity.ai.control;
/*   */ 
/*   */ import net.minecraft.util.Mth;
/*   */ 
/*   */ public interface Control {
/*   */   default float rotateTowards(float paramFloat1, float paramFloat2, float paramFloat3) {
/* 7 */     float f1 = Mth.degreesDifference(paramFloat1, paramFloat2);
/* 8 */     float f2 = Mth.clamp(f1, -paramFloat3, paramFloat3);
/* 9 */     return paramFloat1 + f2;
/*   */   }
/*   */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\control\Control.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */