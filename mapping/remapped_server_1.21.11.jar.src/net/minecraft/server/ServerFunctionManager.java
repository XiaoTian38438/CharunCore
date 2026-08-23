/*     */ package net.minecraft.server;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.commands.CommandResultCallback;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.ExecutionCommandSource;
/*     */ import net.minecraft.commands.FunctionInstantiationException;
/*     */ import net.minecraft.commands.execution.ExecutionContext;
/*     */ import net.minecraft.commands.functions.CommandFunction;
/*     */ import net.minecraft.commands.functions.InstantiatedFunction;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.server.permissions.PermissionSet;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ServerFunctionManager {
/*  24 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  26 */   private static final Identifier TICK_FUNCTION_TAG = Identifier.withDefaultNamespace("tick");
/*  27 */   private static final Identifier LOAD_FUNCTION_TAG = Identifier.withDefaultNamespace("load");
/*     */   
/*     */   private final MinecraftServer server;
/*     */   
/*  31 */   private List<CommandFunction<CommandSourceStack>> ticking = (List<CommandFunction<CommandSourceStack>>)ImmutableList.of();
/*     */   
/*     */   private boolean postReload;
/*     */   private ServerFunctionLibrary library;
/*     */   
/*     */   public ServerFunctionManager(MinecraftServer paramMinecraftServer, ServerFunctionLibrary paramServerFunctionLibrary) {
/*  37 */     this.server = paramMinecraftServer;
/*  38 */     this.library = paramServerFunctionLibrary;
/*  39 */     postReload(paramServerFunctionLibrary);
/*     */   }
/*     */   
/*     */   public CommandDispatcher<CommandSourceStack> getDispatcher() {
/*  43 */     return this.server.getCommands().getDispatcher();
/*     */   }
/*     */   
/*     */   public void tick() {
/*  47 */     if (!this.server.tickRateManager().runsNormally()) {
/*     */       return;
/*     */     }
/*  50 */     if (this.postReload) {
/*  51 */       this.postReload = false;
/*  52 */       List<CommandFunction<CommandSourceStack>> list = this.library.getTag(LOAD_FUNCTION_TAG);
/*  53 */       executeTagFunctions(list, LOAD_FUNCTION_TAG);
/*     */     } 
/*  55 */     executeTagFunctions(this.ticking, TICK_FUNCTION_TAG);
/*     */   }
/*     */   
/*     */   private void executeTagFunctions(Collection<CommandFunction<CommandSourceStack>> paramCollection, Identifier paramIdentifier) {
/*  59 */     Objects.requireNonNull(paramIdentifier); Profiler.get().push(paramIdentifier::toString);
/*  60 */     for (CommandFunction<CommandSourceStack> commandFunction : paramCollection) {
/*  61 */       execute(commandFunction, getGameLoopSender());
/*     */     }
/*  63 */     Profiler.get().pop();
/*     */   }
/*     */   
/*     */   public void execute(CommandFunction<CommandSourceStack> paramCommandFunction, CommandSourceStack paramCommandSourceStack) {
/*  67 */     ProfilerFiller profilerFiller = Profiler.get();
/*  68 */     profilerFiller.push(() -> "function " + String.valueOf(paramCommandFunction.id()));
/*     */     
/*  70 */     try { InstantiatedFunction instantiatedFunction = paramCommandFunction.instantiate(null, getDispatcher());
/*  71 */       Commands.executeCommandInContext(paramCommandSourceStack, paramExecutionContext -> ExecutionContext.queueInitialFunctionCall(paramExecutionContext, paramInstantiatedFunction, (ExecutionCommandSource)paramCommandSourceStack, CommandResultCallback.EMPTY)); }
/*  72 */     catch (FunctionInstantiationException functionInstantiationException) {  }
/*  73 */     catch (Exception exception)
/*  74 */     { LOGGER.warn("Failed to execute function {}", paramCommandFunction.id(), exception); }
/*     */     finally
/*  76 */     { profilerFiller.pop(); }
/*     */   
/*     */   }
/*     */   
/*     */   public void replaceLibrary(ServerFunctionLibrary paramServerFunctionLibrary) {
/*  81 */     this.library = paramServerFunctionLibrary;
/*  82 */     postReload(paramServerFunctionLibrary);
/*     */   }
/*     */   
/*     */   private void postReload(ServerFunctionLibrary paramServerFunctionLibrary) {
/*  86 */     this.ticking = List.copyOf(paramServerFunctionLibrary.getTag(TICK_FUNCTION_TAG));
/*  87 */     this.postReload = true;
/*     */   }
/*     */   
/*     */   public CommandSourceStack getGameLoopSender() {
/*  91 */     return this.server.createCommandSourceStack().withPermission((PermissionSet)LevelBasedPermissionSet.GAMEMASTER).withSuppressedOutput();
/*     */   }
/*     */   
/*     */   public Optional<CommandFunction<CommandSourceStack>> get(Identifier paramIdentifier) {
/*  95 */     return this.library.getFunction(paramIdentifier);
/*     */   }
/*     */   
/*     */   public List<CommandFunction<CommandSourceStack>> getTag(Identifier paramIdentifier) {
/*  99 */     return this.library.getTag(paramIdentifier);
/*     */   }
/*     */   
/*     */   public Iterable<Identifier> getFunctionNames() {
/* 103 */     return this.library.getFunctions().keySet();
/*     */   }
/*     */   
/*     */   public Iterable<Identifier> getTagNames() {
/* 107 */     return this.library.getAvailableTags();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\ServerFunctionManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */