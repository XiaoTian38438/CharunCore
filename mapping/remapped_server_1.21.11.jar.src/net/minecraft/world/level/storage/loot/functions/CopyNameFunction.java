/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.Nameable;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.LootContextArg;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class CopyNameFunction extends LootItemConditionalFunction {
/*    */   static {
/* 17 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> commonFields(paramInstance).and((App)LootContextArg.ENTITY_OR_BLOCK.fieldOf("source").forGetter(())).apply((Applicative)paramInstance, CopyNameFunction::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<CopyNameFunction> CODEC;
/*    */   private final LootContextArg<Object> source;
/*    */   
/*    */   private CopyNameFunction(List<LootItemCondition> paramList, LootContextArg<?> paramLootContextArg) {
/* 24 */     super(paramList);
/* 25 */     this.source = LootContextArg.cast(paramLootContextArg);
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<CopyNameFunction> getType() {
/* 30 */     return LootItemFunctions.COPY_NAME;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<ContextKey<?>> getReferencedContextParams() {
/* 35 */     return Set.of(this.source.contextParam());
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack paramItemStack, LootContext paramLootContext) {
/* 40 */     Object object = this.source.get(paramLootContext);
/*    */     
/* 42 */     if (object instanceof Nameable) { Nameable nameable = (Nameable)object;
/* 43 */       paramItemStack.set(DataComponents.CUSTOM_NAME, nameable.getCustomName()); }
/*    */     
/* 45 */     return paramItemStack;
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> copyName(LootContextArg<?> paramLootContextArg) {
/* 49 */     return simpleBuilder(paramList -> new CopyNameFunction(paramList, paramLootContextArg));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\CopyNameFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */