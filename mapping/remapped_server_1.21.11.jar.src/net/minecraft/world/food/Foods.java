/*    */ package net.minecraft.world.food;
/*    */ 
/*    */ 
/*    */ public class Foods
/*    */ {
/*  6 */   public static final FoodProperties APPLE = (new FoodProperties.Builder()).nutrition(4).saturationModifier(0.3F).build();
/*  7 */   public static final FoodProperties BAKED_POTATO = (new FoodProperties.Builder()).nutrition(5).saturationModifier(0.6F).build();
/*  8 */   public static final FoodProperties BEEF = (new FoodProperties.Builder()).nutrition(3).saturationModifier(0.3F).build();
/*  9 */   public static final FoodProperties BEETROOT = (new FoodProperties.Builder()).nutrition(1).saturationModifier(0.6F).build();
/* 10 */   public static final FoodProperties BEETROOT_SOUP = stew(6).build();
/* 11 */   public static final FoodProperties BREAD = (new FoodProperties.Builder()).nutrition(5).saturationModifier(0.6F).build();
/* 12 */   public static final FoodProperties CARROT = (new FoodProperties.Builder()).nutrition(3).saturationModifier(0.6F).build();
/* 13 */   public static final FoodProperties CHICKEN = (new FoodProperties.Builder()).nutrition(2).saturationModifier(0.3F).build();
/* 14 */   public static final FoodProperties CHORUS_FRUIT = (new FoodProperties.Builder()).nutrition(4).saturationModifier(0.3F).alwaysEdible().build();
/* 15 */   public static final FoodProperties COD = (new FoodProperties.Builder()).nutrition(2).saturationModifier(0.1F).build();
/* 16 */   public static final FoodProperties COOKED_BEEF = (new FoodProperties.Builder()).nutrition(8).saturationModifier(0.8F).build();
/* 17 */   public static final FoodProperties COOKED_CHICKEN = (new FoodProperties.Builder()).nutrition(6).saturationModifier(0.6F).build();
/* 18 */   public static final FoodProperties COOKED_COD = (new FoodProperties.Builder()).nutrition(5).saturationModifier(0.6F).build();
/* 19 */   public static final FoodProperties COOKED_MUTTON = (new FoodProperties.Builder()).nutrition(6).saturationModifier(0.8F).build();
/* 20 */   public static final FoodProperties COOKED_PORKCHOP = (new FoodProperties.Builder()).nutrition(8).saturationModifier(0.8F).build();
/* 21 */   public static final FoodProperties COOKED_RABBIT = (new FoodProperties.Builder()).nutrition(5).saturationModifier(0.6F).build();
/* 22 */   public static final FoodProperties COOKED_SALMON = (new FoodProperties.Builder()).nutrition(6).saturationModifier(0.8F).build();
/* 23 */   public static final FoodProperties COOKIE = (new FoodProperties.Builder()).nutrition(2).saturationModifier(0.1F).build();
/* 24 */   public static final FoodProperties DRIED_KELP = (new FoodProperties.Builder()).nutrition(1).saturationModifier(0.3F).build();
/* 25 */   public static final FoodProperties ENCHANTED_GOLDEN_APPLE = (new FoodProperties.Builder()).nutrition(4).saturationModifier(1.2F).alwaysEdible().build();
/* 26 */   public static final FoodProperties GOLDEN_APPLE = (new FoodProperties.Builder()).nutrition(4).saturationModifier(1.2F).alwaysEdible().build();
/* 27 */   public static final FoodProperties GOLDEN_CARROT = (new FoodProperties.Builder()).nutrition(6).saturationModifier(1.2F).build();
/* 28 */   public static final FoodProperties HONEY_BOTTLE = (new FoodProperties.Builder()).nutrition(6).saturationModifier(0.1F).alwaysEdible().build();
/* 29 */   public static final FoodProperties MELON_SLICE = (new FoodProperties.Builder()).nutrition(2).saturationModifier(0.3F).build();
/* 30 */   public static final FoodProperties MUSHROOM_STEW = stew(6).build();
/* 31 */   public static final FoodProperties MUTTON = (new FoodProperties.Builder()).nutrition(2).saturationModifier(0.3F).build();
/* 32 */   public static final FoodProperties POISONOUS_POTATO = (new FoodProperties.Builder()).nutrition(2).saturationModifier(0.3F).build();
/* 33 */   public static final FoodProperties PORKCHOP = (new FoodProperties.Builder()).nutrition(3).saturationModifier(0.3F).build();
/* 34 */   public static final FoodProperties POTATO = (new FoodProperties.Builder()).nutrition(1).saturationModifier(0.3F).build();
/* 35 */   public static final FoodProperties PUFFERFISH = (new FoodProperties.Builder()).nutrition(1).saturationModifier(0.1F).build();
/* 36 */   public static final FoodProperties PUMPKIN_PIE = (new FoodProperties.Builder()).nutrition(8).saturationModifier(0.3F).build();
/* 37 */   public static final FoodProperties RABBIT = (new FoodProperties.Builder()).nutrition(3).saturationModifier(0.3F).build();
/* 38 */   public static final FoodProperties RABBIT_STEW = stew(10).build();
/* 39 */   public static final FoodProperties ROTTEN_FLESH = (new FoodProperties.Builder()).nutrition(4).saturationModifier(0.1F).build();
/* 40 */   public static final FoodProperties SALMON = (new FoodProperties.Builder()).nutrition(2).saturationModifier(0.1F).build();
/* 41 */   public static final FoodProperties SPIDER_EYE = (new FoodProperties.Builder()).nutrition(2).saturationModifier(0.8F).build();
/* 42 */   public static final FoodProperties SUSPICIOUS_STEW = stew(6).alwaysEdible().build();
/* 43 */   public static final FoodProperties SWEET_BERRIES = (new FoodProperties.Builder()).nutrition(2).saturationModifier(0.1F).build();
/* 44 */   public static final FoodProperties GLOW_BERRIES = (new FoodProperties.Builder()).nutrition(2).saturationModifier(0.1F).build();
/* 45 */   public static final FoodProperties TROPICAL_FISH = (new FoodProperties.Builder()).nutrition(1).saturationModifier(0.1F).build();
/*    */   
/*    */   private static FoodProperties.Builder stew(int paramInt) {
/* 48 */     return (new FoodProperties.Builder()).nutrition(paramInt).saturationModifier(0.6F);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\food\Foods.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */