/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.ClickAction;
/*     */ import net.minecraft.world.inventory.Slot;
/*     */ import net.minecraft.world.inventory.tooltip.TooltipComponent;
/*     */ import net.minecraft.world.item.component.BundleContents;
/*     */ import net.minecraft.world.item.component.TooltipDisplay;
/*     */ import net.minecraft.world.level.Level;
/*     */ import org.apache.commons.lang3.math.Fraction;
/*     */ 
/*     */ public class BundleItem
/*     */   extends Item {
/*     */   public static final int MAX_SHOWN_GRID_ITEMS_X = 4;
/*     */   public static final int MAX_SHOWN_GRID_ITEMS_Y = 3;
/*     */   public static final int MAX_SHOWN_GRID_ITEMS = 12;
/*     */   public static final int OVERFLOWING_MAX_SHOWN_GRID_ITEMS = 11;
/*  36 */   private static final int FULL_BAR_COLOR = ARGB.colorFromFloat(1.0F, 1.0F, 0.33F, 0.33F);
/*  37 */   private static final int BAR_COLOR = ARGB.colorFromFloat(1.0F, 0.44F, 0.53F, 1.0F);
/*     */   private static final int TICKS_AFTER_FIRST_THROW = 10;
/*     */   private static final int TICKS_BETWEEN_THROWS = 2;
/*     */   private static final int TICKS_MAX_THROW_DURATION = 200;
/*     */   
/*     */   public BundleItem(Item.Properties paramProperties) {
/*  43 */     super(paramProperties);
/*     */   }
/*     */   
/*     */   public static float getFullnessDisplay(ItemStack paramItemStack) {
/*  47 */     BundleContents bundleContents = (BundleContents)paramItemStack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
/*  48 */     return bundleContents.weight().floatValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean overrideStackedOnOther(ItemStack paramItemStack, Slot paramSlot, ClickAction paramClickAction, Player paramPlayer) {
/*  53 */     BundleContents bundleContents = (BundleContents)paramItemStack.get(DataComponents.BUNDLE_CONTENTS);
/*  54 */     if (bundleContents == null) {
/*  55 */       return false;
/*     */     }
/*     */     
/*  58 */     ItemStack itemStack = paramSlot.getItem();
/*  59 */     BundleContents.Mutable mutable = new BundleContents.Mutable(bundleContents);
/*     */ 
/*     */     
/*  62 */     if (paramClickAction == ClickAction.PRIMARY && !itemStack.isEmpty()) {
/*  63 */       if (mutable.tryTransfer(paramSlot, paramPlayer) > 0) {
/*  64 */         playInsertSound((Entity)paramPlayer);
/*     */       } else {
/*  66 */         playInsertFailSound((Entity)paramPlayer);
/*     */       } 
/*  68 */       paramItemStack.set(DataComponents.BUNDLE_CONTENTS, mutable.toImmutable());
/*  69 */       broadcastChangesOnContainerMenu(paramPlayer);
/*  70 */       return true;
/*     */     } 
/*     */ 
/*     */     
/*  74 */     if (paramClickAction == ClickAction.SECONDARY && itemStack.isEmpty()) {
/*  75 */       ItemStack itemStack1 = mutable.removeOne();
/*  76 */       if (itemStack1 != null) {
/*  77 */         ItemStack itemStack2 = paramSlot.safeInsert(itemStack1);
/*  78 */         if (itemStack2.getCount() > 0) {
/*  79 */           mutable.tryInsert(itemStack2);
/*     */         } else {
/*  81 */           playRemoveOneSound((Entity)paramPlayer);
/*     */         } 
/*     */       } 
/*  84 */       paramItemStack.set(DataComponents.BUNDLE_CONTENTS, mutable.toImmutable());
/*  85 */       broadcastChangesOnContainerMenu(paramPlayer);
/*  86 */       return true;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  91 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean overrideOtherStackedOnMe(ItemStack paramItemStack1, ItemStack paramItemStack2, Slot paramSlot, ClickAction paramClickAction, Player paramPlayer, SlotAccess paramSlotAccess) {
/*  97 */     if (paramClickAction == ClickAction.PRIMARY && paramItemStack2.isEmpty()) {
/*  98 */       toggleSelectedItem(paramItemStack1, -1);
/*  99 */       return false;
/*     */     } 
/* 101 */     BundleContents bundleContents = (BundleContents)paramItemStack1.get(DataComponents.BUNDLE_CONTENTS);
/* 102 */     if (bundleContents == null) {
/* 103 */       return false;
/*     */     }
/* 105 */     BundleContents.Mutable mutable = new BundleContents.Mutable(bundleContents);
/*     */ 
/*     */     
/* 108 */     if (paramClickAction == ClickAction.PRIMARY && !paramItemStack2.isEmpty()) {
/* 109 */       if (paramSlot.allowModification(paramPlayer) && mutable.tryInsert(paramItemStack2) > 0) {
/* 110 */         playInsertSound((Entity)paramPlayer);
/*     */       } else {
/* 112 */         playInsertFailSound((Entity)paramPlayer);
/*     */       } 
/* 114 */       paramItemStack1.set(DataComponents.BUNDLE_CONTENTS, mutable.toImmutable());
/* 115 */       broadcastChangesOnContainerMenu(paramPlayer);
/* 116 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 120 */     if (paramClickAction == ClickAction.SECONDARY && paramItemStack2.isEmpty()) {
/* 121 */       if (paramSlot.allowModification(paramPlayer)) {
/* 122 */         ItemStack itemStack = mutable.removeOne();
/* 123 */         if (itemStack != null) {
/* 124 */           playRemoveOneSound((Entity)paramPlayer);
/* 125 */           paramSlotAccess.set(itemStack);
/*     */         } 
/*     */       } 
/* 128 */       paramItemStack1.set(DataComponents.BUNDLE_CONTENTS, mutable.toImmutable());
/* 129 */       broadcastChangesOnContainerMenu(paramPlayer);
/* 130 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 134 */     toggleSelectedItem(paramItemStack1, -1);
/* 135 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult use(Level paramLevel, Player paramPlayer, InteractionHand paramInteractionHand) {
/* 140 */     paramPlayer.startUsingItem(paramInteractionHand);
/* 141 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   private void dropContent(Level paramLevel, Player paramPlayer, ItemStack paramItemStack) {
/* 145 */     if (dropContent(paramItemStack, paramPlayer)) {
/* 146 */       playDropContentsSound(paramLevel, (Entity)paramPlayer);
/* 147 */       paramPlayer.awardStat(Stats.ITEM_USED.get(this));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBarVisible(ItemStack paramItemStack) {
/* 153 */     BundleContents bundleContents = (BundleContents)paramItemStack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
/* 154 */     return (bundleContents.weight().compareTo(Fraction.ZERO) > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBarWidth(ItemStack paramItemStack) {
/* 159 */     BundleContents bundleContents = (BundleContents)paramItemStack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
/*     */     
/* 161 */     return Math.min(1 + Mth.mulAndTruncate(bundleContents.weight(), 12), 13);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBarColor(ItemStack paramItemStack) {
/* 166 */     BundleContents bundleContents = (BundleContents)paramItemStack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
/* 167 */     return (bundleContents.weight().compareTo(Fraction.ONE) >= 0) ? FULL_BAR_COLOR : BAR_COLOR;
/*     */   }
/*     */   
/*     */   public static void toggleSelectedItem(ItemStack paramItemStack, int paramInt) {
/* 171 */     BundleContents bundleContents = (BundleContents)paramItemStack.get(DataComponents.BUNDLE_CONTENTS);
/* 172 */     if (bundleContents == null) {
/*     */       return;
/*     */     }
/*     */     
/* 176 */     BundleContents.Mutable mutable = new BundleContents.Mutable(bundleContents);
/* 177 */     mutable.toggleSelectedItem(paramInt);
/* 178 */     paramItemStack.set(DataComponents.BUNDLE_CONTENTS, mutable.toImmutable());
/*     */   }
/*     */   
/*     */   public static boolean hasSelectedItem(ItemStack paramItemStack) {
/* 182 */     BundleContents bundleContents = (BundleContents)paramItemStack.get(DataComponents.BUNDLE_CONTENTS);
/* 183 */     return (bundleContents != null && bundleContents.getSelectedItem() != -1);
/*     */   }
/*     */   
/*     */   public static int getSelectedItem(ItemStack paramItemStack) {
/* 187 */     BundleContents bundleContents = (BundleContents)paramItemStack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
/* 188 */     return bundleContents.getSelectedItem();
/*     */   }
/*     */   
/*     */   public static ItemStack getSelectedItemStack(ItemStack paramItemStack) {
/* 192 */     BundleContents bundleContents = (BundleContents)paramItemStack.get(DataComponents.BUNDLE_CONTENTS);
/* 193 */     if (bundleContents != null && bundleContents.getSelectedItem() != -1) {
/* 194 */       return bundleContents.getItemUnsafe(bundleContents.getSelectedItem());
/*     */     }
/* 196 */     return ItemStack.EMPTY;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getNumberOfItemsToShow(ItemStack paramItemStack) {
/* 201 */     BundleContents bundleContents = (BundleContents)paramItemStack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
/* 202 */     return bundleContents.getNumberOfItemsToShow();
/*     */   }
/*     */   
/*     */   private boolean dropContent(ItemStack paramItemStack, Player paramPlayer) {
/* 206 */     BundleContents bundleContents = (BundleContents)paramItemStack.get(DataComponents.BUNDLE_CONTENTS);
/* 207 */     if (bundleContents == null || bundleContents.isEmpty()) {
/* 208 */       return false;
/*     */     }
/*     */     
/* 211 */     Optional<ItemStack> optional = removeOneItemFromBundle(paramItemStack, paramPlayer, bundleContents);
/* 212 */     if (optional.isPresent()) {
/* 213 */       paramPlayer.drop(optional.get(), true);
/* 214 */       return true;
/*     */     } 
/* 216 */     return false;
/*     */   }
/*     */   
/*     */   private static Optional<ItemStack> removeOneItemFromBundle(ItemStack paramItemStack, Player paramPlayer, BundleContents paramBundleContents) {
/* 220 */     BundleContents.Mutable mutable = new BundleContents.Mutable(paramBundleContents);
/* 221 */     ItemStack itemStack = mutable.removeOne();
/* 222 */     if (itemStack != null) {
/* 223 */       playRemoveOneSound((Entity)paramPlayer);
/* 224 */       paramItemStack.set(DataComponents.BUNDLE_CONTENTS, mutable.toImmutable());
/* 225 */       return Optional.of(itemStack);
/*     */     } 
/* 227 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUseTick(Level paramLevel, LivingEntity paramLivingEntity, ItemStack paramItemStack, int paramInt) {
/* 232 */     if (paramLivingEntity instanceof Player) { Player player = (Player)paramLivingEntity;
/* 233 */       int i = getUseDuration(paramItemStack, paramLivingEntity);
/* 234 */       boolean bool = (paramInt == i) ? true : false;
/* 235 */       if (bool || (paramInt < i - 10 && paramInt % 2 == 0)) {
/* 236 */         dropContent(paramLevel, player, paramItemStack);
/*     */       } }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public int getUseDuration(ItemStack paramItemStack, LivingEntity paramLivingEntity) {
/* 243 */     return 200;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemUseAnimation getUseAnimation(ItemStack paramItemStack) {
/* 248 */     return ItemUseAnimation.BUNDLE;
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<TooltipComponent> getTooltipImage(ItemStack paramItemStack) {
/* 253 */     TooltipDisplay tooltipDisplay = (TooltipDisplay)paramItemStack.getOrDefault(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT);
/* 254 */     if (!tooltipDisplay.shows(DataComponents.BUNDLE_CONTENTS)) {
/* 255 */       return Optional.empty();
/*     */     }
/* 257 */     return Optional.<BundleContents>ofNullable((BundleContents)paramItemStack.get(DataComponents.BUNDLE_CONTENTS)).map(net.minecraft.world.inventory.tooltip.BundleTooltip::new);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDestroyed(ItemEntity paramItemEntity) {
/* 262 */     BundleContents bundleContents = (BundleContents)paramItemEntity.getItem().get(DataComponents.BUNDLE_CONTENTS);
/* 263 */     if (bundleContents == null) {
/*     */       return;
/*     */     }
/* 266 */     paramItemEntity.getItem().set(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
/* 267 */     ItemUtils.onContainerDestroyed(paramItemEntity, bundleContents.itemsCopy());
/*     */   }
/*     */   
/*     */   public static List<BundleItem> getAllBundleItemColors() {
/* 271 */     return Stream.<Item>of(new Item[] { Items.BUNDLE, Items.WHITE_BUNDLE, Items.ORANGE_BUNDLE, Items.MAGENTA_BUNDLE, Items.LIGHT_BLUE_BUNDLE, Items.YELLOW_BUNDLE, Items.LIME_BUNDLE, Items.PINK_BUNDLE, Items.GRAY_BUNDLE, Items.LIGHT_GRAY_BUNDLE, Items.CYAN_BUNDLE, Items.BLACK_BUNDLE, Items.BROWN_BUNDLE, Items.GREEN_BUNDLE, Items.RED_BUNDLE, Items.BLUE_BUNDLE, Items.PURPLE_BUNDLE
/*     */ 
/*     */         
/* 274 */         }).map(paramItem -> (BundleItem)paramItem)
/* 275 */       .toList();
/*     */   }
/*     */   
/*     */   public static Item getByColor(DyeColor paramDyeColor) {
/* 279 */     switch (paramDyeColor) { default: throw new MatchException(null, null);case WHITE: case ORANGE: case MAGENTA: case LIGHT_BLUE: case YELLOW: case LIME: case PINK: case GRAY: case LIGHT_GRAY: case CYAN: case BLUE: case BROWN: case GREEN: case RED: case BLACK: case PURPLE: break; }  return 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 295 */       Items.PURPLE_BUNDLE;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void playRemoveOneSound(Entity paramEntity) {
/* 300 */     paramEntity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + paramEntity.level().getRandom().nextFloat() * 0.4F);
/*     */   }
/*     */   
/*     */   private static void playInsertSound(Entity paramEntity) {
/* 304 */     paramEntity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + paramEntity.level().getRandom().nextFloat() * 0.4F);
/*     */   }
/*     */   
/*     */   private static void playInsertFailSound(Entity paramEntity) {
/* 308 */     paramEntity.playSound(SoundEvents.BUNDLE_INSERT_FAIL, 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */   private static void playDropContentsSound(Level paramLevel, Entity paramEntity) {
/* 312 */     paramLevel.playSound(null, paramEntity.blockPosition(), SoundEvents.BUNDLE_DROP_CONTENTS, SoundSource.PLAYERS, 0.8F, 0.8F + paramEntity.level().getRandom().nextFloat() * 0.4F);
/*     */   }
/*     */   
/*     */   private void broadcastChangesOnContainerMenu(Player paramPlayer) {
/* 316 */     AbstractContainerMenu abstractContainerMenu = paramPlayer.containerMenu;
/* 317 */     if (abstractContainerMenu != null)
/* 318 */       abstractContainerMenu.slotsChanged((Container)paramPlayer.getInventory()); 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\BundleItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */