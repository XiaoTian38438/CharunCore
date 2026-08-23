/*    */ package net.minecraft.world.entity.monster;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*    */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ 
/*    */ public class Giant extends Monster {
/*    */   public Giant(EntityType<? extends Giant> paramEntityType, Level paramLevel) {
/* 12 */     super((EntityType)paramEntityType, paramLevel);
/*    */   }
/*    */   
/*    */   public static AttributeSupplier.Builder createAttributes() {
/* 16 */     return Monster.createMonsterAttributes()
/* 17 */       .add(Attributes.MAX_HEALTH, 100.0D)
/* 18 */       .add(Attributes.MOVEMENT_SPEED, 0.5D)
/* 19 */       .add(Attributes.ATTACK_DAMAGE, 50.0D)
/* 20 */       .add(Attributes.CAMERA_DISTANCE, 16.0D);
/*    */   }
/*    */ 
/*    */   
/*    */   public float getWalkTargetValue(BlockPos paramBlockPos, LevelReader paramLevelReader) {
/* 25 */     return paramLevelReader.getPathfindingCostFromLightLevels(paramBlockPos);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\Giant.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */