/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class EntityRavagerRenameFix
/*    */   extends SimplestEntityRenameFix {
/* 10 */   public static final Map<String, String> RENAMED_IDS = (Map<String, String>)ImmutableMap.builder()
/* 11 */     .put("minecraft:illager_beast_spawn_egg", "minecraft:ravager_spawn_egg")
/* 12 */     .build();
/*    */   
/*    */   public EntityRavagerRenameFix(Schema paramSchema, boolean paramBoolean) {
/* 15 */     super("EntityRavagerRenameFix", paramSchema, paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String rename(String paramString) {
/* 20 */     return Objects.equals("minecraft:illager_beast", paramString) ? "minecraft:ravager" : paramString;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\EntityRavagerRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */