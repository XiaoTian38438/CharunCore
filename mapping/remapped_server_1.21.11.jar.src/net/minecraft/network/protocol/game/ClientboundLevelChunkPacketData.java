/*     */ package net.minecraft.network.protocol.game;
/*     */ import com.google.common.collect.Lists;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import java.util.EnumMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.network.codec.StreamDecoder;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import net.minecraft.world.level.chunk.LevelChunkSection;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ 
/*     */ public class ClientboundLevelChunkPacketData {
/*  28 */   private static final StreamCodec<ByteBuf, Map<Heightmap.Types, long[]>> HEIGHTMAPS_STREAM_CODEC = ByteBufCodecs.map(paramInt -> new EnumMap<>(Heightmap.Types.class), Heightmap.Types.STREAM_CODEC, ByteBufCodecs.LONG_ARRAY);
/*     */   
/*     */   private static final int TWO_MEGABYTES = 2097152;
/*     */   
/*     */   private final Map<Heightmap.Types, long[]> heightmaps;
/*     */   
/*     */   private final byte[] buffer;
/*     */   
/*     */   private final List<BlockEntityInfo> blockEntitiesData;
/*     */ 
/*     */   
/*     */   public ClientboundLevelChunkPacketData(LevelChunk paramLevelChunk) {
/*  40 */     this
/*     */       
/*  42 */       .heightmaps = (Map<Heightmap.Types, long[]>)paramLevelChunk.getHeightmaps().stream().filter(paramEntry -> ((Heightmap.Types)paramEntry.getKey()).sendToClient()).collect(Collectors.toMap(Map.Entry::getKey, paramEntry -> (long[])((Heightmap)paramEntry.getValue()).getRawData().clone()));
/*     */ 
/*     */     
/*  45 */     this.buffer = new byte[calculateChunkSize(paramLevelChunk)];
/*  46 */     extractChunkData(new FriendlyByteBuf(getWriteBuffer()), paramLevelChunk);
/*     */     
/*  48 */     this.blockEntitiesData = Lists.newArrayList();
/*  49 */     for (Map.Entry entry : paramLevelChunk.getBlockEntities().entrySet()) {
/*  50 */       this.blockEntitiesData.add(BlockEntityInfo.create((BlockEntity)entry.getValue()));
/*     */     }
/*     */   }
/*     */   
/*     */   public ClientboundLevelChunkPacketData(RegistryFriendlyByteBuf paramRegistryFriendlyByteBuf, int paramInt1, int paramInt2) {
/*  55 */     this.heightmaps = (Map<Heightmap.Types, long[]>)HEIGHTMAPS_STREAM_CODEC.decode(paramRegistryFriendlyByteBuf);
/*     */     
/*  57 */     int i = paramRegistryFriendlyByteBuf.readVarInt();
/*  58 */     if (i > 2097152) {
/*  59 */       throw new RuntimeException("Chunk Packet trying to allocate too much memory on read.");
/*     */     }
/*     */     
/*  62 */     this.buffer = new byte[i];
/*  63 */     paramRegistryFriendlyByteBuf.readBytes(this.buffer);
/*     */     
/*  65 */     this.blockEntitiesData = (List<BlockEntityInfo>)BlockEntityInfo.LIST_STREAM_CODEC.decode(paramRegistryFriendlyByteBuf);
/*     */   }
/*     */   
/*     */   public void write(RegistryFriendlyByteBuf paramRegistryFriendlyByteBuf) {
/*  69 */     HEIGHTMAPS_STREAM_CODEC.encode(paramRegistryFriendlyByteBuf, this.heightmaps);
/*  70 */     paramRegistryFriendlyByteBuf.writeVarInt(this.buffer.length);
/*  71 */     paramRegistryFriendlyByteBuf.writeBytes(this.buffer);
/*     */     
/*  73 */     BlockEntityInfo.LIST_STREAM_CODEC.encode(paramRegistryFriendlyByteBuf, this.blockEntitiesData);
/*     */   }
/*     */   
/*     */   private static int calculateChunkSize(LevelChunk paramLevelChunk) {
/*  77 */     int i = 0;
/*     */     
/*  79 */     for (LevelChunkSection levelChunkSection : paramLevelChunk.getSections()) {
/*  80 */       i += levelChunkSection.getSerializedSize();
/*     */     }
/*     */     
/*  83 */     return i;
/*     */   }
/*     */   
/*     */   private ByteBuf getWriteBuffer() {
/*  87 */     ByteBuf byteBuf = Unpooled.wrappedBuffer(this.buffer);
/*  88 */     byteBuf.writerIndex(0);
/*  89 */     return byteBuf;
/*     */   }
/*     */   
/*     */   public static void extractChunkData(FriendlyByteBuf paramFriendlyByteBuf, LevelChunk paramLevelChunk) {
/*  93 */     for (LevelChunkSection levelChunkSection : paramLevelChunk.getSections()) {
/*  94 */       levelChunkSection.write(paramFriendlyByteBuf);
/*     */     }
/*  96 */     if (paramFriendlyByteBuf.writerIndex() != paramFriendlyByteBuf.capacity()) {
/*  97 */       throw new IllegalStateException("Didn't fill chunk buffer: expected " + paramFriendlyByteBuf.capacity() + " bytes, got " + paramFriendlyByteBuf.writerIndex());
/*     */     }
/*     */   }
/*     */   
/*     */   public Consumer<BlockEntityTagOutput> getBlockEntitiesTagsConsumer(int paramInt1, int paramInt2) {
/* 102 */     return paramBlockEntityTagOutput -> getBlockEntitiesTags(paramBlockEntityTagOutput, paramInt1, paramInt2);
/*     */   }
/*     */   
/*     */   private void getBlockEntitiesTags(BlockEntityTagOutput paramBlockEntityTagOutput, int paramInt1, int paramInt2) {
/* 106 */     int i = 16 * paramInt1;
/* 107 */     int j = 16 * paramInt2;
/* 108 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/* 109 */     for (BlockEntityInfo blockEntityInfo : this.blockEntitiesData) {
/* 110 */       int k = i + SectionPos.sectionRelative(blockEntityInfo.packedXZ >> 4);
/* 111 */       int m = j + SectionPos.sectionRelative(blockEntityInfo.packedXZ);
/* 112 */       mutableBlockPos.set(k, blockEntityInfo.y, m);
/* 113 */       paramBlockEntityTagOutput.accept((BlockPos)mutableBlockPos, blockEntityInfo.type, blockEntityInfo.tag);
/*     */     } 
/*     */   }
/*     */   public FriendlyByteBuf getReadBuffer() {
/* 117 */     return new FriendlyByteBuf(Unpooled.wrappedBuffer(this.buffer));
/*     */   }
/*     */   
/*     */   public Map<Heightmap.Types, long[]> getHeightmaps() {
/* 121 */     return this.heightmaps;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class BlockEntityInfo
/*     */   {
/* 130 */     public static final StreamCodec<RegistryFriendlyByteBuf, BlockEntityInfo> STREAM_CODEC = StreamCodec.ofMember(BlockEntityInfo::write, BlockEntityInfo::new);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 135 */     public static final StreamCodec<RegistryFriendlyByteBuf, List<BlockEntityInfo>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());
/*     */     
/*     */     final int packedXZ;
/*     */     final int y;
/*     */     final BlockEntityType<?> type;
/*     */     final CompoundTag tag;
/*     */     
/*     */     private BlockEntityInfo(int param1Int1, int param1Int2, BlockEntityType<?> param1BlockEntityType, CompoundTag param1CompoundTag) {
/* 143 */       this.packedXZ = param1Int1;
/* 144 */       this.y = param1Int2;
/* 145 */       this.type = param1BlockEntityType;
/* 146 */       this.tag = param1CompoundTag;
/*     */     }
/*     */     
/*     */     private BlockEntityInfo(RegistryFriendlyByteBuf param1RegistryFriendlyByteBuf) {
/* 150 */       this.packedXZ = param1RegistryFriendlyByteBuf.readByte();
/* 151 */       this.y = param1RegistryFriendlyByteBuf.readShort();
/* 152 */       this.type = (BlockEntityType)ByteBufCodecs.registry(Registries.BLOCK_ENTITY_TYPE).decode(param1RegistryFriendlyByteBuf);
/* 153 */       this.tag = param1RegistryFriendlyByteBuf.readNbt();
/*     */     }
/*     */     
/*     */     private void write(RegistryFriendlyByteBuf param1RegistryFriendlyByteBuf) {
/* 157 */       param1RegistryFriendlyByteBuf.writeByte(this.packedXZ);
/* 158 */       param1RegistryFriendlyByteBuf.writeShort(this.y);
/* 159 */       ByteBufCodecs.registry(Registries.BLOCK_ENTITY_TYPE).encode(param1RegistryFriendlyByteBuf, this.type);
/* 160 */       param1RegistryFriendlyByteBuf.writeNbt((Tag)this.tag);
/*     */     }
/*     */     
/*     */     static BlockEntityInfo create(BlockEntity param1BlockEntity) {
/* 164 */       CompoundTag compoundTag = param1BlockEntity.getUpdateTag((HolderLookup.Provider)param1BlockEntity.getLevel().registryAccess());
/* 165 */       BlockPos blockPos = param1BlockEntity.getBlockPos();
/* 166 */       int i = SectionPos.sectionRelative(blockPos.getX()) << 4 | SectionPos.sectionRelative(blockPos.getZ());
/* 167 */       return new BlockEntityInfo(i, blockPos.getY(), param1BlockEntity.getType(), compoundTag.isEmpty() ? null : compoundTag);
/*     */     }
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface BlockEntityTagOutput {
/*     */     void accept(BlockPos param1BlockPos, BlockEntityType<?> param1BlockEntityType, CompoundTag param1CompoundTag);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\game\ClientboundLevelChunkPacketData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */