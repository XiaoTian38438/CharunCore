/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.RailShape;
/*     */ 
/*     */ 
/*     */ public class RailState
/*     */ {
/*     */   private final Level level;
/*     */   private final BlockPos pos;
/*     */   private final BaseRailBlock block;
/*     */   private BlockState state;
/*     */   private final boolean isStraight;
/*  19 */   private final List<BlockPos> connections = Lists.newArrayList();
/*     */   
/*     */   public RailState(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState) {
/*  22 */     this.level = paramLevel;
/*  23 */     this.pos = paramBlockPos;
/*  24 */     this.state = paramBlockState;
/*  25 */     this.block = (BaseRailBlock)paramBlockState.getBlock();
/*  26 */     RailShape railShape = (RailShape)paramBlockState.getValue(this.block.getShapeProperty());
/*  27 */     this.isStraight = this.block.isStraight();
/*  28 */     updateConnections(railShape);
/*     */   }
/*     */   
/*     */   public List<BlockPos> getConnections() {
/*  32 */     return this.connections;
/*     */   }
/*     */   
/*     */   private void updateConnections(RailShape paramRailShape) {
/*  36 */     this.connections.clear();
/*  37 */     switch (paramRailShape) {
/*     */       case NORTH_SOUTH:
/*  39 */         this.connections.add(this.pos.north());
/*  40 */         this.connections.add(this.pos.south());
/*     */         break;
/*     */       case EAST_WEST:
/*  43 */         this.connections.add(this.pos.west());
/*  44 */         this.connections.add(this.pos.east());
/*     */         break;
/*     */       case ASCENDING_EAST:
/*  47 */         this.connections.add(this.pos.west());
/*  48 */         this.connections.add(this.pos.east().above());
/*     */         break;
/*     */       case ASCENDING_WEST:
/*  51 */         this.connections.add(this.pos.west().above());
/*  52 */         this.connections.add(this.pos.east());
/*     */         break;
/*     */       case ASCENDING_NORTH:
/*  55 */         this.connections.add(this.pos.north().above());
/*  56 */         this.connections.add(this.pos.south());
/*     */         break;
/*     */       case ASCENDING_SOUTH:
/*  59 */         this.connections.add(this.pos.north());
/*  60 */         this.connections.add(this.pos.south().above());
/*     */         break;
/*     */       case SOUTH_EAST:
/*  63 */         this.connections.add(this.pos.east());
/*  64 */         this.connections.add(this.pos.south());
/*     */         break;
/*     */       case SOUTH_WEST:
/*  67 */         this.connections.add(this.pos.west());
/*  68 */         this.connections.add(this.pos.south());
/*     */         break;
/*     */       case NORTH_WEST:
/*  71 */         this.connections.add(this.pos.west());
/*  72 */         this.connections.add(this.pos.north());
/*     */         break;
/*     */       case NORTH_EAST:
/*  75 */         this.connections.add(this.pos.east());
/*  76 */         this.connections.add(this.pos.north());
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void removeSoftConnections() {
/*  82 */     for (byte b = 0; b < this.connections.size(); b++) {
/*  83 */       RailState railState = getRail(this.connections.get(b));
/*  84 */       if (railState == null || !railState.connectsTo(this)) {
/*  85 */         this.connections.remove(b--);
/*     */       } else {
/*  87 */         this.connections.set(b, railState.pos);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean hasRail(BlockPos paramBlockPos) {
/*  93 */     return (BaseRailBlock.isRail(this.level, paramBlockPos) || BaseRailBlock.isRail(this.level, paramBlockPos.above()) || BaseRailBlock.isRail(this.level, paramBlockPos.below()));
/*     */   }
/*     */   
/*     */   private RailState getRail(BlockPos paramBlockPos) {
/*  97 */     BlockPos blockPos = paramBlockPos;
/*  98 */     BlockState blockState = this.level.getBlockState(blockPos);
/*  99 */     if (BaseRailBlock.isRail(blockState)) {
/* 100 */       return new RailState(this.level, blockPos, blockState);
/*     */     }
/*     */     
/* 103 */     blockPos = paramBlockPos.above();
/* 104 */     blockState = this.level.getBlockState(blockPos);
/* 105 */     if (BaseRailBlock.isRail(blockState)) {
/* 106 */       return new RailState(this.level, blockPos, blockState);
/*     */     }
/*     */     
/* 109 */     blockPos = paramBlockPos.below();
/* 110 */     blockState = this.level.getBlockState(blockPos);
/* 111 */     if (BaseRailBlock.isRail(blockState)) {
/* 112 */       return new RailState(this.level, blockPos, blockState);
/*     */     }
/*     */     
/* 115 */     return null;
/*     */   }
/*     */   
/*     */   private boolean connectsTo(RailState paramRailState) {
/* 119 */     return hasConnection(paramRailState.pos);
/*     */   }
/*     */   
/*     */   private boolean hasConnection(BlockPos paramBlockPos) {
/* 123 */     for (byte b = 0; b < this.connections.size(); b++) {
/* 124 */       BlockPos blockPos = this.connections.get(b);
/* 125 */       if (blockPos.getX() == paramBlockPos.getX() && blockPos.getZ() == paramBlockPos.getZ()) {
/* 126 */         return true;
/*     */       }
/*     */     } 
/* 129 */     return false;
/*     */   }
/*     */   
/*     */   protected int countPotentialConnections() {
/* 133 */     byte b = 0;
/*     */     
/* 135 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 136 */       if (hasRail(this.pos.relative(direction))) {
/* 137 */         b++;
/*     */       }
/*     */     } 
/*     */     
/* 141 */     return b;
/*     */   }
/*     */   
/*     */   private boolean canConnectTo(RailState paramRailState) {
/* 145 */     return (connectsTo(paramRailState) || this.connections.size() != 2);
/*     */   }
/*     */   
/*     */   private void connectTo(RailState paramRailState) {
/* 149 */     this.connections.add(paramRailState.pos);
/*     */     
/* 151 */     BlockPos blockPos1 = this.pos.north();
/* 152 */     BlockPos blockPos2 = this.pos.south();
/* 153 */     BlockPos blockPos3 = this.pos.west();
/* 154 */     BlockPos blockPos4 = this.pos.east();
/*     */     
/* 156 */     boolean bool1 = hasConnection(blockPos1);
/* 157 */     boolean bool2 = hasConnection(blockPos2);
/* 158 */     boolean bool3 = hasConnection(blockPos3);
/* 159 */     boolean bool4 = hasConnection(blockPos4);
/*     */     
/* 161 */     RailShape railShape = null;
/*     */     
/* 163 */     if (bool1 || bool2) {
/* 164 */       railShape = RailShape.NORTH_SOUTH;
/*     */     }
/* 166 */     if (bool3 || bool4) {
/* 167 */       railShape = RailShape.EAST_WEST;
/*     */     }
/* 169 */     if (!this.isStraight) {
/* 170 */       if (bool2 && bool4 && !bool1 && !bool3) {
/* 171 */         railShape = RailShape.SOUTH_EAST;
/*     */       }
/* 173 */       if (bool2 && bool3 && !bool1 && !bool4) {
/* 174 */         railShape = RailShape.SOUTH_WEST;
/*     */       }
/* 176 */       if (bool1 && bool3 && !bool2 && !bool4) {
/* 177 */         railShape = RailShape.NORTH_WEST;
/*     */       }
/* 179 */       if (bool1 && bool4 && !bool2 && !bool3) {
/* 180 */         railShape = RailShape.NORTH_EAST;
/*     */       }
/*     */     } 
/* 183 */     if (railShape == RailShape.NORTH_SOUTH) {
/* 184 */       if (BaseRailBlock.isRail(this.level, blockPos1.above())) {
/* 185 */         railShape = RailShape.ASCENDING_NORTH;
/*     */       }
/* 187 */       if (BaseRailBlock.isRail(this.level, blockPos2.above())) {
/* 188 */         railShape = RailShape.ASCENDING_SOUTH;
/*     */       }
/*     */     } 
/* 191 */     if (railShape == RailShape.EAST_WEST) {
/* 192 */       if (BaseRailBlock.isRail(this.level, blockPos4.above())) {
/* 193 */         railShape = RailShape.ASCENDING_EAST;
/*     */       }
/* 195 */       if (BaseRailBlock.isRail(this.level, blockPos3.above())) {
/* 196 */         railShape = RailShape.ASCENDING_WEST;
/*     */       }
/*     */     } 
/*     */     
/* 200 */     if (railShape == null) {
/* 201 */       railShape = RailShape.NORTH_SOUTH;
/*     */     }
/*     */     
/* 204 */     this.state = (BlockState)this.state.setValue(this.block.getShapeProperty(), (Comparable)railShape);
/* 205 */     this.level.setBlock(this.pos, this.state, 3);
/*     */   }
/*     */   
/*     */   private boolean hasNeighborRail(BlockPos paramBlockPos) {
/* 209 */     RailState railState = getRail(paramBlockPos);
/* 210 */     if (railState == null) {
/* 211 */       return false;
/*     */     }
/*     */     
/* 214 */     railState.removeSoftConnections();
/* 215 */     return railState.canConnectTo(this);
/*     */   }
/*     */   
/*     */   public RailState place(boolean paramBoolean1, boolean paramBoolean2, RailShape paramRailShape) {
/* 219 */     BlockPos blockPos1 = this.pos.north();
/* 220 */     BlockPos blockPos2 = this.pos.south();
/* 221 */     BlockPos blockPos3 = this.pos.west();
/* 222 */     BlockPos blockPos4 = this.pos.east();
/*     */     
/* 224 */     boolean bool1 = hasNeighborRail(blockPos1);
/* 225 */     boolean bool2 = hasNeighborRail(blockPos2);
/* 226 */     boolean bool3 = hasNeighborRail(blockPos3);
/* 227 */     boolean bool4 = hasNeighborRail(blockPos4);
/*     */     
/* 229 */     RailShape railShape = null;
/*     */     
/* 231 */     boolean bool5 = (bool1 || bool2) ? true : false;
/* 232 */     boolean bool6 = (bool3 || bool4) ? true : false;
/* 233 */     if (bool5 && !bool6) {
/* 234 */       railShape = RailShape.NORTH_SOUTH;
/*     */     }
/* 236 */     if (bool6 && !bool5) {
/* 237 */       railShape = RailShape.EAST_WEST;
/*     */     }
/*     */     
/* 240 */     boolean bool7 = (bool2 && bool4) ? true : false;
/* 241 */     boolean bool8 = (bool2 && bool3) ? true : false;
/* 242 */     boolean bool9 = (bool1 && bool4) ? true : false;
/* 243 */     boolean bool10 = (bool1 && bool3) ? true : false;
/*     */     
/* 245 */     if (!this.isStraight) {
/* 246 */       if (bool7 && !bool1 && !bool3) {
/* 247 */         railShape = RailShape.SOUTH_EAST;
/*     */       }
/* 249 */       if (bool8 && !bool1 && !bool4) {
/* 250 */         railShape = RailShape.SOUTH_WEST;
/*     */       }
/* 252 */       if (bool10 && !bool2 && !bool4) {
/* 253 */         railShape = RailShape.NORTH_WEST;
/*     */       }
/* 255 */       if (bool9 && !bool2 && !bool3) {
/* 256 */         railShape = RailShape.NORTH_EAST;
/*     */       }
/*     */     } 
/* 259 */     if (railShape == null) {
/* 260 */       if (bool5 && bool6) {
/* 261 */         railShape = paramRailShape;
/* 262 */       } else if (bool5) {
/* 263 */         railShape = RailShape.NORTH_SOUTH;
/* 264 */       } else if (bool6) {
/* 265 */         railShape = RailShape.EAST_WEST;
/*     */       } 
/*     */       
/* 268 */       if (!this.isStraight) {
/* 269 */         if (paramBoolean1) {
/* 270 */           if (bool7) {
/* 271 */             railShape = RailShape.SOUTH_EAST;
/*     */           }
/* 273 */           if (bool8) {
/* 274 */             railShape = RailShape.SOUTH_WEST;
/*     */           }
/* 276 */           if (bool9) {
/* 277 */             railShape = RailShape.NORTH_EAST;
/*     */           }
/* 279 */           if (bool10) {
/* 280 */             railShape = RailShape.NORTH_WEST;
/*     */           }
/*     */         } else {
/* 283 */           if (bool10) {
/* 284 */             railShape = RailShape.NORTH_WEST;
/*     */           }
/* 286 */           if (bool9) {
/* 287 */             railShape = RailShape.NORTH_EAST;
/*     */           }
/* 289 */           if (bool8) {
/* 290 */             railShape = RailShape.SOUTH_WEST;
/*     */           }
/* 292 */           if (bool7) {
/* 293 */             railShape = RailShape.SOUTH_EAST;
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 299 */     if (railShape == RailShape.NORTH_SOUTH) {
/* 300 */       if (BaseRailBlock.isRail(this.level, blockPos1.above())) {
/* 301 */         railShape = RailShape.ASCENDING_NORTH;
/*     */       }
/* 303 */       if (BaseRailBlock.isRail(this.level, blockPos2.above())) {
/* 304 */         railShape = RailShape.ASCENDING_SOUTH;
/*     */       }
/*     */     } 
/* 307 */     if (railShape == RailShape.EAST_WEST) {
/* 308 */       if (BaseRailBlock.isRail(this.level, blockPos4.above())) {
/* 309 */         railShape = RailShape.ASCENDING_EAST;
/*     */       }
/* 311 */       if (BaseRailBlock.isRail(this.level, blockPos3.above())) {
/* 312 */         railShape = RailShape.ASCENDING_WEST;
/*     */       }
/*     */     } 
/*     */     
/* 316 */     if (railShape == null) {
/* 317 */       railShape = paramRailShape;
/*     */     }
/*     */     
/* 320 */     updateConnections(railShape);
/* 321 */     this.state = (BlockState)this.state.setValue(this.block.getShapeProperty(), (Comparable)railShape);
/*     */     
/* 323 */     if (paramBoolean2 || this.level.getBlockState(this.pos) != this.state) {
/* 324 */       this.level.setBlock(this.pos, this.state, 3);
/*     */       
/* 326 */       for (byte b = 0; b < this.connections.size(); b++) {
/* 327 */         RailState railState = getRail(this.connections.get(b));
/* 328 */         if (railState != null) {
/*     */ 
/*     */           
/* 331 */           railState.removeSoftConnections();
/*     */           
/* 333 */           if (railState.canConnectTo(this)) {
/* 334 */             railState.connectTo(this);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 339 */     return this;
/*     */   }
/*     */   
/*     */   public BlockState getState() {
/* 343 */     return this.state;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\RailState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */