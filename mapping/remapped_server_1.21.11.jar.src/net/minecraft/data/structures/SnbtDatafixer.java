/*    */ package net.minecraft.data.structures;
/*    */ 
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.Paths;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.DetectedVersion;
/*    */ import net.minecraft.SharedConstants;
/*    */ import net.minecraft.data.CachedOutput;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.NbtUtils;
/*    */ import net.minecraft.server.Bootstrap;
/*    */ 
/*    */ public class SnbtDatafixer
/*    */ {
/*    */   public static void main(String[] paramArrayOfString) throws IOException {
/* 19 */     SharedConstants.setVersion(DetectedVersion.BUILT_IN);
/* 20 */     Bootstrap.bootStrap();
/* 21 */     for (String str : paramArrayOfString) {
/* 22 */       updateInDirectory(str);
/*    */     }
/*    */   }
/*    */   
/*    */   private static void updateInDirectory(String paramString) throws IOException {
/* 27 */     Stream<Path> stream = Files.walk(Paths.get(paramString, new String[0]), new java.nio.file.FileVisitOption[0]); try {
/* 28 */       stream.filter(paramPath -> paramPath.toString().endsWith(".snbt")).forEach(paramPath -> {
/*    */             try {
/*    */               String str = Files.readString(paramPath);
/*    */               CompoundTag compoundTag1 = NbtUtils.snbtToStructure(str);
/*    */               CompoundTag compoundTag2 = StructureUpdater.update(paramPath.toString(), compoundTag1);
/*    */               NbtToSnbt.writeSnbt(CachedOutput.NO_CACHE, paramPath, NbtUtils.structureToSnbt(compoundTag2));
/* 34 */             } catch (CommandSyntaxException|IOException commandSyntaxException) {
/*    */               throw new RuntimeException(commandSyntaxException);
/*    */             } 
/*    */           });
/* 38 */       if (stream != null) stream.close(); 
/*    */     } catch (Throwable throwable) {
/*    */       if (stream != null)
/*    */         try {
/*    */           stream.close();
/*    */         } catch (Throwable throwable1) {
/*    */           throwable.addSuppressed(throwable1);
/*    */         }  
/*    */       throw throwable;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\structures\SnbtDatafixer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */