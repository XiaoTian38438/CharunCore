/*    */ package net.minecraft.server.commands.data;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.suggestion.SuggestionProvider;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.Locale;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.commands.arguments.IdentifierArgument;
/*    */ import net.minecraft.commands.arguments.NbtPathArgument;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.NbtUtils;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.storage.CommandStorage;
/*    */ 
/*    */ public class StorageDataAccessor implements DataAccessor {
/*    */   static {
/* 25 */     SUGGEST_STORAGE = ((paramCommandContext, paramSuggestionsBuilder) -> SharedSuggestionProvider.suggestResource(getGlobalTags(paramCommandContext).keys(), paramSuggestionsBuilder));
/*    */   }
/*    */   
/*    */   static final SuggestionProvider<CommandSourceStack> SUGGEST_STORAGE;
/*    */   
/*    */   public static final Function<String, DataCommands.DataProvider> PROVIDER = paramString -> new DataCommands.DataProvider() { public DataAccessor access(CommandContext<CommandSourceStack> param1CommandContext) {
/* 31 */         return new StorageDataAccessor(StorageDataAccessor.getGlobalTags(param1CommandContext), IdentifierArgument.getId(param1CommandContext, arg));
/*    */       }
/*    */ 
/*    */       
/*    */       public ArgumentBuilder<CommandSourceStack, ?> wrap(ArgumentBuilder<CommandSourceStack, ?> param1ArgumentBuilder, Function<ArgumentBuilder<CommandSourceStack, ?>, ArgumentBuilder<CommandSourceStack, ?>> param1Function) {
/* 36 */         return param1ArgumentBuilder.then(Commands.literal("storage").then(param1Function.apply(Commands.argument(arg, (ArgumentType)IdentifierArgument.id()).suggests(StorageDataAccessor.SUGGEST_STORAGE))));
/*    */       } }
/*    */   ;
/*    */   
/*    */   static CommandStorage getGlobalTags(CommandContext<CommandSourceStack> paramCommandContext) {
/* 41 */     return ((CommandSourceStack)paramCommandContext.getSource()).getServer().getCommandStorage();
/*    */   }
/*    */   
/*    */   private final CommandStorage storage;
/*    */   private final Identifier id;
/*    */   
/*    */   StorageDataAccessor(CommandStorage paramCommandStorage, Identifier paramIdentifier) {
/* 48 */     this.storage = paramCommandStorage;
/* 49 */     this.id = paramIdentifier;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setData(CompoundTag paramCompoundTag) {
/* 54 */     this.storage.set(this.id, paramCompoundTag);
/*    */   }
/*    */ 
/*    */   
/*    */   public CompoundTag getData() {
/* 59 */     return this.storage.get(this.id);
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getModifiedSuccess() {
/* 64 */     return (Component)Component.translatable("commands.data.storage.modified", new Object[] { Component.translationArg(this.id) });
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getPrintSuccess(Tag paramTag) {
/* 69 */     return (Component)Component.translatable("commands.data.storage.query", new Object[] { Component.translationArg(this.id), NbtUtils.toPrettyComponent(paramTag) });
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getPrintSuccess(NbtPathArgument.NbtPath paramNbtPath, double paramDouble, int paramInt) {
/* 74 */     return (Component)Component.translatable("commands.data.storage.get", new Object[] { paramNbtPath.asString(), Component.translationArg(this.id), String.format(Locale.ROOT, "%.2f", new Object[] { Double.valueOf(paramDouble) }), Integer.valueOf(paramInt) });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\data\StorageDataAccessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */