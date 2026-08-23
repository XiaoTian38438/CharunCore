/*     */ package net.minecraft.world.item.component;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.Item;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Default
/*     */   extends Record
/*     */   implements ItemAttributeModifiers.Display
/*     */ {
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Default;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #165	-> 0
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Default;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #165	-> 0
/*     */   }
/*     */   
/*     */   public final boolean equals(Object paramObject) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Default;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #165	-> 0
/*     */   }
/*     */   
/* 166 */   static final Default INSTANCE = new Default();
/*     */   
/* 168 */   static final MapCodec<Default> CODEC = MapCodec.unit(INSTANCE);
/* 169 */   static final StreamCodec<RegistryFriendlyByteBuf, Default> STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*     */ 
/*     */   
/*     */   public ItemAttributeModifiers.Display.Type type() {
/* 173 */     return ItemAttributeModifiers.Display.Type.DEFAULT;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void apply(Consumer<Component> paramConsumer, Player paramPlayer, Holder<Attribute> paramHolder, AttributeModifier paramAttributeModifier) {
/* 179 */     double d2, d1 = paramAttributeModifier.amount();
/* 180 */     boolean bool = false;
/*     */     
/* 182 */     if (paramPlayer != null) {
/* 183 */       if (paramAttributeModifier.is(Item.BASE_ATTACK_DAMAGE_ID)) {
/* 184 */         d1 += paramPlayer.getAttributeBaseValue(Attributes.ATTACK_DAMAGE);
/* 185 */         bool = true;
/* 186 */       } else if (paramAttributeModifier.is(Item.BASE_ATTACK_SPEED_ID)) {
/* 187 */         d1 += paramPlayer.getAttributeBaseValue(Attributes.ATTACK_SPEED);
/* 188 */         bool = true;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 193 */     if (paramAttributeModifier.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_BASE || paramAttributeModifier.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
/* 194 */       d2 = d1 * 100.0D;
/* 195 */     } else if (paramHolder.is(Attributes.KNOCKBACK_RESISTANCE)) {
/* 196 */       d2 = d1 * 10.0D;
/*     */     } else {
/* 198 */       d2 = d1;
/*     */     } 
/*     */     
/* 201 */     if (bool) {
/* 202 */       paramConsumer.accept(
/* 203 */           CommonComponents.space().append(
/* 204 */             (Component)Component.translatable("attribute.modifier.equals." + paramAttributeModifier.operation().id(), new Object[] { ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT
/* 205 */                 .format(d2), 
/* 206 */                 Component.translatable(((Attribute)paramHolder.value()).getDescriptionId())
/*     */               
/* 208 */               })).withStyle(ChatFormatting.DARK_GREEN));
/*     */     }
/* 210 */     else if (d1 > 0.0D) {
/* 211 */       paramConsumer.accept(Component.translatable("attribute.modifier.plus." + paramAttributeModifier.operation().id(), new Object[] { ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT
/* 212 */               .format(d2), 
/* 213 */               Component.translatable(((Attribute)paramHolder.value()).getDescriptionId())
/* 214 */             }).withStyle(((Attribute)paramHolder.value()).getStyle(true)));
/* 215 */     } else if (d1 < 0.0D) {
/* 216 */       paramConsumer.accept(Component.translatable("attribute.modifier.take." + paramAttributeModifier.operation().id(), new Object[] { ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT
/* 217 */               .format(-d2), 
/* 218 */               Component.translatable(((Attribute)paramHolder.value()).getDescriptionId())
/* 219 */             }).withStyle(((Attribute)paramHolder.value()).getStyle(false)));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\component\ItemAttributeModifiers$Display$Default.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */