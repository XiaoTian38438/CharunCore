/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.OptionalDynamic;
/*    */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*    */ 
/*    */ public class LegacyDragonFightFix extends DataFix {
/*    */   public LegacyDragonFightFix(Schema paramSchema) {
/* 13 */     super(paramSchema, false);
/*    */   }
/*    */   
/*    */   private static <T> Dynamic<T> fixDragonFight(Dynamic<T> paramDynamic) {
/* 17 */     return paramDynamic.update("ExitPortalLocation", ExtraDataFixUtils::fixBlockPos);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 22 */     return fixTypeEverywhereTyped("LegacyDragonFightFix", getInputSchema().getType(References.LEVEL), paramTyped -> paramTyped.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\LegacyDragonFightFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */