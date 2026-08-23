/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.datafixers.util.Unit;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityEquipmentToArmorAndHandFix
/*     */   extends DataFix
/*     */ {
/*     */   public EntityEquipmentToArmorAndHandFix(Schema paramSchema) {
/*  29 */     super(paramSchema, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeRewriteRule makeRule() {
/*  34 */     return cap(getInputSchema().getTypeRaw(References.ITEM_STACK), getOutputSchema().getTypeRaw(References.ITEM_STACK));
/*     */   }
/*     */ 
/*     */   
/*     */   private <ItemStackOld, ItemStackNew> TypeRewriteRule cap(Type<ItemStackOld> paramType, Type<ItemStackNew> paramType1) {
/*  39 */     Type type1 = DSL.named(References.ENTITY_EQUIPMENT.typeName(), DSL.optional((Type)DSL.field("Equipment", (Type)DSL.list(paramType))));
/*  40 */     Type type2 = DSL.named(References.ENTITY_EQUIPMENT.typeName(), DSL.and(
/*  41 */           DSL.optional((Type)DSL.field("ArmorItems", (Type)DSL.list(paramType1))), 
/*  42 */           DSL.optional((Type)DSL.field("HandItems", (Type)DSL.list(paramType1))), 
/*  43 */           DSL.optional((Type)DSL.field("body_armor_item", paramType1)), 
/*  44 */           DSL.optional((Type)DSL.field("saddle", paramType1))));
/*     */ 
/*     */     
/*  47 */     if (!type1.equals(getInputSchema().getType(References.ENTITY_EQUIPMENT))) {
/*  48 */       throw new IllegalStateException("Input entity_equipment type does not match expected");
/*     */     }
/*     */     
/*  51 */     if (!type2.equals(getOutputSchema().getType(References.ENTITY_EQUIPMENT))) {
/*  52 */       throw new IllegalStateException("Output entity_equipment type does not match expected");
/*     */     }
/*     */     
/*  55 */     return TypeRewriteRule.seq(
/*     */         
/*  57 */         fixTypeEverywhereTyped("EntityEquipmentToArmorAndHandFix - drop chances", getInputSchema().getType(References.ENTITY), paramTyped -> paramTyped.update(DSL.remainderFinder(), EntityEquipmentToArmorAndHandFix::fixDropChances)), 
/*     */ 
/*     */         
/*  60 */         fixTypeEverywhere("EntityEquipmentToArmorAndHandFix - equipment", type1, type2, paramDynamicOps -> {
/*     */             Object object = ((Pair)paramType.read((new Dynamic(paramDynamicOps)).emptyMap()).result().orElseThrow(())).getFirst();
/*     */             Either either = Either.right(DSL.unit());
/*     */             return ();
/*     */           }));
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
/*     */   private static Dynamic<?> fixDropChances(Dynamic<?> paramDynamic) {
/*  88 */     Optional<Stream> optional = paramDynamic.get("DropChances").asStreamOpt().result();
/*  89 */     paramDynamic = paramDynamic.remove("DropChances");
/*     */     
/*  91 */     if (optional.isPresent()) {
/*     */ 
/*     */ 
/*     */       
/*  95 */       Iterator<Float> iterator = Stream.concat(((Stream)optional.get()).map(paramDynamic -> Float.valueOf(paramDynamic.asFloat(0.0F))), Stream.generate(() -> Float.valueOf(0.0F))).iterator();
/*  96 */       float f = ((Float)iterator.next()).floatValue();
/*  97 */       if (paramDynamic.get("HandDropChances").result().isEmpty()) {
/*  98 */         Objects.requireNonNull(paramDynamic); paramDynamic = paramDynamic.set("HandDropChances", paramDynamic.createList(Stream.<Float>of(new Float[] { Float.valueOf(f), Float.valueOf(0.0F) }).map(paramDynamic::createFloat)));
/*     */       } 
/*     */       
/* 101 */       if (paramDynamic.get("ArmorDropChances").result().isEmpty()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 107 */         Objects.requireNonNull(paramDynamic); paramDynamic = paramDynamic.set("ArmorDropChances", paramDynamic.createList(Stream.<Float>of(new Float[] { iterator.next(), iterator.next(), iterator.next(), iterator.next() }).map(paramDynamic::createFloat)));
/*     */       } 
/*     */     } 
/*     */     
/* 111 */     return paramDynamic;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\EntityEquipmentToArmorAndHandFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */