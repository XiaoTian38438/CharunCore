/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
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
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.util.datafix.LegacyComponentDataFixUtils;
/*    */ 
/*    */ public class BannerEntityCustomNameToOverrideComponentFix
/*    */   extends DataFix {
/*    */   public BannerEntityCustomNameToOverrideComponentFix(Schema paramSchema) {
/* 21 */     super(paramSchema, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 26 */     Type type = getInputSchema().getType(References.BLOCK_ENTITY);
/* 27 */     TaggedChoice.TaggedChoiceType taggedChoiceType = getInputSchema().findChoiceType(References.BLOCK_ENTITY);
/* 28 */     OpticFinder opticFinder1 = type.findField("CustomName");
/*    */     
/* 30 */     OpticFinder opticFinder2 = DSL.typeFinder(getInputSchema().getType(References.TEXT_COMPONENT));
/*    */     
/* 32 */     return fixTypeEverywhereTyped("Banner entity custom_name to item_name component fix", type, paramTyped -> {
/*    */           Object object = ((Pair)paramTyped.get(paramTaggedChoiceType.finder())).getFirst();
/*    */           return object.equals("minecraft:banner") ? fix(paramTyped, paramOpticFinder1, paramOpticFinder2) : paramTyped;
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Typed<?> fix(Typed<?> paramTyped, OpticFinder<Pair<String, String>> paramOpticFinder, OpticFinder<?> paramOpticFinder1) {
/* 44 */     Optional optional = paramTyped.getOptionalTyped(paramOpticFinder1).flatMap(paramTyped -> paramTyped.getOptional(paramOpticFinder).map(Pair::getSecond));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 49 */     boolean bool = optional.flatMap(LegacyComponentDataFixUtils::extractTranslationString).filter(paramString -> paramString.equals("block.minecraft.ominous_banner")).isPresent();
/*    */     
/* 51 */     if (bool) {
/* 52 */       return Util.writeAndReadTypedOrThrow(paramTyped, paramTyped.getType(), paramDynamic -> {
/*    */             Dynamic dynamic = paramDynamic.createMap(Map.of(paramDynamic.createString("minecraft:item_name"), paramDynamic.createString(paramOptional.get()), paramDynamic.createString("minecraft:hide_additional_tooltip"), paramDynamic.emptyMap()));
/*    */ 
/*    */             
/*    */             return paramDynamic.set("components", dynamic).remove("CustomName");
/*    */           });
/*    */     }
/*    */     
/* 60 */     return paramTyped;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\BannerEntityCustomNameToOverrideComponentFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */