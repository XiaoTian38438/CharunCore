/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.templates.TaggedChoice;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.datafix.LegacyComponentDataFixUtils;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class OminousBannerRarityFix extends DataFix {
/*    */   public OminousBannerRarityFix(Schema paramSchema) {
/* 18 */     super(paramSchema, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 23 */     Type type1 = getInputSchema().getType(References.BLOCK_ENTITY);
/* 24 */     Type type2 = getInputSchema().getType(References.ITEM_STACK);
/* 25 */     TaggedChoice.TaggedChoiceType taggedChoiceType = getInputSchema().findChoiceType(References.BLOCK_ENTITY);
/* 26 */     OpticFinder opticFinder1 = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/* 27 */     OpticFinder opticFinder2 = type1.findField("components");
/* 28 */     OpticFinder opticFinder3 = type2.findField("components");
/*    */     
/* 30 */     OpticFinder opticFinder4 = opticFinder2.type().findField("minecraft:item_name");
/*    */     
/* 32 */     OpticFinder opticFinder5 = DSL.typeFinder(getInputSchema().getType(References.TEXT_COMPONENT));
/*    */     
/* 34 */     return TypeRewriteRule.seq(
/* 35 */         fixTypeEverywhereTyped("Ominous Banner block entity common rarity to uncommon rarity fix", type1, paramTyped -> {
/*    */             Object object = ((Pair)paramTyped.get(paramTaggedChoiceType.finder())).getFirst();
/*    */             
/*    */             return object.equals("minecraft:banner") ? fix(paramTyped, paramOpticFinder1, paramOpticFinder2, paramOpticFinder3) : paramTyped;
/* 39 */           }), fixTypeEverywhereTyped("Ominous Banner item stack common rarity to uncommon rarity fix", type2, paramTyped -> {
/*    */             String str = paramTyped.getOptional(paramOpticFinder1).map(Pair::getSecond).orElse("");
/*    */             return str.equals("minecraft:white_banner") ? fix(paramTyped, paramOpticFinder2, paramOpticFinder3, paramOpticFinder4) : paramTyped;
/*    */           }));
/*    */   }
/*    */ 
/*    */   
/*    */   private Typed<?> fix(Typed<?> paramTyped, OpticFinder<?> paramOpticFinder1, OpticFinder<?> paramOpticFinder2, OpticFinder<Pair<String, String>> paramOpticFinder) {
/* 47 */     return paramTyped.updateTyped(paramOpticFinder1, paramTyped -> {
/*    */           boolean bool = paramTyped.getOptionalTyped(paramOpticFinder1).flatMap(()).map(Pair::getSecond).flatMap(LegacyComponentDataFixUtils::extractTranslationString).filter(()).isPresent();
/*    */           return bool ? paramTyped.updateTyped(paramOpticFinder1, ()).update(DSL.remainderFinder(), ()) : paramTyped;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\OminousBannerRarityFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */