/*    */ package net.minecraft.server.commands;
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.server.packs.repository.PackRepository;
/*    */ import net.minecraft.world.level.storage.WorldData;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class ReloadCommand {
/* 19 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   public static void reloadPacks(Collection<String> paramCollection, CommandSourceStack paramCommandSourceStack) {
/* 22 */     paramCommandSourceStack.getServer().reloadResources(paramCollection).exceptionally(paramThrowable -> {
/*    */           LOGGER.warn("Failed to execute reload", paramThrowable);
/*    */           paramCommandSourceStack.sendFailure((Component)Component.translatable("commands.reload.failure"));
/*    */           return null;
/*    */         });
/*    */   }
/*    */   
/*    */   private static Collection<String> discoverNewPacks(PackRepository paramPackRepository, WorldData paramWorldData, Collection<String> paramCollection) {
/* 30 */     paramPackRepository.reload();
/* 31 */     ArrayList<String> arrayList = Lists.newArrayList(paramCollection);
/* 32 */     List list = paramWorldData.getDataConfiguration().dataPacks().getDisabled();
/*    */     
/* 34 */     for (String str : paramPackRepository.getAvailableIds()) {
/* 35 */       if (!list.contains(str) && !arrayList.contains(str)) {
/* 36 */         arrayList.add(str);
/*    */       }
/*    */     } 
/* 39 */     return arrayList;
/*    */   }
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/* 43 */     paramCommandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("reload")
/* 44 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 45 */         .executes(paramCommandContext -> {
/*    */             CommandSourceStack commandSourceStack = (CommandSourceStack)paramCommandContext.getSource();
/*    */             MinecraftServer minecraftServer = commandSourceStack.getServer();
/*    */             PackRepository packRepository = minecraftServer.getPackRepository();
/*    */             WorldData worldData = minecraftServer.getWorldData();
/*    */             Collection<String> collection1 = packRepository.getSelectedIds();
/*    */             Collection<String> collection2 = discoverNewPacks(packRepository, worldData, collection1);
/*    */             commandSourceStack.sendSuccess((), true);
/*    */             reloadPacks(collection2, commandSourceStack);
/*    */             return 0;
/*    */           }));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\ReloadCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */