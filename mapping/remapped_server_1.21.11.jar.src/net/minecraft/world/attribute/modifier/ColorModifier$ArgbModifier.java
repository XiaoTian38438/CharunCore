/*    */ package net.minecraft.world.attribute.modifier;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.attribute.EnvironmentAttribute;
/*    */ import net.minecraft.world.attribute.LerpFunction;
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
/*    */ @FunctionalInterface
/*    */ public interface ArgbModifier
/*    */   extends ColorModifier<Integer>
/*    */ {
/*    */   default Codec<Integer> argumentCodec(EnvironmentAttribute<Integer> paramEnvironmentAttribute) {
/* 52 */     return Codec.either(ExtraCodecs.STRING_ARGB_COLOR, ExtraCodecs.RGB_COLOR_CODEC).xmap(Either::unwrap, paramInteger -> (ARGB.alpha(paramInteger.intValue()) == 255) ? Either.right(paramInteger) : Either.left(paramInteger));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default LerpFunction<Integer> argumentKeyframeLerp(EnvironmentAttribute<Integer> paramEnvironmentAttribute) {
/* 60 */     return LerpFunction.ofColor();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\attribute\modifier\ColorModifier$ArgbModifier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */