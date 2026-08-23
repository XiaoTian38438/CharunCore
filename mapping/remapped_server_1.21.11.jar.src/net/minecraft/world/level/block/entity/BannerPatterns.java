/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ 
/*     */ public class BannerPatterns {
/*   9 */   public static final ResourceKey<BannerPattern> BASE = create("base");
/*  10 */   public static final ResourceKey<BannerPattern> SQUARE_BOTTOM_LEFT = create("square_bottom_left");
/*  11 */   public static final ResourceKey<BannerPattern> SQUARE_BOTTOM_RIGHT = create("square_bottom_right");
/*  12 */   public static final ResourceKey<BannerPattern> SQUARE_TOP_LEFT = create("square_top_left");
/*  13 */   public static final ResourceKey<BannerPattern> SQUARE_TOP_RIGHT = create("square_top_right");
/*  14 */   public static final ResourceKey<BannerPattern> STRIPE_BOTTOM = create("stripe_bottom");
/*  15 */   public static final ResourceKey<BannerPattern> STRIPE_TOP = create("stripe_top");
/*  16 */   public static final ResourceKey<BannerPattern> STRIPE_LEFT = create("stripe_left");
/*  17 */   public static final ResourceKey<BannerPattern> STRIPE_RIGHT = create("stripe_right");
/*  18 */   public static final ResourceKey<BannerPattern> STRIPE_CENTER = create("stripe_center");
/*  19 */   public static final ResourceKey<BannerPattern> STRIPE_MIDDLE = create("stripe_middle");
/*  20 */   public static final ResourceKey<BannerPattern> STRIPE_DOWNRIGHT = create("stripe_downright");
/*  21 */   public static final ResourceKey<BannerPattern> STRIPE_DOWNLEFT = create("stripe_downleft");
/*  22 */   public static final ResourceKey<BannerPattern> STRIPE_SMALL = create("small_stripes");
/*  23 */   public static final ResourceKey<BannerPattern> CROSS = create("cross");
/*  24 */   public static final ResourceKey<BannerPattern> STRAIGHT_CROSS = create("straight_cross");
/*  25 */   public static final ResourceKey<BannerPattern> TRIANGLE_BOTTOM = create("triangle_bottom");
/*  26 */   public static final ResourceKey<BannerPattern> TRIANGLE_TOP = create("triangle_top");
/*  27 */   public static final ResourceKey<BannerPattern> TRIANGLES_BOTTOM = create("triangles_bottom");
/*  28 */   public static final ResourceKey<BannerPattern> TRIANGLES_TOP = create("triangles_top");
/*  29 */   public static final ResourceKey<BannerPattern> DIAGONAL_LEFT = create("diagonal_left");
/*  30 */   public static final ResourceKey<BannerPattern> DIAGONAL_RIGHT = create("diagonal_up_right");
/*  31 */   public static final ResourceKey<BannerPattern> DIAGONAL_LEFT_MIRROR = create("diagonal_up_left");
/*  32 */   public static final ResourceKey<BannerPattern> DIAGONAL_RIGHT_MIRROR = create("diagonal_right");
/*  33 */   public static final ResourceKey<BannerPattern> CIRCLE_MIDDLE = create("circle");
/*  34 */   public static final ResourceKey<BannerPattern> RHOMBUS_MIDDLE = create("rhombus");
/*  35 */   public static final ResourceKey<BannerPattern> HALF_VERTICAL = create("half_vertical");
/*  36 */   public static final ResourceKey<BannerPattern> HALF_HORIZONTAL = create("half_horizontal");
/*  37 */   public static final ResourceKey<BannerPattern> HALF_VERTICAL_MIRROR = create("half_vertical_right");
/*  38 */   public static final ResourceKey<BannerPattern> HALF_HORIZONTAL_MIRROR = create("half_horizontal_bottom");
/*  39 */   public static final ResourceKey<BannerPattern> BORDER = create("border");
/*  40 */   public static final ResourceKey<BannerPattern> CURLY_BORDER = create("curly_border");
/*  41 */   public static final ResourceKey<BannerPattern> GRADIENT = create("gradient");
/*  42 */   public static final ResourceKey<BannerPattern> GRADIENT_UP = create("gradient_up");
/*  43 */   public static final ResourceKey<BannerPattern> BRICKS = create("bricks");
/*  44 */   public static final ResourceKey<BannerPattern> GLOBE = create("globe");
/*  45 */   public static final ResourceKey<BannerPattern> CREEPER = create("creeper");
/*  46 */   public static final ResourceKey<BannerPattern> SKULL = create("skull");
/*  47 */   public static final ResourceKey<BannerPattern> FLOWER = create("flower");
/*  48 */   public static final ResourceKey<BannerPattern> MOJANG = create("mojang");
/*  49 */   public static final ResourceKey<BannerPattern> PIGLIN = create("piglin");
/*  50 */   public static final ResourceKey<BannerPattern> FLOW = create("flow");
/*  51 */   public static final ResourceKey<BannerPattern> GUSTER = create("guster");
/*     */   
/*     */   private static ResourceKey<BannerPattern> create(String paramString) {
/*  54 */     return ResourceKey.create(Registries.BANNER_PATTERN, Identifier.withDefaultNamespace(paramString));
/*     */   }
/*     */   
/*     */   public static void bootstrap(BootstrapContext<BannerPattern> paramBootstrapContext) {
/*  58 */     register(paramBootstrapContext, BASE);
/*  59 */     register(paramBootstrapContext, SQUARE_BOTTOM_LEFT);
/*  60 */     register(paramBootstrapContext, SQUARE_BOTTOM_RIGHT);
/*  61 */     register(paramBootstrapContext, SQUARE_TOP_LEFT);
/*  62 */     register(paramBootstrapContext, SQUARE_TOP_RIGHT);
/*  63 */     register(paramBootstrapContext, STRIPE_BOTTOM);
/*  64 */     register(paramBootstrapContext, STRIPE_TOP);
/*  65 */     register(paramBootstrapContext, STRIPE_LEFT);
/*  66 */     register(paramBootstrapContext, STRIPE_RIGHT);
/*  67 */     register(paramBootstrapContext, STRIPE_CENTER);
/*  68 */     register(paramBootstrapContext, STRIPE_MIDDLE);
/*  69 */     register(paramBootstrapContext, STRIPE_DOWNRIGHT);
/*  70 */     register(paramBootstrapContext, STRIPE_DOWNLEFT);
/*  71 */     register(paramBootstrapContext, STRIPE_SMALL);
/*  72 */     register(paramBootstrapContext, CROSS);
/*  73 */     register(paramBootstrapContext, STRAIGHT_CROSS);
/*  74 */     register(paramBootstrapContext, TRIANGLE_BOTTOM);
/*  75 */     register(paramBootstrapContext, TRIANGLE_TOP);
/*  76 */     register(paramBootstrapContext, TRIANGLES_BOTTOM);
/*  77 */     register(paramBootstrapContext, TRIANGLES_TOP);
/*  78 */     register(paramBootstrapContext, DIAGONAL_LEFT);
/*  79 */     register(paramBootstrapContext, DIAGONAL_RIGHT);
/*  80 */     register(paramBootstrapContext, DIAGONAL_LEFT_MIRROR);
/*  81 */     register(paramBootstrapContext, DIAGONAL_RIGHT_MIRROR);
/*  82 */     register(paramBootstrapContext, CIRCLE_MIDDLE);
/*  83 */     register(paramBootstrapContext, RHOMBUS_MIDDLE);
/*  84 */     register(paramBootstrapContext, HALF_VERTICAL);
/*  85 */     register(paramBootstrapContext, HALF_HORIZONTAL);
/*  86 */     register(paramBootstrapContext, HALF_VERTICAL_MIRROR);
/*  87 */     register(paramBootstrapContext, HALF_HORIZONTAL_MIRROR);
/*  88 */     register(paramBootstrapContext, BORDER);
/*  89 */     register(paramBootstrapContext, GRADIENT);
/*  90 */     register(paramBootstrapContext, GRADIENT_UP);
/*     */     
/*  92 */     register(paramBootstrapContext, BRICKS);
/*  93 */     register(paramBootstrapContext, CURLY_BORDER);
/*  94 */     register(paramBootstrapContext, GLOBE);
/*  95 */     register(paramBootstrapContext, CREEPER);
/*  96 */     register(paramBootstrapContext, SKULL);
/*  97 */     register(paramBootstrapContext, FLOWER);
/*  98 */     register(paramBootstrapContext, MOJANG);
/*  99 */     register(paramBootstrapContext, PIGLIN);
/* 100 */     register(paramBootstrapContext, FLOW);
/* 101 */     register(paramBootstrapContext, GUSTER);
/*     */   }
/*     */   
/*     */   public static void register(BootstrapContext<BannerPattern> paramBootstrapContext, ResourceKey<BannerPattern> paramResourceKey) {
/* 105 */     paramBootstrapContext.register(paramResourceKey, new BannerPattern(paramResourceKey
/* 106 */           .identifier(), "block.minecraft.banner." + paramResourceKey
/* 107 */           .identifier().toShortLanguageKey()));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\BannerPatterns.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */