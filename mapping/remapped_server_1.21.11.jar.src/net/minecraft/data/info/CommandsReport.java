/*    */ package net.minecraft.data.info;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.tree.CommandNode;
/*    */ import java.nio.file.Path;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.CompletionStage;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.synchronization.ArgumentUtils;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.data.CachedOutput;
/*    */ import net.minecraft.data.DataProvider;
/*    */ import net.minecraft.data.PackOutput;
/*    */ 
/*    */ public class CommandsReport implements DataProvider {
/*    */   private final PackOutput output;
/*    */   
/*    */   public CommandsReport(PackOutput paramPackOutput, CompletableFuture<HolderLookup.Provider> paramCompletableFuture) {
/* 20 */     this.output = paramPackOutput;
/* 21 */     this.registries = paramCompletableFuture;
/*    */   }
/*    */   private final CompletableFuture<HolderLookup.Provider> registries;
/*    */   
/*    */   public CompletableFuture<?> run(CachedOutput paramCachedOutput) {
/* 26 */     Path path = this.output.getOutputFolder(PackOutput.Target.REPORTS).resolve("commands.json");
/*    */     
/* 28 */     return this.registries.thenCompose(paramProvider -> {
/*    */           CommandDispatcher commandDispatcher = (new Commands(Commands.CommandSelection.ALL, Commands.createValidationContext(paramProvider))).getDispatcher();
/*    */           return DataProvider.saveStable(paramCachedOutput, (JsonElement)ArgumentUtils.serializeNodeToJson(commandDispatcher, (CommandNode)commandDispatcher.getRoot()), paramPath);
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getName() {
/* 36 */     return "Command Syntax";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\info\CommandsReport.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */