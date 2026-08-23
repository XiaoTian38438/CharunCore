/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.util.context.ContextKey;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.BlockItemStateProperties;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ 
/*     */ public class CopyBlockState extends LootItemConditionalFunction {
/*     */   static {
/*  26 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> commonFields(paramInstance).and(paramInstance.group((App)BuiltInRegistries.BLOCK.holderByNameCodec().fieldOf("block").forGetter(()), (App)Codec.STRING.listOf().fieldOf("properties").forGetter(()))).apply((Applicative)paramInstance, CopyBlockState::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final MapCodec<CopyBlockState> CODEC;
/*     */   private final Holder<Block> block;
/*     */   private final Set<Property<?>> properties;
/*     */   
/*     */   CopyBlockState(List<LootItemCondition> paramList, Holder<Block> paramHolder, Set<Property<?>> paramSet) {
/*  35 */     super(paramList);
/*  36 */     this.block = paramHolder;
/*  37 */     this.properties = paramSet;
/*     */   }
/*     */   
/*     */   private CopyBlockState(List<LootItemCondition> paramList, Holder<Block> paramHolder, List<String> paramList1) {
/*  41 */     this(paramList, paramHolder, (Set<Property<?>>)paramList1.stream()
/*  42 */         .map(((Block)paramHolder.value()).getStateDefinition()::getProperty)
/*  43 */         .filter(Objects::nonNull)
/*  44 */         .collect(Collectors.toSet()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public LootItemFunctionType<CopyBlockState> getType() {
/*  50 */     return LootItemFunctions.COPY_STATE;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<ContextKey<?>> getReferencedContextParams() {
/*  55 */     return Set.of(LootContextParams.BLOCK_STATE);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ItemStack run(ItemStack paramItemStack, LootContext paramLootContext) {
/*  60 */     BlockState blockState = (BlockState)paramLootContext.getOptionalParameter(LootContextParams.BLOCK_STATE);
/*  61 */     if (blockState != null) {
/*  62 */       paramItemStack.update(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY, paramBlockItemStateProperties -> {
/*     */             for (Property<?> property : this.properties) {
/*     */               if (paramBlockState.hasProperty(property)) {
/*     */                 paramBlockItemStateProperties = paramBlockItemStateProperties.with(property, paramBlockState);
/*     */               }
/*     */             } 
/*     */             
/*     */             return paramBlockItemStateProperties;
/*     */           });
/*     */     }
/*  72 */     return paramItemStack;
/*     */   }
/*     */   
/*     */   public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
/*     */     private final Holder<Block> block;
/*  77 */     private final ImmutableSet.Builder<Property<?>> properties = ImmutableSet.builder();
/*     */     
/*     */     Builder(Block param1Block) {
/*  80 */       this.block = (Holder<Block>)param1Block.builtInRegistryHolder();
/*     */     }
/*     */     
/*     */     public Builder copy(Property<?> param1Property) {
/*  84 */       if (!((Block)this.block.value()).getStateDefinition().getProperties().contains(param1Property)) {
/*  85 */         throw new IllegalStateException("Property " + String.valueOf(param1Property) + " is not present on block " + String.valueOf(this.block));
/*     */       }
/*  87 */       this.properties.add(param1Property);
/*  88 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     protected Builder getThis() {
/*  93 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public LootItemFunction build() {
/*  98 */       return new CopyBlockState(getConditions(), this.block, (Set<Property<?>>)this.properties.build());
/*     */     }
/*     */   }
/*     */   
/*     */   public static Builder copyState(Block paramBlock) {
/* 103 */     return new Builder(paramBlock);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\CopyBlockState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */