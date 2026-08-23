/*    */ package net.minecraft.world.attribute;
/*    */ 
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   implements EnvironmentAttributeReader
/*    */ {
/*    */   public <Value> Value getDimensionValue(EnvironmentAttribute<Value> paramEnvironmentAttribute) {
/* 11 */     return paramEnvironmentAttribute.defaultValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public <Value> Value getValue(EnvironmentAttribute<Value> paramEnvironmentAttribute, Vec3 paramVec3, SpatialAttributeInterpolator paramSpatialAttributeInterpolator) {
/* 16 */     return paramEnvironmentAttribute.defaultValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\attribute\EnvironmentAttributeReader$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */