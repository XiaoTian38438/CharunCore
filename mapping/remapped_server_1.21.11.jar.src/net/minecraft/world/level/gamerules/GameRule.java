/*     */ package net.minecraft.world.level.gamerules;
/*     */ 
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.util.Objects;
/*     */ import java.util.function.ToIntFunction;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.flag.FeatureElement;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class GameRule<T>
/*     */   implements FeatureElement
/*     */ {
/*     */   private final GameRuleCategory category;
/*     */   private final GameRuleType gameRuleType;
/*     */   private final ArgumentType<T> argument;
/*     */   private final GameRules.VisitorCaller<T> visitorCaller;
/*     */   private final Codec<T> valueCodec;
/*     */   private final ToIntFunction<T> commandResultFunction;
/*     */   private final T defaultValue;
/*     */   private final FeatureFlagSet requiredFeatures;
/*     */   
/*     */   public GameRule(GameRuleCategory paramGameRuleCategory, GameRuleType paramGameRuleType, ArgumentType<T> paramArgumentType, GameRules.VisitorCaller<T> paramVisitorCaller, Codec<T> paramCodec, ToIntFunction<T> paramToIntFunction, T paramT, FeatureFlagSet paramFeatureFlagSet) {
/*  37 */     this.category = paramGameRuleCategory;
/*  38 */     this.gameRuleType = paramGameRuleType;
/*  39 */     this.argument = paramArgumentType;
/*  40 */     this.visitorCaller = paramVisitorCaller;
/*  41 */     this.valueCodec = paramCodec;
/*  42 */     this.commandResultFunction = paramToIntFunction;
/*  43 */     this.defaultValue = paramT;
/*  44 */     this.requiredFeatures = paramFeatureFlagSet;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  49 */     return id();
/*     */   }
/*     */   
/*     */   public String id() {
/*  53 */     return getIdentifier().toShortString();
/*     */   }
/*     */   
/*     */   public Identifier getIdentifier() {
/*  57 */     return Objects.<Identifier>requireNonNull(BuiltInRegistries.GAME_RULE.getKey(this));
/*     */   }
/*     */   
/*     */   public String getDescriptionId() {
/*  61 */     return Util.makeDescriptionId("gamerule", getIdentifier());
/*     */   }
/*     */   
/*     */   public String serialize(T paramT) {
/*  65 */     return paramT.toString();
/*     */   }
/*     */   
/*     */   public DataResult<T> deserialize(String paramString) {
/*     */     try {
/*  70 */       StringReader stringReader = new StringReader(paramString);
/*  71 */       Object object = this.argument.parse(stringReader);
/*  72 */       if (stringReader.canRead()) {
/*  73 */         return DataResult.error(() -> "Failed to deserialize; trailing characters", object);
/*     */       }
/*  75 */       return DataResult.success(object);
/*  76 */     } catch (CommandSyntaxException commandSyntaxException) {
/*  77 */       return DataResult.error(() -> "Failed to deserialize");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<T> valueClass() {
/*  83 */     return (Class)this.defaultValue.getClass();
/*     */   }
/*     */   
/*     */   public void callVisitor(GameRuleTypeVisitor paramGameRuleTypeVisitor) {
/*  87 */     this.visitorCaller.call(paramGameRuleTypeVisitor, this);
/*     */   }
/*     */   
/*     */   public int getCommandResult(T paramT) {
/*  91 */     return this.commandResultFunction.applyAsInt(paramT);
/*     */   }
/*     */   
/*     */   public GameRuleCategory category() {
/*  95 */     return this.category;
/*     */   }
/*     */   
/*     */   public GameRuleType gameRuleType() {
/*  99 */     return this.gameRuleType;
/*     */   }
/*     */   
/*     */   public ArgumentType<T> argument() {
/* 103 */     return this.argument;
/*     */   }
/*     */   
/*     */   public Codec<T> valueCodec() {
/* 107 */     return this.valueCodec;
/*     */   }
/*     */   
/*     */   public T defaultValue() {
/* 111 */     return this.defaultValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public FeatureFlagSet requiredFeatures() {
/* 116 */     return this.requiredFeatures;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\gamerules\GameRule.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */