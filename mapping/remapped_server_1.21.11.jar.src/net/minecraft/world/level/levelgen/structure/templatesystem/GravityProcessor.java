/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ 
/*    */ public class GravityProcessor extends StructureProcessor {
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)Heightmap.Types.CODEC.fieldOf("heightmap").orElse(Heightmap.Types.WORLD_SURFACE_WG).forGetter(()), (App)Codec.INT.fieldOf("offset").orElse(Integer.valueOf(0)).forGetter(())).apply((Applicative)paramInstance, GravityProcessor::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<GravityProcessor> CODEC;
/*    */   private final Heightmap.Types heightmap;
/*    */   private final int offset;
/*    */   
/*    */   public GravityProcessor(Heightmap.Types paramTypes, int paramInt) {
/* 22 */     this.heightmap = paramTypes;
/* 23 */     this.offset = paramInt;
/*    */   }
/*    */ 
/*    */   
/*    */   public StructureTemplate.StructureBlockInfo processBlock(LevelReader paramLevelReader, BlockPos paramBlockPos1, BlockPos paramBlockPos2, StructureTemplate.StructureBlockInfo paramStructureBlockInfo1, StructureTemplate.StructureBlockInfo paramStructureBlockInfo2, StructurePlaceSettings paramStructurePlaceSettings) {
/*    */     Heightmap.Types types;
/* 29 */     if (paramLevelReader instanceof net.minecraft.server.level.ServerLevel) {
/*    */       
/* 31 */       if (this.heightmap == Heightmap.Types.WORLD_SURFACE_WG) {
/* 32 */         types = Heightmap.Types.WORLD_SURFACE;
/* 33 */       } else if (this.heightmap == Heightmap.Types.OCEAN_FLOOR_WG) {
/* 34 */         types = Heightmap.Types.OCEAN_FLOOR;
/*    */       } else {
/* 36 */         types = this.heightmap;
/*    */       } 
/*    */     } else {
/* 39 */       types = this.heightmap;
/*    */     } 
/* 41 */     BlockPos blockPos = paramStructureBlockInfo2.pos();
/* 42 */     int i = paramLevelReader.getHeight(types, blockPos.getX(), blockPos.getZ()) + this.offset;
/* 43 */     int j = paramStructureBlockInfo1.pos().getY();
/* 44 */     return new StructureTemplate.StructureBlockInfo(new BlockPos(blockPos.getX(), i + j, blockPos.getZ()), paramStructureBlockInfo2.state(), paramStructureBlockInfo2.nbt());
/*    */   }
/*    */ 
/*    */   
/*    */   protected StructureProcessorType<?> getType() {
/* 49 */     return StructureProcessorType.GRAVITY;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\GravityProcessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */