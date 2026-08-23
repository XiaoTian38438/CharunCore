/*     */ package net.minecraft.world.level.storage.loot;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Builder
/*     */ {
/*     */   private final LootParams params;
/*     */   private RandomSource random;
/*     */   
/*     */   public Builder(LootParams paramLootParams) {
/*  85 */     this.params = paramLootParams;
/*     */   }
/*     */   
/*     */   public Builder withOptionalRandomSeed(long paramLong) {
/*  89 */     if (paramLong != 0L) {
/*  90 */       this.random = RandomSource.create(paramLong);
/*     */     }
/*  92 */     return this;
/*     */   }
/*     */   
/*     */   public Builder withOptionalRandomSource(RandomSource paramRandomSource) {
/*  96 */     this.random = paramRandomSource;
/*  97 */     return this;
/*     */   }
/*     */   
/*     */   public ServerLevel getLevel() {
/* 101 */     return this.params.getLevel();
/*     */   }
/*     */   
/*     */   public LootContext create(Optional<Identifier> paramOptional) {
/* 105 */     ServerLevel serverLevel = getLevel();
/* 106 */     MinecraftServer minecraftServer = serverLevel.getServer();
/*     */ 
/*     */     
/* 109 */     Objects.requireNonNull(serverLevel); RandomSource randomSource = Optional.<RandomSource>ofNullable(this.random).or(() -> { Objects.requireNonNull(paramServerLevel); return paramOptional.map(paramServerLevel::getRandomSequence); }).orElseGet(serverLevel::getRandom);
/* 110 */     return new LootContext(this.params, randomSource, (HolderGetter.Provider)minecraftServer.reloadableRegistries().lookup());
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\LootContext$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */