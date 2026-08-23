/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.component.DataComponentMap;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.animal.golem.CopperGolem;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.BlockItemStateProperties;
/*    */ import net.minecraft.world.level.block.CopperGolemStatueBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class CopperGolemStatueBlockEntity extends BlockEntity {
/*    */   public CopperGolemStatueBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 21 */     super(BlockEntityType.COPPER_GOLEM_STATUE, paramBlockPos, paramBlockState);
/*    */   }
/*    */   
/*    */   public void createStatue(CopperGolem paramCopperGolem) {
/* 25 */     setComponents(DataComponentMap.builder()
/* 26 */         .addAll(components())
/* 27 */         .set(DataComponents.CUSTOM_NAME, paramCopperGolem.getCustomName())
/* 28 */         .build());
/* 29 */     setChanged();
/*    */   }
/*    */   
/*    */   public CopperGolem removeStatue(BlockState paramBlockState) {
/* 33 */     CopperGolem copperGolem = (CopperGolem)EntityType.COPPER_GOLEM.create(this.level, EntitySpawnReason.TRIGGERED);
/* 34 */     if (copperGolem != null) {
/* 35 */       copperGolem.setCustomName((Component)components().get(DataComponents.CUSTOM_NAME));
/* 36 */       return initCopperGolem(paramBlockState, copperGolem);
/*    */     } 
/* 38 */     return null;
/*    */   }
/*    */   
/*    */   private CopperGolem initCopperGolem(BlockState paramBlockState, CopperGolem paramCopperGolem) {
/* 42 */     BlockPos blockPos = getBlockPos();
/* 43 */     paramCopperGolem.snapTo((blockPos.getCenter()).x, blockPos.getY(), (blockPos.getCenter()).z, ((Direction)paramBlockState.getValue((Property)CopperGolemStatueBlock.FACING)).toYRot(), 0.0F);
/* 44 */     paramCopperGolem.yHeadRot = paramCopperGolem.getYRot();
/* 45 */     paramCopperGolem.yBodyRot = paramCopperGolem.getYRot();
/* 46 */     paramCopperGolem.playSpawnSound();
/* 47 */     return paramCopperGolem;
/*    */   }
/*    */ 
/*    */   
/*    */   public ClientboundBlockEntityDataPacket getUpdatePacket() {
/* 52 */     return ClientboundBlockEntityDataPacket.create(this);
/*    */   }
/*    */   
/*    */   public ItemStack getItem(ItemStack paramItemStack, CopperGolemStatueBlock.Pose paramPose) {
/* 56 */     paramItemStack.applyComponents(collectComponents());
/* 57 */     paramItemStack.set(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with((Property)CopperGolemStatueBlock.POSE, (Comparable)paramPose));
/* 58 */     return paramItemStack;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\CopperGolemStatueBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */