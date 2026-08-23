/*     */ package net.minecraft.commands.functions;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import it.unimi.dsi.fastutil.ints.IntList;
/*     */ import java.util.List;
/*     */ import net.minecraft.commands.ExecutionCommandSource;
/*     */ import net.minecraft.commands.FunctionInstantiationException;
/*     */ import net.minecraft.commands.execution.UnboundEntryAction;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class MacroEntry<T extends ExecutionCommandSource<T>>
/*     */   implements MacroFunction.Entry<T>
/*     */ {
/*     */   private final StringTemplate template;
/*     */   private final IntList parameters;
/*     */   private final T compilationContext;
/*     */   
/*     */   public MacroEntry(StringTemplate paramStringTemplate, IntList paramIntList, T paramT) {
/* 142 */     this.template = paramStringTemplate;
/* 143 */     this.parameters = paramIntList;
/* 144 */     this.compilationContext = paramT;
/*     */   }
/*     */ 
/*     */   
/*     */   public IntList parameters() {
/* 149 */     return this.parameters;
/*     */   }
/*     */ 
/*     */   
/*     */   public UnboundEntryAction<T> instantiate(List<String> paramList, CommandDispatcher<T> paramCommandDispatcher, Identifier paramIdentifier) throws FunctionInstantiationException {
/* 154 */     String str = this.template.substitute(paramList);
/*     */     try {
/* 156 */       return CommandFunction.parseCommand(paramCommandDispatcher, this.compilationContext, new StringReader(str));
/* 157 */     } catch (CommandSyntaxException commandSyntaxException) {
/* 158 */       throw new FunctionInstantiationException(Component.translatable("commands.function.error.parse", new Object[] { Component.translationArg(paramIdentifier), str, commandSyntaxException.getMessage() }));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\functions\MacroFunction$MacroEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */