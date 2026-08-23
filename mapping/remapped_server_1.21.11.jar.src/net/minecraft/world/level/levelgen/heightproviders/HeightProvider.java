/*    */ package net.minecraft.world.level.levelgen.heightproviders;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*    */ import net.minecraft.world.level.levelgen.WorldGenerationContext;
/*    */ 
/*    */ public abstract class HeightProvider {
/* 11 */   private static final Codec<Either<VerticalAnchor, HeightProvider>> CONSTANT_OR_DISPATCH_CODEC = Codec.either(VerticalAnchor.CODEC, BuiltInRegistries.HEIGHT_PROVIDER_TYPE
/*    */       
/* 13 */       .byNameCodec().dispatch(HeightProvider::getType, HeightProviderType::codec));
/*    */   static {
/* 15 */     CODEC = CONSTANT_OR_DISPATCH_CODEC.xmap(paramEither -> (HeightProvider)paramEither.map(ConstantHeight::of, ()), paramHeightProvider -> (paramHeightProvider.getType() == HeightProviderType.CONSTANT) ? Either.left(((ConstantHeight)paramHeightProvider).getValue()) : Either.right(paramHeightProvider));
/*    */   }
/*    */   
/*    */   public static final Codec<HeightProvider> CODEC;
/*    */   
/*    */   public abstract HeightProviderType<?> getType();
/*    */   
/*    */   public abstract int sample(RandomSource paramRandomSource, WorldGenerationContext paramWorldGenerationContext);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\heightproviders\HeightProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */