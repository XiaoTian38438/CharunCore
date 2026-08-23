/*     */ package net.minecraft.world.item.component;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.players.ProfileResolver;
/*     */ import net.minecraft.world.entity.player.PlayerSkin;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.TooltipFlag;
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
/*     */ public final class Static
/*     */   extends ResolvableProfile
/*     */ {
/* 150 */   public static final Static EMPTY = new Static(Either.right(ResolvableProfile.Partial.EMPTY), PlayerSkin.Patch.EMPTY);
/*     */   
/*     */   private final Either<GameProfile, ResolvableProfile.Partial> contents;
/*     */   
/*     */   Static(Either<GameProfile, ResolvableProfile.Partial> paramEither, PlayerSkin.Patch paramPatch) {
/* 155 */     super((GameProfile)paramEither.map(paramGameProfile -> paramGameProfile, ResolvableProfile.Partial::createProfile), paramPatch);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 160 */     this.contents = paramEither;
/*     */   }
/*     */ 
/*     */   
/*     */   public CompletableFuture<GameProfile> resolveProfile(ProfileResolver paramProfileResolver) {
/* 165 */     return CompletableFuture.completedFuture(this.partialProfile);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Either<GameProfile, ResolvableProfile.Partial> unpack() {
/* 170 */     return this.contents;
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<String> name() {
/* 175 */     return (Optional<String>)this.contents.map(paramGameProfile -> Optional.of(paramGameProfile.name()), paramPartial -> paramPartial.name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 183 */     if (this != paramObject) { if (paramObject instanceof Static) { Static static_ = (Static)paramObject; if (this.contents.equals(static_.contents) && this.skinPatch.equals(static_.skinPatch)); }  return false; }
/*     */   
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 188 */     int i = 31 + this.contents.hashCode();
/* 189 */     i = 31 * i + this.skinPatch.hashCode();
/* 190 */     return i;
/*     */   }
/*     */   
/*     */   public void addToTooltip(Item.TooltipContext paramTooltipContext, Consumer<Component> paramConsumer, TooltipFlag paramTooltipFlag, DataComponentGetter paramDataComponentGetter) {}
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\component\ResolvableProfile$Static.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */