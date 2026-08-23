/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.IntRange;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class LimitCount extends LootItemConditionalFunction {
/*    */   static {
/* 15 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> commonFields(paramInstance).and((App)IntRange.CODEC.fieldOf("limit").forGetter(())).apply((Applicative)paramInstance, LimitCount::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<LimitCount> CODEC;
/*    */   private final IntRange limiter;
/*    */   
/*    */   private LimitCount(List<LootItemCondition> paramList, IntRange paramIntRange) {
/* 22 */     super(paramList);
/* 23 */     this.limiter = paramIntRange;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<LimitCount> getType() {
/* 28 */     return LootItemFunctions.LIMIT_COUNT;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<ContextKey<?>> getReferencedContextParams() {
/* 33 */     return this.limiter.getReferencedContextParams();
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack paramItemStack, LootContext paramLootContext) {
/* 38 */     int i = this.limiter.clamp(paramLootContext, paramItemStack.getCount());
/* 39 */     paramItemStack.setCount(i);
/* 40 */     return paramItemStack;
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> limitCount(IntRange paramIntRange) {
/* 44 */     return simpleBuilder(paramList -> new LimitCount(paramList, paramIntRange));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\LimitCount.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */