/*    */ package net.minecraft.world.level.storage.loot;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.UnaryOperator;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ 
/*    */ public interface LootContextArg<R>
/*    */ {
/*    */   static {
/* 16 */     ENTITY_OR_BLOCK = createArgCodec(paramArgCodecBuilder -> paramArgCodecBuilder.anyOf((Object[])LootContext.EntityTarget.values()).anyOf((Object[])LootContext.BlockEntityTarget.values()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static final Codec<LootContextArg<Object>> ENTITY_OR_BLOCK;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static <U> LootContextArg<U> cast(LootContextArg<? extends U> paramLootContextArg) {
/* 28 */     return (LootContextArg)paramLootContextArg;
/*    */   }
/*    */   
/*    */   static <R> Codec<LootContextArg<R>> createArgCodec(UnaryOperator<ArgCodecBuilder<R>> paramUnaryOperator) {
/* 32 */     return ((ArgCodecBuilder<R>)paramUnaryOperator.apply(new ArgCodecBuilder<>())).build();
/*    */   }
/*    */   
/*    */   R get(LootContext paramLootContext);
/*    */   
/*    */   ContextKey<?> contextParam();
/*    */   
/*    */   public static interface Getter<T, R> extends LootContextArg<R> {
/*    */     R get(T param1T);
/*    */     
/*    */     default R get(LootContext param1LootContext) {
/* 43 */       T t = (T)param1LootContext.getOptionalParameter((ContextKey)contextParam());
/* 44 */       return (t != null) ? get(t) : null;
/*    */     }
/*    */     
/*    */     ContextKey<? extends T> contextParam();
/*    */   }
/*    */   
/*    */   public static interface SimpleGetter<T> extends LootContextArg<T> {
/*    */     ContextKey<? extends T> contextParam();
/*    */     
/*    */     default T get(LootContext param1LootContext) {
/* 54 */       return param1LootContext.getOptionalParameter((ContextKey)contextParam());
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class ArgCodecBuilder<R> {
/* 59 */     private final ExtraCodecs.LateBoundIdMapper<String, LootContextArg<R>> sources = new ExtraCodecs.LateBoundIdMapper();
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public <T> ArgCodecBuilder<R> anyOf(T[] param1ArrayOfT, Function<T, String> param1Function, Function<T, ? extends LootContextArg<R>> param1Function1) {
/* 65 */       for (T t : param1ArrayOfT) {
/* 66 */         this.sources.put(param1Function.apply(t), param1Function1.apply(t));
/*    */       }
/* 68 */       return this;
/*    */     }
/*    */     
/*    */     public <T extends StringRepresentable> ArgCodecBuilder<R> anyOf(T[] param1ArrayOfT, Function<T, ? extends LootContextArg<R>> param1Function) {
/* 72 */       return anyOf(param1ArrayOfT, StringRepresentable::getSerializedName, param1Function);
/*    */     }
/*    */     
/*    */     public <T extends StringRepresentable & LootContextArg<? extends R>> ArgCodecBuilder<R> anyOf(T[] param1ArrayOfT) {
/* 76 */       return anyOf((StringRepresentable[])param1ArrayOfT, param1Object -> LootContextArg.cast((LootContextArg)param1Object));
/*    */     }
/*    */     
/*    */     public ArgCodecBuilder<R> anyEntity(Function<? super ContextKey<? extends Entity>, ? extends LootContextArg<R>> param1Function) {
/* 80 */       return anyOf(LootContext.EntityTarget.values(), param1EntityTarget -> (LootContextArg)param1Function.apply(param1EntityTarget.contextParam()));
/*    */     }
/*    */     
/*    */     public ArgCodecBuilder<R> anyBlockEntity(Function<? super ContextKey<? extends BlockEntity>, ? extends LootContextArg<R>> param1Function) {
/* 84 */       return anyOf(LootContext.BlockEntityTarget.values(), param1BlockEntityTarget -> (LootContextArg)param1Function.apply(param1BlockEntityTarget.contextParam()));
/*    */     }
/*    */     
/*    */     public ArgCodecBuilder<R> anyItemStack(Function<? super ContextKey<? extends ItemStack>, ? extends LootContextArg<R>> param1Function) {
/* 88 */       return anyOf(LootContext.ItemStackTarget.values(), param1ItemStackTarget -> (LootContextArg)param1Function.apply(param1ItemStackTarget.contextParam()));
/*    */     }
/*    */     
/*    */     Codec<LootContextArg<R>> build() {
/* 92 */       return this.sources.codec((Codec)Codec.STRING);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\LootContextArg.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */