/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class ScoreboardDisplaySlotFix extends DataFix {
/*    */   public ScoreboardDisplaySlotFix(Schema paramSchema) {
/* 17 */     super(paramSchema, false);
/*    */   }
/*    */   
/* 20 */   private static final Map<String, String> SLOT_RENAMES = (Map<String, String>)ImmutableMap.builder()
/* 21 */     .put("slot_0", "list")
/* 22 */     .put("slot_1", "sidebar")
/* 23 */     .put("slot_2", "below_name")
/* 24 */     .put("slot_3", "sidebar.team.black")
/* 25 */     .put("slot_4", "sidebar.team.dark_blue")
/* 26 */     .put("slot_5", "sidebar.team.dark_green")
/* 27 */     .put("slot_6", "sidebar.team.dark_aqua")
/* 28 */     .put("slot_7", "sidebar.team.dark_red")
/* 29 */     .put("slot_8", "sidebar.team.dark_purple")
/* 30 */     .put("slot_9", "sidebar.team.gold")
/* 31 */     .put("slot_10", "sidebar.team.gray")
/* 32 */     .put("slot_11", "sidebar.team.dark_gray")
/* 33 */     .put("slot_12", "sidebar.team.blue")
/* 34 */     .put("slot_13", "sidebar.team.green")
/* 35 */     .put("slot_14", "sidebar.team.aqua")
/* 36 */     .put("slot_15", "sidebar.team.red")
/* 37 */     .put("slot_16", "sidebar.team.light_purple")
/* 38 */     .put("slot_17", "sidebar.team.yellow")
/* 39 */     .put("slot_18", "sidebar.team.white")
/* 40 */     .build();
/*    */   
/*    */   private static String rename(String paramString) {
/* 43 */     return SLOT_RENAMES.get(paramString);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 48 */     Type type = getInputSchema().getType(References.SAVED_DATA_SCOREBOARD);
/* 49 */     OpticFinder opticFinder = type.findField("data");
/*    */     
/* 51 */     return fixTypeEverywhereTyped("Scoreboard DisplaySlot rename", type, paramTyped -> paramTyped.updateTyped(paramOpticFinder, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\ScoreboardDisplaySlotFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */