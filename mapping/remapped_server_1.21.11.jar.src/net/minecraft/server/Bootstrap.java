/*     */ package net.minecraft.server;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.PrintStream;
/*     */ import java.time.Duration;
/*     */ import java.time.Instant;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.SuppressForbidden;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.selector.options.EntitySelectorOptions;
/*     */ import net.minecraft.core.cauldron.CauldronInteraction;
/*     */ import net.minecraft.core.dispenser.DispenseItemBehavior;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.locale.Language;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.effect.MobEffect;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*     */ import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ import net.minecraft.world.item.CreativeModeTabs;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.level.block.ComposterBlock;
/*     */ import net.minecraft.world.level.block.FireBlock;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.gamerules.GameRule;
/*     */ import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ @SuppressForbidden(a = "System.out setup")
/*     */ public class Bootstrap {
/*  38 */   public static final PrintStream STDOUT = System.out;
/*     */   
/*     */   private static volatile boolean isBootstrapped;
/*  41 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  43 */   public static final AtomicLong bootstrapDuration = new AtomicLong(-1L);
/*     */   
/*     */   public static void bootStrap() {
/*  46 */     if (isBootstrapped) {
/*     */       return;
/*     */     }
/*  49 */     isBootstrapped = true;
/*     */     
/*  51 */     Instant instant = Instant.now();
/*     */     
/*  53 */     if (BuiltInRegistries.REGISTRY.keySet().isEmpty()) {
/*  54 */       throw new IllegalStateException("Unable to load registries");
/*     */     }
/*     */     
/*  57 */     FireBlock.bootStrap();
/*  58 */     ComposterBlock.bootStrap();
/*     */     
/*  60 */     if (EntityType.getKey(EntityType.PLAYER) == null) {
/*  61 */       throw new IllegalStateException("Failed loading EntityTypes");
/*     */     }
/*     */     
/*  64 */     EntitySelectorOptions.bootStrap();
/*     */     
/*  66 */     DispenseItemBehavior.bootStrap();
/*     */     
/*  68 */     CauldronInteraction.bootStrap();
/*     */     
/*  70 */     BuiltInRegistries.bootStrap();
/*     */     
/*  72 */     CreativeModeTabs.validate();
/*     */     
/*  74 */     wrapStreams();
/*     */     
/*  76 */     bootstrapDuration.set(Duration.between(instant, Instant.now()).toMillis());
/*     */   }
/*     */   
/*     */   private static <T> void checkTranslations(Iterable<T> paramIterable, Function<T, String> paramFunction, Set<String> paramSet) {
/*  80 */     Language language = Language.getInstance();
/*  81 */     paramIterable.forEach(paramObject -> {
/*     */           String str = paramFunction.apply(paramObject);
/*     */           if (!paramLanguage.has(str)) {
/*     */             paramSet.add(str);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private static void checkGameruleTranslations(final Set<String> missing) {
/*  90 */     final Language language = Language.getInstance();
/*  91 */     GameRules gameRules = new GameRules(FeatureFlags.REGISTRY.allFlags());
/*  92 */     gameRules.visitGameRuleTypes(new GameRuleTypeVisitor()
/*     */         {
/*     */           public <T> void visit(GameRule<T> param1GameRule) {
/*  95 */             if (!language.has(param1GameRule.getDescriptionId())) {
/*  96 */               missing.add(param1GameRule.id());
/*     */             }
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public static Set<String> getMissingTranslations() {
/* 103 */     TreeSet<String> treeSet = new TreeSet();
/* 104 */     checkTranslations((Iterable<?>)BuiltInRegistries.ATTRIBUTE, Attribute::getDescriptionId, treeSet);
/* 105 */     checkTranslations((Iterable<?>)BuiltInRegistries.ENTITY_TYPE, EntityType::getDescriptionId, treeSet);
/* 106 */     checkTranslations((Iterable<?>)BuiltInRegistries.MOB_EFFECT, MobEffect::getDescriptionId, treeSet);
/* 107 */     checkTranslations((Iterable<?>)BuiltInRegistries.ITEM, Item::getDescriptionId, treeSet);
/* 108 */     checkTranslations((Iterable<?>)BuiltInRegistries.BLOCK, BlockBehaviour::getDescriptionId, treeSet);
/* 109 */     checkTranslations((Iterable<?>)BuiltInRegistries.CUSTOM_STAT, paramIdentifier -> "stat." + paramIdentifier.toString().replace(':', '.'), treeSet);
/*     */     
/* 111 */     checkGameruleTranslations(treeSet);
/* 112 */     return treeSet;
/*     */   }
/*     */   
/*     */   public static void checkBootstrapCalled(Supplier<String> paramSupplier) {
/* 116 */     if (!isBootstrapped) {
/* 117 */       throw createBootstrapException(paramSupplier);
/*     */     }
/*     */   }
/*     */   
/*     */   private static RuntimeException createBootstrapException(Supplier<String> paramSupplier) {
/*     */     try {
/* 123 */       String str = paramSupplier.get();
/* 124 */       return new IllegalArgumentException("Not bootstrapped (called from " + str + ")");
/* 125 */     } catch (Exception exception) {
/* 126 */       IllegalArgumentException illegalArgumentException = new IllegalArgumentException("Not bootstrapped (failed to resolve location)");
/* 127 */       illegalArgumentException.addSuppressed(exception);
/* 128 */       return illegalArgumentException;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void validate() {
/* 133 */     checkBootstrapCalled(() -> "validate");
/*     */     
/* 135 */     if (SharedConstants.IS_RUNNING_IN_IDE) {
/* 136 */       getMissingTranslations().forEach(paramString -> LOGGER.error("Missing translations: {}", paramString));
/* 137 */       Commands.validate();
/*     */     } 
/*     */     
/* 140 */     DefaultAttributes.validate();
/*     */   }
/*     */   
/*     */   private static void wrapStreams() {
/* 144 */     if (LOGGER.isDebugEnabled()) {
/* 145 */       System.setErr(new DebugLoggedPrintStream("STDERR", System.err));
/* 146 */       System.setOut(new DebugLoggedPrintStream("STDOUT", STDOUT));
/*     */     } else {
/* 148 */       System.setErr(new LoggedPrintStream("STDERR", System.err));
/* 149 */       System.setOut(new LoggedPrintStream("STDOUT", STDOUT));
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void realStdoutPrintln(String paramString) {
/* 154 */     STDOUT.println(paramString);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\Bootstrap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */