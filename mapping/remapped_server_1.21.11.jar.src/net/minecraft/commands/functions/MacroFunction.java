/*     */ package net.minecraft.commands.functions;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import it.unimi.dsi.fastutil.ints.IntList;
/*     */ import it.unimi.dsi.fastutil.ints.IntLists;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
/*     */ import java.text.DecimalFormat;
/*     */ import java.text.DecimalFormatSymbols;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import net.minecraft.commands.ExecutionCommandSource;
/*     */ import net.minecraft.commands.FunctionInstantiationException;
/*     */ import net.minecraft.commands.execution.UnboundEntryAction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MacroFunction<T extends ExecutionCommandSource<T>>
/*     */   implements CommandFunction<T>
/*     */ {
/*     */   private static final DecimalFormat DECIMAL_FORMAT;
/*     */   private static final int MAX_CACHE_ENTRIES = 8;
/*     */   private final List<String> parameters;
/*     */   
/*     */   static {
/*  33 */     DECIMAL_FORMAT = (DecimalFormat)Util.make(new DecimalFormat("#", DecimalFormatSymbols.getInstance(Locale.ROOT)), paramDecimalFormat -> paramDecimalFormat.setMaximumFractionDigits(15));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  40 */   private final Object2ObjectLinkedOpenHashMap<List<String>, InstantiatedFunction<T>> cache = new Object2ObjectLinkedOpenHashMap(8, 0.25F);
/*     */   
/*     */   private final Identifier id;
/*     */   private final List<Entry<T>> entries;
/*     */   
/*     */   public MacroFunction(Identifier paramIdentifier, List<Entry<T>> paramList, List<String> paramList1) {
/*  46 */     this.id = paramIdentifier;
/*  47 */     this.entries = paramList;
/*  48 */     this.parameters = paramList1;
/*     */   }
/*     */ 
/*     */   
/*     */   public Identifier id() {
/*  53 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public InstantiatedFunction<T> instantiate(CompoundTag paramCompoundTag, CommandDispatcher<T> paramCommandDispatcher) throws FunctionInstantiationException {
/*  58 */     if (paramCompoundTag == null) {
/*  59 */       throw new FunctionInstantiationException(Component.translatable("commands.function.error.missing_arguments", new Object[] { Component.translationArg(id()) }));
/*     */     }
/*  61 */     ArrayList<String> arrayList = new ArrayList(this.parameters.size());
/*  62 */     for (String str : this.parameters) {
/*  63 */       Tag tag = paramCompoundTag.get(str);
/*  64 */       if (tag == null) {
/*  65 */         throw new FunctionInstantiationException(Component.translatable("commands.function.error.missing_argument", new Object[] { Component.translationArg(id()), str }));
/*     */       }
/*  67 */       arrayList.add(stringify(tag));
/*     */     } 
/*     */     
/*  70 */     InstantiatedFunction<T> instantiatedFunction1 = (InstantiatedFunction)this.cache.getAndMoveToLast(arrayList);
/*  71 */     if (instantiatedFunction1 != null) {
/*  72 */       return instantiatedFunction1;
/*     */     }
/*  74 */     if (this.cache.size() >= 8) {
/*  75 */       this.cache.removeFirst();
/*     */     }
/*  77 */     InstantiatedFunction<T> instantiatedFunction2 = substituteAndParse(this.parameters, arrayList, paramCommandDispatcher);
/*  78 */     this.cache.put(arrayList, instantiatedFunction2);
/*  79 */     return instantiatedFunction2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String stringify(Tag paramTag) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: dup
/*     */     //   2: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   5: pop
/*     */     //   6: astore_1
/*     */     //   7: iconst_0
/*     */     //   8: istore_2
/*     */     //   9: aload_1
/*     */     //   10: iload_2
/*     */     //   11: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */     //   16: tableswitch default -> 208, 0 -> 56, 1 -> 83, 2 -> 111, 3 -> 136, 4 -> 161, 5 -> 186
/*     */     //   56: aload_1
/*     */     //   57: checkcast net/minecraft/nbt/FloatTag
/*     */     //   60: astore_3
/*     */     //   61: aload_3
/*     */     //   62: invokevirtual value : ()F
/*     */     //   65: fstore #5
/*     */     //   67: fload #5
/*     */     //   69: fstore #4
/*     */     //   71: getstatic net/minecraft/commands/functions/MacroFunction.DECIMAL_FORMAT : Ljava/text/DecimalFormat;
/*     */     //   74: fload #4
/*     */     //   76: f2d
/*     */     //   77: invokevirtual format : (D)Ljava/lang/String;
/*     */     //   80: goto -> 214
/*     */     //   83: aload_1
/*     */     //   84: checkcast net/minecraft/nbt/DoubleTag
/*     */     //   87: astore #5
/*     */     //   89: aload #5
/*     */     //   91: invokevirtual value : ()D
/*     */     //   94: dstore #8
/*     */     //   96: dload #8
/*     */     //   98: dstore #6
/*     */     //   100: getstatic net/minecraft/commands/functions/MacroFunction.DECIMAL_FORMAT : Ljava/text/DecimalFormat;
/*     */     //   103: dload #6
/*     */     //   105: invokevirtual format : (D)Ljava/lang/String;
/*     */     //   108: goto -> 214
/*     */     //   111: aload_1
/*     */     //   112: checkcast net/minecraft/nbt/ByteTag
/*     */     //   115: astore #8
/*     */     //   117: aload #8
/*     */     //   119: invokevirtual value : ()B
/*     */     //   122: istore #10
/*     */     //   124: iload #10
/*     */     //   126: istore #9
/*     */     //   128: iload #9
/*     */     //   130: invokestatic valueOf : (I)Ljava/lang/String;
/*     */     //   133: goto -> 214
/*     */     //   136: aload_1
/*     */     //   137: checkcast net/minecraft/nbt/ShortTag
/*     */     //   140: astore #10
/*     */     //   142: aload #10
/*     */     //   144: invokevirtual value : ()S
/*     */     //   147: istore #12
/*     */     //   149: iload #12
/*     */     //   151: istore #11
/*     */     //   153: iload #11
/*     */     //   155: invokestatic valueOf : (I)Ljava/lang/String;
/*     */     //   158: goto -> 214
/*     */     //   161: aload_1
/*     */     //   162: checkcast net/minecraft/nbt/LongTag
/*     */     //   165: astore #12
/*     */     //   167: aload #12
/*     */     //   169: invokevirtual value : ()J
/*     */     //   172: lstore #15
/*     */     //   174: lload #15
/*     */     //   176: lstore #13
/*     */     //   178: lload #13
/*     */     //   180: invokestatic valueOf : (J)Ljava/lang/String;
/*     */     //   183: goto -> 214
/*     */     //   186: aload_1
/*     */     //   187: checkcast net/minecraft/nbt/StringTag
/*     */     //   190: astore #15
/*     */     //   192: aload #15
/*     */     //   194: invokevirtual value : ()Ljava/lang/String;
/*     */     //   197: astore #17
/*     */     //   199: aload #17
/*     */     //   201: astore #16
/*     */     //   203: aload #16
/*     */     //   205: goto -> 214
/*     */     //   208: aload_0
/*     */     //   209: invokeinterface toString : ()Ljava/lang/String;
/*     */     //   214: areturn
/*     */     //   215: astore_1
/*     */     //   216: new java/lang/MatchException
/*     */     //   219: dup
/*     */     //   220: aload_1
/*     */     //   221: invokevirtual toString : ()Ljava/lang/String;
/*     */     //   224: aload_1
/*     */     //   225: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   228: athrow
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #83	-> 0
/*     */     //   #85	-> 56
/*     */     //   #86	-> 83
/*     */     //   #87	-> 111
/*     */     //   #88	-> 136
/*     */     //   #89	-> 161
/*     */     //   #91	-> 186
/*     */     //   #92	-> 208
/*     */     //   #83	-> 214
/*     */     //   #91	-> 215
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   62	65	215	java/lang/Throwable
/*     */     //   91	94	215	java/lang/Throwable
/*     */     //   119	122	215	java/lang/Throwable
/*     */     //   144	147	215	java/lang/Throwable
/*     */     //   169	172	215	java/lang/Throwable
/*     */     //   194	197	215	java/lang/Throwable
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void lookupValues(List<String> paramList1, IntList paramIntList, List<String> paramList2) {
/*  97 */     paramList2.clear();
/*  98 */     paramIntList.forEach(paramInt -> paramList1.add(paramList2.get(paramInt)));
/*     */   }
/*     */   
/*     */   private InstantiatedFunction<T> substituteAndParse(List<String> paramList1, List<String> paramList2, CommandDispatcher<T> paramCommandDispatcher) throws FunctionInstantiationException {
/* 102 */     ArrayList<UnboundEntryAction<T>> arrayList = new ArrayList(this.entries.size());
/* 103 */     ArrayList<String> arrayList1 = new ArrayList(paramList2.size());
/*     */     
/* 105 */     for (Entry<T> entry : this.entries) {
/* 106 */       lookupValues(paramList2, entry.parameters(), arrayList1);
/* 107 */       arrayList.add(entry.instantiate(arrayList1, paramCommandDispatcher, this.id));
/*     */     } 
/* 109 */     return new PlainTextFunction<>(id().withPath(paramString -> paramString + "/" + paramString), arrayList);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static class PlainTextEntry<T>
/*     */     implements Entry<T>
/*     */   {
/*     */     private final UnboundEntryAction<T> compiledAction;
/*     */ 
/*     */ 
/*     */     
/*     */     public PlainTextEntry(UnboundEntryAction<T> param1UnboundEntryAction) {
/* 122 */       this.compiledAction = param1UnboundEntryAction;
/*     */     }
/*     */ 
/*     */     
/*     */     public IntList parameters() {
/* 127 */       return IntLists.emptyList();
/*     */     }
/*     */ 
/*     */     
/*     */     public UnboundEntryAction<T> instantiate(List<String> param1List, CommandDispatcher<T> param1CommandDispatcher, Identifier param1Identifier) {
/* 132 */       return this.compiledAction;
/*     */     }
/*     */   }
/*     */   
/*     */   static class MacroEntry<T extends ExecutionCommandSource<T>> implements Entry<T> {
/*     */     private final StringTemplate template;
/*     */     private final IntList parameters;
/*     */     private final T compilationContext;
/*     */     
/*     */     public MacroEntry(StringTemplate param1StringTemplate, IntList param1IntList, T param1T) {
/* 142 */       this.template = param1StringTemplate;
/* 143 */       this.parameters = param1IntList;
/* 144 */       this.compilationContext = param1T;
/*     */     }
/*     */ 
/*     */     
/*     */     public IntList parameters() {
/* 149 */       return this.parameters;
/*     */     }
/*     */ 
/*     */     
/*     */     public UnboundEntryAction<T> instantiate(List<String> param1List, CommandDispatcher<T> param1CommandDispatcher, Identifier param1Identifier) throws FunctionInstantiationException {
/* 154 */       String str = this.template.substitute(param1List);
/*     */       try {
/* 156 */         return CommandFunction.parseCommand(param1CommandDispatcher, this.compilationContext, new StringReader(str));
/* 157 */       } catch (CommandSyntaxException commandSyntaxException) {
/* 158 */         throw new FunctionInstantiationException(Component.translatable("commands.function.error.parse", new Object[] { Component.translationArg(param1Identifier), str, commandSyntaxException.getMessage() }));
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static interface Entry<T> {
/*     */     IntList parameters();
/*     */     
/*     */     UnboundEntryAction<T> instantiate(List<String> param1List, CommandDispatcher<T> param1CommandDispatcher, Identifier param1Identifier) throws FunctionInstantiationException;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\functions\MacroFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */