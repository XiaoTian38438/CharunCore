/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Arrays;
/*    */ import java.util.Optional;
/*    */ import java.util.UUID;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ public abstract class AbstractUUIDFix
/*    */   extends DataFix
/*    */ {
/*    */   protected DSL.TypeReference typeReference;
/*    */   
/*    */   public AbstractUUIDFix(Schema paramSchema, DSL.TypeReference paramTypeReference) {
/* 21 */     super(paramSchema, false);
/* 22 */     this.typeReference = paramTypeReference;
/*    */   }
/*    */   
/*    */   protected Typed<?> updateNamedChoice(Typed<?> paramTyped, String paramString, Function<Dynamic<?>, Dynamic<?>> paramFunction) {
/* 26 */     Type type1 = getInputSchema().getChoiceType(this.typeReference, paramString);
/* 27 */     Type type2 = getOutputSchema().getChoiceType(this.typeReference, paramString);
/* 28 */     return paramTyped.updateTyped(DSL.namedChoice(paramString, type1), type2, paramTyped -> paramTyped.update(DSL.remainderFinder(), paramFunction));
/*    */   }
/*    */   
/*    */   protected static Optional<Dynamic<?>> replaceUUIDString(Dynamic<?> paramDynamic, String paramString1, String paramString2) {
/* 32 */     return createUUIDFromString(paramDynamic, paramString1).map(paramDynamic2 -> paramDynamic1.remove(paramString1).set(paramString2, paramDynamic2));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected static Optional<Dynamic<?>> replaceUUIDMLTag(Dynamic<?> paramDynamic, String paramString1, String paramString2) {
/* 38 */     return paramDynamic.get(paramString1).result().flatMap(AbstractUUIDFix::createUUIDFromML).map(paramDynamic2 -> paramDynamic1.remove(paramString1).set(paramString2, paramDynamic2));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected static Optional<Dynamic<?>> replaceUUIDLeastMost(Dynamic<?> paramDynamic, String paramString1, String paramString2) {
/* 44 */     String str1 = paramString1 + "Most";
/* 45 */     String str2 = paramString1 + "Least";
/* 46 */     return createUUIDFromLongs(paramDynamic, str1, str2).map(paramDynamic2 -> paramDynamic1.remove(paramString1).remove(paramString2).set(paramString3, paramDynamic2));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected static Optional<Dynamic<?>> createUUIDFromString(Dynamic<?> paramDynamic, String paramString) {
/* 52 */     return paramDynamic.get(paramString).result().flatMap(paramDynamic2 -> {
/*    */           String str = paramDynamic2.asString(null);
/*    */           if (str != null) {
/*    */             try {
/*    */               UUID uUID = UUID.fromString(str);
/*    */               return createUUIDTag(paramDynamic1, uUID.getMostSignificantBits(), uUID.getLeastSignificantBits());
/* 58 */             } catch (IllegalArgumentException illegalArgumentException) {}
/*    */           }
/*    */           return Optional.empty();
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected static Optional<Dynamic<?>> createUUIDFromML(Dynamic<?> paramDynamic) {
/* 67 */     return createUUIDFromLongs(paramDynamic, "M", "L");
/*    */   }
/*    */   
/*    */   protected static Optional<Dynamic<?>> createUUIDFromLongs(Dynamic<?> paramDynamic, String paramString1, String paramString2) {
/* 71 */     long l1 = paramDynamic.get(paramString1).asLong(0L);
/* 72 */     long l2 = paramDynamic.get(paramString2).asLong(0L);
/* 73 */     if (l1 == 0L || l2 == 0L) {
/* 74 */       return Optional.empty();
/*    */     }
/* 76 */     return createUUIDTag(paramDynamic, l1, l2);
/*    */   }
/*    */   
/*    */   protected static Optional<Dynamic<?>> createUUIDTag(Dynamic<?> paramDynamic, long paramLong1, long paramLong2) {
/* 80 */     return Optional.of(paramDynamic.createIntList(Arrays.stream(new int[] { (int)(paramLong1 >> 32L), (int)paramLong1, (int)(paramLong2 >> 32L), (int)paramLong2 })));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\AbstractUUIDFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */