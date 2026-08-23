/*     */ package net.minecraft.server.commands;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.Locale;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.SystemReport;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import net.minecraft.util.FileZipper;
/*     */ import net.minecraft.util.TimeUtil;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.profiling.EmptyProfileResults;
/*     */ import net.minecraft.util.profiling.ProfileResults;
/*     */ import net.minecraft.util.profiling.metrics.storage.MetricsPersister;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class PerfCommand {
/*  32 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  33 */   private static final SimpleCommandExceptionType ERROR_NOT_RUNNING = new SimpleCommandExceptionType((Message)Component.translatable("commands.perf.notRunning"));
/*  34 */   private static final SimpleCommandExceptionType ERROR_ALREADY_RUNNING = new SimpleCommandExceptionType((Message)Component.translatable("commands.perf.alreadyRunning"));
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/*  37 */     paramCommandDispatcher.register(
/*  38 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("perf")
/*  39 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_OWNERS)))
/*  40 */         .then(Commands.literal("start").executes(paramCommandContext -> startProfilingDedicatedServer((CommandSourceStack)paramCommandContext.getSource()))))
/*  41 */         .then(Commands.literal("stop").executes(paramCommandContext -> stopProfilingDedicatedServer((CommandSourceStack)paramCommandContext.getSource()))));
/*     */   }
/*     */ 
/*     */   
/*     */   private static int startProfilingDedicatedServer(CommandSourceStack paramCommandSourceStack) throws CommandSyntaxException {
/*  46 */     MinecraftServer minecraftServer = paramCommandSourceStack.getServer();
/*  47 */     if (minecraftServer.isRecordingMetrics()) {
/*  48 */       throw ERROR_ALREADY_RUNNING.create();
/*     */     }
/*     */     
/*  51 */     Consumer consumer1 = paramProfileResults -> whenStopped(paramCommandSourceStack, paramProfileResults);
/*  52 */     Consumer consumer2 = paramPath -> saveResults(paramCommandSourceStack, paramPath, paramMinecraftServer);
/*     */     
/*  54 */     minecraftServer.startRecordingMetrics(consumer1, consumer2);
/*  55 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.perf.started"), false);
/*  56 */     return 0;
/*     */   }
/*     */   
/*     */   private static int stopProfilingDedicatedServer(CommandSourceStack paramCommandSourceStack) throws CommandSyntaxException {
/*  60 */     MinecraftServer minecraftServer = paramCommandSourceStack.getServer();
/*  61 */     if (!minecraftServer.isRecordingMetrics()) {
/*  62 */       throw ERROR_NOT_RUNNING.create();
/*     */     }
/*     */     
/*  65 */     minecraftServer.finishRecordingMetrics();
/*  66 */     return 0;
/*     */   }
/*     */   
/*     */   private static void saveResults(CommandSourceStack paramCommandSourceStack, Path paramPath, MinecraftServer paramMinecraftServer) {
/*  70 */     String str2, str1 = String.format(Locale.ROOT, "%s-%s-%s", new Object[] {
/*  71 */           Util.getFilenameFormattedDateTime(), paramMinecraftServer
/*  72 */           .getWorldData().getLevelName(), 
/*  73 */           SharedConstants.getCurrentVersion().id()
/*     */         });
/*     */     
/*     */     try {
/*  77 */       str2 = FileUtil.findAvailableName(MetricsPersister.PROFILING_RESULTS_DIR, str1, ".zip");
/*  78 */     } catch (IOException iOException) {
/*  79 */       paramCommandSourceStack.sendFailure((Component)Component.translatable("commands.perf.reportFailed"));
/*  80 */       LOGGER.error("Failed to create report name", iOException);
/*     */       
/*     */       return;
/*     */     } 
/*  84 */     FileZipper fileZipper = new FileZipper(MetricsPersister.PROFILING_RESULTS_DIR.resolve(str2)); 
/*  85 */     try { fileZipper.add(Paths.get("system.txt", new String[0]), paramMinecraftServer.fillSystemReport(new SystemReport()).toLineSeparatedString());
/*  86 */       fileZipper.add(paramPath);
/*  87 */       fileZipper.close(); } catch (Throwable throwable) { try { fileZipper.close(); }
/*     */       catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */        throw throwable; }
/*  90 */      try { FileUtils.forceDelete(paramPath.toFile()); }
/*  91 */     catch (IOException iOException)
/*  92 */     { LOGGER.warn("Failed to delete temporary profiling file {}", paramPath, iOException); }
/*     */ 
/*     */     
/*  95 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.perf.reportSaved", new Object[] { paramString }), false);
/*     */   }
/*     */   
/*     */   private static void whenStopped(CommandSourceStack paramCommandSourceStack, ProfileResults paramProfileResults) {
/*  99 */     if (paramProfileResults == EmptyProfileResults.EMPTY) {
/*     */       return;
/*     */     }
/*     */     
/* 103 */     int i = paramProfileResults.getTickDuration();
/* 104 */     double d = paramProfileResults.getNanoDuration() / TimeUtil.NANOSECONDS_PER_SECOND;
/* 105 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.perf.stopped", new Object[] { String.format(Locale.ROOT, "%.2f", new Object[] { Double.valueOf(paramDouble) }), Integer.valueOf(paramInt), String.format(Locale.ROOT, "%.2f", new Object[] { Double.valueOf(paramInt / paramDouble) }) }), false);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\PerfCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */