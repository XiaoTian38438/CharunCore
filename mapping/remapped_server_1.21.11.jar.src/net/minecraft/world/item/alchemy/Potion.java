/*    */ package net.minecraft.world.item.alchemy;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.effect.MobEffect;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.flag.FeatureElement;
/*    */ import net.minecraft.world.flag.FeatureFlag;
/*    */ import net.minecraft.world.flag.FeatureFlagSet;
/*    */ import net.minecraft.world.flag.FeatureFlags;
/*    */ 
/*    */ public class Potion implements FeatureElement {
/* 19 */   public static final Codec<Holder<Potion>> CODEC = BuiltInRegistries.POTION.holderByNameCodec();
/* 20 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Potion>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.POTION);
/*    */   
/*    */   private final String name;
/*    */   
/*    */   private final List<MobEffectInstance> effects;
/* 25 */   private FeatureFlagSet requiredFeatures = FeatureFlags.VANILLA_SET;
/*    */   
/*    */   public Potion(String paramString, MobEffectInstance... paramVarArgs) {
/* 28 */     this.name = paramString;
/* 29 */     this.effects = List.of(paramVarArgs);
/*    */   }
/*    */   
/*    */   public Potion requiredFeatures(FeatureFlag... paramVarArgs) {
/* 33 */     this.requiredFeatures = FeatureFlags.REGISTRY.subset(paramVarArgs);
/* 34 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public FeatureFlagSet requiredFeatures() {
/* 39 */     return this.requiredFeatures;
/*    */   }
/*    */   
/*    */   public List<MobEffectInstance> getEffects() {
/* 43 */     return this.effects;
/*    */   }
/*    */   
/*    */   public String name() {
/* 47 */     return this.name;
/*    */   }
/*    */   
/*    */   public boolean hasInstantEffects() {
/* 51 */     for (MobEffectInstance mobEffectInstance : this.effects) {
/* 52 */       if (((MobEffect)mobEffectInstance.getEffect().value()).isInstantenous()) {
/* 53 */         return true;
/*    */       }
/*    */     } 
/* 56 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\alchemy\Potion.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */