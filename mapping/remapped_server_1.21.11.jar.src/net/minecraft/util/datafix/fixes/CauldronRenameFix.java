/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public class CauldronRenameFix extends DataFix {
/*    */   public CauldronRenameFix(Schema paramSchema, boolean paramBoolean) {
/* 13 */     super(paramSchema, paramBoolean);
/*    */   }
/*    */   
/*    */   private static Dynamic<?> fix(Dynamic<?> paramDynamic) {
/* 17 */     Optional optional = paramDynamic.get("Name").asString().result();
/* 18 */     if (optional.equals(Optional.of("minecraft:cauldron"))) {
/* 19 */       Dynamic dynamic = paramDynamic.get("Properties").orElseEmptyMap();
/* 20 */       if (dynamic.get("level").asString("0").equals("0")) {
/* 21 */         return paramDynamic.remove("Properties");
/*    */       }
/* 23 */       return paramDynamic.set("Name", paramDynamic.createString("minecraft:water_cauldron"));
/*    */     } 
/* 25 */     return paramDynamic;
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 30 */     return fixTypeEverywhereTyped("cauldron_rename_fix", getInputSchema().getType(References.BLOCK_STATE), paramTyped -> paramTyped.update(DSL.remainderFinder(), CauldronRenameFix::fix));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\CauldronRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */