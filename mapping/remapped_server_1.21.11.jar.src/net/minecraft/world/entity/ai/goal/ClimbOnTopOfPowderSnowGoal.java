/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.tags.EntityTypeTags;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ 
/*    */ public class ClimbOnTopOfPowderSnowGoal extends Goal {
/*    */   private final Mob mob;
/*    */   private final Level level;
/*    */   
/*    */   public ClimbOnTopOfPowderSnowGoal(Mob paramMob, Level paramLevel) {
/* 18 */     this.mob = paramMob;
/* 19 */     this.level = paramLevel;
/* 20 */     setFlags(EnumSet.of(Goal.Flag.JUMP));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 25 */     boolean bool = (this.mob.wasInPowderSnow || this.mob.isInPowderSnow) ? true : false;
/* 26 */     if (!bool || !this.mob.getType().is(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS)) {
/* 27 */       return false;
/*    */     }
/* 29 */     BlockPos blockPos = this.mob.blockPosition().above();
/* 30 */     BlockState blockState = this.level.getBlockState(blockPos);
/* 31 */     return (blockState.is(Blocks.POWDER_SNOW) || blockState.getCollisionShape((BlockGetter)this.level, blockPos) == Shapes.empty());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean requiresUpdateEveryTick() {
/* 36 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 41 */     this.mob.getJumpControl().jump();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\ClimbOnTopOfPowderSnowGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */