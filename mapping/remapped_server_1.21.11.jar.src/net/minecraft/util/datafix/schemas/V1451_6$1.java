/*     */ package net.minecraft.util.datafix.schemas;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.types.templates.Hook;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.Map;
/*     */ import net.minecraft.resources.Identifier;
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
/*  95 */     Dynamic dynamic = new Dynamic(paramDynamicOps, paramT);
/*     */     
/*  97 */     return (T)((Dynamic)DataFixUtils.orElse(dynamic
/*  98 */         .get("CriteriaName").asString().result()
/*  99 */         .map(paramString -> {
/*     */             int i = paramString.indexOf(':');
/*     */             if (i < 0) {
/*     */               return Pair.of("_special", paramString);
/*     */             }
/*     */             try {
/*     */               Identifier identifier1 = Identifier.bySeparator(paramString.substring(0, i), '.');
/*     */               Identifier identifier2 = Identifier.bySeparator(paramString.substring(i + 1), '.');
/*     */               return Pair.of(identifier1.toString(), identifier2.toString());
/* 108 */             } catch (Exception exception) {
/*     */               
/*     */               return Pair.of("_special", paramString);
/*     */             } 
/* 112 */           }).map(paramPair -> paramDynamic.set("CriteriaType", paramDynamic.createMap((Map)ImmutableMap.of(paramDynamic.createString("type"), paramDynamic.createString((String)paramPair.getFirst()), paramDynamic.createString("id"), paramDynamic.createString((String)paramPair.getSecond()))))), dynamic))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 121 */       .getValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V1451_6$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */