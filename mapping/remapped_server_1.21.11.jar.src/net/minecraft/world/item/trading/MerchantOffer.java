/*     */ package net.minecraft.world.item.trading;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function10;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public class MerchantOffer {
/*     */   static {
/*  13 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)ItemCost.CODEC.fieldOf("buy").forGetter(()), (App)ItemCost.CODEC.lenientOptionalFieldOf("buyB").forGetter(()), (App)ItemStack.CODEC.fieldOf("sell").forGetter(()), (App)Codec.INT.lenientOptionalFieldOf("uses", Integer.valueOf(0)).forGetter(()), (App)Codec.INT.lenientOptionalFieldOf("maxUses", Integer.valueOf(4)).forGetter(()), (App)Codec.BOOL.lenientOptionalFieldOf("rewardExp", Boolean.valueOf(true)).forGetter(()), (App)Codec.INT.lenientOptionalFieldOf("specialPrice", Integer.valueOf(0)).forGetter(()), (App)Codec.INT.lenientOptionalFieldOf("demand", Integer.valueOf(0)).forGetter(()), (App)Codec.FLOAT.lenientOptionalFieldOf("priceMultiplier", Float.valueOf(0.0F)).forGetter(()), (App)Codec.INT.lenientOptionalFieldOf("xp", Integer.valueOf(1)).forGetter(())).apply((Applicative)paramInstance, MerchantOffer::new));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final Codec<MerchantOffer> CODEC;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  26 */   public static final StreamCodec<RegistryFriendlyByteBuf, MerchantOffer> STREAM_CODEC = StreamCodec.of(MerchantOffer::writeToStream, MerchantOffer::createFromStream);
/*     */   
/*     */   private final ItemCost baseCostA;
/*     */   
/*     */   private final Optional<ItemCost> costB;
/*     */   private final ItemStack result;
/*     */   private int uses;
/*     */   private final int maxUses;
/*     */   private final boolean rewardExp;
/*     */   private int specialPriceDiff;
/*     */   private int demand;
/*     */   private final float priceMultiplier;
/*     */   private final int xp;
/*     */   
/*     */   private MerchantOffer(ItemCost paramItemCost, Optional<ItemCost> paramOptional, ItemStack paramItemStack, int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3, int paramInt4, float paramFloat, int paramInt5) {
/*  41 */     this.baseCostA = paramItemCost;
/*  42 */     this.costB = paramOptional;
/*  43 */     this.result = paramItemStack;
/*  44 */     this.uses = paramInt1;
/*  45 */     this.maxUses = paramInt2;
/*  46 */     this.rewardExp = paramBoolean;
/*  47 */     this.specialPriceDiff = paramInt3;
/*  48 */     this.demand = paramInt4;
/*  49 */     this.priceMultiplier = paramFloat;
/*  50 */     this.xp = paramInt5;
/*     */   }
/*     */   
/*     */   public MerchantOffer(ItemCost paramItemCost, ItemStack paramItemStack, int paramInt1, int paramInt2, float paramFloat) {
/*  54 */     this(paramItemCost, Optional.empty(), paramItemStack, paramInt1, paramInt2, paramFloat);
/*     */   }
/*     */   
/*     */   public MerchantOffer(ItemCost paramItemCost, Optional<ItemCost> paramOptional, ItemStack paramItemStack, int paramInt1, int paramInt2, float paramFloat) {
/*  58 */     this(paramItemCost, paramOptional, paramItemStack, 0, paramInt1, paramInt2, paramFloat);
/*     */   }
/*     */   
/*     */   public MerchantOffer(ItemCost paramItemCost, Optional<ItemCost> paramOptional, ItemStack paramItemStack, int paramInt1, int paramInt2, int paramInt3, float paramFloat) {
/*  62 */     this(paramItemCost, paramOptional, paramItemStack, paramInt1, paramInt2, paramInt3, paramFloat, 0);
/*     */   }
/*     */   
/*     */   public MerchantOffer(ItemCost paramItemCost, Optional<ItemCost> paramOptional, ItemStack paramItemStack, int paramInt1, int paramInt2, int paramInt3, float paramFloat, int paramInt4) {
/*  66 */     this(paramItemCost, paramOptional, paramItemStack, paramInt1, paramInt2, true, 0, paramInt4, paramFloat, paramInt3);
/*     */   }
/*     */   
/*     */   private MerchantOffer(MerchantOffer paramMerchantOffer) {
/*  70 */     this(paramMerchantOffer.baseCostA, paramMerchantOffer.costB, paramMerchantOffer.result
/*     */ 
/*     */         
/*  73 */         .copy(), paramMerchantOffer.uses, paramMerchantOffer.maxUses, paramMerchantOffer.rewardExp, paramMerchantOffer.specialPriceDiff, paramMerchantOffer.demand, paramMerchantOffer.priceMultiplier, paramMerchantOffer.xp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack getBaseCostA() {
/*  85 */     return this.baseCostA.itemStack();
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getCostA() {
/*  90 */     return this.baseCostA.itemStack().copyWithCount(getModifiedCostCount(this.baseCostA));
/*     */   }
/*     */   
/*     */   private int getModifiedCostCount(ItemCost paramItemCost) {
/*  94 */     int i = paramItemCost.count();
/*     */ 
/*     */     
/*  97 */     int j = Math.max(0, Mth.floor((i * this.demand) * this.priceMultiplier));
/*     */     
/*  99 */     return Mth.clamp(i + j + this.specialPriceDiff, 1, paramItemCost.itemStack().getMaxStackSize());
/*     */   }
/*     */   
/*     */   public ItemStack getCostB() {
/* 103 */     return this.costB.<ItemStack>map(ItemCost::itemStack).orElse(ItemStack.EMPTY);
/*     */   }
/*     */   
/*     */   public ItemCost getItemCostA() {
/* 107 */     return this.baseCostA;
/*     */   }
/*     */   
/*     */   public Optional<ItemCost> getItemCostB() {
/* 111 */     return this.costB;
/*     */   }
/*     */   
/*     */   public ItemStack getResult() {
/* 115 */     return this.result;
/*     */   }
/*     */   
/*     */   public void updateDemand() {
/* 119 */     this.demand = this.demand + this.uses - this.maxUses - this.uses;
/*     */   }
/*     */   
/*     */   public ItemStack assemble() {
/* 123 */     return this.result.copy();
/*     */   }
/*     */   
/*     */   public int getUses() {
/* 127 */     return this.uses;
/*     */   }
/*     */   
/*     */   public void resetUses() {
/* 131 */     this.uses = 0;
/*     */   }
/*     */   
/*     */   public int getMaxUses() {
/* 135 */     return this.maxUses;
/*     */   }
/*     */   
/*     */   public void increaseUses() {
/* 139 */     this.uses++;
/*     */   }
/*     */   
/*     */   public int getDemand() {
/* 143 */     return this.demand;
/*     */   }
/*     */   
/*     */   public void addToSpecialPriceDiff(int paramInt) {
/* 147 */     this.specialPriceDiff += paramInt;
/*     */   }
/*     */   
/*     */   public void resetSpecialPriceDiff() {
/* 151 */     this.specialPriceDiff = 0;
/*     */   }
/*     */   
/*     */   public int getSpecialPriceDiff() {
/* 155 */     return this.specialPriceDiff;
/*     */   }
/*     */   
/*     */   public void setSpecialPriceDiff(int paramInt) {
/* 159 */     this.specialPriceDiff = paramInt;
/*     */   }
/*     */   
/*     */   public float getPriceMultiplier() {
/* 163 */     return this.priceMultiplier;
/*     */   }
/*     */   
/*     */   public int getXp() {
/* 167 */     return this.xp;
/*     */   }
/*     */   
/*     */   public boolean isOutOfStock() {
/* 171 */     return (this.uses >= this.maxUses);
/*     */   }
/*     */   
/*     */   public void setToOutOfStock() {
/* 175 */     this.uses = this.maxUses;
/*     */   }
/*     */   
/*     */   public boolean needsRestock() {
/* 179 */     return (this.uses > 0);
/*     */   }
/*     */   
/*     */   public boolean shouldRewardExp() {
/* 183 */     return this.rewardExp;
/*     */   }
/*     */   
/*     */   public boolean satisfiedBy(ItemStack paramItemStack1, ItemStack paramItemStack2) {
/* 187 */     if (!this.baseCostA.test(paramItemStack1) || paramItemStack1.getCount() < getModifiedCostCount(this.baseCostA)) {
/* 188 */       return false;
/*     */     }
/* 190 */     if (this.costB.isPresent()) {
/* 191 */       return (((ItemCost)this.costB.get()).test(paramItemStack2) && paramItemStack2.getCount() >= ((ItemCost)this.costB.get()).count());
/*     */     }
/* 193 */     return paramItemStack2.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean take(ItemStack paramItemStack1, ItemStack paramItemStack2) {
/* 198 */     if (!satisfiedBy(paramItemStack1, paramItemStack2)) {
/* 199 */       return false;
/*     */     }
/*     */     
/* 202 */     paramItemStack1.shrink(getCostA().getCount());
/* 203 */     if (!getCostB().isEmpty()) {
/* 204 */       paramItemStack2.shrink(getCostB().getCount());
/*     */     }
/* 206 */     return true;
/*     */   }
/*     */   
/*     */   public MerchantOffer copy() {
/* 210 */     return new MerchantOffer(this);
/*     */   }
/*     */   
/*     */   private static void writeToStream(RegistryFriendlyByteBuf paramRegistryFriendlyByteBuf, MerchantOffer paramMerchantOffer) {
/* 214 */     ItemCost.STREAM_CODEC.encode(paramRegistryFriendlyByteBuf, paramMerchantOffer.getItemCostA());
/* 215 */     ItemStack.STREAM_CODEC.encode(paramRegistryFriendlyByteBuf, paramMerchantOffer.getResult());
/* 216 */     ItemCost.OPTIONAL_STREAM_CODEC.encode(paramRegistryFriendlyByteBuf, paramMerchantOffer.getItemCostB());
/* 217 */     paramRegistryFriendlyByteBuf.writeBoolean(paramMerchantOffer.isOutOfStock());
/* 218 */     paramRegistryFriendlyByteBuf.writeInt(paramMerchantOffer.getUses());
/* 219 */     paramRegistryFriendlyByteBuf.writeInt(paramMerchantOffer.getMaxUses());
/* 220 */     paramRegistryFriendlyByteBuf.writeInt(paramMerchantOffer.getXp());
/* 221 */     paramRegistryFriendlyByteBuf.writeInt(paramMerchantOffer.getSpecialPriceDiff());
/* 222 */     paramRegistryFriendlyByteBuf.writeFloat(paramMerchantOffer.getPriceMultiplier());
/* 223 */     paramRegistryFriendlyByteBuf.writeInt(paramMerchantOffer.getDemand());
/*     */   }
/*     */   
/*     */   public static MerchantOffer createFromStream(RegistryFriendlyByteBuf paramRegistryFriendlyByteBuf) {
/* 227 */     ItemCost itemCost = (ItemCost)ItemCost.STREAM_CODEC.decode(paramRegistryFriendlyByteBuf);
/* 228 */     ItemStack itemStack = (ItemStack)ItemStack.STREAM_CODEC.decode(paramRegistryFriendlyByteBuf);
/* 229 */     Optional<ItemCost> optional = (Optional)ItemCost.OPTIONAL_STREAM_CODEC.decode(paramRegistryFriendlyByteBuf);
/* 230 */     boolean bool = paramRegistryFriendlyByteBuf.readBoolean();
/* 231 */     int i = paramRegistryFriendlyByteBuf.readInt();
/* 232 */     int j = paramRegistryFriendlyByteBuf.readInt();
/* 233 */     int k = paramRegistryFriendlyByteBuf.readInt();
/* 234 */     int m = paramRegistryFriendlyByteBuf.readInt();
/* 235 */     float f = paramRegistryFriendlyByteBuf.readFloat();
/* 236 */     int n = paramRegistryFriendlyByteBuf.readInt();
/*     */     
/* 238 */     MerchantOffer merchantOffer = new MerchantOffer(itemCost, optional, itemStack, i, j, k, f, n);
/* 239 */     if (bool) {
/* 240 */       merchantOffer.setToOutOfStock();
/*     */     }
/* 242 */     merchantOffer.setSpecialPriceDiff(m);
/* 243 */     return merchantOffer;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\trading\MerchantOffer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */