/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.phys.Vec2;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class InterpolationHandler
/*     */ {
/*     */   public static final int DEFAULT_INTERPOLATION_STEPS = 3;
/*     */   private final Entity entity;
/*     */   private int interpolationSteps;
/*     */   
/*     */   private static class InterpolationData {
/*     */     protected int steps;
/*     */     Vec3 position;
/*     */     float yRot;
/*     */     float xRot;
/*     */     
/*     */     InterpolationData(int param1Int, Vec3 param1Vec3, float param1Float1, float param1Float2) {
/*  22 */       this.steps = param1Int;
/*  23 */       this.position = param1Vec3;
/*  24 */       this.yRot = param1Float1;
/*  25 */       this.xRot = param1Float2;
/*     */     }
/*     */     
/*     */     public void decrease() {
/*  29 */       this.steps--;
/*     */     }
/*     */     
/*     */     public void addDelta(Vec3 param1Vec3) {
/*  33 */       this.position = this.position.add(param1Vec3);
/*     */     }
/*     */     
/*     */     public void addRotation(float param1Float1, float param1Float2) {
/*  37 */       this.yRot += param1Float1;
/*  38 */       this.xRot += param1Float2;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  44 */   private final InterpolationData interpolationData = new InterpolationData(0, Vec3.ZERO, 0.0F, 0.0F);
/*     */   private Vec3 previousTickPosition;
/*     */   private Vec2 previousTickRot;
/*     */   private final Consumer<InterpolationHandler> onInterpolationStart;
/*     */   
/*     */   public InterpolationHandler(Entity paramEntity) {
/*  50 */     this(paramEntity, 3, null);
/*     */   }
/*     */   
/*     */   public InterpolationHandler(Entity paramEntity, int paramInt) {
/*  54 */     this(paramEntity, paramInt, null);
/*     */   }
/*     */   
/*     */   public InterpolationHandler(Entity paramEntity, Consumer<InterpolationHandler> paramConsumer) {
/*  58 */     this(paramEntity, 3, paramConsumer);
/*     */   }
/*     */   
/*     */   public InterpolationHandler(Entity paramEntity, int paramInt, Consumer<InterpolationHandler> paramConsumer) {
/*  62 */     this.interpolationSteps = paramInt;
/*  63 */     this.entity = paramEntity;
/*  64 */     this.onInterpolationStart = paramConsumer;
/*     */   }
/*     */   
/*     */   public Vec3 position() {
/*  68 */     return (this.interpolationData.steps > 0) ? this.interpolationData.position : this.entity.position();
/*     */   }
/*     */   
/*     */   public float yRot() {
/*  72 */     return (this.interpolationData.steps > 0) ? this.interpolationData.yRot : this.entity.getYRot();
/*     */   }
/*     */   
/*     */   public float xRot() {
/*  76 */     return (this.interpolationData.steps > 0) ? this.interpolationData.xRot : this.entity.getXRot();
/*     */   }
/*     */   
/*     */   public void interpolateTo(Vec3 paramVec3, float paramFloat1, float paramFloat2) {
/*  80 */     if (this.interpolationSteps == 0) {
/*  81 */       this.entity.snapTo(paramVec3, paramFloat1, paramFloat2);
/*  82 */       cancel();
/*     */       
/*     */       return;
/*     */     } 
/*  86 */     if (hasActiveInterpolation() && 
/*  87 */       Objects.equals(Float.valueOf(yRot()), Float.valueOf(paramFloat1)) && 
/*  88 */       Objects.equals(Float.valueOf(xRot()), Float.valueOf(paramFloat2)) && 
/*  89 */       Objects.equals(position(), paramVec3)) {
/*     */       return;
/*     */     }
/*     */     
/*  93 */     this.interpolationData.steps = this.interpolationSteps;
/*  94 */     this.interpolationData.position = paramVec3;
/*  95 */     this.interpolationData.yRot = paramFloat1;
/*  96 */     this.interpolationData.xRot = paramFloat2;
/*     */     
/*  98 */     this.previousTickPosition = this.entity.position();
/*  99 */     this.previousTickRot = new Vec2(this.entity.getXRot(), this.entity.getYRot());
/*     */     
/* 101 */     if (this.onInterpolationStart != null) {
/* 102 */       this.onInterpolationStart.accept(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean hasActiveInterpolation() {
/* 107 */     return (this.interpolationData.steps > 0);
/*     */   }
/*     */   
/*     */   public void setInterpolationLength(int paramInt) {
/* 111 */     this.interpolationSteps = paramInt;
/*     */   }
/*     */   
/*     */   public void interpolate() {
/* 115 */     if (!hasActiveInterpolation()) {
/* 116 */       cancel();
/*     */       
/*     */       return;
/*     */     } 
/* 120 */     double d1 = 1.0D / this.interpolationData.steps;
/*     */     
/* 122 */     if (this.previousTickPosition != null) {
/*     */       
/* 124 */       Vec3 vec31 = this.entity.position().subtract(this.previousTickPosition);
/* 125 */       if (this.entity.level().noCollision(this.entity, this.entity.makeBoundingBox(this.interpolationData.position.add(vec31))))
/*     */       {
/* 127 */         this.interpolationData.addDelta(vec31);
/*     */       }
/*     */     } 
/*     */     
/* 131 */     if (this.previousTickRot != null) {
/*     */       
/* 133 */       float f3 = this.entity.getYRot() - this.previousTickRot.y;
/* 134 */       float f4 = this.entity.getXRot() - this.previousTickRot.x;
/* 135 */       this.interpolationData.addRotation(f3, f4);
/*     */     } 
/*     */     
/* 138 */     double d2 = Mth.lerp(d1, this.entity.getX(), this.interpolationData.position.x);
/* 139 */     double d3 = Mth.lerp(d1, this.entity.getY(), this.interpolationData.position.y);
/* 140 */     double d4 = Mth.lerp(d1, this.entity.getZ(), this.interpolationData.position.z);
/* 141 */     Vec3 vec3 = new Vec3(d2, d3, d4);
/*     */     
/* 143 */     float f1 = (float)Mth.rotLerp(d1, this.entity.getYRot(), this.interpolationData.yRot);
/* 144 */     float f2 = (float)Mth.lerp(d1, this.entity.getXRot(), this.interpolationData.xRot);
/*     */     
/* 146 */     this.entity.setPos(vec3);
/* 147 */     this.entity.setRot(f1, f2);
/* 148 */     this.interpolationData.decrease();
/* 149 */     this.previousTickPosition = vec3;
/* 150 */     this.previousTickRot = new Vec2(this.entity.getXRot(), this.entity.getYRot());
/*     */   }
/*     */   
/*     */   public void cancel() {
/* 154 */     this.interpolationData.steps = 0;
/* 155 */     this.previousTickPosition = null;
/* 156 */     this.previousTickRot = null;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\InterpolationHandler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */