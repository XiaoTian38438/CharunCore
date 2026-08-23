/*     */ package net.minecraft.world.level.portal;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.BlockUtil;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.NetherPortalBlock;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ import org.apache.commons.lang3.mutable.MutableInt;
/*     */ 
/*     */ public class PortalShape
/*     */ {
/*     */   private static final int MIN_WIDTH = 2;
/*     */   public static final int MAX_WIDTH = 21;
/*     */   private static final int MIN_HEIGHT = 3;
/*     */   
/*     */   static {
/*  34 */     FRAME = ((paramBlockState, paramBlockGetter, paramBlockPos) -> paramBlockState.is(Blocks.OBSIDIAN));
/*     */   }
/*     */   public static final int MAX_HEIGHT = 21; private static final BlockBehaviour.StatePredicate FRAME; private static final float SAFE_TRAVEL_MAX_ENTITY_XY = 4.0F;
/*     */   private static final double SAFE_TRAVEL_MAX_VERTICAL_DELTA = 1.0D;
/*     */   private final Direction.Axis axis;
/*     */   private final Direction rightDir;
/*     */   private final int numPortalBlocks;
/*     */   private final BlockPos bottomLeft;
/*     */   private final int height;
/*     */   private final int width;
/*     */   
/*     */   private PortalShape(Direction.Axis paramAxis, int paramInt1, Direction paramDirection, BlockPos paramBlockPos, int paramInt2, int paramInt3) {
/*  46 */     this.axis = paramAxis;
/*  47 */     this.numPortalBlocks = paramInt1;
/*  48 */     this.rightDir = paramDirection;
/*  49 */     this.bottomLeft = paramBlockPos;
/*  50 */     this.width = paramInt2;
/*  51 */     this.height = paramInt3;
/*     */   }
/*     */   
/*     */   public static Optional<PortalShape> findEmptyPortalShape(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, Direction.Axis paramAxis) {
/*  55 */     return findPortalShape(paramLevelAccessor, paramBlockPos, paramPortalShape -> (paramPortalShape.isValid() && paramPortalShape.numPortalBlocks == 0), paramAxis);
/*     */   }
/*     */   
/*     */   public static Optional<PortalShape> findPortalShape(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, Predicate<PortalShape> paramPredicate, Direction.Axis paramAxis) {
/*  59 */     Optional<PortalShape> optional = Optional.<PortalShape>of(findAnyShape((BlockGetter)paramLevelAccessor, paramBlockPos, paramAxis)).filter(paramPredicate);
/*  60 */     if (optional.isPresent()) {
/*  61 */       return optional;
/*     */     }
/*     */     
/*  64 */     Direction.Axis axis = (paramAxis == Direction.Axis.X) ? Direction.Axis.Z : Direction.Axis.X;
/*  65 */     return Optional.<PortalShape>of(findAnyShape((BlockGetter)paramLevelAccessor, paramBlockPos, axis)).filter(paramPredicate);
/*     */   }
/*     */   
/*     */   public static PortalShape findAnyShape(BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction.Axis paramAxis) {
/*  69 */     Direction direction = (paramAxis == Direction.Axis.X) ? Direction.WEST : Direction.SOUTH;
/*     */ 
/*     */ 
/*     */     
/*  73 */     BlockPos blockPos = calculateBottomLeft(paramBlockGetter, direction, paramBlockPos);
/*  74 */     if (blockPos == null) {
/*  75 */       return new PortalShape(paramAxis, 0, direction, paramBlockPos, 0, 0);
/*     */     }
/*     */     
/*  78 */     int i = calculateWidth(paramBlockGetter, blockPos, direction);
/*  79 */     if (i == 0) {
/*  80 */       return new PortalShape(paramAxis, 0, direction, blockPos, 0, 0);
/*     */     }
/*     */     
/*  83 */     MutableInt mutableInt = new MutableInt();
/*  84 */     int j = calculateHeight(paramBlockGetter, blockPos, direction, i, mutableInt);
/*  85 */     return new PortalShape(paramAxis, mutableInt.intValue(), direction, blockPos, i, j);
/*     */   }
/*     */ 
/*     */   
/*     */   private static BlockPos calculateBottomLeft(BlockGetter paramBlockGetter, Direction paramDirection, BlockPos paramBlockPos) {
/*  90 */     int i = Math.max(paramBlockGetter.getMinY(), paramBlockPos.getY() - 21);
/*  91 */     while (paramBlockPos.getY() > i && isEmpty(paramBlockGetter.getBlockState(paramBlockPos.below()))) {
/*  92 */       paramBlockPos = paramBlockPos.below();
/*     */     }
/*     */     
/*  95 */     Direction direction = paramDirection.getOpposite();
/*  96 */     int j = getDistanceUntilEdgeAboveFrame(paramBlockGetter, paramBlockPos, direction) - 1;
/*  97 */     if (j < 0) {
/*  98 */       return null;
/*     */     }
/* 100 */     return paramBlockPos.relative(direction, j);
/*     */   }
/*     */   
/*     */   private static int calculateWidth(BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection) {
/* 104 */     int i = getDistanceUntilEdgeAboveFrame(paramBlockGetter, paramBlockPos, paramDirection);
/*     */     
/* 106 */     if (i < 2 || i > 21) {
/* 107 */       return 0;
/*     */     }
/*     */     
/* 110 */     return i;
/*     */   }
/*     */   
/*     */   private static int getDistanceUntilEdgeAboveFrame(BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection) {
/* 114 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/*     */     
/* 116 */     for (byte b = 0; b <= 21; b++) {
/* 117 */       mutableBlockPos.set((Vec3i)paramBlockPos).move(paramDirection, b);
/*     */       
/* 119 */       BlockState blockState1 = paramBlockGetter.getBlockState((BlockPos)mutableBlockPos);
/* 120 */       if (!isEmpty(blockState1)) {
/* 121 */         if (FRAME.test(blockState1, paramBlockGetter, (BlockPos)mutableBlockPos)) {
/* 122 */           return b;
/*     */         }
/*     */         
/*     */         break;
/*     */       } 
/*     */       
/* 128 */       BlockState blockState2 = paramBlockGetter.getBlockState((BlockPos)mutableBlockPos.move(Direction.DOWN));
/* 129 */       if (!FRAME.test(blockState2, paramBlockGetter, (BlockPos)mutableBlockPos)) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */     
/* 134 */     return 0;
/*     */   }
/*     */   
/*     */   private static int calculateHeight(BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection, int paramInt, MutableInt paramMutableInt) {
/* 138 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/* 139 */     int i = getDistanceUntilTop(paramBlockGetter, paramBlockPos, paramDirection, mutableBlockPos, paramInt, paramMutableInt);
/*     */     
/* 141 */     if (i < 3 || i > 21 || !hasTopFrame(paramBlockGetter, paramBlockPos, paramDirection, mutableBlockPos, paramInt, i)) {
/* 142 */       return 0;
/*     */     }
/*     */     
/* 145 */     return i;
/*     */   }
/*     */   
/*     */   private static boolean hasTopFrame(BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection, BlockPos.MutableBlockPos paramMutableBlockPos, int paramInt1, int paramInt2) {
/* 149 */     for (byte b = 0; b < paramInt1; b++) {
/* 150 */       BlockPos.MutableBlockPos mutableBlockPos = paramMutableBlockPos.set((Vec3i)paramBlockPos).move(Direction.UP, paramInt2).move(paramDirection, b);
/* 151 */       if (!FRAME.test(paramBlockGetter.getBlockState((BlockPos)mutableBlockPos), paramBlockGetter, (BlockPos)mutableBlockPos)) {
/* 152 */         return false;
/*     */       }
/*     */     } 
/* 155 */     return true;
/*     */   }
/*     */   
/*     */   private static int getDistanceUntilTop(BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection, BlockPos.MutableBlockPos paramMutableBlockPos, int paramInt, MutableInt paramMutableInt) {
/* 159 */     for (byte b = 0; b < 21; b++) {
/*     */       
/* 161 */       paramMutableBlockPos.set((Vec3i)paramBlockPos).move(Direction.UP, b).move(paramDirection, -1);
/* 162 */       if (!FRAME.test(paramBlockGetter.getBlockState((BlockPos)paramMutableBlockPos), paramBlockGetter, (BlockPos)paramMutableBlockPos)) {
/* 163 */         return b;
/*     */       }
/*     */ 
/*     */       
/* 167 */       paramMutableBlockPos.set((Vec3i)paramBlockPos).move(Direction.UP, b).move(paramDirection, paramInt);
/* 168 */       if (!FRAME.test(paramBlockGetter.getBlockState((BlockPos)paramMutableBlockPos), paramBlockGetter, (BlockPos)paramMutableBlockPos)) {
/* 169 */         return b;
/*     */       }
/*     */ 
/*     */       
/* 173 */       for (byte b1 = 0; b1 < paramInt; b1++) {
/* 174 */         paramMutableBlockPos.set((Vec3i)paramBlockPos).move(Direction.UP, b).move(paramDirection, b1);
/*     */         
/* 176 */         BlockState blockState = paramBlockGetter.getBlockState((BlockPos)paramMutableBlockPos);
/* 177 */         if (!isEmpty(blockState)) {
/* 178 */           return b;
/*     */         }
/*     */         
/* 181 */         if (blockState.is(Blocks.NETHER_PORTAL)) {
/* 182 */           paramMutableInt.increment();
/*     */         }
/*     */       } 
/*     */     } 
/* 186 */     return 21;
/*     */   }
/*     */   
/*     */   private static boolean isEmpty(BlockState paramBlockState) {
/* 190 */     return (paramBlockState.isAir() || paramBlockState.is(BlockTags.FIRE) || paramBlockState.is(Blocks.NETHER_PORTAL));
/*     */   }
/*     */   
/*     */   public boolean isValid() {
/* 194 */     return (this.width >= 2 && this.width <= 21 && this.height >= 3 && this.height <= 21);
/*     */   }
/*     */   
/*     */   public void createPortalBlocks(LevelAccessor paramLevelAccessor) {
/* 198 */     BlockState blockState = (BlockState)Blocks.NETHER_PORTAL.defaultBlockState().setValue((Property)NetherPortalBlock.AXIS, (Comparable)this.axis);
/*     */     
/* 200 */     BlockPos.betweenClosed(this.bottomLeft, this.bottomLeft.relative(Direction.UP, this.height - 1).relative(this.rightDir, this.width - 1)).forEach(paramBlockPos -> paramLevelAccessor.setBlock(paramBlockPos, paramBlockState, 18));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isComplete() {
/* 206 */     return (isValid() && this.numPortalBlocks == this.width * this.height);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Vec3 getRelativePosition(BlockUtil.FoundRectangle paramFoundRectangle, Direction.Axis paramAxis, Vec3 paramVec3, EntityDimensions paramEntityDimensions) {
/* 211 */     double d3, d4, d1 = paramFoundRectangle.axis1Size - paramEntityDimensions.width();
/* 212 */     double d2 = paramFoundRectangle.axis2Size - paramEntityDimensions.height();
/*     */     
/* 214 */     BlockPos blockPos = paramFoundRectangle.minCorner;
/*     */     
/* 216 */     if (d1 > 0.0D) {
/* 217 */       d4 = blockPos.get(paramAxis) + paramEntityDimensions.width() / 2.0D;
/* 218 */       d3 = Mth.clamp(Mth.inverseLerp(paramVec3.get(paramAxis) - d4, 0.0D, d1), 0.0D, 1.0D);
/*     */     } else {
/* 220 */       d3 = 0.5D;
/*     */     } 
/*     */     
/* 223 */     if (d2 > 0.0D) {
/* 224 */       Direction.Axis axis1 = Direction.Axis.Y;
/* 225 */       d4 = Mth.clamp(Mth.inverseLerp(paramVec3.get(axis1) - blockPos.get(axis1), 0.0D, d2), 0.0D, 1.0D);
/*     */     } else {
/* 227 */       d4 = 0.0D;
/*     */     } 
/*     */     
/* 230 */     Direction.Axis axis = (paramAxis == Direction.Axis.X) ? Direction.Axis.Z : Direction.Axis.X;
/* 231 */     double d5 = paramVec3.get(axis) - blockPos.get(axis) + 0.5D;
/*     */     
/* 233 */     return new Vec3(d3, d4, d5);
/*     */   }
/*     */   
/*     */   public static Vec3 findCollisionFreePosition(Vec3 paramVec3, ServerLevel paramServerLevel, Entity paramEntity, EntityDimensions paramEntityDimensions) {
/* 237 */     if (paramEntityDimensions.width() > 4.0F || paramEntityDimensions.height() > 4.0F) {
/* 238 */       return paramVec3;
/*     */     }
/*     */     
/* 241 */     double d = paramEntityDimensions.height() / 2.0D;
/* 242 */     Vec3 vec3 = paramVec3.add(0.0D, d, 0.0D);
/*     */     
/* 244 */     VoxelShape voxelShape = Shapes.create(AABB.ofSize(vec3, paramEntityDimensions.width(), 0.0D, paramEntityDimensions.width()).expandTowards(0.0D, 1.0D, 0.0D).inflate(1.0E-6D));
/* 245 */     Optional optional = paramServerLevel.findFreePosition(paramEntity, voxelShape, vec3, paramEntityDimensions.width(), paramEntityDimensions.height(), paramEntityDimensions.width());
/* 246 */     Optional<Vec3> optional1 = optional.map(paramVec3 -> paramVec3.subtract(0.0D, paramDouble, 0.0D));
/*     */     
/* 248 */     return optional1.orElse(paramVec3);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\portal\PortalShape.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */