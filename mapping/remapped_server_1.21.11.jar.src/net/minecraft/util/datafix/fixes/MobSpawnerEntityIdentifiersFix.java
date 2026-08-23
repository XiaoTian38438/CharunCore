/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public class MobSpawnerEntityIdentifiersFix
/*    */   extends DataFix {
/*    */   public MobSpawnerEntityIdentifiersFix(Schema paramSchema, boolean paramBoolean) {
/* 19 */     super(paramSchema, paramBoolean);
/*    */   }
/*    */   
/*    */   private Dynamic<?> fix(Dynamic<?> paramDynamic) {
/* 23 */     if (!"MobSpawner".equals(paramDynamic.get("id").asString(""))) {
/* 24 */       return paramDynamic;
/*    */     }
/*    */     
/* 27 */     Optional<String> optional = paramDynamic.get("EntityId").asString().result();
/* 28 */     if (optional.isPresent()) {
/* 29 */       Dynamic dynamic = (Dynamic)DataFixUtils.orElse(paramDynamic.get("SpawnData").result(), paramDynamic.emptyMap());
/* 30 */       dynamic = dynamic.set("id", dynamic.createString(((String)optional.get()).isEmpty() ? "Pig" : optional.get()));
/* 31 */       paramDynamic = paramDynamic.set("SpawnData", dynamic);
/*    */       
/* 33 */       paramDynamic = paramDynamic.remove("EntityId");
/*    */     } 
/*    */     
/* 36 */     Optional<Stream> optional1 = paramDynamic.get("SpawnPotentials").asStreamOpt().result();
/* 37 */     if (optional1.isPresent()) {
/* 38 */       paramDynamic = paramDynamic.set("SpawnPotentials", paramDynamic.createList(((Stream)optional1.get()).map(paramDynamic -> {
/*    */                 Optional<String> optional = paramDynamic.get("Type").asString().result();
/*    */                 
/*    */                 if (optional.isPresent()) {
/*    */                   Dynamic dynamic = ((Dynamic)DataFixUtils.orElse(paramDynamic.get("Properties").result(), paramDynamic.emptyMap())).set("id", paramDynamic.createString(optional.get()));
/*    */                   
/*    */                   return paramDynamic.set("Entity", dynamic).remove("Type").remove("Properties");
/*    */                 } 
/*    */                 
/*    */                 return paramDynamic;
/*    */               })));
/*    */     }
/*    */     
/* 51 */     return paramDynamic;
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 56 */     Type type = getOutputSchema().getType(References.UNTAGGED_SPAWNER);
/* 57 */     return fixTypeEverywhereTyped("MobSpawnerEntityIdentifiersFix", getInputSchema().getType(References.UNTAGGED_SPAWNER), type, paramTyped -> {
/*    */           Dynamic<?> dynamic = (Dynamic)paramTyped.get(DSL.remainderFinder());
/*    */           dynamic = dynamic.set("id", dynamic.createString("MobSpawner"));
/*    */           DataResult dataResult = paramType.readTyped(fix(dynamic));
/*    */           return dataResult.result().isEmpty() ? paramTyped : (Typed)((Pair)dataResult.result().get()).getFirst();
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\MobSpawnerEntityIdentifiersFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */