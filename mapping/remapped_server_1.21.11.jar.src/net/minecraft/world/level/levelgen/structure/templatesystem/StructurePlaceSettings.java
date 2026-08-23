/*     */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ 
/*     */ public class StructurePlaceSettings
/*     */ {
/*  16 */   private Mirror mirror = Mirror.NONE;
/*  17 */   private Rotation rotation = Rotation.NONE;
/*  18 */   private BlockPos rotationPivot = BlockPos.ZERO;
/*     */   private boolean ignoreEntities;
/*     */   private BoundingBox boundingBox;
/*  21 */   private LiquidSettings liquidSettings = LiquidSettings.APPLY_WATERLOGGING;
/*     */   private RandomSource random;
/*     */   private int palette;
/*  24 */   private final List<StructureProcessor> processors = Lists.newArrayList();
/*     */   private boolean knownShape;
/*     */   private boolean finalizeEntities;
/*     */   
/*     */   public StructurePlaceSettings copy() {
/*  29 */     StructurePlaceSettings structurePlaceSettings = new StructurePlaceSettings();
/*  30 */     structurePlaceSettings.mirror = this.mirror;
/*  31 */     structurePlaceSettings.rotation = this.rotation;
/*  32 */     structurePlaceSettings.rotationPivot = this.rotationPivot;
/*  33 */     structurePlaceSettings.ignoreEntities = this.ignoreEntities;
/*  34 */     structurePlaceSettings.boundingBox = this.boundingBox;
/*  35 */     structurePlaceSettings.liquidSettings = this.liquidSettings;
/*  36 */     structurePlaceSettings.random = this.random;
/*  37 */     structurePlaceSettings.palette = this.palette;
/*  38 */     structurePlaceSettings.processors.addAll(this.processors);
/*  39 */     structurePlaceSettings.knownShape = this.knownShape;
/*  40 */     structurePlaceSettings.finalizeEntities = this.finalizeEntities;
/*  41 */     return structurePlaceSettings;
/*     */   }
/*     */   
/*     */   public StructurePlaceSettings setMirror(Mirror paramMirror) {
/*  45 */     this.mirror = paramMirror;
/*  46 */     return this;
/*     */   }
/*     */   
/*     */   public StructurePlaceSettings setRotation(Rotation paramRotation) {
/*  50 */     this.rotation = paramRotation;
/*  51 */     return this;
/*     */   }
/*     */   
/*     */   public StructurePlaceSettings setRotationPivot(BlockPos paramBlockPos) {
/*  55 */     this.rotationPivot = paramBlockPos;
/*  56 */     return this;
/*     */   }
/*     */   
/*     */   public StructurePlaceSettings setIgnoreEntities(boolean paramBoolean) {
/*  60 */     this.ignoreEntities = paramBoolean;
/*  61 */     return this;
/*     */   }
/*     */   
/*     */   public StructurePlaceSettings setBoundingBox(BoundingBox paramBoundingBox) {
/*  65 */     this.boundingBox = paramBoundingBox;
/*  66 */     return this;
/*     */   }
/*     */   
/*     */   public StructurePlaceSettings setRandom(RandomSource paramRandomSource) {
/*  70 */     this.random = paramRandomSource;
/*  71 */     return this;
/*     */   }
/*     */   
/*     */   public StructurePlaceSettings setLiquidSettings(LiquidSettings paramLiquidSettings) {
/*  75 */     this.liquidSettings = paramLiquidSettings;
/*  76 */     return this;
/*     */   }
/*     */   
/*     */   public StructurePlaceSettings setKnownShape(boolean paramBoolean) {
/*  80 */     this.knownShape = paramBoolean;
/*  81 */     return this;
/*     */   }
/*     */   
/*     */   public StructurePlaceSettings clearProcessors() {
/*  85 */     this.processors.clear();
/*  86 */     return this;
/*     */   }
/*     */   
/*     */   public StructurePlaceSettings addProcessor(StructureProcessor paramStructureProcessor) {
/*  90 */     this.processors.add(paramStructureProcessor);
/*  91 */     return this;
/*     */   }
/*     */   
/*     */   public StructurePlaceSettings popProcessor(StructureProcessor paramStructureProcessor) {
/*  95 */     this.processors.remove(paramStructureProcessor);
/*  96 */     return this;
/*     */   }
/*     */   
/*     */   public Mirror getMirror() {
/* 100 */     return this.mirror;
/*     */   }
/*     */   
/*     */   public Rotation getRotation() {
/* 104 */     return this.rotation;
/*     */   }
/*     */   
/*     */   public BlockPos getRotationPivot() {
/* 108 */     return this.rotationPivot;
/*     */   }
/*     */   
/*     */   public RandomSource getRandom(BlockPos paramBlockPos) {
/* 112 */     if (this.random != null) {
/* 113 */       return this.random;
/*     */     }
/*     */     
/* 116 */     if (paramBlockPos == null) {
/* 117 */       return RandomSource.create(Util.getMillis());
/*     */     }
/*     */     
/* 120 */     return RandomSource.create(Mth.getSeed((Vec3i)paramBlockPos));
/*     */   }
/*     */   
/*     */   public boolean isIgnoreEntities() {
/* 124 */     return this.ignoreEntities;
/*     */   }
/*     */   
/*     */   public BoundingBox getBoundingBox() {
/* 128 */     return this.boundingBox;
/*     */   }
/*     */   
/*     */   public boolean getKnownShape() {
/* 132 */     return this.knownShape;
/*     */   }
/*     */   
/*     */   public List<StructureProcessor> getProcessors() {
/* 136 */     return this.processors;
/*     */   }
/*     */   
/*     */   public boolean shouldApplyWaterlogging() {
/* 140 */     return (this.liquidSettings == LiquidSettings.APPLY_WATERLOGGING);
/*     */   }
/*     */   
/*     */   public StructureTemplate.Palette getRandomPalette(List<StructureTemplate.Palette> paramList, BlockPos paramBlockPos) {
/* 144 */     int i = paramList.size();
/* 145 */     if (i == 0)
/*     */     {
/* 147 */       throw new IllegalStateException("No palettes");
/*     */     }
/* 149 */     return paramList.get(getRandom(paramBlockPos).nextInt(i));
/*     */   }
/*     */   
/*     */   public StructurePlaceSettings setFinalizeEntities(boolean paramBoolean) {
/* 153 */     this.finalizeEntities = paramBoolean;
/* 154 */     return this;
/*     */   }
/*     */   
/*     */   public boolean shouldFinalizeEntities() {
/* 158 */     return this.finalizeEntities;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\StructurePlaceSettings.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */