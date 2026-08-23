/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import it.unimi.dsi.fastutil.shorts.ShortArrayList;
/*    */ import it.unimi.dsi.fastutil.shorts.ShortList;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.IntStream;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public class ChunkToProtochunkFix extends DataFix {
/*    */   private static final int NUM_SECTIONS = 16;
/*    */   
/*    */   public ChunkToProtochunkFix(Schema paramSchema, boolean paramBoolean) {
/* 20 */     super(paramSchema, paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 25 */     return writeFixAndRead("ChunkToProtoChunkFix", getInputSchema().getType(References.CHUNK), getOutputSchema().getType(References.CHUNK), paramDynamic -> paramDynamic.update("Level", ChunkToProtochunkFix::fixChunkData));
/*    */   }
/*    */ 
/*    */   
/*    */   private static <T> Dynamic<T> fixChunkData(Dynamic<T> paramDynamic) {
/*    */     String str;
/* 31 */     boolean bool = paramDynamic.get("TerrainPopulated").asBoolean(false);
/*    */     
/* 33 */     boolean bool1 = (paramDynamic.get("LightPopulated").asNumber().result().isEmpty() || paramDynamic.get("LightPopulated").asBoolean(false)) ? true : false;
/*    */ 
/*    */     
/* 36 */     if (bool) {
/* 37 */       if (bool1) {
/* 38 */         str = "mobs_spawned";
/*    */       } else {
/* 40 */         str = "decorated";
/*    */       } 
/*    */     } else {
/* 43 */       str = "carved";
/*    */     } 
/* 45 */     return repackTicks(repackBiomes(paramDynamic))
/* 46 */       .set("Status", paramDynamic.createString(str))
/* 47 */       .set("hasLegacyStructureData", paramDynamic.createBoolean(true));
/*    */   }
/*    */   
/*    */   private static <T> Dynamic<T> repackBiomes(Dynamic<T> paramDynamic) {
/* 51 */     return paramDynamic.update("Biomes", paramDynamic2 -> (Dynamic)DataFixUtils.orElse(paramDynamic2.asByteBufferOpt().result().map(()), paramDynamic2));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static <T> Dynamic<T> repackTicks(Dynamic<T> paramDynamic) {
/* 68 */     return (Dynamic<T>)DataFixUtils.orElse(paramDynamic
/* 69 */         .get("TileTicks").asStreamOpt().result().map(paramStream -> { List list = (List)IntStream.range(0, 16).mapToObj(()).collect(Collectors.toList()); paramStream.forEach(()); return paramDynamic.remove("TileTicks").set("ToBeTicked", paramDynamic.createList(list.stream().map(()))); }), paramDynamic);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static short packOffsetCoordinates(int paramInt1, int paramInt2, int paramInt3) {
/* 86 */     return (short)(paramInt1 & 0xF | (paramInt2 & 0xF) << 4 | (paramInt3 & 0xF) << 8);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\ChunkToProtochunkFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */