/*     */ package net.minecraft.world.level.levelgen.structure.pools;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.data.worldgen.Pools;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.SequencedPriorityIterator;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.block.JigsawBlock;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.RandomState;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasLookup;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ import org.apache.commons.lang3.mutable.MutableObject;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class Placer
/*     */ {
/*     */   private final Registry<StructureTemplatePool> pools;
/*     */   private final int maxDepth;
/*     */   private final ChunkGenerator chunkGenerator;
/*     */   private final StructureTemplateManager structureTemplateManager;
/*     */   private final List<? super PoolElementStructurePiece> pieces;
/*     */   private final RandomSource random;
/*  62 */   final SequencedPriorityIterator<JigsawPlacement.PieceState> placing = new SequencedPriorityIterator();
/*     */   
/*     */   Placer(Registry<StructureTemplatePool> paramRegistry, int paramInt, ChunkGenerator paramChunkGenerator, StructureTemplateManager paramStructureTemplateManager, List<? super PoolElementStructurePiece> paramList, RandomSource paramRandomSource) {
/*  65 */     this.pools = paramRegistry;
/*  66 */     this.maxDepth = paramInt;
/*  67 */     this.chunkGenerator = paramChunkGenerator;
/*  68 */     this.structureTemplateManager = paramStructureTemplateManager;
/*  69 */     this.pieces = paramList;
/*  70 */     this.random = paramRandomSource;
/*     */   }
/*     */   
/*     */   void tryPlacingChildren(PoolElementStructurePiece paramPoolElementStructurePiece, MutableObject<VoxelShape> paramMutableObject, int paramInt, boolean paramBoolean, LevelHeightAccessor paramLevelHeightAccessor, RandomState paramRandomState, PoolAliasLookup paramPoolAliasLookup, LiquidSettings paramLiquidSettings) {
/*  74 */     StructurePoolElement structurePoolElement = paramPoolElementStructurePiece.getElement();
/*  75 */     BlockPos blockPos = paramPoolElementStructurePiece.getPosition();
/*  76 */     Rotation rotation = paramPoolElementStructurePiece.getRotation();
/*     */     
/*  78 */     StructureTemplatePool.Projection projection = structurePoolElement.getProjection();
/*  79 */     boolean bool = (projection == StructureTemplatePool.Projection.RIGID) ? true : false;
/*     */     
/*  81 */     MutableObject mutableObject = new MutableObject();
/*     */     
/*  83 */     BoundingBox boundingBox = paramPoolElementStructurePiece.getBoundingBox();
/*  84 */     int i = boundingBox.minY();
/*     */     
/*  86 */     for (StructureTemplate.JigsawBlockInfo jigsawBlockInfo : structurePoolElement.getShuffledJigsawBlocks(this.structureTemplateManager, blockPos, rotation, this.random)) {
/*  87 */       MutableObject<VoxelShape> mutableObject1; StructureTemplate.StructureBlockInfo structureBlockInfo = jigsawBlockInfo.info();
/*  88 */       Direction direction = JigsawBlock.getFrontFacing(structureBlockInfo.state());
/*     */       
/*  90 */       BlockPos blockPos1 = structureBlockInfo.pos();
/*  91 */       BlockPos blockPos2 = blockPos1.relative(direction);
/*     */       
/*  93 */       int j = blockPos1.getY() - i;
/*  94 */       int k = Integer.MIN_VALUE;
/*     */       
/*  96 */       ResourceKey resourceKey = paramPoolAliasLookup.lookup(jigsawBlockInfo.pool());
/*  97 */       Optional<Holder> optional = this.pools.get(resourceKey);
/*     */       
/*  99 */       if (optional.isEmpty()) {
/* 100 */         JigsawPlacement.LOGGER.warn("Empty or non-existent pool: {}", resourceKey.identifier());
/*     */         
/*     */         continue;
/*     */       } 
/* 104 */       Holder holder = optional.get();
/* 105 */       if (((StructureTemplatePool)holder.value()).size() == 0 && !holder.is(Pools.EMPTY)) {
/* 106 */         JigsawPlacement.LOGGER.warn("Empty or non-existent pool: {}", resourceKey.identifier());
/*     */         
/*     */         continue;
/*     */       } 
/* 110 */       Holder<StructureTemplatePool> holder1 = ((StructureTemplatePool)holder.value()).getFallback();
/*     */       
/* 112 */       if (((StructureTemplatePool)holder1.value()).size() == 0 && !holder1.is(Pools.EMPTY)) {
/* 113 */         JigsawPlacement.LOGGER.warn("Empty or non-existent fallback pool: {}", holder1.unwrapKey().map(paramResourceKey -> paramResourceKey.identifier().toString()).orElse("<unregistered>"));
/*     */ 
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 119 */       boolean bool1 = boundingBox.isInside((Vec3i)blockPos2);
/* 120 */       if (bool1) {
/* 121 */         mutableObject1 = mutableObject;
/* 122 */         if (mutableObject.get() == null) {
/* 123 */           mutableObject.setValue(Shapes.create(AABB.of(boundingBox)));
/*     */         }
/*     */       } else {
/* 126 */         mutableObject1 = paramMutableObject;
/*     */       } 
/*     */ 
/*     */       
/* 130 */       ArrayList<StructurePoolElement> arrayList = Lists.newArrayList();
/* 131 */       if (paramInt != this.maxDepth) {
/* 132 */         arrayList.addAll(((StructureTemplatePool)holder.value()).getShuffledTemplates(this.random));
/*     */       }
/* 134 */       arrayList.addAll(((StructureTemplatePool)holder1.value()).getShuffledTemplates(this.random));
/* 135 */       int m = jigsawBlockInfo.placementPriority();
/*     */ 
/*     */       
/* 138 */       for (StructurePoolElement structurePoolElement1 : arrayList) {
/* 139 */         if (structurePoolElement1 == EmptyPoolElement.INSTANCE) {
/*     */           break;
/*     */         }
/*     */         
/* 143 */         for (Rotation rotation1 : Rotation.getShuffled(this.random)) {
/* 144 */           int n; List<StructureTemplate.JigsawBlockInfo> list = structurePoolElement1.getShuffledJigsawBlocks(this.structureTemplateManager, BlockPos.ZERO, rotation1, this.random);
/* 145 */           BoundingBox boundingBox1 = structurePoolElement1.getBoundingBox(this.structureTemplateManager, BlockPos.ZERO, rotation1);
/*     */ 
/*     */           
/* 148 */           if (!paramBoolean || boundingBox1.getYSpan() > 16) {
/* 149 */             n = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           }
/*     */           else {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 162 */             n = list.stream().mapToInt(paramJigsawBlockInfo -> { StructureTemplate.StructureBlockInfo structureBlockInfo = paramJigsawBlockInfo.info(); if (!paramBoundingBox.isInside((Vec3i)structureBlockInfo.pos().relative(JigsawBlock.getFrontFacing(structureBlockInfo.state())))) return 0;  ResourceKey resourceKey = paramPoolAliasLookup.lookup(paramJigsawBlockInfo.pool()); Optional optional1 = this.pools.get(resourceKey); Optional optional2 = optional1.map(()); int i = ((Integer)optional1.map(()).orElse(Integer.valueOf(0))).intValue(); int j = ((Integer)optional2.map(()).orElse(Integer.valueOf(0))).intValue(); return Math.max(i, j); }).max().orElse(0);
/*     */           } 
/*     */           
/* 165 */           for (StructureTemplate.JigsawBlockInfo jigsawBlockInfo1 : list) {
/* 166 */             int i4, i7, i8; if (!JigsawBlock.canAttach(jigsawBlockInfo, jigsawBlockInfo1)) {
/*     */               continue;
/*     */             }
/*     */             
/* 170 */             BlockPos blockPos3 = jigsawBlockInfo1.info().pos();
/*     */             
/* 172 */             BlockPos blockPos4 = blockPos2.subtract((Vec3i)blockPos3);
/* 173 */             BoundingBox boundingBox2 = structurePoolElement1.getBoundingBox(this.structureTemplateManager, blockPos4, rotation1);
/* 174 */             int i1 = boundingBox2.minY();
/*     */             
/* 176 */             StructureTemplatePool.Projection projection1 = structurePoolElement1.getProjection();
/* 177 */             boolean bool2 = (projection1 == StructureTemplatePool.Projection.RIGID) ? true : false;
/*     */ 
/*     */             
/* 180 */             int i2 = blockPos3.getY();
/*     */             
/* 182 */             int i3 = j - i2 + JigsawBlock.getFrontFacing(structureBlockInfo.state()).getStepY();
/*     */ 
/*     */             
/* 185 */             if (bool && bool2) {
/* 186 */               i4 = i + i3;
/*     */             } else {
/* 188 */               if (k == Integer.MIN_VALUE) {
/* 189 */                 k = this.chunkGenerator.getFirstFreeHeight(blockPos1.getX(), blockPos1.getZ(), Heightmap.Types.WORLD_SURFACE_WG, paramLevelHeightAccessor, paramRandomState);
/*     */               }
/* 191 */               i4 = k - i2;
/*     */             } 
/*     */             
/* 194 */             int i5 = i4 - i1;
/*     */             
/* 196 */             BoundingBox boundingBox3 = boundingBox2.moved(0, i5, 0);
/* 197 */             BlockPos blockPos5 = blockPos4.offset(0, i5, 0);
/*     */             
/* 199 */             if (n > 0) {
/* 200 */               int i9 = Math.max(n + 1, boundingBox3.maxY() - boundingBox3.minY());
/* 201 */               boundingBox3.encapsulate(new BlockPos(boundingBox3.minX(), boundingBox3.minY() + i9, boundingBox3.minZ()));
/*     */             } 
/*     */ 
/*     */ 
/*     */             
/* 206 */             if (Shapes.joinIsNotEmpty((VoxelShape)mutableObject1.get(), Shapes.create(AABB.of(boundingBox3).deflate(0.25D)), BooleanOp.ONLY_SECOND)) {
/*     */               continue;
/*     */             }
/*     */             
/* 210 */             mutableObject1.setValue(Shapes.joinUnoptimized((VoxelShape)mutableObject1.get(), Shapes.create(AABB.of(boundingBox3)), BooleanOp.ONLY_FIRST));
/*     */             
/* 212 */             int i6 = paramPoolElementStructurePiece.getGroundLevelDelta();
/*     */             
/* 214 */             if (bool2) {
/*     */               
/* 216 */               i7 = i6 - i3;
/*     */             } else {
/* 218 */               i7 = structurePoolElement1.getGroundLevelDelta();
/*     */             } 
/*     */             
/* 221 */             PoolElementStructurePiece poolElementStructurePiece = new PoolElementStructurePiece(this.structureTemplateManager, structurePoolElement1, blockPos5, i7, rotation1, boundingBox3, paramLiquidSettings);
/*     */ 
/*     */             
/* 224 */             if (bool) {
/* 225 */               i8 = i + j;
/* 226 */             } else if (bool2) {
/* 227 */               i8 = i4 + i2;
/*     */             } else {
/* 229 */               if (k == Integer.MIN_VALUE) {
/* 230 */                 k = this.chunkGenerator.getFirstFreeHeight(blockPos1.getX(), blockPos1.getZ(), Heightmap.Types.WORLD_SURFACE_WG, paramLevelHeightAccessor, paramRandomState);
/*     */               }
/* 232 */               i8 = k + i3 / 2;
/*     */             } 
/*     */             
/* 235 */             paramPoolElementStructurePiece.addJunction(new JigsawJunction(blockPos2
/* 236 */                   .getX(), i8 - j + i6, blockPos2
/*     */                   
/* 238 */                   .getZ(), i3, projection1));
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 243 */             poolElementStructurePiece.addJunction(new JigsawJunction(blockPos1
/* 244 */                   .getX(), i8 - i2 + i7, blockPos1
/*     */                   
/* 246 */                   .getZ(), -i3, projection));
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 251 */             this.pieces.add(poolElementStructurePiece);
/* 252 */             if (paramInt + 1 <= this.maxDepth) {
/* 253 */               JigsawPlacement.PieceState pieceState = new JigsawPlacement.PieceState(poolElementStructurePiece, mutableObject1, paramInt + 1);
/* 254 */               this.placing.add(pieceState, m);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\pools\JigsawPlacement$Placer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */