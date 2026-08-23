/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class ApplyExplosionDecay extends LootItemConditionalFunction {
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> commonFields(paramInstance).apply((Applicative)paramInstance, ApplyExplosionDecay::new));
/*    */   } public static final MapCodec<ApplyExplosionDecay> CODEC;
/*    */   private ApplyExplosionDecay(List<LootItemCondition> paramList) {
/* 17 */     super(paramList);
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<ApplyExplosionDecay> getType() {
/* 22 */     return LootItemFunctions.EXPLOSION_DECAY;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack paramItemStack, LootContext paramLootContext) {
/* 27 */     Float float_ = (Float)paramLootContext.getOptionalParameter(LootContextParams.EXPLOSION_RADIUS);
/*    */     
/* 29 */     if (float_ != null) {
/* 30 */       RandomSource randomSource = paramLootContext.getRandom();
/*    */       
/* 32 */       float f = 1.0F / float_.floatValue();
/* 33 */       int i = paramItemStack.getCount();
/* 34 */       byte b1 = 0;
/* 35 */       for (byte b2 = 0; b2 < i; b2++) {
/* 36 */         if (randomSource.nextFloat() <= f) {
/* 37 */           b1++;
/*    */         }
/*    */       } 
/*    */       
/* 41 */       paramItemStack.setCount(b1);
/*    */     } 
/* 43 */     return paramItemStack;
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> explosionDecay() {
/* 47 */     return simpleBuilder(ApplyExplosionDecay::new);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\ApplyExplosionDecay.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */