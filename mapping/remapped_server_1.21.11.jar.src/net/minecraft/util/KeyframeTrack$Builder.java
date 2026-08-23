/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Builder<T>
/*    */ {
/* 72 */   private final ImmutableList.Builder<Keyframe<T>> keyframes = ImmutableList.builder();
/* 73 */   private EasingType easing = EasingType.LINEAR;
/*    */   
/*    */   public Builder<T> addKeyframe(int paramInt, T paramT) {
/* 76 */     this.keyframes.add(new Keyframe<>(paramInt, paramT));
/* 77 */     return this;
/*    */   }
/*    */   
/*    */   public Builder<T> setEasing(EasingType paramEasingType) {
/* 81 */     this.easing = paramEasingType;
/* 82 */     return this;
/*    */   }
/*    */   
/*    */   public KeyframeTrack<T> build() {
/* 86 */     List<Keyframe<T>> list = (List)KeyframeTrack.<T>validateKeyframes((List<Keyframe<T>>)this.keyframes.build()).getOrThrow();
/* 87 */     return new KeyframeTrack<>(list, this.easing);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\KeyframeTrack$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */