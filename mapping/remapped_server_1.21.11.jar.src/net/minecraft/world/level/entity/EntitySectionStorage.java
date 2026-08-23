/*     */ package net.minecraft.world.level.entity;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectFunction;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
/*     */ import it.unimi.dsi.fastutil.longs.LongBidirectionalIterator;
/*     */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import it.unimi.dsi.fastutil.longs.LongSortedSet;
/*     */ import java.util.Objects;
/*     */ import java.util.PrimitiveIterator;
/*     */ import java.util.Spliterators;
/*     */ import java.util.stream.LongStream;
/*     */ import java.util.stream.Stream;
/*     */ import java.util.stream.StreamSupport;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.util.AbortableIterationConsumer;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntitySectionStorage<T extends EntityAccess>
/*     */ {
/*     */   public static final int CHONKY_ENTITY_SEARCH_GRACE = 2;
/*     */   public static final int MAX_NON_CHONKY_ENTITY_SIZE = 4;
/*     */   private final Class<T> entityClass;
/*     */   private final Long2ObjectFunction<Visibility> intialSectionVisibility;
/*  32 */   private final Long2ObjectMap<EntitySection<T>> sections = (Long2ObjectMap<EntitySection<T>>)new Long2ObjectOpenHashMap();
/*     */ 
/*     */   
/*  35 */   private final LongSortedSet sectionIds = (LongSortedSet)new LongAVLTreeSet();
/*     */   
/*     */   public EntitySectionStorage(Class<T> paramClass, Long2ObjectFunction<Visibility> paramLong2ObjectFunction) {
/*  38 */     this.entityClass = paramClass;
/*  39 */     this.intialSectionVisibility = paramLong2ObjectFunction;
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEachAccessibleNonEmptySection(AABB paramAABB, AbortableIterationConsumer<EntitySection<T>> paramAbortableIterationConsumer) {
/*  44 */     int i = SectionPos.posToSectionCoord(paramAABB.minX - 2.0D);
/*  45 */     int j = SectionPos.posToSectionCoord(paramAABB.minY - 4.0D);
/*  46 */     int k = SectionPos.posToSectionCoord(paramAABB.minZ - 2.0D);
/*     */     
/*  48 */     int m = SectionPos.posToSectionCoord(paramAABB.maxX + 2.0D);
/*  49 */     int n = SectionPos.posToSectionCoord(paramAABB.maxY + 0.0D);
/*  50 */     int i1 = SectionPos.posToSectionCoord(paramAABB.maxZ + 2.0D);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  55 */     for (int i2 = i; i2 <= m; i2++) {
/*  56 */       long l1 = SectionPos.asLong(i2, 0, 0);
/*  57 */       long l2 = SectionPos.asLong(i2, -1, -1);
/*  58 */       LongBidirectionalIterator longBidirectionalIterator = this.sectionIds.subSet(l1, l2 + 1L).iterator();
/*  59 */       while (longBidirectionalIterator.hasNext()) {
/*  60 */         long l = longBidirectionalIterator.nextLong();
/*  61 */         int i3 = SectionPos.y(l);
/*  62 */         int i4 = SectionPos.z(l);
/*  63 */         if (i3 >= j && i3 <= n && i4 >= k && i4 <= i1) {
/*  64 */           EntitySection entitySection = (EntitySection)this.sections.get(l);
/*  65 */           if (entitySection != null && !entitySection.isEmpty() && entitySection.getStatus().isAccessible() && 
/*  66 */             paramAbortableIterationConsumer.accept(entitySection).shouldAbort()) {
/*     */             return;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public LongStream getExistingSectionPositionsInChunk(long paramLong) {
/*  76 */     int i = ChunkPos.getX(paramLong);
/*  77 */     int j = ChunkPos.getZ(paramLong);
/*  78 */     LongSortedSet longSortedSet = getChunkSections(i, j);
/*  79 */     if (longSortedSet.isEmpty()) {
/*  80 */       return LongStream.empty();
/*     */     }
/*  82 */     LongBidirectionalIterator longBidirectionalIterator = longSortedSet.iterator();
/*  83 */     return StreamSupport.longStream(Spliterators.spliteratorUnknownSize((PrimitiveIterator.OfLong)longBidirectionalIterator, 1301), false);
/*     */   }
/*     */   
/*     */   private LongSortedSet getChunkSections(int paramInt1, int paramInt2) {
/*  87 */     long l1 = SectionPos.asLong(paramInt1, 0, paramInt2);
/*  88 */     long l2 = SectionPos.asLong(paramInt1, -1, paramInt2);
/*  89 */     return this.sectionIds.subSet(l1, l2 + 1L);
/*     */   }
/*     */   
/*     */   public Stream<EntitySection<T>> getExistingSectionsInChunk(long paramLong) {
/*  93 */     Objects.requireNonNull(this.sections); return getExistingSectionPositionsInChunk(paramLong).<EntitySection<T>>mapToObj(this.sections::get).filter(Objects::nonNull);
/*     */   }
/*     */   
/*     */   private static long getChunkKeyFromSectionKey(long paramLong) {
/*  97 */     return ChunkPos.asLong(SectionPos.x(paramLong), SectionPos.z(paramLong));
/*     */   }
/*     */   
/*     */   public EntitySection<T> getOrCreateSection(long paramLong) {
/* 101 */     return (EntitySection<T>)this.sections.computeIfAbsent(paramLong, this::createSection);
/*     */   }
/*     */   
/*     */   public EntitySection<T> getSection(long paramLong) {
/* 105 */     return (EntitySection<T>)this.sections.get(paramLong);
/*     */   }
/*     */   
/*     */   private EntitySection<T> createSection(long paramLong) {
/* 109 */     long l = getChunkKeyFromSectionKey(paramLong);
/* 110 */     Visibility visibility = (Visibility)this.intialSectionVisibility.get(l);
/* 111 */     this.sectionIds.add(paramLong);
/* 112 */     return new EntitySection<>(this.entityClass, visibility);
/*     */   }
/*     */   
/*     */   public LongSet getAllChunksWithExistingSections() {
/* 116 */     LongOpenHashSet longOpenHashSet = new LongOpenHashSet();
/* 117 */     this.sections.keySet().forEach(paramLong -> paramLongSet.add(getChunkKeyFromSectionKey(paramLong)));
/* 118 */     return (LongSet)longOpenHashSet;
/*     */   }
/*     */   
/*     */   public void getEntities(AABB paramAABB, AbortableIterationConsumer<T> paramAbortableIterationConsumer) {
/* 122 */     forEachAccessibleNonEmptySection(paramAABB, paramEntitySection -> paramEntitySection.getEntities(paramAABB, paramAbortableIterationConsumer));
/*     */   }
/*     */   
/*     */   public <U extends T> void getEntities(EntityTypeTest<T, U> paramEntityTypeTest, AABB paramAABB, AbortableIterationConsumer<U> paramAbortableIterationConsumer) {
/* 126 */     forEachAccessibleNonEmptySection(paramAABB, paramEntitySection -> paramEntitySection.getEntities(paramEntityTypeTest, paramAABB, paramAbortableIterationConsumer));
/*     */   }
/*     */   
/*     */   public void remove(long paramLong) {
/* 130 */     this.sections.remove(paramLong);
/* 131 */     this.sectionIds.remove(paramLong);
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public int count() {
/* 136 */     return this.sectionIds.size();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\entity\EntitySectionStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */