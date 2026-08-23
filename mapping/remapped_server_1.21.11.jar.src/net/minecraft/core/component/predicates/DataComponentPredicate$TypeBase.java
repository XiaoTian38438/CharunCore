/*    */ package net.minecraft.core.component.predicates;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
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
/*    */ public abstract class TypeBase<T extends DataComponentPredicate>
/*    */   implements DataComponentPredicate.Type<T>
/*    */ {
/*    */   private final Codec<T> codec;
/*    */   private final MapCodec<DataComponentPredicate.Single<T>> wrappedCodec;
/*    */   private final StreamCodec<RegistryFriendlyByteBuf, DataComponentPredicate.Single<T>> singleStreamCodec;
/*    */   
/*    */   public TypeBase(Codec<T> paramCodec) {
/* 79 */     this.codec = paramCodec;
/* 80 */     this.wrappedCodec = DataComponentPredicate.Single.wrapCodec(this, paramCodec);
/* 81 */     this.singleStreamCodec = ByteBufCodecs.fromCodecWithRegistries(paramCodec).map(paramDataComponentPredicate -> new DataComponentPredicate.Single<>(this, paramDataComponentPredicate), DataComponentPredicate.Single::predicate);
/*    */   }
/*    */ 
/*    */   
/*    */   public Codec<T> codec() {
/* 86 */     return this.codec;
/*    */   }
/*    */ 
/*    */   
/*    */   public MapCodec<DataComponentPredicate.Single<T>> wrappedCodec() {
/* 91 */     return this.wrappedCodec;
/*    */   }
/*    */ 
/*    */   
/*    */   public StreamCodec<RegistryFriendlyByteBuf, DataComponentPredicate.Single<T>> singleStreamCodec() {
/* 96 */     return this.singleStreamCodec;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\component\predicates\DataComponentPredicate$TypeBase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */