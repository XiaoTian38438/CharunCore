/*     */ package net.minecraft.commands.functions;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.ParseResults;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.context.ContextChain;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.ExecutionCommandSource;
/*     */ import net.minecraft.commands.FunctionInstantiationException;
/*     */ import net.minecraft.commands.execution.UnboundEntryAction;
/*     */ import net.minecraft.commands.execution.tasks.BuildContexts;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface CommandFunction<T>
/*     */ {
/*     */   Identifier id();
/*     */   
/*     */   InstantiatedFunction<T> instantiate(CompoundTag paramCompoundTag, CommandDispatcher<T> paramCommandDispatcher) throws FunctionInstantiationException;
/*     */   
/*     */   private static boolean shouldConcatenateNextLine(CharSequence paramCharSequence) {
/*  28 */     int i = paramCharSequence.length();
/*  29 */     return (i > 0 && paramCharSequence.charAt(i - 1) == '\\');
/*     */   }
/*     */   
/*     */   static <T extends ExecutionCommandSource<T>> CommandFunction<T> fromLines(Identifier paramIdentifier, CommandDispatcher<T> paramCommandDispatcher, T paramT, List<String> paramList) {
/*  33 */     FunctionBuilder<ExecutionCommandSource> functionBuilder = new FunctionBuilder<>();
/*  34 */     byte b = 0; while (true) { if (b < paramList.size())
/*  35 */       { int i = b + 1;
/*     */ 
/*     */         
/*  38 */         String str = ((String)paramList.get(b)).trim();
/*     */         
/*  40 */         if (shouldConcatenateNextLine(str))
/*  41 */         { StringBuilder stringBuilder = new StringBuilder(str);
/*     */           for (;; b++)
/*  43 */           { b++;
/*  44 */             if (b == paramList.size()) {
/*  45 */               throw new IllegalArgumentException("Line continuation at end of file");
/*     */             }
/*  47 */             stringBuilder.deleteCharAt(stringBuilder.length() - 1);
/*  48 */             String str1 = ((String)paramList.get(b)).trim();
/*  49 */             stringBuilder.append(str1);
/*  50 */             checkCommandLineLength(stringBuilder);
/*  51 */             if (!shouldConcatenateNextLine(stringBuilder))
/*  52 */             { String str2 = stringBuilder.toString();
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*  57 */               checkCommandLineLength(str2);
/*     */               
/*  59 */               StringReader stringReader = new StringReader(str2);
/*     */               
/*  61 */               if (!stringReader.canRead() || stringReader.peek() == '#') {
/*     */                 break;
/*     */               }
/*     */               
/*  65 */               if (stringReader.peek() == '/') {
/*  66 */                 stringReader.skip();
/*  67 */                 if (stringReader.peek() == '/') {
/*  68 */                   throw new IllegalArgumentException("Unknown or invalid command '" + str2 + "' on line " + i + " (if you intended to make a comment, use '#' not '//')");
/*     */                 }
/*  70 */                 str1 = stringReader.readUnquotedString();
/*  71 */                 throw new IllegalArgumentException("Unknown or invalid command '" + str2 + "' on line " + i + " (did you mean '" + str1 + "'? Do not use a preceding forwards slash.)");
/*     */               } 
/*  73 */               if (stringReader.peek() == '$')
/*     */               
/*  75 */               { functionBuilder.addMacro(str2.substring(1), i, (ExecutionCommandSource)paramT); }
/*     */               else
/*     */               { 
/*  78 */                 try { functionBuilder.addCommand(parseCommand(paramCommandDispatcher, (ExecutionCommandSource)paramT, stringReader)); }
/*  79 */                 catch (CommandSyntaxException commandSyntaxException)
/*  80 */                 { throw new IllegalArgumentException("Whilst parsing command on line " + i + ": " + commandSyntaxException.getMessage()); }  }  } else { continue; }  }  }
/*     */         else { String str1 = str; continue; }
/*     */          }
/*     */       else { break; }
/*     */        b++; }
/*  85 */      return (CommandFunction)functionBuilder.build(paramIdentifier);
/*     */   }
/*     */   
/*     */   static void checkCommandLineLength(CharSequence paramCharSequence) {
/*  89 */     if (paramCharSequence.length() > 2000000) {
/*  90 */       CharSequence charSequence = paramCharSequence.subSequence(0, Math.min(512, 2000000));
/*  91 */       throw new IllegalStateException("Command too long: " + paramCharSequence.length() + " characters, contents: " + String.valueOf(charSequence) + "...");
/*     */     } 
/*     */   }
/*     */   
/*     */   static <T extends ExecutionCommandSource<T>> UnboundEntryAction<T> parseCommand(CommandDispatcher<T> paramCommandDispatcher, T paramT, StringReader paramStringReader) throws CommandSyntaxException {
/*  96 */     ParseResults parseResults = paramCommandDispatcher.parse(paramStringReader, paramT);
/*  97 */     Commands.validateParseResults(parseResults);
/*     */     
/*  99 */     Optional<ContextChain> optional = ContextChain.tryFlatten(parseResults.getContext().build(paramStringReader.getString()));
/* 100 */     if (optional.isEmpty()) {
/* 101 */       throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(parseResults.getReader());
/*     */     }
/* 103 */     return (UnboundEntryAction<T>)new BuildContexts.Unbound(paramStringReader.getString(), optional.get());
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\functions\CommandFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */