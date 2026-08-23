/*    */ package net.minecraft.world.attribute.modifier;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
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
/*    */ @FunctionalInterface
/*    */ public interface RgbModifier
/*    */   extends ColorModifier<Integer>
/*    */ {
/*    */   default Codec<Integer> argumentCodec(EnvironmentAttribute<Integer> paramEnvironmentAttribute) {
/* 39 */     return ExtraCodecs.STRING_RGB_COLOR;
/*    */   }
/*    */ 
/*    */   
/*    */   default LerpFunction<Integer> argumentKeyframeLerp(EnvironmentAttribute<Integer> paramEnvironmentAttribute) {
/* 44 */     return LerpFunction.ofColor();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\attribute\modifier\ColorModifier$RgbModifier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */