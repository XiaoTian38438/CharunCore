/*     */ package com.mojang.math;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.Util;
/*     */ import org.apache.commons.lang3.tuple.Triple;
/*     */ import org.joml.Matrix3f;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Quaternionf;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Transformation
/*     */ {
/*     */   private final Matrix4fc matrix;
/*     */   public static final Codec<Transformation> CODEC;
/*     */   
/*     */   static {
/*  35 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)ExtraCodecs.VECTOR3F.fieldOf("translation").forGetter(()), (App)ExtraCodecs.QUATERNIONF.fieldOf("left_rotation").forGetter(()), (App)ExtraCodecs.VECTOR3F.fieldOf("scale").forGetter(()), (App)ExtraCodecs.QUATERNIONF.fieldOf("right_rotation").forGetter(())).apply((Applicative)paramInstance, Transformation::new));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  43 */   public static final Codec<Transformation> EXTENDED_CODEC = Codec.withAlternative(CODEC, ExtraCodecs.MATRIX4F
/*     */       
/*  45 */       .xmap(Transformation::new, Transformation::getMatrix));
/*     */   
/*     */   private boolean decomposed;
/*     */   private Vector3fc translation;
/*     */   private Quaternionfc leftRotation;
/*     */   private Vector3fc scale;
/*     */   private Quaternionfc rightRotation;
/*     */   private static final Transformation IDENTITY;
/*     */   
/*     */   public Transformation(Matrix4fc paramMatrix4fc) {
/*  55 */     if (paramMatrix4fc == null) {
/*  56 */       this.matrix = (Matrix4fc)new Matrix4f();
/*     */     } else {
/*  58 */       this.matrix = paramMatrix4fc;
/*     */     } 
/*     */   }
/*     */   
/*     */   public Transformation(Vector3fc paramVector3fc1, Quaternionfc paramQuaternionfc1, Vector3fc paramVector3fc2, Quaternionfc paramQuaternionfc2) {
/*  63 */     this.matrix = (Matrix4fc)compose(paramVector3fc1, paramQuaternionfc1, paramVector3fc2, paramQuaternionfc2);
/*  64 */     this.translation = (paramVector3fc1 != null) ? paramVector3fc1 : (Vector3fc)new Vector3f();
/*  65 */     this.leftRotation = (paramQuaternionfc1 != null) ? paramQuaternionfc1 : (Quaternionfc)new Quaternionf();
/*  66 */     this.scale = (paramVector3fc2 != null) ? paramVector3fc2 : (Vector3fc)new Vector3f(1.0F, 1.0F, 1.0F);
/*  67 */     this.rightRotation = (paramQuaternionfc2 != null) ? paramQuaternionfc2 : (Quaternionfc)new Quaternionf();
/*  68 */     this.decomposed = true;
/*     */   }
/*     */   static {
/*  71 */     IDENTITY = (Transformation)Util.make(() -> {
/*     */           Transformation transformation = new Transformation((Matrix4fc)new Matrix4f());
/*     */           transformation.translation = (Vector3fc)new Vector3f();
/*     */           transformation.leftRotation = (Quaternionfc)new Quaternionf();
/*     */           transformation.scale = (Vector3fc)new Vector3f(1.0F, 1.0F, 1.0F);
/*     */           transformation.rightRotation = (Quaternionfc)new Quaternionf();
/*     */           transformation.decomposed = true;
/*     */           return transformation;
/*     */         });
/*     */   }
/*     */   public static Transformation identity() {
/*  82 */     return IDENTITY;
/*     */   }
/*     */   
/*     */   public Transformation compose(Transformation paramTransformation) {
/*  86 */     Matrix4f matrix4f = getMatrixCopy();
/*  87 */     matrix4f.mul(paramTransformation.getMatrix());
/*  88 */     return new Transformation((Matrix4fc)matrix4f);
/*     */   }
/*     */   
/*     */   public Transformation inverse() {
/*  92 */     if (this == IDENTITY) {
/*  93 */       return this;
/*     */     }
/*  95 */     Matrix4f matrix4f = getMatrixCopy().invertAffine();
/*  96 */     if (matrix4f.isFinite()) {
/*  97 */       return new Transformation((Matrix4fc)matrix4f);
/*     */     }
/*  99 */     return null;
/*     */   }
/*     */   
/*     */   private void ensureDecomposed() {
/* 103 */     if (!this.decomposed) {
/* 104 */       float f = 1.0F / this.matrix.m33();
/* 105 */       Triple<Quaternionf, Vector3f, Quaternionf> triple = MatrixUtil.svdDecompose((new Matrix3f(this.matrix)).scale(f));
/* 106 */       this.translation = (Vector3fc)this.matrix.getTranslation(new Vector3f()).mul(f);
/* 107 */       this.leftRotation = (Quaternionfc)new Quaternionf((Quaternionfc)triple.getLeft());
/* 108 */       this.scale = (Vector3fc)new Vector3f((Vector3fc)triple.getMiddle());
/* 109 */       this.rightRotation = (Quaternionfc)new Quaternionf((Quaternionfc)triple.getRight());
/* 110 */       this.decomposed = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Matrix4f compose(Vector3fc paramVector3fc1, Quaternionfc paramQuaternionfc1, Vector3fc paramVector3fc2, Quaternionfc paramQuaternionfc2) {
/* 115 */     Matrix4f matrix4f = new Matrix4f();
/* 116 */     if (paramVector3fc1 != null) {
/* 117 */       matrix4f.translation(paramVector3fc1);
/*     */     }
/* 119 */     if (paramQuaternionfc1 != null) {
/* 120 */       matrix4f.rotate(paramQuaternionfc1);
/*     */     }
/* 122 */     if (paramVector3fc2 != null) {
/* 123 */       matrix4f.scale(paramVector3fc2);
/*     */     }
/* 125 */     if (paramQuaternionfc2 != null) {
/* 126 */       matrix4f.rotate(paramQuaternionfc2);
/*     */     }
/* 128 */     return matrix4f;
/*     */   }
/*     */   
/*     */   public Matrix4fc getMatrix() {
/* 132 */     return this.matrix;
/*     */   }
/*     */   
/*     */   public Matrix4f getMatrixCopy() {
/* 136 */     return new Matrix4f(this.matrix);
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector3fc getTranslation() {
/* 141 */     ensureDecomposed();
/* 142 */     return this.translation;
/*     */   }
/*     */ 
/*     */   
/*     */   public Quaternionfc getLeftRotation() {
/* 147 */     ensureDecomposed();
/* 148 */     return this.leftRotation;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector3fc getScale() {
/* 153 */     ensureDecomposed();
/* 154 */     return this.scale;
/*     */   }
/*     */ 
/*     */   
/*     */   public Quaternionfc getRightRotation() {
/* 159 */     ensureDecomposed();
/* 160 */     return this.rightRotation;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 165 */     if (this == paramObject) {
/* 166 */       return true;
/*     */     }
/* 168 */     if (paramObject == null || getClass() != paramObject.getClass()) {
/* 169 */       return false;
/*     */     }
/* 171 */     Transformation transformation = (Transformation)paramObject;
/* 172 */     return Objects.equals(this.matrix, transformation.matrix);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 177 */     return Objects.hash(new Object[] { this.matrix });
/*     */   }
/*     */   
/*     */   public Transformation slerp(Transformation paramTransformation, float paramFloat) {
/* 181 */     return new Transformation((Vector3fc)
/* 182 */         getTranslation().lerp(paramTransformation.getTranslation(), paramFloat, new Vector3f()), (Quaternionfc)
/* 183 */         getLeftRotation().slerp(paramTransformation.getLeftRotation(), paramFloat, new Quaternionf()), (Vector3fc)
/* 184 */         getScale().lerp(paramTransformation.getScale(), paramFloat, new Vector3f()), (Quaternionfc)
/* 185 */         getRightRotation().slerp(paramTransformation.getRightRotation(), paramFloat, new Quaternionf()));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\math\Transformation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */