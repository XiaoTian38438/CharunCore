/*    */ package net.minecraft.world.item;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.alchemy.PotionContents;
/*    */ import net.minecraft.world.item.alchemy.Potions;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class PotionItem extends Item {
/*    */   public PotionItem(Item.Properties paramProperties) {
/* 24 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack getDefaultInstance() {
/* 29 */     ItemStack itemStack = super.getDefaultInstance();
/* 30 */     itemStack.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER));
/* 31 */     return itemStack;
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult useOn(UseOnContext paramUseOnContext) {
/* 36 */     Level level = paramUseOnContext.getLevel();
/* 37 */     BlockPos blockPos = paramUseOnContext.getClickedPos();
/* 38 */     Player player = paramUseOnContext.getPlayer();
/* 39 */     ItemStack itemStack = paramUseOnContext.getItemInHand();
/* 40 */     PotionContents potionContents = (PotionContents)itemStack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
/*    */     
/* 42 */     BlockState blockState = level.getBlockState(blockPos);
/* 43 */     if (paramUseOnContext.getClickedFace() != Direction.DOWN && blockState.is(BlockTags.CONVERTABLE_TO_MUD) && potionContents.is(Potions.WATER)) {
/* 44 */       level.playSound(null, blockPos, SoundEvents.GENERIC_SPLASH, SoundSource.BLOCKS, 1.0F, 1.0F);
/*    */       
/* 46 */       player.setItemInHand(paramUseOnContext.getHand(), ItemUtils.createFilledResult(itemStack, player, new ItemStack(Items.GLASS_BOTTLE)));
/*    */       
/* 48 */       if (!level.isClientSide()) {
/* 49 */         ServerLevel serverLevel = (ServerLevel)level;
/* 50 */         for (byte b = 0; b < 5; b++) {
/* 51 */           serverLevel.sendParticles((ParticleOptions)ParticleTypes.SPLASH, blockPos.getX() + level.random.nextDouble(), (blockPos.getY() + 1), blockPos.getZ() + level.random.nextDouble(), 1, 0.0D, 0.0D, 0.0D, 1.0D);
/*    */         }
/*    */       } 
/*    */       
/* 55 */       level.playSound(null, blockPos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 56 */       level.gameEvent(null, (Holder)GameEvent.FLUID_PLACE, blockPos);
/*    */       
/* 58 */       level.setBlockAndUpdate(blockPos, Blocks.MUD.defaultBlockState());
/* 59 */       return (InteractionResult)InteractionResult.SUCCESS;
/*    */     } 
/*    */     
/* 62 */     return (InteractionResult)InteractionResult.PASS;
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getName(ItemStack paramItemStack) {
/* 67 */     PotionContents potionContents = (PotionContents)paramItemStack.get(DataComponents.POTION_CONTENTS);
/* 68 */     return (potionContents != null) ? potionContents.getName(this.descriptionId + ".effect.") : super.getName(paramItemStack);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\PotionItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */