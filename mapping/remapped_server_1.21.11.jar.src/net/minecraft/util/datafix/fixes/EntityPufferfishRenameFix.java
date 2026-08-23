/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class EntityPufferfishRenameFix
/*    */   extends SimplestEntityRenameFix {
/* 10 */   public static final Map<String, String> RENAMED_IDS = (Map<String, String>)ImmutableMap.builder()
/* 11 */     .put("minecraft:puffer_fish_spawn_egg", "minecraft:pufferfish_spawn_egg")
/* 12 */     .build();
/*    */   
/*    */   public EntityPufferfishRenameFix(Schema paramSchema, boolean paramBoolean) {
/* 15 */     super("EntityPufferfishRenameFix", paramSchema, paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String rename(String paramString) {
/* 20 */     return Objects.equals("minecraft:puffer_fish", paramString) ? "minecraft:pufferfish" : paramString;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\EntityPufferfishRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */