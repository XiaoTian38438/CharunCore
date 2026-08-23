/*    */ package net.minecraft.world.level.storage.loot;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
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
/*    */ public final class ArgCodecBuilder<R>
/*    */ {
/* 59 */   private final ExtraCodecs.LateBoundIdMapper<String, LootContextArg<R>> sources = new ExtraCodecs.LateBoundIdMapper();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <T> ArgCodecBuilder<R> anyOf(T[] paramArrayOfT, Function<T, String> paramFunction, Function<T, ? extends LootContextArg<R>> paramFunction1) {
/* 65 */     for (T t : paramArrayOfT) {
/* 66 */       this.sources.put(paramFunction.apply(t), paramFunction1.apply(t));
/*    */     }
/* 68 */     return this;
/*    */   }
/*    */   
/*    */   public <T extends StringRepresentable> ArgCodecBuilder<R> anyOf(T[] paramArrayOfT, Function<T, ? extends LootContextArg<R>> paramFunction) {
/* 72 */     return anyOf(paramArrayOfT, StringRepresentable::getSerializedName, paramFunction);
/*    */   }
/*    */   
/*    */   public <T extends StringRepresentable & LootContextArg<? extends R>> ArgCodecBuilder<R> anyOf(T[] paramArrayOfT) {
/* 76 */     return anyOf((StringRepresentable[])paramArrayOfT, paramObject -> LootContextArg.cast((LootContextArg)paramObject));
/*    */   }
/*    */   
/*    */   public ArgCodecBuilder<R> anyEntity(Function<? super ContextKey<? extends Entity>, ? extends LootContextArg<R>> paramFunction) {
/* 80 */     return anyOf(LootContext.EntityTarget.values(), paramEntityTarget -> (LootContextArg)paramFunction.apply(paramEntityTarget.contextParam()));
/*    */   }
/*    */   
/*    */   public ArgCodecBuilder<R> anyBlockEntity(Function<? super ContextKey<? extends BlockEntity>, ? extends LootContextArg<R>> paramFunction) {
/* 84 */     return anyOf(LootContext.BlockEntityTarget.values(), paramBlockEntityTarget -> (LootContextArg)paramFunction.apply(paramBlockEntityTarget.contextParam()));
/*    */   }
/*    */   
/*    */   public ArgCodecBuilder<R> anyItemStack(Function<? super ContextKey<? extends ItemStack>, ? extends LootContextArg<R>> paramFunction) {
/* 88 */     return anyOf(LootContext.ItemStackTarget.values(), paramItemStackTarget -> (LootContextArg)paramFunction.apply(paramItemStackTarget.contextParam()));
/*    */   }
/*    */   
/*    */   Codec<LootContextArg<R>> build() {
/* 92 */     return this.sources.codec((Codec)Codec.STRING);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\LootContextArg$ArgCodecBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */