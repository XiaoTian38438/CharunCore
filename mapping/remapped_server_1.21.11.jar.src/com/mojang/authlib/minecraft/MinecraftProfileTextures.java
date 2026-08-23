/*    */ package com.mojang.authlib.minecraft;public final class MinecraftProfileTextures extends Record { @Nullable
/*    */   private final MinecraftProfileTexture skin; @Nullable
/*    */   private final MinecraftProfileTexture cape; @Nullable
/*    */   private final MinecraftProfileTexture elytra;
/*    */   private final SignatureState signatureState;
/*    */   
/*  7 */   public MinecraftProfileTextures(@Nullable MinecraftProfileTexture paramMinecraftProfileTexture1, @Nullable MinecraftProfileTexture paramMinecraftProfileTexture2, @Nullable MinecraftProfileTexture paramMinecraftProfileTexture3, SignatureState paramSignatureState) { this.skin = paramMinecraftProfileTexture1; this.cape = paramMinecraftProfileTexture2; this.elytra = paramMinecraftProfileTexture3; this.signatureState = paramSignatureState; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/authlib/minecraft/MinecraftProfileTextures;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*  7 */     //   #7	-> 0 } @Nullable public MinecraftProfileTexture skin() { return this.skin; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/authlib/minecraft/MinecraftProfileTextures;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0 } public final boolean equals(Object paramObject) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/authlib/minecraft/MinecraftProfileTextures;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*  7 */     //   #7	-> 0 } @Nullable public MinecraftProfileTexture cape() { return this.cape; } @Nullable public MinecraftProfileTexture elytra() { return this.elytra; } public SignatureState signatureState() { return this.signatureState; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 13 */   public static final MinecraftProfileTextures EMPTY = new MinecraftProfileTextures(null, null, null, SignatureState.SIGNED); }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\minecraft\MinecraftProfileTextures.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */