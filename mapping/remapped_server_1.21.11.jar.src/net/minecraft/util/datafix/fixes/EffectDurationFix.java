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
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EffectDurationFix
/*    */   extends DataFix
/*    */ {
/* 21 */   private static final Set<String> POTION_ITEMS = Set.of("minecraft:potion", "minecraft:splash_potion", "minecraft:lingering_potion", "minecraft:tipped_arrow");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public EffectDurationFix(Schema paramSchema) {
/* 29 */     super(paramSchema, false);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 34 */     Schema schema = getInputSchema();
/* 35 */     Type type = getInputSchema().getType(References.ITEM_STACK);
/* 36 */     OpticFinder opticFinder1 = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/* 37 */     OpticFinder opticFinder2 = type.findField("tag");
/* 38 */     return TypeRewriteRule.seq(
/* 39 */         fixTypeEverywhereTyped("EffectDurationEntity", schema.getType(References.ENTITY), paramTyped -> paramTyped.update(DSL.remainderFinder(), this::updateEntity)), new TypeRewriteRule[] {
/*    */ 
/*    */           
/* 42 */           fixTypeEverywhereTyped("EffectDurationPlayer", schema.getType(References.PLAYER), paramTyped -> paramTyped.update(DSL.remainderFinder(), this::updateEntity)), 
/*    */ 
/*    */           
/* 45 */           fixTypeEverywhereTyped("EffectDurationItem", type, paramTyped -> {
/*    */               if (paramTyped.getOptional(paramOpticFinder1).filter(()).isPresent()) {
/*    */                 Optional<Typed> optional = paramTyped.getOptionalTyped(paramOpticFinder2);
/*    */                 if (optional.isPresent()) {
/*    */                   Dynamic dynamic = (Dynamic)((Typed)optional.get()).get(DSL.remainderFinder());
/*    */                   Typed typed = ((Typed)optional.get()).set(DSL.remainderFinder(), dynamic.update("CustomPotionEffects", this::fix));
/*    */                   return paramTyped.set(paramOpticFinder2, typed);
/*    */                 } 
/*    */               } 
/*    */               return paramTyped;
/*    */             })
/*    */         });
/*    */   }
/*    */   
/*    */   private Dynamic<?> fixEffect(Dynamic<?> paramDynamic) {
/* 60 */     return paramDynamic.update("FactorCalculationData", paramDynamic2 -> {
/*    */           int i = paramDynamic2.get("effect_changed_timestamp").asInt(-1);
/*    */           paramDynamic2 = paramDynamic2.remove("effect_changed_timestamp");
/*    */           int j = paramDynamic1.get("Duration").asInt(-1);
/*    */           int k = i - j;
/*    */           return paramDynamic2.set("ticks_active", paramDynamic2.createInt(k));
/*    */         });
/*    */   }
/*    */   
/*    */   private Dynamic<?> fix(Dynamic<?> paramDynamic) {
/* 70 */     return paramDynamic.createList(paramDynamic.asStream().map(this::fixEffect));
/*    */   }
/*    */ 
/*    */   
/*    */   private Dynamic<?> updateEntity(Dynamic<?> paramDynamic) {
/* 75 */     paramDynamic = paramDynamic.update("Effects", this::fix);
/* 76 */     paramDynamic = paramDynamic.update("ActiveEffects", this::fix);
/* 77 */     paramDynamic = paramDynamic.update("CustomPotionEffects", this::fix);
/* 78 */     return paramDynamic;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\EffectDurationFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */