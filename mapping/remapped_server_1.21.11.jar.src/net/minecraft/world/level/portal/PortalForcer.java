/*     */ package net.minecraft.world.level.portal;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.BlockUtil;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiRecord;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiTypes;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.NetherPortalBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.border.WorldBorder;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PortalForcer
/*     */ {
/*     */   public static final int TICKET_RADIUS = 3;
/*     */   private static final int NETHER_PORTAL_RADIUS = 16;
/*     */   private static final int OVERWORLD_PORTAL_RADIUS = 128;
/*     */   private static final int FRAME_HEIGHT = 5;
/*     */   private static final int FRAME_WIDTH = 4;
/*     */   private static final int FRAME_BOX = 3;
/*     */   private static final int FRAME_HEIGHT_START = -1;
/*     */   private static final int FRAME_HEIGHT_END = 4;
/*     */   private static final int FRAME_WIDTH_START = -1;
/*     */   private static final int FRAME_WIDTH_END = 3;
/*     */   private static final int FRAME_BOX_START = -1;
/*     */   private static final int FRAME_BOX_END = 2;
/*     */   private static final int NOTHING_FOUND = -1;
/*     */   private final ServerLevel level;
/*     */   
/*     */   public PortalForcer(ServerLevel paramServerLevel) {
/*  45 */     this.level = paramServerLevel;
/*     */   }
/*     */   
/*     */   public Optional<BlockPos> findClosestPortalPosition(BlockPos paramBlockPos, boolean paramBoolean, WorldBorder paramWorldBorder) {
/*  49 */     PoiManager poiManager = this.level.getPoiManager();
/*  50 */     boolean bool = paramBoolean ? true : true;
/*  51 */     poiManager.ensureLoadedAndValid((LevelReader)this.level, paramBlockPos, bool);
/*     */ 
/*     */ 
/*     */     
/*  55 */     Objects.requireNonNull(paramWorldBorder); return poiManager.getInSquare(paramHolder -> paramHolder.is(PoiTypes.NETHER_PORTAL), paramBlockPos, bool, PoiManager.Occupancy.ANY).map(PoiRecord::getPos).filter(paramWorldBorder::isWithinBounds)
/*  56 */       .filter(paramBlockPos -> this.level.getBlockState(paramBlockPos).hasProperty((Property)BlockStateProperties.HORIZONTAL_AXIS))
/*  57 */       .min(Comparator.<BlockPos>comparingDouble(paramBlockPos2 -> paramBlockPos2.distSqr((Vec3i)paramBlockPos1)).thenComparingInt(Vec3i::getY));
/*     */   }
/*     */   
/*     */   public Optional<BlockUtil.FoundRectangle> createPortal(BlockPos paramBlockPos, Direction.Axis paramAxis) {
/*  61 */     Direction direction = Direction.get(Direction.AxisDirection.POSITIVE, paramAxis);
/*     */     
/*  63 */     double d1 = -1.0D;
/*  64 */     BlockPos blockPos1 = null;
/*  65 */     double d2 = -1.0D;
/*  66 */     BlockPos blockPos2 = null;
/*     */     
/*  68 */     WorldBorder worldBorder = this.level.getWorldBorder();
/*  69 */     int i = Math.min(this.level.getMaxY(), this.level.getMinY() + this.level.getLogicalHeight() - 1);
/*     */     
/*  71 */     boolean bool = true;
/*     */     
/*  73 */     BlockPos.MutableBlockPos mutableBlockPos = paramBlockPos.mutable();
/*  74 */     for (BlockPos.MutableBlockPos mutableBlockPos1 : BlockPos.spiralAround(paramBlockPos, 16, Direction.EAST, Direction.SOUTH)) {
/*  75 */       int j = Math.min(i, this.level.getHeight(Heightmap.Types.MOTION_BLOCKING, mutableBlockPos1.getX(), mutableBlockPos1.getZ()));
/*     */ 
/*     */       
/*  78 */       if (!worldBorder.isWithinBounds((BlockPos)mutableBlockPos1) || !worldBorder.isWithinBounds((BlockPos)mutableBlockPos1.move(direction, 1))) {
/*     */         continue;
/*     */       }
/*  81 */       mutableBlockPos1.move(direction.getOpposite(), 1);
/*     */       
/*  83 */       for (int k = j; k >= this.level.getMinY(); k--) {
/*  84 */         mutableBlockPos1.setY(k);
/*  85 */         if (canPortalReplaceBlock(mutableBlockPos1)) {
/*     */ 
/*     */ 
/*     */           
/*  89 */           int m = k;
/*     */           
/*  91 */           while (k > this.level.getMinY() && canPortalReplaceBlock(mutableBlockPos1.move(Direction.DOWN))) {
/*  92 */             k--;
/*     */           }
/*     */ 
/*     */           
/*  96 */           if (k + 4 <= i) {
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 101 */             int n = m - k;
/* 102 */             if (n <= 0 || n >= 3) {
/*     */ 
/*     */ 
/*     */               
/* 106 */               mutableBlockPos1.setY(k);
/*     */               
/* 108 */               if (canHostFrame((BlockPos)mutableBlockPos1, mutableBlockPos, direction, 0)) {
/*     */                 
/* 110 */                 double d = paramBlockPos.distSqr((Vec3i)mutableBlockPos1);
/*     */ 
/*     */                 
/* 113 */                 if (canHostFrame((BlockPos)mutableBlockPos1, mutableBlockPos, direction, -1) && 
/* 114 */                   canHostFrame((BlockPos)mutableBlockPos1, mutableBlockPos, direction, 1))
/*     */                 {
/*     */                   
/* 117 */                   if (d1 == -1.0D || d1 > d) {
/* 118 */                     d1 = d;
/* 119 */                     blockPos1 = mutableBlockPos1.immutable();
/*     */                   } 
/*     */                 }
/*     */ 
/*     */                 
/* 124 */                 if (d1 == -1.0D && (d2 == -1.0D || d2 > d)) {
/* 125 */                   d2 = d;
/* 126 */                   blockPos2 = mutableBlockPos1.immutable();
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 133 */     }  if (d1 == -1.0D && d2 != -1.0D) {
/* 134 */       blockPos1 = blockPos2;
/* 135 */       d1 = d2;
/*     */     } 
/*     */     
/* 138 */     if (d1 == -1.0D) {
/*     */ 
/*     */       
/* 141 */       int j = Math.max(this.level.getMinY() - -1, 70);
/* 142 */       int k = i - 9;
/* 143 */       if (k < j) {
/* 144 */         return Optional.empty();
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 151 */       blockPos1 = (new BlockPos(paramBlockPos.getX() - direction.getStepX() * 1, Mth.clamp(paramBlockPos.getY(), j, k), paramBlockPos.getZ() - direction.getStepZ() * 1)).immutable();
/* 152 */       blockPos1 = worldBorder.clampToBounds(blockPos1);
/* 153 */       Direction direction1 = direction.getClockWise();
/*     */ 
/*     */       
/* 156 */       for (byte b2 = -1; b2 < 2; b2++) {
/* 157 */         for (byte b3 = 0; b3 < 2; b3++) {
/*     */           
/* 159 */           for (byte b4 = -1; b4 < 3; b4++) {
/* 160 */             BlockState blockState1 = (b4 < 0) ? Blocks.OBSIDIAN.defaultBlockState() : Blocks.AIR.defaultBlockState();
/*     */             
/* 162 */             mutableBlockPos.setWithOffset((Vec3i)blockPos1, b3 * direction
/*     */                 
/* 164 */                 .getStepX() + b2 * direction1.getStepX(), b4, b3 * direction
/*     */                 
/* 166 */                 .getStepZ() + b2 * direction1.getStepZ());
/*     */             
/* 168 */             this.level.setBlockAndUpdate((BlockPos)mutableBlockPos, blockState1);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 175 */     for (byte b = -1; b < 3; b++) {
/* 176 */       for (byte b2 = -1; b2 < 4; b2++) {
/*     */         
/* 178 */         if (b == -1 || b == 2 || b2 == -1 || b2 == 3) {
/* 179 */           mutableBlockPos.setWithOffset((Vec3i)blockPos1, b * direction
/*     */               
/* 181 */               .getStepX(), b2, b * direction
/*     */               
/* 183 */               .getStepZ());
/*     */           
/* 185 */           this.level.setBlock((BlockPos)mutableBlockPos, Blocks.OBSIDIAN.defaultBlockState(), 3);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 191 */     BlockState blockState = (BlockState)Blocks.NETHER_PORTAL.defaultBlockState().setValue((Property)NetherPortalBlock.AXIS, (Comparable)paramAxis);
/*     */     
/* 193 */     for (byte b1 = 0; b1 < 2; b1++) {
/* 194 */       for (byte b2 = 0; b2 < 3; b2++) {
/* 195 */         mutableBlockPos.setWithOffset((Vec3i)blockPos1, b1 * direction
/*     */             
/* 197 */             .getStepX(), b2, b1 * direction
/*     */             
/* 199 */             .getStepZ());
/*     */         
/* 201 */         this.level.setBlock((BlockPos)mutableBlockPos, blockState, 18);
/*     */       } 
/*     */     } 
/*     */     
/* 205 */     return Optional.of(new BlockUtil.FoundRectangle(blockPos1.immutable(), 2, 3));
/*     */   }
/*     */   
/*     */   private boolean canPortalReplaceBlock(BlockPos.MutableBlockPos paramMutableBlockPos) {
/* 209 */     BlockState blockState = this.level.getBlockState((BlockPos)paramMutableBlockPos);
/* 210 */     return (blockState.canBeReplaced() && blockState.getFluidState().isEmpty());
/*     */   }
/*     */   
/*     */   private boolean canHostFrame(BlockPos paramBlockPos, BlockPos.MutableBlockPos paramMutableBlockPos, Direction paramDirection, int paramInt) {
/* 214 */     Direction direction = paramDirection.getClockWise();
/*     */     
/* 216 */     for (byte b = -1; b < 3; b++) {
/* 217 */       for (byte b1 = -1; b1 < 4; b1++) {
/* 218 */         paramMutableBlockPos.setWithOffset((Vec3i)paramBlockPos, paramDirection
/*     */             
/* 220 */             .getStepX() * b + direction.getStepX() * paramInt, b1, paramDirection
/*     */             
/* 222 */             .getStepZ() * b + direction.getStepZ() * paramInt);
/*     */ 
/*     */         
/* 225 */         if (b1 < 0 && !this.level.getBlockState((BlockPos)paramMutableBlockPos).isSolid()) {
/* 226 */           return false;
/*     */         }
/* 228 */         if (b1 >= 0 && !canPortalReplaceBlock(paramMutableBlockPos)) {
/* 229 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 234 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\portal\PortalForcer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */