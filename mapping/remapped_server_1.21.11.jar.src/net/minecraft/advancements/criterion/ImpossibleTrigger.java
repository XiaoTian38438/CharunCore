/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.advancements.CriterionTrigger;
/*    */ import net.minecraft.advancements.CriterionTriggerInstance;
/*    */ import net.minecraft.server.PlayerAdvancements;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ImpossibleTrigger
/*    */   implements CriterionTrigger<ImpossibleTrigger.TriggerInstance>
/*    */ {
/*    */   public void addPlayerListener(PlayerAdvancements paramPlayerAdvancements, CriterionTrigger.Listener<TriggerInstance> paramListener) {}
/*    */   
/*    */   public void removePlayerListener(PlayerAdvancements paramPlayerAdvancements, CriterionTrigger.Listener<TriggerInstance> paramListener) {}
/*    */   
/*    */   public void removePlayerListeners(PlayerAdvancements paramPlayerAdvancements) {}
/*    */   
/*    */   public Codec<TriggerInstance> codec() {
/* 24 */     return TriggerInstance.CODEC;
/*    */   }
/*    */   
/*    */   public static final class TriggerInstance extends Record implements CriterionTriggerInstance {
/* 28 */     public static final Codec<TriggerInstance> CODEC = MapCodec.unitCodec(new TriggerInstance());
/*    */     
/*    */     public final boolean equals(Object param1Object) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/ImpossibleTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #27	-> 0
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/ImpossibleTrigger$TriggerInstance;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #27	-> 0
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/ImpossibleTrigger$TriggerInstance;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #27	-> 0
/*    */     }
/*    */     
/*    */     public void validate(CriterionValidator param1CriterionValidator) {}
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\advancements\criterion\ImpossibleTrigger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */