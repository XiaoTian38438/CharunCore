/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.IntFunction;
/*    */ 
/*    */ public class EntityVariantFix
/*    */   extends NamedEntityFix {
/*    */   private final String fieldName;
/*    */   private final IntFunction<String> idConversions;
/*    */   
/*    */   public EntityVariantFix(Schema paramSchema, String paramString1, DSL.TypeReference paramTypeReference, String paramString2, String paramString3, IntFunction<String> paramIntFunction) {
/* 18 */     super(paramSchema, false, paramString1, paramTypeReference, paramString2);
/* 19 */     this.fieldName = paramString3;
/* 20 */     this.idConversions = paramIntFunction;
/*    */   }
/*    */   
/*    */   private static <T> Dynamic<T> updateAndRename(Dynamic<T> paramDynamic, String paramString1, String paramString2, Function<Dynamic<T>, Dynamic<T>> paramFunction) {
/* 24 */     return paramDynamic.map(paramObject -> {
/*    */           DynamicOps dynamicOps = paramDynamic.getOps();
/*    */           Function function = ();
/*    */           return dynamicOps.get(paramObject, paramString1).map(()).result().orElse(paramObject);
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> paramTyped) {
/* 36 */     return paramTyped.update(DSL.remainderFinder(), paramDynamic -> updateAndRename(paramDynamic, this.fieldName, "variant", ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\EntityVariantFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */