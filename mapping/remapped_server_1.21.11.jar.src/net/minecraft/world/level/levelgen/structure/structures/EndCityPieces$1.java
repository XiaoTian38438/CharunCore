/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.util.RandomSource;
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
/*     */ class null
/*     */   implements EndCityPieces.SectionGenerator
/*     */ {
/*     */   public void init() {}
/*     */   
/*     */   public boolean generate(StructureTemplateManager paramStructureTemplateManager, int paramInt, EndCityPieces.EndCityPiece paramEndCityPiece, BlockPos paramBlockPos, List<StructurePiece> paramList, RandomSource paramRandomSource) {
/* 158 */     if (paramInt > 8) {
/* 159 */       return false;
/*     */     }
/*     */     
/* 162 */     Rotation rotation = paramEndCityPiece.placeSettings().getRotation();
/* 163 */     EndCityPieces.EndCityPiece endCityPiece = EndCityPieces.addHelper(paramList, EndCityPieces.addPiece(paramStructureTemplateManager, paramEndCityPiece, paramBlockPos, "base_floor", rotation, true));
/*     */     
/* 165 */     int i = paramRandomSource.nextInt(3);
/* 166 */     if (i == 0) {
/* 167 */       endCityPiece = EndCityPieces.addHelper(paramList, EndCityPieces.addPiece(paramStructureTemplateManager, endCityPiece, new BlockPos(-1, 4, -1), "base_roof", rotation, true));
/* 168 */     } else if (i == 1) {
/* 169 */       endCityPiece = EndCityPieces.addHelper(paramList, EndCityPieces.addPiece(paramStructureTemplateManager, endCityPiece, new BlockPos(-1, 0, -1), "second_floor_2", rotation, false));
/* 170 */       endCityPiece = EndCityPieces.addHelper(paramList, EndCityPieces.addPiece(paramStructureTemplateManager, endCityPiece, new BlockPos(-1, 8, -1), "second_roof", rotation, false));
/*     */       
/* 172 */       EndCityPieces.recursiveChildren(paramStructureTemplateManager, EndCityPieces.TOWER_GENERATOR, paramInt + 1, endCityPiece, null, paramList, paramRandomSource);
/* 173 */     } else if (i == 2) {
/* 174 */       endCityPiece = EndCityPieces.addHelper(paramList, EndCityPieces.addPiece(paramStructureTemplateManager, endCityPiece, new BlockPos(-1, 0, -1), "second_floor_2", rotation, false));
/* 175 */       endCityPiece = EndCityPieces.addHelper(paramList, EndCityPieces.addPiece(paramStructureTemplateManager, endCityPiece, new BlockPos(-1, 4, -1), "third_floor_2", rotation, false));
/* 176 */       endCityPiece = EndCityPieces.addHelper(paramList, EndCityPieces.addPiece(paramStructureTemplateManager, endCityPiece, new BlockPos(-1, 8, -1), "third_roof", rotation, true));
/*     */       
/* 178 */       EndCityPieces.recursiveChildren(paramStructureTemplateManager, EndCityPieces.TOWER_GENERATOR, paramInt + 1, endCityPiece, null, paramList, paramRandomSource);
/*     */     } 
/* 180 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\EndCityPieces$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */