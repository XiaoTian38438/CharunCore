/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.storage.loot.LootTable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetContainerLootTable extends LootItemConditionalFunction {
/*    */   static {
/* 21 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> commonFields(paramInstance).and(paramInstance.group((App)LootTable.KEY_CODEC.fieldOf("name").forGetter(()), (App)Codec.LONG.optionalFieldOf("seed", Long.valueOf(0L)).forGetter(()), (App)BuiltInRegistries.BLOCK_ENTITY_TYPE.holderByNameCodec().fieldOf("type").forGetter(()))).apply((Applicative)paramInstance, SetContainerLootTable::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<SetContainerLootTable> CODEC;
/*    */   
/*    */   private final ResourceKey<LootTable> name;
/*    */   private final long seed;
/*    */   private final Holder<BlockEntityType<?>> type;
/*    */   
/*    */   private SetContainerLootTable(List<LootItemCondition> paramList, ResourceKey<LootTable> paramResourceKey, long paramLong, Holder<BlockEntityType<?>> paramHolder) {
/* 32 */     super(paramList);
/* 33 */     this.name = paramResourceKey;
/* 34 */     this.seed = paramLong;
/* 35 */     this.type = paramHolder;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<SetContainerLootTable> getType() {
/* 40 */     return LootItemFunctions.SET_LOOT_TABLE;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack paramItemStack, LootContext paramLootContext) {
/* 45 */     if (paramItemStack.isEmpty()) {
/* 46 */       return paramItemStack;
/*    */     }
/* 48 */     paramItemStack.set(DataComponents.CONTAINER_LOOT, new SeededContainerLoot(this.name, this.seed));
/* 49 */     return paramItemStack;
/*    */   }
/*    */ 
/*    */   
/*    */   public void validate(ValidationContext paramValidationContext) {
/* 54 */     super.validate(paramValidationContext);
/*    */ 
/*    */ 
/*    */     
/* 58 */     if (!paramValidationContext.allowsReferences()) {
/* 59 */       paramValidationContext.reportProblem((ProblemReporter.Problem)new ValidationContext.ReferenceNotAllowedProblem(this.name));
/*    */       
/*    */       return;
/*    */     } 
/* 63 */     if (paramValidationContext.resolver().get(this.name).isEmpty()) {
/* 64 */       paramValidationContext.reportProblem((ProblemReporter.Problem)new ValidationContext.MissingReferenceProblem(this.name));
/*    */     }
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> withLootTable(BlockEntityType<?> paramBlockEntityType, ResourceKey<LootTable> paramResourceKey) {
/* 69 */     return simpleBuilder(paramList -> new SetContainerLootTable(paramList, paramResourceKey, 0L, (Holder<BlockEntityType<?>>)paramBlockEntityType.builtInRegistryHolder()));
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> withLootTable(BlockEntityType<?> paramBlockEntityType, ResourceKey<LootTable> paramResourceKey, long paramLong) {
/* 73 */     return simpleBuilder(paramList -> new SetContainerLootTable(paramList, paramResourceKey, paramLong, (Holder<BlockEntityType<?>>)paramBlockEntityType.builtInRegistryHolder()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\SetContainerLootTable.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */