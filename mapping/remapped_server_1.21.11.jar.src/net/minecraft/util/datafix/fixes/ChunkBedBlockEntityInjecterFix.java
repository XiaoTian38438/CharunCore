/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.google.common.collect.Maps;
/*    */ import com.google.common.collect.Streams;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.templates.List;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public class ChunkBedBlockEntityInjecterFix
/*    */   extends DataFix {
/*    */   public ChunkBedBlockEntityInjecterFix(Schema paramSchema, boolean paramBoolean) {
/* 26 */     super(paramSchema, paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 31 */     Type type1 = getOutputSchema().getType(References.CHUNK);
/* 32 */     Type<?> type = type1.findFieldType("Level");
/* 33 */     Type type2 = type.findFieldType("TileEntities");
/* 34 */     if (!(type2 instanceof List.ListType)) {
/* 35 */       throw new IllegalStateException("Tile entity type is not a list type.");
/*    */     }
/* 37 */     List.ListType<?> listType = (List.ListType)type2;
/*    */     
/* 39 */     return cap(type, listType);
/*    */   }
/*    */   
/*    */   private <TE> TypeRewriteRule cap(Type<?> paramType, List.ListType<TE> paramListType) {
/* 43 */     Type type = paramListType.getElement();
/* 44 */     OpticFinder opticFinder1 = DSL.fieldFinder("Level", paramType);
/* 45 */     OpticFinder opticFinder2 = DSL.fieldFinder("TileEntities", (Type)paramListType);
/*    */ 
/*    */     
/* 48 */     char c = 'Ơ';
/*    */     
/* 50 */     return TypeRewriteRule.seq(
/* 51 */         fixTypeEverywhere("InjectBedBlockEntityType", (Type)getInputSchema().findChoiceType(References.BLOCK_ENTITY), (Type)getOutputSchema().findChoiceType(References.BLOCK_ENTITY), paramDynamicOps -> ()), 
/* 52 */         fixTypeEverywhereTyped("BedBlockEntityInjecter", getOutputSchema().getType(References.CHUNK), paramTyped -> {
/*    */             Typed typed = paramTyped.getTyped(paramOpticFinder1);
/*    */             Dynamic dynamic = (Dynamic)typed.get(DSL.remainderFinder());
/*    */             int i = dynamic.get("xPos").asInt(0);
/*    */             int j = dynamic.get("zPos").asInt(0);
/*    */             ArrayList arrayList = Lists.newArrayList((Iterable)typed.getOrCreate(paramOpticFinder2));
/*    */             List list = dynamic.get("Sections").asList(Function.identity());
/*    */             for (Dynamic dynamic1 : list) {
/*    */               int k = dynamic1.get("Y").asInt(0);
/*    */               Streams.mapWithIndex(dynamic1.get("Blocks").asIntStream(), ()).forEachOrdered(());
/*    */             } 
/*    */             return !arrayList.isEmpty() ? paramTyped.set(paramOpticFinder1, typed.set(paramOpticFinder2, arrayList)) : paramTyped;
/*    */           }));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\ChunkBedBlockEntityInjecterFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */