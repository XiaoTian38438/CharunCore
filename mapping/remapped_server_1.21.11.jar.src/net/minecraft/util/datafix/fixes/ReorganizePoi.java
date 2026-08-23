/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public class ReorganizePoi extends DataFix {
/*    */   public ReorganizePoi(Schema paramSchema, boolean paramBoolean) {
/* 21 */     super(paramSchema, paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 26 */     Type type = DSL.named(References.POI_CHUNK.typeName(), DSL.remainderType());
/*    */     
/* 28 */     if (!Objects.equals(type, getInputSchema().getType(References.POI_CHUNK))) {
/* 29 */       throw new IllegalStateException("Poi type is not what was expected.");
/*    */     }
/* 31 */     return fixTypeEverywhere("POI reorganization", type, paramDynamicOps -> ());
/*    */   }
/*    */   
/*    */   private static <T> Dynamic<T> cap(Dynamic<T> paramDynamic) {
/* 35 */     HashMap<Dynamic, Dynamic> hashMap = Maps.newHashMap();
/* 36 */     for (byte b = 0; b < 16; b++) {
/* 37 */       String str = String.valueOf(b);
/* 38 */       Optional<Dynamic> optional = paramDynamic.get(str).result();
/* 39 */       if (optional.isPresent()) {
/* 40 */         Dynamic dynamic1 = optional.get();
/* 41 */         Dynamic dynamic2 = paramDynamic.createMap((Map)ImmutableMap.of(paramDynamic.createString("Records"), dynamic1));
/* 42 */         hashMap.put(paramDynamic.createString(Integer.toString(b)), dynamic2);
/* 43 */         paramDynamic = paramDynamic.remove(str);
/*    */       } 
/*    */     } 
/*    */     
/* 47 */     return paramDynamic.set("Sections", paramDynamic.createMap(hashMap));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\ReorganizePoi.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */