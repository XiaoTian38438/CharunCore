/*    */ package net.minecraft.world.level.storage;
/*    */ 
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.OptionalDynamic;
/*    */ import net.minecraft.SharedConstants;
/*    */ 
/*    */ public class LevelVersion {
/*    */   private final int levelDataVersion;
/*    */   private final long lastPlayed;
/*    */   private final String minecraftVersionName;
/*    */   private final DataVersion minecraftVersion;
/*    */   private final boolean snapshot;
/*    */   
/*    */   private LevelVersion(int paramInt1, long paramLong, String paramString1, int paramInt2, String paramString2, boolean paramBoolean) {
/* 15 */     this.levelDataVersion = paramInt1;
/* 16 */     this.lastPlayed = paramLong;
/* 17 */     this.minecraftVersionName = paramString1;
/* 18 */     this.minecraftVersion = new DataVersion(paramInt2, paramString2);
/* 19 */     this.snapshot = paramBoolean;
/*    */   }
/*    */   
/*    */   public static LevelVersion parse(Dynamic<?> paramDynamic) {
/* 23 */     int i = paramDynamic.get("version").asInt(0);
/* 24 */     long l = paramDynamic.get("LastPlayed").asLong(0L);
/* 25 */     OptionalDynamic optionalDynamic = paramDynamic.get("Version");
/*    */     
/* 27 */     if (optionalDynamic.result().isPresent()) {
/* 28 */       return new LevelVersion(i, l, optionalDynamic
/*    */ 
/*    */           
/* 31 */           .get("Name").asString(SharedConstants.getCurrentVersion().name()), optionalDynamic
/* 32 */           .get("Id").asInt(SharedConstants.getCurrentVersion().dataVersion().version()), optionalDynamic
/* 33 */           .get("Series").asString("main"), optionalDynamic
/* 34 */           .get("Snapshot").asBoolean(!SharedConstants.getCurrentVersion().stable()));
/*    */     }
/*    */     
/* 37 */     return new LevelVersion(i, l, "", 0, "main", false);
/*    */   }
/*    */   
/*    */   public int levelDataVersion() {
/* 41 */     return this.levelDataVersion;
/*    */   }
/*    */   
/*    */   public long lastPlayed() {
/* 45 */     return this.lastPlayed;
/*    */   }
/*    */   
/*    */   public String minecraftVersionName() {
/* 49 */     return this.minecraftVersionName;
/*    */   }
/*    */   
/*    */   public DataVersion minecraftVersion() {
/* 53 */     return this.minecraftVersion;
/*    */   }
/*    */   
/*    */   public boolean snapshot() {
/* 57 */     return this.snapshot;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\LevelVersion.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */