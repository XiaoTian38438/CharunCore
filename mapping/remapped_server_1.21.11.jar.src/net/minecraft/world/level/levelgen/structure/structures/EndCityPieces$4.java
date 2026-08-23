/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Tuple;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class null
/*     */   implements EndCityPieces.SectionGenerator
/*     */ {
/*     */   public void init() {}
/*     */   
/*     */   public boolean generate(StructureTemplateManager paramStructureTemplateManager, int paramInt, EndCityPieces.EndCityPiece paramEndCityPiece, BlockPos paramBlockPos, List<StructurePiece> paramList, RandomSource paramRandomSource) {
/* 297 */     Rotation rotation = paramEndCityPiece.placeSettings().getRotation();
/*     */     
/* 299 */     EndCityPieces.EndCityPiece endCityPiece = EndCityPieces.addHelper(paramList, EndCityPieces.addPiece(paramStructureTemplateManager, paramEndCityPiece, new BlockPos(-3, 4, -3), "fat_tower_base", rotation, true));
/* 300 */     endCityPiece = EndCityPieces.addHelper(paramList, EndCityPieces.addPiece(paramStructureTemplateManager, endCityPiece, new BlockPos(0, 4, 0), "fat_tower_middle", rotation, true));
/* 301 */     for (byte b = 0; b < 2 && 
/* 302 */       paramRandomSource.nextInt(3) != 0; b++) {
/*     */ 
/*     */       
/* 305 */       endCityPiece = EndCityPieces.addHelper(paramList, EndCityPieces.addPiece(paramStructureTemplateManager, endCityPiece, new BlockPos(0, 8, 0), "fat_tower_middle", rotation, true));
/*     */       
/* 307 */       for (Tuple<Rotation, BlockPos> tuple : EndCityPieces.FAT_TOWER_BRIDGES) {
/* 308 */         if (paramRandomSource.nextBoolean()) {
/*     */           
/* 310 */           EndCityPieces.EndCityPiece endCityPiece1 = EndCityPieces.addHelper(paramList, EndCityPieces.addPiece(paramStructureTemplateManager, endCityPiece, (BlockPos)tuple.getB(), "bridge_end", rotation.getRotated((Rotation)tuple.getA()), true));
/* 311 */           EndCityPieces.recursiveChildren(paramStructureTemplateManager, EndCityPieces.TOWER_BRIDGE_GENERATOR, paramInt + 1, endCityPiece1, null, paramList, paramRandomSource);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 316 */     endCityPiece = EndCityPieces.addHelper(paramList, EndCityPieces.addPiece(paramStructureTemplateManager, endCityPiece, new BlockPos(-2, 8, -2), "fat_tower_top", rotation, true));
/* 317 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\EndCityPieces$4.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */