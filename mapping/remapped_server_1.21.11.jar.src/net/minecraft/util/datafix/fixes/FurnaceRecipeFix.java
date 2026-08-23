/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.datafixers.util.Unit;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FurnaceRecipeFix
/*    */   extends DataFix
/*    */ {
/*    */   public FurnaceRecipeFix(Schema paramSchema, boolean paramBoolean) {
/* 29 */     super(paramSchema, paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 34 */     return cap(getOutputSchema().getTypeRaw(References.RECIPE));
/*    */   }
/*    */   
/*    */   private <R> TypeRewriteRule cap(Type<R> paramType) {
/* 38 */     Type type1 = DSL.and(
/* 39 */         DSL.optional((Type)DSL.field("RecipesUsed", DSL.and((Type)DSL.compoundList(paramType, DSL.intType()), DSL.remainderType()))), 
/* 40 */         DSL.remainderType());
/*    */ 
/*    */     
/* 43 */     OpticFinder opticFinder1 = DSL.namedChoice("minecraft:furnace", getInputSchema().getChoiceType(References.BLOCK_ENTITY, "minecraft:furnace"));
/* 44 */     OpticFinder opticFinder2 = DSL.namedChoice("minecraft:blast_furnace", getInputSchema().getChoiceType(References.BLOCK_ENTITY, "minecraft:blast_furnace"));
/* 45 */     OpticFinder opticFinder3 = DSL.namedChoice("minecraft:smoker", getInputSchema().getChoiceType(References.BLOCK_ENTITY, "minecraft:smoker"));
/*    */     
/* 47 */     Type type2 = getOutputSchema().getChoiceType(References.BLOCK_ENTITY, "minecraft:furnace");
/* 48 */     Type type3 = getOutputSchema().getChoiceType(References.BLOCK_ENTITY, "minecraft:blast_furnace");
/* 49 */     Type type4 = getOutputSchema().getChoiceType(References.BLOCK_ENTITY, "minecraft:smoker");
/*    */     
/* 51 */     Type type5 = getInputSchema().getType(References.BLOCK_ENTITY);
/* 52 */     Type type6 = getOutputSchema().getType(References.BLOCK_ENTITY);
/* 53 */     return fixTypeEverywhereTyped("FurnaceRecipesFix", type5, type6, paramTyped -> paramTyped.updateTyped(paramOpticFinder1, paramType1, ()).updateTyped(paramOpticFinder2, paramType4, ()).updateTyped(paramOpticFinder3, paramType5, ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private <R> Typed<?> updateFurnaceContents(Type<R> paramType, Type<Pair<Either<Pair<List<Pair<R, Integer>>, Dynamic<?>>, Unit>, Dynamic<?>>> paramType1, Typed<?> paramTyped) {
/* 62 */     Dynamic dynamic = (Dynamic)paramTyped.getOrCreate(DSL.remainderFinder());
/*    */     
/* 64 */     int i = dynamic.get("RecipesUsedSize").asInt(0);
/* 65 */     dynamic = dynamic.remove("RecipesUsedSize");
/*    */     
/* 67 */     ArrayList arrayList = Lists.newArrayList();
/* 68 */     for (byte b = 0; b < i; b++) {
/* 69 */       String str1 = "RecipeLocation" + b;
/* 70 */       String str2 = "RecipeAmount" + b;
/*    */       
/* 72 */       Optional optional = dynamic.get(str1).result();
/* 73 */       int j = dynamic.get(str2).asInt(0);
/* 74 */       if (j > 0) {
/* 75 */         optional.ifPresent(paramDynamic -> {
/*    */               Optional optional = paramType.read(paramDynamic).result();
/*    */               
/*    */               optional.ifPresent(());
/*    */             });
/*    */       }
/* 81 */       dynamic = dynamic.remove(str1).remove(str2);
/*    */     } 
/*    */     
/* 84 */     return paramTyped.set(DSL.remainderFinder(), paramType1, Pair.of(Either.left(Pair.of(arrayList, dynamic.emptyMap())), dynamic));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\FurnaceRecipeFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */