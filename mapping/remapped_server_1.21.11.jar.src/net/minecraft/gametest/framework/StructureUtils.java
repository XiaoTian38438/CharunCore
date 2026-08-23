/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.commands.arguments.blocks.BlockInput;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiTypes;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.TestInstanceBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StructureUtils
/*     */ {
/*     */   public static final int DEFAULT_Y_SEARCH_RADIUS = 10;
/*     */   public static final String DEFAULT_TEST_STRUCTURES_DIR = "Minecraft.Server/src/test/convertables/data";
/*  40 */   public static Path testStructuresDir = Paths.get("Minecraft.Server/src/test/convertables/data", new String[0]);
/*     */   
/*     */   public static Rotation getRotationForRotationSteps(int paramInt) {
/*  43 */     switch (paramInt) {
/*     */       case 0:
/*  45 */         return Rotation.NONE;
/*     */       case 1:
/*  47 */         return Rotation.CLOCKWISE_90;
/*     */       case 2:
/*  49 */         return Rotation.CLOCKWISE_180;
/*     */       case 3:
/*  51 */         return Rotation.COUNTERCLOCKWISE_90;
/*     */     } 
/*  53 */     throw new IllegalArgumentException("rotationSteps must be a value from 0-3. Got value " + paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getRotationStepsForRotation(Rotation paramRotation) {
/*  58 */     switch (paramRotation) {
/*     */       case NONE:
/*  60 */         return 0;
/*     */       case CLOCKWISE_90:
/*  62 */         return 1;
/*     */       case CLOCKWISE_180:
/*  64 */         return 2;
/*     */       case COUNTERCLOCKWISE_90:
/*  66 */         return 3;
/*     */     } 
/*  68 */     throw new IllegalArgumentException("Unknown rotation value, don't know how many steps it represents: " + String.valueOf(paramRotation));
/*     */   }
/*     */ 
/*     */   
/*     */   public static TestInstanceBlockEntity createNewEmptyTest(Identifier paramIdentifier, BlockPos paramBlockPos, Vec3i paramVec3i, Rotation paramRotation, ServerLevel paramServerLevel) {
/*  73 */     BoundingBox boundingBox = getStructureBoundingBox(TestInstanceBlockEntity.getStructurePos(paramBlockPos), paramVec3i, paramRotation);
/*  74 */     clearSpaceForStructure(boundingBox, paramServerLevel);
/*     */     
/*  76 */     paramServerLevel.setBlockAndUpdate(paramBlockPos, Blocks.TEST_INSTANCE_BLOCK.defaultBlockState());
/*     */     
/*  78 */     TestInstanceBlockEntity testInstanceBlockEntity = (TestInstanceBlockEntity)paramServerLevel.getBlockEntity(paramBlockPos);
/*  79 */     ResourceKey resourceKey = ResourceKey.create(Registries.TEST_INSTANCE, paramIdentifier);
/*  80 */     testInstanceBlockEntity.set(new TestInstanceBlockEntity.Data(
/*  81 */           Optional.of(resourceKey), paramVec3i, paramRotation, false, TestInstanceBlockEntity.Status.CLEARED, Optional.empty()));
/*     */ 
/*     */     
/*  84 */     return testInstanceBlockEntity;
/*     */   }
/*     */   
/*     */   public static void clearSpaceForStructure(BoundingBox paramBoundingBox, ServerLevel paramServerLevel) {
/*  88 */     int i = paramBoundingBox.minY() - 1;
/*     */     
/*  90 */     BlockPos.betweenClosedStream(paramBoundingBox).forEach(paramBlockPos -> clearBlock(paramInt, paramBlockPos, paramServerLevel));
/*  91 */     paramServerLevel.getBlockTicks().clearArea(paramBoundingBox);
/*  92 */     paramServerLevel.clearBlockEvents(paramBoundingBox);
/*  93 */     AABB aABB = AABB.of(paramBoundingBox);
/*  94 */     List list = paramServerLevel.getEntitiesOfClass(Entity.class, aABB, paramEntity -> !(paramEntity instanceof net.minecraft.world.entity.player.Player));
/*  95 */     list.forEach(Entity::discard);
/*     */   }
/*     */   
/*     */   public static BlockPos getTransformedFarCorner(BlockPos paramBlockPos, Vec3i paramVec3i, Rotation paramRotation) {
/*  99 */     BlockPos blockPos = paramBlockPos.offset(paramVec3i).offset(-1, -1, -1);
/* 100 */     return StructureTemplate.transform(blockPos, Mirror.NONE, paramRotation, paramBlockPos);
/*     */   }
/*     */   
/*     */   public static BoundingBox getStructureBoundingBox(BlockPos paramBlockPos, Vec3i paramVec3i, Rotation paramRotation) {
/* 104 */     BlockPos blockPos = getTransformedFarCorner(paramBlockPos, paramVec3i, paramRotation);
/* 105 */     BoundingBox boundingBox = BoundingBox.fromCorners((Vec3i)paramBlockPos, (Vec3i)blockPos);
/*     */     
/* 107 */     int i = Math.min(boundingBox.minX(), boundingBox.maxX());
/* 108 */     int j = Math.min(boundingBox.minZ(), boundingBox.maxZ());
/*     */ 
/*     */     
/* 111 */     return boundingBox.move(paramBlockPos.getX() - i, 0, paramBlockPos.getZ() - j);
/*     */   }
/*     */   
/*     */   public static Optional<BlockPos> findTestContainingPos(BlockPos paramBlockPos, int paramInt, ServerLevel paramServerLevel) {
/* 115 */     return findTestBlocks(paramBlockPos, paramInt, paramServerLevel)
/* 116 */       .filter(paramBlockPos2 -> doesStructureContain(paramBlockPos2, paramBlockPos1, paramServerLevel))
/* 117 */       .findFirst();
/*     */   }
/*     */   
/*     */   public static Optional<BlockPos> findNearestTest(BlockPos paramBlockPos, int paramInt, ServerLevel paramServerLevel) {
/* 121 */     Comparator<?> comparator = Comparator.comparingInt(paramBlockPos2 -> paramBlockPos2.distManhattan((Vec3i)paramBlockPos1));
/*     */     
/* 123 */     return (Optional)findTestBlocks(paramBlockPos, paramInt, paramServerLevel).min(comparator);
/*     */   }
/*     */   
/*     */   public static Stream<BlockPos> findTestBlocks(BlockPos paramBlockPos, int paramInt, ServerLevel paramServerLevel) {
/* 127 */     return paramServerLevel.getPoiManager().findAll(paramHolder -> paramHolder.is(PoiTypes.TEST_INSTANCE), paramBlockPos -> true, paramBlockPos, paramInt, PoiManager.Occupancy.ANY)
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 133 */       .map(BlockPos::immutable);
/*     */   }
/*     */   
/*     */   public static Stream<BlockPos> lookedAtTestPos(BlockPos paramBlockPos, Entity paramEntity, ServerLevel paramServerLevel) {
/* 137 */     char c = 'ú';
/* 138 */     Vec3 vec31 = paramEntity.getEyePosition();
/* 139 */     Vec3 vec32 = vec31.add(paramEntity.getLookAngle().scale(250.0D));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 146 */     Objects.requireNonNull(paramBlockPos); return findTestBlocks(paramBlockPos, 250, paramServerLevel).map(paramBlockPos -> paramServerLevel.getBlockEntity(paramBlockPos, BlockEntityType.TEST_INSTANCE_BLOCK)).flatMap(Optional::stream).filter(paramTestInstanceBlockEntity -> paramTestInstanceBlockEntity.getStructureBounds().clip(paramVec31, paramVec32).isPresent()).map(BlockEntity::getBlockPos).sorted(Comparator.comparing(paramBlockPos::distSqr))
/* 147 */       .limit(1L);
/*     */   }
/*     */   
/*     */   private static void clearBlock(int paramInt, BlockPos paramBlockPos, ServerLevel paramServerLevel) {
/*     */     BlockState blockState;
/* 152 */     if (paramBlockPos.getY() < paramInt) {
/* 153 */       blockState = Blocks.STONE.defaultBlockState();
/*     */     } else {
/* 155 */       blockState = Blocks.AIR.defaultBlockState();
/*     */     } 
/* 157 */     BlockInput blockInput = new BlockInput(blockState, Collections.emptySet(), null);
/* 158 */     blockInput.place(paramServerLevel, paramBlockPos, 818);
/* 159 */     paramServerLevel.updateNeighborsAt(paramBlockPos, blockState.getBlock());
/*     */   }
/*     */   
/*     */   private static boolean doesStructureContain(BlockPos paramBlockPos1, BlockPos paramBlockPos2, ServerLevel paramServerLevel) {
/* 163 */     BlockEntity blockEntity = paramServerLevel.getBlockEntity(paramBlockPos1); if (blockEntity instanceof TestInstanceBlockEntity) { TestInstanceBlockEntity testInstanceBlockEntity = (TestInstanceBlockEntity)blockEntity;
/* 164 */       return testInstanceBlockEntity.getStructureBoundingBox().isInside((Vec3i)paramBlockPos2); }
/*     */     
/* 166 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\gametest\framework\StructureUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */