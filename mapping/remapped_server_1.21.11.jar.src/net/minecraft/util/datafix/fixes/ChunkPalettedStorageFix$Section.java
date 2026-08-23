/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ import it.unimi.dsi.fastutil.ints.IntList;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.util.CrudeIncrementalIntIdentityHashBiMap;
/*     */ import net.minecraft.util.datafix.PackedBitStorage;
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
/*     */ class Section
/*     */ {
/* 377 */   private final CrudeIncrementalIntIdentityHashBiMap<Dynamic<?>> palette = CrudeIncrementalIntIdentityHashBiMap.create(32);
/*     */   
/*     */   private final List<Dynamic<?>> listTag;
/*     */   private final Dynamic<?> section;
/*     */   private final boolean hasData;
/* 382 */   final Int2ObjectMap<IntList> toFix = (Int2ObjectMap<IntList>)new Int2ObjectLinkedOpenHashMap();
/*     */   
/* 384 */   final IntList update = (IntList)new IntArrayList();
/*     */   public final int y;
/* 386 */   private final Set<Dynamic<?>> seen = Sets.newIdentityHashSet();
/* 387 */   private final int[] buffer = new int[4096];
/*     */   
/*     */   public Section(Dynamic<?> paramDynamic) {
/* 390 */     this.listTag = Lists.newArrayList();
/* 391 */     this.section = paramDynamic;
/* 392 */     this.y = paramDynamic.get("Y").asInt(0);
/* 393 */     this.hasData = paramDynamic.get("Blocks").result().isPresent();
/*     */   }
/*     */   
/*     */   public Dynamic<?> getBlock(int paramInt) {
/* 397 */     if (paramInt < 0 || paramInt > 4095) {
/* 398 */       return ChunkPalettedStorageFix.MappingConstants.AIR;
/*     */     }
/*     */     
/* 401 */     Dynamic<?> dynamic = (Dynamic)this.palette.byId(this.buffer[paramInt]);
/* 402 */     return (dynamic == null) ? ChunkPalettedStorageFix.MappingConstants.AIR : dynamic;
/*     */   }
/*     */   
/*     */   public void setBlock(int paramInt, Dynamic<?> paramDynamic) {
/* 406 */     if (this.seen.add(paramDynamic)) {
/* 407 */       this.listTag.add("%%FILTER_ME%%".equals(ChunkPalettedStorageFix.getName(paramDynamic)) ? ChunkPalettedStorageFix.MappingConstants.AIR : paramDynamic);
/*     */     }
/* 409 */     this.buffer[paramInt] = ChunkPalettedStorageFix.idFor(this.palette, paramDynamic);
/*     */   }
/*     */   
/*     */   public int upgrade(int paramInt) {
/* 413 */     if (!this.hasData) {
/* 414 */       return paramInt;
/*     */     }
/* 416 */     ByteBuffer byteBuffer = this.section.get("Blocks").asByteBufferOpt().result().get();
/* 417 */     ChunkPalettedStorageFix.DataLayer dataLayer1 = this.section.get("Data").asByteBufferOpt().map(paramByteBuffer -> new ChunkPalettedStorageFix.DataLayer(DataFixUtils.toArray(paramByteBuffer))).result().orElseGet(DataLayer::new);
/* 418 */     ChunkPalettedStorageFix.DataLayer dataLayer2 = this.section.get("Add").asByteBufferOpt().map(paramByteBuffer -> new ChunkPalettedStorageFix.DataLayer(DataFixUtils.toArray(paramByteBuffer))).result().orElseGet(DataLayer::new);
/*     */     
/* 420 */     this.seen.add(ChunkPalettedStorageFix.MappingConstants.AIR);
/* 421 */     ChunkPalettedStorageFix.idFor(this.palette, ChunkPalettedStorageFix.MappingConstants.AIR);
/* 422 */     this.listTag.add(ChunkPalettedStorageFix.MappingConstants.AIR);
/*     */     
/* 424 */     for (byte b = 0; b < 'က'; b++) {
/* 425 */       int i = b & 0xF;
/* 426 */       int j = b >> 8 & 0xF;
/* 427 */       int k = b >> 4 & 0xF;
/* 428 */       int m = dataLayer2.get(i, j, k) << 12 | (byteBuffer.get(b) & 0xFF) << 4 | dataLayer1.get(i, j, k);
/*     */       
/* 430 */       if (ChunkPalettedStorageFix.MappingConstants.FIX.get(m >> 4)) {
/* 431 */         addFix(m >> 4, b);
/*     */       }
/* 433 */       if (ChunkPalettedStorageFix.MappingConstants.VIRTUAL.get(m >> 4)) {
/*     */         
/* 435 */         int n = ChunkPalettedStorageFix.getSideMask((i == 0), (i == 15), (k == 0), (k == 15));
/* 436 */         if (n == 0) {
/*     */           
/* 438 */           this.update.add(b);
/*     */         } else {
/* 440 */           paramInt |= n;
/*     */         } 
/*     */       } 
/*     */       
/* 444 */       setBlock(b, BlockStateData.getTag(m));
/*     */     } 
/*     */     
/* 447 */     return paramInt;
/*     */   }
/*     */   private void addFix(int paramInt1, int paramInt2) {
/*     */     IntArrayList intArrayList;
/* 451 */     IntList intList = (IntList)this.toFix.get(paramInt1);
/* 452 */     if (intList == null) {
/* 453 */       intArrayList = new IntArrayList();
/* 454 */       this.toFix.put(paramInt1, intArrayList);
/*     */     } 
/* 456 */     intArrayList.add(paramInt2);
/*     */   }
/*     */   
/*     */   public Dynamic<?> write() {
/* 460 */     Dynamic<?> dynamic = this.section;
/* 461 */     if (!this.hasData) {
/* 462 */       return dynamic;
/*     */     }
/* 464 */     dynamic = dynamic.set("Palette", dynamic.createList(this.listTag.stream()));
/*     */     
/* 466 */     int i = Math.max(4, DataFixUtils.ceillog2(this.seen.size()));
/* 467 */     PackedBitStorage packedBitStorage = new PackedBitStorage(i, 4096);
/* 468 */     for (byte b = 0; b < this.buffer.length; b++) {
/* 469 */       packedBitStorage.set(b, this.buffer[b]);
/*     */     }
/*     */     
/* 472 */     dynamic = dynamic.set("BlockStates", dynamic.createLongList(Arrays.stream(packedBitStorage.getRaw())));
/*     */     
/* 474 */     dynamic = dynamic.remove("Blocks");
/* 475 */     dynamic = dynamic.remove("Data");
/* 476 */     dynamic = dynamic.remove("Add");
/*     */     
/* 478 */     return dynamic;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\ChunkPalettedStorageFix$Section.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */