/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.templates.TaggedChoice;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.UnaryOperator;
/*    */ 
/*    */ public class BlockEntityRenameFix extends DataFix {
/*    */   private final String name;
/*    */   
/*    */   private BlockEntityRenameFix(Schema paramSchema, String paramString, UnaryOperator<String> paramUnaryOperator) {
/* 16 */     super(paramSchema, true);
/* 17 */     this.name = paramString;
/* 18 */     this.nameChangeLookup = paramUnaryOperator;
/*    */   }
/*    */   private final UnaryOperator<String> nameChangeLookup;
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 23 */     TaggedChoice.TaggedChoiceType taggedChoiceType1 = getInputSchema().findChoiceType(References.BLOCK_ENTITY);
/* 24 */     TaggedChoice.TaggedChoiceType taggedChoiceType2 = getOutputSchema().findChoiceType(References.BLOCK_ENTITY);
/*    */     
/* 26 */     return fixTypeEverywhere(this.name, (Type)taggedChoiceType1, (Type)taggedChoiceType2, paramDynamicOps -> ());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static DataFix create(Schema paramSchema, String paramString, UnaryOperator<String> paramUnaryOperator) {
/* 32 */     return new BlockEntityRenameFix(paramSchema, paramString, paramUnaryOperator);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\BlockEntityRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */