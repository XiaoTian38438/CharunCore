/*     */ package net.minecraft.world.item.component;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.properties.PropertyMap;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.players.ProfileResolver;
/*     */ import net.minecraft.util.Util;
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
/*     */ public final class Dynamic
/*     */   extends ResolvableProfile
/*     */ {
/* 204 */   private static final Component DYNAMIC_TOOLTIP = (Component)Component.translatable("component.profile.dynamic").withStyle(ChatFormatting.GRAY);
/*     */   private final Either<String, UUID> nameOrId;
/*     */   
/*     */   Dynamic(Either<String, UUID> paramEither, PlayerSkin.Patch paramPatch) {
/* 208 */     super(ResolvableProfile.createPartialProfile(paramEither.left(), paramEither.right(), PropertyMap.EMPTY), paramPatch);
/* 209 */     this.nameOrId = paramEither;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Optional<String> name() {
/* 215 */     return this.nameOrId.left();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 220 */     if (this != paramObject) { if (paramObject instanceof Dynamic) { Dynamic dynamic = (Dynamic)paramObject; if (this.nameOrId.equals(dynamic.nameOrId) && this.skinPatch.equals(dynamic.skinPatch)); }  return false; }
/*     */   
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 225 */     int i = 31 + this.nameOrId.hashCode();
/* 226 */     i = 31 * i + this.skinPatch.hashCode();
/* 227 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Either<GameProfile, ResolvableProfile.Partial> unpack() {
/* 232 */     return Either.right(new ResolvableProfile.Partial(this.nameOrId.left(), this.nameOrId.right(), PropertyMap.EMPTY));
/*     */   }
/*     */ 
/*     */   
/*     */   public CompletableFuture<GameProfile> resolveProfile(ProfileResolver paramProfileResolver) {
/* 237 */     return CompletableFuture.supplyAsync(() -> (GameProfile)paramProfileResolver.fetchByNameOrId(this.nameOrId).orElse(this.partialProfile), (Executor)Util.nonCriticalIoPool());
/*     */   }
/*     */ 
/*     */   
/*     */   public void addToTooltip(Item.TooltipContext paramTooltipContext, Consumer<Component> paramConsumer, TooltipFlag paramTooltipFlag, DataComponentGetter paramDataComponentGetter) {
/* 242 */     paramConsumer.accept(DYNAMIC_TOOLTIP);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\component\ResolvableProfile$Dynamic.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */