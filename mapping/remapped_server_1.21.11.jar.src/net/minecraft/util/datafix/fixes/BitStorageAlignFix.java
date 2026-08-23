/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.OpticFinder;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.templates.List;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.List;
/*     */ import java.util.stream.LongStream;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class BitStorageAlignFix
/*     */   extends DataFix
/*     */ {
/*     */   private static final int BIT_TO_LONG_SHIFT = 6;
/*     */   private static final int SECTION_WIDTH = 16;
/*     */   private static final int SECTION_HEIGHT = 16;
/*     */   private static final int SECTION_SIZE = 4096;
/*     */   private static final int HEIGHTMAP_BITS = 9;
/*     */   private static final int HEIGHTMAP_SIZE = 256;
/*     */   
/*     */   public BitStorageAlignFix(Schema paramSchema) {
/*  29 */     super(paramSchema, false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected TypeRewriteRule makeRule() {
/*  34 */     Type type1 = getInputSchema().getType(References.CHUNK);
/*  35 */     Type type2 = type1.findFieldType("Level");
/*     */     
/*  37 */     OpticFinder opticFinder1 = DSL.fieldFinder("Level", type2);
/*  38 */     OpticFinder opticFinder2 = opticFinder1.type().findField("Sections");
/*     */     
/*  40 */     Type type3 = ((List.ListType)opticFinder2.type()).getElement();
/*  41 */     OpticFinder opticFinder3 = DSL.typeFinder(type3);
/*     */     
/*  43 */     Type type4 = DSL.named(References.BLOCK_STATE.typeName(), DSL.remainderType());
/*  44 */     OpticFinder opticFinder4 = DSL.fieldFinder("Palette", (Type)DSL.list(type4));
/*     */     
/*  46 */     return fixTypeEverywhereTyped("BitStorageAlignFix", type1, getOutputSchema().getType(References.CHUNK), paramTyped -> paramTyped.updateTyped(paramOpticFinder1, ()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Typed<?> updateHeightmaps(Typed<?> paramTyped) {
/*  54 */     return paramTyped.update(DSL.remainderFinder(), paramDynamic -> paramDynamic.update("Heightmaps", ()));
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
/*     */   private static Typed<?> updateSections(OpticFinder<?> paramOpticFinder1, OpticFinder<?> paramOpticFinder2, OpticFinder<List<Pair<String, Dynamic<?>>>> paramOpticFinder, Typed<?> paramTyped) {
/*  66 */     return paramTyped.updateTyped(paramOpticFinder1, paramTyped -> paramTyped.updateTyped(paramOpticFinder1, ()));
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
/*     */   private static Dynamic<?> updateBitStorage(Dynamic<?> paramDynamic1, Dynamic<?> paramDynamic2, int paramInt1, int paramInt2) {
/*  82 */     long[] arrayOfLong1 = paramDynamic2.asLongStream().toArray();
/*  83 */     long[] arrayOfLong2 = addPadding(paramInt1, paramInt2, arrayOfLong1);
/*  84 */     return paramDynamic1.createLongList(LongStream.of(arrayOfLong2));
/*     */   }
/*     */   
/*     */   public static long[] addPadding(int paramInt1, int paramInt2, long[] paramArrayOflong) {
/*  88 */     int i = paramArrayOflong.length;
/*  89 */     if (i == 0) {
/*  90 */       return paramArrayOflong;
/*     */     }
/*     */     
/*  93 */     long l1 = (1L << paramInt2) - 1L;
/*  94 */     int j = 64 / paramInt2;
/*  95 */     int k = (paramInt1 + j - 1) / j;
/*     */     
/*  97 */     long[] arrayOfLong = new long[k];
/*     */     
/*  99 */     byte b1 = 0;
/* 100 */     int m = 0;
/* 101 */     long l2 = 0L;
/*     */     
/* 103 */     int n = 0;
/* 104 */     long l3 = paramArrayOflong[0];
/* 105 */     long l4 = (i > 1) ? paramArrayOflong[1] : 0L;
/*     */     
/* 107 */     for (byte b2 = 0; b2 < paramInt1; b2++) {
/* 108 */       long l; int i1 = b2 * paramInt2;
/* 109 */       int i2 = i1 >> 6;
/* 110 */       int i3 = (b2 + 1) * paramInt2 - 1 >> 6;
/* 111 */       int i4 = i1 ^ i2 << 6;
/*     */       
/* 113 */       if (i2 != n) {
/* 114 */         l3 = l4;
/* 115 */         l4 = (i2 + 1 < i) ? paramArrayOflong[i2 + 1] : 0L;
/* 116 */         n = i2;
/*     */       } 
/*     */ 
/*     */       
/* 120 */       if (i2 == i3) {
/* 121 */         l = l3 >>> i4 & l1;
/*     */       } else {
/* 123 */         int i6 = 64 - i4;
/* 124 */         l = (l3 >>> i4 | l4 << i6) & l1;
/*     */       } 
/*     */       
/* 127 */       int i5 = m + paramInt2;
/* 128 */       if (i5 >= 64) {
/* 129 */         arrayOfLong[b1++] = l2;
/* 130 */         l2 = l;
/* 131 */         m = paramInt2;
/*     */       } else {
/* 133 */         l2 |= l << m;
/* 134 */         m = i5;
/*     */       } 
/*     */     } 
/* 137 */     if (l2 != 0L) {
/* 138 */       arrayOfLong[b1] = l2;
/*     */     }
/*     */     
/* 141 */     return arrayOfLong;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\BitStorageAlignFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */