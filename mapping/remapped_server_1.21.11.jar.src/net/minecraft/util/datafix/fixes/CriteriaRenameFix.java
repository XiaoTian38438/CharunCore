/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.function.UnaryOperator;
/*    */ 
/*    */ public class CriteriaRenameFix
/*    */   extends DataFix {
/*    */   private final String name;
/*    */   
/*    */   public CriteriaRenameFix(Schema paramSchema, String paramString1, String paramString2, UnaryOperator<String> paramUnaryOperator) {
/* 18 */     super(paramSchema, false);
/* 19 */     this.name = paramString1;
/* 20 */     this.advancementId = paramString2;
/* 21 */     this.conversions = paramUnaryOperator;
/*    */   }
/*    */   private final String advancementId; private final UnaryOperator<String> conversions;
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 26 */     return fixTypeEverywhereTyped(this.name, getInputSchema().getType(References.ADVANCEMENTS), paramTyped -> paramTyped.update(DSL.remainderFinder(), this::fixAdvancements));
/*    */   }
/*    */   
/*    */   private Dynamic<?> fixAdvancements(Dynamic<?> paramDynamic) {
/* 30 */     return paramDynamic.update(this.advancementId, paramDynamic -> paramDynamic.update("criteria", ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\CriteriaRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */