/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.parsing.packrat.ParseState;
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
/*    */ class null
/*    */   implements SnbtOperations.BuiltinOperation
/*    */ {
/*    */   public <T> T run(DynamicOps<T> paramDynamicOps, List<T> paramList, ParseState<StringReader> paramParseState) {
/* 34 */     Boolean bool = convert(paramDynamicOps, paramList.getFirst());
/* 35 */     if (bool == null) {
/* 36 */       paramParseState.errorCollector().store(paramParseState.mark(), SnbtOperations.ERROR_EXPECTED_NUMBER_OR_BOOLEAN);
/* 37 */       return null;
/*    */     } 
/* 39 */     return (T)paramDynamicOps.createBoolean(bool.booleanValue());
/*    */   }
/*    */   
/*    */   private static <T> Boolean convert(DynamicOps<T> paramDynamicOps, T paramT) {
/* 43 */     Optional<Boolean> optional = paramDynamicOps.getBooleanValue(paramT).result();
/* 44 */     if (optional.isPresent()) {
/* 45 */       return optional.get();
/*    */     }
/* 47 */     Optional<Number> optional1 = paramDynamicOps.getNumberValue(paramT).result();
/* 48 */     if (optional1.isPresent()) {
/* 49 */       return Boolean.valueOf((((Number)optional1.get()).doubleValue() != 0.0D));
/*    */     }
/* 51 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\nbt\SnbtOperations$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */