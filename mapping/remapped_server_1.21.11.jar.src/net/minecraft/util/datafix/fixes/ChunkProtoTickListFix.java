/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
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
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Stream;
/*     */ import org.apache.commons.lang3.mutable.MutableInt;
/*     */ 
/*     */ public class ChunkProtoTickListFix
/*     */   extends DataFix {
/*     */   private static final int SECTION_WIDTH = 16;
/*  31 */   private static final ImmutableSet<String> ALWAYS_WATERLOGGED = ImmutableSet.of("minecraft:bubble_column", "minecraft:kelp", "minecraft:kelp_plant", "minecraft:seagrass", "minecraft:tall_seagrass");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChunkProtoTickListFix(Schema paramSchema) {
/*  40 */     super(paramSchema, false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected TypeRewriteRule makeRule() {
/*  45 */     Type type = getInputSchema().getType(References.CHUNK);
/*  46 */     OpticFinder opticFinder1 = type.findField("Level");
/*  47 */     OpticFinder opticFinder2 = opticFinder1.type().findField("Sections");
/*  48 */     OpticFinder opticFinder3 = ((List.ListType)opticFinder2.type()).getElement().finder();
/*  49 */     OpticFinder opticFinder4 = opticFinder3.type().findField("block_states");
/*  50 */     OpticFinder opticFinder5 = opticFinder3.type().findField("biomes");
/*  51 */     OpticFinder opticFinder6 = opticFinder4.type().findField("palette");
/*  52 */     OpticFinder opticFinder7 = opticFinder1.type().findField("TileTicks");
/*     */     
/*  54 */     return fixTypeEverywhereTyped("ChunkProtoTickListFix", type, paramTyped -> paramTyped.updateTyped(paramOpticFinder1, ()));
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
/*     */   private Dynamic<?> makeTickList(Dynamic<?> paramDynamic, Int2ObjectMap<Supplier<PoorMansPalettedContainer>> paramInt2ObjectMap, byte paramByte, int paramInt1, int paramInt2, String paramString, Function<Dynamic<?>, String> paramFunction) {
/* 106 */     Stream<?> stream = Stream.empty();
/* 107 */     List<Dynamic> list = paramDynamic.get(paramString).asList(Function.identity());
/* 108 */     for (byte b = 0; b < list.size(); b++) {
/* 109 */       int i = b + paramByte;
/* 110 */       Supplier supplier = (Supplier)paramInt2ObjectMap.get(i);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 115 */       Stream<?> stream1 = ((Dynamic)list.get(b)).asStream().mapToInt(paramDynamic -> paramDynamic.asShort((short)-1)).filter(paramInt -> (paramInt > 0)).mapToObj(paramInt4 -> createTick(paramDynamic, paramSupplier, paramInt1, paramInt2, paramInt3, paramInt4, paramFunction));
/*     */       
/* 117 */       stream = Stream.concat(stream, stream1);
/*     */     } 
/* 119 */     return paramDynamic.createList(stream);
/*     */   }
/*     */   
/*     */   private static String getBlock(Dynamic<?> paramDynamic) {
/* 123 */     return (paramDynamic != null) ? paramDynamic.get("Name").asString("minecraft:air") : "minecraft:air";
/*     */   }
/*     */   
/*     */   private static String getLiquid(Dynamic<?> paramDynamic) {
/* 127 */     if (paramDynamic == null) {
/* 128 */       return "minecraft:empty";
/*     */     }
/* 130 */     String str = paramDynamic.get("Name").asString("");
/* 131 */     if ("minecraft:water".equals(str)) {
/* 132 */       return (paramDynamic.get("Properties").get("level").asInt(0) == 0) ? "minecraft:water" : "minecraft:flowing_water";
/*     */     }
/* 134 */     if ("minecraft:lava".equals(str)) {
/* 135 */       return (paramDynamic.get("Properties").get("level").asInt(0) == 0) ? "minecraft:lava" : "minecraft:flowing_lava";
/*     */     }
/* 137 */     if (ALWAYS_WATERLOGGED.contains(str) || paramDynamic.get("Properties").get("waterlogged").asBoolean(false)) {
/* 138 */       return "minecraft:water";
/*     */     }
/* 140 */     return "minecraft:empty";
/*     */   }
/*     */   
/*     */   private Dynamic<?> createTick(Dynamic<?> paramDynamic, Supplier<PoorMansPalettedContainer> paramSupplier, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Function<Dynamic<?>, String> paramFunction) {
/* 144 */     int i = paramInt4 & 0xF;
/* 145 */     int j = paramInt4 >>> 4 & 0xF;
/* 146 */     int k = paramInt4 >>> 8 & 0xF;
/* 147 */     String str = paramFunction.apply((paramSupplier != null) ? ((PoorMansPalettedContainer)paramSupplier.get()).get(i, j, k) : null);
/* 148 */     return paramDynamic.createMap((Map)ImmutableMap.builder()
/* 149 */         .put(paramDynamic.createString("i"), paramDynamic.createString(str))
/* 150 */         .put(paramDynamic.createString("x"), paramDynamic.createInt(paramInt1 * 16 + i))
/* 151 */         .put(paramDynamic.createString("y"), paramDynamic.createInt(paramInt2 * 16 + j))
/* 152 */         .put(paramDynamic.createString("z"), paramDynamic.createInt(paramInt3 * 16 + k))
/* 153 */         .put(paramDynamic.createString("t"), paramDynamic.createInt(0))
/* 154 */         .put(paramDynamic.createString("p"), paramDynamic.createInt(0))
/* 155 */         .build());
/*     */   }
/*     */   
/*     */   public static final class PoorMansPalettedContainer
/*     */   {
/*     */     private static final long SIZE_BITS = 4L;
/*     */     private final List<? extends Dynamic<?>> palette;
/*     */     private final long[] data;
/*     */     private final int bits;
/*     */     private final long mask;
/*     */     private final int valuesPerLong;
/*     */     
/*     */     public PoorMansPalettedContainer(List<? extends Dynamic<?>> param1List, long[] param1ArrayOflong) {
/* 168 */       this.palette = param1List;
/* 169 */       this.data = param1ArrayOflong;
/*     */       
/* 171 */       this.bits = Math.max(4, ChunkHeightAndBiomeFix.ceillog2(param1List.size()));
/* 172 */       this.mask = (1L << this.bits) - 1L;
/* 173 */       this.valuesPerLong = (char)(64 / this.bits);
/*     */     }
/*     */     
/*     */     public Dynamic<?> get(int param1Int1, int param1Int2, int param1Int3) {
/* 177 */       int i = this.palette.size();
/* 178 */       if (i < 1) {
/* 179 */         return null;
/*     */       }
/* 181 */       if (i == 1) {
/* 182 */         return this.palette.getFirst();
/*     */       }
/*     */       
/* 185 */       int j = getIndex(param1Int1, param1Int2, param1Int3);
/* 186 */       int k = j / this.valuesPerLong;
/* 187 */       if (k < 0 || k >= this.data.length) {
/* 188 */         return null;
/*     */       }
/* 190 */       long l = this.data[k];
/* 191 */       int m = (j - k * this.valuesPerLong) * this.bits;
/* 192 */       int n = (int)(l >> m & this.mask);
/* 193 */       if (n < 0 || n >= i) {
/* 194 */         return null;
/*     */       }
/* 196 */       return this.palette.get(n);
/*     */     }
/*     */     
/*     */     private int getIndex(int param1Int1, int param1Int2, int param1Int3) {
/* 200 */       return (param1Int2 << 4 | param1Int3) << 4 | param1Int1;
/*     */     }
/*     */     
/*     */     public List<? extends Dynamic<?>> palette() {
/* 204 */       return this.palette;
/*     */     }
/*     */     
/*     */     public long[] data() {
/* 208 */       return this.data;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\ChunkProtoTickListFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */