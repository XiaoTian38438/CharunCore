/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class RecipesRenameningFix
/*    */ {
/*  8 */   public static final Map<String, String> RECIPES = (Map<String, String>)ImmutableMap.builder()
/*  9 */     .put("minecraft:acacia_bark", "minecraft:acacia_wood")
/* 10 */     .put("minecraft:birch_bark", "minecraft:birch_wood")
/* 11 */     .put("minecraft:dark_oak_bark", "minecraft:dark_oak_wood")
/* 12 */     .put("minecraft:jungle_bark", "minecraft:jungle_wood")
/* 13 */     .put("minecraft:oak_bark", "minecraft:oak_wood")
/* 14 */     .put("minecraft:spruce_bark", "minecraft:spruce_wood")
/* 15 */     .build();
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\RecipesRenameningFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */