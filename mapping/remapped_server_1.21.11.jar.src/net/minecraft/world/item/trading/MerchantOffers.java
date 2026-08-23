/*    */ package net.minecraft.world.item.trading;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class MerchantOffers
/*    */   extends ArrayList<MerchantOffer> {
/* 16 */   public static final Codec<MerchantOffers> CODEC = MerchantOffer.CODEC.listOf().optionalFieldOf("Recipes", List.of())
/* 17 */     .xmap(MerchantOffers::new, Function.identity())
/* 18 */     .codec();
/*    */   
/* 20 */   public static final StreamCodec<RegistryFriendlyByteBuf, MerchantOffers> STREAM_CODEC = MerchantOffer.STREAM_CODEC.apply(ByteBufCodecs.collection(MerchantOffers::new));
/*    */ 
/*    */   
/*    */   public MerchantOffers() {}
/*    */   
/*    */   private MerchantOffers(int paramInt) {
/* 26 */     super(paramInt);
/*    */   }
/*    */   
/*    */   private MerchantOffers(Collection<MerchantOffer> paramCollection) {
/* 30 */     super(paramCollection);
/*    */   }
/*    */   
/*    */   public MerchantOffer getRecipeFor(ItemStack paramItemStack1, ItemStack paramItemStack2, int paramInt) {
/* 34 */     if (paramInt > 0 && paramInt < size()) {
/*    */       
/* 36 */       MerchantOffer merchantOffer = get(paramInt);
/* 37 */       if (merchantOffer.satisfiedBy(paramItemStack1, paramItemStack2)) {
/* 38 */         return merchantOffer;
/*    */       }
/* 40 */       return null;
/*    */     } 
/*    */     
/* 43 */     for (byte b = 0; b < size(); b++) {
/* 44 */       MerchantOffer merchantOffer = get(b);
/* 45 */       if (merchantOffer.satisfiedBy(paramItemStack1, paramItemStack2)) {
/* 46 */         return merchantOffer;
/*    */       }
/*    */     } 
/* 49 */     return null;
/*    */   }
/*    */   
/*    */   public MerchantOffers copy() {
/* 53 */     MerchantOffers merchantOffers = new MerchantOffers(size());
/* 54 */     for (MerchantOffer merchantOffer : this) {
/* 55 */       merchantOffers.add(merchantOffer.copy());
/*    */     }
/* 57 */     return merchantOffers;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\trading\MerchantOffers.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */