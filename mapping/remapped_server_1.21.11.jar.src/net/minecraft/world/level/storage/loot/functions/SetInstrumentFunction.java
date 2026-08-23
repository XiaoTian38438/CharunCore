/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.world.item.Instrument;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.InstrumentComponent;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetInstrumentFunction extends LootItemConditionalFunction {
/*    */   static {
/* 20 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> commonFields(paramInstance).and((App)TagKey.hashedCodec(Registries.INSTRUMENT).fieldOf("options").forGetter(())).apply((Applicative)paramInstance, SetInstrumentFunction::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<SetInstrumentFunction> CODEC;
/*    */   private final TagKey<Instrument> options;
/*    */   
/*    */   private SetInstrumentFunction(List<LootItemCondition> paramList, TagKey<Instrument> paramTagKey) {
/* 27 */     super(paramList);
/* 28 */     this.options = paramTagKey;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<SetInstrumentFunction> getType() {
/* 33 */     return LootItemFunctions.SET_INSTRUMENT;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack paramItemStack, LootContext paramLootContext) {
/* 38 */     Registry registry = paramLootContext.getLevel().registryAccess().lookupOrThrow(Registries.INSTRUMENT);
/* 39 */     Optional<Holder> optional = registry.getRandomElementOf(this.options, paramLootContext.getRandom());
/* 40 */     if (optional.isPresent()) {
/* 41 */       paramItemStack.set(DataComponents.INSTRUMENT, new InstrumentComponent(optional.get()));
/*    */     }
/* 43 */     return paramItemStack;
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> setInstrumentOptions(TagKey<Instrument> paramTagKey) {
/* 47 */     return simpleBuilder(paramList -> new SetInstrumentFunction(paramList, paramTagKey));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\SetInstrumentFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */