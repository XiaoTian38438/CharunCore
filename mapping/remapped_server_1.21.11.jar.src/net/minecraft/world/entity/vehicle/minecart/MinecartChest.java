/*    */ package net.minecraft.world.entity.vehicle.minecart;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.ContainerUser;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.monster.piglin.PiglinAi;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*    */ import net.minecraft.world.inventory.ChestMenu;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ 
/*    */ public class MinecartChest extends AbstractMinecartContainer {
/*    */   public MinecartChest(EntityType<? extends MinecartChest> paramEntityType, Level paramLevel) {
/* 25 */     super(paramEntityType, paramLevel);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Item getDropItem() {
/* 30 */     return Items.CHEST_MINECART;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack getPickResult() {
/* 35 */     return new ItemStack((ItemLike)Items.CHEST_MINECART);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getContainerSize() {
/* 40 */     return 27;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getDefaultDisplayBlockState() {
/* 45 */     return (BlockState)Blocks.CHEST.defaultBlockState().setValue((Property)ChestBlock.FACING, (Comparable)Direction.NORTH);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getDefaultDisplayOffset() {
/* 50 */     return 8;
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractContainerMenu createMenu(int paramInt, Inventory paramInventory) {
/* 55 */     return (AbstractContainerMenu)ChestMenu.threeRows(paramInt, paramInventory, (Container)this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void stopOpen(ContainerUser paramContainerUser) {
/* 60 */     level().gameEvent((Holder)GameEvent.CONTAINER_CLOSE, position(), GameEvent.Context.of((Entity)paramContainerUser.getLivingEntity()));
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult interact(Player paramPlayer, InteractionHand paramInteractionHand) {
/* 65 */     InteractionResult interactionResult = interactWithContainerVehicle(paramPlayer);
/* 66 */     if (interactionResult.consumesAction()) { Level level = paramPlayer.level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 67 */         gameEvent((Holder)GameEvent.CONTAINER_OPEN, (Entity)paramPlayer);
/* 68 */         PiglinAi.angerNearbyPiglins(serverLevel, paramPlayer, true); }
/*    */        }
/* 70 */      return interactionResult;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\vehicle\minecart\MinecartChest.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */