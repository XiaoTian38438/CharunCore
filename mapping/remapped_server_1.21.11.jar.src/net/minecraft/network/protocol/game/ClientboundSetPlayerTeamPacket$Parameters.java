/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.world.scores.PlayerTeam;
/*     */ import net.minecraft.world.scores.Team;
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
/*     */ public class Parameters
/*     */ {
/*     */   private final Component displayName;
/*     */   private final Component playerPrefix;
/*     */   private final Component playerSuffix;
/*     */   private final Team.Visibility nametagVisibility;
/*     */   private final Team.CollisionRule collisionRule;
/*     */   private final ChatFormatting color;
/*     */   private final int options;
/*     */   
/*     */   public Parameters(PlayerTeam paramPlayerTeam) {
/* 162 */     this.displayName = paramPlayerTeam.getDisplayName();
/* 163 */     this.options = paramPlayerTeam.packOptions();
/* 164 */     this.nametagVisibility = paramPlayerTeam.getNameTagVisibility();
/* 165 */     this.collisionRule = paramPlayerTeam.getCollisionRule();
/* 166 */     this.color = paramPlayerTeam.getColor();
/* 167 */     this.playerPrefix = paramPlayerTeam.getPlayerPrefix();
/* 168 */     this.playerSuffix = paramPlayerTeam.getPlayerSuffix();
/*     */   }
/*     */   
/*     */   public Parameters(RegistryFriendlyByteBuf paramRegistryFriendlyByteBuf) {
/* 172 */     this.displayName = (Component)ComponentSerialization.TRUSTED_STREAM_CODEC.decode(paramRegistryFriendlyByteBuf);
/* 173 */     this.options = paramRegistryFriendlyByteBuf.readByte();
/* 174 */     this.nametagVisibility = (Team.Visibility)Team.Visibility.STREAM_CODEC.decode(paramRegistryFriendlyByteBuf);
/* 175 */     this.collisionRule = (Team.CollisionRule)Team.CollisionRule.STREAM_CODEC.decode(paramRegistryFriendlyByteBuf);
/* 176 */     this.color = (ChatFormatting)paramRegistryFriendlyByteBuf.readEnum(ChatFormatting.class);
/* 177 */     this.playerPrefix = (Component)ComponentSerialization.TRUSTED_STREAM_CODEC.decode(paramRegistryFriendlyByteBuf);
/* 178 */     this.playerSuffix = (Component)ComponentSerialization.TRUSTED_STREAM_CODEC.decode(paramRegistryFriendlyByteBuf);
/*     */   }
/*     */   
/*     */   public Component getDisplayName() {
/* 182 */     return this.displayName;
/*     */   }
/*     */   
/*     */   public int getOptions() {
/* 186 */     return this.options;
/*     */   }
/*     */   
/*     */   public ChatFormatting getColor() {
/* 190 */     return this.color;
/*     */   }
/*     */   
/*     */   public Team.Visibility getNametagVisibility() {
/* 194 */     return this.nametagVisibility;
/*     */   }
/*     */   
/*     */   public Team.CollisionRule getCollisionRule() {
/* 198 */     return this.collisionRule;
/*     */   }
/*     */   
/*     */   public Component getPlayerPrefix() {
/* 202 */     return this.playerPrefix;
/*     */   }
/*     */   
/*     */   public Component getPlayerSuffix() {
/* 206 */     return this.playerSuffix;
/*     */   }
/*     */   
/*     */   public void write(RegistryFriendlyByteBuf paramRegistryFriendlyByteBuf) {
/* 210 */     ComponentSerialization.TRUSTED_STREAM_CODEC.encode(paramRegistryFriendlyByteBuf, this.displayName);
/* 211 */     paramRegistryFriendlyByteBuf.writeByte(this.options);
/* 212 */     Team.Visibility.STREAM_CODEC.encode(paramRegistryFriendlyByteBuf, this.nametagVisibility);
/* 213 */     Team.CollisionRule.STREAM_CODEC.encode(paramRegistryFriendlyByteBuf, this.collisionRule);
/* 214 */     paramRegistryFriendlyByteBuf.writeEnum((Enum)this.color);
/* 215 */     ComponentSerialization.TRUSTED_STREAM_CODEC.encode(paramRegistryFriendlyByteBuf, this.playerPrefix);
/* 216 */     ComponentSerialization.TRUSTED_STREAM_CODEC.encode(paramRegistryFriendlyByteBuf, this.playerSuffix);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\game\ClientboundSetPlayerTeamPacket$Parameters.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */