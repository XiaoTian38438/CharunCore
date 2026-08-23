/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.LootTable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class NestedLootTable extends LootPoolSingletonContainer {
/*    */   static {
/* 22 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)Codec.either(LootTable.KEY_CODEC, LootTable.DIRECT_CODEC).fieldOf("value").forGetter(())).and(singletonFields(paramInstance)).apply((Applicative)paramInstance, NestedLootTable::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<NestedLootTable> CODEC;
/*    */ 
/*    */   
/* 29 */   public static final ProblemReporter.PathElement INLINE_LOOT_TABLE_PATH_ELEMENT = new ProblemReporter.PathElement()
/*    */     {
/*    */       public String get() {
/* 32 */         return "->{inline}";
/*    */       }
/*    */     };
/*    */   
/*    */   private final Either<ResourceKey<LootTable>, LootTable> contents;
/*    */   
/*    */   private NestedLootTable(Either<ResourceKey<LootTable>, LootTable> paramEither, int paramInt1, int paramInt2, List<LootItemCondition> paramList, List<LootItemFunction> paramList1) {
/* 39 */     super(paramInt1, paramInt2, paramList, paramList1);
/* 40 */     this.contents = paramEither;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootPoolEntryType getType() {
/* 45 */     return LootPoolEntries.LOOT_TABLE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void createItemStack(Consumer<ItemStack> paramConsumer, LootContext paramLootContext) {
/* 50 */     ((LootTable)this.contents.map(paramResourceKey -> (LootTable)paramLootContext.getResolver().get(paramResourceKey).map(Holder::value).orElse(LootTable.EMPTY), paramLootTable -> paramLootTable))
/*    */ 
/*    */       
/* 53 */       .getRandomItemsRaw(paramLootContext, paramConsumer);
/*    */   }
/*    */ 
/*    */   
/*    */   public void validate(ValidationContext paramValidationContext) {
/* 58 */     Optional<ResourceKey> optional = this.contents.left();
/* 59 */     if (optional.isPresent()) {
/* 60 */       ResourceKey resourceKey = optional.get();
/* 61 */       if (!paramValidationContext.allowsReferences()) {
/* 62 */         paramValidationContext.reportProblem((ProblemReporter.Problem)new ValidationContext.ReferenceNotAllowedProblem(resourceKey));
/*    */         return;
/*    */       } 
/* 65 */       if (paramValidationContext.hasVisitedElement(resourceKey)) {
/* 66 */         paramValidationContext.reportProblem((ProblemReporter.Problem)new ValidationContext.RecursiveReferenceProblem(resourceKey));
/*    */         
/*    */         return;
/*    */       } 
/*    */     } 
/* 71 */     super.validate(paramValidationContext);
/*    */     
/* 73 */     this.contents
/* 74 */       .ifLeft(paramResourceKey -> paramValidationContext.resolver().get(paramResourceKey).ifPresentOrElse((), ()))
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 80 */       .ifRight(paramLootTable -> paramLootTable.validate(paramValidationContext.forChild(INLINE_LOOT_TABLE_PATH_ELEMENT)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static LootPoolSingletonContainer.Builder<?> lootTableReference(ResourceKey<LootTable> paramResourceKey) {
/* 86 */     return simpleBuilder((paramInt1, paramInt2, paramList1, paramList2) -> new NestedLootTable(Either.left(paramResourceKey), paramInt1, paramInt2, paramList1, paramList2));
/*    */   }
/*    */   
/*    */   public static LootPoolSingletonContainer.Builder<?> inlineLootTable(LootTable paramLootTable) {
/* 90 */     return simpleBuilder((paramInt1, paramInt2, paramList1, paramList2) -> new NestedLootTable(Either.right(paramLootTable), paramInt1, paramInt2, paramList1, paramList2));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\entries\NestedLootTable.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */