/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import java.util.UUID;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.world.BossEvent;
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
/*     */ class AddOperation
/*     */   implements ClientboundBossEventPacket.Operation
/*     */ {
/*     */   private final Component name;
/*     */   private final float progress;
/*     */   private final BossEvent.BossBarColor color;
/*     */   private final BossEvent.BossBarOverlay overlay;
/*     */   private final boolean darkenScreen;
/*     */   private final boolean playMusic;
/*     */   private final boolean createWorldFog;
/*     */   
/*     */   AddOperation(BossEvent paramBossEvent) {
/* 148 */     this.name = paramBossEvent.getName();
/* 149 */     this.progress = paramBossEvent.getProgress();
/* 150 */     this.color = paramBossEvent.getColor();
/* 151 */     this.overlay = paramBossEvent.getOverlay();
/* 152 */     this.darkenScreen = paramBossEvent.shouldDarkenScreen();
/* 153 */     this.playMusic = paramBossEvent.shouldPlayBossMusic();
/* 154 */     this.createWorldFog = paramBossEvent.shouldCreateWorldFog();
/*     */   }
/*     */   
/*     */   private AddOperation(RegistryFriendlyByteBuf paramRegistryFriendlyByteBuf) {
/* 158 */     this.name = (Component)ComponentSerialization.TRUSTED_STREAM_CODEC.decode(paramRegistryFriendlyByteBuf);
/* 159 */     this.progress = paramRegistryFriendlyByteBuf.readFloat();
/* 160 */     this.color = (BossEvent.BossBarColor)paramRegistryFriendlyByteBuf.readEnum(BossEvent.BossBarColor.class);
/* 161 */     this.overlay = (BossEvent.BossBarOverlay)paramRegistryFriendlyByteBuf.readEnum(BossEvent.BossBarOverlay.class);
/* 162 */     short s = paramRegistryFriendlyByteBuf.readUnsignedByte();
/* 163 */     this.darkenScreen = ((s & 0x1) > 0);
/* 164 */     this.playMusic = ((s & 0x2) > 0);
/* 165 */     this.createWorldFog = ((s & 0x4) > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public ClientboundBossEventPacket.OperationType getType() {
/* 170 */     return ClientboundBossEventPacket.OperationType.ADD;
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispatch(UUID paramUUID, ClientboundBossEventPacket.Handler paramHandler) {
/* 175 */     paramHandler.add(paramUUID, this.name, this.progress, this.color, this.overlay, this.darkenScreen, this.playMusic, this.createWorldFog);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(RegistryFriendlyByteBuf paramRegistryFriendlyByteBuf) {
/* 180 */     ComponentSerialization.TRUSTED_STREAM_CODEC.encode(paramRegistryFriendlyByteBuf, this.name);
/* 181 */     paramRegistryFriendlyByteBuf.writeFloat(this.progress);
/* 182 */     paramRegistryFriendlyByteBuf.writeEnum((Enum)this.color);
/* 183 */     paramRegistryFriendlyByteBuf.writeEnum((Enum)this.overlay);
/* 184 */     paramRegistryFriendlyByteBuf.writeByte(ClientboundBossEventPacket.encodeProperties(this.darkenScreen, this.playMusic, this.createWorldFog));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\game\ClientboundBossEventPacket$AddOperation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */