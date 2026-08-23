/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*    */ 
/*    */ 
/*    */ public abstract class NamedEntityWriteReadFix
/*    */   extends DataFix
/*    */ {
/*    */   private final String name;
/*    */   private final String entityName;
/*    */   private final DSL.TypeReference type;
/*    */   
/*    */   public NamedEntityWriteReadFix(Schema paramSchema, boolean paramBoolean, String paramString1, DSL.TypeReference paramTypeReference, String paramString2) {
/* 23 */     super(paramSchema, paramBoolean);
/* 24 */     this.name = paramString1;
/* 25 */     this.type = paramTypeReference;
/* 26 */     this.entityName = paramString2;
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 31 */     Type<?> type1 = getInputSchema().getType(this.type);
/* 32 */     Type type = getInputSchema().getChoiceType(this.type, this.entityName);
/*    */     
/* 34 */     Type<?> type2 = getOutputSchema().getType(this.type);
/*    */     
/* 36 */     OpticFinder<?> opticFinder = DSL.namedChoice(this.entityName, type);
/*    */ 
/*    */ 
/*    */     
/* 40 */     Type<?> type3 = ExtraDataFixUtils.patchSubType(type1, type1, type2);
/*    */     
/* 42 */     return fix(type1, type2, type3, opticFinder);
/*    */   }
/*    */   
/*    */   private <S, T, A> TypeRewriteRule fix(Type<S> paramType, Type<T> paramType1, Type<?> paramType2, OpticFinder<A> paramOpticFinder) {
/* 46 */     return fixTypeEverywhereTyped(this.name, paramType, paramType1, paramTyped -> {
/*    */           if (paramTyped.getOptional(paramOpticFinder).isEmpty())
/*    */             return ExtraDataFixUtils.cast(paramType1, paramTyped); 
/*    */           Typed typed = ExtraDataFixUtils.cast(paramType2, paramTyped);
/*    */           return Util.writeAndReadTypedOrThrow(typed, paramType1, this::fix);
/*    */         });
/*    */   }
/*    */   
/*    */   protected abstract <T> Dynamic<T> fix(Dynamic<T> paramDynamic);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\NamedEntityWriteReadFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */