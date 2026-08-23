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
/*     */ class null
/*     */   implements EndCityPieces.SectionGenerator
/*     */ {
/*     */   public void init() {}
/*     */   
/*     */   public boolean generate(StructureTemplateManager paramStructureTemplateManager, int paramInt, EndCityPieces.EndCityPiece paramEndCityPiece, BlockPos paramBlockPos, List<StructurePiece> paramList, RandomSource paramRandomSource) {
/* 198 */     Rotation rotation = paramEndCityPiece.placeSettings().getRotation();
/* 199 */     EndCityPieces.EndCityPiece endCityPiece1 = paramEndCityPiece;
/* 200 */     endCityPiece1 = EndCityPieces.addHelper(paramList, EndCityPieces.addPiece(paramStructureTemplateManager, endCityPiece1, new BlockPos(3 + paramRandomSource.nextInt(2), -3, 3 + paramRandomSource.nextInt(2)), "tower_base", rotation, true));
/* 201 */     endCityPiece1 = EndCityPieces.addHelper(paramList, EndCityPieces.addPiece(paramStructureTemplateManager, endCityPiece1, new BlockPos(0, 7, 0), "tower_piece", rotation, true));
/*     */     
/* 203 */     EndCityPieces.EndCityPiece endCityPiece2 = (paramRandomSource.nextInt(3) == 0) ? endCityPiece1 : null;
/*     */     
/* 205 */     int i = 1 + paramRandomSource.nextInt(3);
/* 206 */     for (byte b = 0; b < i; b++) {
/* 207 */       endCityPiece1 = EndCityPieces.addHelper(paramList, EndCityPieces.addPiece(paramStructureTemplateManager, endCityPiece1, new BlockPos(0, 4, 0), "tower_piece", rotation, true));
/* 208 */       if (b < i - 1 && paramRandomSource.nextBoolean()) {
/* 209 */         endCityPiece2 = endCityPiece1;
/*     */       }
/*     */     } 
/*     */     
/* 213 */     if (endCityPiece2 != null) {
/* 214 */       for (Tuple<Rotation, BlockPos> tuple : EndCityPieces.TOWER_BRIDGES) {
/* 215 */         if (paramRandomSource.nextBoolean()) {
/*     */           
/* 217 */           EndCityPieces.EndCityPiece endCityPiece = EndCityPieces.addHelper(paramList, EndCityPieces.addPiece(paramStructureTemplateManager, endCityPiece2, (BlockPos)tuple.getB(), "bridge_end", rotation.getRotated((Rotation)tuple.getA()), true));
/* 218 */           EndCityPieces.recursiveChildren(paramStructureTemplateManager, EndCityPieces.TOWER_BRIDGE_GENERATOR, paramInt + 1, endCityPiece, null, paramList, paramRandomSource);
/*     */         } 
/*     */       } 
/*     */       
/* 222 */       endCityPiece1 = EndCityPieces.addHelper(paramList, EndCityPieces.addPiece(paramStructureTemplateManager, endCityPiece1, new BlockPos(-1, 4, -1), "tower_top", rotation, true));
/*     */     }
/* 224 */     else if (paramInt == 7) {
/* 225 */       endCityPiece1 = EndCityPieces.addHelper(paramList, EndCityPieces.addPiece(paramStructureTemplateManager, endCityPiece1, new BlockPos(-1, 4, -1), "tower_top", rotation, true));
/*     */     } else {
/* 227 */       return EndCityPieces.recursiveChildren(paramStructureTemplateManager, EndCityPieces.FAT_TOWER_GENERATOR, paramInt + 1, endCityPiece1, null, paramList, paramRandomSource);
/*     */     } 
/*     */     
/* 230 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\EndCityPieces$2.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */