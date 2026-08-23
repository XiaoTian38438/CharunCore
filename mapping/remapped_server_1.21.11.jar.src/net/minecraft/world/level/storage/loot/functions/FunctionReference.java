/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class FunctionReference extends LootItemConditionalFunction {
/* 19 */   private static final Logger LOGGER = LogUtils.getLogger(); public static final MapCodec<FunctionReference> CODEC;
/*    */   static {
/* 21 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> commonFields(paramInstance).and((App)ResourceKey.codec(Registries.ITEM_MODIFIER).fieldOf("name").forGetter(())).apply((Applicative)paramInstance, FunctionReference::new));
/*    */   }
/*    */ 
/*    */   
/*    */   private final ResourceKey<LootItemFunction> name;
/*    */   
/*    */   private FunctionReference(List<LootItemCondition> paramList, ResourceKey<LootItemFunction> paramResourceKey) {
/* 28 */     super(paramList);
/* 29 */     this.name = paramResourceKey;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<FunctionReference> getType() {
/* 34 */     return LootItemFunctions.REFERENCE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void validate(ValidationContext paramValidationContext) {
/* 39 */     if (!paramValidationContext.allowsReferences()) {
/* 40 */       paramValidationContext.reportProblem((ProblemReporter.Problem)new ValidationContext.ReferenceNotAllowedProblem(this.name));
/*    */       
/*    */       return;
/*    */     } 
/* 44 */     if (paramValidationContext.hasVisitedElement(this.name)) {
/* 45 */       paramValidationContext.reportProblem((ProblemReporter.Problem)new ValidationContext.RecursiveReferenceProblem(this.name));
/*    */       
/*    */       return;
/*    */     } 
/* 49 */     super.validate(paramValidationContext);
/*    */     
/* 51 */     paramValidationContext.resolver().get(this.name).ifPresentOrElse(paramReference -> ((LootItemFunction)paramReference.value()).validate(paramValidationContext.enterElement((ProblemReporter.PathElement)new ProblemReporter.ElementReferencePathElement(this.name), this.name)), () -> paramValidationContext.reportProblem((ProblemReporter.Problem)new ValidationContext.MissingReferenceProblem(this.name)));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected ItemStack run(ItemStack paramItemStack, LootContext paramLootContext) {
/* 59 */     LootItemFunction lootItemFunction = paramLootContext.getResolver().get(this.name).map(Holder::value).orElse(null);
/* 60 */     if (lootItemFunction == null) {
/* 61 */       LOGGER.warn("Unknown function: {}", this.name.identifier());
/* 62 */       return paramItemStack;
/*    */     } 
/* 64 */     LootContext.VisitedEntry visitedEntry = LootContext.createVisitedEntry(lootItemFunction);
/* 65 */     if (paramLootContext.pushVisitedElement(visitedEntry)) {
/*    */       try {
/* 67 */         return lootItemFunction.apply(paramItemStack, paramLootContext);
/*    */       } finally {
/* 69 */         paramLootContext.popVisitedElement(visitedEntry);
/*    */       } 
/*    */     }
/* 72 */     LOGGER.warn("Detected infinite loop in loot tables");
/* 73 */     return paramItemStack;
/*    */   }
/*    */ 
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> functionReference(ResourceKey<LootItemFunction> paramResourceKey) {
/* 78 */     return simpleBuilder(paramList -> new FunctionReference(paramList, paramResourceKey));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\FunctionReference.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */