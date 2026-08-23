/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.component.InstrumentComponent;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ 
/*    */ public class InstrumentItem
/*    */   extends Item {
/*    */   public InstrumentItem(Item.Properties paramProperties) {
/* 23 */     super(paramProperties);
/*    */   }
/*    */   
/*    */   public static ItemStack create(Item paramItem, Holder<Instrument> paramHolder) {
/* 27 */     ItemStack itemStack = new ItemStack(paramItem);
/* 28 */     itemStack.set(DataComponents.INSTRUMENT, new InstrumentComponent(paramHolder));
/* 29 */     return itemStack;
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult use(Level paramLevel, Player paramPlayer, InteractionHand paramInteractionHand) {
/* 34 */     ItemStack itemStack = paramPlayer.getItemInHand(paramInteractionHand);
/* 35 */     Optional<Holder<Instrument>> optional = getInstrument(itemStack, (HolderLookup.Provider)paramPlayer.registryAccess());
/* 36 */     if (optional.isPresent()) {
/* 37 */       Instrument instrument = (Instrument)((Holder)optional.get()).value();
/* 38 */       paramPlayer.startUsingItem(paramInteractionHand);
/* 39 */       play(paramLevel, paramPlayer, instrument);
/* 40 */       paramPlayer.getCooldowns().addCooldown(itemStack, Mth.floor(instrument.useDuration() * 20.0F));
/* 41 */       paramPlayer.awardStat(Stats.ITEM_USED.get(this));
/* 42 */       return (InteractionResult)InteractionResult.CONSUME;
/*    */     } 
/* 44 */     return (InteractionResult)InteractionResult.FAIL;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getUseDuration(ItemStack paramItemStack, LivingEntity paramLivingEntity) {
/* 49 */     Optional<Holder<Instrument>> optional = getInstrument(paramItemStack, (HolderLookup.Provider)paramLivingEntity.registryAccess());
/* 50 */     return ((Integer)optional.<Integer>map(paramHolder -> Integer.valueOf(Mth.floor(((Instrument)paramHolder.value()).useDuration() * 20.0F))).orElse(Integer.valueOf(0))).intValue();
/*    */   }
/*    */   
/*    */   private Optional<Holder<Instrument>> getInstrument(ItemStack paramItemStack, HolderLookup.Provider paramProvider) {
/* 54 */     InstrumentComponent instrumentComponent = (InstrumentComponent)paramItemStack.get(DataComponents.INSTRUMENT);
/* 55 */     return (instrumentComponent != null) ? instrumentComponent.unwrap(paramProvider) : Optional.<Holder<Instrument>>empty();
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemUseAnimation getUseAnimation(ItemStack paramItemStack) {
/* 60 */     return ItemUseAnimation.TOOT_HORN;
/*    */   }
/*    */   
/*    */   private static void play(Level paramLevel, Player paramPlayer, Instrument paramInstrument) {
/* 64 */     SoundEvent soundEvent = (SoundEvent)paramInstrument.soundEvent().value();
/* 65 */     float f = paramInstrument.range() / 16.0F;
/* 66 */     paramLevel.playSound((Entity)paramPlayer, (Entity)paramPlayer, soundEvent, SoundSource.RECORDS, f, 1.0F);
/* 67 */     paramLevel.gameEvent((Holder)GameEvent.INSTRUMENT_PLAY, paramPlayer.position(), GameEvent.Context.of((Entity)paramPlayer));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\InstrumentItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */