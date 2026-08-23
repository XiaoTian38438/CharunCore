/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.util.Mth;
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
/*    */ public final class SingleAngle
/*    */ {
/*    */   private final float angle;
/*    */   private final boolean isRelative;
/*    */   
/*    */   SingleAngle(float paramFloat, boolean paramBoolean) {
/* 53 */     this.angle = paramFloat;
/* 54 */     this.isRelative = paramBoolean;
/*    */   }
/*    */   
/*    */   public float getAngle(CommandSourceStack paramCommandSourceStack) {
/* 58 */     return Mth.wrapDegrees(this.isRelative ? (this.angle + (paramCommandSourceStack.getRotation()).y) : this.angle);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\arguments\AngleArgument$SingleAngle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */