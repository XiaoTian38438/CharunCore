/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public class OverreachingTickFix
/*    */   extends DataFix {
/*    */   public OverreachingTickFix(Schema paramSchema) {
/* 17 */     super(paramSchema, false);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 22 */     Type type = getInputSchema().getType(References.CHUNK);
/* 23 */     OpticFinder opticFinder = type.findField("block_ticks");
/*    */     
/* 25 */     return fixTypeEverywhereTyped("Handle ticks saved in the wrong chunk", type, paramTyped -> {
/*    */           Optional<Typed> optional = paramTyped.getOptionalTyped(paramOpticFinder);
/*    */           Optional optional1 = optional.isPresent() ? ((Typed)optional.get()).write().result() : Optional.empty();
/*    */           return paramTyped.update(DSL.remainderFinder(), ());
/*    */         });
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
/*    */   private static Dynamic<?> extractOverreachingTicks(Dynamic<?> paramDynamic, int paramInt1, int paramInt2, Optional<? extends Dynamic<?>> paramOptional, String paramString) {
/* 41 */     if (paramOptional.isPresent()) {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 51 */       List list = ((Dynamic)paramOptional.get()).asStream().filter(paramDynamic -> { int i = paramDynamic.get("x").asInt(0); int j = paramDynamic.get("z").asInt(0); int k = Math.abs(paramInt1 - (i >> 4)); int m = Math.abs(paramInt2 - (j >> 4)); return ((k != 0 || m != 0) && k <= 1 && m <= 1); }).toList();
/* 52 */       if (!list.isEmpty()) {
/* 53 */         paramDynamic = paramDynamic.set("UpgradeData", paramDynamic.get("UpgradeData").orElseEmptyMap().set(paramString, paramDynamic.createList(list.stream())));
/*    */       }
/*    */     } 
/* 56 */     return paramDynamic;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\OverreachingTickFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */