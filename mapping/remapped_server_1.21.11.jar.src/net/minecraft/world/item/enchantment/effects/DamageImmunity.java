/*   */ package net.minecraft.world.item.enchantment.effects;
/*   */ public final class DamageImmunity extends Record {
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/DamageImmunity;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */   }
/*   */   
/* 7 */   public static final DamageImmunity INSTANCE = new DamageImmunity();
/*   */   public final int hashCode() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/DamageImmunity;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0 } public final boolean equals(Object paramObject) { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/DamageImmunity;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/* 8 */     //   #6	-> 0 } public static final Codec<DamageImmunity> CODEC = MapCodec.unitCodec(INSTANCE);
/*   */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\enchantment\effects\DamageImmunity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */