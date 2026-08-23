/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class EntityCodSalmonFix
/*    */   extends SimplestEntityRenameFix {
/*  9 */   public static final Map<String, String> RENAMED_IDS = (Map<String, String>)ImmutableMap.builder()
/* 10 */     .put("minecraft:salmon_mob", "minecraft:salmon")
/* 11 */     .put("minecraft:cod_mob", "minecraft:cod")
/* 12 */     .build();
/*    */   
/* 14 */   public static final Map<String, String> RENAMED_EGG_IDS = (Map<String, String>)ImmutableMap.builder()
/* 15 */     .put("minecraft:salmon_mob_spawn_egg", "minecraft:salmon_spawn_egg")
/* 16 */     .put("minecraft:cod_mob_spawn_egg", "minecraft:cod_spawn_egg")
/* 17 */     .build();
/*    */   
/*    */   public EntityCodSalmonFix(Schema paramSchema, boolean paramBoolean) {
/* 20 */     super("EntityCodSalmonFix", paramSchema, paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String rename(String paramString) {
/* 25 */     return RENAMED_IDS.getOrDefault(paramString, paramString);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\EntityCodSalmonFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */