/*     */ package net.minecraft.world.level.levelgen.structure.pools;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ 
/*     */ 
/*     */ public abstract class StructurePoolElement
/*     */ {
/*  31 */   public static final Codec<StructurePoolElement> CODEC = BuiltInRegistries.STRUCTURE_POOL_ELEMENT.byNameCodec().dispatch("element_type", StructurePoolElement::getType, StructurePoolElementType::codec);
/*     */   
/*  33 */   private static final Holder<StructureProcessorList> EMPTY = Holder.direct(new StructureProcessorList(List.of()));
/*     */   
/*     */   protected static <E extends StructurePoolElement> RecordCodecBuilder<E, StructureTemplatePool.Projection> projectionCodec() {
/*  36 */     return StructureTemplatePool.Projection.CODEC.fieldOf("projection").forGetter(StructurePoolElement::getProjection);
/*     */   }
/*     */   
/*     */   private volatile StructureTemplatePool.Projection projection;
/*     */   
/*     */   protected StructurePoolElement(StructureTemplatePool.Projection paramProjection) {
/*  42 */     this.projection = paramProjection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleDataMarker(LevelAccessor paramLevelAccessor, StructureTemplate.StructureBlockInfo paramStructureBlockInfo, BlockPos paramBlockPos, Rotation paramRotation, RandomSource paramRandomSource, BoundingBox paramBoundingBox) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StructurePoolElement setProjection(StructureTemplatePool.Projection paramProjection) {
/*  59 */     this.projection = paramProjection;
/*  60 */     return this;
/*     */   }
/*     */   
/*     */   public StructureTemplatePool.Projection getProjection() {
/*  64 */     StructureTemplatePool.Projection projection = this.projection;
/*  65 */     if (projection == null) {
/*  66 */       throw new IllegalStateException();
/*     */     }
/*  68 */     return projection;
/*     */   }
/*     */   
/*     */   public int getGroundLevelDelta() {
/*  72 */     return 1;
/*     */   }
/*     */   
/*     */   public static Function<StructureTemplatePool.Projection, EmptyPoolElement> empty() {
/*  76 */     return paramProjection -> EmptyPoolElement.INSTANCE;
/*     */   }
/*     */   
/*     */   public static Function<StructureTemplatePool.Projection, LegacySinglePoolElement> legacy(String paramString) {
/*  80 */     return paramProjection -> new LegacySinglePoolElement(Either.left(Identifier.parse(paramString)), EMPTY, paramProjection, Optional.empty());
/*     */   }
/*     */   
/*     */   public static Function<StructureTemplatePool.Projection, LegacySinglePoolElement> legacy(String paramString, Holder<StructureProcessorList> paramHolder) {
/*  84 */     return paramProjection -> new LegacySinglePoolElement(Either.left(Identifier.parse(paramString)), paramHolder, paramProjection, Optional.empty());
/*     */   }
/*     */   
/*     */   public static Function<StructureTemplatePool.Projection, SinglePoolElement> single(String paramString) {
/*  88 */     return paramProjection -> new SinglePoolElement(Either.left(Identifier.parse(paramString)), EMPTY, paramProjection, Optional.empty());
/*     */   }
/*     */   
/*     */   public static Function<StructureTemplatePool.Projection, SinglePoolElement> single(String paramString, Holder<StructureProcessorList> paramHolder) {
/*  92 */     return paramProjection -> new SinglePoolElement(Either.left(Identifier.parse(paramString)), paramHolder, paramProjection, Optional.empty());
/*     */   }
/*     */   
/*     */   public static Function<StructureTemplatePool.Projection, SinglePoolElement> single(String paramString, LiquidSettings paramLiquidSettings) {
/*  96 */     return paramProjection -> new SinglePoolElement(Either.left(Identifier.parse(paramString)), EMPTY, paramProjection, Optional.of(paramLiquidSettings));
/*     */   }
/*     */   
/*     */   public static Function<StructureTemplatePool.Projection, SinglePoolElement> single(String paramString, Holder<StructureProcessorList> paramHolder, LiquidSettings paramLiquidSettings) {
/* 100 */     return paramProjection -> new SinglePoolElement(Either.left(Identifier.parse(paramString)), paramHolder, paramProjection, Optional.of(paramLiquidSettings));
/*     */   }
/*     */   
/*     */   public static Function<StructureTemplatePool.Projection, FeaturePoolElement> feature(Holder<PlacedFeature> paramHolder) {
/* 104 */     return paramProjection -> new FeaturePoolElement(paramHolder, paramProjection);
/*     */   }
/*     */   
/*     */   public static Function<StructureTemplatePool.Projection, ListPoolElement> list(List<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>> paramList) {
/* 108 */     return paramProjection -> new ListPoolElement((List<StructurePoolElement>)paramList.stream().map(()).collect(Collectors.toList()), paramProjection);
/*     */   }
/*     */   
/*     */   public abstract Vec3i getSize(StructureTemplateManager paramStructureTemplateManager, Rotation paramRotation);
/*     */   
/*     */   public abstract List<StructureTemplate.JigsawBlockInfo> getShuffledJigsawBlocks(StructureTemplateManager paramStructureTemplateManager, BlockPos paramBlockPos, Rotation paramRotation, RandomSource paramRandomSource);
/*     */   
/*     */   public abstract BoundingBox getBoundingBox(StructureTemplateManager paramStructureTemplateManager, BlockPos paramBlockPos, Rotation paramRotation);
/*     */   
/*     */   public abstract boolean place(StructureTemplateManager paramStructureTemplateManager, WorldGenLevel paramWorldGenLevel, StructureManager paramStructureManager, ChunkGenerator paramChunkGenerator, BlockPos paramBlockPos1, BlockPos paramBlockPos2, Rotation paramRotation, BoundingBox paramBoundingBox, RandomSource paramRandomSource, LiquidSettings paramLiquidSettings, boolean paramBoolean);
/*     */   
/*     */   public abstract StructurePoolElementType<?> getType();
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\pools\StructurePoolElement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */