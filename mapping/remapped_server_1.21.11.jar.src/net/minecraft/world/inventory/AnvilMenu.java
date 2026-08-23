/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.item.enchantment.ItemEnchantments;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.AnvilBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class AnvilMenu
/*     */   extends ItemCombinerMenu
/*     */ {
/*     */   public static final int INPUT_SLOT = 0;
/*     */   public static final int ADDITIONAL_SLOT = 1;
/*     */   public static final int RESULT_SLOT = 2;
/*  31 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final boolean DEBUG_COST = false;
/*     */   
/*     */   public static final int MAX_NAME_LENGTH = 50;
/*     */   
/*     */   private int repairItemCountCost;
/*     */   private String itemName;
/*  39 */   private final DataSlot cost = DataSlot.standalone();
/*     */   
/*     */   private boolean onlyRenaming = false;
/*     */   
/*     */   private static final int COST_FAIL = 0;
/*     */   
/*     */   private static final int COST_BASE = 1;
/*     */   
/*     */   private static final int COST_ADDED_BASE = 1;
/*     */   
/*     */   private static final int COST_REPAIR_MATERIAL = 1;
/*     */   
/*     */   private static final int COST_REPAIR_SACRIFICE = 2;
/*     */   
/*     */   private static final int COST_INCOMPATIBLE_PENALTY = 1;
/*     */   
/*     */   private static final int COST_RENAME = 1;
/*     */   
/*     */   private static final int INPUT_SLOT_X_PLACEMENT = 27;
/*     */   
/*     */   private static final int ADDITIONAL_SLOT_X_PLACEMENT = 76;
/*     */   
/*     */   private static final int RESULT_SLOT_X_PLACEMENT = 134;
/*     */   
/*     */   private static final int SLOT_Y_PLACEMENT = 47;
/*     */ 
/*     */   
/*     */   public AnvilMenu(int paramInt, Inventory paramInventory) {
/*  67 */     this(paramInt, paramInventory, ContainerLevelAccess.NULL);
/*     */   }
/*     */   
/*     */   public AnvilMenu(int paramInt, Inventory paramInventory, ContainerLevelAccess paramContainerLevelAccess) {
/*  71 */     super(MenuType.ANVIL, paramInt, paramInventory, paramContainerLevelAccess, createInputSlotDefinitions());
/*     */     
/*  73 */     addDataSlot(this.cost);
/*     */   }
/*     */   
/*     */   private static ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
/*  77 */     return ItemCombinerMenuSlotDefinition.create()
/*  78 */       .withSlot(0, 27, 47, paramItemStack -> true)
/*  79 */       .withSlot(1, 76, 47, paramItemStack -> true)
/*  80 */       .withResultSlot(2, 134, 47)
/*  81 */       .build();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isValidBlock(BlockState paramBlockState) {
/*  86 */     return paramBlockState.is(BlockTags.ANVIL);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean mayPickup(Player paramPlayer, boolean paramBoolean) {
/*  91 */     return ((paramPlayer.hasInfiniteMaterials() || paramPlayer.experienceLevel >= this.cost.get()) && this.cost.get() > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onTake(Player paramPlayer, ItemStack paramItemStack) {
/*  96 */     if (!paramPlayer.hasInfiniteMaterials()) {
/*  97 */       paramPlayer.giveExperienceLevels(-this.cost.get());
/*     */     }
/*     */     
/* 100 */     if (this.repairItemCountCost > 0) {
/* 101 */       ItemStack itemStack = this.inputSlots.getItem(1);
/* 102 */       if (!itemStack.isEmpty() && itemStack.getCount() > this.repairItemCountCost) {
/* 103 */         itemStack.shrink(this.repairItemCountCost);
/* 104 */         this.inputSlots.setItem(1, itemStack);
/*     */       } else {
/* 106 */         this.inputSlots.setItem(1, ItemStack.EMPTY);
/*     */       } 
/* 108 */     } else if (!this.onlyRenaming) {
/* 109 */       this.inputSlots.setItem(1, ItemStack.EMPTY);
/*     */     } 
/* 111 */     this.cost.set(0);
/* 112 */     if (paramPlayer instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)paramPlayer;
/* 113 */       if (!StringUtil.isBlank(this.itemName) && 
/* 114 */         !this.inputSlots.getItem(0).getHoverName().getString().equals(this.itemName))
/*     */       {
/* 116 */         serverPlayer.getTextFilter().processStreamMessage(this.itemName); }  }
/*     */     
/* 118 */     this.inputSlots.setItem(0, ItemStack.EMPTY);
/*     */     
/* 120 */     this.access.execute((paramLevel, paramBlockPos) -> {
/*     */           BlockState blockState = paramLevel.getBlockState(paramBlockPos);
/*     */           if (!paramPlayer.hasInfiniteMaterials() && blockState.is(BlockTags.ANVIL) && paramPlayer.getRandom().nextFloat() < 0.12F) {
/*     */             BlockState blockState1 = AnvilBlock.damage(blockState);
/*     */             if (blockState1 == null) {
/*     */               paramLevel.removeBlock(paramBlockPos, false);
/*     */               paramLevel.levelEvent(1029, paramBlockPos, 0);
/*     */             } else {
/*     */               paramLevel.setBlock(paramBlockPos, blockState1, 2);
/*     */               paramLevel.levelEvent(1030, paramBlockPos, 0);
/*     */             } 
/*     */           } else {
/*     */             paramLevel.levelEvent(1030, paramBlockPos, 0);
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void createResult() {
/* 139 */     ItemStack itemStack1 = this.inputSlots.getItem(0);
/* 140 */     this.onlyRenaming = false;
/* 141 */     this.cost.set(1);
/* 142 */     int i = 0;
/* 143 */     long l = 0L;
/* 144 */     byte b = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 150 */     if (itemStack1.isEmpty() || !EnchantmentHelper.canStoreEnchantments(itemStack1)) {
/* 151 */       this.resultSlots.setItem(0, ItemStack.EMPTY);
/* 152 */       this.cost.set(0);
/*     */       
/*     */       return;
/*     */     } 
/* 156 */     ItemStack itemStack2 = itemStack1.copy();
/* 157 */     ItemStack itemStack3 = this.inputSlots.getItem(1);
/*     */     
/* 159 */     ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(EnchantmentHelper.getEnchantmentsForCrafting(itemStack2));
/*     */     
/* 161 */     l += ((Integer)itemStack1.getOrDefault(DataComponents.REPAIR_COST, Integer.valueOf(0))).intValue() + ((Integer)itemStack3.getOrDefault(DataComponents.REPAIR_COST, Integer.valueOf(0))).intValue();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 166 */     this.repairItemCountCost = 0;
/*     */     
/* 168 */     if (!itemStack3.isEmpty()) {
/* 169 */       boolean bool1 = itemStack3.has(DataComponents.STORED_ENCHANTMENTS);
/*     */       
/* 171 */       if (itemStack2.isDamageableItem() && itemStack1.isValidRepairItem(itemStack3)) {
/* 172 */         int j = Math.min(itemStack2.getDamageValue(), itemStack2.getMaxDamage() / 4);
/* 173 */         if (j <= 0) {
/* 174 */           this.resultSlots.setItem(0, ItemStack.EMPTY);
/* 175 */           this.cost.set(0);
/*     */           return;
/*     */         } 
/* 178 */         byte b1 = 0;
/* 179 */         while (j > 0 && b1 < itemStack3.getCount()) {
/* 180 */           int k = itemStack2.getDamageValue() - j;
/* 181 */           itemStack2.setDamageValue(k);
/* 182 */           i++;
/*     */           
/* 184 */           j = Math.min(itemStack2.getDamageValue(), itemStack2.getMaxDamage() / 4);
/* 185 */           b1++;
/*     */         } 
/* 187 */         this.repairItemCountCost = b1;
/*     */       } else {
/* 189 */         if (!bool1 && (!itemStack2.is(itemStack3.getItem()) || !itemStack2.isDamageableItem())) {
/* 190 */           this.resultSlots.setItem(0, ItemStack.EMPTY);
/* 191 */           this.cost.set(0);
/*     */           return;
/*     */         } 
/* 194 */         if (itemStack2.isDamageableItem() && !bool1) {
/* 195 */           int j = itemStack1.getMaxDamage() - itemStack1.getDamageValue();
/* 196 */           int k = itemStack3.getMaxDamage() - itemStack3.getDamageValue();
/* 197 */           int m = k + itemStack2.getMaxDamage() * 12 / 100;
/* 198 */           int n = j + m;
/* 199 */           int i1 = itemStack2.getMaxDamage() - n;
/* 200 */           if (i1 < 0) {
/* 201 */             i1 = 0;
/*     */           }
/*     */           
/* 204 */           if (i1 < itemStack2.getDamageValue()) {
/* 205 */             itemStack2.setDamageValue(i1);
/* 206 */             i += 2;
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 213 */         ItemEnchantments itemEnchantments = EnchantmentHelper.getEnchantmentsForCrafting(itemStack3);
/* 214 */         boolean bool2 = false;
/* 215 */         boolean bool3 = false;
/*     */         
/* 217 */         for (Object2IntMap.Entry entry : itemEnchantments.entrySet()) {
/* 218 */           Holder holder = (Holder)entry.getKey();
/* 219 */           int j = mutable.getLevel(holder);
/* 220 */           int k = entry.getIntValue();
/* 221 */           k = (j == k) ? (k + 1) : Math.max(k, j);
/*     */           
/* 223 */           Enchantment enchantment = (Enchantment)holder.value();
/* 224 */           boolean bool4 = enchantment.canEnchant(itemStack1);
/* 225 */           if (this.player.hasInfiniteMaterials() || itemStack1.is(Items.ENCHANTED_BOOK)) {
/* 226 */             bool4 = true;
/*     */           }
/*     */           
/* 229 */           for (Holder holder1 : mutable.keySet()) {
/* 230 */             if (!holder1.equals(holder) && !Enchantment.areCompatible(holder, holder1)) {
/* 231 */               bool4 = false;
/* 232 */               i++;
/*     */             } 
/*     */           } 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 239 */           if (!bool4) {
/* 240 */             bool3 = true;
/*     */             continue;
/*     */           } 
/* 243 */           bool2 = true;
/* 244 */           if (k > enchantment.getMaxLevel()) {
/* 245 */             k = enchantment.getMaxLevel();
/*     */           }
/* 247 */           mutable.set(holder, k);
/* 248 */           int m = enchantment.getAnvilCost();
/*     */           
/* 250 */           if (bool1) {
/* 251 */             m = Math.max(1, m / 2);
/*     */           }
/*     */           
/* 254 */           i += m * k;
/*     */           
/* 256 */           if (itemStack1.getCount() > 1) {
/* 257 */             i = 40;
/*     */           }
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 264 */         if (bool3 && !bool2) {
/*     */           
/* 266 */           this.resultSlots.setItem(0, ItemStack.EMPTY);
/* 267 */           this.cost.set(0);
/*     */           
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } 
/* 273 */     if (this.itemName == null || StringUtil.isBlank(this.itemName)) {
/* 274 */       if (itemStack1.has(DataComponents.CUSTOM_NAME)) {
/* 275 */         b = 1;
/*     */         
/* 277 */         i += b;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 282 */         itemStack2.remove(DataComponents.CUSTOM_NAME);
/*     */       } 
/* 284 */     } else if (!this.itemName.equals(itemStack1.getHoverName().getString())) {
/* 285 */       b = 1;
/*     */       
/* 287 */       i += b;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 292 */       itemStack2.set(DataComponents.CUSTOM_NAME, Component.literal(this.itemName));
/*     */     } 
/*     */     
/* 295 */     boolean bool = (i <= 0) ? false : (int)Mth.clamp(l + i, 0L, 2147483647L);
/* 296 */     this.cost.set(bool);
/* 297 */     if (i <= 0)
/*     */     {
/*     */ 
/*     */       
/* 301 */       itemStack2 = ItemStack.EMPTY;
/*     */     }
/* 303 */     if (b == i && b > 0) {
/* 304 */       if (this.cost.get() >= 40)
/*     */       {
/*     */ 
/*     */         
/* 308 */         this.cost.set(39);
/*     */       }
/* 310 */       this.onlyRenaming = true;
/*     */     } 
/* 312 */     if (this.cost.get() >= 40 && !this.player.hasInfiniteMaterials())
/*     */     {
/*     */ 
/*     */       
/* 316 */       itemStack2 = ItemStack.EMPTY;
/*     */     }
/*     */     
/* 319 */     if (!itemStack2.isEmpty()) {
/* 320 */       int j = ((Integer)itemStack2.getOrDefault(DataComponents.REPAIR_COST, Integer.valueOf(0))).intValue();
/* 321 */       if (j < ((Integer)itemStack3.getOrDefault(DataComponents.REPAIR_COST, Integer.valueOf(0))).intValue()) {
/* 322 */         j = ((Integer)itemStack3.getOrDefault(DataComponents.REPAIR_COST, Integer.valueOf(0))).intValue();
/*     */       }
/*     */       
/* 325 */       if (b != i || b == 0) {
/* 326 */         j = calculateIncreasedRepairCost(j);
/*     */       }
/*     */       
/* 329 */       itemStack2.set(DataComponents.REPAIR_COST, Integer.valueOf(j));
/* 330 */       EnchantmentHelper.setEnchantments(itemStack2, mutable.toImmutable());
/*     */     } 
/*     */     
/* 333 */     this.resultSlots.setItem(0, itemStack2);
/*     */     
/* 335 */     broadcastChanges();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int calculateIncreasedRepairCost(int paramInt) {
/* 343 */     return (int)Math.min(paramInt * 2L + 1L, 2147483647L);
/*     */   }
/*     */   
/*     */   public boolean setItemName(String paramString) {
/* 347 */     String str = validateName(paramString);
/* 348 */     if (str == null || str.equals(this.itemName)) {
/* 349 */       return false;
/*     */     }
/*     */     
/* 352 */     this.itemName = str;
/*     */     
/* 354 */     if (getSlot(2).hasItem()) {
/* 355 */       ItemStack itemStack = getSlot(2).getItem();
/*     */       
/* 357 */       if (StringUtil.isBlank(str)) {
/* 358 */         itemStack.remove(DataComponents.CUSTOM_NAME);
/*     */       } else {
/* 360 */         itemStack.set(DataComponents.CUSTOM_NAME, Component.literal(str));
/*     */       } 
/*     */     } 
/*     */     
/* 364 */     createResult();
/* 365 */     return true;
/*     */   }
/*     */   
/*     */   private static String validateName(String paramString) {
/* 369 */     String str = StringUtil.filterText(paramString);
/* 370 */     if (str.length() <= 50) {
/* 371 */       return str;
/*     */     }
/* 373 */     return null;
/*     */   }
/*     */   
/*     */   public int getCost() {
/* 377 */     return this.cost.get();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\inventory\AnvilMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */