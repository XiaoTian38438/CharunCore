/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Predicate;
/*    */ 
/*    */ 
/*    */ public class InvalidLockComponentFix
/*    */   extends DataComponentRemainderFix
/*    */ {
/* 13 */   private static final Optional<String> INVALID_LOCK_CUSTOM_NAME = Optional.of("\"\"");
/*    */   
/*    */   public InvalidLockComponentFix(Schema paramSchema) {
/* 16 */     super(paramSchema, "InvalidLockComponentPredicateFix", "minecraft:lock");
/*    */   }
/*    */ 
/*    */   
/*    */   protected <T> Dynamic<T> fixComponent(Dynamic<T> paramDynamic) {
/* 21 */     return fixLock(paramDynamic);
/*    */   }
/*    */   
/*    */   public static <T> Dynamic<T> fixLock(Dynamic<T> paramDynamic) {
/* 25 */     return isBrokenLock(paramDynamic) ? null : paramDynamic;
/*    */   }
/*    */   
/*    */   private static <T> boolean isBrokenLock(Dynamic<T> paramDynamic) {
/* 29 */     return isMapWithOneField(paramDynamic, "components", paramDynamic -> isMapWithOneField(paramDynamic, "minecraft:custom_name", ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static <T> boolean isMapWithOneField(Dynamic<T> paramDynamic, String paramString, Predicate<Dynamic<T>> paramPredicate) {
/* 37 */     Optional<Map> optional = paramDynamic.getMapValues().result();
/* 38 */     if (optional.isEmpty() || ((Map)optional.get()).size() != 1) {
/* 39 */       return false;
/*    */     }
/* 41 */     return paramDynamic.get(paramString).result().filter(paramPredicate).isPresent();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\InvalidLockComponentFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */