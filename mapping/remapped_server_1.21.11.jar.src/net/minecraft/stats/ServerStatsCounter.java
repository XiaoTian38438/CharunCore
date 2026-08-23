/*     */ package net.minecraft.stats;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import net.minecraft.util.StrictJsonParser;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ServerStatsCounter extends StatsCounter {
/*  43 */   private static final Gson GSON = (new GsonBuilder())
/*  44 */     .setPrettyPrinting()
/*  45 */     .create();
/*     */   
/*  47 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   private static final Codec<Map<Stat<?>, Integer>> STATS_CODEC;
/*     */   private final Path file;
/*     */   
/*     */   static {
/*  52 */     STATS_CODEC = Codec.dispatchedMap(BuiltInRegistries.STAT_TYPE.byNameCodec(), Util.memoize(ServerStatsCounter::createTypedStatsCodec)).xmap(paramMap -> {
/*     */           HashMap<Object, Object> hashMap = new HashMap<>();
/*     */           paramMap.forEach(());
/*     */           return hashMap;
/*     */         }paramMap -> (Map)paramMap.entrySet().stream().collect(Collectors.groupingBy((), Util.toMap())));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> Codec<Map<Stat<?>, Integer>> createTypedStatsCodec(StatType<T> paramStatType) {
/*  67 */     Codec codec1 = paramStatType.getRegistry().byNameCodec();
/*  68 */     Objects.requireNonNull(paramStatType); Codec codec2 = codec1.flatComapMap(paramStatType::get, paramStat -> (paramStat.getType() == paramStatType) ? DataResult.success(paramStat.getValue()) : DataResult.error(()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  75 */     return (Codec<Map<Stat<?>, Integer>>)Codec.unboundedMap(codec2, (Codec)Codec.INT);
/*     */   }
/*     */ 
/*     */   
/*  79 */   private final Set<Stat<?>> dirty = Sets.newHashSet();
/*     */   
/*     */   public ServerStatsCounter(MinecraftServer paramMinecraftServer, Path paramPath) {
/*  82 */     this.file = paramPath;
/*  83 */     if (Files.isRegularFile(paramPath, new java.nio.file.LinkOption[0])) {
/*  84 */       try { BufferedReader bufferedReader = Files.newBufferedReader(paramPath, StandardCharsets.UTF_8); 
/*  85 */         try { JsonElement jsonElement = StrictJsonParser.parse(bufferedReader);
/*  86 */           parse(paramMinecraftServer.getFixerUpper(), jsonElement);
/*  87 */           if (bufferedReader != null) bufferedReader.close();  } catch (Throwable throwable) { if (bufferedReader != null) try { bufferedReader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException iOException)
/*  88 */       { LOGGER.error("Couldn't read statistics file {}", paramPath, iOException); }
/*  89 */       catch (JsonParseException jsonParseException)
/*  90 */       { LOGGER.error("Couldn't parse statistics file {}", paramPath, jsonParseException); }
/*     */     
/*     */     }
/*     */   }
/*     */   
/*     */   public void save() {
/*     */     
/*  97 */     try { FileUtil.createDirectoriesSafe(this.file.getParent());
/*  98 */       BufferedWriter bufferedWriter = Files.newBufferedWriter(this.file, StandardCharsets.UTF_8, new java.nio.file.OpenOption[0]); 
/*  99 */       try { GSON.toJson(toJson(), GSON.newJsonWriter(bufferedWriter));
/* 100 */         if (bufferedWriter != null) bufferedWriter.close();  } catch (Throwable throwable) { if (bufferedWriter != null)
/* 101 */           try { bufferedWriter.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException|com.google.gson.JsonIOException iOException)
/* 102 */     { LOGGER.error("Couldn't save stats to {}", this.file, iOException); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValue(Player paramPlayer, Stat<?> paramStat, int paramInt) {
/* 108 */     super.setValue(paramPlayer, paramStat, paramInt);
/* 109 */     this.dirty.add(paramStat);
/*     */   }
/*     */   
/*     */   private Set<Stat<?>> getDirty() {
/* 113 */     HashSet<Stat<?>> hashSet = Sets.newHashSet(this.dirty);
/* 114 */     this.dirty.clear();
/* 115 */     return hashSet;
/*     */   }
/*     */   
/*     */   public void parse(DataFixer paramDataFixer, JsonElement paramJsonElement) {
/* 119 */     Dynamic dynamic = new Dynamic((DynamicOps)JsonOps.INSTANCE, paramJsonElement);
/*     */     
/* 121 */     dynamic = DataFixTypes.STATS.updateToCurrentVersion(paramDataFixer, dynamic, NbtUtils.getDataVersion(dynamic, 1343));
/*     */     
/* 123 */     this.stats.putAll(STATS_CODEC.parse(dynamic.get("stats").orElseEmptyMap())
/* 124 */         .resultOrPartial(paramString -> LOGGER.error("Failed to parse statistics for {}: {}", this.file, paramString))
/* 125 */         .orElse(Map.of()));
/*     */   }
/*     */ 
/*     */   
/*     */   protected JsonElement toJson() {
/* 130 */     JsonObject jsonObject = new JsonObject();
/* 131 */     jsonObject.add("stats", (JsonElement)STATS_CODEC.encodeStart((DynamicOps)JsonOps.INSTANCE, this.stats).getOrThrow());
/* 132 */     jsonObject.addProperty("DataVersion", Integer.valueOf(SharedConstants.getCurrentVersion().dataVersion().version()));
/* 133 */     return (JsonElement)jsonObject;
/*     */   }
/*     */   
/*     */   public void markAllDirty() {
/* 137 */     this.dirty.addAll((Collection<? extends Stat<?>>)this.stats.keySet());
/*     */   }
/*     */   
/*     */   public void sendStats(ServerPlayer paramServerPlayer) {
/* 141 */     Object2IntOpenHashMap object2IntOpenHashMap = new Object2IntOpenHashMap();
/*     */     
/* 143 */     for (Stat<?> stat : getDirty()) {
/* 144 */       object2IntOpenHashMap.put(stat, getValue(stat));
/*     */     }
/*     */     
/* 147 */     paramServerPlayer.connection.send((Packet)new ClientboundAwardStatsPacket((Object2IntMap)object2IntOpenHashMap));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\stats\ServerStatsCounter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */