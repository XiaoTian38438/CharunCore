/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ 
/*    */ public class BlockStateStructureTemplateFix extends DataFix {
/*    */   public BlockStateStructureTemplateFix(Schema paramSchema, boolean paramBoolean) {
/* 10 */     super(paramSchema, paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 15 */     return fixTypeEverywhereTyped("BlockStateStructureTemplateFix", getInputSchema().getType(References.BLOCK_STATE), paramTyped -> paramTyped.update(DSL.remainderFinder(), BlockStateData::upgradeBlockStateTag));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\BlockStateStructureTemplateFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */