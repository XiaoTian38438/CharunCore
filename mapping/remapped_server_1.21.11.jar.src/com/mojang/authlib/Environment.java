/*   */ package com.mojang.authlib;public final class Environment extends Record { private final String sessionHost; private final String servicesHost; private final String profilesHost; private final String name;
/*   */   
/* 3 */   public Environment(String paramString1, String paramString2, String paramString3, String paramString4) { this.sessionHost = paramString1; this.servicesHost = paramString2; this.profilesHost = paramString3; this.name = paramString4; } public final String toString() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lcom/mojang/authlib/Environment;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/* 3 */     //   #3	-> 0 } public String sessionHost() { return this.sessionHost; } public final int hashCode() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lcom/mojang/authlib/Environment;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #3	-> 0 } public final boolean equals(Object paramObject) { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lcom/mojang/authlib/Environment;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/* 3 */     //   #3	-> 0 } public String servicesHost() { return this.servicesHost; } public String profilesHost() { return this.profilesHost; } public String name() { return this.name; }
/*   */    }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\Environment.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */