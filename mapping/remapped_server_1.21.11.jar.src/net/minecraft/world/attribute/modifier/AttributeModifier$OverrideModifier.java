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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class OverrideModifier<Value>
/*    */   extends Record
/*    */   implements AttributeModifier<Value, Value>
/*    */ {
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/attribute/modifier/AttributeModifier$OverrideModifier;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #53	-> 0
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/attribute/modifier/AttributeModifier$OverrideModifier;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #53	-> 0
/*    */   }
/*    */   
/*    */   public final boolean equals(Object paramObject) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/attribute/modifier/AttributeModifier$OverrideModifier;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #53	-> 0
/*    */   }
/*    */   
/* 54 */   static final OverrideModifier<?> INSTANCE = new OverrideModifier();
/*    */ 
/*    */   
/*    */   public Value apply(Value paramValue1, Value paramValue2) {
/* 58 */     return paramValue2;
/*    */   }
/*    */ 
/*    */   
/*    */   public Codec<Value> argumentCodec(EnvironmentAttribute<Value> paramEnvironmentAttribute) {
/* 63 */     return paramEnvironmentAttribute.valueCodec();
/*    */   }
/*    */ 
/*    */   
/*    */   public LerpFunction<Value> argumentKeyframeLerp(EnvironmentAttribute<Value> paramEnvironmentAttribute) {
/* 68 */     return paramEnvironmentAttribute.type().keyframeLerp();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\attribute\modifier\AttributeModifier$OverrideModifier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */