/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.templates.TaggedChoice;
/*    */ import java.util.Map;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class StatsRenameFix
/*    */   extends DataFix {
/*    */   private final String name;
/*    */   private final Map<String, String> renames;
/*    */   
/*    */   public StatsRenameFix(Schema paramSchema, String paramString, Map<String, String> paramMap) {
/* 20 */     super(paramSchema, false);
/* 21 */     this.name = paramString;
/* 22 */     this.renames = paramMap;
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 27 */     return TypeRewriteRule.seq(createStatRule(), createCriteriaRule());
/*    */   }
/*    */   
/*    */   private TypeRewriteRule createCriteriaRule() {
/* 31 */     Type type1 = getOutputSchema().getType(References.OBJECTIVE);
/* 32 */     Type type2 = getInputSchema().getType(References.OBJECTIVE);
/*    */     
/* 34 */     OpticFinder opticFinder1 = type2.findField("CriteriaType");
/* 35 */     TaggedChoice.TaggedChoiceType taggedChoiceType = (TaggedChoice.TaggedChoiceType)opticFinder1.type().findChoiceType("type", -1).orElseThrow(() -> new IllegalStateException("Can't find choice type for criteria"));
/* 36 */     Type type3 = (Type)taggedChoiceType.types().get("minecraft:custom");
/* 37 */     if (type3 == null) {
/* 38 */       throw new IllegalStateException("Failed to find custom criterion type variant");
/*    */     }
/*    */     
/* 41 */     OpticFinder opticFinder2 = DSL.namedChoice("minecraft:custom", type3);
/* 42 */     OpticFinder opticFinder3 = DSL.fieldFinder("id", NamespacedSchema.namespacedString());
/*    */     
/* 44 */     return fixTypeEverywhereTyped(this.name, type2, type1, paramTyped -> paramTyped.updateTyped(paramOpticFinder1, ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private TypeRewriteRule createStatRule() {
/* 54 */     Type type1 = getOutputSchema().getType(References.STATS);
/* 55 */     Type type2 = getInputSchema().getType(References.STATS);
/* 56 */     OpticFinder opticFinder1 = type2.findField("stats");
/* 57 */     OpticFinder opticFinder2 = opticFinder1.type().findField("minecraft:custom");
/* 58 */     OpticFinder opticFinder3 = NamespacedSchema.namespacedString().finder();
/* 59 */     return fixTypeEverywhereTyped(this.name, type2, type1, paramTyped -> paramTyped.updateTyped(paramOpticFinder1, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\StatsRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */