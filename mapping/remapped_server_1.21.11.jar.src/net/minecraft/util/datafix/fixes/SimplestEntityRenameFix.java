/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.templates.TaggedChoice;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Locale;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public abstract class SimplestEntityRenameFix extends DataFix {
/*    */   private final String name;
/*    */   
/*    */   public SimplestEntityRenameFix(String paramString, Schema paramSchema, boolean paramBoolean) {
/* 20 */     super(paramSchema, paramBoolean);
/* 21 */     this.name = paramString;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 27 */     TaggedChoice.TaggedChoiceType taggedChoiceType1 = getInputSchema().findChoiceType(References.ENTITY);
/* 28 */     TaggedChoice.TaggedChoiceType taggedChoiceType2 = getOutputSchema().findChoiceType(References.ENTITY);
/*    */     
/* 30 */     Type type = DSL.named(References.ENTITY_NAME.typeName(), NamespacedSchema.namespacedString());
/* 31 */     if (!Objects.equals(getOutputSchema().getType(References.ENTITY_NAME), type)) {
/* 32 */       throw new IllegalStateException("Entity name type is not what was expected.");
/*    */     }
/*    */     
/* 35 */     return TypeRewriteRule.seq(
/* 36 */         fixTypeEverywhere(this.name, (Type)taggedChoiceType1, (Type)taggedChoiceType2, paramDynamicOps -> ()), 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 48 */         fixTypeEverywhere(this.name + " for entity name", type, paramDynamicOps -> ()));
/*    */   }
/*    */   
/*    */   protected abstract String rename(String paramString);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\SimplestEntityRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */