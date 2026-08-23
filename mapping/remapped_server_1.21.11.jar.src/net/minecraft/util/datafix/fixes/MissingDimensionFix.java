/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.FieldFinder;
/*     */ import com.mojang.datafixers.OpticFinder;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.templates.CompoundList;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.datafixers.util.Unit;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
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
/*     */ public class MissingDimensionFix
/*     */   extends DataFix
/*     */ {
/*     */   public MissingDimensionFix(Schema paramSchema, boolean paramBoolean) {
/*  35 */     super(paramSchema, paramBoolean);
/*     */   }
/*     */   
/*     */   protected static <A> Type<Pair<A, Dynamic<?>>> fields(String paramString, Type<A> paramType) {
/*  39 */     return DSL.and((Type)DSL.field(paramString, paramType), DSL.remainderType());
/*     */   }
/*     */   
/*     */   protected static <A> Type<Pair<Either<A, Unit>, Dynamic<?>>> optionalFields(String paramString, Type<A> paramType) {
/*  43 */     return DSL.and(DSL.optional((Type)DSL.field(paramString, paramType)), DSL.remainderType());
/*     */   }
/*     */   
/*     */   protected static <A1, A2> Type<Pair<Either<A1, Unit>, Pair<Either<A2, Unit>, Dynamic<?>>>> optionalFields(String paramString1, Type<A1> paramType, String paramString2, Type<A2> paramType1) {
/*  47 */     return DSL.and(
/*  48 */         DSL.optional((Type)DSL.field(paramString1, paramType)), 
/*  49 */         DSL.optional((Type)DSL.field(paramString2, paramType1)), 
/*  50 */         DSL.remainderType());
/*     */   }
/*     */ 
/*     */   
/*     */   protected TypeRewriteRule makeRule() {
/*  55 */     Schema schema = getInputSchema();
/*  56 */     Type<?> type = DSL.taggedChoiceType("type", DSL.string(), (Map)ImmutableMap.of("minecraft:debug", 
/*  57 */           DSL.remainderType(), "minecraft:flat", 
/*  58 */           flatType(schema), "minecraft:noise", 
/*  59 */           optionalFields("biome_source", 
/*  60 */             DSL.taggedChoiceType("type", DSL.string(), (Map)ImmutableMap.of("minecraft:fixed", 
/*  61 */                 fields("biome", schema.getType(References.BIOME)), "minecraft:multi_noise", 
/*  62 */                 DSL.list(fields("biome", schema.getType(References.BIOME))), "minecraft:checkerboard", 
/*  63 */                 fields("biomes", (Type<?>)DSL.list(schema.getType(References.BIOME))), "minecraft:vanilla_layered", 
/*  64 */                 DSL.remainderType(), "minecraft:the_end", 
/*  65 */                 DSL.remainderType())), "settings", 
/*     */             
/*  67 */             DSL.or(DSL.string(), optionalFields("default_block", schema
/*  68 */                 .getType(References.BLOCK_NAME), "default_fluid", schema
/*  69 */                 .getType(References.BLOCK_NAME))))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  74 */     CompoundList.CompoundListType compoundListType = DSL.compoundList(NamespacedSchema.namespacedString(), fields("generator", type));
/*  75 */     Type type1 = DSL.and((Type)compoundListType, DSL.remainderType());
/*     */     
/*  77 */     Type type2 = schema.getType(References.WORLD_GEN_SETTINGS);
/*     */     
/*  79 */     FieldFinder fieldFinder = new FieldFinder("dimensions", type1);
/*  80 */     if (!type2.findFieldType("dimensions").equals(type1)) {
/*  81 */       throw new IllegalStateException();
/*     */     }
/*  83 */     OpticFinder opticFinder = compoundListType.finder();
/*  84 */     return fixTypeEverywhereTyped("MissingDimensionFix", type2, paramTyped -> paramTyped.updateTyped((OpticFinder)paramFieldFinder, ()));
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
/*     */   protected static Type<? extends Pair<? extends Either<? extends Pair<? extends Either<?, Unit>, ? extends Pair<? extends Either<? extends List<? extends Pair<? extends Either<?, Unit>, Dynamic<?>>>, Unit>, Dynamic<?>>>, Unit>, Dynamic<?>>> flatType(Schema paramSchema) {
/*  99 */     return (Type)optionalFields("settings", optionalFields("biome", paramSchema
/* 100 */           .getType(References.BIOME), "layers", 
/* 101 */           (Type<?>)DSL.list(optionalFields("block", paramSchema.getType(References.BLOCK_NAME)))));
/*     */   }
/*     */ 
/*     */   
/*     */   private <T> Dynamic<T> recreateSettings(Dynamic<T> paramDynamic) {
/* 106 */     long l = paramDynamic.get("seed").asLong(0L);
/* 107 */     return new Dynamic(paramDynamic.getOps(), WorldGenSettingsFix.vanillaLevels(paramDynamic, l, WorldGenSettingsFix.defaultOverworld(paramDynamic, l), false));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\MissingDimensionFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */