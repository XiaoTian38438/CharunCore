/*    */ package net.minecraft.commands.arguments.coordinates;
/*    */ 
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.world.phys.Vec2;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public interface Coordinates
/*    */ {
/*    */   Vec3 getPosition(CommandSourceStack paramCommandSourceStack);
/*    */   
/*    */   default BlockPos getBlockPos(CommandSourceStack paramCommandSourceStack) {
/* 14 */     return BlockPos.containing((Position)getPosition(paramCommandSourceStack));
/*    */   }
/*    */   
/*    */   Vec2 getRotation(CommandSourceStack paramCommandSourceStack);
/*    */   
/*    */   boolean isXRelative();
/*    */   
/*    */   boolean isYRelative();
/*    */   
/*    */   boolean isZRelative();
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\arguments\coordinates\Coordinates.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */