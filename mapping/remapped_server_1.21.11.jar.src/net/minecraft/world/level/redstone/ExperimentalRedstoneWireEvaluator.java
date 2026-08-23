/*     */ package net.minecraft.world.level.redstone;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Deque;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.debug.DebugSubscriptions;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.RedStoneWireBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.RedstoneSide;
/*     */ 
/*     */ public class ExperimentalRedstoneWireEvaluator extends RedstoneWireEvaluator {
/*  22 */   private final Deque<BlockPos> wiresToTurnOff = new ArrayDeque<>();
/*  23 */   private final Deque<BlockPos> wiresToTurnOn = new ArrayDeque<>();
/*     */   
/*  25 */   private final Object2IntMap<BlockPos> updatedWires = (Object2IntMap<BlockPos>)new Object2IntLinkedOpenHashMap();
/*     */   
/*     */   public ExperimentalRedstoneWireEvaluator(RedStoneWireBlock paramRedStoneWireBlock) {
/*  28 */     super(paramRedStoneWireBlock);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updatePowerStrength(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, Orientation paramOrientation, boolean paramBoolean) {
/*  33 */     Orientation orientation = getInitialOrientation(paramLevel, paramOrientation);
/*     */     
/*  35 */     calculateCurrentChanges(paramLevel, paramBlockPos, orientation);
/*     */ 
/*     */     
/*  38 */     ObjectIterator objectIterator = this.updatedWires.object2IntEntrySet().iterator();
/*  39 */     boolean bool = true;
/*  40 */     while (objectIterator.hasNext()) {
/*  41 */       Object2IntMap.Entry entry = (Object2IntMap.Entry)objectIterator.next();
/*  42 */       BlockPos blockPos = (BlockPos)entry.getKey();
/*  43 */       int i = entry.getIntValue();
/*  44 */       int j = unpackPower(i);
/*  45 */       BlockState blockState = paramLevel.getBlockState(blockPos);
/*  46 */       if (blockState.is((Block)this.wireBlock) && !((Integer)blockState.getValue((Property)RedStoneWireBlock.POWER)).equals(Integer.valueOf(j))) {
/*     */         
/*  48 */         int k = 2;
/*  49 */         if (!paramBoolean || !bool) {
/*  50 */           k |= 0x80;
/*     */         }
/*  52 */         paramLevel.setBlock(blockPos, (BlockState)blockState.setValue((Property)RedStoneWireBlock.POWER, Integer.valueOf(j)), k);
/*     */       } else {
/*  54 */         objectIterator.remove();
/*     */       } 
/*  56 */       bool = false;
/*     */     } 
/*     */     
/*  59 */     causeNeighborUpdates(paramLevel);
/*     */   }
/*     */   
/*     */   private void causeNeighborUpdates(Level paramLevel) {
/*  63 */     this.updatedWires.forEach((paramBlockPos, paramInt) -> {
/*     */           Orientation orientation = unpackOrientation(paramInt);
/*     */           BlockState blockState = paramLevel.getBlockState(paramBlockPos);
/*     */           for (Direction direction : orientation.getDirections()) {
/*     */             if (isConnected(blockState, direction)) {
/*     */               BlockPos blockPos = paramBlockPos.relative(direction);
/*     */               BlockState blockState1 = paramLevel.getBlockState(blockPos);
/*     */               Orientation orientation1 = orientation.withFrontPreserveUp(direction);
/*     */               paramLevel.neighborChanged(blockState1, blockPos, (Block)this.wireBlock, orientation1, false);
/*     */               if (blockState1.isRedstoneConductor((BlockGetter)paramLevel, blockPos)) {
/*     */                 for (Direction direction1 : orientation1.getDirections()) {
/*     */                   if (direction1 != direction.getOpposite()) {
/*     */                     paramLevel.neighborChanged(blockPos.relative(direction1), (Block)this.wireBlock, orientation1.withFrontPreserveUp(direction1));
/*     */                   }
/*     */                 } 
/*     */               }
/*     */             } 
/*     */           } 
/*     */         });
/*  82 */     if (paramLevel instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)paramLevel; if (serverLevel.debugSynchronizers().hasAnySubscriberFor(DebugSubscriptions.REDSTONE_WIRE_ORIENTATIONS)) {
/*  83 */         this.updatedWires.forEach((paramBlockPos, paramInt) -> paramServerLevel.debugSynchronizers().sendBlockValue(paramBlockPos, DebugSubscriptions.REDSTONE_WIRE_ORIENTATIONS, unpackOrientation(paramInt)));
/*     */       } }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isConnected(BlockState paramBlockState, Direction paramDirection) {
/*  90 */     EnumProperty enumProperty = (EnumProperty)RedStoneWireBlock.PROPERTY_BY_DIRECTION.get(paramDirection);
/*  91 */     if (enumProperty == null) {
/*  92 */       return (paramDirection == Direction.DOWN);
/*     */     }
/*  94 */     return ((RedstoneSide)paramBlockState.getValue((Property)enumProperty)).isConnected();
/*     */   }
/*     */   
/*     */   private static Orientation getInitialOrientation(Level paramLevel, Orientation paramOrientation) {
/*     */     Orientation orientation;
/*  99 */     if (paramOrientation != null) {
/* 100 */       orientation = paramOrientation;
/*     */     } else {
/* 102 */       orientation = Orientation.random(paramLevel.random);
/*     */     } 
/* 104 */     return orientation.withUp(Direction.UP).withSideBias(Orientation.SideBias.LEFT);
/*     */   }
/*     */ 
/*     */   
/*     */   private void calculateCurrentChanges(Level paramLevel, BlockPos paramBlockPos, Orientation paramOrientation) {
/* 109 */     BlockState blockState = paramLevel.getBlockState(paramBlockPos);
/* 110 */     if (blockState.is((Block)this.wireBlock)) {
/* 111 */       setPower(paramBlockPos, ((Integer)blockState.getValue((Property)RedStoneWireBlock.POWER)).intValue(), paramOrientation);
/* 112 */       this.wiresToTurnOff.add(paramBlockPos);
/*     */     } else {
/*     */       
/* 115 */       propagateChangeToNeighbors(paramLevel, paramBlockPos, 0, paramOrientation, true);
/*     */     } 
/*     */     
/* 118 */     while (!this.wiresToTurnOff.isEmpty()) {
/* 119 */       int i1; BlockPos blockPos = this.wiresToTurnOff.removeFirst();
/* 120 */       int i = this.updatedWires.getInt(blockPos);
/* 121 */       Orientation orientation = unpackOrientation(i);
/* 122 */       int j = unpackPower(i);
/* 123 */       int k = getBlockSignal(paramLevel, blockPos);
/* 124 */       int m = getIncomingWireSignal(paramLevel, blockPos);
/* 125 */       int n = Math.max(k, m);
/*     */ 
/*     */       
/* 128 */       if (n < j) {
/* 129 */         if (k > 0 && !this.wiresToTurnOn.contains(blockPos)) {
/* 130 */           this.wiresToTurnOn.add(blockPos);
/*     */         }
/*     */         
/* 133 */         i1 = 0;
/*     */       } else {
/* 135 */         i1 = n;
/*     */       } 
/* 137 */       if (i1 != j) {
/* 138 */         setPower(blockPos, i1, orientation);
/*     */       }
/* 140 */       propagateChangeToNeighbors(paramLevel, blockPos, i1, orientation, (j > n));
/*     */     } 
/* 142 */     while (!this.wiresToTurnOn.isEmpty()) {
/* 143 */       BlockPos blockPos = this.wiresToTurnOn.removeFirst();
/* 144 */       int i = this.updatedWires.getInt(blockPos);
/* 145 */       int j = unpackPower(i);
/* 146 */       int k = getBlockSignal(paramLevel, blockPos);
/* 147 */       int m = getIncomingWireSignal(paramLevel, blockPos);
/* 148 */       int n = Math.max(k, m);
/*     */       
/* 150 */       Orientation orientation = unpackOrientation(i);
/* 151 */       if (n > j) {
/* 152 */         setPower(blockPos, n, orientation);
/* 153 */       } else if (n < j) {
/* 154 */         throw new IllegalStateException("Turning off wire while trying to turn it on. Should not happen.");
/*     */       } 
/* 156 */       propagateChangeToNeighbors(paramLevel, blockPos, n, orientation, false);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static int packOrientationAndPower(Orientation paramOrientation, int paramInt) {
/* 161 */     return paramOrientation.getIndex() << 4 | paramInt;
/*     */   }
/*     */   
/*     */   private static Orientation unpackOrientation(int paramInt) {
/* 165 */     return Orientation.fromIndex(paramInt >> 4);
/*     */   }
/*     */   
/*     */   private static int unpackPower(int paramInt) {
/* 169 */     return paramInt & 0xF;
/*     */   }
/*     */   
/*     */   private void setPower(BlockPos paramBlockPos, int paramInt, Orientation paramOrientation) {
/* 173 */     this.updatedWires.compute(paramBlockPos, (paramBlockPos, paramInteger) -> (paramInteger == null) ? Integer.valueOf(packOrientationAndPower(paramOrientation, paramInt)) : Integer.valueOf(packOrientationAndPower(unpackOrientation(paramInteger.intValue()), paramInt)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void propagateChangeToNeighbors(Level paramLevel, BlockPos paramBlockPos, int paramInt, Orientation paramOrientation, boolean paramBoolean) {
/* 183 */     for (Direction direction : paramOrientation.getHorizontalDirections()) {
/* 184 */       BlockPos blockPos = paramBlockPos.relative(direction);
/* 185 */       enqueueNeighborWire(paramLevel, blockPos, paramInt, paramOrientation.withFront(direction), paramBoolean);
/*     */     } 
/* 187 */     for (Direction direction : paramOrientation.getVerticalDirections()) {
/* 188 */       BlockPos blockPos = paramBlockPos.relative(direction);
/* 189 */       boolean bool = paramLevel.getBlockState(blockPos).isRedstoneConductor((BlockGetter)paramLevel, blockPos);
/* 190 */       for (Direction direction1 : paramOrientation.getHorizontalDirections()) {
/*     */ 
/*     */         
/* 193 */         BlockPos blockPos1 = paramBlockPos.relative(direction1);
/* 194 */         if (direction == Direction.UP && !bool) {
/* 195 */           BlockPos blockPos2 = blockPos.relative(direction1);
/* 196 */           enqueueNeighborWire(paramLevel, blockPos2, paramInt, paramOrientation.withFront(direction1), paramBoolean); continue;
/* 197 */         }  if (direction == Direction.DOWN && !paramLevel.getBlockState(blockPos1).isRedstoneConductor((BlockGetter)paramLevel, blockPos1)) {
/*     */           
/* 199 */           BlockPos blockPos2 = blockPos.relative(direction1);
/* 200 */           enqueueNeighborWire(paramLevel, blockPos2, paramInt, paramOrientation.withFront(direction1), paramBoolean);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void enqueueNeighborWire(Level paramLevel, BlockPos paramBlockPos, int paramInt, Orientation paramOrientation, boolean paramBoolean) {
/* 207 */     BlockState blockState = paramLevel.getBlockState(paramBlockPos);
/* 208 */     if (blockState.is((Block)this.wireBlock)) {
/* 209 */       int i = getWireSignal(paramBlockPos, blockState);
/*     */       
/* 211 */       if (i < paramInt - 1 && !this.wiresToTurnOn.contains(paramBlockPos)) {
/*     */         
/* 213 */         this.wiresToTurnOn.add(paramBlockPos);
/* 214 */         setPower(paramBlockPos, i, paramOrientation);
/*     */       } 
/* 216 */       if (paramBoolean)
/*     */       {
/* 218 */         if (i > paramInt && !this.wiresToTurnOff.contains(paramBlockPos)) {
/*     */           
/* 220 */           this.wiresToTurnOff.add(paramBlockPos);
/* 221 */           setPower(paramBlockPos, i, paramOrientation);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getWireSignal(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 229 */     int i = this.updatedWires.getOrDefault(paramBlockPos, -1);
/* 230 */     if (i != -1) {
/* 231 */       return unpackPower(i);
/*     */     }
/* 233 */     return super.getWireSignal(paramBlockPos, paramBlockState);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\redstone\ExperimentalRedstoneWireEvaluator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */