/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.OpticFinder;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.datafixers.util.Unit;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EquipmentFormatFix
/*     */   extends DataFix
/*     */ {
/*     */   public EquipmentFormatFix(Schema paramSchema) {
/*  27 */     super(paramSchema, true);
/*     */   }
/*     */ 
/*     */   
/*     */   protected TypeRewriteRule makeRule() {
/*  32 */     Type<?> type1 = getInputSchema().getTypeRaw(References.ITEM_STACK);
/*  33 */     Type<?> type2 = getOutputSchema().getTypeRaw(References.ITEM_STACK);
/*  34 */     OpticFinder<?> opticFinder = type1.findField("id");
/*  35 */     return fix(type1, type2, opticFinder);
/*     */   }
/*     */ 
/*     */   
/*     */   private <ItemStackOld, ItemStackNew> TypeRewriteRule fix(Type<ItemStackOld> paramType, Type<ItemStackNew> paramType1, OpticFinder<?> paramOpticFinder) {
/*  40 */     Type type1 = DSL.named(References.ENTITY_EQUIPMENT.typeName(), DSL.and(
/*  41 */           DSL.optional((Type)DSL.field("ArmorItems", (Type)DSL.list(paramType))), 
/*  42 */           DSL.optional((Type)DSL.field("HandItems", (Type)DSL.list(paramType))), 
/*  43 */           DSL.optional((Type)DSL.field("body_armor_item", paramType)), 
/*  44 */           DSL.optional((Type)DSL.field("saddle", paramType))));
/*     */ 
/*     */     
/*  47 */     Type type2 = DSL.named(References.ENTITY_EQUIPMENT.typeName(), 
/*  48 */         DSL.optional((Type)DSL.field("equipment", DSL.and(
/*  49 */               DSL.optional((Type)DSL.field("mainhand", paramType1)), 
/*  50 */               DSL.optional((Type)DSL.field("offhand", paramType1)), 
/*  51 */               DSL.optional((Type)DSL.field("feet", paramType1)), 
/*  52 */               DSL.and(
/*  53 */                 DSL.optional((Type)DSL.field("legs", paramType1)), 
/*  54 */                 DSL.optional((Type)DSL.field("chest", paramType1)), 
/*  55 */                 DSL.optional((Type)DSL.field("head", paramType1)), 
/*  56 */                 DSL.and(
/*  57 */                   DSL.optional((Type)DSL.field("body", paramType1)), 
/*  58 */                   DSL.optional((Type)DSL.field("saddle", paramType1)), 
/*  59 */                   DSL.remainderType()))))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  65 */     if (!type1.equals(getInputSchema().getType(References.ENTITY_EQUIPMENT))) {
/*  66 */       throw new IllegalStateException("Input entity_equipment type does not match expected");
/*     */     }
/*     */     
/*  69 */     if (!type2.equals(getOutputSchema().getType(References.ENTITY_EQUIPMENT))) {
/*  70 */       throw new IllegalStateException("Output entity_equipment type does not match expected");
/*     */     }
/*     */     
/*  73 */     return fixTypeEverywhere("EquipmentFormatFix", type1, type2, paramDynamicOps -> {
/*     */           Predicate predicate = ();
/*     */           return ();
/*     */         });
/*     */   }
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
/*     */   @SafeVarargs
/*     */   private static boolean areAllEmpty(Either<?, Unit>... paramVarArgs) {
/* 131 */     for (Either<?, Unit> either : paramVarArgs) {
/* 132 */       if (either.right().isEmpty()) {
/* 133 */         return false;
/*     */       }
/*     */     } 
/* 136 */     return true;
/*     */   }
/*     */   
/*     */   private static <ItemStack> Either<ItemStack, Unit> getItemFromList(int paramInt, List<ItemStack> paramList, Predicate<ItemStack> paramPredicate) {
/* 140 */     if (paramInt >= paramList.size()) {
/* 141 */       return Either.right(Unit.INSTANCE);
/*     */     }
/* 143 */     ItemStack itemStack = paramList.get(paramInt);
/* 144 */     if (paramPredicate.test(itemStack)) {
/* 145 */       return Either.right(Unit.INSTANCE);
/*     */     }
/* 147 */     return Either.left(itemStack);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\EquipmentFormatFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */