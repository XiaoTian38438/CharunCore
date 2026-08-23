/*    */ package net.minecraft.world.entity.ambient;
/*    */ 
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public abstract class AmbientCreature extends Mob {
/*    */   protected AmbientCreature(EntityType<? extends AmbientCreature> paramEntityType, Level paramLevel) {
/*  9 */     super(paramEntityType, paramLevel);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canBeLeashed() {
/* 14 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ambient\AmbientCreature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */