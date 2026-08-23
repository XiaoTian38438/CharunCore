/*    */ package net.minecraft.commands.functions;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*    */ import it.unimi.dsi.fastutil.ints.IntList;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.commands.ExecutionCommandSource;
/*    */ import net.minecraft.commands.execution.UnboundEntryAction;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ 
/*    */ 
/*    */ class FunctionBuilder<T extends ExecutionCommandSource<T>>
/*    */ {
/* 15 */   private List<UnboundEntryAction<T>> plainEntries = new ArrayList<>();
/*    */   private List<MacroFunction.Entry<T>> macroEntries;
/* 17 */   private final List<String> macroArguments = new ArrayList<>();
/*    */   
/*    */   public void addCommand(UnboundEntryAction<T> paramUnboundEntryAction) {
/* 20 */     if (this.macroEntries != null) {
/* 21 */       this.macroEntries.add(new MacroFunction.PlainTextEntry<>(paramUnboundEntryAction));
/*    */     } else {
/* 23 */       this.plainEntries.add(paramUnboundEntryAction);
/*    */     } 
/*    */   }
/*    */   
/*    */   private int getArgumentIndex(String paramString) {
/* 28 */     int i = this.macroArguments.indexOf(paramString);
/* 29 */     if (i == -1) {
/* 30 */       i = this.macroArguments.size();
/* 31 */       this.macroArguments.add(paramString);
/*    */     } 
/* 33 */     return i;
/*    */   }
/*    */   
/*    */   private IntList convertToIndices(List<String> paramList) {
/* 37 */     IntArrayList intArrayList = new IntArrayList(paramList.size());
/* 38 */     for (String str : paramList) {
/* 39 */       intArrayList.add(getArgumentIndex(str));
/*    */     }
/* 41 */     return (IntList)intArrayList;
/*    */   }
/*    */   
/*    */   public void addMacro(String paramString, int paramInt, T paramT) {
/*    */     StringTemplate stringTemplate;
/*    */     try {
/* 47 */       stringTemplate = StringTemplate.fromString(paramString);
/* 48 */     } catch (Exception exception) {
/* 49 */       throw new IllegalArgumentException("Can't parse function line " + paramInt + ": '" + paramString + "'", exception);
/*    */     } 
/*    */     
/* 52 */     if (this.plainEntries != null) {
/* 53 */       this.macroEntries = new ArrayList<>(this.plainEntries.size() + 1);
/* 54 */       for (UnboundEntryAction<T> unboundEntryAction : this.plainEntries) {
/* 55 */         this.macroEntries.add(new MacroFunction.PlainTextEntry<>(unboundEntryAction));
/*    */       }
/* 57 */       this.plainEntries = null;
/*    */     } 
/*    */     
/* 60 */     this.macroEntries.add(new MacroFunction.MacroEntry<>(stringTemplate, convertToIndices(stringTemplate.variables()), paramT));
/*    */   }
/*    */   
/*    */   public CommandFunction<T> build(Identifier paramIdentifier) {
/* 64 */     if (this.macroEntries != null) {
/* 65 */       return new MacroFunction<>(paramIdentifier, this.macroEntries, this.macroArguments);
/*    */     }
/*    */     
/* 68 */     return new PlainTextFunction<>(paramIdentifier, this.plainEntries);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\functions\FunctionBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */