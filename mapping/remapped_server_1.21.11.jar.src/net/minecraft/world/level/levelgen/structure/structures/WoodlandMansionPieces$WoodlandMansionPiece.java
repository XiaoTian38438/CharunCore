/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.ChestBlock;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WoodlandMansionPiece
/*     */   extends TemplateStructurePiece
/*     */ {
/*     */   public WoodlandMansionPiece(StructureTemplateManager paramStructureTemplateManager, String paramString, BlockPos paramBlockPos, Rotation paramRotation) {
/*  40 */     this(paramStructureTemplateManager, paramString, paramBlockPos, paramRotation, Mirror.NONE);
/*     */   }
/*     */   
/*     */   public WoodlandMansionPiece(StructureTemplateManager paramStructureTemplateManager, String paramString, BlockPos paramBlockPos, Rotation paramRotation, Mirror paramMirror) {
/*  44 */     super(StructurePieceType.WOODLAND_MANSION_PIECE, 0, paramStructureTemplateManager, makeLocation(paramString), paramString, makeSettings(paramMirror, paramRotation), paramBlockPos);
/*     */   }
/*     */   
/*     */   public WoodlandMansionPiece(StructureTemplateManager paramStructureTemplateManager, CompoundTag paramCompoundTag) {
/*  48 */     super(StructurePieceType.WOODLAND_MANSION_PIECE, paramCompoundTag, paramStructureTemplateManager, paramIdentifier -> makeSettings(paramCompoundTag.read("Mi", Mirror.LEGACY_CODEC).orElseThrow(), paramCompoundTag.read("Rot", Rotation.LEGACY_CODEC).orElseThrow()));
/*     */   }
/*     */ 
/*     */   
/*     */   protected Identifier makeTemplateLocation() {
/*  53 */     return makeLocation(this.templateName);
/*     */   }
/*     */   
/*     */   private static Identifier makeLocation(String paramString) {
/*  57 */     return Identifier.withDefaultNamespace("woodland_mansion/" + paramString);
/*     */   }
/*     */   
/*     */   private static StructurePlaceSettings makeSettings(Mirror paramMirror, Rotation paramRotation) {
/*  61 */     return (new StructurePlaceSettings()).setIgnoreEntities(true).setRotation(paramRotation).setMirror(paramMirror).addProcessor((StructureProcessor)BlockIgnoreProcessor.STRUCTURE_BLOCK);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(StructurePieceSerializationContext paramStructurePieceSerializationContext, CompoundTag paramCompoundTag) {
/*  66 */     super.addAdditionalSaveData(paramStructurePieceSerializationContext, paramCompoundTag);
/*     */     
/*  68 */     paramCompoundTag.store("Rot", Rotation.LEGACY_CODEC, this.placeSettings.getRotation());
/*  69 */     paramCompoundTag.store("Mi", Mirror.LEGACY_CODEC, this.placeSettings.getMirror());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void handleDataMarker(String paramString, BlockPos paramBlockPos, ServerLevelAccessor paramServerLevelAccessor, RandomSource paramRandomSource, BoundingBox paramBoundingBox) {
/*  74 */     if (paramString.startsWith("Chest")) {
/*  75 */       Rotation rotation = this.placeSettings.getRotation();
/*  76 */       BlockState blockState = Blocks.CHEST.defaultBlockState();
/*  77 */       if ("ChestWest".equals(paramString)) {
/*  78 */         blockState = (BlockState)blockState.setValue((Property)ChestBlock.FACING, (Comparable)rotation.rotate(Direction.WEST));
/*  79 */       } else if ("ChestEast".equals(paramString)) {
/*  80 */         blockState = (BlockState)blockState.setValue((Property)ChestBlock.FACING, (Comparable)rotation.rotate(Direction.EAST));
/*  81 */       } else if ("ChestSouth".equals(paramString)) {
/*  82 */         blockState = (BlockState)blockState.setValue((Property)ChestBlock.FACING, (Comparable)rotation.rotate(Direction.SOUTH));
/*  83 */       } else if ("ChestNorth".equals(paramString)) {
/*  84 */         blockState = (BlockState)blockState.setValue((Property)ChestBlock.FACING, (Comparable)rotation.rotate(Direction.NORTH));
/*     */       } 
/*  86 */       createChest(paramServerLevelAccessor, paramBoundingBox, paramRandomSource, paramBlockPos, BuiltInLootTables.WOODLAND_MANSION, blockState);
/*     */     } else {
/*  88 */       int i; byte b; ArrayList<Mob> arrayList = new ArrayList();
/*  89 */       switch (paramString) {
/*     */         case "Mage":
/*  91 */           arrayList.add((Mob)EntityType.EVOKER.create((Level)paramServerLevelAccessor.getLevel(), EntitySpawnReason.STRUCTURE));
/*     */           break;
/*     */         case "Warrior":
/*  94 */           arrayList.add((Mob)EntityType.VINDICATOR.create((Level)paramServerLevelAccessor.getLevel(), EntitySpawnReason.STRUCTURE));
/*     */           break;
/*     */         case "Group of Allays":
/*  97 */           i = paramServerLevelAccessor.getRandom().nextInt(3) + 1;
/*  98 */           for (b = 0; b < i; b++) {
/*  99 */             arrayList.add((Mob)EntityType.ALLAY.create((Level)paramServerLevelAccessor.getLevel(), EntitySpawnReason.STRUCTURE));
/*     */           }
/*     */           break;
/*     */         
/*     */         default:
/*     */           return;
/*     */       } 
/* 106 */       for (Mob mob : arrayList) {
/* 107 */         if (mob == null) {
/*     */           continue;
/*     */         }
/* 110 */         mob.setPersistenceRequired();
/* 111 */         mob.snapTo(paramBlockPos, 0.0F, 0.0F);
/* 112 */         mob.finalizeSpawn(paramServerLevelAccessor, paramServerLevelAccessor.getCurrentDifficultyAt(mob.blockPosition()), EntitySpawnReason.STRUCTURE, null);
/* 113 */         paramServerLevelAccessor.addFreshEntityWithPassengers((Entity)mob);
/* 114 */         paramServerLevelAccessor.setBlock(paramBlockPos, Blocks.AIR.defaultBlockState(), 2);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\WoodlandMansionPieces$WoodlandMansionPiece.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */