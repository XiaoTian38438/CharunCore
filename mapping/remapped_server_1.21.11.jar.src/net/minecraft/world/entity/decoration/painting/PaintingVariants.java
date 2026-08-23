/*     */ package net.minecraft.world.entity.decoration.painting;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ 
/*     */ public class PaintingVariants
/*     */ {
/*  13 */   public static final ResourceKey<PaintingVariant> KEBAB = create("kebab");
/*  14 */   public static final ResourceKey<PaintingVariant> AZTEC = create("aztec");
/*  15 */   public static final ResourceKey<PaintingVariant> ALBAN = create("alban");
/*  16 */   public static final ResourceKey<PaintingVariant> AZTEC2 = create("aztec2");
/*  17 */   public static final ResourceKey<PaintingVariant> BOMB = create("bomb");
/*  18 */   public static final ResourceKey<PaintingVariant> PLANT = create("plant");
/*  19 */   public static final ResourceKey<PaintingVariant> WASTELAND = create("wasteland");
/*  20 */   public static final ResourceKey<PaintingVariant> POOL = create("pool");
/*  21 */   public static final ResourceKey<PaintingVariant> COURBET = create("courbet");
/*  22 */   public static final ResourceKey<PaintingVariant> SEA = create("sea");
/*  23 */   public static final ResourceKey<PaintingVariant> SUNSET = create("sunset");
/*  24 */   public static final ResourceKey<PaintingVariant> CREEBET = create("creebet");
/*  25 */   public static final ResourceKey<PaintingVariant> WANDERER = create("wanderer");
/*  26 */   public static final ResourceKey<PaintingVariant> GRAHAM = create("graham");
/*  27 */   public static final ResourceKey<PaintingVariant> MATCH = create("match");
/*  28 */   public static final ResourceKey<PaintingVariant> BUST = create("bust");
/*  29 */   public static final ResourceKey<PaintingVariant> STAGE = create("stage");
/*  30 */   public static final ResourceKey<PaintingVariant> VOID = create("void");
/*  31 */   public static final ResourceKey<PaintingVariant> SKULL_AND_ROSES = create("skull_and_roses");
/*  32 */   public static final ResourceKey<PaintingVariant> WITHER = create("wither");
/*  33 */   public static final ResourceKey<PaintingVariant> FIGHTERS = create("fighters");
/*  34 */   public static final ResourceKey<PaintingVariant> POINTER = create("pointer");
/*  35 */   public static final ResourceKey<PaintingVariant> PIGSCENE = create("pigscene");
/*  36 */   public static final ResourceKey<PaintingVariant> BURNING_SKULL = create("burning_skull");
/*  37 */   public static final ResourceKey<PaintingVariant> SKELETON = create("skeleton");
/*  38 */   public static final ResourceKey<PaintingVariant> DONKEY_KONG = create("donkey_kong");
/*  39 */   public static final ResourceKey<PaintingVariant> EARTH = create("earth");
/*  40 */   public static final ResourceKey<PaintingVariant> WIND = create("wind");
/*  41 */   public static final ResourceKey<PaintingVariant> WATER = create("water");
/*  42 */   public static final ResourceKey<PaintingVariant> FIRE = create("fire");
/*  43 */   public static final ResourceKey<PaintingVariant> BAROQUE = create("baroque");
/*  44 */   public static final ResourceKey<PaintingVariant> HUMBLE = create("humble");
/*  45 */   public static final ResourceKey<PaintingVariant> MEDITATIVE = create("meditative");
/*  46 */   public static final ResourceKey<PaintingVariant> PRAIRIE_RIDE = create("prairie_ride");
/*  47 */   public static final ResourceKey<PaintingVariant> UNPACKED = create("unpacked");
/*  48 */   public static final ResourceKey<PaintingVariant> BACKYARD = create("backyard");
/*  49 */   public static final ResourceKey<PaintingVariant> BOUQUET = create("bouquet");
/*  50 */   public static final ResourceKey<PaintingVariant> CAVEBIRD = create("cavebird");
/*  51 */   public static final ResourceKey<PaintingVariant> CHANGING = create("changing");
/*  52 */   public static final ResourceKey<PaintingVariant> COTAN = create("cotan");
/*  53 */   public static final ResourceKey<PaintingVariant> ENDBOSS = create("endboss");
/*  54 */   public static final ResourceKey<PaintingVariant> FERN = create("fern");
/*  55 */   public static final ResourceKey<PaintingVariant> FINDING = create("finding");
/*  56 */   public static final ResourceKey<PaintingVariant> LOWMIST = create("lowmist");
/*  57 */   public static final ResourceKey<PaintingVariant> ORB = create("orb");
/*  58 */   public static final ResourceKey<PaintingVariant> OWLEMONS = create("owlemons");
/*  59 */   public static final ResourceKey<PaintingVariant> PASSAGE = create("passage");
/*  60 */   public static final ResourceKey<PaintingVariant> POND = create("pond");
/*  61 */   public static final ResourceKey<PaintingVariant> SUNFLOWERS = create("sunflowers");
/*  62 */   public static final ResourceKey<PaintingVariant> TIDES = create("tides");
/*  63 */   public static final ResourceKey<PaintingVariant> DENNIS = create("dennis");
/*     */   
/*     */   public static void bootstrap(BootstrapContext<PaintingVariant> paramBootstrapContext) {
/*  66 */     register(paramBootstrapContext, KEBAB, 1, 1);
/*  67 */     register(paramBootstrapContext, AZTEC, 1, 1);
/*  68 */     register(paramBootstrapContext, ALBAN, 1, 1);
/*  69 */     register(paramBootstrapContext, AZTEC2, 1, 1);
/*  70 */     register(paramBootstrapContext, BOMB, 1, 1);
/*  71 */     register(paramBootstrapContext, PLANT, 1, 1);
/*  72 */     register(paramBootstrapContext, WASTELAND, 1, 1);
/*  73 */     register(paramBootstrapContext, POOL, 2, 1);
/*  74 */     register(paramBootstrapContext, COURBET, 2, 1);
/*  75 */     register(paramBootstrapContext, SEA, 2, 1);
/*  76 */     register(paramBootstrapContext, SUNSET, 2, 1);
/*  77 */     register(paramBootstrapContext, CREEBET, 2, 1);
/*  78 */     register(paramBootstrapContext, WANDERER, 1, 2);
/*  79 */     register(paramBootstrapContext, GRAHAM, 1, 2);
/*  80 */     register(paramBootstrapContext, MATCH, 2, 2);
/*  81 */     register(paramBootstrapContext, BUST, 2, 2);
/*  82 */     register(paramBootstrapContext, STAGE, 2, 2);
/*  83 */     register(paramBootstrapContext, VOID, 2, 2);
/*  84 */     register(paramBootstrapContext, SKULL_AND_ROSES, 2, 2);
/*  85 */     register(paramBootstrapContext, WITHER, 2, 2, false);
/*  86 */     register(paramBootstrapContext, FIGHTERS, 4, 2);
/*  87 */     register(paramBootstrapContext, POINTER, 4, 4);
/*  88 */     register(paramBootstrapContext, PIGSCENE, 4, 4);
/*  89 */     register(paramBootstrapContext, BURNING_SKULL, 4, 4);
/*  90 */     register(paramBootstrapContext, SKELETON, 4, 3);
/*  91 */     register(paramBootstrapContext, EARTH, 2, 2, false);
/*  92 */     register(paramBootstrapContext, WIND, 2, 2, false);
/*  93 */     register(paramBootstrapContext, WATER, 2, 2, false);
/*  94 */     register(paramBootstrapContext, FIRE, 2, 2, false);
/*  95 */     register(paramBootstrapContext, DONKEY_KONG, 4, 3);
/*  96 */     register(paramBootstrapContext, BAROQUE, 2, 2);
/*  97 */     register(paramBootstrapContext, HUMBLE, 2, 2);
/*  98 */     register(paramBootstrapContext, MEDITATIVE, 1, 1);
/*  99 */     register(paramBootstrapContext, PRAIRIE_RIDE, 1, 2);
/* 100 */     register(paramBootstrapContext, UNPACKED, 4, 4);
/* 101 */     register(paramBootstrapContext, BACKYARD, 3, 4);
/* 102 */     register(paramBootstrapContext, BOUQUET, 3, 3);
/* 103 */     register(paramBootstrapContext, CAVEBIRD, 3, 3);
/* 104 */     register(paramBootstrapContext, CHANGING, 4, 2);
/* 105 */     register(paramBootstrapContext, COTAN, 3, 3);
/* 106 */     register(paramBootstrapContext, ENDBOSS, 3, 3);
/* 107 */     register(paramBootstrapContext, FERN, 3, 3);
/* 108 */     register(paramBootstrapContext, FINDING, 4, 2);
/* 109 */     register(paramBootstrapContext, LOWMIST, 4, 2);
/* 110 */     register(paramBootstrapContext, ORB, 4, 4);
/* 111 */     register(paramBootstrapContext, OWLEMONS, 3, 3);
/* 112 */     register(paramBootstrapContext, PASSAGE, 4, 2);
/* 113 */     register(paramBootstrapContext, POND, 3, 4);
/* 114 */     register(paramBootstrapContext, SUNFLOWERS, 3, 3);
/* 115 */     register(paramBootstrapContext, TIDES, 3, 3);
/* 116 */     register(paramBootstrapContext, DENNIS, 3, 3);
/*     */   }
/*     */   
/*     */   private static void register(BootstrapContext<PaintingVariant> paramBootstrapContext, ResourceKey<PaintingVariant> paramResourceKey, int paramInt1, int paramInt2) {
/* 120 */     register(paramBootstrapContext, paramResourceKey, paramInt1, paramInt2, true);
/*     */   }
/*     */   
/*     */   private static void register(BootstrapContext<PaintingVariant> paramBootstrapContext, ResourceKey<PaintingVariant> paramResourceKey, int paramInt1, int paramInt2, boolean paramBoolean) {
/* 124 */     paramBootstrapContext.register(paramResourceKey, new PaintingVariant(paramInt1, paramInt2, paramResourceKey
/*     */ 
/*     */           
/* 127 */           .identifier(), 
/* 128 */           (Optional)Optional.of(Component.translatable(paramResourceKey.identifier().toLanguageKey("painting", "title")).withStyle(ChatFormatting.YELLOW)), 
/* 129 */           paramBoolean ? (Optional)Optional.of(Component.translatable(paramResourceKey.identifier().toLanguageKey("painting", "author")).withStyle(ChatFormatting.GRAY)) : Optional.<Component>empty()));
/*     */   }
/*     */ 
/*     */   
/*     */   private static ResourceKey<PaintingVariant> create(String paramString) {
/* 134 */     return ResourceKey.create(Registries.PAINTING_VARIANT, Identifier.withDefaultNamespace(paramString));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\decoration\painting\PaintingVariants.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */