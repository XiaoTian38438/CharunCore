/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class JigsawRotationFix
/*    */   extends AbstractBlockPropertyFix {
/* 10 */   private static final Map<String, String> RENAMES = (Map<String, String>)ImmutableMap.builder()
/* 11 */     .put("down", "down_south")
/* 12 */     .put("up", "up_north")
/* 13 */     .put("north", "north_up")
/* 14 */     .put("south", "south_up")
/* 15 */     .put("west", "west_up")
/* 16 */     .put("east", "east_up")
/* 17 */     .build();
/*    */   
/*    */   public JigsawRotationFix(Schema paramSchema) {
/* 20 */     super(paramSchema, "jigsaw_rotation_fix");
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldFix(String paramString) {
/* 25 */     return paramString.equals("minecraft:jigsaw");
/*    */   }
/*    */ 
/*    */   
/*    */   protected <T> Dynamic<T> fixProperties(String paramString, Dynamic<T> paramDynamic) {
/* 30 */     String str = paramDynamic.get("facing").asString("north");
/* 31 */     return paramDynamic
/* 32 */       .remove("facing")
/* 33 */       .set("orientation", paramDynamic.createString(RENAMES.getOrDefault(str, str)));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\JigsawRotationFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */