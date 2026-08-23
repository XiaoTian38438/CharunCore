/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class BlockNameFlatteningFix
/*    */   extends DataFix {
/*    */   public BlockNameFlatteningFix(Schema paramSchema, boolean paramBoolean) {
/* 18 */     super(paramSchema, paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 23 */     Type type1 = getInputSchema().getType(References.BLOCK_NAME);
/* 24 */     Type type2 = getOutputSchema().getType(References.BLOCK_NAME);
/*    */     
/* 26 */     Type type3 = DSL.named(References.BLOCK_NAME.typeName(), DSL.or(DSL.intType(), NamespacedSchema.namespacedString()));
/* 27 */     Type type4 = DSL.named(References.BLOCK_NAME.typeName(), NamespacedSchema.namespacedString());
/*    */     
/* 29 */     if (!Objects.equals(type1, type3) || !Objects.equals(type2, type4)) {
/* 30 */       throw new IllegalStateException("Expected and actual types don't match.");
/*    */     }
/* 32 */     return fixTypeEverywhere("BlockNameFlatteningFix", type3, type4, paramDynamicOps -> ());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\BlockNameFlatteningFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */