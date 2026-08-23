/*     */ package net.minecraft.commands.arguments.selector;
/*     */ 
/*     */ import com.google.common.primitives.Doubles;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.advancements.criterion.MinMaxBounds;
/*     */ import net.minecraft.commands.arguments.selector.options.EntitySelectorOptions;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.permissions.PermissionSetSupplier;
/*     */ import net.minecraft.server.permissions.Permissions;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.ToFloatFunction;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntitySelectorParser
/*     */ {
/*     */   public static final char SYNTAX_SELECTOR_START = '@';
/*     */   private static final char SYNTAX_OPTIONS_START = '[';
/*     */   private static final char SYNTAX_OPTIONS_END = ']';
/*     */   public static final char SYNTAX_OPTIONS_KEY_VALUE_SEPARATOR = '=';
/*     */   private static final char SYNTAX_OPTIONS_SEPARATOR = ',';
/*     */   public static final char SYNTAX_NOT = '!';
/*     */   public static final char SYNTAX_TAG = '#';
/*     */   private static final char SELECTOR_NEAREST_PLAYER = 'p';
/*     */   private static final char SELECTOR_ALL_PLAYERS = 'a';
/*     */   private static final char SELECTOR_RANDOM_PLAYERS = 'r';
/*     */   private static final char SELECTOR_CURRENT_ENTITY = 's';
/*     */   private static final char SELECTOR_ALL_ENTITIES = 'e';
/*     */   private static final char SELECTOR_NEAREST_ENTITY = 'n';
/*  52 */   public static final SimpleCommandExceptionType ERROR_INVALID_NAME_OR_UUID = new SimpleCommandExceptionType((Message)Component.translatable("argument.entity.invalid")); public static final DynamicCommandExceptionType ERROR_UNKNOWN_SELECTOR_TYPE; static {
/*  53 */     ERROR_UNKNOWN_SELECTOR_TYPE = new DynamicCommandExceptionType(paramObject -> Component.translatableEscape("argument.entity.selector.unknown", new Object[] { paramObject }));
/*  54 */   } public static final SimpleCommandExceptionType ERROR_SELECTORS_NOT_ALLOWED = new SimpleCommandExceptionType((Message)Component.translatable("argument.entity.selector.not_allowed"));
/*  55 */   public static final SimpleCommandExceptionType ERROR_MISSING_SELECTOR_TYPE = new SimpleCommandExceptionType((Message)Component.translatable("argument.entity.selector.missing")); public static final DynamicCommandExceptionType ERROR_EXPECTED_OPTION_VALUE; public static final BiConsumer<Vec3, List<? extends Entity>> ORDER_NEAREST; public static final BiConsumer<Vec3, List<? extends Entity>> ORDER_FURTHEST;
/*  56 */   public static final SimpleCommandExceptionType ERROR_EXPECTED_END_OF_OPTIONS = new SimpleCommandExceptionType((Message)Component.translatable("argument.entity.options.unterminated")); public static final BiConsumer<Vec3, List<? extends Entity>> ORDER_RANDOM; public static final BiFunction<SuggestionsBuilder, Consumer<SuggestionsBuilder>, CompletableFuture<Suggestions>> SUGGEST_NOTHING; static {
/*  57 */     ERROR_EXPECTED_OPTION_VALUE = new DynamicCommandExceptionType(paramObject -> Component.translatableEscape("argument.entity.options.valueless", new Object[] { paramObject }));
/*     */     
/*  59 */     ORDER_NEAREST = ((paramVec3, paramList) -> paramList.sort(()));
/*  60 */     ORDER_FURTHEST = ((paramVec3, paramList) -> paramList.sort(()));
/*  61 */     ORDER_RANDOM = ((paramVec3, paramList) -> Collections.shuffle(paramList));
/*     */     
/*  63 */     SUGGEST_NOTHING = ((paramSuggestionsBuilder, paramConsumer) -> paramSuggestionsBuilder.buildFuture());
/*     */   }
/*     */   private final StringReader reader;
/*     */   private final boolean allowSelectors;
/*     */   private int maxResults;
/*     */   private boolean includesEntities;
/*     */   private boolean worldLimited;
/*     */   private MinMaxBounds.Doubles distance;
/*     */   private MinMaxBounds.Ints level;
/*     */   private Double x;
/*     */   private Double y;
/*     */   private Double z;
/*     */   private Double deltaX;
/*     */   private Double deltaY;
/*     */   private Double deltaZ;
/*     */   private MinMaxBounds.FloatDegrees rotX;
/*     */   private MinMaxBounds.FloatDegrees rotY;
/*  80 */   private final List<Predicate<Entity>> predicates = new ArrayList<>();
/*  81 */   private BiConsumer<Vec3, List<? extends Entity>> order = EntitySelector.ORDER_ARBITRARY;
/*     */   private boolean currentEntity;
/*     */   private String playerName;
/*     */   private int startPosition;
/*     */   private UUID entityUUID;
/*  86 */   private BiFunction<SuggestionsBuilder, Consumer<SuggestionsBuilder>, CompletableFuture<Suggestions>> suggestions = SUGGEST_NOTHING;
/*     */   private boolean hasNameEquals;
/*     */   private boolean hasNameNotEquals;
/*     */   private boolean isLimited;
/*     */   private boolean isSorted;
/*     */   private boolean hasGamemodeEquals;
/*     */   private boolean hasGamemodeNotEquals;
/*     */   private boolean hasTeamEquals;
/*     */   private boolean hasTeamNotEquals;
/*     */   private EntityType<?> type;
/*     */   private boolean typeInverse;
/*     */   private boolean hasScores;
/*     */   private boolean hasAdvancements;
/*     */   private boolean usesSelectors;
/*     */   
/*     */   public EntitySelectorParser(StringReader paramStringReader, boolean paramBoolean) {
/* 102 */     this.reader = paramStringReader;
/* 103 */     this.allowSelectors = paramBoolean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <S> boolean allowSelectors(S paramS) {
/* 111 */     if (paramS instanceof PermissionSetSupplier) { PermissionSetSupplier permissionSetSupplier = (PermissionSetSupplier)paramS; if (permissionSetSupplier.permissions().hasPermission(Permissions.COMMANDS_ENTITY_SELECTORS)); }  return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static boolean allowSelectors(PermissionSetSupplier paramPermissionSetSupplier) {
/* 119 */     return paramPermissionSetSupplier.permissions().hasPermission(Permissions.COMMANDS_ENTITY_SELECTORS);
/*     */   }
/*     */   public EntitySelector getSelector() {
/*     */     AABB aABB;
/*     */     Function<Vec3, Vec3> function;
/* 124 */     if (this.deltaX != null || this.deltaY != null || this.deltaZ != null) {
/* 125 */       aABB = createAabb((this.deltaX == null) ? 0.0D : this.deltaX.doubleValue(), (this.deltaY == null) ? 0.0D : this.deltaY.doubleValue(), (this.deltaZ == null) ? 0.0D : this.deltaZ.doubleValue());
/* 126 */     } else if (this.distance != null && this.distance.max().isPresent()) {
/* 127 */       double d = ((Double)this.distance.max().get()).doubleValue();
/* 128 */       aABB = new AABB(-d, -d, -d, d + 1.0D, d + 1.0D, d + 1.0D);
/*     */     } else {
/* 130 */       aABB = null;
/*     */     } 
/*     */     
/* 133 */     if (this.x == null && this.y == null && this.z == null) {
/* 134 */       function = (paramVec3 -> paramVec3);
/*     */     } else {
/* 136 */       function = (paramVec3 -> new Vec3((this.x == null) ? paramVec3.x : this.x.doubleValue(), (this.y == null) ? paramVec3.y : this.y.doubleValue(), (this.z == null) ? paramVec3.z : this.z.doubleValue()));
/*     */     } 
/* 138 */     return new EntitySelector(this.maxResults, this.includesEntities, this.worldLimited, List.copyOf(this.predicates), this.distance, function, aABB, this.order, this.currentEntity, this.playerName, this.entityUUID, this.type, this.usesSelectors);
/*     */   }
/*     */   
/*     */   private AABB createAabb(double paramDouble1, double paramDouble2, double paramDouble3) {
/* 142 */     boolean bool1 = (paramDouble1 < 0.0D) ? true : false;
/* 143 */     boolean bool2 = (paramDouble2 < 0.0D) ? true : false;
/* 144 */     boolean bool3 = (paramDouble3 < 0.0D) ? true : false;
/* 145 */     double d1 = bool1 ? paramDouble1 : 0.0D;
/* 146 */     double d2 = bool2 ? paramDouble2 : 0.0D;
/* 147 */     double d3 = bool3 ? paramDouble3 : 0.0D;
/* 148 */     double d4 = (bool1 ? 0.0D : paramDouble1) + 1.0D;
/* 149 */     double d5 = (bool2 ? 0.0D : paramDouble2) + 1.0D;
/* 150 */     double d6 = (bool3 ? 0.0D : paramDouble3) + 1.0D;
/* 151 */     return new AABB(d1, d2, d3, d4, d5, d6);
/*     */   }
/*     */   
/*     */   private void finalizePredicates() {
/* 155 */     if (this.rotX != null) {
/* 156 */       this.predicates.add(createRotationPredicate(this.rotX, Entity::getXRot));
/*     */     }
/* 158 */     if (this.rotY != null) {
/* 159 */       this.predicates.add(createRotationPredicate(this.rotY, Entity::getYRot));
/*     */     }
/* 161 */     if (this.level != null)
/* 162 */       this.predicates.add(paramEntity -> {
/*     */             if (paramEntity instanceof ServerPlayer) {
/*     */               ServerPlayer serverPlayer = (ServerPlayer)paramEntity;
/*     */               if (this.level.matches(serverPlayer.experienceLevel));
/*     */             } 
/*     */             return false;
/* 168 */           });  } private Predicate<Entity> createRotationPredicate(MinMaxBounds.FloatDegrees paramFloatDegrees, ToFloatFunction<Entity> paramToFloatFunction) { float f1 = Mth.wrapDegrees(((Float)paramFloatDegrees.min().orElse(Float.valueOf(0.0F))).floatValue());
/* 169 */     float f2 = Mth.wrapDegrees(((Float)paramFloatDegrees.max().orElse(Float.valueOf(359.0F))).floatValue());
/* 170 */     return paramEntity -> {
/*     */         float f = Mth.wrapDegrees(paramToFloatFunction.applyAsFloat(paramEntity));
/*     */         
/* 173 */         return (paramFloat1 > paramFloat2) ? ((f >= paramFloat1 || f <= paramFloat2)) : (
/*     */           
/* 175 */           (f >= paramFloat1 && f <= paramFloat2));
/*     */       }; }
/*     */   
/*     */   protected void parseSelector() throws CommandSyntaxException {
/*     */     boolean bool;
/* 180 */     this.usesSelectors = true;
/* 181 */     this.suggestions = this::suggestSelector;
/* 182 */     if (!this.reader.canRead()) {
/* 183 */       throw ERROR_MISSING_SELECTOR_TYPE.createWithContext(this.reader);
/*     */     }
/* 185 */     int i = this.reader.getCursor();
/* 186 */     char c = this.reader.read();
/*     */ 
/*     */     
/* 189 */     switch (c) {
/*     */       case 'p':
/* 191 */         this.maxResults = 1;
/* 192 */         this.includesEntities = false;
/* 193 */         this.order = ORDER_NEAREST;
/* 194 */         limitToType(EntityType.PLAYER);
/* 195 */         bool = false;
/*     */         break;
/*     */       case 'a':
/* 198 */         this.maxResults = Integer.MAX_VALUE;
/* 199 */         this.includesEntities = false;
/* 200 */         this.order = EntitySelector.ORDER_ARBITRARY;
/* 201 */         limitToType(EntityType.PLAYER);
/* 202 */         bool = false;
/*     */         break;
/*     */       case 'r':
/* 205 */         this.maxResults = 1;
/* 206 */         this.includesEntities = false;
/* 207 */         this.order = ORDER_RANDOM;
/* 208 */         limitToType(EntityType.PLAYER);
/* 209 */         bool = false;
/*     */         break;
/*     */       case 's':
/* 212 */         this.maxResults = 1;
/* 213 */         this.includesEntities = true;
/* 214 */         this.currentEntity = true;
/*     */         
/* 216 */         bool = false;
/*     */         break;
/*     */       case 'e':
/* 219 */         this.maxResults = Integer.MAX_VALUE;
/* 220 */         this.includesEntities = true;
/* 221 */         this.order = EntitySelector.ORDER_ARBITRARY;
/* 222 */         bool = true;
/*     */         break;
/*     */       case 'n':
/* 225 */         this.maxResults = 1;
/* 226 */         this.includesEntities = true;
/* 227 */         this.order = ORDER_NEAREST;
/* 228 */         bool = true;
/*     */         break;
/*     */       default:
/* 231 */         this.reader.setCursor(i);
/* 232 */         throw ERROR_UNKNOWN_SELECTOR_TYPE.createWithContext(this.reader, "@" + String.valueOf(c));
/*     */     } 
/*     */ 
/*     */     
/* 236 */     if (bool) {
/* 237 */       this.predicates.add(Entity::isAlive);
/*     */     }
/* 239 */     this.suggestions = this::suggestOpenOptions;
/* 240 */     if (this.reader.canRead() && this.reader.peek() == '[') {
/* 241 */       this.reader.skip();
/* 242 */       this.suggestions = this::suggestOptionsKeyOrClose;
/* 243 */       parseOptions();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void parseNameOrUUID() throws CommandSyntaxException {
/* 248 */     if (this.reader.canRead()) {
/* 249 */       this.suggestions = this::suggestName;
/*     */     }
/* 251 */     int i = this.reader.getCursor();
/* 252 */     String str = this.reader.readString();
/*     */     
/*     */     try {
/* 255 */       this.entityUUID = UUID.fromString(str);
/* 256 */       this.includesEntities = true;
/* 257 */     } catch (IllegalArgumentException illegalArgumentException) {
/* 258 */       if (str.isEmpty() || str.length() > 16) {
/* 259 */         this.reader.setCursor(i);
/* 260 */         throw ERROR_INVALID_NAME_OR_UUID.createWithContext(this.reader);
/*     */       } 
/* 262 */       this.includesEntities = false;
/* 263 */       this.playerName = str;
/*     */     } 
/*     */     
/* 266 */     this.maxResults = 1;
/*     */   }
/*     */   
/*     */   protected void parseOptions() throws CommandSyntaxException {
/* 270 */     this.suggestions = this::suggestOptionsKey;
/* 271 */     this.reader.skipWhitespace();
/* 272 */     while (this.reader.canRead() && this.reader.peek() != ']') {
/* 273 */       this.reader.skipWhitespace();
/* 274 */       int i = this.reader.getCursor();
/* 275 */       String str = this.reader.readString();
/* 276 */       EntitySelectorOptions.Modifier modifier = EntitySelectorOptions.get(this, str, i);
/* 277 */       this.reader.skipWhitespace();
/* 278 */       if (!this.reader.canRead() || this.reader.peek() != '=') {
/* 279 */         this.reader.setCursor(i);
/* 280 */         throw ERROR_EXPECTED_OPTION_VALUE.createWithContext(this.reader, str);
/*     */       } 
/* 282 */       this.reader.skip();
/* 283 */       this.reader.skipWhitespace();
/*     */       
/* 285 */       this.suggestions = SUGGEST_NOTHING;
/* 286 */       modifier.handle(this);
/* 287 */       this.reader.skipWhitespace();
/*     */       
/* 289 */       this.suggestions = this::suggestOptionsNextOrClose;
/* 290 */       if (this.reader.canRead()) {
/* 291 */         if (this.reader.peek() == ',') {
/* 292 */           this.reader.skip();
/* 293 */           this.suggestions = this::suggestOptionsKey; continue;
/* 294 */         }  if (this.reader.peek() == ']') {
/*     */           break;
/*     */         }
/* 297 */         throw ERROR_EXPECTED_END_OF_OPTIONS.createWithContext(this.reader);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 302 */     if (this.reader.canRead()) {
/* 303 */       this.reader.skip();
/* 304 */       this.suggestions = SUGGEST_NOTHING;
/*     */     } else {
/* 306 */       throw ERROR_EXPECTED_END_OF_OPTIONS.createWithContext(this.reader);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean shouldInvertValue() {
/* 311 */     this.reader.skipWhitespace();
/* 312 */     if (this.reader.canRead() && this.reader.peek() == '!') {
/* 313 */       this.reader.skip();
/* 314 */       this.reader.skipWhitespace();
/* 315 */       return true;
/*     */     } 
/* 317 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isTag() {
/* 321 */     this.reader.skipWhitespace();
/* 322 */     if (this.reader.canRead() && this.reader.peek() == '#') {
/* 323 */       this.reader.skip();
/* 324 */       this.reader.skipWhitespace();
/* 325 */       return true;
/*     */     } 
/* 327 */     return false;
/*     */   }
/*     */   
/*     */   public StringReader getReader() {
/* 331 */     return this.reader;
/*     */   }
/*     */   
/*     */   public void addPredicate(Predicate<Entity> paramPredicate) {
/* 335 */     this.predicates.add(paramPredicate);
/*     */   }
/*     */   
/*     */   public void setWorldLimited() {
/* 339 */     this.worldLimited = true;
/*     */   }
/*     */   
/*     */   public MinMaxBounds.Doubles getDistance() {
/* 343 */     return this.distance;
/*     */   }
/*     */   
/*     */   public void setDistance(MinMaxBounds.Doubles paramDoubles) {
/* 347 */     this.distance = paramDoubles;
/*     */   }
/*     */   
/*     */   public MinMaxBounds.Ints getLevel() {
/* 351 */     return this.level;
/*     */   }
/*     */   
/*     */   public void setLevel(MinMaxBounds.Ints paramInts) {
/* 355 */     this.level = paramInts;
/*     */   }
/*     */   
/*     */   public MinMaxBounds.FloatDegrees getRotX() {
/* 359 */     return this.rotX;
/*     */   }
/*     */   
/*     */   public void setRotX(MinMaxBounds.FloatDegrees paramFloatDegrees) {
/* 363 */     this.rotX = paramFloatDegrees;
/*     */   }
/*     */   
/*     */   public MinMaxBounds.FloatDegrees getRotY() {
/* 367 */     return this.rotY;
/*     */   }
/*     */   
/*     */   public void setRotY(MinMaxBounds.FloatDegrees paramFloatDegrees) {
/* 371 */     this.rotY = paramFloatDegrees;
/*     */   }
/*     */   
/*     */   public Double getX() {
/* 375 */     return this.x;
/*     */   }
/*     */   
/*     */   public Double getY() {
/* 379 */     return this.y;
/*     */   }
/*     */   
/*     */   public Double getZ() {
/* 383 */     return this.z;
/*     */   }
/*     */   
/*     */   public void setX(double paramDouble) {
/* 387 */     this.x = Double.valueOf(paramDouble);
/*     */   }
/*     */   
/*     */   public void setY(double paramDouble) {
/* 391 */     this.y = Double.valueOf(paramDouble);
/*     */   }
/*     */   
/*     */   public void setZ(double paramDouble) {
/* 395 */     this.z = Double.valueOf(paramDouble);
/*     */   }
/*     */   
/*     */   public void setDeltaX(double paramDouble) {
/* 399 */     this.deltaX = Double.valueOf(paramDouble);
/*     */   }
/*     */   
/*     */   public void setDeltaY(double paramDouble) {
/* 403 */     this.deltaY = Double.valueOf(paramDouble);
/*     */   }
/*     */   
/*     */   public void setDeltaZ(double paramDouble) {
/* 407 */     this.deltaZ = Double.valueOf(paramDouble);
/*     */   }
/*     */   
/*     */   public Double getDeltaX() {
/* 411 */     return this.deltaX;
/*     */   }
/*     */   
/*     */   public Double getDeltaY() {
/* 415 */     return this.deltaY;
/*     */   }
/*     */   
/*     */   public Double getDeltaZ() {
/* 419 */     return this.deltaZ;
/*     */   }
/*     */   
/*     */   public void setMaxResults(int paramInt) {
/* 423 */     this.maxResults = paramInt;
/*     */   }
/*     */   
/*     */   public void setIncludesEntities(boolean paramBoolean) {
/* 427 */     this.includesEntities = paramBoolean;
/*     */   }
/*     */   
/*     */   public BiConsumer<Vec3, List<? extends Entity>> getOrder() {
/* 431 */     return this.order;
/*     */   }
/*     */   
/*     */   public void setOrder(BiConsumer<Vec3, List<? extends Entity>> paramBiConsumer) {
/* 435 */     this.order = paramBiConsumer;
/*     */   }
/*     */   
/*     */   public EntitySelector parse() throws CommandSyntaxException {
/* 439 */     this.startPosition = this.reader.getCursor();
/* 440 */     this.suggestions = this::suggestNameOrSelector;
/* 441 */     if (this.reader.canRead() && this.reader.peek() == '@') {
/* 442 */       if (!this.allowSelectors) {
/* 443 */         throw ERROR_SELECTORS_NOT_ALLOWED.createWithContext(this.reader);
/*     */       }
/* 445 */       this.reader.skip();
/* 446 */       parseSelector();
/*     */     } else {
/* 448 */       parseNameOrUUID();
/*     */     } 
/* 450 */     finalizePredicates();
/* 451 */     return getSelector();
/*     */   }
/*     */   
/*     */   private static void fillSelectorSuggestions(SuggestionsBuilder paramSuggestionsBuilder) {
/* 455 */     paramSuggestionsBuilder.suggest("@p", (Message)Component.translatable("argument.entity.selector.nearestPlayer"));
/* 456 */     paramSuggestionsBuilder.suggest("@a", (Message)Component.translatable("argument.entity.selector.allPlayers"));
/* 457 */     paramSuggestionsBuilder.suggest("@r", (Message)Component.translatable("argument.entity.selector.randomPlayer"));
/* 458 */     paramSuggestionsBuilder.suggest("@s", (Message)Component.translatable("argument.entity.selector.self"));
/* 459 */     paramSuggestionsBuilder.suggest("@e", (Message)Component.translatable("argument.entity.selector.allEntities"));
/* 460 */     paramSuggestionsBuilder.suggest("@n", (Message)Component.translatable("argument.entity.selector.nearestEntity"));
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestNameOrSelector(SuggestionsBuilder paramSuggestionsBuilder, Consumer<SuggestionsBuilder> paramConsumer) {
/* 464 */     paramConsumer.accept(paramSuggestionsBuilder);
/* 465 */     if (this.allowSelectors) {
/* 466 */       fillSelectorSuggestions(paramSuggestionsBuilder);
/*     */     }
/* 468 */     return paramSuggestionsBuilder.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestName(SuggestionsBuilder paramSuggestionsBuilder, Consumer<SuggestionsBuilder> paramConsumer) {
/* 472 */     SuggestionsBuilder suggestionsBuilder = paramSuggestionsBuilder.createOffset(this.startPosition);
/* 473 */     paramConsumer.accept(suggestionsBuilder);
/* 474 */     return paramSuggestionsBuilder.add(suggestionsBuilder).buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestSelector(SuggestionsBuilder paramSuggestionsBuilder, Consumer<SuggestionsBuilder> paramConsumer) {
/* 478 */     SuggestionsBuilder suggestionsBuilder = paramSuggestionsBuilder.createOffset(paramSuggestionsBuilder.getStart() - 1);
/* 479 */     fillSelectorSuggestions(suggestionsBuilder);
/* 480 */     paramSuggestionsBuilder.add(suggestionsBuilder);
/* 481 */     return paramSuggestionsBuilder.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestOpenOptions(SuggestionsBuilder paramSuggestionsBuilder, Consumer<SuggestionsBuilder> paramConsumer) {
/* 485 */     paramSuggestionsBuilder.suggest(String.valueOf('['));
/* 486 */     return paramSuggestionsBuilder.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestOptionsKeyOrClose(SuggestionsBuilder paramSuggestionsBuilder, Consumer<SuggestionsBuilder> paramConsumer) {
/* 490 */     paramSuggestionsBuilder.suggest(String.valueOf(']'));
/* 491 */     EntitySelectorOptions.suggestNames(this, paramSuggestionsBuilder);
/* 492 */     return paramSuggestionsBuilder.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestOptionsKey(SuggestionsBuilder paramSuggestionsBuilder, Consumer<SuggestionsBuilder> paramConsumer) {
/* 496 */     EntitySelectorOptions.suggestNames(this, paramSuggestionsBuilder);
/* 497 */     return paramSuggestionsBuilder.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestOptionsNextOrClose(SuggestionsBuilder paramSuggestionsBuilder, Consumer<SuggestionsBuilder> paramConsumer) {
/* 501 */     paramSuggestionsBuilder.suggest(String.valueOf(','));
/* 502 */     paramSuggestionsBuilder.suggest(String.valueOf(']'));
/* 503 */     return paramSuggestionsBuilder.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestEquals(SuggestionsBuilder paramSuggestionsBuilder, Consumer<SuggestionsBuilder> paramConsumer) {
/* 507 */     paramSuggestionsBuilder.suggest(String.valueOf('='));
/* 508 */     return paramSuggestionsBuilder.buildFuture();
/*     */   }
/*     */   
/*     */   public boolean isCurrentEntity() {
/* 512 */     return this.currentEntity;
/*     */   }
/*     */   
/*     */   public void setSuggestions(BiFunction<SuggestionsBuilder, Consumer<SuggestionsBuilder>, CompletableFuture<Suggestions>> paramBiFunction) {
/* 516 */     this.suggestions = paramBiFunction;
/*     */   }
/*     */   
/*     */   public CompletableFuture<Suggestions> fillSuggestions(SuggestionsBuilder paramSuggestionsBuilder, Consumer<SuggestionsBuilder> paramConsumer) {
/* 520 */     return this.suggestions.apply(paramSuggestionsBuilder.createOffset(this.reader.getCursor()), paramConsumer);
/*     */   }
/*     */   
/*     */   public boolean hasNameEquals() {
/* 524 */     return this.hasNameEquals;
/*     */   }
/*     */   
/*     */   public void setHasNameEquals(boolean paramBoolean) {
/* 528 */     this.hasNameEquals = paramBoolean;
/*     */   }
/*     */   
/*     */   public boolean hasNameNotEquals() {
/* 532 */     return this.hasNameNotEquals;
/*     */   }
/*     */   
/*     */   public void setHasNameNotEquals(boolean paramBoolean) {
/* 536 */     this.hasNameNotEquals = paramBoolean;
/*     */   }
/*     */   
/*     */   public boolean isLimited() {
/* 540 */     return this.isLimited;
/*     */   }
/*     */   
/*     */   public void setLimited(boolean paramBoolean) {
/* 544 */     this.isLimited = paramBoolean;
/*     */   }
/*     */   
/*     */   public boolean isSorted() {
/* 548 */     return this.isSorted;
/*     */   }
/*     */   
/*     */   public void setSorted(boolean paramBoolean) {
/* 552 */     this.isSorted = paramBoolean;
/*     */   }
/*     */   
/*     */   public boolean hasGamemodeEquals() {
/* 556 */     return this.hasGamemodeEquals;
/*     */   }
/*     */   
/*     */   public void setHasGamemodeEquals(boolean paramBoolean) {
/* 560 */     this.hasGamemodeEquals = paramBoolean;
/*     */   }
/*     */   
/*     */   public boolean hasGamemodeNotEquals() {
/* 564 */     return this.hasGamemodeNotEquals;
/*     */   }
/*     */   
/*     */   public void setHasGamemodeNotEquals(boolean paramBoolean) {
/* 568 */     this.hasGamemodeNotEquals = paramBoolean;
/*     */   }
/*     */   
/*     */   public boolean hasTeamEquals() {
/* 572 */     return this.hasTeamEquals;
/*     */   }
/*     */   
/*     */   public void setHasTeamEquals(boolean paramBoolean) {
/* 576 */     this.hasTeamEquals = paramBoolean;
/*     */   }
/*     */   
/*     */   public boolean hasTeamNotEquals() {
/* 580 */     return this.hasTeamNotEquals;
/*     */   }
/*     */   
/*     */   public void setHasTeamNotEquals(boolean paramBoolean) {
/* 584 */     this.hasTeamNotEquals = paramBoolean;
/*     */   }
/*     */   
/*     */   public void limitToType(EntityType<?> paramEntityType) {
/* 588 */     this.type = paramEntityType;
/*     */   }
/*     */   
/*     */   public void setTypeLimitedInversely() {
/* 592 */     this.typeInverse = true;
/*     */   }
/*     */   
/*     */   public boolean isTypeLimited() {
/* 596 */     return (this.type != null);
/*     */   }
/*     */   
/*     */   public boolean isTypeLimitedInversely() {
/* 600 */     return this.typeInverse;
/*     */   }
/*     */   
/*     */   public boolean hasScores() {
/* 604 */     return this.hasScores;
/*     */   }
/*     */   
/*     */   public void setHasScores(boolean paramBoolean) {
/* 608 */     this.hasScores = paramBoolean;
/*     */   }
/*     */   
/*     */   public boolean hasAdvancements() {
/* 612 */     return this.hasAdvancements;
/*     */   }
/*     */   
/*     */   public void setHasAdvancements(boolean paramBoolean) {
/* 616 */     this.hasAdvancements = paramBoolean;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\arguments\selector\EntitySelectorParser.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */