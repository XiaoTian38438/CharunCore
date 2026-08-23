/*    */ package net.minecraft.world.item.component;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.effect.MobEffects;
/*    */ import net.minecraft.world.item.ItemUseAnimation;
/*    */ import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
/*    */ import net.minecraft.world.item.consume_effects.ClearAllStatusEffectsConsumeEffect;
/*    */ import net.minecraft.world.item.consume_effects.ConsumeEffect;
/*    */ import net.minecraft.world.item.consume_effects.RemoveStatusEffectsConsumeEffect;
/*    */ import net.minecraft.world.item.consume_effects.TeleportRandomlyConsumeEffect;
/*    */ 
/*    */ public class Consumables {
/* 16 */   public static final Consumable DEFAULT_FOOD = defaultFood().build();
/* 17 */   public static final Consumable DEFAULT_DRINK = defaultDrink().build();
/* 18 */   public static final Consumable HONEY_BOTTLE = defaultDrink()
/* 19 */     .consumeSeconds(2.0F)
/* 20 */     .sound((Holder<SoundEvent>)SoundEvents.HONEY_DRINK)
/* 21 */     .onConsume((ConsumeEffect)new RemoveStatusEffectsConsumeEffect(MobEffects.POISON))
/* 22 */     .build();
/* 23 */   public static final Consumable OMINOUS_BOTTLE = defaultDrink()
/* 24 */     .soundAfterConsume((Holder<SoundEvent>)SoundEvents.OMINOUS_BOTTLE_DISPOSE)
/* 25 */     .build();
/* 26 */   public static final Consumable DRIED_KELP = defaultFood().consumeSeconds(0.8F).build();
/* 27 */   public static final Consumable CHICKEN = defaultFood().onConsume((ConsumeEffect)new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.3F)).build();
/* 28 */   public static final Consumable ENCHANTED_GOLDEN_APPLE = defaultFood()
/* 29 */     .onConsume((ConsumeEffect)new ApplyStatusEffectsConsumeEffect(List.of(new MobEffectInstance(MobEffects.REGENERATION, 400, 1), new MobEffectInstance(MobEffects.RESISTANCE, 6000, 0), new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 6000, 0), new MobEffectInstance(MobEffects.ABSORPTION, 2400, 3))))
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 35 */     .build();
/* 36 */   public static final Consumable GOLDEN_APPLE = defaultFood()
/* 37 */     .onConsume((ConsumeEffect)new ApplyStatusEffectsConsumeEffect(List.of(new MobEffectInstance(MobEffects.REGENERATION, 100, 1), new MobEffectInstance(MobEffects.ABSORPTION, 2400, 0))))
/*    */ 
/*    */ 
/*    */     
/* 41 */     .build();
/* 42 */   public static final Consumable POISONOUS_POTATO = defaultFood().onConsume((ConsumeEffect)new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.POISON, 100, 0), 0.6F)).build();
/* 43 */   public static final Consumable PUFFERFISH = defaultFood()
/* 44 */     .onConsume((ConsumeEffect)new ApplyStatusEffectsConsumeEffect(List.of(new MobEffectInstance(MobEffects.POISON, 1200, 1), new MobEffectInstance(MobEffects.HUNGER, 300, 2), new MobEffectInstance(MobEffects.NAUSEA, 300, 0))))
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 49 */     .build();
/* 50 */   public static final Consumable ROTTEN_FLESH = defaultFood().onConsume((ConsumeEffect)new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.8F)).build();
/* 51 */   public static final Consumable SPIDER_EYE = defaultFood().onConsume((ConsumeEffect)new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.POISON, 100, 0))).build();
/* 52 */   public static final Consumable MILK_BUCKET = defaultDrink().onConsume((ConsumeEffect)ClearAllStatusEffectsConsumeEffect.INSTANCE).build();
/* 53 */   public static final Consumable CHORUS_FRUIT = defaultFood().onConsume((ConsumeEffect)new TeleportRandomlyConsumeEffect()).build();
/*    */   
/*    */   public static Consumable.Builder defaultFood() {
/* 56 */     return Consumable.builder()
/* 57 */       .consumeSeconds(1.6F)
/* 58 */       .animation(ItemUseAnimation.EAT)
/* 59 */       .sound((Holder<SoundEvent>)SoundEvents.GENERIC_EAT)
/* 60 */       .hasConsumeParticles(true);
/*    */   }
/*    */   
/*    */   public static Consumable.Builder defaultDrink() {
/* 64 */     return Consumable.builder()
/* 65 */       .consumeSeconds(1.6F)
/* 66 */       .animation(ItemUseAnimation.DRINK)
/* 67 */       .sound((Holder<SoundEvent>)SoundEvents.GENERIC_DRINK)
/* 68 */       .hasConsumeParticles(false);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\component\Consumables.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */