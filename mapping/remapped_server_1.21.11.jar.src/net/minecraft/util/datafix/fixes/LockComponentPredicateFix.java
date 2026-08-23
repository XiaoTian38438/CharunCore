/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.escape.Escaper;
/*    */ import com.google.common.escape.Escapers;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public class LockComponentPredicateFix
/*    */   extends DataComponentRemainderFix
/*    */ {
/* 12 */   public static final Escaper ESCAPER = Escapers.builder()
/* 13 */     .addEscape('"', "\\\"")
/* 14 */     .addEscape('\\', "\\\\")
/* 15 */     .build();
/*    */   
/*    */   public LockComponentPredicateFix(Schema paramSchema) {
/* 18 */     super(paramSchema, "LockComponentPredicateFix", "minecraft:lock");
/*    */   }
/*    */ 
/*    */   
/*    */   protected <T> Dynamic<T> fixComponent(Dynamic<T> paramDynamic) {
/* 23 */     return fixLock(paramDynamic);
/*    */   }
/*    */   
/*    */   public static <T> Dynamic<T> fixLock(Dynamic<T> paramDynamic) {
/* 27 */     Optional<String> optional = paramDynamic.asString().result();
/* 28 */     if (optional.isEmpty()) {
/* 29 */       return null;
/*    */     }
/*    */     
/* 32 */     if (((String)optional.get()).isEmpty()) {
/* 33 */       return null;
/*    */     }
/* 35 */     Dynamic dynamic1 = paramDynamic.createString("\"" + ESCAPER.escape(optional.get()) + "\"");
/* 36 */     Dynamic dynamic2 = paramDynamic.emptyMap().set("minecraft:custom_name", dynamic1);
/* 37 */     return paramDynamic.emptyMap().set("components", dynamic2);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\LockComponentPredicateFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */