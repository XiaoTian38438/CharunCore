/*    */ package net.minecraft.core.particles;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class ItemParticleOption implements ParticleOptions {
/* 11 */   private static final Codec<ItemStack> ITEM_CODEC = Codec.withAlternative(ItemStack.SINGLE_ITEM_CODEC, Item.CODEC, ItemStack::new);
/*    */   
/*    */   private final ParticleType<ItemParticleOption> type;
/*    */   private final ItemStack itemStack;
/*    */   
/*    */   public static MapCodec<ItemParticleOption> codec(ParticleType<ItemParticleOption> paramParticleType) {
/* 17 */     return ITEM_CODEC.xmap(paramItemStack -> new ItemParticleOption(paramParticleType, paramItemStack), paramItemParticleOption -> paramItemParticleOption.itemStack).fieldOf("item");
/*    */   }
/*    */   
/*    */   public static StreamCodec<? super RegistryFriendlyByteBuf, ItemParticleOption> streamCodec(ParticleType<ItemParticleOption> paramParticleType) {
/* 21 */     return ItemStack.STREAM_CODEC.map(paramItemStack -> new ItemParticleOption(paramParticleType, paramItemStack), paramItemParticleOption -> paramItemParticleOption.itemStack);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemParticleOption(ParticleType<ItemParticleOption> paramParticleType, ItemStack paramItemStack) {
/* 28 */     if (paramItemStack.isEmpty()) {
/* 29 */       throw new IllegalArgumentException("Empty stacks are not allowed");
/*    */     }
/* 31 */     this.type = paramParticleType;
/* 32 */     this.itemStack = paramItemStack;
/*    */   }
/*    */ 
/*    */   
/*    */   public ParticleType<ItemParticleOption> getType() {
/* 37 */     return this.type;
/*    */   }
/*    */   
/*    */   public ItemStack getItem() {
/* 41 */     return this.itemStack;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\particles\ItemParticleOption.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */