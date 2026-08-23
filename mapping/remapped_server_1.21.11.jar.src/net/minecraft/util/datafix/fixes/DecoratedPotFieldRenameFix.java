/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ 
/*    */ public class DecoratedPotFieldRenameFix
/*    */   extends DataFix {
/*    */   private static final String DECORATED_POT_ID = "minecraft:decorated_pot";
/*    */   
/*    */   public DecoratedPotFieldRenameFix(Schema paramSchema) {
/* 13 */     super(paramSchema, true);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 18 */     Type type1 = getInputSchema().getChoiceType(References.BLOCK_ENTITY, "minecraft:decorated_pot");
/* 19 */     Type type2 = getOutputSchema().getChoiceType(References.BLOCK_ENTITY, "minecraft:decorated_pot");
/*    */     
/* 21 */     return convertUnchecked("DecoratedPotFieldRenameFix", type1, type2);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\DecoratedPotFieldRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */