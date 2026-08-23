/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.templates.TaggedChoice;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Locale;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*    */ 
/*    */ public abstract class EntityRenameFix
/*    */   extends DataFix {
/*    */   protected final String name;
/*    */   
/*    */   public EntityRenameFix(String paramString, Schema paramSchema, boolean paramBoolean) {
/* 21 */     super(paramSchema, paramBoolean);
/* 22 */     this.name = paramString;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 28 */     TaggedChoice.TaggedChoiceType taggedChoiceType1 = getInputSchema().findChoiceType(References.ENTITY);
/* 29 */     TaggedChoice.TaggedChoiceType taggedChoiceType2 = getOutputSchema().findChoiceType(References.ENTITY);
/*    */     
/* 31 */     Function function = Util.memoize(paramString -> {
/*    */           Type type = (Type)paramTaggedChoiceType1.types().get(paramString);
/*    */           
/*    */           return ExtraDataFixUtils.patchSubType(type, (Type)paramTaggedChoiceType1, (Type)paramTaggedChoiceType2);
/*    */         });
/* 36 */     return fixTypeEverywhere(this.name, (Type)taggedChoiceType1, (Type)taggedChoiceType2, paramDynamicOps -> ());
/*    */   }
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
/*    */ 
/*    */ 
/*    */   
/*    */   private <A> Typed<A> getEntity(Object paramObject, DynamicOps<?> paramDynamicOps, Type<A> paramType) {
/* 53 */     return new Typed(paramType, paramDynamicOps, paramObject);
/*    */   }
/*    */   
/*    */   protected abstract Pair<String, Typed<?>> fix(String paramString, Typed<?> paramTyped);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\EntityRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */