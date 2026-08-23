/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.BaseRailBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.RailShape;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class MinecartItem extends Item {
/*    */   private final EntityType<? extends AbstractMinecart> type;
/*    */   
/*    */   public MinecartItem(EntityType<? extends AbstractMinecart> paramEntityType, Item.Properties paramProperties) {
/* 25 */     super(paramProperties);
/* 26 */     this.type = paramEntityType;
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult useOn(UseOnContext paramUseOnContext) {
/* 31 */     Level level = paramUseOnContext.getLevel();
/* 32 */     BlockPos blockPos = paramUseOnContext.getClickedPos();
/*    */     
/* 34 */     BlockState blockState = level.getBlockState(blockPos);
/* 35 */     if (!blockState.is(BlockTags.RAILS)) {
/* 36 */       return (InteractionResult)InteractionResult.FAIL;
/*    */     }
/*    */     
/* 39 */     ItemStack itemStack = paramUseOnContext.getItemInHand();
/*    */     
/* 41 */     RailShape railShape = (blockState.getBlock() instanceof BaseRailBlock) ? (RailShape)blockState.getValue(((BaseRailBlock)blockState.getBlock()).getShapeProperty()) : RailShape.NORTH_SOUTH;
/* 42 */     double d = 0.0D;
/* 43 */     if (railShape.isSlope()) {
/* 44 */       d = 0.5D;
/*    */     }
/* 46 */     Vec3 vec3 = new Vec3(blockPos.getX() + 0.5D, blockPos.getY() + 0.0625D + d, blockPos.getZ() + 0.5D);
/* 47 */     AbstractMinecart abstractMinecart = AbstractMinecart.createMinecart(level, vec3.x, vec3.y, vec3.z, this.type, EntitySpawnReason.DISPENSER, itemStack, paramUseOnContext.getPlayer());
/* 48 */     if (abstractMinecart == null) {
/* 49 */       return (InteractionResult)InteractionResult.FAIL;
/*    */     }
/*    */     
/* 52 */     if (AbstractMinecart.useExperimentalMovement(level)) {
/* 53 */       List list = level.getEntities(null, abstractMinecart.getBoundingBox());
/* 54 */       for (Entity entity : list) {
/* 55 */         if (entity instanceof AbstractMinecart) {
/* 56 */           return (InteractionResult)InteractionResult.FAIL;
/*    */         }
/*    */       } 
/*    */     } 
/* 60 */     if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 61 */       serverLevel.addFreshEntity((Entity)abstractMinecart);
/* 62 */       serverLevel.gameEvent((Holder)GameEvent.ENTITY_PLACE, blockPos, GameEvent.Context.of((Entity)paramUseOnContext.getPlayer(), serverLevel.getBlockState(blockPos.below()))); }
/*    */ 
/*    */     
/* 65 */     itemStack.shrink(1);
/* 66 */     return (InteractionResult)InteractionResult.SUCCESS;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\MinecartItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */