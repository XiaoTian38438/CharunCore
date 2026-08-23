/*    */ package net.minecraft.util.parsing.packrat;
/*    */ 
/*    */ import com.mojang.brigadier.ImmutableStringReader;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import net.minecraft.util.parsing.packrat.commands.StringReaderTerms;
/*    */ 
/*    */ public interface DelayedException<T extends Exception>
/*    */ {
/*    */   static DelayedException<CommandSyntaxException> create(SimpleCommandExceptionType paramSimpleCommandExceptionType) {
/* 12 */     return (paramString, paramInt) -> paramSimpleCommandExceptionType.createWithContext((ImmutableStringReader)StringReaderTerms.createReader(paramString, paramInt));
/*    */   }
/*    */   
/*    */   static DelayedException<CommandSyntaxException> create(DynamicCommandExceptionType paramDynamicCommandExceptionType, String paramString) {
/* 16 */     return (paramString2, paramInt) -> paramDynamicCommandExceptionType.createWithContext((ImmutableStringReader)StringReaderTerms.createReader(paramString2, paramInt), paramString1);
/*    */   }
/*    */   
/*    */   T create(String paramString, int paramInt);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\parsing\packrat\DelayedException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */