/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.MapLike;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public class ChunkRenamesFix
/*    */   extends DataFix
/*    */ {
/*    */   public ChunkRenamesFix(Schema paramSchema) {
/* 21 */     super(paramSchema, true);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 26 */     Type type1 = getInputSchema().getType(References.CHUNK);
/* 27 */     OpticFinder opticFinder1 = type1.findField("Level");
/* 28 */     OpticFinder opticFinder2 = opticFinder1.type().findField("Structures");
/*    */     
/* 30 */     Type type2 = getOutputSchema().getType(References.CHUNK);
/* 31 */     Type type3 = type2.findFieldType("structures");
/*    */     
/* 33 */     return fixTypeEverywhereTyped("Chunk Renames; purge Level-tag", type1, type2, paramTyped -> {
/*    */           Typed<?> typed = paramTyped.getTyped(paramOpticFinder1);
/*    */           Typed<Pair<String, ?>> typed1 = appendChunkName(typed);
/*    */           typed1 = typed1.set(DSL.remainderFinder(), mergeRemainders(paramTyped, (Dynamic)typed.get(DSL.remainderFinder())));
/*    */           typed1 = (Typed)renameField(typed1, "TileEntities", "block_entities");
/*    */           typed1 = (Typed)renameField(typed1, "TileTicks", "block_ticks");
/*    */           typed1 = (Typed)renameField(typed1, "Entities", "entities");
/*    */           typed1 = (Typed)renameField(typed1, "Sections", "sections");
/*    */           typed1 = typed1.updateTyped(paramOpticFinder2, paramType, ());
/*    */           typed1 = (Typed)renameField(typed1, "Structures", "structures");
/*    */           return typed1.update(DSL.remainderFinder(), ());
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static Typed<?> renameField(Typed<?> paramTyped, String paramString1, String paramString2) {
/* 51 */     return renameFieldHelper(paramTyped, paramString1, paramString2, paramTyped.getType().findFieldType(paramString1)).update(DSL.remainderFinder(), paramDynamic -> paramDynamic.remove(paramString));
/*    */   }
/*    */   
/*    */   private static <A> Typed<?> renameFieldHelper(Typed<?> paramTyped, String paramString1, String paramString2, Type<A> paramType) {
/* 55 */     Type type1 = DSL.optional((Type)DSL.field(paramString1, paramType));
/* 56 */     Type type2 = DSL.optional((Type)DSL.field(paramString2, paramType));
/* 57 */     return paramTyped.update(type1.finder(), type2, Function.identity());
/*    */   }
/*    */   
/*    */   private static <A> Typed<Pair<String, A>> appendChunkName(Typed<A> paramTyped) {
/* 61 */     return new Typed(DSL.named("chunk", paramTyped.getType()), paramTyped.getOps(), Pair.of("chunk", paramTyped.getValue()));
/*    */   }
/*    */   
/*    */   private static <T> Dynamic<T> mergeRemainders(Typed<?> paramTyped, Dynamic<T> paramDynamic) {
/* 65 */     DynamicOps dynamicOps = paramDynamic.getOps();
/* 66 */     Dynamic dynamic = ((Dynamic)paramTyped.get(DSL.remainderFinder())).convert(dynamicOps);
/* 67 */     DataResult dataResult = dynamicOps.getMap(paramDynamic.getValue()).flatMap(paramMapLike -> paramDynamicOps.mergeToMap(paramDynamic.getValue(), paramMapLike));
/* 68 */     return dataResult.result().map(paramObject -> new Dynamic(paramDynamicOps, paramObject)).orElse(paramDynamic);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\ChunkRenamesFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */