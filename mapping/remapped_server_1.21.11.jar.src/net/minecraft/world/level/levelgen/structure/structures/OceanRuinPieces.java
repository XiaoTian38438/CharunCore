/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.valueproviders.ConstantInt;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.monster.zombie.Drowned;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.ChestBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*     */ import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.AlwaysTrueTest;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.CappedProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.PosAlwaysTrueTest;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.RuleProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.AppendLoot;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.RuleBlockEntityModifier;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ 
/*     */ public class OceanRuinPieces {
/*  57 */   static final StructureProcessor WARM_SUSPICIOUS_BLOCK_PROCESSOR = archyRuleProcessor(Blocks.SAND, Blocks.SUSPICIOUS_SAND, BuiltInLootTables.OCEAN_RUIN_WARM_ARCHAEOLOGY);
/*  58 */   static final StructureProcessor COLD_SUSPICIOUS_BLOCK_PROCESSOR = archyRuleProcessor(Blocks.GRAVEL, Blocks.SUSPICIOUS_GRAVEL, BuiltInLootTables.OCEAN_RUIN_COLD_ARCHAEOLOGY);
/*     */   
/*  60 */   private static final Identifier[] WARM_RUINS = new Identifier[] {
/*  61 */       Identifier.withDefaultNamespace("underwater_ruin/warm_1"), 
/*  62 */       Identifier.withDefaultNamespace("underwater_ruin/warm_2"), 
/*  63 */       Identifier.withDefaultNamespace("underwater_ruin/warm_3"), 
/*  64 */       Identifier.withDefaultNamespace("underwater_ruin/warm_4"), 
/*  65 */       Identifier.withDefaultNamespace("underwater_ruin/warm_5"), 
/*  66 */       Identifier.withDefaultNamespace("underwater_ruin/warm_6"), 
/*  67 */       Identifier.withDefaultNamespace("underwater_ruin/warm_7"), 
/*  68 */       Identifier.withDefaultNamespace("underwater_ruin/warm_8")
/*     */     };
/*     */   
/*  71 */   private static final Identifier[] RUINS_BRICK = new Identifier[] {
/*  72 */       Identifier.withDefaultNamespace("underwater_ruin/brick_1"), 
/*  73 */       Identifier.withDefaultNamespace("underwater_ruin/brick_2"), 
/*  74 */       Identifier.withDefaultNamespace("underwater_ruin/brick_3"), 
/*  75 */       Identifier.withDefaultNamespace("underwater_ruin/brick_4"), 
/*  76 */       Identifier.withDefaultNamespace("underwater_ruin/brick_5"), 
/*  77 */       Identifier.withDefaultNamespace("underwater_ruin/brick_6"), 
/*  78 */       Identifier.withDefaultNamespace("underwater_ruin/brick_7"), 
/*  79 */       Identifier.withDefaultNamespace("underwater_ruin/brick_8")
/*     */     };
/*     */   
/*  82 */   private static final Identifier[] RUINS_CRACKED = new Identifier[] {
/*  83 */       Identifier.withDefaultNamespace("underwater_ruin/cracked_1"), 
/*  84 */       Identifier.withDefaultNamespace("underwater_ruin/cracked_2"), 
/*  85 */       Identifier.withDefaultNamespace("underwater_ruin/cracked_3"), 
/*  86 */       Identifier.withDefaultNamespace("underwater_ruin/cracked_4"), 
/*  87 */       Identifier.withDefaultNamespace("underwater_ruin/cracked_5"), 
/*  88 */       Identifier.withDefaultNamespace("underwater_ruin/cracked_6"), 
/*  89 */       Identifier.withDefaultNamespace("underwater_ruin/cracked_7"), 
/*  90 */       Identifier.withDefaultNamespace("underwater_ruin/cracked_8")
/*     */     };
/*     */   
/*  93 */   private static final Identifier[] RUINS_MOSSY = new Identifier[] {
/*  94 */       Identifier.withDefaultNamespace("underwater_ruin/mossy_1"), 
/*  95 */       Identifier.withDefaultNamespace("underwater_ruin/mossy_2"), 
/*  96 */       Identifier.withDefaultNamespace("underwater_ruin/mossy_3"), 
/*  97 */       Identifier.withDefaultNamespace("underwater_ruin/mossy_4"), 
/*  98 */       Identifier.withDefaultNamespace("underwater_ruin/mossy_5"), 
/*  99 */       Identifier.withDefaultNamespace("underwater_ruin/mossy_6"), 
/* 100 */       Identifier.withDefaultNamespace("underwater_ruin/mossy_7"), 
/* 101 */       Identifier.withDefaultNamespace("underwater_ruin/mossy_8")
/*     */     };
/*     */   
/* 104 */   private static final Identifier[] BIG_RUINS_BRICK = new Identifier[] {
/* 105 */       Identifier.withDefaultNamespace("underwater_ruin/big_brick_1"), 
/* 106 */       Identifier.withDefaultNamespace("underwater_ruin/big_brick_2"), 
/* 107 */       Identifier.withDefaultNamespace("underwater_ruin/big_brick_3"), 
/* 108 */       Identifier.withDefaultNamespace("underwater_ruin/big_brick_8")
/*     */     };
/*     */   
/* 111 */   private static final Identifier[] BIG_RUINS_MOSSY = new Identifier[] {
/* 112 */       Identifier.withDefaultNamespace("underwater_ruin/big_mossy_1"), 
/* 113 */       Identifier.withDefaultNamespace("underwater_ruin/big_mossy_2"), 
/* 114 */       Identifier.withDefaultNamespace("underwater_ruin/big_mossy_3"), 
/* 115 */       Identifier.withDefaultNamespace("underwater_ruin/big_mossy_8")
/*     */     };
/*     */   
/* 118 */   private static final Identifier[] BIG_RUINS_CRACKED = new Identifier[] {
/* 119 */       Identifier.withDefaultNamespace("underwater_ruin/big_cracked_1"), 
/* 120 */       Identifier.withDefaultNamespace("underwater_ruin/big_cracked_2"), 
/* 121 */       Identifier.withDefaultNamespace("underwater_ruin/big_cracked_3"), 
/* 122 */       Identifier.withDefaultNamespace("underwater_ruin/big_cracked_8")
/*     */     };
/*     */   
/* 125 */   private static final Identifier[] BIG_WARM_RUINS = new Identifier[] {
/* 126 */       Identifier.withDefaultNamespace("underwater_ruin/big_warm_4"), 
/* 127 */       Identifier.withDefaultNamespace("underwater_ruin/big_warm_5"), 
/* 128 */       Identifier.withDefaultNamespace("underwater_ruin/big_warm_6"), 
/* 129 */       Identifier.withDefaultNamespace("underwater_ruin/big_warm_7")
/*     */     };
/*     */   
/*     */   private static StructureProcessor archyRuleProcessor(Block paramBlock1, Block paramBlock2, ResourceKey<LootTable> paramResourceKey) {
/* 133 */     return (StructureProcessor)new CappedProcessor((StructureProcessor)new RuleProcessor(
/*     */           
/* 135 */           List.of(new ProcessorRule((RuleTest)new BlockMatchTest(paramBlock1), (RuleTest)AlwaysTrueTest.INSTANCE, (PosRuleTest)PosAlwaysTrueTest.INSTANCE, paramBlock2
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 140 */               .defaultBlockState(), (RuleBlockEntityModifier)new AppendLoot(paramResourceKey)))), 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 145 */         (IntProvider)ConstantInt.of(5));
/*     */   }
/*     */ 
/*     */   
/*     */   private static Identifier getSmallWarmRuin(RandomSource paramRandomSource) {
/* 150 */     return (Identifier)Util.getRandom((Object[])WARM_RUINS, paramRandomSource);
/*     */   }
/*     */   
/*     */   private static Identifier getBigWarmRuin(RandomSource paramRandomSource) {
/* 154 */     return (Identifier)Util.getRandom((Object[])BIG_WARM_RUINS, paramRandomSource);
/*     */   }
/*     */   
/*     */   public static void addPieces(StructureTemplateManager paramStructureTemplateManager, BlockPos paramBlockPos, Rotation paramRotation, StructurePieceAccessor paramStructurePieceAccessor, RandomSource paramRandomSource, OceanRuinStructure paramOceanRuinStructure) {
/* 158 */     boolean bool = (paramRandomSource.nextFloat() <= paramOceanRuinStructure.largeProbability) ? true : false;
/* 159 */     float f = bool ? 0.9F : 0.8F;
/*     */     
/* 161 */     addPiece(paramStructureTemplateManager, paramBlockPos, paramRotation, paramStructurePieceAccessor, paramRandomSource, paramOceanRuinStructure, bool, f);
/*     */     
/* 163 */     if (bool && paramRandomSource.nextFloat() <= paramOceanRuinStructure.clusterProbability) {
/* 164 */       addClusterRuins(paramStructureTemplateManager, paramRandomSource, paramRotation, paramBlockPos, paramOceanRuinStructure, paramStructurePieceAccessor);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static void addClusterRuins(StructureTemplateManager paramStructureTemplateManager, RandomSource paramRandomSource, Rotation paramRotation, BlockPos paramBlockPos, OceanRuinStructure paramOceanRuinStructure, StructurePieceAccessor paramStructurePieceAccessor) {
/* 170 */     BlockPos blockPos1 = new BlockPos(paramBlockPos.getX(), 90, paramBlockPos.getZ());
/* 171 */     BlockPos blockPos2 = StructureTemplate.transform(new BlockPos(15, 0, 15), Mirror.NONE, paramRotation, BlockPos.ZERO).offset((Vec3i)blockPos1);
/* 172 */     BoundingBox boundingBox = BoundingBox.fromCorners((Vec3i)blockPos1, (Vec3i)blockPos2);
/* 173 */     BlockPos blockPos3 = new BlockPos(Math.min(blockPos1.getX(), blockPos2.getX()), blockPos1.getY(), Math.min(blockPos1.getZ(), blockPos2.getZ()));
/* 174 */     List<BlockPos> list = allPositions(paramRandomSource, blockPos3);
/* 175 */     int i = Mth.nextInt(paramRandomSource, 4, 8);
/*     */     
/* 177 */     for (byte b = 0; b < i; b++) {
/* 178 */       if (!list.isEmpty()) {
/* 179 */         int j = paramRandomSource.nextInt(list.size());
/* 180 */         BlockPos blockPos4 = list.remove(j);
/* 181 */         Rotation rotation = Rotation.getRandom(paramRandomSource);
/* 182 */         BlockPos blockPos5 = StructureTemplate.transform(new BlockPos(5, 0, 6), Mirror.NONE, rotation, BlockPos.ZERO).offset((Vec3i)blockPos4);
/* 183 */         BoundingBox boundingBox1 = BoundingBox.fromCorners((Vec3i)blockPos4, (Vec3i)blockPos5);
/* 184 */         if (!boundingBox1.intersects(boundingBox))
/*     */         {
/*     */ 
/*     */           
/* 188 */           addPiece(paramStructureTemplateManager, blockPos4, rotation, paramStructurePieceAccessor, paramRandomSource, paramOceanRuinStructure, false, 0.8F); } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static List<BlockPos> allPositions(RandomSource paramRandomSource, BlockPos paramBlockPos) {
/* 194 */     ArrayList<BlockPos> arrayList = Lists.newArrayList();
/* 195 */     arrayList.add(paramBlockPos.offset(-16 + Mth.nextInt(paramRandomSource, 1, 8), 0, 16 + Mth.nextInt(paramRandomSource, 1, 7)));
/* 196 */     arrayList.add(paramBlockPos.offset(-16 + Mth.nextInt(paramRandomSource, 1, 8), 0, Mth.nextInt(paramRandomSource, 1, 7)));
/* 197 */     arrayList.add(paramBlockPos.offset(-16 + Mth.nextInt(paramRandomSource, 1, 8), 0, -16 + Mth.nextInt(paramRandomSource, 4, 8)));
/* 198 */     arrayList.add(paramBlockPos.offset(Mth.nextInt(paramRandomSource, 1, 7), 0, 16 + Mth.nextInt(paramRandomSource, 1, 7)));
/* 199 */     arrayList.add(paramBlockPos.offset(Mth.nextInt(paramRandomSource, 1, 7), 0, -16 + Mth.nextInt(paramRandomSource, 4, 6)));
/* 200 */     arrayList.add(paramBlockPos.offset(16 + Mth.nextInt(paramRandomSource, 1, 7), 0, 16 + Mth.nextInt(paramRandomSource, 3, 8)));
/* 201 */     arrayList.add(paramBlockPos.offset(16 + Mth.nextInt(paramRandomSource, 1, 7), 0, Mth.nextInt(paramRandomSource, 1, 7)));
/* 202 */     arrayList.add(paramBlockPos.offset(16 + Mth.nextInt(paramRandomSource, 1, 7), 0, -16 + Mth.nextInt(paramRandomSource, 4, 8)));
/*     */     
/* 204 */     return arrayList;
/*     */   }
/*     */   private static void addPiece(StructureTemplateManager paramStructureTemplateManager, BlockPos paramBlockPos, Rotation paramRotation, StructurePieceAccessor paramStructurePieceAccessor, RandomSource paramRandomSource, OceanRuinStructure paramOceanRuinStructure, boolean paramBoolean, float paramFloat) {
/*     */     Identifier identifier;
/* 208 */     switch (paramOceanRuinStructure.biomeTemp) {
/*     */       
/*     */       default:
/* 211 */         identifier = paramBoolean ? getBigWarmRuin(paramRandomSource) : getSmallWarmRuin(paramRandomSource);
/* 212 */         paramStructurePieceAccessor.addPiece((StructurePiece)new OceanRuinPiece(paramStructureTemplateManager, identifier, paramBlockPos, paramRotation, paramFloat, paramOceanRuinStructure.biomeTemp, paramBoolean)); return;
/*     */       case COLD:
/*     */         break;
/* 215 */     }  Identifier[] arrayOfIdentifier1 = paramBoolean ? BIG_RUINS_BRICK : RUINS_BRICK;
/* 216 */     Identifier[] arrayOfIdentifier2 = paramBoolean ? BIG_RUINS_CRACKED : RUINS_CRACKED;
/* 217 */     Identifier[] arrayOfIdentifier3 = paramBoolean ? BIG_RUINS_MOSSY : RUINS_MOSSY;
/*     */     
/* 219 */     int i = paramRandomSource.nextInt(arrayOfIdentifier1.length);
/* 220 */     paramStructurePieceAccessor.addPiece((StructurePiece)new OceanRuinPiece(paramStructureTemplateManager, arrayOfIdentifier1[i], paramBlockPos, paramRotation, paramFloat, paramOceanRuinStructure.biomeTemp, paramBoolean));
/* 221 */     paramStructurePieceAccessor.addPiece((StructurePiece)new OceanRuinPiece(paramStructureTemplateManager, arrayOfIdentifier2[i], paramBlockPos, paramRotation, 0.7F, paramOceanRuinStructure.biomeTemp, paramBoolean));
/* 222 */     paramStructurePieceAccessor.addPiece((StructurePiece)new OceanRuinPiece(paramStructureTemplateManager, arrayOfIdentifier3[i], paramBlockPos, paramRotation, 0.5F, paramOceanRuinStructure.biomeTemp, paramBoolean));
/*     */   }
/*     */   
/*     */   public static class OceanRuinPiece
/*     */     extends TemplateStructurePiece
/*     */   {
/*     */     private final OceanRuinStructure.Type biomeType;
/*     */     private final float integrity;
/*     */     private final boolean isLarge;
/*     */     
/*     */     public OceanRuinPiece(StructureTemplateManager param1StructureTemplateManager, Identifier param1Identifier, BlockPos param1BlockPos, Rotation param1Rotation, float param1Float, OceanRuinStructure.Type param1Type, boolean param1Boolean) {
/* 233 */       super(StructurePieceType.OCEAN_RUIN, 0, param1StructureTemplateManager, param1Identifier, param1Identifier.toString(), makeSettings(param1Rotation, param1Float, param1Type), param1BlockPos);
/*     */       
/* 235 */       this.integrity = param1Float;
/* 236 */       this.biomeType = param1Type;
/* 237 */       this.isLarge = param1Boolean;
/*     */     }
/*     */     
/*     */     private OceanRuinPiece(StructureTemplateManager param1StructureTemplateManager, CompoundTag param1CompoundTag, Rotation param1Rotation, float param1Float, OceanRuinStructure.Type param1Type, boolean param1Boolean) {
/* 241 */       super(StructurePieceType.OCEAN_RUIN, param1CompoundTag, param1StructureTemplateManager, param1Identifier -> makeSettings(param1Rotation, param1Float, param1Type));
/*     */       
/* 243 */       this.integrity = param1Float;
/* 244 */       this.biomeType = param1Type;
/* 245 */       this.isLarge = param1Boolean;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private static StructurePlaceSettings makeSettings(Rotation param1Rotation, float param1Float, OceanRuinStructure.Type param1Type) {
/* 251 */       StructureProcessor structureProcessor = (param1Type == OceanRuinStructure.Type.COLD) ? OceanRuinPieces.COLD_SUSPICIOUS_BLOCK_PROCESSOR : OceanRuinPieces.WARM_SUSPICIOUS_BLOCK_PROCESSOR;
/*     */       
/* 253 */       return (new StructurePlaceSettings())
/* 254 */         .setRotation(param1Rotation)
/* 255 */         .setMirror(Mirror.NONE)
/* 256 */         .addProcessor((StructureProcessor)new BlockRotProcessor(param1Float))
/* 257 */         .addProcessor((StructureProcessor)BlockIgnoreProcessor.STRUCTURE_AND_AIR)
/* 258 */         .addProcessor(structureProcessor);
/*     */     }
/*     */     
/*     */     public static OceanRuinPiece create(StructureTemplateManager param1StructureTemplateManager, CompoundTag param1CompoundTag) {
/* 262 */       Rotation rotation = param1CompoundTag.read("Rot", Rotation.LEGACY_CODEC).orElseThrow();
/* 263 */       float f = param1CompoundTag.getFloatOr("Integrity", 0.0F);
/* 264 */       OceanRuinStructure.Type type = param1CompoundTag.read("BiomeType", OceanRuinStructure.Type.LEGACY_CODEC).orElseThrow();
/* 265 */       boolean bool = param1CompoundTag.getBooleanOr("IsLarge", false);
/* 266 */       return new OceanRuinPiece(param1StructureTemplateManager, param1CompoundTag, rotation, f, type, bool);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void addAdditionalSaveData(StructurePieceSerializationContext param1StructurePieceSerializationContext, CompoundTag param1CompoundTag) {
/* 271 */       super.addAdditionalSaveData(param1StructurePieceSerializationContext, param1CompoundTag);
/* 272 */       param1CompoundTag.store("Rot", Rotation.LEGACY_CODEC, this.placeSettings.getRotation());
/* 273 */       param1CompoundTag.putFloat("Integrity", this.integrity);
/* 274 */       param1CompoundTag.store("BiomeType", OceanRuinStructure.Type.LEGACY_CODEC, this.biomeType);
/* 275 */       param1CompoundTag.putBoolean("IsLarge", this.isLarge);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void handleDataMarker(String param1String, BlockPos param1BlockPos, ServerLevelAccessor param1ServerLevelAccessor, RandomSource param1RandomSource, BoundingBox param1BoundingBox) {
/* 280 */       if ("chest".equals(param1String)) {
/* 281 */         param1ServerLevelAccessor.setBlock(param1BlockPos, (BlockState)Blocks.CHEST.defaultBlockState().setValue((Property)ChestBlock.WATERLOGGED, Boolean.valueOf(param1ServerLevelAccessor.getFluidState(param1BlockPos).is(FluidTags.WATER))), 2);
/*     */         
/* 283 */         BlockEntity blockEntity = param1ServerLevelAccessor.getBlockEntity(param1BlockPos);
/* 284 */         if (blockEntity instanceof ChestBlockEntity) {
/* 285 */           ((ChestBlockEntity)blockEntity).setLootTable(this.isLarge ? BuiltInLootTables.UNDERWATER_RUIN_BIG : BuiltInLootTables.UNDERWATER_RUIN_SMALL, param1RandomSource.nextLong());
/*     */         }
/*     */       }
/* 288 */       else if ("drowned".equals(param1String)) {
/* 289 */         Drowned drowned = (Drowned)EntityType.DROWNED.create((Level)param1ServerLevelAccessor.getLevel(), EntitySpawnReason.STRUCTURE);
/* 290 */         if (drowned != null) {
/* 291 */           drowned.setPersistenceRequired();
/* 292 */           drowned.snapTo(param1BlockPos, 0.0F, 0.0F);
/* 293 */           drowned.finalizeSpawn(param1ServerLevelAccessor, param1ServerLevelAccessor.getCurrentDifficultyAt(param1BlockPos), EntitySpawnReason.STRUCTURE, null);
/* 294 */           param1ServerLevelAccessor.addFreshEntityWithPassengers((Entity)drowned);
/* 295 */           if (param1BlockPos.getY() > param1ServerLevelAccessor.getSeaLevel()) {
/* 296 */             param1ServerLevelAccessor.setBlock(param1BlockPos, Blocks.AIR.defaultBlockState(), 2);
/*     */           } else {
/* 298 */             param1ServerLevelAccessor.setBlock(param1BlockPos, Blocks.WATER.defaultBlockState(), 2);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void postProcess(WorldGenLevel param1WorldGenLevel, StructureManager param1StructureManager, ChunkGenerator param1ChunkGenerator, RandomSource param1RandomSource, BoundingBox param1BoundingBox, ChunkPos param1ChunkPos, BlockPos param1BlockPos) {
/* 306 */       int i = param1WorldGenLevel.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, this.templatePosition.getX(), this.templatePosition.getZ());
/* 307 */       this.templatePosition = new BlockPos(this.templatePosition.getX(), i, this.templatePosition.getZ());
/* 308 */       BlockPos blockPos = StructureTemplate.transform(new BlockPos(this.template.getSize().getX() - 1, 0, this.template.getSize().getZ() - 1), Mirror.NONE, this.placeSettings.getRotation(), BlockPos.ZERO).offset((Vec3i)this.templatePosition);
/* 309 */       this.templatePosition = new BlockPos(this.templatePosition.getX(), getHeight(this.templatePosition, (BlockGetter)param1WorldGenLevel, blockPos), this.templatePosition.getZ());
/*     */       
/* 311 */       super.postProcess(param1WorldGenLevel, param1StructureManager, param1ChunkGenerator, param1RandomSource, param1BoundingBox, param1ChunkPos, param1BlockPos);
/*     */     }
/*     */     
/*     */     private int getHeight(BlockPos param1BlockPos1, BlockGetter param1BlockGetter, BlockPos param1BlockPos2) {
/* 315 */       int i = param1BlockPos1.getY();
/* 316 */       int j = 512;
/* 317 */       int k = i - 1;
/* 318 */       byte b = 0;
/* 319 */       for (BlockPos blockPos : BlockPos.betweenClosed(param1BlockPos1, param1BlockPos2)) {
/* 320 */         int n = blockPos.getX();
/* 321 */         int i1 = blockPos.getZ();
/* 322 */         int i2 = param1BlockPos1.getY() - 1;
/* 323 */         BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(n, i2, i1);
/* 324 */         BlockState blockState = param1BlockGetter.getBlockState((BlockPos)mutableBlockPos);
/* 325 */         FluidState fluidState = param1BlockGetter.getFluidState((BlockPos)mutableBlockPos);
/* 326 */         while ((blockState.isAir() || fluidState.is(FluidTags.WATER) || blockState.is(BlockTags.ICE)) && i2 > param1BlockGetter.getMinY() + 1) {
/* 327 */           i2--;
/* 328 */           mutableBlockPos.set(n, i2, i1);
/* 329 */           blockState = param1BlockGetter.getBlockState((BlockPos)mutableBlockPos);
/* 330 */           fluidState = param1BlockGetter.getFluidState((BlockPos)mutableBlockPos);
/*     */         } 
/*     */         
/* 333 */         j = Math.min(j, i2);
/* 334 */         if (i2 < k - 2) {
/* 335 */           b++;
/*     */         }
/*     */       } 
/*     */       
/* 339 */       int m = Math.abs(param1BlockPos1.getX() - param1BlockPos2.getX());
/* 340 */       if (k - j > 2 && b > m - 2) {
/* 341 */         i = j + 1;
/*     */       }
/*     */       
/* 344 */       return i;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\OceanRuinPieces.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */