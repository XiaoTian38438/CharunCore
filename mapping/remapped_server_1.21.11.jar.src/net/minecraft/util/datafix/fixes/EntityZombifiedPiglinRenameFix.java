/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class EntityZombifiedPiglinRenameFix
/*    */   extends SimplestEntityRenameFix {
/* 10 */   public static final Map<String, String> RENAMED_IDS = (Map<String, String>)ImmutableMap.builder()
/* 11 */     .put("minecraft:zombie_pigman_spawn_egg", "minecraft:zombified_piglin_spawn_egg")
/* 12 */     .build();
/*    */   
/*    */   public EntityZombifiedPiglinRenameFix(Schema paramSchema) {
/* 15 */     super("EntityZombifiedPiglinRenameFix", paramSchema, true);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String rename(String paramString) {
/* 20 */     return Objects.equals("minecraft:zombie_pigman", paramString) ? "minecraft:zombified_piglin" : paramString;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\EntityZombifiedPiglinRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */