/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public class LevelLegacyWorldGenSettingsFix
/*    */   extends DataFix {
/*    */   private static final String WORLD_GEN_SETTINGS = "WorldGenSettings";
/* 15 */   private static final List<String> OLD_SETTINGS_KEYS = List.of("RandomSeed", "generatorName", "generatorOptions", "generatorVersion", "legacy_custom_options", "MapFeatures", "BonusChest");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public LevelLegacyWorldGenSettingsFix(Schema paramSchema) {
/* 26 */     super(paramSchema, false);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 31 */     return fixTypeEverywhereTyped("LevelLegacyWorldGenSettingsFix", getInputSchema().getType(References.LEVEL), paramTyped -> paramTyped.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\LevelLegacyWorldGenSettingsFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */