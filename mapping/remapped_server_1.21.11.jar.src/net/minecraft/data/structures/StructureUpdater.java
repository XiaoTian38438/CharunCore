/*    */ package net.minecraft.data.structures;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.NbtUtils;
/*    */ import net.minecraft.server.packs.PackType;
/*    */ import net.minecraft.util.datafix.DataFixTypes;
/*    */ import net.minecraft.util.datafix.DataFixers;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class StructureUpdater
/*    */   implements SnbtToNbt.Filter
/*    */ {
/* 17 */   private static final Logger LOGGER = LogUtils.getLogger();
/* 18 */   private static final String PREFIX = PackType.SERVER_DATA.getDirectory() + "/minecraft/structure/";
/*    */ 
/*    */   
/*    */   public CompoundTag apply(String paramString, CompoundTag paramCompoundTag) {
/* 22 */     if (paramString.startsWith(PREFIX)) {
/* 23 */       return update(paramString, paramCompoundTag);
/*    */     }
/* 25 */     return paramCompoundTag;
/*    */   }
/*    */   
/*    */   public static CompoundTag update(String paramString, CompoundTag paramCompoundTag) {
/* 29 */     StructureTemplate structureTemplate = new StructureTemplate();
/* 30 */     int i = NbtUtils.getDataVersion(paramCompoundTag, 500);
/* 31 */     char c = 'ሪ';
/* 32 */     if (i < 4650) {
/* 33 */       LOGGER.warn("SNBT Too old, do not forget to update: {} < {}: {}", new Object[] { Integer.valueOf(i), Integer.valueOf(4650), paramString });
/*    */     }
/* 35 */     CompoundTag compoundTag = DataFixTypes.STRUCTURE.updateToCurrentVersion(DataFixers.getDataFixer(), paramCompoundTag, i);
/* 36 */     structureTemplate.load((HolderGetter)BuiltInRegistries.BLOCK, compoundTag);
/* 37 */     return structureTemplate.save(new CompoundTag());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\structures\StructureUpdater.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */