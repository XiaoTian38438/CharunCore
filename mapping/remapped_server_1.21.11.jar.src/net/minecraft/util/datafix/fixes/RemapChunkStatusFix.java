/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.function.UnaryOperator;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class RemapChunkStatusFix extends DataFix {
/*    */   private final String name;
/*    */   
/*    */   public RemapChunkStatusFix(Schema paramSchema, String paramString, UnaryOperator<String> paramUnaryOperator) {
/* 19 */     super(paramSchema, false);
/* 20 */     this.name = paramString;
/* 21 */     this.mapper = paramUnaryOperator;
/*    */   }
/*    */   private final UnaryOperator<String> mapper;
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 26 */     return fixTypeEverywhereTyped(this.name, getInputSchema().getType(References.CHUNK), paramTyped -> paramTyped.update(DSL.remainderFinder(), ()));
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
/*    */   private <T> Dynamic<T> fixStatus(Dynamic<T> paramDynamic) {
/* 38 */     Objects.requireNonNull(paramDynamic); Optional optional = paramDynamic.asString().result().map(NamespacedSchema::ensureNamespaced).map(this.mapper).map(paramDynamic::createString);
/*    */     
/* 40 */     return (Dynamic<T>)DataFixUtils.orElse(optional, paramDynamic);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\RemapChunkStatusFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */