/*     */ package net.minecraft.world.level.levelgen.structure.pools;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.state.properties.StructureMode;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.JigsawReplacementProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ 
/*     */ public class SinglePoolElement extends StructurePoolElement {
/*  41 */   private static final Comparator<StructureTemplate.JigsawBlockInfo> HIGHEST_SELECTION_PRIORITY_FIRST = Comparator.<StructureTemplate.JigsawBlockInfo>comparingInt(StructureTemplate.JigsawBlockInfo::selectionPriority).reversed();
/*     */   
/*     */   private static <T> DataResult<T> encodeTemplate(Either<Identifier, StructureTemplate> paramEither, DynamicOps<T> paramDynamicOps, T paramT) {
/*  44 */     Optional<Identifier> optional = paramEither.left();
/*  45 */     if (optional.isEmpty()) {
/*  46 */       return DataResult.error(() -> "Can not serialize a runtime pool element");
/*     */     }
/*  48 */     return Identifier.CODEC.encode(optional.get(), paramDynamicOps, paramT);
/*     */   }
/*     */   
/*  51 */   private static final Codec<Either<Identifier, StructureTemplate>> TEMPLATE_CODEC = Codec.of(SinglePoolElement::encodeTemplate, Identifier.CODEC
/*     */       
/*  53 */       .map(Either::left)); public static final MapCodec<SinglePoolElement> CODEC; protected final Either<Identifier, StructureTemplate> template; protected final Holder<StructureProcessorList> processors; protected final Optional<LiquidSettings> overrideLiquidSettings;
/*     */   
/*     */   static {
/*  56 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)templateCodec(), (App)processorsCodec(), (App)projectionCodec(), (App)overrideLiquidSettingsCodec()).apply((Applicative)paramInstance, SinglePoolElement::new));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static <E extends SinglePoolElement> RecordCodecBuilder<E, Holder<StructureProcessorList>> processorsCodec() {
/*  64 */     return StructureProcessorType.LIST_CODEC.fieldOf("processors").forGetter(paramSinglePoolElement -> paramSinglePoolElement.processors);
/*     */   }
/*     */   
/*     */   protected static <E extends SinglePoolElement> RecordCodecBuilder<E, Optional<LiquidSettings>> overrideLiquidSettingsCodec() {
/*  68 */     return LiquidSettings.CODEC.optionalFieldOf("override_liquid_settings").forGetter(paramSinglePoolElement -> paramSinglePoolElement.overrideLiquidSettings);
/*     */   }
/*     */   
/*     */   protected static <E extends SinglePoolElement> RecordCodecBuilder<E, Either<Identifier, StructureTemplate>> templateCodec() {
/*  72 */     return TEMPLATE_CODEC.fieldOf("location").forGetter(paramSinglePoolElement -> paramSinglePoolElement.template);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SinglePoolElement(Either<Identifier, StructureTemplate> paramEither, Holder<StructureProcessorList> paramHolder, StructureTemplatePool.Projection paramProjection, Optional<LiquidSettings> paramOptional) {
/*  80 */     super(paramProjection);
/*  81 */     this.template = paramEither;
/*  82 */     this.processors = paramHolder;
/*  83 */     this.overrideLiquidSettings = paramOptional;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3i getSize(StructureTemplateManager paramStructureTemplateManager, Rotation paramRotation) {
/*  88 */     StructureTemplate structureTemplate = getTemplate(paramStructureTemplateManager);
/*  89 */     return structureTemplate.getSize(paramRotation);
/*     */   }
/*     */   
/*     */   private StructureTemplate getTemplate(StructureTemplateManager paramStructureTemplateManager) {
/*  93 */     Objects.requireNonNull(paramStructureTemplateManager); return (StructureTemplate)this.template.map(paramStructureTemplateManager::getOrCreate, Function.identity());
/*     */   }
/*     */   
/*     */   public List<StructureTemplate.StructureBlockInfo> getDataMarkers(StructureTemplateManager paramStructureTemplateManager, BlockPos paramBlockPos, Rotation paramRotation, boolean paramBoolean) {
/*  97 */     StructureTemplate structureTemplate = getTemplate(paramStructureTemplateManager);
/*  98 */     ObjectArrayList objectArrayList = structureTemplate.filterBlocks(paramBlockPos, (new StructurePlaceSettings()).setRotation(paramRotation), Blocks.STRUCTURE_BLOCK, paramBoolean);
/*  99 */     ArrayList<StructureTemplate.StructureBlockInfo> arrayList = Lists.newArrayList();
/* 100 */     for (StructureTemplate.StructureBlockInfo structureBlockInfo : objectArrayList) {
/* 101 */       CompoundTag compoundTag = structureBlockInfo.nbt();
/* 102 */       if (compoundTag == null) {
/*     */         continue;
/*     */       }
/*     */       
/* 106 */       StructureMode structureMode = compoundTag.read("mode", StructureMode.LEGACY_CODEC).orElseThrow();
/* 107 */       if (structureMode != StructureMode.DATA) {
/*     */         continue;
/*     */       }
/*     */       
/* 111 */       arrayList.add(structureBlockInfo);
/*     */     } 
/*     */     
/* 114 */     return arrayList;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<StructureTemplate.JigsawBlockInfo> getShuffledJigsawBlocks(StructureTemplateManager paramStructureTemplateManager, BlockPos paramBlockPos, Rotation paramRotation, RandomSource paramRandomSource) {
/* 119 */     List<StructureTemplate.JigsawBlockInfo> list = getTemplate(paramStructureTemplateManager).getJigsaws(paramBlockPos, paramRotation);
/* 120 */     Util.shuffle(list, paramRandomSource);
/* 121 */     sortBySelectionPriority(list);
/* 122 */     return list;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static void sortBySelectionPriority(List<StructureTemplate.JigsawBlockInfo> paramList) {
/* 127 */     paramList.sort(HIGHEST_SELECTION_PRIORITY_FIRST);
/*     */   }
/*     */ 
/*     */   
/*     */   public BoundingBox getBoundingBox(StructureTemplateManager paramStructureTemplateManager, BlockPos paramBlockPos, Rotation paramRotation) {
/* 132 */     StructureTemplate structureTemplate = getTemplate(paramStructureTemplateManager);
/* 133 */     return structureTemplate.getBoundingBox((new StructurePlaceSettings()).setRotation(paramRotation), paramBlockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean place(StructureTemplateManager paramStructureTemplateManager, WorldGenLevel paramWorldGenLevel, StructureManager paramStructureManager, ChunkGenerator paramChunkGenerator, BlockPos paramBlockPos1, BlockPos paramBlockPos2, Rotation paramRotation, BoundingBox paramBoundingBox, RandomSource paramRandomSource, LiquidSettings paramLiquidSettings, boolean paramBoolean) {
/* 138 */     StructureTemplate structureTemplate = getTemplate(paramStructureTemplateManager);
/* 139 */     StructurePlaceSettings structurePlaceSettings = getSettings(paramRotation, paramBoundingBox, paramLiquidSettings, paramBoolean);
/*     */     
/* 141 */     if (structureTemplate.placeInWorld((ServerLevelAccessor)paramWorldGenLevel, paramBlockPos1, paramBlockPos2, structurePlaceSettings, paramRandomSource, 18)) {
/* 142 */       List list = StructureTemplate.processBlockInfos((ServerLevelAccessor)paramWorldGenLevel, paramBlockPos1, paramBlockPos2, structurePlaceSettings, getDataMarkers(paramStructureTemplateManager, paramBlockPos1, paramRotation, false));
/* 143 */       for (StructureTemplate.StructureBlockInfo structureBlockInfo : list) {
/* 144 */         handleDataMarker((LevelAccessor)paramWorldGenLevel, structureBlockInfo, paramBlockPos1, paramRotation, paramRandomSource, paramBoundingBox);
/*     */       }
/*     */       
/* 147 */       return true;
/*     */     } 
/* 149 */     return false;
/*     */   }
/*     */   
/*     */   protected StructurePlaceSettings getSettings(Rotation paramRotation, BoundingBox paramBoundingBox, LiquidSettings paramLiquidSettings, boolean paramBoolean) {
/* 153 */     StructurePlaceSettings structurePlaceSettings = new StructurePlaceSettings();
/* 154 */     structurePlaceSettings.setBoundingBox(paramBoundingBox);
/* 155 */     structurePlaceSettings.setRotation(paramRotation);
/* 156 */     structurePlaceSettings.setKnownShape(true);
/* 157 */     structurePlaceSettings.setIgnoreEntities(false);
/* 158 */     structurePlaceSettings.addProcessor((StructureProcessor)BlockIgnoreProcessor.STRUCTURE_BLOCK);
/* 159 */     structurePlaceSettings.setFinalizeEntities(true);
/* 160 */     structurePlaceSettings.setLiquidSettings(this.overrideLiquidSettings.orElse(paramLiquidSettings));
/* 161 */     if (!paramBoolean) {
/* 162 */       structurePlaceSettings.addProcessor((StructureProcessor)JigsawReplacementProcessor.INSTANCE);
/*     */     }
/* 164 */     Objects.requireNonNull(structurePlaceSettings); ((StructureProcessorList)this.processors.value()).list().forEach(structurePlaceSettings::addProcessor);
/* 165 */     Objects.requireNonNull(structurePlaceSettings); getProjection().getProcessors().forEach(structurePlaceSettings::addProcessor);
/* 166 */     return structurePlaceSettings;
/*     */   }
/*     */ 
/*     */   
/*     */   public StructurePoolElementType<?> getType() {
/* 171 */     return StructurePoolElementType.SINGLE;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 176 */     return "Single[" + String.valueOf(this.template) + "]";
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public Identifier getTemplateLocation() {
/* 181 */     return (Identifier)this.template.orThrow();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\pools\SinglePoolElement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */