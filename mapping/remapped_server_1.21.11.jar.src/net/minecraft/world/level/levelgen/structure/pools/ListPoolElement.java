/*     */ package net.minecraft.world.level.levelgen.structure.pools;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ 
/*     */ public class ListPoolElement extends StructurePoolElement {
/*     */   static {
/*  24 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)StructurePoolElement.CODEC.listOf().fieldOf("elements").forGetter(()), (App)projectionCodec()).apply((Applicative)paramInstance, ListPoolElement::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final MapCodec<ListPoolElement> CODEC;
/*     */   private final List<StructurePoolElement> elements;
/*     */   
/*     */   public ListPoolElement(List<StructurePoolElement> paramList, StructureTemplatePool.Projection paramProjection) {
/*  32 */     super(paramProjection);
/*  33 */     if (paramList.isEmpty()) {
/*  34 */       throw new IllegalArgumentException("Elements are empty");
/*     */     }
/*  36 */     this.elements = paramList;
/*  37 */     setProjectionOnEachElement(paramProjection);
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3i getSize(StructureTemplateManager paramStructureTemplateManager, Rotation paramRotation) {
/*  42 */     int i = 0;
/*  43 */     int j = 0;
/*  44 */     int k = 0;
/*  45 */     for (StructurePoolElement structurePoolElement : this.elements) {
/*  46 */       Vec3i vec3i = structurePoolElement.getSize(paramStructureTemplateManager, paramRotation);
/*  47 */       i = Math.max(i, vec3i.getX());
/*  48 */       j = Math.max(j, vec3i.getY());
/*  49 */       k = Math.max(k, vec3i.getZ());
/*     */     } 
/*     */     
/*  52 */     return new Vec3i(i, j, k);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<StructureTemplate.JigsawBlockInfo> getShuffledJigsawBlocks(StructureTemplateManager paramStructureTemplateManager, BlockPos paramBlockPos, Rotation paramRotation, RandomSource paramRandomSource) {
/*  57 */     return ((StructurePoolElement)this.elements.get(0)).getShuffledJigsawBlocks(paramStructureTemplateManager, paramBlockPos, paramRotation, paramRandomSource);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BoundingBox getBoundingBox(StructureTemplateManager paramStructureTemplateManager, BlockPos paramBlockPos, Rotation paramRotation) {
/*  64 */     Stream stream = this.elements.stream().filter(paramStructurePoolElement -> (paramStructurePoolElement != EmptyPoolElement.INSTANCE)).map(paramStructurePoolElement -> paramStructurePoolElement.getBoundingBox(paramStructureTemplateManager, paramBlockPos, paramRotation));
/*     */     
/*  66 */     Objects.requireNonNull(stream); return (BoundingBox)BoundingBox.encapsulatingBoxes(stream::iterator).orElseThrow(() -> new IllegalStateException("Unable to calculate boundingbox for ListPoolElement"));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean place(StructureTemplateManager paramStructureTemplateManager, WorldGenLevel paramWorldGenLevel, StructureManager paramStructureManager, ChunkGenerator paramChunkGenerator, BlockPos paramBlockPos1, BlockPos paramBlockPos2, Rotation paramRotation, BoundingBox paramBoundingBox, RandomSource paramRandomSource, LiquidSettings paramLiquidSettings, boolean paramBoolean) {
/*  71 */     for (StructurePoolElement structurePoolElement : this.elements) {
/*  72 */       if (!structurePoolElement.place(paramStructureTemplateManager, paramWorldGenLevel, paramStructureManager, paramChunkGenerator, paramBlockPos1, paramBlockPos2, paramRotation, paramBoundingBox, paramRandomSource, paramLiquidSettings, paramBoolean)) {
/*  73 */         return false;
/*     */       }
/*     */     } 
/*  76 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public StructurePoolElementType<?> getType() {
/*  81 */     return StructurePoolElementType.LIST;
/*     */   }
/*     */ 
/*     */   
/*     */   public StructurePoolElement setProjection(StructureTemplatePool.Projection paramProjection) {
/*  86 */     super.setProjection(paramProjection);
/*  87 */     setProjectionOnEachElement(paramProjection);
/*  88 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  93 */     return "List[" + (String)this.elements.stream().map(Object::toString).collect(Collectors.joining(", ")) + "]";
/*     */   }
/*     */   
/*     */   private void setProjectionOnEachElement(StructureTemplatePool.Projection paramProjection) {
/*  97 */     this.elements.forEach(paramStructurePoolElement -> paramStructurePoolElement.setProjection(paramProjection));
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public List<StructurePoolElement> getElements() {
/* 102 */     return this.elements;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\pools\ListPoolElement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */