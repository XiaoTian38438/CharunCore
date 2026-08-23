/*    */ package net.minecraft.world.level.storage;
/*    */ import com.mojang.datafixers.DataFixer;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.io.File;
/*    */ import java.nio.file.CopyOption;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.StandardCopyOption;
/*    */ import java.nio.file.attribute.FileAttribute;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.NbtAccounter;
/*    */ import net.minecraft.nbt.NbtIo;
/*    */ import net.minecraft.nbt.NbtUtils;
/*    */ import net.minecraft.server.players.NameAndId;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.util.datafix.DataFixTypes;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class PlayerDataStorage {
/* 24 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   private final File playerDir;
/*    */   protected final DataFixer fixerUpper;
/*    */   
/*    */   public PlayerDataStorage(LevelStorageSource.LevelStorageAccess paramLevelStorageAccess, DataFixer paramDataFixer) {
/* 29 */     this.fixerUpper = paramDataFixer;
/* 30 */     this.playerDir = paramLevelStorageAccess.getLevelPath(LevelResource.PLAYER_DATA_DIR).toFile();
/* 31 */     this.playerDir.mkdirs();
/*    */   }
/*    */   public void save(Player paramPlayer) {
/*    */     
/* 35 */     try { ProblemReporter.ScopedCollector scopedCollector = new ProblemReporter.ScopedCollector(paramPlayer.problemPath(), LOGGER); 
/* 36 */       try { TagValueOutput tagValueOutput = TagValueOutput.createWithContext((ProblemReporter)scopedCollector, (HolderLookup.Provider)paramPlayer.registryAccess());
/* 37 */         paramPlayer.saveWithoutId(tagValueOutput);
/* 38 */         Path path1 = this.playerDir.toPath();
/* 39 */         Path path2 = Files.createTempFile(path1, paramPlayer.getStringUUID() + "-", ".dat", (FileAttribute<?>[])new FileAttribute[0]);
/* 40 */         CompoundTag compoundTag = tagValueOutput.buildResult();
/* 41 */         NbtIo.writeCompressed(compoundTag, path2);
/*    */         
/* 43 */         Path path3 = path1.resolve(paramPlayer.getStringUUID() + ".dat");
/* 44 */         Path path4 = path1.resolve(paramPlayer.getStringUUID() + ".dat_old");
/* 45 */         Util.safeReplaceFile(path3, path2, path4);
/* 46 */         scopedCollector.close(); } catch (Throwable throwable) { try { scopedCollector.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (Exception exception)
/* 47 */     { LOGGER.warn("Failed to save player data for {}", paramPlayer.getPlainTextName()); }
/*    */   
/*    */   }
/*    */   
/*    */   private void backup(NameAndId paramNameAndId, String paramString) {
/* 52 */     Path path1 = this.playerDir.toPath();
/* 53 */     String str = paramNameAndId.id().toString();
/* 54 */     Path path2 = path1.resolve(str + str);
/* 55 */     Path path3 = path1.resolve(str + "_corrupted_" + str + ZonedDateTime.now().format(FileNameDateFormatter.FORMATTER));
/*    */     
/* 57 */     if (!Files.isRegularFile(path2, new java.nio.file.LinkOption[0])) {
/*    */       return;
/*    */     }
/*    */     
/*    */     try {
/* 62 */       Files.copy(path2, path3, new CopyOption[] { StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES });
/* 63 */     } catch (Exception exception) {
/* 64 */       LOGGER.warn("Failed to copy the player.dat file for {}", paramNameAndId.name(), exception);
/*    */     } 
/*    */   }
/*    */   
/*    */   private Optional<CompoundTag> load(NameAndId paramNameAndId, String paramString) {
/* 69 */     File file = new File(this.playerDir, String.valueOf(paramNameAndId.id()) + String.valueOf(paramNameAndId.id()));
/* 70 */     if (file.exists() && file.isFile()) {
/*    */       try {
/* 72 */         return Optional.of(NbtIo.readCompressed(file.toPath(), NbtAccounter.unlimitedHeap()));
/* 73 */       } catch (Exception exception) {
/* 74 */         LOGGER.warn("Failed to load player data for {}", paramNameAndId.name());
/*    */       } 
/*    */     }
/* 77 */     return Optional.empty();
/*    */   }
/*    */   
/*    */   public Optional<CompoundTag> load(NameAndId paramNameAndId) {
/* 81 */     Optional<CompoundTag> optional = load(paramNameAndId, ".dat");
/* 82 */     if (optional.isEmpty()) {
/* 83 */       backup(paramNameAndId, ".dat");
/*    */     }
/*    */     
/* 86 */     return optional.or(() -> load(paramNameAndId, ".dat_old"))
/* 87 */       .map(paramCompoundTag -> {
/*    */           int i = NbtUtils.getDataVersion(paramCompoundTag);
/*    */           return DataFixTypes.PLAYER.updateToCurrentVersion(this.fixerUpper, paramCompoundTag, i);
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\PlayerDataStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */