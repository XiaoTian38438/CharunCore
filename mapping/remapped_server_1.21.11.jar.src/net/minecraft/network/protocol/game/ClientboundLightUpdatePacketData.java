/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.BitSet;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.codec.StreamEncoder;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.LightLayer;
/*    */ import net.minecraft.world.level.chunk.DataLayer;
/*    */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*    */ 
/*    */ public class ClientboundLightUpdatePacketData {
/* 19 */   private static final StreamCodec<ByteBuf, byte[]> DATA_LAYER_STREAM_CODEC = ByteBufCodecs.byteArray(2048);
/*    */   
/*    */   private final BitSet skyYMask;
/*    */   private final BitSet blockYMask;
/*    */   private final BitSet emptySkyYMask;
/*    */   private final BitSet emptyBlockYMask;
/*    */   private final List<byte[]> skyUpdates;
/*    */   private final List<byte[]> blockUpdates;
/*    */   
/*    */   public ClientboundLightUpdatePacketData(ChunkPos paramChunkPos, LevelLightEngine paramLevelLightEngine, BitSet paramBitSet1, BitSet paramBitSet2) {
/* 29 */     this.skyYMask = new BitSet();
/* 30 */     this.blockYMask = new BitSet();
/* 31 */     this.emptySkyYMask = new BitSet();
/* 32 */     this.emptyBlockYMask = new BitSet();
/* 33 */     this.skyUpdates = Lists.newArrayList();
/* 34 */     this.blockUpdates = Lists.newArrayList();
/* 35 */     for (byte b = 0; b < paramLevelLightEngine.getLightSectionCount(); b++) {
/* 36 */       if (paramBitSet1 == null || paramBitSet1.get(b)) {
/* 37 */         prepareSectionData(paramChunkPos, paramLevelLightEngine, LightLayer.SKY, b, this.skyYMask, this.emptySkyYMask, this.skyUpdates);
/*    */       }
/* 39 */       if (paramBitSet2 == null || paramBitSet2.get(b)) {
/* 40 */         prepareSectionData(paramChunkPos, paramLevelLightEngine, LightLayer.BLOCK, b, this.blockYMask, this.emptyBlockYMask, this.blockUpdates);
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   public ClientboundLightUpdatePacketData(FriendlyByteBuf paramFriendlyByteBuf, int paramInt1, int paramInt2) {
/* 46 */     this.skyYMask = paramFriendlyByteBuf.readBitSet();
/* 47 */     this.blockYMask = paramFriendlyByteBuf.readBitSet();
/* 48 */     this.emptySkyYMask = paramFriendlyByteBuf.readBitSet();
/* 49 */     this.emptyBlockYMask = paramFriendlyByteBuf.readBitSet();
/* 50 */     this.skyUpdates = paramFriendlyByteBuf.readList((StreamDecoder)DATA_LAYER_STREAM_CODEC);
/* 51 */     this.blockUpdates = paramFriendlyByteBuf.readList((StreamDecoder)DATA_LAYER_STREAM_CODEC);
/*    */   }
/*    */   
/*    */   public void write(FriendlyByteBuf paramFriendlyByteBuf) {
/* 55 */     paramFriendlyByteBuf.writeBitSet(this.skyYMask);
/* 56 */     paramFriendlyByteBuf.writeBitSet(this.blockYMask);
/* 57 */     paramFriendlyByteBuf.writeBitSet(this.emptySkyYMask);
/* 58 */     paramFriendlyByteBuf.writeBitSet(this.emptyBlockYMask);
/* 59 */     paramFriendlyByteBuf.writeCollection(this.skyUpdates, (StreamEncoder)DATA_LAYER_STREAM_CODEC);
/* 60 */     paramFriendlyByteBuf.writeCollection(this.blockUpdates, (StreamEncoder)DATA_LAYER_STREAM_CODEC);
/*    */   }
/*    */   
/*    */   private void prepareSectionData(ChunkPos paramChunkPos, LevelLightEngine paramLevelLightEngine, LightLayer paramLightLayer, int paramInt, BitSet paramBitSet1, BitSet paramBitSet2, List<byte[]> paramList) {
/* 64 */     DataLayer dataLayer = paramLevelLightEngine.getLayerListener(paramLightLayer).getDataLayerData(SectionPos.of(paramChunkPos, paramLevelLightEngine.getMinLightSection() + paramInt));
/* 65 */     if (dataLayer != null) {
/* 66 */       if (dataLayer.isEmpty()) {
/* 67 */         paramBitSet2.set(paramInt);
/*    */       } else {
/* 69 */         paramBitSet1.set(paramInt);
/* 70 */         paramList.add(dataLayer.copy().getData());
/*    */       } 
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public BitSet getSkyYMask() {
/* 77 */     return this.skyYMask;
/*    */   }
/*    */   
/*    */   public BitSet getEmptySkyYMask() {
/* 81 */     return this.emptySkyYMask;
/*    */   }
/*    */   
/*    */   public List<byte[]> getSkyUpdates() {
/* 85 */     return this.skyUpdates;
/*    */   }
/*    */   
/*    */   public BitSet getBlockYMask() {
/* 89 */     return this.blockYMask;
/*    */   }
/*    */   
/*    */   public BitSet getEmptyBlockYMask() {
/* 93 */     return this.emptyBlockYMask;
/*    */   }
/*    */   
/*    */   public List<byte[]> getBlockUpdates() {
/* 97 */     return this.blockUpdates;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\game\ClientboundLightUpdatePacketData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */