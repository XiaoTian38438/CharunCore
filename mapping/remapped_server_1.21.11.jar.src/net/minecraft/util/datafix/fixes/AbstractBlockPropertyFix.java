/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public abstract class AbstractBlockPropertyFix extends DataFix {
/*    */   private final String name;
/*    */   
/*    */   public AbstractBlockPropertyFix(Schema paramSchema, String paramString) {
/* 16 */     super(paramSchema, false);
/* 17 */     this.name = paramString;
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 22 */     return fixTypeEverywhereTyped(this.name, getInputSchema().getType(References.BLOCK_STATE), paramTyped -> paramTyped.update(DSL.remainderFinder(), this::fixBlockState));
/*    */   }
/*    */   
/*    */   private Dynamic<?> fixBlockState(Dynamic<?> paramDynamic) {
/* 26 */     Optional<String> optional = paramDynamic.get("Name").asString().result().map(NamespacedSchema::ensureNamespaced);
/* 27 */     if (optional.isPresent() && shouldFix(optional.get())) {
/* 28 */       return paramDynamic.update("Properties", paramDynamic -> fixProperties(paramOptional.get(), paramDynamic));
/*    */     }
/* 30 */     return paramDynamic;
/*    */   }
/*    */   
/*    */   protected abstract boolean shouldFix(String paramString);
/*    */   
/*    */   protected abstract <T> Dynamic<T> fixProperties(String paramString, Dynamic<T> paramDynamic);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\AbstractBlockPropertyFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */