/*     */ package com.mojang.math;
/*     */ import org.apache.commons.lang3.tuple.Triple;
/*     */ import org.joml.Math;
/*     */ import org.joml.Matrix3f;
/*     */ import org.joml.Matrix3fc;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Quaternionf;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector3f;
/*     */ 
/*     */ public class MatrixUtil {
/*  13 */   private static final float G = 3.0F + 2.0F * Math.sqrt(2.0F);
/*     */   
/*  15 */   private static final GivensParameters PI_4 = GivensParameters.fromPositiveAngle(0.7853982F);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Matrix4f mulComponentWise(Matrix4f paramMatrix4f, float paramFloat) {
/*  21 */     return paramMatrix4f.set(paramMatrix4f
/*  22 */         .m00() * paramFloat, paramMatrix4f.m01() * paramFloat, paramMatrix4f.m02() * paramFloat, paramMatrix4f.m03() * paramFloat, paramMatrix4f
/*  23 */         .m10() * paramFloat, paramMatrix4f.m11() * paramFloat, paramMatrix4f.m12() * paramFloat, paramMatrix4f.m13() * paramFloat, paramMatrix4f
/*  24 */         .m20() * paramFloat, paramMatrix4f.m21() * paramFloat, paramMatrix4f.m22() * paramFloat, paramMatrix4f.m23() * paramFloat, paramMatrix4f
/*  25 */         .m30() * paramFloat, paramMatrix4f.m31() * paramFloat, paramMatrix4f.m32() * paramFloat, paramMatrix4f.m33() * paramFloat);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static GivensParameters approxGivensQuat(float paramFloat1, float paramFloat2, float paramFloat3) {
/*  31 */     float f1 = 2.0F * (paramFloat1 - paramFloat3);
/*  32 */     float f2 = paramFloat2;
/*     */ 
/*     */     
/*  35 */     if (G * f2 * f2 < f1 * f1) {
/*  36 */       return GivensParameters.fromUnnormalized(f2, f1);
/*     */     }
/*  38 */     return PI_4;
/*     */   }
/*     */ 
/*     */   
/*     */   private static GivensParameters qrGivensQuat(float paramFloat1, float paramFloat2) {
/*  43 */     float f1 = (float)Math.hypot(paramFloat1, paramFloat2);
/*  44 */     float f2 = (f1 > 1.0E-6F) ? paramFloat2 : 0.0F;
/*  45 */     float f3 = Math.abs(paramFloat1) + Math.max(f1, 1.0E-6F);
/*  46 */     if (paramFloat1 < 0.0F) {
/*  47 */       float f = f2;
/*  48 */       f2 = f3;
/*  49 */       f3 = f;
/*     */     } 
/*  51 */     return GivensParameters.fromUnnormalized(f2, f3);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void similarityTransform(Matrix3f paramMatrix3f1, Matrix3f paramMatrix3f2) {
/*  56 */     paramMatrix3f1.mul((Matrix3fc)paramMatrix3f2);
/*     */     
/*  58 */     paramMatrix3f2.transpose();
/*     */     
/*  60 */     paramMatrix3f2.mul((Matrix3fc)paramMatrix3f1);
/*     */     
/*  62 */     paramMatrix3f1.set((Matrix3fc)paramMatrix3f2);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void stepJacobi(Matrix3f paramMatrix3f1, Matrix3f paramMatrix3f2, Quaternionf paramQuaternionf1, Quaternionf paramQuaternionf2) {
/*  67 */     if (paramMatrix3f1.m01 * paramMatrix3f1.m01 + paramMatrix3f1.m10 * paramMatrix3f1.m10 > 1.0E-6F) {
/*  68 */       GivensParameters givensParameters = approxGivensQuat(paramMatrix3f1.m00, 0.5F * (paramMatrix3f1.m01 + paramMatrix3f1.m10), paramMatrix3f1.m11);
/*     */       
/*  70 */       Quaternionf quaternionf = givensParameters.aroundZ(paramQuaternionf1);
/*  71 */       paramQuaternionf2.mul((Quaternionfc)quaternionf);
/*     */       
/*  73 */       givensParameters.aroundZ(paramMatrix3f2);
/*  74 */       similarityTransform(paramMatrix3f1, paramMatrix3f2);
/*     */     } 
/*     */     
/*  77 */     if (paramMatrix3f1.m02 * paramMatrix3f1.m02 + paramMatrix3f1.m20 * paramMatrix3f1.m20 > 1.0E-6F) {
/*     */       
/*  79 */       GivensParameters givensParameters = approxGivensQuat(paramMatrix3f1.m00, 0.5F * (paramMatrix3f1.m02 + paramMatrix3f1.m20), paramMatrix3f1.m22).inverse();
/*     */       
/*  81 */       Quaternionf quaternionf = givensParameters.aroundY(paramQuaternionf1);
/*  82 */       paramQuaternionf2.mul((Quaternionfc)quaternionf);
/*     */       
/*  84 */       givensParameters.aroundY(paramMatrix3f2);
/*  85 */       similarityTransform(paramMatrix3f1, paramMatrix3f2);
/*     */     } 
/*     */     
/*  88 */     if (paramMatrix3f1.m12 * paramMatrix3f1.m12 + paramMatrix3f1.m21 * paramMatrix3f1.m21 > 1.0E-6F) {
/*  89 */       GivensParameters givensParameters = approxGivensQuat(paramMatrix3f1.m11, 0.5F * (paramMatrix3f1.m12 + paramMatrix3f1.m21), paramMatrix3f1.m22);
/*     */       
/*  91 */       Quaternionf quaternionf = givensParameters.aroundX(paramQuaternionf1);
/*  92 */       paramQuaternionf2.mul((Quaternionfc)quaternionf);
/*     */       
/*  94 */       givensParameters.aroundX(paramMatrix3f2);
/*  95 */       similarityTransform(paramMatrix3f1, paramMatrix3f2);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Quaternionf eigenvalueJacobi(Matrix3f paramMatrix3f, int paramInt) {
/* 110 */     Quaternionf quaternionf1 = new Quaternionf();
/*     */     
/* 112 */     Matrix3f matrix3f = new Matrix3f();
/* 113 */     Quaternionf quaternionf2 = new Quaternionf();
/*     */     
/* 115 */     for (byte b = 0; b < paramInt; b++) {
/* 116 */       stepJacobi(paramMatrix3f, matrix3f, quaternionf2, quaternionf1);
/*     */     }
/*     */     
/* 119 */     quaternionf1.normalize();
/* 120 */     return quaternionf1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Triple<Quaternionf, Vector3f, Quaternionf> svdDecompose(Matrix3f paramMatrix3f) {
/* 133 */     Matrix3f matrix3f1 = new Matrix3f((Matrix3fc)paramMatrix3f);
/* 134 */     matrix3f1.transpose();
/* 135 */     matrix3f1.mul((Matrix3fc)paramMatrix3f);
/*     */     
/* 137 */     Quaternionf quaternionf1 = eigenvalueJacobi(matrix3f1, 5);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 143 */     float f1 = matrix3f1.m00;
/* 144 */     float f2 = matrix3f1.m11;
/*     */ 
/*     */ 
/*     */     
/* 148 */     boolean bool1 = (f1 < 1.0E-6D) ? true : false;
/* 149 */     boolean bool2 = (f2 < 1.0E-6D) ? true : false;
/*     */     
/* 151 */     Matrix3f matrix3f2 = matrix3f1;
/*     */ 
/*     */ 
/*     */     
/* 155 */     Matrix3f matrix3f3 = paramMatrix3f.rotate((Quaternionfc)quaternionf1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 185 */     Quaternionf quaternionf2 = new Quaternionf();
/*     */     
/* 187 */     Quaternionf quaternionf3 = new Quaternionf();
/*     */ 
/*     */     
/* 190 */     if (bool1) {
/* 191 */       givensParameters = qrGivensQuat(matrix3f3.m11, -matrix3f3.m10);
/*     */     } else {
/* 193 */       givensParameters = qrGivensQuat(matrix3f3.m00, matrix3f3.m01);
/*     */     } 
/*     */     
/* 196 */     Quaternionf quaternionf4 = givensParameters.aroundZ(quaternionf3);
/* 197 */     Matrix3f matrix3f4 = givensParameters.aroundZ(matrix3f2);
/*     */ 
/*     */ 
/*     */     
/* 201 */     quaternionf2.mul((Quaternionfc)quaternionf4);
/* 202 */     matrix3f4.transpose().mul((Matrix3fc)matrix3f3);
/*     */     
/* 204 */     matrix3f2 = matrix3f3;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 209 */     if (bool1) {
/* 210 */       givensParameters = qrGivensQuat(matrix3f4.m22, -matrix3f4.m20);
/*     */     } else {
/* 212 */       givensParameters = qrGivensQuat(matrix3f4.m00, matrix3f4.m02);
/*     */     } 
/*     */     
/* 215 */     GivensParameters givensParameters = givensParameters.inverse();
/*     */     
/* 217 */     Quaternionf quaternionf5 = givensParameters.aroundY(quaternionf3);
/*     */     
/* 219 */     Matrix3f matrix3f5 = givensParameters.aroundY(matrix3f2);
/*     */ 
/*     */     
/* 222 */     quaternionf2.mul((Quaternionfc)quaternionf5);
/* 223 */     matrix3f5.transpose().mul((Matrix3fc)matrix3f4);
/*     */     
/* 225 */     matrix3f2 = matrix3f4;
/*     */ 
/*     */     
/* 228 */     if (bool2) {
/* 229 */       givensParameters = qrGivensQuat(matrix3f5.m22, -matrix3f5.m21);
/*     */     } else {
/* 231 */       givensParameters = qrGivensQuat(matrix3f5.m11, matrix3f5.m12);
/*     */     } 
/*     */     
/* 234 */     Quaternionf quaternionf6 = givensParameters.aroundX(quaternionf3);
/*     */     
/* 236 */     Matrix3f matrix3f6 = givensParameters.aroundX(matrix3f2);
/*     */ 
/*     */     
/* 239 */     quaternionf2.mul((Quaternionfc)quaternionf6);
/* 240 */     matrix3f6.transpose().mul((Matrix3fc)matrix3f5);
/*     */ 
/*     */     
/* 243 */     Vector3f vector3f = new Vector3f(matrix3f6.m00, matrix3f6.m11, matrix3f6.m22);
/*     */ 
/*     */     
/* 246 */     return Triple.of(quaternionf2, vector3f, quaternionf1.conjugate());
/*     */   }
/*     */   
/*     */   private static boolean checkPropertyRaw(Matrix4fc paramMatrix4fc, int paramInt) {
/* 250 */     return ((paramMatrix4fc.properties() & paramInt) != 0);
/*     */   }
/*     */   
/*     */   public static boolean checkProperty(Matrix4fc paramMatrix4fc, int paramInt) {
/* 254 */     if (checkPropertyRaw(paramMatrix4fc, paramInt)) {
/* 255 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 259 */     if (paramMatrix4fc instanceof Matrix4f) { Matrix4f matrix4f = (Matrix4f)paramMatrix4fc;
/* 260 */       matrix4f.determineProperties();
/* 261 */       return checkPropertyRaw(paramMatrix4fc, paramInt); }
/*     */ 
/*     */     
/* 264 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean isIdentity(Matrix4fc paramMatrix4fc) {
/* 268 */     return checkProperty(paramMatrix4fc, 4);
/*     */   }
/*     */   
/*     */   public static boolean isPureTranslation(Matrix4fc paramMatrix4fc) {
/* 272 */     return checkProperty(paramMatrix4fc, 8);
/*     */   }
/*     */   
/*     */   public static boolean isOrthonormal(Matrix4fc paramMatrix4fc) {
/* 276 */     return checkProperty(paramMatrix4fc, 16);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\math\MatrixUtil.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */