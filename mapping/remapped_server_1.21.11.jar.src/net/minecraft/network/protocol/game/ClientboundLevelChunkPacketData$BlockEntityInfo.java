/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.network.codec.StreamDecoder;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class BlockEntityInfo
/*     */ {
/* 130 */   public static final StreamCodec<RegistryFriendlyByteBuf, BlockEntityInfo> STREAM_CODEC = StreamCodec.ofMember(BlockEntityInfo::write, BlockEntityInfo::new);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 135 */   public static final StreamCodec<RegistryFriendlyByteBuf, List<BlockEntityInfo>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());
/*     */   
/*     */   final int packedXZ;
/*     */   final int y;
/*     */   final BlockEntityType<?> type;
/*     */   final CompoundTag tag;
/*     */   
/*     */   private BlockEntityInfo(int paramInt1, int paramInt2, BlockEntityType<?> paramBlockEntityType, CompoundTag paramCompoundTag) {
/* 143 */     this.packedXZ = paramInt1;
/* 144 */     this.y = paramInt2;
/* 145 */     this.type = paramBlockEntityType;
/* 146 */     this.tag = paramCompoundTag;
/*     */   }
/*     */   
/*     */   private BlockEntityInfo(RegistryFriendlyByteBuf paramRegistryFriendlyByteBuf) {
/* 150 */     this.packedXZ = paramRegistryFriendlyByteBuf.readByte();
/* 151 */     this.y = paramRegistryFriendlyByteBuf.readShort();
/* 152 */     this.type = (BlockEntityType)ByteBufCodecs.registry(Registries.BLOCK_ENTITY_TYPE).decode(paramRegistryFriendlyByteBuf);
/* 153 */     this.tag = paramRegistryFriendlyByteBuf.readNbt();
/*     */   }
/*     */   
/*     */   private void write(RegistryFriendlyByteBuf paramRegistryFriendlyByteBuf) {
/* 157 */     paramRegistryFriendlyByteBuf.writeByte(this.packedXZ);
/* 158 */     paramRegistryFriendlyByteBuf.writeShort(this.y);
/* 159 */     ByteBufCodecs.registry(Registries.BLOCK_ENTITY_TYPE).encode(paramRegistryFriendlyByteBuf, this.type);
/* 160 */     paramRegistryFriendlyByteBuf.writeNbt((Tag)this.tag);
/*     */   }
/*     */   
/*     */   static BlockEntityInfo create(BlockEntity paramBlockEntity) {
/* 164 */     CompoundTag compoundTag = paramBlockEntity.getUpdateTag((HolderLookup.Provider)paramBlockEntity.getLevel().registryAccess());
/* 165 */     BlockPos blockPos = paramBlockEntity.getBlockPos();
/* 166 */     int i = SectionPos.sectionRelative(blockPos.getX()) << 4 | SectionPos.sectionRelative(blockPos.getZ());
/* 167 */     return new BlockEntityInfo(i, blockPos.getY(), paramBlockEntity.getType(), compoundTag.isEmpty() ? null : compoundTag);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\game\ClientboundLevelChunkPacketData$BlockEntityInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */