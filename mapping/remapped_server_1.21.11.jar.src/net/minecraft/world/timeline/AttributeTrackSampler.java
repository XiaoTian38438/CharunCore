/*    */ package net.minecraft.world.timeline;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.function.LongSupplier;
/*    */ import net.minecraft.util.KeyframeTrack;
/*    */ import net.minecraft.util.KeyframeTrackSampler;
/*    */ import net.minecraft.world.attribute.EnvironmentAttributeLayer;
/*    */ import net.minecraft.world.attribute.LerpFunction;
/*    */ import net.minecraft.world.attribute.modifier.AttributeModifier;
/*    */ 
/*    */ 
/*    */ public class AttributeTrackSampler<Value, Argument>
/*    */   implements EnvironmentAttributeLayer.TimeBased<Value>
/*    */ {
/*    */   private final AttributeModifier<Value, Argument> modifier;
/*    */   private final KeyframeTrackSampler<Argument> argumentSampler;
/*    */   private final LongSupplier dayTimeGetter;
/*    */   private int cachedTickId;
/*    */   private Argument cachedArgument;
/*    */   
/*    */   public AttributeTrackSampler(Optional<Integer> paramOptional, AttributeModifier<Value, Argument> paramAttributeModifier, KeyframeTrack<Argument> paramKeyframeTrack, LerpFunction<Argument> paramLerpFunction, LongSupplier paramLongSupplier) {
/* 22 */     this.modifier = paramAttributeModifier;
/* 23 */     this.dayTimeGetter = paramLongSupplier;
/* 24 */     this.argumentSampler = paramKeyframeTrack.bakeSampler(paramOptional, paramLerpFunction);
/*    */   }
/*    */ 
/*    */   
/*    */   public Value applyTimeBased(Value paramValue, int paramInt) {
/* 29 */     if (this.cachedArgument == null || paramInt != this.cachedTickId) {
/* 30 */       this.cachedTickId = paramInt;
/* 31 */       this.cachedArgument = (Argument)this.argumentSampler.sample(this.dayTimeGetter.getAsLong());
/*    */     } 
/* 33 */     return (Value)this.modifier.apply(paramValue, this.cachedArgument);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\timeline\AttributeTrackSampler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */