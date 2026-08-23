/*    */ package net.minecraft.world.item.slot;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ public final class EmptySlotSource extends Record implements SlotSource {
/*  7 */   public static final MapCodec<EmptySlotSource> MAP_CODEC = MapCodec.unit(new EmptySlotSource());
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/slot/EmptySlotSource;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0 } public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/slot/EmptySlotSource;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */   } public MapCodec<EmptySlotSource> codec() {
/* 11 */     return MAP_CODEC;
/*    */   } public final boolean equals(Object paramObject) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/slot/EmptySlotSource;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */   }
/*    */   public SlotCollection provide(LootContext paramLootContext) {
/* 16 */     return SlotCollection.EMPTY;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\slot\EmptySlotSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */