/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.alchemy.Potion;
/*    */ import net.minecraft.world.item.alchemy.PotionContents;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetPotionFunction extends LootItemConditionalFunction {
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> commonFields(paramInstance).and((App)Potion.CODEC.fieldOf("id").forGetter(())).apply((Applicative)paramInstance, SetPotionFunction::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<SetPotionFunction> CODEC;
/*    */   private final Holder<Potion> potion;
/*    */   
/*    */   private SetPotionFunction(List<LootItemCondition> paramList, Holder<Potion> paramHolder) {
/* 23 */     super(paramList);
/* 24 */     this.potion = paramHolder;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<SetPotionFunction> getType() {
/* 29 */     return LootItemFunctions.SET_POTION;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack paramItemStack, LootContext paramLootContext) {
/* 34 */     paramItemStack.update(DataComponents.POTION_CONTENTS, PotionContents.EMPTY, this.potion, PotionContents::withPotion);
/* 35 */     return paramItemStack;
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> setPotion(Holder<Potion> paramHolder) {
/* 39 */     return simpleBuilder(paramList -> new SetPotionFunction(paramList, paramHolder));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\SetPotionFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */