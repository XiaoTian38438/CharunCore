/*    */ package net.minecraft.world.level.levelgen.structure.pools;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ public class JigsawJunction
/*    */ {
/*    */   private final int sourceX;
/*    */   private final int sourceGroundY;
/*    */   
/*    */   public JigsawJunction(int paramInt1, int paramInt2, int paramInt3, int paramInt4, StructureTemplatePool.Projection paramProjection) {
/* 15 */     this.sourceX = paramInt1;
/* 16 */     this.sourceGroundY = paramInt2;
/* 17 */     this.sourceZ = paramInt3;
/* 18 */     this.deltaY = paramInt4;
/* 19 */     this.destProjection = paramProjection;
/*    */   }
/*    */   private final int sourceZ; private final int deltaY; private final StructureTemplatePool.Projection destProjection;
/*    */   public int getSourceX() {
/* 23 */     return this.sourceX;
/*    */   }
/*    */   
/*    */   public int getSourceGroundY() {
/* 27 */     return this.sourceGroundY;
/*    */   }
/*    */   
/*    */   public int getSourceZ() {
/* 31 */     return this.sourceZ;
/*    */   }
/*    */   
/*    */   public int getDeltaY() {
/* 35 */     return this.deltaY;
/*    */   }
/*    */   
/*    */   public StructureTemplatePool.Projection getDestProjection() {
/* 39 */     return this.destProjection;
/*    */   }
/*    */   
/*    */   public <T> Dynamic<T> serialize(DynamicOps<T> paramDynamicOps) {
/* 43 */     ImmutableMap.Builder builder = ImmutableMap.builder();
/* 44 */     builder
/* 45 */       .put(paramDynamicOps.createString("source_x"), paramDynamicOps.createInt(this.sourceX))
/* 46 */       .put(paramDynamicOps.createString("source_ground_y"), paramDynamicOps.createInt(this.sourceGroundY))
/* 47 */       .put(paramDynamicOps.createString("source_z"), paramDynamicOps.createInt(this.sourceZ))
/* 48 */       .put(paramDynamicOps.createString("delta_y"), paramDynamicOps.createInt(this.deltaY))
/* 49 */       .put(paramDynamicOps.createString("dest_proj"), paramDynamicOps.createString(this.destProjection.getName()));
/*    */     
/* 51 */     return new Dynamic(paramDynamicOps, paramDynamicOps.createMap((Map)builder.build()));
/*    */   }
/*    */   
/*    */   public static <T> JigsawJunction deserialize(Dynamic<T> paramDynamic) {
/* 55 */     return new JigsawJunction(paramDynamic
/* 56 */         .get("source_x").asInt(0), paramDynamic
/* 57 */         .get("source_ground_y").asInt(0), paramDynamic
/* 58 */         .get("source_z").asInt(0), paramDynamic
/* 59 */         .get("delta_y").asInt(0), 
/* 60 */         StructureTemplatePool.Projection.byName(paramDynamic.get("dest_proj").asString("")));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 66 */     if (this == paramObject) {
/* 67 */       return true;
/*    */     }
/* 69 */     if (paramObject == null || getClass() != paramObject.getClass()) {
/* 70 */       return false;
/*    */     }
/*    */     
/* 73 */     JigsawJunction jigsawJunction = (JigsawJunction)paramObject;
/*    */     
/* 75 */     if (this.sourceX != jigsawJunction.sourceX) {
/* 76 */       return false;
/*    */     }
/* 78 */     if (this.sourceZ != jigsawJunction.sourceZ) {
/* 79 */       return false;
/*    */     }
/* 81 */     if (this.deltaY != jigsawJunction.deltaY) {
/* 82 */       return false;
/*    */     }
/* 84 */     return (this.destProjection == jigsawJunction.destProjection);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 89 */     int i = this.sourceX;
/* 90 */     i = 31 * i + this.sourceGroundY;
/* 91 */     i = 31 * i + this.sourceZ;
/* 92 */     i = 31 * i + this.deltaY;
/* 93 */     i = 31 * i + this.destProjection.hashCode();
/* 94 */     return i;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 99 */     return "JigsawJunction{sourceX=" + this.sourceX + ", sourceGroundY=" + this.sourceGroundY + ", sourceZ=" + this.sourceZ + ", deltaY=" + this.deltaY + ", destProjection=" + String.valueOf(this.destProjection) + "}";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\pools\JigsawJunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */