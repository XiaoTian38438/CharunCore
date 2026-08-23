/*     */ package net.minecraft.world.item.component;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.item.ItemUseAnimation;
/*     */ import net.minecraft.world.item.consume_effects.ConsumeEffect;
/*     */ import net.minecraft.world.item.consume_effects.PlaySoundConsumeEffect;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Builder
/*     */ {
/* 137 */   private float consumeSeconds = 1.6F;
/* 138 */   private ItemUseAnimation animation = ItemUseAnimation.EAT;
/* 139 */   private Holder<SoundEvent> sound = (Holder<SoundEvent>)SoundEvents.GENERIC_EAT;
/*     */   private boolean hasConsumeParticles = true;
/* 141 */   private final List<ConsumeEffect> onConsumeEffects = new ArrayList<>();
/*     */ 
/*     */ 
/*     */   
/*     */   public Builder consumeSeconds(float paramFloat) {
/* 146 */     this.consumeSeconds = paramFloat;
/* 147 */     return this;
/*     */   }
/*     */   
/*     */   public Builder animation(ItemUseAnimation paramItemUseAnimation) {
/* 151 */     this.animation = paramItemUseAnimation;
/* 152 */     return this;
/*     */   }
/*     */   
/*     */   public Builder sound(Holder<SoundEvent> paramHolder) {
/* 156 */     this.sound = paramHolder;
/* 157 */     return this;
/*     */   }
/*     */   
/*     */   public Builder soundAfterConsume(Holder<SoundEvent> paramHolder) {
/* 161 */     return onConsume((ConsumeEffect)new PlaySoundConsumeEffect(paramHolder));
/*     */   }
/*     */   
/*     */   public Builder hasConsumeParticles(boolean paramBoolean) {
/* 165 */     this.hasConsumeParticles = paramBoolean;
/* 166 */     return this;
/*     */   }
/*     */   
/*     */   public Builder onConsume(ConsumeEffect paramConsumeEffect) {
/* 170 */     this.onConsumeEffects.add(paramConsumeEffect);
/* 171 */     return this;
/*     */   }
/*     */   
/*     */   public Consumable build() {
/* 175 */     return new Consumable(this.consumeSeconds, this.animation, this.sound, this.hasConsumeParticles, this.onConsumeEffects);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\component\Consumable$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */