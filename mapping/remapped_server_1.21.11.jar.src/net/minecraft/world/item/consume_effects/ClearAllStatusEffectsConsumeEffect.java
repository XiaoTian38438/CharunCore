/*    */ package net.minecraft.world.item.consume_effects;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ 
/*    */ public final class ClearAllStatusEffectsConsumeEffect extends Record implements ConsumeEffect {
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/consume_effects/ClearAllStatusEffectsConsumeEffect;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */   }
/*    */   
/* 11 */   public static final ClearAllStatusEffectsConsumeEffect INSTANCE = new ClearAllStatusEffectsConsumeEffect();
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/consume_effects/ClearAllStatusEffectsConsumeEffect;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0 } public final boolean equals(Object paramObject) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/consume_effects/ClearAllStatusEffectsConsumeEffect;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/* 12 */     //   #10	-> 0 } public static final MapCodec<ClearAllStatusEffectsConsumeEffect> CODEC = MapCodec.unit(INSTANCE);
/* 13 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClearAllStatusEffectsConsumeEffect> STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*    */ 
/*    */   
/*    */   public ConsumeEffect.Type<ClearAllStatusEffectsConsumeEffect> getType() {
/* 17 */     return ConsumeEffect.Type.CLEAR_ALL_EFFECTS;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean apply(Level paramLevel, ItemStack paramItemStack, LivingEntity paramLivingEntity) {
/* 22 */     return paramLivingEntity.removeAllEffects();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\consume_effects\ClearAllStatusEffectsConsumeEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */