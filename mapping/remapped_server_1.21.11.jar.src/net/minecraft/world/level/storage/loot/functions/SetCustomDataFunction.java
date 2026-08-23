/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.TagParser;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.CustomData;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetCustomDataFunction extends LootItemConditionalFunction {
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> commonFields(paramInstance).and((App)TagParser.LENIENT_CODEC.fieldOf("tag").forGetter(())).apply((Applicative)paramInstance, SetCustomDataFunction::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<SetCustomDataFunction> CODEC;
/*    */   private final CompoundTag tag;
/*    */   
/*    */   private SetCustomDataFunction(List<LootItemCondition> paramList, CompoundTag paramCompoundTag) {
/* 23 */     super(paramList);
/* 24 */     this.tag = paramCompoundTag;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<SetCustomDataFunction> getType() {
/* 29 */     return LootItemFunctions.SET_CUSTOM_DATA;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack paramItemStack, LootContext paramLootContext) {
/* 34 */     CustomData.update(DataComponents.CUSTOM_DATA, paramItemStack, paramCompoundTag -> paramCompoundTag.merge(this.tag));
/* 35 */     return paramItemStack;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public static LootItemConditionalFunction.Builder<?> setCustomData(CompoundTag paramCompoundTag) {
/* 43 */     return simpleBuilder(paramList -> new SetCustomDataFunction(paramList, paramCompoundTag));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\SetCustomDataFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */