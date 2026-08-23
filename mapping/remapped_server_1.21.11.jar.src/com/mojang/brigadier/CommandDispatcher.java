/*     */ package com.mojang.brigadier;
/*     */ 
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.context.CommandContextBuilder;
/*     */ import com.mojang.brigadier.context.ContextChain;
/*     */ import com.mojang.brigadier.context.SuggestionContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import com.mojang.brigadier.tree.CommandNode;
/*     */ import com.mojang.brigadier.tree.LiteralCommandNode;
/*     */ import com.mojang.brigadier.tree.RootCommandNode;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Collectors;
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
/*     */ public class CommandDispatcher<S>
/*     */ {
/*     */   public static final String ARGUMENT_SEPARATOR = " ";
/*     */   public static final char ARGUMENT_SEPARATOR_CHAR = ' ';
/*     */   private static final String USAGE_OPTIONAL_OPEN = "[";
/*     */   private static final String USAGE_OPTIONAL_CLOSE = "]";
/*     */   private static final String USAGE_REQUIRED_OPEN = "(";
/*     */   private static final String USAGE_REQUIRED_CLOSE = ")";
/*     */   private static final String USAGE_OR = "|";
/*     */   private final RootCommandNode<S> root;
/*     */   
/*  61 */   private final Predicate<CommandNode<S>> hasCommand = new Predicate<CommandNode<S>>()
/*     */     {
/*     */       public boolean test(CommandNode<S> param1CommandNode) {
/*  64 */         return (param1CommandNode != null && (param1CommandNode.getCommand() != null || param1CommandNode.getChildren().stream().anyMatch(CommandDispatcher.this.hasCommand)));
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ResultConsumer<S> consumer = (paramCommandContext, paramBoolean, paramInt) -> {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*     */   public CommandDispatcher(RootCommandNode<S> paramRootCommandNode) {
/*  78 */     this.root = paramRootCommandNode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CommandDispatcher() {
/*  85 */     this(new RootCommandNode());
/*     */   }
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
/*     */   public LiteralCommandNode<S> register(LiteralArgumentBuilder<S> paramLiteralArgumentBuilder) {
/*  99 */     LiteralCommandNode<S> literalCommandNode = paramLiteralArgumentBuilder.build();
/* 100 */     this.root.addChild((CommandNode)literalCommandNode);
/* 101 */     return literalCommandNode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConsumer(ResultConsumer<S> paramResultConsumer) {
/* 110 */     this.consumer = paramResultConsumer;
/*     */   }
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
/*     */   public int execute(String paramString, S paramS) throws CommandSyntaxException {
/* 144 */     return execute(new StringReader(paramString), paramS);
/*     */   }
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
/*     */   public int execute(StringReader paramStringReader, S paramS) throws CommandSyntaxException {
/* 178 */     ParseResults<S> parseResults = parse(paramStringReader, paramS);
/* 179 */     return execute(parseResults);
/*     */   }
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
/*     */   public int execute(ParseResults<S> paramParseResults) throws CommandSyntaxException {
/* 209 */     if (paramParseResults.getReader().canRead()) {
/* 210 */       if (paramParseResults.getExceptions().size() == 1)
/* 211 */         throw (CommandSyntaxException)paramParseResults.getExceptions().values().iterator().next(); 
/* 212 */       if (paramParseResults.getContext().getRange().isEmpty()) {
/* 213 */         throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(paramParseResults.getReader());
/*     */       }
/* 215 */       throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(paramParseResults.getReader());
/*     */     } 
/*     */ 
/*     */     
/* 219 */     String str = paramParseResults.getReader().getString();
/* 220 */     CommandContext<S> commandContext = paramParseResults.getContext().build(str);
/*     */     
/* 222 */     Optional<ContextChain> optional = ContextChain.tryFlatten(commandContext);
/* 223 */     if (!optional.isPresent()) {
/* 224 */       this.consumer.onCommandComplete(commandContext, false, 0);
/* 225 */       throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(paramParseResults.getReader());
/*     */     } 
/*     */     
/* 228 */     return ((ContextChain)optional.get()).executeAll(commandContext.getSource(), this.consumer);
/*     */   }
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
/*     */   public ParseResults<S> parse(String paramString, S paramS) {
/* 259 */     return parse(new StringReader(paramString), paramS);
/*     */   }
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
/*     */   public ParseResults<S> parse(StringReader paramStringReader, S paramS) {
/* 290 */     CommandContextBuilder<S> commandContextBuilder = new CommandContextBuilder(this, paramS, (CommandNode)this.root, paramStringReader.getCursor());
/* 291 */     return parseNodes((CommandNode<S>)this.root, paramStringReader, commandContextBuilder);
/*     */   }
/*     */   
/*     */   private ParseResults<S> parseNodes(CommandNode<S> paramCommandNode, StringReader paramStringReader, CommandContextBuilder<S> paramCommandContextBuilder) {
/* 295 */     Object object = paramCommandContextBuilder.getSource();
/* 296 */     LinkedHashMap<Object, Object> linkedHashMap = null;
/* 297 */     ArrayList<ParseResults<S>> arrayList = null;
/* 298 */     int i = paramStringReader.getCursor();
/*     */     
/* 300 */     for (CommandNode<S> commandNode : (Iterable<CommandNode<S>>)paramCommandNode.getRelevantNodes(paramStringReader)) {
/* 301 */       if (!commandNode.canUse(object)) {
/*     */         continue;
/*     */       }
/* 304 */       CommandContextBuilder<S> commandContextBuilder = paramCommandContextBuilder.copy();
/* 305 */       StringReader stringReader = new StringReader(paramStringReader);
/*     */       try {
/*     */         try {
/* 308 */           commandNode.parse(stringReader, commandContextBuilder);
/* 309 */         } catch (RuntimeException runtimeException) {
/* 310 */           throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().createWithContext(stringReader, runtimeException.getMessage());
/*     */         } 
/* 312 */         if (stringReader.canRead() && 
/* 313 */           stringReader.peek() != ' ') {
/* 314 */           throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherExpectedArgumentSeparator().createWithContext(stringReader);
/*     */         }
/*     */       }
/* 317 */       catch (CommandSyntaxException commandSyntaxException) {
/* 318 */         if (linkedHashMap == null) {
/* 319 */           linkedHashMap = new LinkedHashMap<>();
/*     */         }
/* 321 */         linkedHashMap.put(commandNode, commandSyntaxException);
/* 322 */         stringReader.setCursor(i);
/*     */         
/*     */         continue;
/*     */       } 
/* 326 */       commandContextBuilder.withCommand(commandNode.getCommand());
/* 327 */       if (stringReader.canRead((commandNode.getRedirect() == null) ? 2 : 1)) {
/* 328 */         stringReader.skip();
/* 329 */         if (commandNode.getRedirect() != null) {
/* 330 */           CommandContextBuilder<S> commandContextBuilder1 = new CommandContextBuilder(this, object, commandNode.getRedirect(), stringReader.getCursor());
/* 331 */           ParseResults<S> parseResults1 = parseNodes(commandNode.getRedirect(), stringReader, commandContextBuilder1);
/* 332 */           commandContextBuilder.withChild(parseResults1.getContext());
/* 333 */           return new ParseResults<>(commandContextBuilder, parseResults1.getReader(), parseResults1.getExceptions());
/*     */         } 
/* 335 */         ParseResults<S> parseResults = parseNodes(commandNode, stringReader, commandContextBuilder);
/* 336 */         if (arrayList == null) {
/* 337 */           arrayList = new ArrayList(1);
/*     */         }
/* 339 */         arrayList.add(parseResults);
/*     */         continue;
/*     */       } 
/* 342 */       if (arrayList == null) {
/* 343 */         arrayList = new ArrayList<>(1);
/*     */       }
/* 345 */       arrayList.add(new ParseResults<>(commandContextBuilder, stringReader, Collections.emptyMap()));
/*     */     } 
/*     */ 
/*     */     
/* 349 */     if (arrayList != null) {
/* 350 */       if (arrayList.size() > 1) {
/* 351 */         arrayList.sort((paramParseResults1, paramParseResults2) -> 
/* 352 */             (!paramParseResults1.getReader().canRead() && paramParseResults2.getReader().canRead()) ? -1 : (
/*     */ 
/*     */             
/* 355 */             (paramParseResults1.getReader().canRead() && !paramParseResults2.getReader().canRead()) ? 1 : (
/*     */ 
/*     */             
/* 358 */             (paramParseResults1.getExceptions().isEmpty() && !paramParseResults2.getExceptions().isEmpty()) ? -1 : (
/*     */ 
/*     */             
/* 361 */             (!paramParseResults1.getExceptions().isEmpty() && paramParseResults2.getExceptions().isEmpty()) ? 1 : 0))));
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 367 */       return arrayList.get(0);
/*     */     } 
/*     */     
/* 370 */     return new ParseResults<>(paramCommandContextBuilder, paramStringReader, (linkedHashMap == null) ? Collections.<CommandNode<S>, CommandSyntaxException>emptyMap() : (Map)linkedHashMap);
/*     */   }
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
/*     */   public String[] getAllUsage(CommandNode<S> paramCommandNode, S paramS, boolean paramBoolean) {
/* 395 */     ArrayList<String> arrayList = new ArrayList();
/* 396 */     getAllUsage(paramCommandNode, paramS, arrayList, "", paramBoolean);
/* 397 */     return arrayList.<String>toArray(new String[arrayList.size()]);
/*     */   }
/*     */   
/*     */   private void getAllUsage(CommandNode<S> paramCommandNode, S paramS, ArrayList<String> paramArrayList, String paramString, boolean paramBoolean) {
/* 401 */     if (paramBoolean && !paramCommandNode.canUse(paramS)) {
/*     */       return;
/*     */     }
/*     */     
/* 405 */     if (paramCommandNode.getCommand() != null) {
/* 406 */       paramArrayList.add(paramString);
/*     */     }
/*     */     
/* 409 */     if (paramCommandNode.getRedirect() != null) {
/* 410 */       String str = (paramCommandNode.getRedirect() == this.root) ? "..." : ("-> " + paramCommandNode.getRedirect().getUsageText());
/* 411 */       paramArrayList.add(paramString.isEmpty() ? (paramCommandNode.getUsageText() + " " + str) : (paramString + " " + str));
/* 412 */     } else if (!paramCommandNode.getChildren().isEmpty()) {
/* 413 */       for (CommandNode<S> commandNode : (Iterable<CommandNode<S>>)paramCommandNode.getChildren()) {
/* 414 */         getAllUsage(commandNode, paramS, paramArrayList, paramString.isEmpty() ? commandNode.getUsageText() : (paramString + " " + commandNode.getUsageText()), paramBoolean);
/*     */       }
/*     */     } 
/*     */   }
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
/*     */   public Map<CommandNode<S>, String> getSmartUsage(CommandNode<S> paramCommandNode, S paramS) {
/* 441 */     LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<>();
/*     */     
/* 443 */     boolean bool = (paramCommandNode.getCommand() != null) ? true : false;
/* 444 */     for (CommandNode<S> commandNode : (Iterable<CommandNode<S>>)paramCommandNode.getChildren()) {
/* 445 */       String str = getSmartUsage(commandNode, paramS, bool, false);
/* 446 */       if (str != null) {
/* 447 */         linkedHashMap.put(commandNode, str);
/*     */       }
/*     */     } 
/* 450 */     return (Map)linkedHashMap;
/*     */   }
/*     */   
/*     */   private String getSmartUsage(CommandNode<S> paramCommandNode, S paramS, boolean paramBoolean1, boolean paramBoolean2) {
/* 454 */     if (!paramCommandNode.canUse(paramS)) {
/* 455 */       return null;
/*     */     }
/*     */     
/* 458 */     String str1 = paramBoolean1 ? ("[" + paramCommandNode.getUsageText() + "]") : paramCommandNode.getUsageText();
/* 459 */     boolean bool = (paramCommandNode.getCommand() != null) ? true : false;
/* 460 */     String str2 = bool ? "[" : "(";
/* 461 */     String str3 = bool ? "]" : ")";
/*     */     
/* 463 */     if (!paramBoolean2) {
/* 464 */       if (paramCommandNode.getRedirect() != null) {
/* 465 */         String str = (paramCommandNode.getRedirect() == this.root) ? "..." : ("-> " + paramCommandNode.getRedirect().getUsageText());
/* 466 */         return str1 + " " + str;
/*     */       } 
/* 468 */       Collection<CommandNode<S>> collection = (Collection)paramCommandNode.getChildren().stream().filter(paramCommandNode -> paramCommandNode.canUse(paramObject)).collect(Collectors.toList());
/* 469 */       if (collection.size() == 1) {
/* 470 */         String str = getSmartUsage(collection.iterator().next(), paramS, bool, bool);
/* 471 */         if (str != null) {
/* 472 */           return str1 + " " + str;
/*     */         }
/* 474 */       } else if (collection.size() > 1) {
/* 475 */         LinkedHashSet<String> linkedHashSet = new LinkedHashSet();
/* 476 */         for (CommandNode<S> commandNode : collection) {
/* 477 */           String str = getSmartUsage(commandNode, paramS, bool, true);
/* 478 */           if (str != null) {
/* 479 */             linkedHashSet.add(str);
/*     */           }
/*     */         } 
/* 482 */         if (linkedHashSet.size() == 1) {
/* 483 */           String str = linkedHashSet.iterator().next();
/* 484 */           return str1 + " " + (bool ? ("[" + str + "]") : str);
/* 485 */         }  if (linkedHashSet.size() > 1) {
/* 486 */           StringBuilder stringBuilder = new StringBuilder(str2);
/* 487 */           byte b = 0;
/* 488 */           for (CommandNode<S> commandNode : collection) {
/* 489 */             if (b) {
/* 490 */               stringBuilder.append("|");
/*     */             }
/* 492 */             stringBuilder.append(commandNode.getUsageText());
/* 493 */             b++;
/*     */           } 
/* 495 */           if (b > 0) {
/* 496 */             stringBuilder.append(str3);
/* 497 */             return str1 + " " + stringBuilder.toString();
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 504 */     return str1;
/*     */   }
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
/*     */   public CompletableFuture<Suggestions> getCompletionSuggestions(ParseResults<S> paramParseResults) {
/* 523 */     return getCompletionSuggestions(paramParseResults, paramParseResults.getReader().getTotalLength());
/*     */   }
/*     */   
/*     */   public CompletableFuture<Suggestions> getCompletionSuggestions(ParseResults<S> paramParseResults, int paramInt) {
/* 527 */     CommandContextBuilder<S> commandContextBuilder = paramParseResults.getContext();
/*     */     
/* 529 */     SuggestionContext suggestionContext = commandContextBuilder.findSuggestionContext(paramInt);
/* 530 */     CommandNode commandNode = suggestionContext.parent;
/* 531 */     int i = Math.min(suggestionContext.startPos, paramInt);
/*     */     
/* 533 */     String str1 = paramParseResults.getReader().getString();
/* 534 */     String str2 = str1.substring(0, paramInt);
/* 535 */     String str3 = str2.toLowerCase(Locale.ROOT);
/* 536 */     CompletableFuture[] arrayOfCompletableFuture = new CompletableFuture[commandNode.getChildren().size()];
/* 537 */     byte b = 0;
/* 538 */     for (CommandNode commandNode1 : commandNode.getChildren()) {
/* 539 */       CompletableFuture completableFuture1 = Suggestions.empty();
/*     */       try {
/* 541 */         completableFuture1 = commandNode1.listSuggestions(commandContextBuilder.build(str2), new SuggestionsBuilder(str2, str3, i));
/* 542 */       } catch (CommandSyntaxException commandSyntaxException) {}
/*     */       
/* 544 */       arrayOfCompletableFuture[b++] = completableFuture1;
/*     */     } 
/*     */     
/* 547 */     CompletableFuture<Suggestions> completableFuture = new CompletableFuture();
/* 548 */     CompletableFuture.allOf((CompletableFuture<?>[])arrayOfCompletableFuture).thenRun(() -> {
/*     */           ArrayList arrayList = new ArrayList();
/*     */           
/*     */           for (CompletableFuture completableFuture : paramArrayOfCompletableFuture) {
/*     */             arrayList.add(completableFuture.join());
/*     */           }
/*     */           paramCompletableFuture.complete(Suggestions.merge(paramString, arrayList));
/*     */         });
/* 556 */     return completableFuture;
/*     */   }
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
/*     */   public RootCommandNode<S> getRoot() {
/* 569 */     return this.root;
/*     */   }
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
/*     */   public Collection<String> getPath(CommandNode<S> paramCommandNode) {
/* 587 */     ArrayList<List<CommandNode<S>>> arrayList = new ArrayList();
/* 588 */     addPaths((CommandNode<S>)this.root, arrayList, new ArrayList<>());
/*     */     
/* 590 */     for (List<CommandNode<S>> list : arrayList) {
/* 591 */       if (list.get(list.size() - 1) == paramCommandNode) {
/* 592 */         ArrayList<String> arrayList1 = new ArrayList(list.size());
/* 593 */         for (CommandNode<S> commandNode : list) {
/* 594 */           if (commandNode != this.root) {
/* 595 */             arrayList1.add(commandNode.getName());
/*     */           }
/*     */         } 
/* 598 */         return arrayList1;
/*     */       } 
/*     */     } 
/*     */     
/* 602 */     return Collections.emptyList();
/*     */   }
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
/*     */   public CommandNode<S> findNode(Collection<String> paramCollection) {
/*     */     CommandNode<S> commandNode;
/* 617 */     RootCommandNode<S> rootCommandNode = this.root;
/* 618 */     for (String str : paramCollection) {
/* 619 */       commandNode = rootCommandNode.getChild(str);
/* 620 */       if (commandNode == null) {
/* 621 */         return null;
/*     */       }
/*     */     } 
/* 624 */     return commandNode;
/*     */   }
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
/*     */   public void findAmbiguities(AmbiguityConsumer<S> paramAmbiguityConsumer) {
/* 638 */     this.root.findAmbiguities(paramAmbiguityConsumer);
/*     */   }
/*     */   
/*     */   private void addPaths(CommandNode<S> paramCommandNode, List<List<CommandNode<S>>> paramList, List<CommandNode<S>> paramList1) {
/* 642 */     ArrayList<CommandNode<S>> arrayList = new ArrayList<>(paramList1);
/* 643 */     arrayList.add(paramCommandNode);
/* 644 */     paramList.add(arrayList);
/*     */     
/* 646 */     for (CommandNode<S> commandNode : (Iterable<CommandNode<S>>)paramCommandNode.getChildren())
/* 647 */       addPaths(commandNode, paramList, arrayList); 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\brigadier\CommandDispatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */