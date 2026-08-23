/*    */ package net.minecraft.world.level.block.state.pattern;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ 
/*    */ public class BlockInWorld
/*    */ {
/*    */   private final LevelReader level;
/*    */   private final BlockPos pos;
/*    */   private final boolean loadChunks;
/*    */   private BlockState state;
/*    */   private BlockEntity entity;
/*    */   private boolean cachedEntity;
/*    */   
/*    */   public BlockInWorld(LevelReader paramLevelReader, BlockPos paramBlockPos, boolean paramBoolean) {
/* 20 */     this.level = paramLevelReader;
/* 21 */     this.pos = paramBlockPos.immutable();
/* 22 */     this.loadChunks = paramBoolean;
/*    */   }
/*    */   
/*    */   public BlockState getState() {
/* 26 */     if (this.state == null && (this.loadChunks || this.level.hasChunkAt(this.pos))) {
/* 27 */       this.state = this.level.getBlockState(this.pos);
/*    */     }
/*    */     
/* 30 */     return this.state;
/*    */   }
/*    */   
/*    */   public BlockEntity getEntity() {
/* 34 */     if (this.entity == null && !this.cachedEntity) {
/* 35 */       this.entity = this.level.getBlockEntity(this.pos);
/* 36 */       this.cachedEntity = true;
/*    */     } 
/*    */     
/* 39 */     return this.entity;
/*    */   }
/*    */   
/*    */   public LevelReader getLevel() {
/* 43 */     return this.level;
/*    */   }
/*    */   
/*    */   public BlockPos getPos() {
/* 47 */     return this.pos;
/*    */   }
/*    */   
/*    */   public static Predicate<BlockInWorld> hasState(Predicate<BlockState> paramPredicate) {
/* 51 */     return paramBlockInWorld -> (paramBlockInWorld != null && paramPredicate.test(paramBlockInWorld.getState()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\state\pattern\BlockInWorld.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */