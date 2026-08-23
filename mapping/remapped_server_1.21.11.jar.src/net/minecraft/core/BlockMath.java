/*    */ package net.minecraft.core;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.math.MatrixUtil;
/*    */ import com.mojang.math.Transformation;
/*    */ import java.util.Map;
/*    */ import net.minecraft.util.Util;
/*    */ import org.joml.Matrix4f;
/*    */ import org.joml.Matrix4fc;
/*    */ import org.joml.Quaternionf;
/*    */ import org.joml.Quaternionfc;
/*    */ import org.joml.Vector3f;
/*    */ 
/*    */ public class BlockMath
/*    */ {
/* 16 */   private static final Map<Direction, Transformation> VANILLA_UV_TRANSFORM_LOCAL_TO_GLOBAL = Maps.newEnumMap(Map.of(Direction.SOUTH, 
/* 17 */         Transformation.identity(), Direction.EAST, new Transformation(null, (Quaternionfc)(new Quaternionf())
/* 18 */           .rotateY(1.5707964F), null, null), Direction.WEST, new Transformation(null, (Quaternionfc)(new Quaternionf())
/* 19 */           .rotateY(-1.5707964F), null, null), Direction.NORTH, new Transformation(null, (Quaternionfc)(new Quaternionf())
/* 20 */           .rotateY(3.1415927F), null, null), Direction.UP, new Transformation(null, (Quaternionfc)(new Quaternionf())
/* 21 */           .rotateX(-1.5707964F), null, null), Direction.DOWN, new Transformation(null, (Quaternionfc)(new Quaternionf())
/* 22 */           .rotateX(1.5707964F), null, null)));
/*    */   
/* 24 */   private static final Map<Direction, Transformation> VANILLA_UV_TRANSFORM_GLOBAL_TO_LOCAL = Maps.newEnumMap(Util.mapValues(VANILLA_UV_TRANSFORM_LOCAL_TO_GLOBAL, Transformation::inverse));
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Transformation blockCenterToCorner(Transformation paramTransformation) {
/* 30 */     Matrix4f matrix4f = (new Matrix4f()).translation(0.5F, 0.5F, 0.5F);
/* 31 */     matrix4f.mul(paramTransformation.getMatrix());
/* 32 */     matrix4f.translate(-0.5F, -0.5F, -0.5F);
/* 33 */     return new Transformation((Matrix4fc)matrix4f);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Transformation blockCornerToCenter(Transformation paramTransformation) {
/* 40 */     Matrix4f matrix4f = (new Matrix4f()).translation(-0.5F, -0.5F, -0.5F);
/* 41 */     matrix4f.mul(paramTransformation.getMatrix());
/* 42 */     matrix4f.translate(0.5F, 0.5F, 0.5F);
/* 43 */     return new Transformation((Matrix4fc)matrix4f);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Transformation getFaceTransformation(Transformation paramTransformation, Direction paramDirection) {
/* 50 */     if (MatrixUtil.isIdentity(paramTransformation.getMatrix())) {
/* 51 */       return paramTransformation;
/*    */     }
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
/* 64 */     Transformation transformation = VANILLA_UV_TRANSFORM_LOCAL_TO_GLOBAL.get(paramDirection);
/*    */ 
/*    */     
/* 67 */     transformation = paramTransformation.compose(transformation);
/*    */ 
/*    */ 
/*    */     
/* 71 */     Vector3f vector3f = transformation.getMatrix().transformDirection(new Vector3f(0.0F, 0.0F, 1.0F));
/* 72 */     Direction direction = Direction.getApproximateNearest(vector3f.x, vector3f.y, vector3f.z);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 77 */     return ((Transformation)VANILLA_UV_TRANSFORM_GLOBAL_TO_LOCAL.get(direction)).compose(transformation);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\BlockMath.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */