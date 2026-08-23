/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.level.block.state.StateHolder;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ interface ValueMatcher
/*    */ {
/*    */   public static final Codec<ValueMatcher> CODEC;
/*    */   public static final StreamCodec<ByteBuf, ValueMatcher> STREAM_CODEC;
/*    */   
/*    */   static {
/* 50 */     CODEC = Codec.either(StatePropertiesPredicate.ExactMatcher.CODEC, StatePropertiesPredicate.RangedMatcher.CODEC).xmap(Either::unwrap, paramValueMatcher -> {
/*    */           if (paramValueMatcher instanceof StatePropertiesPredicate.ExactMatcher) {
/*    */             StatePropertiesPredicate.ExactMatcher exactMatcher = (StatePropertiesPredicate.ExactMatcher)paramValueMatcher;
/*    */             return Either.left(exactMatcher);
/*    */           } 
/*    */           if (paramValueMatcher instanceof StatePropertiesPredicate.RangedMatcher) {
/*    */             StatePropertiesPredicate.RangedMatcher rangedMatcher = (StatePropertiesPredicate.RangedMatcher)paramValueMatcher;
/*    */             return Either.right(rangedMatcher);
/*    */           } 
/*    */           throw new UnsupportedOperationException();
/*    */         });
/* 61 */     STREAM_CODEC = ByteBufCodecs.either(StatePropertiesPredicate.ExactMatcher.STREAM_CODEC, StatePropertiesPredicate.RangedMatcher.STREAM_CODEC).map(Either::unwrap, paramValueMatcher -> {
/*    */           if (paramValueMatcher instanceof StatePropertiesPredicate.ExactMatcher) {
/*    */             StatePropertiesPredicate.ExactMatcher exactMatcher = (StatePropertiesPredicate.ExactMatcher)paramValueMatcher;
/*    */             return Either.left(exactMatcher);
/*    */           } 
/*    */           if (paramValueMatcher instanceof StatePropertiesPredicate.RangedMatcher) {
/*    */             StatePropertiesPredicate.RangedMatcher rangedMatcher = (StatePropertiesPredicate.RangedMatcher)paramValueMatcher;
/*    */             return Either.right(rangedMatcher);
/*    */           } 
/*    */           throw new UnsupportedOperationException();
/*    */         });
/*    */   }
/*    */   
/*    */   <T extends Comparable<T>> boolean match(StateHolder<?, ?> paramStateHolder, Property<T> paramProperty);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\advancements\criterion\StatePropertiesPredicate$ValueMatcher.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */