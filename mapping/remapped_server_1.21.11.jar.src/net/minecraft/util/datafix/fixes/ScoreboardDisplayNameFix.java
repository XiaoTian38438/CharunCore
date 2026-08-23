/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import net.minecraft.util.datafix.LegacyComponentDataFixUtils;
/*    */ 
/*    */ public class ScoreboardDisplayNameFix extends DataFix {
/*    */   private final String name;
/*    */   
/*    */   public ScoreboardDisplayNameFix(Schema paramSchema, String paramString, DSL.TypeReference paramTypeReference) {
/* 17 */     super(paramSchema, false);
/* 18 */     this.name = paramString;
/* 19 */     this.type = paramTypeReference;
/*    */   }
/*    */   private final DSL.TypeReference type;
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 24 */     Type type = getInputSchema().getType(this.type);
/* 25 */     OpticFinder opticFinder1 = type.findField("DisplayName");
/*    */     
/* 27 */     OpticFinder opticFinder2 = DSL.typeFinder(getInputSchema().getType(References.TEXT_COMPONENT));
/* 28 */     return fixTypeEverywhereTyped(this.name, type, paramTyped -> paramTyped.updateTyped(paramOpticFinder1, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\ScoreboardDisplayNameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */