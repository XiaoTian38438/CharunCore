/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.item.component.TooltipDisplay;
/*     */ 
/*     */ public class SmithingTemplateItem
/*     */   extends Item {
/*  14 */   private static final ChatFormatting TITLE_FORMAT = ChatFormatting.GRAY;
/*  15 */   private static final ChatFormatting DESCRIPTION_FORMAT = ChatFormatting.BLUE;
/*  16 */   private static final Component INGREDIENTS_TITLE = (Component)Component.translatable(Util.makeDescriptionId("item", Identifier.withDefaultNamespace("smithing_template.ingredients"))).withStyle(TITLE_FORMAT);
/*  17 */   private static final Component APPLIES_TO_TITLE = (Component)Component.translatable(Util.makeDescriptionId("item", Identifier.withDefaultNamespace("smithing_template.applies_to"))).withStyle(TITLE_FORMAT);
/*  18 */   private static final Component SMITHING_TEMPLATE_SUFFIX = (Component)Component.translatable(Util.makeDescriptionId("item", Identifier.withDefaultNamespace("smithing_template"))).withStyle(TITLE_FORMAT);
/*  19 */   private static final Component ARMOR_TRIM_APPLIES_TO = (Component)Component.translatable(Util.makeDescriptionId("item", Identifier.withDefaultNamespace("smithing_template.armor_trim.applies_to"))).withStyle(DESCRIPTION_FORMAT);
/*  20 */   private static final Component ARMOR_TRIM_INGREDIENTS = (Component)Component.translatable(Util.makeDescriptionId("item", Identifier.withDefaultNamespace("smithing_template.armor_trim.ingredients"))).withStyle(DESCRIPTION_FORMAT);
/*  21 */   private static final Component ARMOR_TRIM_BASE_SLOT_DESCRIPTION = (Component)Component.translatable(Util.makeDescriptionId("item", Identifier.withDefaultNamespace("smithing_template.armor_trim.base_slot_description")));
/*  22 */   private static final Component ARMOR_TRIM_ADDITIONS_SLOT_DESCRIPTION = (Component)Component.translatable(Util.makeDescriptionId("item", Identifier.withDefaultNamespace("smithing_template.armor_trim.additions_slot_description")));
/*  23 */   private static final Component NETHERITE_UPGRADE_APPLIES_TO = (Component)Component.translatable(Util.makeDescriptionId("item", Identifier.withDefaultNamespace("smithing_template.netherite_upgrade.applies_to"))).withStyle(DESCRIPTION_FORMAT);
/*  24 */   private static final Component NETHERITE_UPGRADE_INGREDIENTS = (Component)Component.translatable(Util.makeDescriptionId("item", Identifier.withDefaultNamespace("smithing_template.netherite_upgrade.ingredients"))).withStyle(DESCRIPTION_FORMAT);
/*  25 */   private static final Component NETHERITE_UPGRADE_BASE_SLOT_DESCRIPTION = (Component)Component.translatable(Util.makeDescriptionId("item", Identifier.withDefaultNamespace("smithing_template.netherite_upgrade.base_slot_description")));
/*  26 */   private static final Component NETHERITE_UPGRADE_ADDITIONS_SLOT_DESCRIPTION = (Component)Component.translatable(Util.makeDescriptionId("item", Identifier.withDefaultNamespace("smithing_template.netherite_upgrade.additions_slot_description")));
/*  27 */   private static final Identifier EMPTY_SLOT_HELMET = Identifier.withDefaultNamespace("container/slot/helmet");
/*  28 */   private static final Identifier EMPTY_SLOT_CHESTPLATE = Identifier.withDefaultNamespace("container/slot/chestplate");
/*  29 */   private static final Identifier EMPTY_SLOT_LEGGINGS = Identifier.withDefaultNamespace("container/slot/leggings");
/*  30 */   private static final Identifier EMPTY_SLOT_BOOTS = Identifier.withDefaultNamespace("container/slot/boots");
/*  31 */   private static final Identifier EMPTY_SLOT_HOE = Identifier.withDefaultNamespace("container/slot/hoe");
/*  32 */   private static final Identifier EMPTY_SLOT_AXE = Identifier.withDefaultNamespace("container/slot/axe");
/*  33 */   private static final Identifier EMPTY_SLOT_SWORD = Identifier.withDefaultNamespace("container/slot/sword");
/*  34 */   private static final Identifier EMPTY_SLOT_SHOVEL = Identifier.withDefaultNamespace("container/slot/shovel");
/*  35 */   private static final Identifier EMPTY_SLOT_SPEAR = Identifier.withDefaultNamespace("container/slot/spear");
/*  36 */   private static final Identifier EMPTY_SLOT_PICKAXE = Identifier.withDefaultNamespace("container/slot/pickaxe");
/*  37 */   private static final Identifier EMPTY_SLOT_INGOT = Identifier.withDefaultNamespace("container/slot/ingot");
/*  38 */   private static final Identifier EMPTY_SLOT_REDSTONE_DUST = Identifier.withDefaultNamespace("container/slot/redstone_dust");
/*  39 */   private static final Identifier EMPTY_SLOT_QUARTZ = Identifier.withDefaultNamespace("container/slot/quartz");
/*  40 */   private static final Identifier EMPTY_SLOT_EMERALD = Identifier.withDefaultNamespace("container/slot/emerald");
/*  41 */   private static final Identifier EMPTY_SLOT_DIAMOND = Identifier.withDefaultNamespace("container/slot/diamond");
/*  42 */   private static final Identifier EMPTY_SLOT_LAPIS_LAZULI = Identifier.withDefaultNamespace("container/slot/lapis_lazuli");
/*  43 */   private static final Identifier EMPTY_SLOT_AMETHYST_SHARD = Identifier.withDefaultNamespace("container/slot/amethyst_shard");
/*  44 */   private static final Identifier EMPTY_SLOT_NAUTILUS_ARMOR = Identifier.withDefaultNamespace("container/slot/nautilus_armor");
/*     */   
/*     */   private final Component appliesTo;
/*     */   private final Component ingredients;
/*     */   private final Component baseSlotDescription;
/*     */   private final Component additionsSlotDescription;
/*     */   private final List<Identifier> baseSlotEmptyIcons;
/*     */   private final List<Identifier> additionalSlotEmptyIcons;
/*     */   
/*     */   public SmithingTemplateItem(Component paramComponent1, Component paramComponent2, Component paramComponent3, Component paramComponent4, List<Identifier> paramList1, List<Identifier> paramList2, Item.Properties paramProperties) {
/*  54 */     super(paramProperties);
/*     */     
/*  56 */     this.appliesTo = paramComponent1;
/*  57 */     this.ingredients = paramComponent2;
/*  58 */     this.baseSlotDescription = paramComponent3;
/*  59 */     this.additionsSlotDescription = paramComponent4;
/*  60 */     this.baseSlotEmptyIcons = paramList1;
/*  61 */     this.additionalSlotEmptyIcons = paramList2;
/*     */   }
/*     */   
/*     */   public static SmithingTemplateItem createArmorTrimTemplate(Item.Properties paramProperties) {
/*  65 */     return new SmithingTemplateItem(ARMOR_TRIM_APPLIES_TO, ARMOR_TRIM_INGREDIENTS, ARMOR_TRIM_BASE_SLOT_DESCRIPTION, ARMOR_TRIM_ADDITIONS_SLOT_DESCRIPTION, createTrimmableArmorIconList(), createTrimmableMaterialIconList(), paramProperties);
/*     */   }
/*     */   
/*     */   public static SmithingTemplateItem createNetheriteUpgradeTemplate(Item.Properties paramProperties) {
/*  69 */     return new SmithingTemplateItem(NETHERITE_UPGRADE_APPLIES_TO, NETHERITE_UPGRADE_INGREDIENTS, NETHERITE_UPGRADE_BASE_SLOT_DESCRIPTION, NETHERITE_UPGRADE_ADDITIONS_SLOT_DESCRIPTION, createNetheriteUpgradeIconList(), createNetheriteUpgradeMaterialList(), paramProperties);
/*     */   }
/*     */ 
/*     */   
/*     */   private static List<Identifier> createTrimmableArmorIconList() {
/*  74 */     return List.of(EMPTY_SLOT_HELMET, EMPTY_SLOT_CHESTPLATE, EMPTY_SLOT_LEGGINGS, EMPTY_SLOT_BOOTS);
/*     */   }
/*     */   
/*     */   private static List<Identifier> createTrimmableMaterialIconList() {
/*  78 */     return List.of(EMPTY_SLOT_INGOT, EMPTY_SLOT_REDSTONE_DUST, EMPTY_SLOT_LAPIS_LAZULI, EMPTY_SLOT_QUARTZ, EMPTY_SLOT_DIAMOND, EMPTY_SLOT_EMERALD, EMPTY_SLOT_AMETHYST_SHARD);
/*     */   }
/*     */   
/*     */   private static List<Identifier> createNetheriteUpgradeIconList() {
/*  82 */     return List.of(new Identifier[] { EMPTY_SLOT_HELMET, EMPTY_SLOT_SWORD, EMPTY_SLOT_CHESTPLATE, EMPTY_SLOT_PICKAXE, EMPTY_SLOT_LEGGINGS, EMPTY_SLOT_AXE, EMPTY_SLOT_BOOTS, EMPTY_SLOT_HOE, EMPTY_SLOT_SHOVEL, EMPTY_SLOT_NAUTILUS_ARMOR, EMPTY_SLOT_SPEAR });
/*     */   }
/*     */ 
/*     */   
/*     */   private static List<Identifier> createNetheriteUpgradeMaterialList() {
/*  87 */     return List.of(EMPTY_SLOT_INGOT);
/*     */   }
/*     */ 
/*     */   
/*     */   public void appendHoverText(ItemStack paramItemStack, Item.TooltipContext paramTooltipContext, TooltipDisplay paramTooltipDisplay, Consumer<Component> paramConsumer, TooltipFlag paramTooltipFlag) {
/*  92 */     paramConsumer.accept(SMITHING_TEMPLATE_SUFFIX);
/*  93 */     paramConsumer.accept(CommonComponents.EMPTY);
/*  94 */     paramConsumer.accept(APPLIES_TO_TITLE);
/*  95 */     paramConsumer.accept(CommonComponents.space().append(this.appliesTo));
/*  96 */     paramConsumer.accept(INGREDIENTS_TITLE);
/*  97 */     paramConsumer.accept(CommonComponents.space().append(this.ingredients));
/*     */   }
/*     */   
/*     */   public Component getBaseSlotDescription() {
/* 101 */     return this.baseSlotDescription;
/*     */   }
/*     */   
/*     */   public Component getAdditionSlotDescription() {
/* 105 */     return this.additionsSlotDescription;
/*     */   }
/*     */   
/*     */   public List<Identifier> getBaseSlotEmptyIcons() {
/* 109 */     return this.baseSlotEmptyIcons;
/*     */   }
/*     */   
/*     */   public List<Identifier> getAdditionalSlotEmptyIcons() {
/* 113 */     return this.additionalSlotEmptyIcons;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\SmithingTemplateItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */