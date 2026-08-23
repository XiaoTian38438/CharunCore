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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public boolean shipCreated;
/*     */   
/*     */   public void init() {
/* 239 */     this.shipCreated = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean generate(StructureTemplateManager paramStructureTemplateManager, int paramInt, EndCityPieces.EndCityPiece paramEndCityPiece, BlockPos paramBlockPos, List<StructurePiece> paramList, RandomSource paramRandomSource) {
/* 244 */     Rotation rotation = paramEndCityPiece.placeSettings().getRotation();
/* 245 */     int i = paramRandomSource.nextInt(4) + 1;
/*     */     
/* 247 */     EndCityPieces.EndCityPiece endCityPiece = EndCityPieces.addHelper(paramList, EndCityPieces.addPiece(paramStructureTemplateManager, paramEndCityPiece, new BlockPos(0, 0, -4), "bridge_piece", rotation, true));
/* 248 */     endCityPiece.setGenDepth(-1);
/* 249 */     byte b1 = 0;
/* 250 */     for (byte b2 = 0; b2 < i; b2++) {
/* 251 */       if (paramRandomSource.nextBoolean()) {
/* 252 */         endCityPiece = EndCityPieces.addHelper(paramList, EndCityPieces.addPiece(paramStructureTemplateManager, endCityPiece, new BlockPos(0, b1, -4), "bridge_piece", rotation, true));
/* 253 */         b1 = 0;
/*     */       } else {
/* 255 */         if (paramRandomSource.nextBoolean()) {
/* 256 */           endCityPiece = EndCityPieces.addHelper(paramList, EndCityPieces.addPiece(paramStructureTemplateManager, endCityPiece, new BlockPos(0, b1, -4), "bridge_steep_stairs", rotation, true));
/*     */         } else {
/* 258 */           endCityPiece = EndCityPieces.addHelper(paramList, EndCityPieces.addPiece(paramStructureTemplateManager, endCityPiece, new BlockPos(0, b1, -8), "bridge_gentle_stairs", rotation, true));
/*     */         } 
/* 260 */         b1 = 4;
/*     */       } 
/*     */     } 
/*     */     
/* 264 */     if (this.shipCreated || paramRandomSource.nextInt(10 - paramInt) != 0) {
/* 265 */       if (!EndCityPieces.recursiveChildren(paramStructureTemplateManager, EndCityPieces.HOUSE_TOWER_GENERATOR, paramInt + 1, endCityPiece, new BlockPos(-3, b1 + 1, -11), paramList, paramRandomSource)) {
/* 266 */         return false;
/*     */       }
/*     */     } else {
/*     */       
/* 270 */       EndCityPieces.addHelper(paramList, EndCityPieces.addPiece(paramStructureTemplateManager, endCityPiece, new BlockPos(-8 + paramRandomSource.nextInt(8), b1, -70 + paramRandomSource.nextInt(10)), "ship", rotation, true));
/* 271 */       this.shipCreated = true;
/*     */     } 
/*     */ 
/*     */     
/* 275 */     endCityPiece = EndCityPieces.addHelper(paramList, EndCityPieces.addPiece(paramStructureTemplateManager, endCityPiece, new BlockPos(4, b1, 0), "bridge_end", rotation.getRotated(Rotation.CLOCKWISE_180), true));
/* 276 */     endCityPiece.setGenDepth(-1);
/*     */     
/* 278 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\EndCityPieces$3.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */