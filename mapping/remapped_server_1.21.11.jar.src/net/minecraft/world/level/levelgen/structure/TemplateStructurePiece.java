/*     */ package net.minecraft.world.level.levelgen.structure;
/*     */ 
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.commands.arguments.blocks.BlockStateParser;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.StructureMode;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ public abstract class TemplateStructurePiece
/*     */   extends StructurePiece
/*     */ {
/*  34 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   protected final String templateName;
/*     */   
/*     */   protected StructureTemplate template;
/*     */   protected StructurePlaceSettings placeSettings;
/*     */   protected BlockPos templatePosition;
/*     */   
/*     */   public TemplateStructurePiece(StructurePieceType paramStructurePieceType, int paramInt, StructureTemplateManager paramStructureTemplateManager, Identifier paramIdentifier, String paramString, StructurePlaceSettings paramStructurePlaceSettings, BlockPos paramBlockPos) {
/*  43 */     super(paramStructurePieceType, paramInt, paramStructureTemplateManager.getOrCreate(paramIdentifier).getBoundingBox(paramStructurePlaceSettings, paramBlockPos));
/*  44 */     setOrientation(Direction.NORTH);
/*     */     
/*  46 */     this.templateName = paramString;
/*  47 */     this.templatePosition = paramBlockPos;
/*  48 */     this.template = paramStructureTemplateManager.getOrCreate(paramIdentifier);
/*  49 */     this.placeSettings = paramStructurePlaceSettings;
/*     */   }
/*     */   
/*     */   public TemplateStructurePiece(StructurePieceType paramStructurePieceType, CompoundTag paramCompoundTag, StructureTemplateManager paramStructureTemplateManager, Function<Identifier, StructurePlaceSettings> paramFunction) {
/*  53 */     super(paramStructurePieceType, paramCompoundTag);
/*  54 */     setOrientation(Direction.NORTH);
/*     */     
/*  56 */     this.templateName = paramCompoundTag.getStringOr("Template", "");
/*  57 */     this.templatePosition = new BlockPos(paramCompoundTag.getIntOr("TPX", 0), paramCompoundTag.getIntOr("TPY", 0), paramCompoundTag.getIntOr("TPZ", 0));
/*  58 */     Identifier identifier = makeTemplateLocation();
/*  59 */     this.template = paramStructureTemplateManager.getOrCreate(identifier);
/*  60 */     this.placeSettings = paramFunction.apply(identifier);
/*     */ 
/*     */     
/*  63 */     this.boundingBox = this.template.getBoundingBox(this.placeSettings, this.templatePosition);
/*     */   }
/*     */   
/*     */   protected Identifier makeTemplateLocation() {
/*  67 */     return Identifier.parse(this.templateName);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(StructurePieceSerializationContext paramStructurePieceSerializationContext, CompoundTag paramCompoundTag) {
/*  72 */     paramCompoundTag.putInt("TPX", this.templatePosition.getX());
/*  73 */     paramCompoundTag.putInt("TPY", this.templatePosition.getY());
/*  74 */     paramCompoundTag.putInt("TPZ", this.templatePosition.getZ());
/*  75 */     paramCompoundTag.putString("Template", this.templateName);
/*     */   }
/*     */ 
/*     */   
/*     */   public void postProcess(WorldGenLevel paramWorldGenLevel, StructureManager paramStructureManager, ChunkGenerator paramChunkGenerator, RandomSource paramRandomSource, BoundingBox paramBoundingBox, ChunkPos paramChunkPos, BlockPos paramBlockPos) {
/*  80 */     this.placeSettings.setBoundingBox(paramBoundingBox);
/*     */     
/*  82 */     this.boundingBox = this.template.getBoundingBox(this.placeSettings, this.templatePosition);
/*  83 */     if (this.template.placeInWorld((ServerLevelAccessor)paramWorldGenLevel, this.templatePosition, paramBlockPos, this.placeSettings, paramRandomSource, 2)) {
/*  84 */       List list1 = this.template.filterBlocks(this.templatePosition, this.placeSettings, Blocks.STRUCTURE_BLOCK);
/*  85 */       for (StructureTemplate.StructureBlockInfo structureBlockInfo : list1) {
/*  86 */         if (structureBlockInfo.nbt() == null) {
/*     */           continue;
/*     */         }
/*     */         
/*  90 */         StructureMode structureMode = structureBlockInfo.nbt().read("mode", StructureMode.LEGACY_CODEC).orElseThrow();
/*  91 */         if (structureMode != StructureMode.DATA) {
/*     */           continue;
/*     */         }
/*     */         
/*  95 */         handleDataMarker(structureBlockInfo.nbt().getStringOr("metadata", ""), structureBlockInfo.pos(), (ServerLevelAccessor)paramWorldGenLevel, paramRandomSource, paramBoundingBox);
/*     */       } 
/*     */       
/*  98 */       List list2 = this.template.filterBlocks(this.templatePosition, this.placeSettings, Blocks.JIGSAW);
/*  99 */       for (StructureTemplate.StructureBlockInfo structureBlockInfo : list2) {
/* 100 */         if (structureBlockInfo.nbt() == null) {
/*     */           continue;
/*     */         }
/*     */         
/* 104 */         String str = structureBlockInfo.nbt().getStringOr("final_state", "minecraft:air");
/* 105 */         BlockState blockState = Blocks.AIR.defaultBlockState();
/*     */         try {
/* 107 */           blockState = BlockStateParser.parseForBlock(paramWorldGenLevel.holderLookup(Registries.BLOCK), str, true).blockState();
/* 108 */         } catch (CommandSyntaxException commandSyntaxException) {
/* 109 */           LOGGER.error("Error while parsing blockstate {} in jigsaw block @ {}", str, structureBlockInfo.pos());
/*     */         } 
/*     */         
/* 112 */         paramWorldGenLevel.setBlock(structureBlockInfo.pos(), blockState, 3);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void handleDataMarker(String paramString, BlockPos paramBlockPos, ServerLevelAccessor paramServerLevelAccessor, RandomSource paramRandomSource, BoundingBox paramBoundingBox);
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void move(int paramInt1, int paramInt2, int paramInt3) {
/* 125 */     super.move(paramInt1, paramInt2, paramInt3);
/* 126 */     this.templatePosition = this.templatePosition.offset(paramInt1, paramInt2, paramInt3);
/*     */   }
/*     */ 
/*     */   
/*     */   public Rotation getRotation() {
/* 131 */     return this.placeSettings.getRotation();
/*     */   }
/*     */   
/*     */   public StructureTemplate template() {
/* 135 */     return this.template;
/*     */   }
/*     */   
/*     */   public BlockPos templatePosition() {
/* 139 */     return this.templatePosition;
/*     */   }
/*     */   
/*     */   public StructurePlaceSettings placeSettings() {
/* 143 */     return this.placeSettings;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\TemplateStructurePiece.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */