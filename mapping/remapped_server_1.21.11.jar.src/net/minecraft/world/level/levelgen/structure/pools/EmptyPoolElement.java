/*    */ package net.minecraft.world.level.levelgen.structure.pools;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.StructureManager;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*    */ 
/*    */ public class EmptyPoolElement
/*    */   extends StructurePoolElement {
/* 20 */   public static final MapCodec<EmptyPoolElement> CODEC = MapCodec.unit(() -> INSTANCE);
/*    */   
/* 22 */   public static final EmptyPoolElement INSTANCE = new EmptyPoolElement();
/*    */   
/*    */   private EmptyPoolElement() {
/* 25 */     super(StructureTemplatePool.Projection.TERRAIN_MATCHING);
/*    */   }
/*    */ 
/*    */   
/*    */   public Vec3i getSize(StructureTemplateManager paramStructureTemplateManager, Rotation paramRotation) {
/* 30 */     return Vec3i.ZERO;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<StructureTemplate.JigsawBlockInfo> getShuffledJigsawBlocks(StructureTemplateManager paramStructureTemplateManager, BlockPos paramBlockPos, Rotation paramRotation, RandomSource paramRandomSource) {
/* 35 */     return Collections.emptyList();
/*    */   }
/*    */ 
/*    */   
/*    */   public BoundingBox getBoundingBox(StructureTemplateManager paramStructureTemplateManager, BlockPos paramBlockPos, Rotation paramRotation) {
/* 40 */     throw new IllegalStateException("Invalid call to EmptyPoolElement.getBoundingBox, filter me!");
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(StructureTemplateManager paramStructureTemplateManager, WorldGenLevel paramWorldGenLevel, StructureManager paramStructureManager, ChunkGenerator paramChunkGenerator, BlockPos paramBlockPos1, BlockPos paramBlockPos2, Rotation paramRotation, BoundingBox paramBoundingBox, RandomSource paramRandomSource, LiquidSettings paramLiquidSettings, boolean paramBoolean) {
/* 45 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public StructurePoolElementType<?> getType() {
/* 50 */     return StructurePoolElementType.EMPTY;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 55 */     return "Empty";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\pools\EmptyPoolElement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */