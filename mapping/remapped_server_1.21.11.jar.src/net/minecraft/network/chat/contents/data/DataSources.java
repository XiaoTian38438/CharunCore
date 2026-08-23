/*    */ package net.minecraft.network.chat.contents.data;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public class DataSources {
/*  8 */   private static final ExtraCodecs.LateBoundIdMapper<String, MapCodec<? extends DataSource>> ID_MAPPER = new ExtraCodecs.LateBoundIdMapper();
/*    */   
/* 10 */   public static final MapCodec<DataSource> CODEC = ComponentSerialization.createLegacyComponentMatcher(ID_MAPPER, DataSource::codec, "source");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 17 */     ID_MAPPER.put("entity", EntityDataSource.MAP_CODEC);
/* 18 */     ID_MAPPER.put("block", BlockDataSource.MAP_CODEC);
/* 19 */     ID_MAPPER.put("storage", StorageDataSource.MAP_CODEC);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\chat\contents\data\DataSources.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */