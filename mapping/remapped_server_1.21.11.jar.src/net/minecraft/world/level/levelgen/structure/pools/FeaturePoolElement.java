/*    */ package net.minecraft.world.level.levelgen.structure.pools;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.data.worldgen.Pools;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.StructureManager;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.JigsawBlock;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ import net.minecraft.world.level.block.entity.JigsawBlockEntity;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*    */ 
/*    */ public class FeaturePoolElement extends StructurePoolElement {
/*    */   static {
/* 30 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)PlacedFeature.CODEC.fieldOf("feature").forGetter(()), (App)projectionCodec()).apply((Applicative)paramInstance, FeaturePoolElement::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<FeaturePoolElement> CODEC;
/* 35 */   private static final Identifier DEFAULT_JIGSAW_NAME = Identifier.withDefaultNamespace("bottom");
/*    */   
/*    */   private final Holder<PlacedFeature> feature;
/*    */   private final CompoundTag defaultJigsawNBT;
/*    */   
/*    */   protected FeaturePoolElement(Holder<PlacedFeature> paramHolder, StructureTemplatePool.Projection paramProjection) {
/* 41 */     super(paramProjection);
/* 42 */     this.feature = paramHolder;
/* 43 */     this.defaultJigsawNBT = fillDefaultJigsawNBT();
/*    */   }
/*    */   
/*    */   private CompoundTag fillDefaultJigsawNBT() {
/* 47 */     CompoundTag compoundTag = new CompoundTag();
/* 48 */     compoundTag.store("name", Identifier.CODEC, DEFAULT_JIGSAW_NAME);
/* 49 */     compoundTag.putString("final_state", "minecraft:air");
/*    */ 
/*    */     
/* 52 */     compoundTag.store("pool", JigsawBlockEntity.POOL_CODEC, Pools.EMPTY);
/* 53 */     compoundTag.store("target", Identifier.CODEC, JigsawBlockEntity.EMPTY_ID);
/* 54 */     compoundTag.store("joint", (Codec)JigsawBlockEntity.JointType.CODEC, JigsawBlockEntity.JointType.ROLLABLE);
/*    */     
/* 56 */     return compoundTag;
/*    */   }
/*    */ 
/*    */   
/*    */   public Vec3i getSize(StructureTemplateManager paramStructureTemplateManager, Rotation paramRotation) {
/* 61 */     return Vec3i.ZERO;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<StructureTemplate.JigsawBlockInfo> getShuffledJigsawBlocks(StructureTemplateManager paramStructureTemplateManager, BlockPos paramBlockPos, Rotation paramRotation, RandomSource paramRandomSource) {
/* 66 */     return List.of(StructureTemplate.JigsawBlockInfo.of(new StructureTemplate.StructureBlockInfo(paramBlockPos, (BlockState)Blocks.JIGSAW.defaultBlockState().setValue((Property)JigsawBlock.ORIENTATION, (Comparable)FrontAndTop.fromFrontAndTop(Direction.DOWN, Direction.SOUTH)), this.defaultJigsawNBT)));
/*    */   }
/*    */ 
/*    */   
/*    */   public BoundingBox getBoundingBox(StructureTemplateManager paramStructureTemplateManager, BlockPos paramBlockPos, Rotation paramRotation) {
/* 71 */     Vec3i vec3i = getSize(paramStructureTemplateManager, paramRotation);
/* 72 */     return new BoundingBox(paramBlockPos.getX(), paramBlockPos.getY(), paramBlockPos.getZ(), paramBlockPos.getX() + vec3i.getX(), paramBlockPos.getY() + vec3i.getY(), paramBlockPos.getZ() + vec3i.getZ());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(StructureTemplateManager paramStructureTemplateManager, WorldGenLevel paramWorldGenLevel, StructureManager paramStructureManager, ChunkGenerator paramChunkGenerator, BlockPos paramBlockPos1, BlockPos paramBlockPos2, Rotation paramRotation, BoundingBox paramBoundingBox, RandomSource paramRandomSource, LiquidSettings paramLiquidSettings, boolean paramBoolean) {
/* 77 */     return ((PlacedFeature)this.feature.value()).place(paramWorldGenLevel, paramChunkGenerator, paramRandomSource, paramBlockPos1);
/*    */   }
/*    */ 
/*    */   
/*    */   public StructurePoolElementType<?> getType() {
/* 82 */     return StructurePoolElementType.FEATURE;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 87 */     return "Feature[" + String.valueOf(this.feature) + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\pools\FeaturePoolElement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */