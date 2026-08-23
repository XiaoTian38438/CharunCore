/*     */ package net.minecraft.util.datafix;
/*     */ 
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.OpticFinder;
/*     */ import com.mojang.datafixers.RewriteResult;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.View;
/*     */ import com.mojang.datafixers.functions.PointFreeRule;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.BitSet;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.UnaryOperator;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class ExtraDataFixUtils
/*     */ {
/*     */   public static Dynamic<?> fixBlockPos(Dynamic<?> paramDynamic) {
/*  28 */     Optional<Number> optional1 = paramDynamic.get("X").asNumber().result();
/*  29 */     Optional<Number> optional2 = paramDynamic.get("Y").asNumber().result();
/*  30 */     Optional<Number> optional3 = paramDynamic.get("Z").asNumber().result();
/*  31 */     if (optional1.isEmpty() || optional2.isEmpty() || optional3.isEmpty())
/*     */     {
/*  33 */       return paramDynamic;
/*     */     }
/*  35 */     return createBlockPos(paramDynamic, ((Number)optional1.get()).intValue(), ((Number)optional2.get()).intValue(), ((Number)optional3.get()).intValue());
/*     */   }
/*     */   
/*     */   public static Dynamic<?> fixInlineBlockPos(Dynamic<?> paramDynamic, String paramString1, String paramString2, String paramString3, String paramString4) {
/*  39 */     Optional<Number> optional1 = paramDynamic.get(paramString1).asNumber().result();
/*  40 */     Optional<Number> optional2 = paramDynamic.get(paramString2).asNumber().result();
/*  41 */     Optional<Number> optional3 = paramDynamic.get(paramString3).asNumber().result();
/*  42 */     if (optional1.isEmpty() || optional2.isEmpty() || optional3.isEmpty()) {
/*  43 */       return paramDynamic;
/*     */     }
/*  45 */     return paramDynamic.remove(paramString1).remove(paramString2).remove(paramString3)
/*  46 */       .set(paramString4, createBlockPos(paramDynamic, ((Number)optional1.get()).intValue(), ((Number)optional2.get()).intValue(), ((Number)optional3.get()).intValue()));
/*     */   }
/*     */   
/*     */   public static Dynamic<?> createBlockPos(Dynamic<?> paramDynamic, int paramInt1, int paramInt2, int paramInt3) {
/*  50 */     return paramDynamic.createIntList(IntStream.of(new int[] { paramInt1, paramInt2, paramInt3 }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T, R> Typed<R> cast(Type<R> paramType, Typed<T> paramTyped) {
/*  57 */     return new Typed(paramType, paramTyped.getOps(), paramTyped.getValue());
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> Typed<T> cast(Type<T> paramType, Object paramObject, DynamicOps<?> paramDynamicOps) {
/*  62 */     return new Typed(paramType, paramDynamicOps, paramObject);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Type<?> patchSubType(Type<?> paramType1, Type<?> paramType2, Type<?> paramType3) {
/*  67 */     return paramType1.all(typePatcher(paramType2, paramType3), true, false).view().newType();
/*     */   }
/*     */   
/*     */   private static <A, B> TypeRewriteRule typePatcher(Type<A> paramType, Type<B> paramType1) {
/*  71 */     RewriteResult rewriteResult = RewriteResult.create(View.create("Patcher", paramType, paramType1, paramDynamicOps -> ()), new BitSet());
/*     */ 
/*     */ 
/*     */     
/*  75 */     return TypeRewriteRule.everywhere(TypeRewriteRule.ifSame(paramType, rewriteResult), PointFreeRule.nop(), true, true);
/*     */   }
/*     */   
/*     */   @SafeVarargs
/*     */   public static <T> Function<Typed<?>, Typed<?>> chainAllFilters(Function<Typed<?>, Typed<?>>... paramVarArgs) {
/*  80 */     return paramTyped -> {
/*     */         for (Function<Typed, Typed> function : paramArrayOfFunction) {
/*     */           paramTyped = function.apply(paramTyped);
/*     */         }
/*     */         return paramTyped;
/*     */       };
/*     */   }
/*     */   
/*     */   public static Dynamic<?> blockState(String paramString, Map<String, String> paramMap) {
/*  89 */     Dynamic dynamic = new Dynamic((DynamicOps)NbtOps.INSTANCE, new CompoundTag());
/*  90 */     Dynamic<?> dynamic1 = dynamic.set("Name", dynamic.createString(paramString));
/*  91 */     if (!paramMap.isEmpty()) {
/*  92 */       dynamic1 = dynamic1.set("Properties", dynamic.createMap((Map)paramMap.entrySet().stream()
/*  93 */             .collect(Collectors.toMap(paramEntry -> paramDynamic.createString((String)paramEntry.getKey()), paramEntry -> paramDynamic.createString((String)paramEntry.getValue())))));
/*     */     }
/*     */     
/*  96 */     return dynamic1;
/*     */   }
/*     */   
/*     */   public static Dynamic<?> blockState(String paramString) {
/* 100 */     return blockState(paramString, Map.of());
/*     */   }
/*     */   
/*     */   public static Dynamic<?> fixStringField(Dynamic<?> paramDynamic, String paramString, UnaryOperator<String> paramUnaryOperator) {
/* 104 */     return paramDynamic.update(paramString, paramDynamic2 -> {
/*     */           Objects.requireNonNull(paramDynamic1);
/*     */           return (Dynamic)DataFixUtils.orElse(paramDynamic2.asString().map(paramUnaryOperator).map(paramDynamic1::createString).result(), paramDynamic2);
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
/*     */   public static String dyeColorIdToName(int paramInt) {
/* 119 */     switch (paramInt) { default: case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: case 10: case 11: case 12: case 13: case 14: case 15: break; }  return 
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
/* 135 */       "black";
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> Typed<?> readAndSet(Typed<?> paramTyped, OpticFinder<T> paramOpticFinder, Dynamic<?> paramDynamic) {
/* 140 */     return paramTyped.set(paramOpticFinder, Util.readTypedOrThrow(paramOpticFinder.type(), paramDynamic, true));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\ExtraDataFixUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */