/*     */ package net.minecraft.world.level.levelgen.structure;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.JigsawJunction;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
/*     */ import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ 
/*     */ public class PoolElementStructurePiece extends StructurePiece {
/*     */   protected final StructurePoolElement element;
/*     */   protected BlockPos position;
/*     */   private final int groundLevelDelta;
/*     */   protected final Rotation rotation;
/*  33 */   private final List<JigsawJunction> junctions = Lists.newArrayList();
/*     */   private final StructureTemplateManager structureTemplateManager;
/*     */   private final LiquidSettings liquidSettings;
/*     */   
/*     */   public PoolElementStructurePiece(StructureTemplateManager paramStructureTemplateManager, StructurePoolElement paramStructurePoolElement, BlockPos paramBlockPos, int paramInt, Rotation paramRotation, BoundingBox paramBoundingBox, LiquidSettings paramLiquidSettings) {
/*  38 */     super(StructurePieceType.JIGSAW, 0, paramBoundingBox);
/*  39 */     this.structureTemplateManager = paramStructureTemplateManager;
/*  40 */     this.element = paramStructurePoolElement;
/*  41 */     this.position = paramBlockPos;
/*  42 */     this.groundLevelDelta = paramInt;
/*  43 */     this.rotation = paramRotation;
/*  44 */     this.liquidSettings = paramLiquidSettings;
/*     */   }
/*     */   
/*     */   public PoolElementStructurePiece(StructurePieceSerializationContext paramStructurePieceSerializationContext, CompoundTag paramCompoundTag) {
/*  48 */     super(StructurePieceType.JIGSAW, paramCompoundTag);
/*  49 */     this.structureTemplateManager = paramStructurePieceSerializationContext.structureTemplateManager();
/*  50 */     this.position = new BlockPos(paramCompoundTag.getIntOr("PosX", 0), paramCompoundTag.getIntOr("PosY", 0), paramCompoundTag.getIntOr("PosZ", 0));
/*  51 */     this.groundLevelDelta = paramCompoundTag.getIntOr("ground_level_delta", 0);
/*     */     
/*  53 */     RegistryOps registryOps = paramStructurePieceSerializationContext.registryAccess().createSerializationContext((DynamicOps)NbtOps.INSTANCE);
/*  54 */     this.element = (StructurePoolElement)paramCompoundTag.read("pool_element", StructurePoolElement.CODEC, (DynamicOps)registryOps).orElseThrow(() -> new IllegalStateException("Invalid pool element found"));
/*     */     
/*  56 */     this.rotation = paramCompoundTag.read("rotation", Rotation.LEGACY_CODEC).orElseThrow();
/*  57 */     this.boundingBox = this.element.getBoundingBox(this.structureTemplateManager, this.position, this.rotation);
/*     */     
/*  59 */     ListTag listTag = paramCompoundTag.getListOrEmpty("junctions");
/*  60 */     this.junctions.clear();
/*  61 */     listTag.forEach(paramTag -> this.junctions.add(JigsawJunction.deserialize(new Dynamic(paramDynamicOps, paramTag))));
/*     */     
/*  63 */     this.liquidSettings = paramCompoundTag.read("liquid_settings", LiquidSettings.CODEC).orElse(JigsawStructure.DEFAULT_LIQUID_SETTINGS);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(StructurePieceSerializationContext paramStructurePieceSerializationContext, CompoundTag paramCompoundTag) {
/*  68 */     paramCompoundTag.putInt("PosX", this.position.getX());
/*  69 */     paramCompoundTag.putInt("PosY", this.position.getY());
/*  70 */     paramCompoundTag.putInt("PosZ", this.position.getZ());
/*  71 */     paramCompoundTag.putInt("ground_level_delta", this.groundLevelDelta);
/*     */     
/*  73 */     RegistryOps registryOps = paramStructurePieceSerializationContext.registryAccess().createSerializationContext((DynamicOps)NbtOps.INSTANCE);
/*  74 */     paramCompoundTag.store("pool_element", StructurePoolElement.CODEC, (DynamicOps)registryOps, this.element);
/*     */     
/*  76 */     paramCompoundTag.store("rotation", Rotation.LEGACY_CODEC, this.rotation);
/*  77 */     ListTag listTag = new ListTag();
/*  78 */     for (JigsawJunction jigsawJunction : this.junctions) {
/*  79 */       listTag.add(jigsawJunction.serialize((DynamicOps)registryOps).getValue());
/*     */     }
/*  81 */     paramCompoundTag.put("junctions", (Tag)listTag);
/*     */     
/*  83 */     if (this.liquidSettings != JigsawStructure.DEFAULT_LIQUID_SETTINGS) {
/*  84 */       paramCompoundTag.store("liquid_settings", LiquidSettings.CODEC, (DynamicOps)registryOps, this.liquidSettings);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void postProcess(WorldGenLevel paramWorldGenLevel, StructureManager paramStructureManager, ChunkGenerator paramChunkGenerator, RandomSource paramRandomSource, BoundingBox paramBoundingBox, ChunkPos paramChunkPos, BlockPos paramBlockPos) {
/*  90 */     place(paramWorldGenLevel, paramStructureManager, paramChunkGenerator, paramRandomSource, paramBoundingBox, paramBlockPos, false);
/*     */   }
/*     */   
/*     */   public void place(WorldGenLevel paramWorldGenLevel, StructureManager paramStructureManager, ChunkGenerator paramChunkGenerator, RandomSource paramRandomSource, BoundingBox paramBoundingBox, BlockPos paramBlockPos, boolean paramBoolean) {
/*  94 */     this.element.place(this.structureTemplateManager, paramWorldGenLevel, paramStructureManager, paramChunkGenerator, this.position, paramBlockPos, this.rotation, paramBoundingBox, paramRandomSource, this.liquidSettings, paramBoolean);
/*     */   }
/*     */ 
/*     */   
/*     */   public void move(int paramInt1, int paramInt2, int paramInt3) {
/*  99 */     super.move(paramInt1, paramInt2, paramInt3);
/* 100 */     this.position = this.position.offset(paramInt1, paramInt2, paramInt3);
/*     */   }
/*     */ 
/*     */   
/*     */   public Rotation getRotation() {
/* 105 */     return this.rotation;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 110 */     return String.format(Locale.ROOT, "<%s | %s | %s | %s>", new Object[] { getClass().getSimpleName(), this.position, this.rotation, this.element });
/*     */   }
/*     */   
/*     */   public StructurePoolElement getElement() {
/* 114 */     return this.element;
/*     */   }
/*     */   
/*     */   public BlockPos getPosition() {
/* 118 */     return this.position;
/*     */   }
/*     */   
/*     */   public int getGroundLevelDelta() {
/* 122 */     return this.groundLevelDelta;
/*     */   }
/*     */   
/*     */   public void addJunction(JigsawJunction paramJigsawJunction) {
/* 126 */     this.junctions.add(paramJigsawJunction);
/*     */   }
/*     */   
/*     */   public List<JigsawJunction> getJunctions() {
/* 130 */     return this.junctions;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\PoolElementStructurePiece.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */