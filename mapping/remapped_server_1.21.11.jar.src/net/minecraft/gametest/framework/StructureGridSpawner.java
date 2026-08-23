/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StructureGridSpawner
/*    */   implements GameTestRunner.StructureSpawner
/*    */ {
/*    */   private static final int SPACE_BETWEEN_COLUMNS = 5;
/*    */   private static final int SPACE_BETWEEN_ROWS = 6;
/*    */   private final int testsPerRow;
/*    */   private int currentRowCount;
/*    */   private AABB rowBounds;
/*    */   private final BlockPos.MutableBlockPos nextTestNorthWestCorner;
/*    */   private final BlockPos firstTestNorthWestCorner;
/*    */   private final boolean clearOnBatch;
/* 27 */   private float maxX = -1.0F;
/* 28 */   private final Collection<GameTestInfo> testInLastBatch = new ArrayList<>();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public StructureGridSpawner(BlockPos paramBlockPos, int paramInt, boolean paramBoolean) {
/* 34 */     this.testsPerRow = paramInt;
/* 35 */     this.nextTestNorthWestCorner = paramBlockPos.mutable();
/* 36 */     this.rowBounds = new AABB((BlockPos)this.nextTestNorthWestCorner);
/* 37 */     this.firstTestNorthWestCorner = paramBlockPos;
/* 38 */     this.clearOnBatch = paramBoolean;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onBatchStart(ServerLevel paramServerLevel) {
/* 43 */     if (this.clearOnBatch) {
/* 44 */       this.testInLastBatch.forEach(paramGameTestInfo -> {
/*    */             BoundingBox boundingBox = paramGameTestInfo.getTestInstanceBlockEntity().getStructureBoundingBox();
/*    */             StructureUtils.clearSpaceForStructure(boundingBox, paramServerLevel);
/*    */           });
/* 48 */       this.testInLastBatch.clear();
/* 49 */       this.rowBounds = new AABB(this.firstTestNorthWestCorner);
/* 50 */       this.nextTestNorthWestCorner.set((Vec3i)this.firstTestNorthWestCorner);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<GameTestInfo> spawnStructure(GameTestInfo paramGameTestInfo) {
/* 56 */     BlockPos blockPos = new BlockPos((Vec3i)this.nextTestNorthWestCorner);
/* 57 */     paramGameTestInfo.setTestBlockPos(blockPos);
/* 58 */     GameTestInfo gameTestInfo = paramGameTestInfo.prepareTestStructure();
/* 59 */     if (gameTestInfo == null) {
/* 60 */       return Optional.empty();
/*    */     }
/* 62 */     gameTestInfo.startExecution(1);
/*    */     
/* 64 */     AABB aABB = paramGameTestInfo.getTestInstanceBlockEntity().getStructureBounds();
/* 65 */     this.rowBounds = this.rowBounds.minmax(aABB);
/*    */     
/* 67 */     this.nextTestNorthWestCorner.move((int)aABB.getXsize() + 5, 0, 0);
/* 68 */     if (this.nextTestNorthWestCorner.getX() > this.maxX) {
/* 69 */       this.maxX = this.nextTestNorthWestCorner.getX();
/*    */     }
/*    */     
/* 72 */     if (++this.currentRowCount >= this.testsPerRow) {
/*    */       
/* 74 */       this.currentRowCount = 0;
/* 75 */       this.nextTestNorthWestCorner.move(0, 0, (int)this.rowBounds.getZsize() + 6);
/* 76 */       this.nextTestNorthWestCorner.setX(this.firstTestNorthWestCorner.getX());
/* 77 */       this.rowBounds = new AABB((BlockPos)this.nextTestNorthWestCorner);
/*    */     } 
/*    */     
/* 80 */     this.testInLastBatch.add(paramGameTestInfo);
/* 81 */     return Optional.of(paramGameTestInfo);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\gametest\framework\StructureGridSpawner.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */