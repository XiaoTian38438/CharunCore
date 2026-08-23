/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class SetItemDamageFunction extends LootItemConditionalFunction {
/* 20 */   private static final Logger LOGGER = LogUtils.getLogger(); public static final MapCodec<SetItemDamageFunction> CODEC;
/*    */   static {
/* 22 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> commonFields(paramInstance).and(paramInstance.group((App)NumberProviders.CODEC.fieldOf("damage").forGetter(()), (App)Codec.BOOL.fieldOf("add").orElse(Boolean.valueOf(false)).forGetter(()))).apply((Applicative)paramInstance, SetItemDamageFunction::new));
/*    */   }
/*    */ 
/*    */   
/*    */   private final NumberProvider damage;
/*    */   
/*    */   private final boolean add;
/*    */   
/*    */   private SetItemDamageFunction(List<LootItemCondition> paramList, NumberProvider paramNumberProvider, boolean paramBoolean) {
/* 31 */     super(paramList);
/* 32 */     this.damage = paramNumberProvider;
/* 33 */     this.add = paramBoolean;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<SetItemDamageFunction> getType() {
/* 38 */     return LootItemFunctions.SET_DAMAGE;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<ContextKey<?>> getReferencedContextParams() {
/* 43 */     return this.damage.getReferencedContextParams();
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack paramItemStack, LootContext paramLootContext) {
/* 48 */     if (paramItemStack.isDamageableItem()) {
/* 49 */       int i = paramItemStack.getMaxDamage();
/* 50 */       float f1 = this.add ? (1.0F - paramItemStack.getDamageValue() / i) : 0.0F;
/* 51 */       float f2 = 1.0F - Mth.clamp(this.damage.getFloat(paramLootContext) + f1, 0.0F, 1.0F);
/* 52 */       paramItemStack.setDamageValue(Mth.floor(f2 * i));
/*    */     } else {
/* 54 */       LOGGER.warn("Couldn't set damage of loot item {}", paramItemStack);
/*    */     } 
/* 56 */     return paramItemStack;
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> setDamage(NumberProvider paramNumberProvider) {
/* 60 */     return simpleBuilder(paramList -> new SetItemDamageFunction(paramList, paramNumberProvider, false));
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> setDamage(NumberProvider paramNumberProvider, boolean paramBoolean) {
/* 64 */     return simpleBuilder(paramList -> new SetItemDamageFunction(paramList, paramNumberProvider, paramBoolean));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\SetItemDamageFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */