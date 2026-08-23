/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.CustomModelData;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*    */ 
/*    */ public class SetCustomModelDataFunction extends LootItemConditionalFunction {
/* 25 */   private static final Codec<NumberProvider> COLOR_PROVIDER_CODEC = Codec.withAlternative(NumberProviders.CODEC, ExtraCodecs.RGB_COLOR_CODEC, net.minecraft.world.level.storage.loot.providers.number.ConstantValue::new);
/*    */   public static final MapCodec<SetCustomModelDataFunction> CODEC;
/*    */   private final Optional<ListOperation.StandAlone<NumberProvider>> floats;
/*    */   
/*    */   static {
/* 30 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> commonFields(paramInstance).and(paramInstance.group((App)ListOperation.StandAlone.<T>codec(NumberProviders.CODEC, 2147483647).optionalFieldOf("floats").forGetter(()), (App)ListOperation.StandAlone.<T>codec((Codec<T>)Codec.BOOL, 2147483647).optionalFieldOf("flags").forGetter(()), (App)ListOperation.StandAlone.<T>codec((Codec<T>)Codec.STRING, 2147483647).optionalFieldOf("strings").forGetter(()), (App)ListOperation.StandAlone.<T>codec((Codec)COLOR_PROVIDER_CODEC, 2147483647).optionalFieldOf("colors").forGetter(()))).apply((Applicative)paramInstance, SetCustomModelDataFunction::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private final Optional<ListOperation.StandAlone<Boolean>> flags;
/*    */   
/*    */   private final Optional<ListOperation.StandAlone<String>> strings;
/*    */   
/*    */   private final Optional<ListOperation.StandAlone<NumberProvider>> colors;
/*    */ 
/*    */   
/*    */   public SetCustomModelDataFunction(List<LootItemCondition> paramList, Optional<ListOperation.StandAlone<NumberProvider>> paramOptional1, Optional<ListOperation.StandAlone<Boolean>> paramOptional, Optional<ListOperation.StandAlone<String>> paramOptional2, Optional<ListOperation.StandAlone<NumberProvider>> paramOptional3) {
/* 43 */     super(paramList);
/* 44 */     this.floats = paramOptional1;
/* 45 */     this.flags = paramOptional;
/* 46 */     this.strings = paramOptional2;
/* 47 */     this.colors = paramOptional3;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<ContextKey<?>> getReferencedContextParams() {
/* 52 */     return (Set<ContextKey<?>>)Stream.concat(this.floats
/* 53 */         .stream(), this.colors
/* 54 */         .stream())
/*    */       
/* 56 */       .flatMap(paramStandAlone -> paramStandAlone.value().stream())
/* 57 */       .flatMap(paramNumberProvider -> paramNumberProvider.getReferencedContextParams().stream())
/* 58 */       .collect(Collectors.toSet());
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<SetCustomModelDataFunction> getType() {
/* 63 */     return LootItemFunctions.SET_CUSTOM_MODEL_DATA;
/*    */   }
/*    */   
/*    */   private static <T> List<T> apply(Optional<ListOperation.StandAlone<T>> paramOptional, List<T> paramList) {
/* 67 */     return paramOptional.<List<T>>map(paramStandAlone -> paramStandAlone.apply(paramList)).orElse(paramList);
/*    */   }
/*    */   
/*    */   private static <T, E> List<E> apply(Optional<ListOperation.StandAlone<T>> paramOptional, List<E> paramList, Function<T, E> paramFunction) {
/* 71 */     return paramOptional.<List<E>>map(paramStandAlone -> {
/*    */           List<?> list = paramStandAlone.value().stream().map(paramFunction).toList();
/*    */           return paramStandAlone.operation().apply(paramList, list);
/* 74 */         }).orElse(paramList);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack paramItemStack, LootContext paramLootContext) {
/* 79 */     CustomModelData customModelData = (CustomModelData)paramItemStack.getOrDefault(DataComponents.CUSTOM_MODEL_DATA, CustomModelData.EMPTY);
/*    */     
/* 81 */     paramItemStack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(
/* 82 */           apply(this.floats, customModelData.floats(), paramNumberProvider -> Float.valueOf(paramNumberProvider.getFloat(paramLootContext))), 
/* 83 */           apply(this.flags, customModelData.flags()), 
/* 84 */           apply(this.strings, customModelData.strings()), 
/* 85 */           apply(this.colors, customModelData.colors(), paramNumberProvider -> Integer.valueOf(paramNumberProvider.getInt(paramLootContext)))));
/*    */     
/* 87 */     return paramItemStack;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\SetCustomModelDataFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */