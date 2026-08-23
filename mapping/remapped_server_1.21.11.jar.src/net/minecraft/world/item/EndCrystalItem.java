/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.dimension.end.EndDragonFight;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ public class EndCrystalItem extends Item {
/*    */   public EndCrystalItem(Item.Properties paramProperties) {
/* 20 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult useOn(UseOnContext paramUseOnContext) {
/* 25 */     Level level = paramUseOnContext.getLevel();
/* 26 */     BlockPos blockPos1 = paramUseOnContext.getClickedPos();
/*    */     
/* 28 */     BlockState blockState = level.getBlockState(blockPos1);
/* 29 */     if (!blockState.is(Blocks.OBSIDIAN) && !blockState.is(Blocks.BEDROCK)) {
/* 30 */       return (InteractionResult)InteractionResult.FAIL;
/*    */     }
/*    */     
/* 33 */     BlockPos blockPos2 = blockPos1.above();
/* 34 */     if (!level.isEmptyBlock(blockPos2)) {
/* 35 */       return (InteractionResult)InteractionResult.FAIL;
/*    */     }
/*    */     
/* 38 */     double d1 = blockPos2.getX();
/* 39 */     double d2 = blockPos2.getY();
/* 40 */     double d3 = blockPos2.getZ();
/*    */     
/* 42 */     List list = level.getEntities(null, new AABB(d1, d2, d3, d1 + 1.0D, d2 + 2.0D, d3 + 1.0D));
/* 43 */     if (!list.isEmpty()) {
/* 44 */       return (InteractionResult)InteractionResult.FAIL;
/*    */     }
/*    */     
/* 47 */     if (level instanceof ServerLevel) {
/* 48 */       EndCrystal endCrystal = new EndCrystal(level, d1 + 0.5D, d2, d3 + 0.5D);
/* 49 */       endCrystal.setShowBottom(false);
/* 50 */       level.addFreshEntity((Entity)endCrystal);
/* 51 */       level.gameEvent((Entity)paramUseOnContext.getPlayer(), (Holder)GameEvent.ENTITY_PLACE, blockPos2);
/*    */       
/* 53 */       EndDragonFight endDragonFight = ((ServerLevel)level).getDragonFight();
/*    */       
/* 55 */       if (endDragonFight != null) {
/* 56 */         endDragonFight.tryRespawn();
/*    */       }
/*    */     } 
/* 59 */     paramUseOnContext.getItemInHand().shrink(1);
/* 60 */     return (InteractionResult)InteractionResult.SUCCESS;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\EndCrystalItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */