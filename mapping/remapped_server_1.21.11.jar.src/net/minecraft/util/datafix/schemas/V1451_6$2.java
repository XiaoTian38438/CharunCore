/*     */ package net.minecraft.util.datafix.schemas;
/*     */ 
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.types.templates.Hook;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.Optional;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class null
/*     */   implements Hook.HookFunction
/*     */ {
/*     */   public <T> T apply(DynamicOps<T> paramDynamicOps, T paramT) {
/* 133 */     Dynamic dynamic = new Dynamic(paramDynamicOps, paramT);
/*     */     
/* 135 */     Optional optional = dynamic.get("CriteriaType").get().result().flatMap(paramDynamic2 -> {
/*     */           Optional<String> optional1 = paramDynamic2.get("type").asString().result();
/*     */           
/*     */           Optional<String> optional2 = paramDynamic2.get("id").asString().result();
/*     */           
/*     */           if (optional1.isPresent() && optional2.isPresent()) {
/*     */             String str = optional1.get();
/*     */             
/*     */             return str.equals("_special") ? Optional.of(paramDynamic1.createString(optional2.get())) : Optional.of(paramDynamic2.createString(V1451_6.packNamespacedWithDot(str) + ":" + V1451_6.packNamespacedWithDot(str)));
/*     */           } 
/*     */           
/*     */           return Optional.empty();
/*     */         });
/*     */     
/* 149 */     return (T)((Dynamic)DataFixUtils.orElse(optional.map(paramDynamic2 -> paramDynamic1.set("CriteriaName", paramDynamic2).remove("CriteriaType")), dynamic)).getValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V1451_6$2.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */