/*    */ package net.minecraft.world.attribute;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public interface EnvironmentAttributeReader {
/*  8 */   public static final EnvironmentAttributeReader EMPTY = new EnvironmentAttributeReader()
/*    */     {
/*    */       public <Value> Value getDimensionValue(EnvironmentAttribute<Value> param1EnvironmentAttribute) {
/* 11 */         return param1EnvironmentAttribute.defaultValue();
/*    */       }
/*    */ 
/*    */       
/*    */       public <Value> Value getValue(EnvironmentAttribute<Value> param1EnvironmentAttribute, Vec3 param1Vec3, SpatialAttributeInterpolator param1SpatialAttributeInterpolator) {
/* 16 */         return param1EnvironmentAttribute.defaultValue();
/*    */       }
/*    */     };
/*    */   
/*    */   <Value> Value getDimensionValue(EnvironmentAttribute<Value> paramEnvironmentAttribute);
/*    */   
/*    */   default <Value> Value getValue(EnvironmentAttribute<Value> paramEnvironmentAttribute, BlockPos paramBlockPos) {
/* 23 */     return getValue(paramEnvironmentAttribute, Vec3.atCenterOf((Vec3i)paramBlockPos));
/*    */   }
/*    */   
/*    */   default <Value> Value getValue(EnvironmentAttribute<Value> paramEnvironmentAttribute, Vec3 paramVec3) {
/* 27 */     return getValue(paramEnvironmentAttribute, paramVec3, null);
/*    */   }
/*    */   
/*    */   <Value> Value getValue(EnvironmentAttribute<Value> paramEnvironmentAttribute, Vec3 paramVec3, SpatialAttributeInterpolator paramSpatialAttributeInterpolator);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\attribute\EnvironmentAttributeReader.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */