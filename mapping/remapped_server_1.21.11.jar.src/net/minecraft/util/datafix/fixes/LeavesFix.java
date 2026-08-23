/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Lists;
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
/*     */ import it.unimi.dsi.fastutil.ints.Int2IntMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.ints.IntIterator;
/*     */ import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.ints.IntSet;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.util.datafix.PackedBitStorage;
/*     */ 
/*     */ 
/*     */ public class LeavesFix
/*     */   extends DataFix
/*     */ {
/*     */   private static final int NORTH_WEST_MASK = 128;
/*     */   private static final int WEST_MASK = 64;
/*     */   private static final int SOUTH_WEST_MASK = 32;
/*     */   private static final int SOUTH_MASK = 16;
/*  45 */   private static final int[][] DIRECTIONS = new int[][] { { -1, 0, 0 }, { 1, 0, 0 }, { 0, -1, 0 }, { 0, 1, 0 }, { 0, 0, -1 }, { 0, 0, 1 } };
/*     */   
/*     */   private static final int SOUTH_EAST_MASK = 8;
/*     */   
/*     */   private static final int EAST_MASK = 4;
/*     */   private static final int NORTH_EAST_MASK = 2;
/*     */   private static final int NORTH_MASK = 1;
/*     */   private static final int DECAY_DISTANCE = 7;
/*     */   private static final int SIZE_BITS = 12;
/*     */   private static final int SIZE = 4096;
/*     */   static final Object2IntMap<String> LEAVES;
/*     */   
/*     */   static {
/*  58 */     LEAVES = (Object2IntMap<String>)DataFixUtils.make(new Object2IntOpenHashMap(), paramObject2IntOpenHashMap -> {
/*     */           paramObject2IntOpenHashMap.put("minecraft:acacia_leaves", 0);
/*     */           paramObject2IntOpenHashMap.put("minecraft:birch_leaves", 1);
/*     */           paramObject2IntOpenHashMap.put("minecraft:dark_oak_leaves", 2);
/*     */           paramObject2IntOpenHashMap.put("minecraft:jungle_leaves", 3);
/*     */           paramObject2IntOpenHashMap.put("minecraft:oak_leaves", 4);
/*     */           paramObject2IntOpenHashMap.put("minecraft:spruce_leaves", 5);
/*     */         });
/*     */   }
/*  67 */   static final Set<String> LOGS = (Set<String>)ImmutableSet.of("minecraft:acacia_bark", "minecraft:birch_bark", "minecraft:dark_oak_bark", "minecraft:jungle_bark", "minecraft:oak_bark", "minecraft:spruce_bark", (Object[])new String[] { "minecraft:acacia_log", "minecraft:birch_log", "minecraft:dark_oak_log", "minecraft:jungle_log", "minecraft:oak_log", "minecraft:spruce_log", "minecraft:stripped_acacia_log", "minecraft:stripped_birch_log", "minecraft:stripped_dark_oak_log", "minecraft:stripped_jungle_log", "minecraft:stripped_oak_log", "minecraft:stripped_spruce_log" });
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
/*     */   public LeavesFix(Schema paramSchema, boolean paramBoolean) {
/*  89 */     super(paramSchema, paramBoolean);
/*     */   }
/*     */ 
/*     */   
/*     */   protected TypeRewriteRule makeRule() {
/*  94 */     Type type1 = getInputSchema().getType(References.CHUNK);
/*     */     
/*  96 */     OpticFinder opticFinder1 = type1.findField("Level");
/*  97 */     OpticFinder opticFinder2 = opticFinder1.type().findField("Sections");
/*  98 */     Type type2 = opticFinder2.type();
/*  99 */     if (!(type2 instanceof List.ListType)) {
/* 100 */       throw new IllegalStateException("Expecting sections to be a list.");
/*     */     }
/* 102 */     Type type3 = ((List.ListType)type2).getElement();
/* 103 */     OpticFinder opticFinder3 = DSL.typeFinder(type3);
/*     */     
/* 105 */     return fixTypeEverywhereTyped("Leaves fix", type1, paramTyped -> paramTyped.updateTyped(paramOpticFinder1, ()));
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
/*     */   public static abstract class Section
/*     */   {
/*     */     protected static final String BLOCK_STATES_TAG = "BlockStates";
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
/*     */     protected static final String NAME_TAG = "Name";
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
/*     */     protected static final String PROPERTIES_TAG = "Properties";
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
/* 193 */     private final Type<Pair<String, Dynamic<?>>> blockStateType = DSL.named(References.BLOCK_STATE.typeName(), DSL.remainderType());
/* 194 */     protected final OpticFinder<List<Pair<String, Dynamic<?>>>> paletteFinder = DSL.fieldFinder("Palette", (Type)DSL.list(this.blockStateType));
/*     */     
/*     */     protected final List<Dynamic<?>> palette;
/*     */     protected final int index;
/*     */     protected PackedBitStorage storage;
/*     */     
/*     */     public Section(Typed<?> param1Typed, Schema param1Schema) {
/* 201 */       if (!Objects.equals(param1Schema.getType(References.BLOCK_STATE), this.blockStateType)) {
/* 202 */         throw new IllegalStateException("Block state type is not what was expected.");
/*     */       }
/*     */       
/* 205 */       Optional optional = param1Typed.getOptional(this.paletteFinder);
/*     */       
/* 207 */       this.palette = (List<Dynamic<?>>)optional.map(param1List -> (List)param1List.stream().map(Pair::getSecond).collect(Collectors.toList())).orElse(ImmutableList.of());
/*     */       
/* 209 */       Dynamic<?> dynamic = (Dynamic)param1Typed.get(DSL.remainderFinder());
/* 210 */       this.index = dynamic.get("Y").asInt(0);
/*     */       
/* 212 */       readStorage(dynamic);
/*     */     }
/*     */     
/*     */     protected void readStorage(Dynamic<?> param1Dynamic) {
/* 216 */       if (skippable()) {
/* 217 */         this.storage = null;
/*     */       } else {
/* 219 */         long[] arrayOfLong = param1Dynamic.get("BlockStates").asLongStream().toArray();
/* 220 */         int i = Math.max(4, DataFixUtils.ceillog2(this.palette.size()));
/* 221 */         this.storage = new PackedBitStorage(i, 4096, arrayOfLong);
/*     */       } 
/*     */     }
/*     */     
/*     */     public Typed<?> write(Typed<?> param1Typed) {
/* 226 */       if (isSkippable()) {
/* 227 */         return param1Typed;
/*     */       }
/* 229 */       return param1Typed
/* 230 */         .update(DSL.remainderFinder(), param1Dynamic -> param1Dynamic.set("BlockStates", param1Dynamic.createLongList(Arrays.stream(this.storage.getRaw()))))
/* 231 */         .set(this.paletteFinder, this.palette.stream().map(param1Dynamic -> Pair.of(References.BLOCK_STATE.typeName(), param1Dynamic)).collect(Collectors.toList()));
/*     */     }
/*     */     
/*     */     public boolean isSkippable() {
/* 235 */       return (this.storage == null);
/*     */     }
/*     */     
/*     */     public int getBlock(int param1Int) {
/* 239 */       return this.storage.get(param1Int);
/*     */     }
/*     */     
/*     */     protected int getStateId(String param1String, boolean param1Boolean, int param1Int) {
/* 243 */       return LeavesFix.LEAVES.get(param1String).intValue() << 5 | (param1Boolean ? 16 : 0) | param1Int;
/*     */     }
/*     */     
/*     */     int getIndex() {
/* 247 */       return this.index;
/*     */     }
/*     */     
/*     */     protected abstract boolean skippable();
/*     */   }
/*     */   
/*     */   public static final class LeavesSection
/*     */     extends Section
/*     */   {
/*     */     private static final String PERSISTENT = "persistent";
/*     */     private static final String DECAYABLE = "decayable";
/*     */     private static final String DISTANCE = "distance";
/*     */     private IntSet leaveIds;
/*     */     private IntSet logIds;
/*     */     private Int2IntMap stateToIdMap;
/*     */     
/*     */     public LeavesSection(Typed<?> param1Typed, Schema param1Schema) {
/* 264 */       super(param1Typed, param1Schema);
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean skippable() {
/* 269 */       this.leaveIds = (IntSet)new IntOpenHashSet();
/* 270 */       this.logIds = (IntSet)new IntOpenHashSet();
/* 271 */       this.stateToIdMap = (Int2IntMap)new Int2IntOpenHashMap();
/*     */       
/* 273 */       for (byte b = 0; b < this.palette.size(); b++) {
/* 274 */         Dynamic<?> dynamic = this.palette.get(b);
/* 275 */         String str = dynamic.get("Name").asString("");
/* 276 */         if (LeavesFix.LEAVES.containsKey(str)) {
/* 277 */           boolean bool = Objects.equals(dynamic.get("Properties").get("decayable").asString(""), "false");
/* 278 */           this.leaveIds.add(b);
/* 279 */           this.stateToIdMap.put(getStateId(str, bool, 7), b);
/* 280 */           this.palette.set(b, makeLeafTag(dynamic, str, bool, 7));
/*     */         } 
/* 282 */         if (LeavesFix.LOGS.contains(str)) {
/* 283 */           this.logIds.add(b);
/*     */         }
/*     */       } 
/*     */       
/* 287 */       return (this.leaveIds.isEmpty() && this.logIds.isEmpty());
/*     */     }
/*     */     
/*     */     private Dynamic<?> makeLeafTag(Dynamic<?> param1Dynamic, String param1String, boolean param1Boolean, int param1Int) {
/* 291 */       Dynamic dynamic = param1Dynamic.emptyMap();
/* 292 */       dynamic = dynamic.set("persistent", dynamic.createString(param1Boolean ? "true" : "false"));
/* 293 */       dynamic = dynamic.set("distance", dynamic.createString(Integer.toString(param1Int)));
/*     */       
/* 295 */       Dynamic<?> dynamic1 = param1Dynamic.emptyMap();
/* 296 */       dynamic1 = dynamic1.set("Properties", dynamic);
/* 297 */       dynamic1 = dynamic1.set("Name", dynamic1.createString(param1String));
/* 298 */       return dynamic1;
/*     */     }
/*     */     
/*     */     public boolean isLog(int param1Int) {
/* 302 */       return this.logIds.contains(param1Int);
/*     */     }
/*     */     
/*     */     public boolean isLeaf(int param1Int) {
/* 306 */       return this.leaveIds.contains(param1Int);
/*     */     }
/*     */     
/*     */     int getDistance(int param1Int) {
/* 310 */       if (isLog(param1Int)) {
/* 311 */         return 0;
/*     */       }
/* 313 */       return Integer.parseInt(((Dynamic)this.palette.get(param1Int)).get("Properties").get("distance").asString(""));
/*     */     }
/*     */     
/*     */     void setDistance(int param1Int1, int param1Int2, int param1Int3) {
/* 317 */       Dynamic<?> dynamic = this.palette.get(param1Int2);
/* 318 */       String str = dynamic.get("Name").asString("");
/* 319 */       boolean bool = Objects.equals(dynamic.get("Properties").get("persistent").asString(""), "true");
/* 320 */       int i = getStateId(str, bool, param1Int3);
/*     */       
/* 322 */       if (!this.stateToIdMap.containsKey(i)) {
/* 323 */         int k = this.palette.size();
/* 324 */         this.leaveIds.add(k);
/* 325 */         this.stateToIdMap.put(i, k);
/* 326 */         this.palette.add(makeLeafTag(dynamic, str, bool, param1Int3));
/*     */       } 
/*     */       
/* 329 */       int j = this.stateToIdMap.get(i);
/* 330 */       if (1 << this.storage.getBits() <= j) {
/* 331 */         PackedBitStorage packedBitStorage = new PackedBitStorage(this.storage.getBits() + 1, 4096);
/* 332 */         for (byte b = 0; b < 'က'; b++) {
/* 333 */           packedBitStorage.set(b, this.storage.get(b));
/*     */         }
/* 335 */         this.storage = packedBitStorage;
/*     */       } 
/* 337 */       this.storage.set(param1Int1, j);
/*     */     }
/*     */   }
/*     */   
/*     */   public static int getIndex(int paramInt1, int paramInt2, int paramInt3) {
/* 342 */     return paramInt2 << 8 | paramInt3 << 4 | paramInt1;
/*     */   }
/*     */   
/*     */   private int getX(int paramInt) {
/* 346 */     return paramInt & 0xF;
/*     */   }
/*     */   
/*     */   private int getY(int paramInt) {
/* 350 */     return paramInt >> 8 & 0xFF;
/*     */   }
/*     */   
/*     */   private int getZ(int paramInt) {
/* 354 */     return paramInt >> 4 & 0xF;
/*     */   }
/*     */   
/*     */   public static int getSideMask(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4) {
/* 358 */     int i = 0;
/* 359 */     if (paramBoolean3) {
/* 360 */       if (paramBoolean2) {
/* 361 */         i |= 0x2;
/* 362 */       } else if (paramBoolean1) {
/* 363 */         i |= 0x80;
/*     */       } else {
/* 365 */         i |= 0x1;
/*     */       } 
/* 367 */     } else if (paramBoolean4) {
/* 368 */       if (paramBoolean1) {
/* 369 */         i |= 0x20;
/* 370 */       } else if (paramBoolean2) {
/* 371 */         i |= 0x8;
/*     */       } else {
/* 373 */         i |= 0x10;
/*     */       } 
/* 375 */     } else if (paramBoolean2) {
/* 376 */       i |= 0x4;
/* 377 */     } else if (paramBoolean1) {
/* 378 */       i |= 0x40;
/*     */     } 
/* 380 */     return i;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\LeavesFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */