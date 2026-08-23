/*    */ package net.minecraft.world.item;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.decoration.GlowItemFrame;
/*    */ import net.minecraft.world.entity.decoration.HangingEntity;
/*    */ import net.minecraft.world.entity.decoration.ItemFrame;
/*    */ import net.minecraft.world.entity.decoration.painting.Painting;
/*    */ import net.minecraft.world.entity.decoration.painting.PaintingVariant;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.component.TooltipDisplay;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ 
/*    */ public class HangingEntityItem extends Item {
/* 26 */   private static final Component TOOLTIP_RANDOM_VARIANT = (Component)Component.translatable("painting.random").withStyle(ChatFormatting.GRAY);
/*    */   
/*    */   private final EntityType<? extends HangingEntity> type;
/*    */   
/*    */   public HangingEntityItem(EntityType<? extends HangingEntity> paramEntityType, Item.Properties paramProperties) {
/* 31 */     super(paramProperties);
/* 32 */     this.type = paramEntityType;
/*    */   }
/*    */   
/*    */   public InteractionResult useOn(UseOnContext paramUseOnContext) {
/*    */     GlowItemFrame glowItemFrame;
/* 37 */     BlockPos blockPos1 = paramUseOnContext.getClickedPos();
/* 38 */     Direction direction = paramUseOnContext.getClickedFace();
/*    */     
/* 40 */     BlockPos blockPos2 = blockPos1.relative(direction);
/* 41 */     Player player = paramUseOnContext.getPlayer();
/* 42 */     ItemStack itemStack = paramUseOnContext.getItemInHand();
/*    */     
/* 44 */     if (player != null && !mayPlace(player, direction, itemStack, blockPos2)) {
/* 45 */       return (InteractionResult)InteractionResult.FAIL;
/*    */     }
/*    */     
/* 48 */     Level level = paramUseOnContext.getLevel();
/*    */     
/* 50 */     if (this.type == EntityType.PAINTING) {
/* 51 */       Optional<HangingEntity> optional = Painting.create(level, blockPos2, direction);
/* 52 */       if (optional.isEmpty()) {
/* 53 */         return (InteractionResult)InteractionResult.CONSUME;
/*    */       }
/* 55 */       HangingEntity hangingEntity = optional.get();
/* 56 */     } else if (this.type == EntityType.ITEM_FRAME) {
/* 57 */       ItemFrame itemFrame = new ItemFrame(level, blockPos2, direction);
/* 58 */     } else if (this.type == EntityType.GLOW_ITEM_FRAME) {
/* 59 */       glowItemFrame = new GlowItemFrame(level, blockPos2, direction);
/*    */     } else {
/* 61 */       return (InteractionResult)InteractionResult.SUCCESS;
/*    */     } 
/*    */     
/* 64 */     EntityType.createDefaultStackConfig(level, itemStack, (LivingEntity)player).accept(glowItemFrame);
/*    */     
/* 66 */     if (glowItemFrame.survives()) {
/* 67 */       if (!level.isClientSide()) {
/* 68 */         glowItemFrame.playPlacementSound();
/* 69 */         level.gameEvent((Entity)player, (Holder)GameEvent.ENTITY_PLACE, glowItemFrame.position());
/* 70 */         level.addFreshEntity((Entity)glowItemFrame);
/*    */       } 
/* 72 */       itemStack.shrink(1);
/* 73 */       return (InteractionResult)InteractionResult.SUCCESS;
/*    */     } 
/*    */     
/* 76 */     return (InteractionResult)InteractionResult.CONSUME;
/*    */   }
/*    */   
/*    */   protected boolean mayPlace(Player paramPlayer, Direction paramDirection, ItemStack paramItemStack, BlockPos paramBlockPos) {
/* 80 */     return (!paramDirection.getAxis().isVertical() && paramPlayer.mayUseItemAt(paramBlockPos, paramDirection, paramItemStack));
/*    */   }
/*    */ 
/*    */   
/*    */   public void appendHoverText(ItemStack paramItemStack, Item.TooltipContext paramTooltipContext, TooltipDisplay paramTooltipDisplay, Consumer<Component> paramConsumer, TooltipFlag paramTooltipFlag) {
/* 85 */     if (this.type == EntityType.PAINTING && paramTooltipDisplay.shows(DataComponents.PAINTING_VARIANT)) {
/* 86 */       Holder holder = (Holder)paramItemStack.get(DataComponents.PAINTING_VARIANT);
/* 87 */       if (holder != null) {
/* 88 */         ((PaintingVariant)holder.value()).title().ifPresent(paramConsumer);
/* 89 */         ((PaintingVariant)holder.value()).author().ifPresent(paramConsumer);
/* 90 */         paramConsumer.accept(Component.translatable("painting.dimensions", new Object[] { Integer.valueOf(((PaintingVariant)holder.value()).width()), Integer.valueOf(((PaintingVariant)holder.value()).height()) }));
/* 91 */       } else if (paramTooltipFlag.isCreative()) {
/* 92 */         paramConsumer.accept(TOOLTIP_RANDOM_VARIANT);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\HangingEntityItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */