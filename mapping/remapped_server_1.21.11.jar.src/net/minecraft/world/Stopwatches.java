/*    */ package net.minecraft.world;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.TreeMap;
/*    */ import java.util.function.Supplier;
/*    */ import java.util.function.UnaryOperator;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.util.datafix.DataFixTypes;
/*    */ import net.minecraft.world.level.saveddata.SavedData;
/*    */ import net.minecraft.world.level.saveddata.SavedDataType;
/*    */ 
/*    */ public class Stopwatches
/*    */   extends SavedData {
/* 18 */   private static final Codec<Stopwatches> CODEC = Codec.unboundedMap(Identifier.CODEC, (Codec)Codec.LONG).fieldOf("stopwatches").codec()
/* 19 */     .xmap(Stopwatches::unpack, Stopwatches::pack);
/* 20 */   public static final SavedDataType<Stopwatches> TYPE = new SavedDataType("stopwatches", Stopwatches::new, CODEC, DataFixTypes.SAVED_DATA_STOPWATCHES);
/* 21 */   private final Map<Identifier, Stopwatch> stopwatches = (Map<Identifier, Stopwatch>)new Object2ObjectOpenHashMap();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static Stopwatches unpack(Map<Identifier, Long> paramMap) {
/* 27 */     Stopwatches stopwatches = new Stopwatches();
/* 28 */     long l = currentTime();
/* 29 */     paramMap.forEach((paramIdentifier, paramLong1) -> paramStopwatches.stopwatches.put(paramIdentifier, new Stopwatch(paramLong, paramLong1.longValue())));
/* 30 */     return stopwatches;
/*    */   }
/*    */   
/*    */   private Map<Identifier, Long> pack() {
/* 34 */     long l = currentTime();
/* 35 */     TreeMap<Object, Object> treeMap = new TreeMap<>();
/* 36 */     this.stopwatches.forEach((paramIdentifier, paramStopwatch) -> paramMap.put(paramIdentifier, Long.valueOf(paramStopwatch.elapsedMilliseconds(paramLong))));
/* 37 */     return (Map)treeMap;
/*    */   }
/*    */   
/*    */   public Stopwatch get(Identifier paramIdentifier) {
/* 41 */     return this.stopwatches.get(paramIdentifier);
/*    */   }
/*    */   
/*    */   public boolean add(Identifier paramIdentifier, Stopwatch paramStopwatch) {
/* 45 */     if (this.stopwatches.putIfAbsent(paramIdentifier, paramStopwatch) == null) {
/* 46 */       setDirty();
/* 47 */       return true;
/*    */     } 
/* 49 */     return false;
/*    */   }
/*    */   
/*    */   public boolean update(Identifier paramIdentifier, UnaryOperator<Stopwatch> paramUnaryOperator) {
/* 53 */     if (this.stopwatches.computeIfPresent(paramIdentifier, (paramIdentifier, paramStopwatch) -> (Stopwatch)paramUnaryOperator.apply(paramStopwatch)) != null) {
/* 54 */       setDirty();
/* 55 */       return true;
/*    */     } 
/* 57 */     return false;
/*    */   }
/*    */   
/*    */   public boolean remove(Identifier paramIdentifier) {
/* 61 */     boolean bool = (this.stopwatches.remove(paramIdentifier) != null) ? true : false;
/* 62 */     if (bool) {
/* 63 */       setDirty();
/*    */     }
/* 65 */     return bool;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isDirty() {
/* 70 */     return (super.isDirty() || !this.stopwatches.isEmpty());
/*    */   }
/*    */   
/*    */   public List<Identifier> ids() {
/* 74 */     return List.copyOf(this.stopwatches.keySet());
/*    */   }
/*    */ 
/*    */   
/*    */   public static long currentTime() {
/* 79 */     return Util.getMillis();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\Stopwatches.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */