/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.component.LodestoneTracker;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ 
/*    */ public class CompassItem
/*    */   extends Item {
/* 23 */   private static final Component LODESTONE_COMPASS_NAME = (Component)Component.translatable("item.minecraft.lodestone_compass");
/*    */   
/*    */   public CompassItem(Item.Properties paramProperties) {
/* 26 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isFoil(ItemStack paramItemStack) {
/* 31 */     return (paramItemStack.has(DataComponents.LODESTONE_TRACKER) || super.isFoil(paramItemStack));
/*    */   }
/*    */ 
/*    */   
/*    */   public void inventoryTick(ItemStack paramItemStack, ServerLevel paramServerLevel, Entity paramEntity, EquipmentSlot paramEquipmentSlot) {
/* 36 */     LodestoneTracker lodestoneTracker = (LodestoneTracker)paramItemStack.get(DataComponents.LODESTONE_TRACKER);
/* 37 */     if (lodestoneTracker != null) {
/* 38 */       LodestoneTracker lodestoneTracker1 = lodestoneTracker.tick(paramServerLevel);
/* 39 */       if (lodestoneTracker1 != lodestoneTracker) {
/* 40 */         paramItemStack.set(DataComponents.LODESTONE_TRACKER, lodestoneTracker1);
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult useOn(UseOnContext paramUseOnContext) {
/* 47 */     BlockPos blockPos = paramUseOnContext.getClickedPos();
/* 48 */     Level level = paramUseOnContext.getLevel();
/*    */     
/* 50 */     if (level.getBlockState(blockPos).is(Blocks.LODESTONE)) {
/* 51 */       level.playSound(null, blockPos, SoundEvents.LODESTONE_COMPASS_LOCK, SoundSource.PLAYERS, 1.0F, 1.0F);
/*    */       
/* 53 */       Player player = paramUseOnContext.getPlayer();
/* 54 */       ItemStack itemStack = paramUseOnContext.getItemInHand();
/* 55 */       boolean bool = (!player.hasInfiniteMaterials() && itemStack.getCount() == 1) ? true : false;
/*    */       
/* 57 */       LodestoneTracker lodestoneTracker = new LodestoneTracker(Optional.of(GlobalPos.of(level.dimension(), blockPos)), true);
/* 58 */       if (bool) {
/* 59 */         itemStack.set(DataComponents.LODESTONE_TRACKER, lodestoneTracker);
/*    */       } else {
/* 61 */         ItemStack itemStack1 = itemStack.transmuteCopy(Items.COMPASS, 1);
/* 62 */         itemStack.consume(1, (LivingEntity)player);
/* 63 */         itemStack1.set(DataComponents.LODESTONE_TRACKER, lodestoneTracker);
/* 64 */         if (!player.getInventory().add(itemStack1)) {
/* 65 */           player.drop(itemStack1, false);
/*    */         }
/*    */       } 
/*    */       
/* 69 */       return (InteractionResult)InteractionResult.SUCCESS;
/*    */     } 
/* 71 */     return super.useOn(paramUseOnContext);
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getName(ItemStack paramItemStack) {
/* 76 */     return paramItemStack.has(DataComponents.LODESTONE_TRACKER) ? LODESTONE_COMPASS_NAME : super.getName(paramItemStack);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\CompassItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */