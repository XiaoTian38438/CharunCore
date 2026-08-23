/*    */ package net.minecraft.world.level.saveddata.maps;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.datafix.DataFixTypes;
/*    */ import net.minecraft.world.level.saveddata.SavedDataType;
/*    */ 
/*    */ public class MapIndex extends SavedData {
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)Codec.INT.optionalFieldOf("map", Integer.valueOf(-1)).forGetter(())).apply((Applicative)paramInstance, MapIndex::new));
/*    */   }
/*    */   private static final int NO_MAP_ID = -1;
/*    */   public static final Codec<MapIndex> CODEC;
/* 16 */   public static final SavedDataType<MapIndex> TYPE = new SavedDataType("idcounts", MapIndex::new, CODEC, DataFixTypes.SAVED_DATA_MAP_INDEX);
/*    */   
/*    */   private int lastMapId;
/*    */   
/*    */   public MapIndex() {
/* 21 */     this(-1);
/*    */   }
/*    */   
/*    */   public MapIndex(int paramInt) {
/* 25 */     this.lastMapId = paramInt;
/*    */   }
/*    */   
/*    */   public MapId getNextMapId() {
/* 29 */     MapId mapId = new MapId(++this.lastMapId);
/* 30 */     setDirty();
/* 31 */     return mapId;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\saveddata\maps\MapIndex.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */