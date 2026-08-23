/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.flag.FeatureElement;
/*    */ import net.minecraft.world.flag.FeatureFlag;
/*    */ import net.minecraft.world.flag.FeatureFlagSet;
/*    */ import net.minecraft.world.flag.FeatureFlags;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MenuType<T extends AbstractContainerMenu>
/*    */   implements FeatureElement
/*    */ {
/* 16 */   public static final MenuType<ChestMenu> GENERIC_9x1 = register("generic_9x1", ChestMenu::oneRow);
/* 17 */   public static final MenuType<ChestMenu> GENERIC_9x2 = register("generic_9x2", ChestMenu::twoRows);
/* 18 */   public static final MenuType<ChestMenu> GENERIC_9x3 = register("generic_9x3", ChestMenu::threeRows);
/* 19 */   public static final MenuType<ChestMenu> GENERIC_9x4 = register("generic_9x4", ChestMenu::fourRows);
/* 20 */   public static final MenuType<ChestMenu> GENERIC_9x5 = register("generic_9x5", ChestMenu::fiveRows);
/* 21 */   public static final MenuType<ChestMenu> GENERIC_9x6 = register("generic_9x6", ChestMenu::sixRows);
/* 22 */   public static final MenuType<DispenserMenu> GENERIC_3x3 = register("generic_3x3", DispenserMenu::new);
/* 23 */   public static final MenuType<CrafterMenu> CRAFTER_3x3 = register("crafter_3x3", CrafterMenu::new);
/*    */   
/* 25 */   public static final MenuType<AnvilMenu> ANVIL = register("anvil", AnvilMenu::new);
/* 26 */   public static final MenuType<BeaconMenu> BEACON = register("beacon", BeaconMenu::new);
/* 27 */   public static final MenuType<BlastFurnaceMenu> BLAST_FURNACE = register("blast_furnace", BlastFurnaceMenu::new);
/* 28 */   public static final MenuType<BrewingStandMenu> BREWING_STAND = register("brewing_stand", BrewingStandMenu::new);
/* 29 */   public static final MenuType<CraftingMenu> CRAFTING = register("crafting", CraftingMenu::new);
/* 30 */   public static final MenuType<EnchantmentMenu> ENCHANTMENT = register("enchantment", EnchantmentMenu::new);
/* 31 */   public static final MenuType<FurnaceMenu> FURNACE = register("furnace", FurnaceMenu::new);
/* 32 */   public static final MenuType<GrindstoneMenu> GRINDSTONE = register("grindstone", GrindstoneMenu::new); public static final MenuType<LecternMenu> LECTERN;
/* 33 */   public static final MenuType<HopperMenu> HOPPER = register("hopper", HopperMenu::new); static {
/* 34 */     LECTERN = register("lectern", (paramInt, paramInventory) -> new LecternMenu(paramInt));
/* 35 */   } public static final MenuType<LoomMenu> LOOM = register("loom", LoomMenu::new);
/* 36 */   public static final MenuType<MerchantMenu> MERCHANT = register("merchant", MerchantMenu::new);
/* 37 */   public static final MenuType<ShulkerBoxMenu> SHULKER_BOX = register("shulker_box", ShulkerBoxMenu::new);
/* 38 */   public static final MenuType<SmithingMenu> SMITHING = register("smithing", SmithingMenu::new);
/* 39 */   public static final MenuType<SmokerMenu> SMOKER = register("smoker", SmokerMenu::new);
/* 40 */   public static final MenuType<CartographyTableMenu> CARTOGRAPHY_TABLE = register("cartography_table", CartographyTableMenu::new);
/* 41 */   public static final MenuType<StonecutterMenu> STONECUTTER = register("stonecutter", StonecutterMenu::new);
/*    */   private final FeatureFlagSet requiredFeatures;
/*    */   private final MenuSupplier<T> constructor;
/*    */   
/*    */   private static <T extends AbstractContainerMenu> MenuType<T> register(String paramString, MenuSupplier<T> paramMenuSupplier) {
/* 46 */     return (MenuType<T>)Registry.register(BuiltInRegistries.MENU, paramString, new MenuType<>(paramMenuSupplier, FeatureFlags.VANILLA_SET));
/*    */   }
/*    */   
/*    */   private static <T extends AbstractContainerMenu> MenuType<T> register(String paramString, MenuSupplier<T> paramMenuSupplier, FeatureFlag... paramVarArgs) {
/* 50 */     return (MenuType<T>)Registry.register(BuiltInRegistries.MENU, paramString, new MenuType<>(paramMenuSupplier, FeatureFlags.REGISTRY.subset(paramVarArgs)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private MenuType(MenuSupplier<T> paramMenuSupplier, FeatureFlagSet paramFeatureFlagSet) {
/* 56 */     this.constructor = paramMenuSupplier;
/* 57 */     this.requiredFeatures = paramFeatureFlagSet;
/*    */   }
/*    */   
/*    */   public T create(int paramInt, Inventory paramInventory) {
/* 61 */     return this.constructor.create(paramInt, paramInventory);
/*    */   }
/*    */ 
/*    */   
/*    */   public FeatureFlagSet requiredFeatures() {
/* 66 */     return this.requiredFeatures;
/*    */   }
/*    */   
/*    */   private static interface MenuSupplier<T extends AbstractContainerMenu> {
/*    */     T create(int param1Int, Inventory param1Inventory);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\inventory\MenuType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */