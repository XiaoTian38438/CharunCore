/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.EnumMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ public class EntityAttachments
/*    */ {
/*    */   private final Map<EntityAttachment, List<Vec3>> attachments;
/*    */   
/*    */   EntityAttachments(Map<EntityAttachment, List<Vec3>> paramMap) {
/* 17 */     this.attachments = paramMap;
/*    */   }
/*    */   
/*    */   public static EntityAttachments createDefault(float paramFloat1, float paramFloat2) {
/* 21 */     return builder().build(paramFloat1, paramFloat2);
/*    */   }
/*    */   
/*    */   public static Builder builder() {
/* 25 */     return new Builder();
/*    */   }
/*    */   
/*    */   public EntityAttachments scale(float paramFloat1, float paramFloat2, float paramFloat3) {
/* 29 */     return new EntityAttachments(Util.makeEnumMap(EntityAttachment.class, paramEntityAttachment -> {
/*    */             ArrayList<Vec3> arrayList = new ArrayList();
/*    */             for (Vec3 vec3 : this.attachments.get(paramEntityAttachment)) {
/*    */               arrayList.add(vec3.multiply(paramFloat1, paramFloat2, paramFloat3));
/*    */             }
/*    */             return arrayList;
/*    */           }));
/*    */   }
/*    */   
/*    */   public Vec3 getNullable(EntityAttachment paramEntityAttachment, int paramInt, float paramFloat) {
/* 39 */     List<Vec3> list = this.attachments.get(paramEntityAttachment);
/* 40 */     if (paramInt < 0 || paramInt >= list.size()) {
/* 41 */       return null;
/*    */     }
/* 43 */     return transformPoint(list.get(paramInt), paramFloat);
/*    */   }
/*    */   
/*    */   public Vec3 get(EntityAttachment paramEntityAttachment, int paramInt, float paramFloat) {
/* 47 */     Vec3 vec3 = getNullable(paramEntityAttachment, paramInt, paramFloat);
/* 48 */     if (vec3 == null) {
/* 49 */       throw new IllegalStateException("Had no attachment point of type: " + String.valueOf(paramEntityAttachment) + " for index: " + paramInt);
/*    */     }
/* 51 */     return vec3;
/*    */   }
/*    */   
/*    */   public Vec3 getAverage(EntityAttachment paramEntityAttachment) {
/* 55 */     List list = this.attachments.get(paramEntityAttachment);
/* 56 */     if (list == null || list.isEmpty()) {
/* 57 */       throw new IllegalStateException("No attachment points of type: PASSENGER");
/*    */     }
/* 59 */     Vec3 vec3 = Vec3.ZERO;
/* 60 */     for (Vec3 vec31 : list) {
/* 61 */       vec3 = vec3.add(vec31);
/*    */     }
/* 63 */     return vec3.scale((1.0F / list.size()));
/*    */   }
/*    */   
/*    */   public Vec3 getClamped(EntityAttachment paramEntityAttachment, int paramInt, float paramFloat) {
/* 67 */     List<Vec3> list = this.attachments.get(paramEntityAttachment);
/* 68 */     if (list.isEmpty()) {
/* 69 */       throw new IllegalStateException("Had no attachment points of type: " + String.valueOf(paramEntityAttachment));
/*    */     }
/* 71 */     Vec3 vec3 = list.get(Mth.clamp(paramInt, 0, list.size() - 1));
/* 72 */     return transformPoint(vec3, paramFloat);
/*    */   }
/*    */   
/*    */   private static Vec3 transformPoint(Vec3 paramVec3, float paramFloat) {
/* 76 */     return paramVec3.yRot(-paramFloat * 0.017453292F);
/*    */   }
/*    */   
/*    */   public static class Builder {
/* 80 */     private final Map<EntityAttachment, List<Vec3>> attachments = new EnumMap<>(EntityAttachment.class);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public Builder attach(EntityAttachment param1EntityAttachment, float param1Float1, float param1Float2, float param1Float3) {
/* 86 */       return attach(param1EntityAttachment, new Vec3(param1Float1, param1Float2, param1Float3));
/*    */     }
/*    */     
/*    */     public Builder attach(EntityAttachment param1EntityAttachment, Vec3 param1Vec3) {
/* 90 */       ((List<Vec3>)this.attachments.computeIfAbsent(param1EntityAttachment, param1EntityAttachment -> new ArrayList(1))).add(param1Vec3);
/* 91 */       return this;
/*    */     }
/*    */     
/*    */     public EntityAttachments build(float param1Float1, float param1Float2) {
/* 95 */       Map<EntityAttachment, List<Vec3>> map = Util.makeEnumMap(EntityAttachment.class, param1EntityAttachment -> {
/*    */             List<?> list = this.attachments.get(param1EntityAttachment);
/*    */             return (list == null) ? param1EntityAttachment.createFallbackPoints(param1Float1, param1Float2) : List.copyOf(list);
/*    */           });
/* 99 */       return new EntityAttachments(map);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\EntityAttachments.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */