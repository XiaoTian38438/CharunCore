/*     */ package net.minecraft.world.item.component;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.List;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.TooltipFlag;
/*     */ 
/*     */ public final class ChargedProjectiles implements TooltipProvider {
/*  20 */   public static final ChargedProjectiles EMPTY = new ChargedProjectiles(List.of()); public static final Codec<ChargedProjectiles> CODEC;
/*     */   static {
/*  22 */     CODEC = ItemStack.CODEC.listOf().xmap(ChargedProjectiles::new, paramChargedProjectiles -> paramChargedProjectiles.items);
/*     */ 
/*     */     
/*  25 */     STREAM_CODEC = ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()).map(ChargedProjectiles::new, paramChargedProjectiles -> paramChargedProjectiles.items);
/*     */   }
/*     */   public static final StreamCodec<RegistryFriendlyByteBuf, ChargedProjectiles> STREAM_CODEC; private final List<ItemStack> items;
/*     */   
/*     */   private ChargedProjectiles(List<ItemStack> paramList) {
/*  30 */     this.items = paramList;
/*     */   }
/*     */   
/*     */   public static ChargedProjectiles of(ItemStack paramItemStack) {
/*  34 */     return new ChargedProjectiles(List.of(paramItemStack.copy()));
/*     */   }
/*     */   
/*     */   public static ChargedProjectiles of(List<ItemStack> paramList) {
/*  38 */     return new ChargedProjectiles(List.copyOf(Lists.transform(paramList, ItemStack::copy)));
/*     */   }
/*     */   
/*     */   public boolean contains(Item paramItem) {
/*  42 */     for (ItemStack itemStack : this.items) {
/*  43 */       if (itemStack.is(paramItem)) {
/*  44 */         return true;
/*     */       }
/*     */     } 
/*  47 */     return false;
/*     */   }
/*     */   
/*     */   public List<ItemStack> getItems() {
/*  51 */     return Lists.transform(this.items, ItemStack::copy);
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  55 */     return this.items.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/*  60 */     if (this == paramObject) {
/*  61 */       return true;
/*     */     }
/*  63 */     if (paramObject instanceof ChargedProjectiles) { ChargedProjectiles chargedProjectiles = (ChargedProjectiles)paramObject; if (ItemStack.listMatches(this.items, chargedProjectiles.items)); }  return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  68 */     return ItemStack.hashStackList(this.items);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  73 */     return "ChargedProjectiles[items=" + String.valueOf(this.items) + "]";
/*     */   }
/*     */ 
/*     */   
/*     */   public void addToTooltip(Item.TooltipContext paramTooltipContext, Consumer<Component> paramConsumer, TooltipFlag paramTooltipFlag, DataComponentGetter paramDataComponentGetter) {
/*  78 */     ItemStack itemStack = null;
/*  79 */     byte b = 0;
/*  80 */     for (ItemStack itemStack1 : this.items) {
/*  81 */       if (itemStack == null) {
/*  82 */         itemStack = itemStack1;
/*  83 */         b = 1; continue;
/*  84 */       }  if (ItemStack.matches(itemStack, itemStack1)) {
/*  85 */         b++; continue;
/*     */       } 
/*  87 */       addProjectileTooltip(paramTooltipContext, paramConsumer, itemStack, b);
/*  88 */       itemStack = itemStack1;
/*  89 */       b = 1;
/*     */     } 
/*     */     
/*  92 */     if (itemStack != null) {
/*  93 */       addProjectileTooltip(paramTooltipContext, paramConsumer, itemStack, b);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void addProjectileTooltip(Item.TooltipContext paramTooltipContext, Consumer<Component> paramConsumer, ItemStack paramItemStack, int paramInt) {
/*  98 */     if (paramInt == 1) {
/*  99 */       paramConsumer.accept(Component.translatable("item.minecraft.crossbow.projectile.single", new Object[] { paramItemStack.getDisplayName() }));
/*     */     } else {
/* 101 */       paramConsumer.accept(Component.translatable("item.minecraft.crossbow.projectile.multiple", new Object[] { Integer.valueOf(paramInt), paramItemStack.getDisplayName() }));
/*     */     } 
/* 103 */     TooltipDisplay tooltipDisplay = (TooltipDisplay)paramItemStack.getOrDefault(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT);
/* 104 */     paramItemStack.addDetailsToTooltip(paramTooltipContext, tooltipDisplay, null, (TooltipFlag)TooltipFlag.NORMAL, paramComponent -> paramConsumer.accept(Component.literal("  ").append(paramComponent).withStyle(ChatFormatting.GRAY)));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\component\ChargedProjectiles.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */