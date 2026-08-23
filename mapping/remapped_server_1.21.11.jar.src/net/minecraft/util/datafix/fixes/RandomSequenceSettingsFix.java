/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class RandomSequenceSettingsFix extends DataFix {
/*    */   public RandomSequenceSettingsFix(Schema paramSchema) {
/* 10 */     super(paramSchema, false);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 15 */     return fixTypeEverywhereTyped("RandomSequenceSettingsFix", getInputSchema().getType(References.SAVED_DATA_RANDOM_SEQUENCES), paramTyped -> paramTyped.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\RandomSequenceSettingsFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */