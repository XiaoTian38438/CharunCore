/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.templates.TaggedChoice;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public class AddNewChoices extends DataFix {
/*    */   private final String name;
/*    */   
/*    */   public AddNewChoices(Schema paramSchema, String paramString, DSL.TypeReference paramTypeReference) {
/* 16 */     super(paramSchema, true);
/* 17 */     this.name = paramString;
/* 18 */     this.type = paramTypeReference;
/*    */   }
/*    */   private final DSL.TypeReference type;
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 23 */     TaggedChoice.TaggedChoiceType<?> taggedChoiceType1 = getInputSchema().findChoiceType(this.type);
/* 24 */     TaggedChoice.TaggedChoiceType<?> taggedChoiceType2 = getOutputSchema().findChoiceType(this.type);
/* 25 */     return cap(taggedChoiceType1, taggedChoiceType2);
/*    */   }
/*    */ 
/*    */   
/*    */   private <K> TypeRewriteRule cap(TaggedChoice.TaggedChoiceType<K> paramTaggedChoiceType, TaggedChoice.TaggedChoiceType<?> paramTaggedChoiceType1) {
/* 30 */     if (paramTaggedChoiceType.getKeyType() != paramTaggedChoiceType1.getKeyType()) {
/* 31 */       throw new IllegalStateException("Could not inject: key type is not the same");
/*    */     }
/* 33 */     TaggedChoice.TaggedChoiceType<?> taggedChoiceType = paramTaggedChoiceType1;
/* 34 */     return fixTypeEverywhere(this.name, (Type)paramTaggedChoiceType, (Type)taggedChoiceType, paramDynamicOps -> ());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\AddNewChoices.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */