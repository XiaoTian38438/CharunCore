/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.EnumMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.phys.Vec3;
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
/*    */ public class Builder
/*    */ {
/* 80 */   private final Map<EntityAttachment, List<Vec3>> attachments = new EnumMap<>(EntityAttachment.class);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Builder attach(EntityAttachment paramEntityAttachment, float paramFloat1, float paramFloat2, float paramFloat3) {
/* 86 */     return attach(paramEntityAttachment, new Vec3(paramFloat1, paramFloat2, paramFloat3));
/*    */   }
/*    */   
/*    */   public Builder attach(EntityAttachment paramEntityAttachment, Vec3 paramVec3) {
/* 90 */     ((List<Vec3>)this.attachments.computeIfAbsent(paramEntityAttachment, paramEntityAttachment -> new ArrayList(1))).add(paramVec3);
/* 91 */     return this;
/*    */   }
/*    */   
/*    */   public EntityAttachments build(float paramFloat1, float paramFloat2) {
/* 95 */     Map<EntityAttachment, List<Vec3>> map = Util.makeEnumMap(EntityAttachment.class, paramEntityAttachment -> {
/*    */           List<?> list = this.attachments.get(paramEntityAttachment);
/*    */           return (list == null) ? paramEntityAttachment.createFallbackPoints(paramFloat1, paramFloat2) : List.copyOf(list);
/*    */         });
/* 99 */     return new EntityAttachments(map);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\EntityAttachments$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */