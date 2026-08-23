/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ 
/*    */ public abstract class NamedEntityFix extends DataFix {
/*    */   private final String name;
/*    */   protected final String entityName;
/*    */   protected final DSL.TypeReference type;
/*    */   
/*    */   public NamedEntityFix(Schema paramSchema, boolean paramBoolean, String paramString1, DSL.TypeReference paramTypeReference, String paramString2) {
/* 16 */     super(paramSchema, paramBoolean);
/* 17 */     this.name = paramString1;
/* 18 */     this.type = paramTypeReference;
/* 19 */     this.entityName = paramString2;
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 24 */     OpticFinder opticFinder = DSL.namedChoice(this.entityName, getInputSchema().getChoiceType(this.type, this.entityName));
/*    */     
/* 26 */     return fixTypeEverywhereTyped(this.name, getInputSchema().getType(this.type), getOutputSchema().getType(this.type), paramTyped -> paramTyped.updateTyped(paramOpticFinder, getOutputSchema().getChoiceType(this.type, this.entityName), this::fix));
/*    */   }
/*    */   
/*    */   protected abstract Typed<?> fix(Typed<?> paramTyped);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\NamedEntityFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */