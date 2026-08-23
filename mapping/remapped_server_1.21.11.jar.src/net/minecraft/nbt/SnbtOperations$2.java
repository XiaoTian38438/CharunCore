/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.UUID;
/*    */ import java.util.stream.IntStream;
/*    */ import net.minecraft.core.UUIDUtil;
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
/*    */     UUID uUID;
/* 57 */     Optional<String> optional = paramDynamicOps.getStringValue(paramList.getFirst()).result();
/* 58 */     if (optional.isEmpty()) {
/* 59 */       paramParseState.errorCollector().store(paramParseState.mark(), SnbtOperations.ERROR_EXPECTED_STRING_UUID);
/* 60 */       return null;
/*    */     } 
/*    */ 
/*    */     
/*    */     try {
/* 65 */       uUID = UUID.fromString(optional.get());
/* 66 */     } catch (IllegalArgumentException illegalArgumentException) {
/* 67 */       paramParseState.errorCollector().store(paramParseState.mark(), SnbtOperations.ERROR_EXPECTED_STRING_UUID);
/* 68 */       return null;
/*    */     } 
/* 70 */     return (T)paramDynamicOps.createIntList(IntStream.of(UUIDUtil.uuidToIntArray(uUID)));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\nbt\SnbtOperations$2.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */