/*    */ package net.minecraft.world.attribute.modifier;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
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
/*    */ @FunctionalInterface
/*    */ public interface Simple
/*    */   extends FloatModifier<Float>
/*    */ {
/*    */   default Codec<Float> argumentCodec(EnvironmentAttribute<Float> paramEnvironmentAttribute) {
/* 39 */     return (Codec<Float>)Codec.FLOAT;
/*    */   }
/*    */ 
/*    */   
/*    */   default LerpFunction<Float> argumentKeyframeLerp(EnvironmentAttribute<Float> paramEnvironmentAttribute) {
/* 44 */     return LerpFunction.ofFloat();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\attribute\modifier\FloatModifier$Simple.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */