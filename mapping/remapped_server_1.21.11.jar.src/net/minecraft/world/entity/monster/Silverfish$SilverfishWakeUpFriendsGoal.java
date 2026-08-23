/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.InfestedBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class SilverfishWakeUpFriendsGoal
/*     */   extends Goal
/*     */ {
/*     */   private final Silverfish silverfish;
/*     */   private int lookForFriends;
/*     */   
/*     */   public SilverfishWakeUpFriendsGoal(Silverfish paramSilverfish) {
/* 142 */     this.silverfish = paramSilverfish;
/*     */   }
/*     */   
/*     */   public void notifyHurt() {
/* 146 */     if (this.lookForFriends == 0) {
/* 147 */       this.lookForFriends = adjustedTickDelay(20);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/* 153 */     return (this.lookForFriends > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 158 */     this.lookForFriends--;
/* 159 */     if (this.lookForFriends <= 0) {
/* 160 */       Level level = this.silverfish.level();
/* 161 */       RandomSource randomSource = this.silverfish.getRandom();
/*     */ 
/*     */       
/* 164 */       BlockPos blockPos = this.silverfish.blockPosition();
/*     */       
/*     */       int i;
/* 167 */       for (i = 0; i <= 5 && i >= -5; i = ((i <= 0) ? 1 : 0) - i) {
/* 168 */         int j; for (j = 0; j <= 10 && j >= -10; j = ((j <= 0) ? 1 : 0) - j) {
/* 169 */           int k; for (k = 0; k <= 10 && k >= -10; k = ((k <= 0) ? 1 : 0) - k) {
/* 170 */             BlockPos blockPos1 = blockPos.offset(j, i, k);
/* 171 */             BlockState blockState = level.getBlockState(blockPos1);
/*     */             
/* 173 */             Block block = blockState.getBlock();
/* 174 */             if (block instanceof InfestedBlock) {
/* 175 */               if (((Boolean)getServerLevel(level).getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue()) {
/* 176 */                 level.destroyBlock(blockPos1, true, (Entity)this.silverfish);
/*     */               } else {
/* 178 */                 level.setBlock(blockPos1, ((InfestedBlock)block).hostStateByInfested(level.getBlockState(blockPos1)), 3);
/*     */               } 
/* 180 */               if (randomSource.nextBoolean())
/*     */                 // Byte code: goto -> 251 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\Silverfish$SilverfishWakeUpFriendsGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */