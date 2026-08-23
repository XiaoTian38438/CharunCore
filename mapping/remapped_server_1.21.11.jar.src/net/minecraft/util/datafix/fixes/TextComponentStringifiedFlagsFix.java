/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public class TextComponentStringifiedFlagsFix
/*    */   extends DataFix {
/*    */   public TextComponentStringifiedFlagsFix(Schema paramSchema) {
/* 17 */     super(paramSchema, false);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 23 */     Type type = getInputSchema().getType(References.TEXT_COMPONENT);
/* 24 */     return fixTypeEverywhere("TextComponentStringyFlagsFix", type, paramDynamicOps -> ());
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
/*    */   private static <T> Dynamic<T> stringToBool(Dynamic<T> paramDynamic) {
/* 36 */     Optional<String> optional = paramDynamic.asString().result();
/* 37 */     if (optional.isPresent()) {
/* 38 */       return paramDynamic.createBoolean(Boolean.parseBoolean(optional.get()));
/*    */     }
/* 40 */     return paramDynamic;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\TextComponentStringifiedFlagsFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */