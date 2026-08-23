/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.RandomizableContainer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.decoration.ItemFrame;
/*    */ import net.minecraft.world.entity.monster.Shulker;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.ServerLevelAccessor;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*    */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EndCityPiece
/*    */   extends TemplateStructurePiece
/*    */ {
/*    */   public EndCityPiece(StructureTemplateManager paramStructureTemplateManager, String paramString, BlockPos paramBlockPos, Rotation paramRotation, boolean paramBoolean) {
/* 45 */     super(StructurePieceType.END_CITY_PIECE, 0, paramStructureTemplateManager, makeIdentifier(paramString), paramString, makeSettings(paramBoolean, paramRotation), paramBlockPos);
/*    */   }
/*    */   
/*    */   public EndCityPiece(StructureTemplateManager paramStructureTemplateManager, CompoundTag paramCompoundTag) {
/* 49 */     super(StructurePieceType.END_CITY_PIECE, paramCompoundTag, paramStructureTemplateManager, paramIdentifier -> makeSettings(paramCompoundTag.getBooleanOr("OW", false), paramCompoundTag.read("Rot", Rotation.LEGACY_CODEC).orElseThrow()));
/*    */   }
/*    */   
/*    */   private static StructurePlaceSettings makeSettings(boolean paramBoolean, Rotation paramRotation) {
/* 53 */     BlockIgnoreProcessor blockIgnoreProcessor = paramBoolean ? BlockIgnoreProcessor.STRUCTURE_BLOCK : BlockIgnoreProcessor.STRUCTURE_AND_AIR;
/* 54 */     return (new StructurePlaceSettings()).setIgnoreEntities(true).addProcessor((StructureProcessor)blockIgnoreProcessor).setRotation(paramRotation);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Identifier makeTemplateLocation() {
/* 59 */     return makeIdentifier(this.templateName);
/*    */   }
/*    */   
/*    */   private static Identifier makeIdentifier(String paramString) {
/* 63 */     return Identifier.withDefaultNamespace("end_city/" + paramString);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void addAdditionalSaveData(StructurePieceSerializationContext paramStructurePieceSerializationContext, CompoundTag paramCompoundTag) {
/* 68 */     super.addAdditionalSaveData(paramStructurePieceSerializationContext, paramCompoundTag);
/*    */     
/* 70 */     paramCompoundTag.store("Rot", Rotation.LEGACY_CODEC, this.placeSettings.getRotation());
/* 71 */     paramCompoundTag.putBoolean("OW", (this.placeSettings.getProcessors().get(0) == BlockIgnoreProcessor.STRUCTURE_BLOCK));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void handleDataMarker(String paramString, BlockPos paramBlockPos, ServerLevelAccessor paramServerLevelAccessor, RandomSource paramRandomSource, BoundingBox paramBoundingBox) {
/* 76 */     if (paramString.startsWith("Chest")) {
/* 77 */       BlockPos blockPos = paramBlockPos.below();
/*    */       
/* 79 */       if (paramBoundingBox.isInside((Vec3i)blockPos)) {
/* 80 */         RandomizableContainer.setBlockEntityLootTable((BlockGetter)paramServerLevelAccessor, paramRandomSource, blockPos, BuiltInLootTables.END_CITY_TREASURE);
/*    */       }
/* 82 */     } else if (paramBoundingBox.isInside((Vec3i)paramBlockPos) && Level.isInSpawnableBounds(paramBlockPos)) {
/* 83 */       if (paramString.startsWith("Sentry")) {
/* 84 */         Shulker shulker = (Shulker)EntityType.SHULKER.create((Level)paramServerLevelAccessor.getLevel(), EntitySpawnReason.STRUCTURE);
/* 85 */         if (shulker != null) {
/* 86 */           shulker.setPos(paramBlockPos.getX() + 0.5D, paramBlockPos.getY(), paramBlockPos.getZ() + 0.5D);
/* 87 */           paramServerLevelAccessor.addFreshEntity((Entity)shulker);
/*    */         } 
/* 89 */       } else if (paramString.startsWith("Elytra")) {
/* 90 */         ItemFrame itemFrame = new ItemFrame((Level)paramServerLevelAccessor.getLevel(), paramBlockPos, this.placeSettings.getRotation().rotate(Direction.SOUTH));
/* 91 */         itemFrame.setItem(new ItemStack((ItemLike)Items.ELYTRA), false);
/* 92 */         paramServerLevelAccessor.addFreshEntity((Entity)itemFrame);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\EndCityPieces$EndCityPiece.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */