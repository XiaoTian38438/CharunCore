/*    */ package net.minecraft.util.parsing.packrat.commands;
/*    */ 
/*    */ import com.mojang.brigadier.ImmutableStringReader;
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.parsing.packrat.DelayedException;
/*    */ import net.minecraft.util.parsing.packrat.NamedRule;
/*    */ import net.minecraft.util.parsing.packrat.ParseState;
/*    */ import net.minecraft.util.parsing.packrat.Rule;
/*    */ 
/*    */ public abstract class ResourceLookupRule<C, V>
/*    */   implements Rule<StringReader, V>, ResourceSuggestion {
/*    */   private final NamedRule<StringReader, Identifier> idParser;
/*    */   protected final C context;
/*    */   private final DelayedException<CommandSyntaxException> error;
/*    */   
/*    */   protected ResourceLookupRule(NamedRule<StringReader, Identifier> paramNamedRule, C paramC) {
/* 19 */     this.idParser = paramNamedRule;
/* 20 */     this.context = paramC;
/* 21 */     this.error = DelayedException.create(Identifier.ERROR_INVALID);
/*    */   }
/*    */ 
/*    */   
/*    */   public V parse(ParseState<StringReader> paramParseState) {
/* 26 */     ((StringReader)paramParseState.input()).skipWhitespace();
/* 27 */     int i = paramParseState.mark();
/*    */     
/* 29 */     Identifier identifier = (Identifier)paramParseState.parse(this.idParser);
/* 30 */     if (identifier != null) {
/*    */       try {
/* 32 */         return validateElement((ImmutableStringReader)paramParseState.input(), identifier);
/* 33 */       } catch (Exception exception) {
/* 34 */         paramParseState.errorCollector().store(i, this, exception);
/* 35 */         return null;
/*    */       } 
/*    */     }
/*    */     
/* 39 */     paramParseState.errorCollector().store(i, this, this.error);
/* 40 */     return null;
/*    */   }
/*    */   
/*    */   protected abstract V validateElement(ImmutableStringReader paramImmutableStringReader, Identifier paramIdentifier) throws Exception;
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\parsing\packrat\commands\ResourceLookupRule.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */