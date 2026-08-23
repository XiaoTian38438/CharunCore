/*   */ package com.mojang.authlib.minecraft;public final class BanDetails extends Record { private final UUID id; @Nullable
/*   */   private final Instant expires; @Nullable
/*   */   private final String reason; @Nullable
/*   */   private final String reasonMessage;
/*   */   public static final String MULTIPLAYER_SCOPE = "MULTIPLAYER";
/*   */   
/* 7 */   public BanDetails(UUID paramUUID, @Nullable Instant paramInstant, @Nullable String paramString1, @Nullable String paramString2) { this.id = paramUUID; this.expires = paramInstant; this.reason = paramString1; this.reasonMessage = paramString2; } public final String toString() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lcom/mojang/authlib/minecraft/BanDetails;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/* 7 */     //   #7	-> 0 } public UUID id() { return this.id; } public final int hashCode() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lcom/mojang/authlib/minecraft/BanDetails;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #7	-> 0 } public final boolean equals(Object paramObject) { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lcom/mojang/authlib/minecraft/BanDetails;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/* 7 */     //   #7	-> 0 } @Nullable public Instant expires() { return this.expires; } @Nullable public String reason() { return this.reason; } @Nullable public String reasonMessage() { return this.reasonMessage; }
/*   */    }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\minecraft\BanDetails.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */