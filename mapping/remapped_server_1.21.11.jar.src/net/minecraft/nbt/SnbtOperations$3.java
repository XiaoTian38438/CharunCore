/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import java.util.Set;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.util.parsing.packrat.ParseState;
/*    */ import net.minecraft.util.parsing.packrat.SuggestionSupplier;
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
/*    */ class null
/*    */   implements SuggestionSupplier<StringReader>
/*    */ {
/* 76 */   private final Set<String> keys = (Set<String>)Stream.concat(
/* 77 */       Stream.of((Object[])new String[] { "false", "true" }, ), SnbtOperations.BUILTIN_OPERATIONS
/* 78 */       .keySet().stream().map(SnbtOperations.BuiltinKey::id))
/* 79 */     .collect(Collectors.toSet());
/*    */ 
/*    */   
/*    */   public Stream<String> possibleValues(ParseState<StringReader> paramParseState) {
/* 83 */     return this.keys.stream();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\nbt\SnbtOperations$3.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */