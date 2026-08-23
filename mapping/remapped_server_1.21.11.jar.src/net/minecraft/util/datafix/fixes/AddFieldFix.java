/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Locale;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AddFieldFix
/*    */   extends DataFix
/*    */ {
/*    */   private final String name;
/*    */   private final DSL.TypeReference type;
/*    */   private final String fieldName;
/*    */   private final String[] path;
/*    */   private final Function<Dynamic<?>, Dynamic<?>> fieldGenerator;
/*    */   
/*    */   public AddFieldFix(Schema paramSchema, DSL.TypeReference paramTypeReference, String paramString, Function<Dynamic<?>, Dynamic<?>> paramFunction, String... paramVarArgs) {
/* 28 */     super(paramSchema, false);
/* 29 */     this.name = "Adding field `" + paramString + "` to type `" + paramTypeReference.typeName().toLowerCase(Locale.ROOT) + "`";
/* 30 */     this.type = paramTypeReference;
/* 31 */     this.fieldName = paramString;
/* 32 */     this.path = paramVarArgs;
/* 33 */     this.fieldGenerator = paramFunction;
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 38 */     return fixTypeEverywhereTyped(this.name, getInputSchema().getType(this.type), getOutputSchema().getType(this.type), paramTyped -> paramTyped.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ 
/*    */   
/*    */   private Dynamic<?> addField(Dynamic<?> paramDynamic, int paramInt) {
/* 43 */     if (paramInt >= this.path.length) {
/* 44 */       return paramDynamic.set(this.fieldName, this.fieldGenerator.apply(paramDynamic));
/*    */     }
/*    */     
/* 47 */     Optional<Dynamic> optional = paramDynamic.get(this.path[paramInt]).result();
/* 48 */     if (optional.isEmpty()) {
/* 49 */       return paramDynamic;
/*    */     }
/*    */     
/* 52 */     return addField(optional.get(), paramInt + 1);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\AddFieldFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */