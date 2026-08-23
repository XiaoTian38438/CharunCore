/*    */ package net.minecraft.world;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.levelgen.RandomSupport;
/*    */ import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
/*    */ 
/*    */ public class RandomSequence {
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)XoroshiroRandomSource.CODEC.fieldOf("source").forGetter(())).apply((Applicative)paramInstance, RandomSequence::new));
/*    */   }
/*    */   
/*    */   public static final Codec<RandomSequence> CODEC;
/*    */   private final XoroshiroRandomSource source;
/*    */   
/*    */   public RandomSequence(XoroshiroRandomSource paramXoroshiroRandomSource) {
/* 20 */     this.source = paramXoroshiroRandomSource;
/*    */   }
/*    */   
/*    */   public RandomSequence(long paramLong, Identifier paramIdentifier) {
/* 24 */     this(createSequence(paramLong, Optional.of(paramIdentifier)));
/*    */   }
/*    */   
/*    */   public RandomSequence(long paramLong, Optional<Identifier> paramOptional) {
/* 28 */     this(createSequence(paramLong, paramOptional));
/*    */   }
/*    */ 
/*    */   
/*    */   private static XoroshiroRandomSource createSequence(long paramLong, Optional<Identifier> paramOptional) {
/* 33 */     RandomSupport.Seed128bit seed128bit = RandomSupport.upgradeSeedTo128bitUnmixed(paramLong);
/* 34 */     if (paramOptional.isPresent()) {
/* 35 */       seed128bit = seed128bit.xor(seedForKey(paramOptional.get()));
/*    */     }
/* 37 */     return new XoroshiroRandomSource(seed128bit.mixed());
/*    */   }
/*    */   
/*    */   public static RandomSupport.Seed128bit seedForKey(Identifier paramIdentifier) {
/* 41 */     return RandomSupport.seedFromHashOf(paramIdentifier.toString());
/*    */   }
/*    */   
/*    */   public RandomSource random() {
/* 45 */     return (RandomSource)this.source;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\RandomSequence.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */