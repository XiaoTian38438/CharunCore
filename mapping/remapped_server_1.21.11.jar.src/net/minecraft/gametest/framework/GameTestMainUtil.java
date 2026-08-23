/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Stream;
/*     */ import joptsimple.OptionParser;
/*     */ import joptsimple.OptionSet;
/*     */ import joptsimple.OptionSpec;
/*     */ import net.minecraft.SuppressForbidden;
/*     */ import net.minecraft.server.Bootstrap;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.packs.repository.PackRepository;
/*     */ import net.minecraft.server.packs.repository.ServerPacksSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.storage.LevelStorageSource;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class GameTestMainUtil {
/*  27 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final String DEFAULT_UNIVERSE_DIR = "gametestserver";
/*     */   private static final String LEVEL_NAME = "gametestworld";
/*  31 */   private static final OptionParser parser = new OptionParser();
/*  32 */   private static final OptionSpec<String> universe = (OptionSpec<String>)parser.accepts("universe", "The path to where the test server world will be created. Any existing folder will be replaced.").withRequiredArg().defaultsTo("gametestserver", (Object[])new String[0]);
/*  33 */   private static final OptionSpec<File> report = (OptionSpec<File>)parser.accepts("report", "Exports results in a junit-like XML report at the given path.").withRequiredArg().ofType(File.class);
/*  34 */   private static final OptionSpec<String> tests = (OptionSpec<String>)parser.accepts("tests", "Which test(s) to run (namespaced ID selector using wildcards). Empty means run all.").withRequiredArg();
/*  35 */   private static final OptionSpec<Boolean> verify = (OptionSpec<Boolean>)parser.accepts("verify", "Runs the tests specified with `test` or `testNamespace` 100 times for each 90 degree rotation step").withRequiredArg().ofType(Boolean.class).defaultsTo(Boolean.valueOf(false), (Object[])new Boolean[0]);
/*  36 */   private static final OptionSpec<String> packs = (OptionSpec<String>)parser.accepts("packs", "A folder of datapacks to include in the world").withRequiredArg();
/*  37 */   private static final OptionSpec<Void> help = (OptionSpec<Void>)parser.accepts("help").forHelp();
/*     */   
/*     */   @SuppressForbidden(a = "Using System.err due to no bootstrap")
/*     */   public static void runGameTestServer(String[] paramArrayOfString, Consumer<String> paramConsumer) throws Exception {
/*  41 */     parser.allowsUnrecognizedOptions();
/*     */     
/*  43 */     OptionSet optionSet = parser.parse(paramArrayOfString);
/*  44 */     if (optionSet.has(help)) {
/*  45 */       parser.printHelpOn(System.err);
/*     */       
/*     */       return;
/*     */     } 
/*  49 */     if (((Boolean)optionSet.valueOf(verify)).booleanValue() && !optionSet.has(tests)) {
/*  50 */       LOGGER.error("Please specify a test selection to run the verify option. For example: --verify --tests example:test_something_*");
/*  51 */       System.exit(-1);
/*     */     } 
/*     */     
/*  54 */     LOGGER.info("Running GameTestMain with cwd '{}', universe path '{}'", System.getProperty("user.dir"), optionSet.valueOf(universe));
/*     */     
/*  56 */     if (optionSet.has(report)) {
/*  57 */       GlobalTestReporter.replaceWith(new JUnitLikeTestReporter((File)report.value(optionSet)));
/*     */     }
/*     */     
/*  60 */     Bootstrap.bootStrap();
/*  61 */     Util.startTimerHackThread();
/*     */     
/*  63 */     String str = (String)optionSet.valueOf(universe);
/*  64 */     createOrResetDir(str);
/*  65 */     paramConsumer.accept(str);
/*  66 */     if (optionSet.has(packs)) {
/*  67 */       String str1 = (String)optionSet.valueOf(packs);
/*  68 */       copyPacks(str, str1);
/*     */     } 
/*     */     
/*  71 */     LevelStorageSource.LevelStorageAccess levelStorageAccess = LevelStorageSource.createDefault(Paths.get(str, new String[0])).createAccess("gametestworld");
/*  72 */     PackRepository packRepository = ServerPacksSource.createPackRepository(levelStorageAccess);
/*  73 */     MinecraftServer.spin(paramThread -> GameTestServer.create(paramThread, paramLevelStorageAccess, paramPackRepository, optionalFromOption(paramOptionSet, tests), paramOptionSet.has(verify)));
/*     */   }
/*     */   
/*     */   private static Optional<String> optionalFromOption(OptionSet paramOptionSet, OptionSpec<String> paramOptionSpec) {
/*  77 */     return paramOptionSet.has(paramOptionSpec) ? Optional.<String>of((String)paramOptionSet.valueOf(paramOptionSpec)) : Optional.<String>empty();
/*     */   }
/*     */   
/*     */   private static void createOrResetDir(String paramString) throws IOException {
/*  81 */     Path path = Paths.get(paramString, new String[0]);
/*  82 */     if (Files.exists(path, new java.nio.file.LinkOption[0])) {
/*  83 */       FileUtils.deleteDirectory(path.toFile());
/*     */     }
/*  85 */     Files.createDirectories(path, (FileAttribute<?>[])new FileAttribute[0]);
/*     */   }
/*     */   
/*     */   private static void copyPacks(String paramString1, String paramString2) throws IOException {
/*  89 */     Path path1 = Paths.get(paramString1, new String[0]).resolve("gametestworld").resolve("datapacks");
/*  90 */     if (!Files.exists(path1, new java.nio.file.LinkOption[0])) {
/*  91 */       Files.createDirectories(path1, (FileAttribute<?>[])new FileAttribute[0]);
/*     */     }
/*  93 */     Path path2 = Paths.get(paramString2, new String[0]);
/*  94 */     if (Files.exists(path2, new java.nio.file.LinkOption[0])) {
/*  95 */       Stream<Path> stream = Files.list(path2); try {
/*  96 */         for (Path path3 : stream.toList()) {
/*  97 */           Path path4 = path1.resolve(path3.getFileName());
/*  98 */           if (Files.isDirectory(path3, new java.nio.file.LinkOption[0])) {
/*  99 */             if (Files.isRegularFile(path3.resolve("pack.mcmeta"), new java.nio.file.LinkOption[0])) {
/* 100 */               FileUtils.copyDirectory(path3.toFile(), path4.toFile());
/* 101 */               LOGGER.info("Included folder pack {}", path3.getFileName());
/*     */             }  continue;
/* 103 */           }  if (path3.toString().endsWith(".zip")) {
/* 104 */             Files.copy(path3, path4, new java.nio.file.CopyOption[0]);
/* 105 */             LOGGER.info("Included zip pack {}", path3.getFileName());
/*     */           } 
/*     */         } 
/* 108 */         if (stream != null) stream.close(); 
/*     */       } catch (Throwable throwable) {
/*     */         if (stream != null)
/*     */           try {
/*     */             stream.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\gametest\framework\GameTestMainUtil.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */