/*     */ package net.minecraft.server.commands;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.StringArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.UuidArgument;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.chat.ClickEvent;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.network.chat.contents.objects.ObjectInfo;
/*     */ import net.minecraft.network.chat.contents.objects.PlayerSprite;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.players.ProfileResolver;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.item.component.ResolvableProfile;
/*     */ 
/*     */ public class FetchProfileCommand {
/*     */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/*  34 */     paramCommandDispatcher.register(
/*  35 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("fetchprofile")
/*  36 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  37 */         .then(
/*  38 */           Commands.literal("name")
/*  39 */           .then(
/*  40 */             Commands.argument("name", (ArgumentType)StringArgumentType.greedyString())
/*  41 */             .executes(paramCommandContext -> resolveName((CommandSourceStack)paramCommandContext.getSource(), StringArgumentType.getString(paramCommandContext, "name"))))))
/*     */ 
/*     */         
/*  44 */         .then(
/*  45 */           Commands.literal("id")
/*  46 */           .then(
/*  47 */             Commands.argument("id", (ArgumentType)UuidArgument.uuid())
/*  48 */             .executes(paramCommandContext -> resolveId((CommandSourceStack)paramCommandContext.getSource(), UuidArgument.getUuid(paramCommandContext, "id"))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void reportResolvedProfile(CommandSourceStack paramCommandSourceStack, GameProfile paramGameProfile, String paramString, Component paramComponent) {
/*  55 */     ResolvableProfile resolvableProfile = ResolvableProfile.createResolved(paramGameProfile);
/*  56 */     ResolvableProfile.CODEC.encodeStart((DynamicOps)NbtOps.INSTANCE, resolvableProfile)
/*  57 */       .ifSuccess(paramTag -> {
/*     */           String str = paramTag.toString();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           MutableComponent mutableComponent = Component.object((ObjectInfo)new PlayerSprite(paramResolvableProfile, true));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           ComponentSerialization.CODEC.encodeStart((DynamicOps)NbtOps.INSTANCE, mutableComponent).ifSuccess(()).ifError(());
/*  81 */         }).ifError(paramError -> paramCommandSourceStack.sendFailure((Component)Component.translatable("commands.fetchprofile.failed_to_serialize", new Object[] { paramError.message() })));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int resolveName(CommandSourceStack paramCommandSourceStack, String paramString) {
/*  87 */     MinecraftServer minecraftServer = paramCommandSourceStack.getServer();
/*  88 */     ProfileResolver profileResolver = minecraftServer.services().profileResolver();
/*     */     
/*  90 */     Util.nonCriticalIoPool().execute(() -> {
/*     */           MutableComponent mutableComponent = Component.literal(paramString);
/*     */ 
/*     */           
/*     */           Optional optional = paramProfileResolver.fetchByName(paramString);
/*     */ 
/*     */           
/*     */           paramMinecraftServer.execute(());
/*     */         });
/*     */     
/* 100 */     return 1;
/*     */   }
/*     */   
/*     */   private static int resolveId(CommandSourceStack paramCommandSourceStack, UUID paramUUID) {
/* 104 */     MinecraftServer minecraftServer = paramCommandSourceStack.getServer();
/* 105 */     ProfileResolver profileResolver = minecraftServer.services().profileResolver();
/*     */     
/* 107 */     Util.nonCriticalIoPool().execute(() -> {
/*     */           Component component = Component.translationArg(paramUUID);
/*     */ 
/*     */           
/*     */           Optional optional = paramProfileResolver.fetchById(paramUUID);
/*     */ 
/*     */           
/*     */           paramMinecraftServer.execute(());
/*     */         });
/*     */     
/* 117 */     return 1;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\FetchProfileCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */