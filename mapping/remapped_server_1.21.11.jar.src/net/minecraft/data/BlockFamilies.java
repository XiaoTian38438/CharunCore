/*     */ package net.minecraft.data;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ 
/*     */ public class BlockFamilies
/*     */ {
/*  12 */   private static final Map<Block, BlockFamily> MAP = Maps.newHashMap();
/*     */   
/*     */   private static final String RECIPE_GROUP_PREFIX_WOODEN = "wooden";
/*     */   
/*     */   private static final String RECIPE_UNLOCKED_BY_HAS_PLANKS = "has_planks";
/*  17 */   public static final BlockFamily ACACIA_PLANKS = familyBuilder(Blocks.ACACIA_PLANKS)
/*  18 */     .button(Blocks.ACACIA_BUTTON)
/*  19 */     .fence(Blocks.ACACIA_FENCE)
/*  20 */     .fenceGate(Blocks.ACACIA_FENCE_GATE)
/*  21 */     .pressurePlate(Blocks.ACACIA_PRESSURE_PLATE)
/*  22 */     .sign(Blocks.ACACIA_SIGN, Blocks.ACACIA_WALL_SIGN)
/*  23 */     .slab(Blocks.ACACIA_SLAB)
/*  24 */     .stairs(Blocks.ACACIA_STAIRS)
/*  25 */     .door(Blocks.ACACIA_DOOR)
/*  26 */     .trapdoor(Blocks.ACACIA_TRAPDOOR)
/*  27 */     .recipeGroupPrefix("wooden")
/*  28 */     .recipeUnlockedBy("has_planks")
/*  29 */     .getFamily();
/*     */   
/*  31 */   public static final BlockFamily CHERRY_PLANKS = familyBuilder(Blocks.CHERRY_PLANKS)
/*  32 */     .button(Blocks.CHERRY_BUTTON)
/*  33 */     .fence(Blocks.CHERRY_FENCE)
/*  34 */     .fenceGate(Blocks.CHERRY_FENCE_GATE)
/*  35 */     .pressurePlate(Blocks.CHERRY_PRESSURE_PLATE)
/*  36 */     .sign(Blocks.CHERRY_SIGN, Blocks.CHERRY_WALL_SIGN)
/*  37 */     .slab(Blocks.CHERRY_SLAB)
/*  38 */     .stairs(Blocks.CHERRY_STAIRS)
/*  39 */     .door(Blocks.CHERRY_DOOR)
/*  40 */     .trapdoor(Blocks.CHERRY_TRAPDOOR)
/*  41 */     .recipeGroupPrefix("wooden")
/*  42 */     .recipeUnlockedBy("has_planks")
/*  43 */     .getFamily();
/*     */   
/*  45 */   public static final BlockFamily BIRCH_PLANKS = familyBuilder(Blocks.BIRCH_PLANKS)
/*  46 */     .button(Blocks.BIRCH_BUTTON)
/*  47 */     .fence(Blocks.BIRCH_FENCE)
/*  48 */     .fenceGate(Blocks.BIRCH_FENCE_GATE)
/*  49 */     .pressurePlate(Blocks.BIRCH_PRESSURE_PLATE)
/*  50 */     .sign(Blocks.BIRCH_SIGN, Blocks.BIRCH_WALL_SIGN)
/*  51 */     .slab(Blocks.BIRCH_SLAB)
/*  52 */     .stairs(Blocks.BIRCH_STAIRS)
/*  53 */     .door(Blocks.BIRCH_DOOR)
/*  54 */     .trapdoor(Blocks.BIRCH_TRAPDOOR)
/*  55 */     .recipeGroupPrefix("wooden")
/*  56 */     .recipeUnlockedBy("has_planks")
/*  57 */     .getFamily();
/*     */   
/*  59 */   public static final BlockFamily CRIMSON_PLANKS = familyBuilder(Blocks.CRIMSON_PLANKS)
/*  60 */     .button(Blocks.CRIMSON_BUTTON)
/*  61 */     .fence(Blocks.CRIMSON_FENCE)
/*  62 */     .fenceGate(Blocks.CRIMSON_FENCE_GATE)
/*  63 */     .pressurePlate(Blocks.CRIMSON_PRESSURE_PLATE)
/*  64 */     .sign(Blocks.CRIMSON_SIGN, Blocks.CRIMSON_WALL_SIGN)
/*  65 */     .slab(Blocks.CRIMSON_SLAB)
/*  66 */     .stairs(Blocks.CRIMSON_STAIRS)
/*  67 */     .door(Blocks.CRIMSON_DOOR)
/*  68 */     .trapdoor(Blocks.CRIMSON_TRAPDOOR)
/*  69 */     .recipeGroupPrefix("wooden")
/*  70 */     .recipeUnlockedBy("has_planks")
/*  71 */     .getFamily();
/*     */   
/*  73 */   public static final BlockFamily JUNGLE_PLANKS = familyBuilder(Blocks.JUNGLE_PLANKS)
/*  74 */     .button(Blocks.JUNGLE_BUTTON)
/*  75 */     .fence(Blocks.JUNGLE_FENCE)
/*  76 */     .fenceGate(Blocks.JUNGLE_FENCE_GATE)
/*  77 */     .pressurePlate(Blocks.JUNGLE_PRESSURE_PLATE)
/*  78 */     .sign(Blocks.JUNGLE_SIGN, Blocks.JUNGLE_WALL_SIGN)
/*  79 */     .slab(Blocks.JUNGLE_SLAB)
/*  80 */     .stairs(Blocks.JUNGLE_STAIRS)
/*  81 */     .door(Blocks.JUNGLE_DOOR)
/*  82 */     .trapdoor(Blocks.JUNGLE_TRAPDOOR)
/*  83 */     .recipeGroupPrefix("wooden")
/*  84 */     .recipeUnlockedBy("has_planks")
/*  85 */     .getFamily();
/*     */   
/*  87 */   public static final BlockFamily OAK_PLANKS = familyBuilder(Blocks.OAK_PLANKS)
/*  88 */     .button(Blocks.OAK_BUTTON)
/*  89 */     .fence(Blocks.OAK_FENCE)
/*  90 */     .fenceGate(Blocks.OAK_FENCE_GATE)
/*  91 */     .pressurePlate(Blocks.OAK_PRESSURE_PLATE)
/*  92 */     .sign(Blocks.OAK_SIGN, Blocks.OAK_WALL_SIGN)
/*  93 */     .slab(Blocks.OAK_SLAB)
/*  94 */     .stairs(Blocks.OAK_STAIRS)
/*  95 */     .door(Blocks.OAK_DOOR)
/*  96 */     .trapdoor(Blocks.OAK_TRAPDOOR)
/*  97 */     .recipeGroupPrefix("wooden")
/*  98 */     .recipeUnlockedBy("has_planks")
/*  99 */     .getFamily();
/*     */   
/* 101 */   public static final BlockFamily DARK_OAK_PLANKS = familyBuilder(Blocks.DARK_OAK_PLANKS)
/* 102 */     .button(Blocks.DARK_OAK_BUTTON)
/* 103 */     .fence(Blocks.DARK_OAK_FENCE)
/* 104 */     .fenceGate(Blocks.DARK_OAK_FENCE_GATE)
/* 105 */     .pressurePlate(Blocks.DARK_OAK_PRESSURE_PLATE)
/* 106 */     .sign(Blocks.DARK_OAK_SIGN, Blocks.DARK_OAK_WALL_SIGN)
/* 107 */     .slab(Blocks.DARK_OAK_SLAB)
/* 108 */     .stairs(Blocks.DARK_OAK_STAIRS)
/* 109 */     .door(Blocks.DARK_OAK_DOOR)
/* 110 */     .trapdoor(Blocks.DARK_OAK_TRAPDOOR)
/* 111 */     .recipeGroupPrefix("wooden")
/* 112 */     .recipeUnlockedBy("has_planks")
/* 113 */     .getFamily();
/*     */   
/* 115 */   public static final BlockFamily PALE_OAK_PLANKS = familyBuilder(Blocks.PALE_OAK_PLANKS)
/* 116 */     .button(Blocks.PALE_OAK_BUTTON)
/* 117 */     .fence(Blocks.PALE_OAK_FENCE)
/* 118 */     .fenceGate(Blocks.PALE_OAK_FENCE_GATE)
/* 119 */     .pressurePlate(Blocks.PALE_OAK_PRESSURE_PLATE)
/* 120 */     .sign(Blocks.PALE_OAK_SIGN, Blocks.PALE_OAK_WALL_SIGN)
/* 121 */     .slab(Blocks.PALE_OAK_SLAB)
/* 122 */     .stairs(Blocks.PALE_OAK_STAIRS)
/* 123 */     .door(Blocks.PALE_OAK_DOOR)
/* 124 */     .trapdoor(Blocks.PALE_OAK_TRAPDOOR)
/* 125 */     .recipeGroupPrefix("wooden")
/* 126 */     .recipeUnlockedBy("has_planks")
/* 127 */     .getFamily();
/*     */   
/* 129 */   public static final BlockFamily SPRUCE_PLANKS = familyBuilder(Blocks.SPRUCE_PLANKS)
/* 130 */     .button(Blocks.SPRUCE_BUTTON)
/* 131 */     .fence(Blocks.SPRUCE_FENCE)
/* 132 */     .fenceGate(Blocks.SPRUCE_FENCE_GATE)
/* 133 */     .pressurePlate(Blocks.SPRUCE_PRESSURE_PLATE)
/* 134 */     .sign(Blocks.SPRUCE_SIGN, Blocks.SPRUCE_WALL_SIGN)
/* 135 */     .slab(Blocks.SPRUCE_SLAB)
/* 136 */     .stairs(Blocks.SPRUCE_STAIRS)
/* 137 */     .door(Blocks.SPRUCE_DOOR)
/* 138 */     .trapdoor(Blocks.SPRUCE_TRAPDOOR)
/* 139 */     .recipeGroupPrefix("wooden")
/* 140 */     .recipeUnlockedBy("has_planks")
/* 141 */     .getFamily();
/*     */   
/* 143 */   public static final BlockFamily WARPED_PLANKS = familyBuilder(Blocks.WARPED_PLANKS)
/* 144 */     .button(Blocks.WARPED_BUTTON)
/* 145 */     .fence(Blocks.WARPED_FENCE)
/* 146 */     .fenceGate(Blocks.WARPED_FENCE_GATE)
/* 147 */     .pressurePlate(Blocks.WARPED_PRESSURE_PLATE)
/* 148 */     .sign(Blocks.WARPED_SIGN, Blocks.WARPED_WALL_SIGN)
/* 149 */     .slab(Blocks.WARPED_SLAB)
/* 150 */     .stairs(Blocks.WARPED_STAIRS)
/* 151 */     .door(Blocks.WARPED_DOOR)
/* 152 */     .trapdoor(Blocks.WARPED_TRAPDOOR)
/* 153 */     .recipeGroupPrefix("wooden")
/* 154 */     .recipeUnlockedBy("has_planks")
/* 155 */     .getFamily();
/*     */   
/* 157 */   public static final BlockFamily MANGROVE_PLANKS = familyBuilder(Blocks.MANGROVE_PLANKS)
/* 158 */     .button(Blocks.MANGROVE_BUTTON)
/* 159 */     .slab(Blocks.MANGROVE_SLAB)
/* 160 */     .stairs(Blocks.MANGROVE_STAIRS)
/* 161 */     .fence(Blocks.MANGROVE_FENCE)
/* 162 */     .fenceGate(Blocks.MANGROVE_FENCE_GATE)
/* 163 */     .pressurePlate(Blocks.MANGROVE_PRESSURE_PLATE)
/* 164 */     .sign(Blocks.MANGROVE_SIGN, Blocks.MANGROVE_WALL_SIGN)
/* 165 */     .door(Blocks.MANGROVE_DOOR)
/* 166 */     .trapdoor(Blocks.MANGROVE_TRAPDOOR)
/* 167 */     .recipeGroupPrefix("wooden")
/* 168 */     .recipeUnlockedBy("has_planks")
/* 169 */     .getFamily();
/*     */   
/* 171 */   public static final BlockFamily BAMBOO_PLANKS = familyBuilder(Blocks.BAMBOO_PLANKS)
/* 172 */     .button(Blocks.BAMBOO_BUTTON)
/* 173 */     .slab(Blocks.BAMBOO_SLAB)
/* 174 */     .stairs(Blocks.BAMBOO_STAIRS)
/* 175 */     .customFence(Blocks.BAMBOO_FENCE)
/* 176 */     .customFenceGate(Blocks.BAMBOO_FENCE_GATE)
/* 177 */     .pressurePlate(Blocks.BAMBOO_PRESSURE_PLATE)
/* 178 */     .sign(Blocks.BAMBOO_SIGN, Blocks.BAMBOO_WALL_SIGN)
/* 179 */     .door(Blocks.BAMBOO_DOOR)
/* 180 */     .trapdoor(Blocks.BAMBOO_TRAPDOOR)
/* 181 */     .mosaic(Blocks.BAMBOO_MOSAIC)
/* 182 */     .recipeGroupPrefix("wooden")
/* 183 */     .recipeUnlockedBy("has_planks")
/* 184 */     .getFamily();
/*     */   
/* 186 */   public static final BlockFamily BAMBOO_MOSAIC = familyBuilder(Blocks.BAMBOO_MOSAIC)
/* 187 */     .slab(Blocks.BAMBOO_MOSAIC_SLAB)
/* 188 */     .stairs(Blocks.BAMBOO_MOSAIC_STAIRS)
/* 189 */     .getFamily();
/*     */   
/* 191 */   public static final BlockFamily MUD_BRICKS = familyBuilder(Blocks.MUD_BRICKS)
/* 192 */     .wall(Blocks.MUD_BRICK_WALL)
/* 193 */     .stairs(Blocks.MUD_BRICK_STAIRS)
/* 194 */     .slab(Blocks.MUD_BRICK_SLAB)
/* 195 */     .getFamily();
/*     */   
/* 197 */   public static final BlockFamily ANDESITE = familyBuilder(Blocks.ANDESITE)
/* 198 */     .wall(Blocks.ANDESITE_WALL)
/* 199 */     .stairs(Blocks.ANDESITE_STAIRS)
/* 200 */     .slab(Blocks.ANDESITE_SLAB)
/* 201 */     .polished(Blocks.POLISHED_ANDESITE)
/* 202 */     .getFamily();
/*     */   
/* 204 */   public static final BlockFamily POLISHED_ANDESITE = familyBuilder(Blocks.POLISHED_ANDESITE)
/* 205 */     .stairs(Blocks.POLISHED_ANDESITE_STAIRS)
/* 206 */     .slab(Blocks.POLISHED_ANDESITE_SLAB)
/* 207 */     .getFamily();
/*     */   
/* 209 */   public static final BlockFamily BLACKSTONE = familyBuilder(Blocks.BLACKSTONE)
/* 210 */     .wall(Blocks.BLACKSTONE_WALL)
/* 211 */     .stairs(Blocks.BLACKSTONE_STAIRS)
/* 212 */     .slab(Blocks.BLACKSTONE_SLAB)
/* 213 */     .polished(Blocks.POLISHED_BLACKSTONE)
/* 214 */     .getFamily();
/*     */   
/* 216 */   public static final BlockFamily POLISHED_BLACKSTONE = familyBuilder(Blocks.POLISHED_BLACKSTONE)
/* 217 */     .wall(Blocks.POLISHED_BLACKSTONE_WALL)
/* 218 */     .pressurePlate(Blocks.POLISHED_BLACKSTONE_PRESSURE_PLATE)
/* 219 */     .button(Blocks.POLISHED_BLACKSTONE_BUTTON)
/* 220 */     .stairs(Blocks.POLISHED_BLACKSTONE_STAIRS)
/* 221 */     .slab(Blocks.POLISHED_BLACKSTONE_SLAB)
/* 222 */     .polished(Blocks.POLISHED_BLACKSTONE_BRICKS)
/* 223 */     .chiseled(Blocks.CHISELED_POLISHED_BLACKSTONE)
/* 224 */     .getFamily();
/*     */   
/* 226 */   public static final BlockFamily POLISHED_BLACKSTONE_BRICKS = familyBuilder(Blocks.POLISHED_BLACKSTONE_BRICKS)
/* 227 */     .wall(Blocks.POLISHED_BLACKSTONE_BRICK_WALL)
/* 228 */     .stairs(Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS)
/* 229 */     .slab(Blocks.POLISHED_BLACKSTONE_BRICK_SLAB)
/* 230 */     .cracked(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS)
/* 231 */     .getFamily();
/*     */   
/* 233 */   public static final BlockFamily BRICKS = familyBuilder(Blocks.BRICKS)
/* 234 */     .wall(Blocks.BRICK_WALL)
/* 235 */     .stairs(Blocks.BRICK_STAIRS)
/* 236 */     .slab(Blocks.BRICK_SLAB)
/* 237 */     .getFamily();
/*     */   
/* 239 */   public static final BlockFamily END_STONE_BRICKS = familyBuilder(Blocks.END_STONE_BRICKS)
/* 240 */     .wall(Blocks.END_STONE_BRICK_WALL)
/* 241 */     .stairs(Blocks.END_STONE_BRICK_STAIRS)
/* 242 */     .slab(Blocks.END_STONE_BRICK_SLAB)
/* 243 */     .getFamily();
/*     */   
/* 245 */   public static final BlockFamily MOSSY_STONE_BRICKS = familyBuilder(Blocks.MOSSY_STONE_BRICKS)
/* 246 */     .wall(Blocks.MOSSY_STONE_BRICK_WALL)
/* 247 */     .stairs(Blocks.MOSSY_STONE_BRICK_STAIRS)
/* 248 */     .slab(Blocks.MOSSY_STONE_BRICK_SLAB)
/* 249 */     .getFamily();
/*     */   
/* 251 */   public static final BlockFamily COPPER_BLOCK = familyBuilder(Blocks.COPPER_BLOCK)
/* 252 */     .cut(Blocks.CUT_COPPER)
/* 253 */     .dontGenerateModel()
/* 254 */     .getFamily();
/*     */   
/* 256 */   public static final BlockFamily CUT_COPPER = familyBuilder(Blocks.CUT_COPPER)
/* 257 */     .slab(Blocks.CUT_COPPER_SLAB)
/* 258 */     .stairs(Blocks.CUT_COPPER_STAIRS)
/* 259 */     .chiseled(Blocks.CHISELED_COPPER)
/* 260 */     .dontGenerateModel()
/* 261 */     .getFamily();
/*     */   
/* 263 */   public static final BlockFamily WAXED_COPPER_BLOCK = familyBuilder(Blocks.WAXED_COPPER_BLOCK)
/* 264 */     .cut(Blocks.WAXED_CUT_COPPER)
/* 265 */     .recipeGroupPrefix("waxed_cut_copper")
/* 266 */     .dontGenerateModel()
/* 267 */     .getFamily();
/*     */   
/* 269 */   public static final BlockFamily WAXED_CUT_COPPER = familyBuilder(Blocks.WAXED_CUT_COPPER)
/* 270 */     .slab(Blocks.WAXED_CUT_COPPER_SLAB)
/* 271 */     .stairs(Blocks.WAXED_CUT_COPPER_STAIRS)
/* 272 */     .recipeGroupPrefix("waxed_cut_copper")
/* 273 */     .dontGenerateModel()
/* 274 */     .getFamily();
/*     */   
/* 276 */   public static final BlockFamily EXPOSED_COPPER = familyBuilder(Blocks.EXPOSED_COPPER)
/* 277 */     .cut(Blocks.EXPOSED_CUT_COPPER)
/* 278 */     .dontGenerateModel()
/* 279 */     .getFamily();
/*     */   
/* 281 */   public static final BlockFamily EXPOSED_CUT_COPPER = familyBuilder(Blocks.EXPOSED_CUT_COPPER)
/* 282 */     .slab(Blocks.EXPOSED_CUT_COPPER_SLAB)
/* 283 */     .stairs(Blocks.EXPOSED_CUT_COPPER_STAIRS)
/* 284 */     .chiseled(Blocks.EXPOSED_CHISELED_COPPER)
/* 285 */     .dontGenerateModel()
/* 286 */     .getFamily();
/*     */   
/* 288 */   public static final BlockFamily WAXED_EXPOSED_COPPER = familyBuilder(Blocks.WAXED_EXPOSED_COPPER)
/* 289 */     .cut(Blocks.WAXED_EXPOSED_CUT_COPPER)
/* 290 */     .recipeGroupPrefix("waxed_exposed_cut_copper")
/* 291 */     .dontGenerateModel()
/* 292 */     .getFamily();
/*     */   
/* 294 */   public static final BlockFamily WAXED_EXPOSED_CUT_COPPER = familyBuilder(Blocks.WAXED_EXPOSED_CUT_COPPER)
/* 295 */     .slab(Blocks.WAXED_EXPOSED_CUT_COPPER_SLAB)
/* 296 */     .stairs(Blocks.WAXED_EXPOSED_CUT_COPPER_STAIRS)
/* 297 */     .recipeGroupPrefix("waxed_exposed_cut_copper")
/* 298 */     .dontGenerateModel()
/* 299 */     .getFamily();
/*     */   
/* 301 */   public static final BlockFamily WEATHERED_COPPER = familyBuilder(Blocks.WEATHERED_COPPER)
/* 302 */     .cut(Blocks.WEATHERED_CUT_COPPER)
/* 303 */     .dontGenerateModel()
/* 304 */     .getFamily();
/*     */   
/* 306 */   public static final BlockFamily WEATHERED_CUT_COPPER = familyBuilder(Blocks.WEATHERED_CUT_COPPER)
/* 307 */     .slab(Blocks.WEATHERED_CUT_COPPER_SLAB)
/* 308 */     .stairs(Blocks.WEATHERED_CUT_COPPER_STAIRS)
/* 309 */     .chiseled(Blocks.WEATHERED_CHISELED_COPPER)
/* 310 */     .dontGenerateModel()
/* 311 */     .getFamily();
/*     */   
/* 313 */   public static final BlockFamily WAXED_WEATHERED_COPPER = familyBuilder(Blocks.WAXED_WEATHERED_COPPER)
/* 314 */     .cut(Blocks.WAXED_WEATHERED_CUT_COPPER)
/* 315 */     .recipeGroupPrefix("waxed_weathered_cut_copper")
/* 316 */     .dontGenerateModel()
/* 317 */     .getFamily();
/*     */   
/* 319 */   public static final BlockFamily WAXED_WEATHERED_CUT_COPPER = familyBuilder(Blocks.WAXED_WEATHERED_CUT_COPPER)
/* 320 */     .slab(Blocks.WAXED_WEATHERED_CUT_COPPER_SLAB)
/* 321 */     .stairs(Blocks.WAXED_WEATHERED_CUT_COPPER_STAIRS)
/* 322 */     .recipeGroupPrefix("waxed_weathered_cut_copper")
/* 323 */     .dontGenerateModel()
/* 324 */     .getFamily();
/*     */   
/* 326 */   public static final BlockFamily OXIDIZED_COPPER = familyBuilder(Blocks.OXIDIZED_COPPER)
/* 327 */     .cut(Blocks.OXIDIZED_CUT_COPPER)
/* 328 */     .dontGenerateModel()
/* 329 */     .getFamily();
/*     */   
/* 331 */   public static final BlockFamily OXIDIZED_CUT_COPPER = familyBuilder(Blocks.OXIDIZED_CUT_COPPER)
/* 332 */     .slab(Blocks.OXIDIZED_CUT_COPPER_SLAB)
/* 333 */     .stairs(Blocks.OXIDIZED_CUT_COPPER_STAIRS)
/* 334 */     .chiseled(Blocks.OXIDIZED_CHISELED_COPPER)
/* 335 */     .dontGenerateModel()
/* 336 */     .getFamily();
/*     */   
/* 338 */   public static final BlockFamily WAXED_OXIDIZED_COPPER = familyBuilder(Blocks.WAXED_OXIDIZED_COPPER)
/* 339 */     .cut(Blocks.WAXED_OXIDIZED_CUT_COPPER)
/* 340 */     .recipeGroupPrefix("waxed_oxidized_cut_copper")
/* 341 */     .dontGenerateModel()
/* 342 */     .getFamily();
/*     */   
/* 344 */   public static final BlockFamily WAXED_OXIDIZED_CUT_COPPER = familyBuilder(Blocks.WAXED_OXIDIZED_CUT_COPPER)
/* 345 */     .slab(Blocks.WAXED_OXIDIZED_CUT_COPPER_SLAB)
/* 346 */     .stairs(Blocks.WAXED_OXIDIZED_CUT_COPPER_STAIRS)
/* 347 */     .recipeGroupPrefix("waxed_oxidized_cut_copper")
/* 348 */     .dontGenerateModel()
/* 349 */     .getFamily();
/*     */   
/* 351 */   public static final BlockFamily COBBLESTONE = familyBuilder(Blocks.COBBLESTONE)
/* 352 */     .wall(Blocks.COBBLESTONE_WALL)
/* 353 */     .stairs(Blocks.COBBLESTONE_STAIRS)
/* 354 */     .slab(Blocks.COBBLESTONE_SLAB)
/* 355 */     .getFamily();
/*     */   
/* 357 */   public static final BlockFamily MOSSY_COBBLESTONE = familyBuilder(Blocks.MOSSY_COBBLESTONE)
/* 358 */     .wall(Blocks.MOSSY_COBBLESTONE_WALL)
/* 359 */     .stairs(Blocks.MOSSY_COBBLESTONE_STAIRS)
/* 360 */     .slab(Blocks.MOSSY_COBBLESTONE_SLAB)
/* 361 */     .getFamily();
/*     */   
/* 363 */   public static final BlockFamily DIORITE = familyBuilder(Blocks.DIORITE)
/* 364 */     .wall(Blocks.DIORITE_WALL)
/* 365 */     .stairs(Blocks.DIORITE_STAIRS)
/* 366 */     .slab(Blocks.DIORITE_SLAB)
/* 367 */     .polished(Blocks.POLISHED_DIORITE)
/* 368 */     .getFamily();
/*     */   
/* 370 */   public static final BlockFamily POLISHED_DIORITE = familyBuilder(Blocks.POLISHED_DIORITE)
/* 371 */     .stairs(Blocks.POLISHED_DIORITE_STAIRS)
/* 372 */     .slab(Blocks.POLISHED_DIORITE_SLAB)
/* 373 */     .getFamily();
/*     */   
/* 375 */   public static final BlockFamily GRANITE = familyBuilder(Blocks.GRANITE)
/* 376 */     .wall(Blocks.GRANITE_WALL)
/* 377 */     .stairs(Blocks.GRANITE_STAIRS)
/* 378 */     .slab(Blocks.GRANITE_SLAB)
/* 379 */     .polished(Blocks.POLISHED_GRANITE)
/* 380 */     .getFamily();
/*     */   
/* 382 */   public static final BlockFamily POLISHED_GRANITE = familyBuilder(Blocks.POLISHED_GRANITE)
/* 383 */     .stairs(Blocks.POLISHED_GRANITE_STAIRS)
/* 384 */     .slab(Blocks.POLISHED_GRANITE_SLAB)
/* 385 */     .getFamily();
/*     */   
/* 387 */   public static final BlockFamily TUFF = familyBuilder(Blocks.TUFF)
/* 388 */     .wall(Blocks.TUFF_WALL)
/* 389 */     .stairs(Blocks.TUFF_STAIRS)
/* 390 */     .slab(Blocks.TUFF_SLAB)
/* 391 */     .chiseled(Blocks.CHISELED_TUFF)
/* 392 */     .polished(Blocks.POLISHED_TUFF)
/* 393 */     .getFamily();
/*     */   
/* 395 */   public static final BlockFamily POLISHED_TUFF = familyBuilder(Blocks.POLISHED_TUFF)
/* 396 */     .wall(Blocks.POLISHED_TUFF_WALL)
/* 397 */     .stairs(Blocks.POLISHED_TUFF_STAIRS)
/* 398 */     .slab(Blocks.POLISHED_TUFF_SLAB)
/* 399 */     .polished(Blocks.TUFF_BRICKS)
/* 400 */     .getFamily();
/*     */   
/* 402 */   public static final BlockFamily TUFF_BRICKS = familyBuilder(Blocks.TUFF_BRICKS)
/* 403 */     .wall(Blocks.TUFF_BRICK_WALL)
/* 404 */     .stairs(Blocks.TUFF_BRICK_STAIRS)
/* 405 */     .slab(Blocks.TUFF_BRICK_SLAB)
/* 406 */     .chiseled(Blocks.CHISELED_TUFF_BRICKS)
/* 407 */     .getFamily();
/*     */   
/* 409 */   public static final BlockFamily RESIN_BRICKS = familyBuilder(Blocks.RESIN_BRICKS)
/* 410 */     .wall(Blocks.RESIN_BRICK_WALL)
/* 411 */     .stairs(Blocks.RESIN_BRICK_STAIRS)
/* 412 */     .slab(Blocks.RESIN_BRICK_SLAB)
/* 413 */     .chiseled(Blocks.CHISELED_RESIN_BRICKS)
/* 414 */     .getFamily();
/*     */   
/* 416 */   public static final BlockFamily NETHER_BRICKS = familyBuilder(Blocks.NETHER_BRICKS)
/* 417 */     .fence(Blocks.NETHER_BRICK_FENCE)
/* 418 */     .wall(Blocks.NETHER_BRICK_WALL)
/* 419 */     .stairs(Blocks.NETHER_BRICK_STAIRS)
/* 420 */     .slab(Blocks.NETHER_BRICK_SLAB)
/* 421 */     .chiseled(Blocks.CHISELED_NETHER_BRICKS)
/* 422 */     .cracked(Blocks.CRACKED_NETHER_BRICKS)
/* 423 */     .getFamily();
/*     */   
/* 425 */   public static final BlockFamily RED_NETHER_BRICKS = familyBuilder(Blocks.RED_NETHER_BRICKS)
/* 426 */     .slab(Blocks.RED_NETHER_BRICK_SLAB)
/* 427 */     .stairs(Blocks.RED_NETHER_BRICK_STAIRS)
/* 428 */     .wall(Blocks.RED_NETHER_BRICK_WALL)
/* 429 */     .getFamily();
/*     */   
/* 431 */   public static final BlockFamily PRISMARINE = familyBuilder(Blocks.PRISMARINE)
/* 432 */     .wall(Blocks.PRISMARINE_WALL)
/* 433 */     .stairs(Blocks.PRISMARINE_STAIRS)
/* 434 */     .slab(Blocks.PRISMARINE_SLAB)
/* 435 */     .getFamily();
/*     */   
/* 437 */   public static final BlockFamily PURPUR = familyBuilder(Blocks.PURPUR_BLOCK)
/* 438 */     .stairs(Blocks.PURPUR_STAIRS)
/* 439 */     .slab(Blocks.PURPUR_SLAB)
/* 440 */     .dontGenerateRecipe()
/* 441 */     .getFamily();
/*     */   
/* 443 */   public static final BlockFamily PRISMARINE_BRICKS = familyBuilder(Blocks.PRISMARINE_BRICKS)
/* 444 */     .stairs(Blocks.PRISMARINE_BRICK_STAIRS)
/* 445 */     .slab(Blocks.PRISMARINE_BRICK_SLAB)
/* 446 */     .getFamily();
/*     */   
/* 448 */   public static final BlockFamily DARK_PRISMARINE = familyBuilder(Blocks.DARK_PRISMARINE)
/* 449 */     .stairs(Blocks.DARK_PRISMARINE_STAIRS)
/* 450 */     .slab(Blocks.DARK_PRISMARINE_SLAB)
/* 451 */     .getFamily();
/*     */   
/* 453 */   public static final BlockFamily QUARTZ = familyBuilder(Blocks.QUARTZ_BLOCK)
/* 454 */     .stairs(Blocks.QUARTZ_STAIRS)
/* 455 */     .slab(Blocks.QUARTZ_SLAB)
/* 456 */     .chiseled(Blocks.CHISELED_QUARTZ_BLOCK)
/* 457 */     .dontGenerateRecipe()
/* 458 */     .getFamily();
/*     */   
/* 460 */   public static final BlockFamily SMOOTH_QUARTZ = familyBuilder(Blocks.SMOOTH_QUARTZ)
/* 461 */     .stairs(Blocks.SMOOTH_QUARTZ_STAIRS)
/* 462 */     .slab(Blocks.SMOOTH_QUARTZ_SLAB)
/* 463 */     .getFamily();
/*     */   
/* 465 */   public static final BlockFamily SANDSTONE = familyBuilder(Blocks.SANDSTONE)
/* 466 */     .wall(Blocks.SANDSTONE_WALL)
/* 467 */     .stairs(Blocks.SANDSTONE_STAIRS)
/* 468 */     .slab(Blocks.SANDSTONE_SLAB)
/* 469 */     .chiseled(Blocks.CHISELED_SANDSTONE)
/* 470 */     .cut(Blocks.CUT_SANDSTONE)
/* 471 */     .dontGenerateRecipe()
/* 472 */     .getFamily();
/*     */   
/* 474 */   public static final BlockFamily CUT_SANDSTONE = familyBuilder(Blocks.CUT_SANDSTONE)
/* 475 */     .slab(Blocks.CUT_SANDSTONE_SLAB)
/* 476 */     .getFamily();
/*     */   
/* 478 */   public static final BlockFamily SMOOTH_SANDSTONE = familyBuilder(Blocks.SMOOTH_SANDSTONE)
/* 479 */     .slab(Blocks.SMOOTH_SANDSTONE_SLAB)
/* 480 */     .stairs(Blocks.SMOOTH_SANDSTONE_STAIRS)
/* 481 */     .getFamily();
/*     */   
/* 483 */   public static final BlockFamily RED_SANDSTONE = familyBuilder(Blocks.RED_SANDSTONE)
/* 484 */     .wall(Blocks.RED_SANDSTONE_WALL)
/* 485 */     .stairs(Blocks.RED_SANDSTONE_STAIRS)
/* 486 */     .slab(Blocks.RED_SANDSTONE_SLAB)
/* 487 */     .chiseled(Blocks.CHISELED_RED_SANDSTONE)
/* 488 */     .cut(Blocks.CUT_RED_SANDSTONE)
/* 489 */     .dontGenerateRecipe()
/* 490 */     .getFamily();
/*     */   
/* 492 */   public static final BlockFamily CUT_RED_SANDSTONE = familyBuilder(Blocks.CUT_RED_SANDSTONE)
/* 493 */     .slab(Blocks.CUT_RED_SANDSTONE_SLAB)
/* 494 */     .getFamily();
/*     */   
/* 496 */   public static final BlockFamily SMOOTH_RED_SANDSTONE = familyBuilder(Blocks.SMOOTH_RED_SANDSTONE)
/* 497 */     .slab(Blocks.SMOOTH_RED_SANDSTONE_SLAB)
/* 498 */     .stairs(Blocks.SMOOTH_RED_SANDSTONE_STAIRS)
/* 499 */     .getFamily();
/*     */   
/* 501 */   public static final BlockFamily STONE = familyBuilder(Blocks.STONE)
/* 502 */     .slab(Blocks.STONE_SLAB)
/* 503 */     .pressurePlate(Blocks.STONE_PRESSURE_PLATE)
/* 504 */     .button(Blocks.STONE_BUTTON)
/* 505 */     .stairs(Blocks.STONE_STAIRS)
/* 506 */     .getFamily();
/*     */   
/* 508 */   public static final BlockFamily STONE_BRICK = familyBuilder(Blocks.STONE_BRICKS)
/* 509 */     .wall(Blocks.STONE_BRICK_WALL)
/* 510 */     .stairs(Blocks.STONE_BRICK_STAIRS)
/* 511 */     .slab(Blocks.STONE_BRICK_SLAB)
/* 512 */     .chiseled(Blocks.CHISELED_STONE_BRICKS)
/* 513 */     .cracked(Blocks.CRACKED_STONE_BRICKS)
/* 514 */     .dontGenerateRecipe()
/* 515 */     .getFamily();
/*     */   
/* 517 */   public static final BlockFamily DEEPSLATE = familyBuilder(Blocks.DEEPSLATE)
/* 518 */     .getFamily();
/*     */   
/* 520 */   public static final BlockFamily COBBLED_DEEPSLATE = familyBuilder(Blocks.COBBLED_DEEPSLATE)
/* 521 */     .slab(Blocks.COBBLED_DEEPSLATE_SLAB)
/* 522 */     .stairs(Blocks.COBBLED_DEEPSLATE_STAIRS)
/* 523 */     .wall(Blocks.COBBLED_DEEPSLATE_WALL)
/* 524 */     .chiseled(Blocks.CHISELED_DEEPSLATE)
/* 525 */     .polished(Blocks.POLISHED_DEEPSLATE)
/* 526 */     .getFamily();
/*     */   
/* 528 */   public static final BlockFamily POLISHED_DEEPSLATE = familyBuilder(Blocks.POLISHED_DEEPSLATE)
/* 529 */     .slab(Blocks.POLISHED_DEEPSLATE_SLAB)
/* 530 */     .stairs(Blocks.POLISHED_DEEPSLATE_STAIRS)
/* 531 */     .wall(Blocks.POLISHED_DEEPSLATE_WALL)
/* 532 */     .getFamily();
/*     */   
/* 534 */   public static final BlockFamily DEEPSLATE_BRICKS = familyBuilder(Blocks.DEEPSLATE_BRICKS)
/* 535 */     .slab(Blocks.DEEPSLATE_BRICK_SLAB)
/* 536 */     .stairs(Blocks.DEEPSLATE_BRICK_STAIRS)
/* 537 */     .wall(Blocks.DEEPSLATE_BRICK_WALL)
/* 538 */     .cracked(Blocks.CRACKED_DEEPSLATE_BRICKS)
/* 539 */     .getFamily();
/*     */   
/* 541 */   public static final BlockFamily DEEPSLATE_TILES = familyBuilder(Blocks.DEEPSLATE_TILES)
/* 542 */     .slab(Blocks.DEEPSLATE_TILE_SLAB)
/* 543 */     .stairs(Blocks.DEEPSLATE_TILE_STAIRS)
/* 544 */     .wall(Blocks.DEEPSLATE_TILE_WALL)
/* 545 */     .cracked(Blocks.CRACKED_DEEPSLATE_TILES)
/* 546 */     .getFamily();
/*     */   
/*     */   private static BlockFamily.Builder familyBuilder(Block paramBlock) {
/* 549 */     BlockFamily.Builder builder = new BlockFamily.Builder(paramBlock);
/* 550 */     BlockFamily blockFamily = MAP.put(paramBlock, builder.getFamily());
/* 551 */     if (blockFamily != null) {
/* 552 */       throw new IllegalStateException("Duplicate family definition for " + String.valueOf(BuiltInRegistries.BLOCK.getKey(paramBlock)));
/*     */     }
/* 554 */     return builder;
/*     */   }
/*     */   
/*     */   public static Stream<BlockFamily> getAllFamilies() {
/* 558 */     return MAP.values().stream();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\BlockFamilies.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */