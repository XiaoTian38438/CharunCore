/*    */ package net.minecraft.world.level.levelgen;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.OptionalLong;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ 
/*    */ public class WorldOptions {
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)Codec.LONG.fieldOf("seed").stable().forGetter(WorldOptions::seed), (App)Codec.BOOL.fieldOf("generate_features").orElse(Boolean.valueOf(true)).stable().forGetter(WorldOptions::generateStructures), (App)Codec.BOOL.fieldOf("bonus_chest").orElse(Boolean.valueOf(false)).stable().forGetter(WorldOptions::generateBonusChest), (App)Codec.STRING.lenientOptionalFieldOf("legacy_custom_options").stable().forGetter(())).apply((Applicative)paramInstance, paramInstance.stable(WorldOptions::new)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static final MapCodec<WorldOptions> CODEC;
/*    */ 
/*    */   
/* 21 */   public static final WorldOptions DEMO_OPTIONS = new WorldOptions("North Carolina".hashCode(), true, true);
/*    */   
/*    */   private final long seed;
/*    */   
/*    */   private final boolean generateStructures;
/*    */   private final boolean generateBonusChest;
/*    */   private final Optional<String> legacyCustomOptions;
/*    */   
/*    */   public WorldOptions(long paramLong, boolean paramBoolean1, boolean paramBoolean2) {
/* 30 */     this(paramLong, paramBoolean1, paramBoolean2, Optional.empty());
/*    */   }
/*    */   
/*    */   public static WorldOptions defaultWithRandomSeed() {
/* 34 */     return new WorldOptions(randomSeed(), true, false);
/*    */   }
/*    */   
/*    */   public static WorldOptions testWorldWithRandomSeed() {
/* 38 */     return new WorldOptions(randomSeed(), false, false);
/*    */   }
/*    */   
/*    */   private WorldOptions(long paramLong, boolean paramBoolean1, boolean paramBoolean2, Optional<String> paramOptional) {
/* 42 */     this.seed = paramLong;
/* 43 */     this.generateStructures = paramBoolean1;
/* 44 */     this.generateBonusChest = paramBoolean2;
/* 45 */     this.legacyCustomOptions = paramOptional;
/*    */   }
/*    */   
/*    */   public long seed() {
/* 49 */     return this.seed;
/*    */   }
/*    */   
/*    */   public boolean generateStructures() {
/* 53 */     return this.generateStructures;
/*    */   }
/*    */   
/*    */   public boolean generateBonusChest() {
/* 57 */     return this.generateBonusChest;
/*    */   }
/*    */   
/*    */   public boolean isOldCustomizedWorld() {
/* 61 */     return this.legacyCustomOptions.isPresent();
/*    */   }
/*    */   
/*    */   public WorldOptions withBonusChest(boolean paramBoolean) {
/* 65 */     return new WorldOptions(this.seed, this.generateStructures, paramBoolean, this.legacyCustomOptions);
/*    */   }
/*    */   
/*    */   public WorldOptions withStructures(boolean paramBoolean) {
/* 69 */     return new WorldOptions(this.seed, paramBoolean, this.generateBonusChest, this.legacyCustomOptions);
/*    */   }
/*    */   
/*    */   public WorldOptions withSeed(OptionalLong paramOptionalLong) {
/* 73 */     return new WorldOptions(paramOptionalLong.orElse(randomSeed()), this.generateStructures, this.generateBonusChest, this.legacyCustomOptions);
/*    */   }
/*    */   
/*    */   public static OptionalLong parseSeed(String paramString) {
/* 77 */     paramString = paramString.trim();
/*    */     
/* 79 */     if (StringUtils.isEmpty(paramString)) {
/* 80 */       return OptionalLong.empty();
/*    */     }
/*    */     
/*    */     try {
/* 84 */       return OptionalLong.of(Long.parseLong(paramString));
/* 85 */     } catch (NumberFormatException numberFormatException) {
/*    */       
/* 87 */       return OptionalLong.of(paramString.hashCode());
/*    */     } 
/*    */   }
/*    */   
/*    */   public static long randomSeed() {
/* 92 */     return RandomSource.create().nextLong();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\WorldOptions.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */