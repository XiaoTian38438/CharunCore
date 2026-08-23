/*     */ package net.minecraft.world.level.block.piston;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.PushReaction;
/*     */ 
/*     */ public class PistonStructureResolver
/*     */ {
/*     */   public static final int MAX_PUSH_DEPTH = 12;
/*     */   private final Level level;
/*     */   private final BlockPos pistonPos;
/*     */   private final boolean extending;
/*     */   private final BlockPos startPos;
/*     */   private final Direction pushDirection;
/*  21 */   private final List<BlockPos> toPush = Lists.newArrayList();
/*  22 */   private final List<BlockPos> toDestroy = Lists.newArrayList();
/*     */   private final Direction pistonDirection;
/*     */   
/*     */   public PistonStructureResolver(Level paramLevel, BlockPos paramBlockPos, Direction paramDirection, boolean paramBoolean) {
/*  26 */     this.level = paramLevel;
/*  27 */     this.pistonPos = paramBlockPos;
/*  28 */     this.pistonDirection = paramDirection;
/*  29 */     this.extending = paramBoolean;
/*     */     
/*  31 */     if (paramBoolean) {
/*  32 */       this.pushDirection = paramDirection;
/*  33 */       this.startPos = paramBlockPos.relative(paramDirection);
/*     */     } else {
/*  35 */       this.pushDirection = paramDirection.getOpposite();
/*  36 */       this.startPos = paramBlockPos.relative(paramDirection, 2);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean resolve() {
/*  41 */     this.toPush.clear();
/*  42 */     this.toDestroy.clear();
/*     */     
/*  44 */     BlockState blockState = this.level.getBlockState(this.startPos);
/*     */     
/*  46 */     if (!PistonBaseBlock.isPushable(blockState, this.level, this.startPos, this.pushDirection, false, this.pistonDirection)) {
/*  47 */       if (this.extending && blockState.getPistonPushReaction() == PushReaction.DESTROY) {
/*  48 */         this.toDestroy.add(this.startPos);
/*  49 */         return true;
/*     */       } 
/*     */       
/*  52 */       return false;
/*     */     } 
/*     */ 
/*     */     
/*  56 */     if (!addBlockLine(this.startPos, this.pushDirection))
/*     */     {
/*  58 */       return false;
/*     */     }
/*     */     
/*  61 */     for (byte b = 0; b < this.toPush.size(); b++) {
/*  62 */       BlockPos blockPos = this.toPush.get(b);
/*     */ 
/*     */       
/*  65 */       if (isSticky(this.level.getBlockState(blockPos)) && 
/*  66 */         !addBranchingBlocks(blockPos))
/*     */       {
/*  68 */         return false;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*  73 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean isSticky(BlockState paramBlockState) {
/*  77 */     return (paramBlockState.is(Blocks.SLIME_BLOCK) || paramBlockState.is(Blocks.HONEY_BLOCK));
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean canStickToEachOther(BlockState paramBlockState1, BlockState paramBlockState2) {
/*  82 */     if (paramBlockState1.is(Blocks.HONEY_BLOCK) && paramBlockState2.is(Blocks.SLIME_BLOCK)) {
/*  83 */       return false;
/*     */     }
/*  85 */     if (paramBlockState1.is(Blocks.SLIME_BLOCK) && paramBlockState2.is(Blocks.HONEY_BLOCK)) {
/*  86 */       return false;
/*     */     }
/*  88 */     return (isSticky(paramBlockState1) || isSticky(paramBlockState2));
/*     */   }
/*     */   
/*     */   private boolean addBlockLine(BlockPos paramBlockPos, Direction paramDirection) {
/*  92 */     BlockState blockState = this.level.getBlockState(paramBlockPos);
/*  93 */     if (blockState.isAir())
/*     */     {
/*  95 */       return true; } 
/*  96 */     if (!PistonBaseBlock.isPushable(blockState, this.level, paramBlockPos, this.pushDirection, false, paramDirection))
/*     */     {
/*  98 */       return true; } 
/*  99 */     if (paramBlockPos.equals(this.pistonPos))
/*     */     {
/* 101 */       return true; } 
/* 102 */     if (this.toPush.contains(paramBlockPos))
/*     */     {
/* 104 */       return true;
/*     */     }
/*     */     
/* 107 */     byte b1 = 1;
/* 108 */     if (b1 + this.toPush.size() > 12)
/*     */     {
/* 110 */       return false;
/*     */     }
/*     */     
/* 113 */     while (isSticky(blockState)) {
/* 114 */       BlockPos blockPos = paramBlockPos.relative(this.pushDirection.getOpposite(), b1);
/* 115 */       BlockState blockState1 = blockState;
/* 116 */       blockState = this.level.getBlockState(blockPos);
/*     */       
/* 118 */       if (blockState.isAir() || !canStickToEachOther(blockState1, blockState) || !PistonBaseBlock.isPushable(blockState, this.level, blockPos, this.pushDirection, false, this.pushDirection.getOpposite()) || blockPos.equals(this.pistonPos)) {
/*     */         break;
/*     */       }
/* 121 */       b1++;
/* 122 */       if (b1 + this.toPush.size() > 12) {
/* 123 */         return false;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 128 */     byte b2 = 0;
/*     */     
/*     */     int i;
/* 131 */     for (i = b1 - 1; i >= 0; i--) {
/* 132 */       this.toPush.add(paramBlockPos.relative(this.pushDirection.getOpposite(), i));
/* 133 */       b2++;
/*     */     } 
/*     */ 
/*     */     
/* 137 */     for (i = 1;; i++) {
/* 138 */       BlockPos blockPos = paramBlockPos.relative(this.pushDirection, i);
/*     */       
/* 140 */       int j = this.toPush.indexOf(blockPos);
/* 141 */       if (j > -1) {
/*     */         
/* 143 */         reorderListAtCollision(b2, j);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 148 */         for (byte b = 0; b <= j + b2; b++) {
/* 149 */           BlockPos blockPos1 = this.toPush.get(b);
/* 150 */           if (isSticky(this.level.getBlockState(blockPos1)) && 
/* 151 */             !addBranchingBlocks(blockPos1)) {
/* 152 */             return false;
/*     */           }
/*     */         } 
/*     */ 
/*     */         
/* 157 */         return true;
/*     */       } 
/*     */       
/* 160 */       blockState = this.level.getBlockState(blockPos);
/*     */       
/* 162 */       if (blockState.isAir())
/*     */       {
/* 164 */         return true;
/*     */       }
/*     */       
/* 167 */       if (!PistonBaseBlock.isPushable(blockState, this.level, blockPos, this.pushDirection, true, this.pushDirection) || blockPos.equals(this.pistonPos))
/*     */       {
/* 169 */         return false;
/*     */       }
/*     */       
/* 172 */       if (blockState.getPistonPushReaction() == PushReaction.DESTROY) {
/* 173 */         this.toDestroy.add(blockPos);
/* 174 */         return true;
/*     */       } 
/*     */       
/* 177 */       if (this.toPush.size() >= 12) {
/* 178 */         return false;
/*     */       }
/*     */       
/* 181 */       this.toPush.add(blockPos);
/* 182 */       b2++;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void reorderListAtCollision(int paramInt1, int paramInt2) {
/* 187 */     ArrayList<? extends BlockPos> arrayList1 = Lists.newArrayList();
/* 188 */     ArrayList<? extends BlockPos> arrayList2 = Lists.newArrayList();
/* 189 */     ArrayList<? extends BlockPos> arrayList3 = Lists.newArrayList();
/*     */     
/* 191 */     arrayList1.addAll(this.toPush.subList(0, paramInt2));
/* 192 */     arrayList2.addAll(this.toPush.subList(this.toPush.size() - paramInt1, this.toPush.size()));
/* 193 */     arrayList3.addAll(this.toPush.subList(paramInt2, this.toPush.size() - paramInt1));
/*     */     
/* 195 */     this.toPush.clear();
/* 196 */     this.toPush.addAll(arrayList1);
/* 197 */     this.toPush.addAll(arrayList2);
/* 198 */     this.toPush.addAll(arrayList3);
/*     */   }
/*     */   
/*     */   private boolean addBranchingBlocks(BlockPos paramBlockPos) {
/* 202 */     BlockState blockState = this.level.getBlockState(paramBlockPos);
/* 203 */     for (Direction direction : Direction.values()) {
/* 204 */       if (direction.getAxis() != this.pushDirection.getAxis()) {
/* 205 */         BlockPos blockPos = paramBlockPos.relative(direction);
/* 206 */         BlockState blockState1 = this.level.getBlockState(blockPos);
/* 207 */         if (canStickToEachOther(blockState1, blockState))
/*     */         {
/*     */           
/* 210 */           if (!addBlockLine(blockPos, direction)) {
/* 211 */             return false;
/*     */           }
/*     */         }
/*     */       } 
/*     */     } 
/* 216 */     return true;
/*     */   }
/*     */   
/*     */   public Direction getPushDirection() {
/* 220 */     return this.pushDirection;
/*     */   }
/*     */   
/*     */   public List<BlockPos> getToPush() {
/* 224 */     return this.toPush;
/*     */   }
/*     */   
/*     */   public List<BlockPos> getToDestroy() {
/* 228 */     return this.toDestroy;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\piston\PistonStructureResolver.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */