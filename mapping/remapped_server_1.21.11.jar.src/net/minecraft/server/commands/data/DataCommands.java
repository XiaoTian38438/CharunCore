/*     */ package net.minecraft.server.commands.data;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.DoubleArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.CompoundTagArgument;
/*     */ import net.minecraft.commands.arguments.NbtPathArgument;
/*     */ import net.minecraft.commands.arguments.NbtTagArgument;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NumericTag;
/*     */ import net.minecraft.nbt.StringTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DataCommands
/*     */ {
/*  48 */   private static final SimpleCommandExceptionType ERROR_MERGE_UNCHANGED = new SimpleCommandExceptionType((Message)Component.translatable("commands.data.merge.failed")); private static final DynamicCommandExceptionType ERROR_GET_NOT_NUMBER; private static final DynamicCommandExceptionType ERROR_GET_NON_EXISTENT; static {
/*  49 */     ERROR_GET_NOT_NUMBER = new DynamicCommandExceptionType(paramObject -> Component.translatableEscape("commands.data.get.invalid", new Object[] { paramObject }));
/*  50 */     ERROR_GET_NON_EXISTENT = new DynamicCommandExceptionType(paramObject -> Component.translatableEscape("commands.data.get.unknown", new Object[] { paramObject }));
/*  51 */   } private static final SimpleCommandExceptionType ERROR_MULTIPLE_TAGS = new SimpleCommandExceptionType((Message)Component.translatable("commands.data.get.multiple")); private static final DynamicCommandExceptionType ERROR_EXPECTED_OBJECT; static {
/*  52 */     ERROR_EXPECTED_OBJECT = new DynamicCommandExceptionType(paramObject -> Component.translatableEscape("commands.data.modify.expected_object", new Object[] { paramObject }));
/*  53 */     ERROR_EXPECTED_VALUE = new DynamicCommandExceptionType(paramObject -> Component.translatableEscape("commands.data.modify.expected_value", new Object[] { paramObject }));
/*  54 */     ERROR_INVALID_SUBSTRING = new Dynamic2CommandExceptionType((paramObject1, paramObject2) -> Component.translatableEscape("commands.data.modify.invalid_substring", new Object[] { paramObject1, paramObject2 }));
/*     */   }
/*  56 */   private static final DynamicCommandExceptionType ERROR_EXPECTED_VALUE; private static final Dynamic2CommandExceptionType ERROR_INVALID_SUBSTRING; public static final List<Function<String, DataProvider>> ALL_PROVIDERS = (List<Function<String, DataProvider>>)ImmutableList.of(EntityDataAccessor.PROVIDER, BlockDataAccessor.PROVIDER, StorageDataAccessor.PROVIDER); public static final List<DataProvider> TARGET_PROVIDERS; public static final List<DataProvider> SOURCE_PROVIDERS;
/*     */   static {
/*  58 */     TARGET_PROVIDERS = (List<DataProvider>)ALL_PROVIDERS.stream().map(paramFunction -> (DataProvider)paramFunction.apply("target")).collect(ImmutableList.toImmutableList());
/*  59 */     SOURCE_PROVIDERS = (List<DataProvider>)ALL_PROVIDERS.stream().map(paramFunction -> (DataProvider)paramFunction.apply("source")).collect(ImmutableList.toImmutableList());
/*     */   }
/*     */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/*  62 */     LiteralArgumentBuilder literalArgumentBuilder = (LiteralArgumentBuilder)Commands.literal("data").requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS));
/*     */     
/*  64 */     for (DataProvider dataProvider : TARGET_PROVIDERS) {
/*  65 */       ((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)literalArgumentBuilder
/*  66 */         .then(dataProvider
/*  67 */           .wrap((ArgumentBuilder<CommandSourceStack, ?>)Commands.literal("merge"), paramArgumentBuilder -> paramArgumentBuilder.then(Commands.argument("nbt", (ArgumentType)CompoundTagArgument.compoundTag()).executes(())))))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  74 */         .then(dataProvider
/*  75 */           .wrap((ArgumentBuilder<CommandSourceStack, ?>)Commands.literal("get"), paramArgumentBuilder -> paramArgumentBuilder.executes(()).then(((RequiredArgumentBuilder)Commands.argument("path", (ArgumentType)NbtPathArgument.nbtPath()).executes(())).then(Commands.argument("scale", (ArgumentType)DoubleArgumentType.doubleArg()).executes(()))))))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  87 */         .then(dataProvider
/*  88 */           .wrap((ArgumentBuilder<CommandSourceStack, ?>)Commands.literal("remove"), paramArgumentBuilder -> paramArgumentBuilder.then(Commands.argument("path", (ArgumentType)NbtPathArgument.nbtPath()).executes(())))))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  95 */         .then(
/*  96 */           decorateModification((paramArgumentBuilder, paramDataManipulatorDecorator) -> paramArgumentBuilder.then(Commands.literal("insert").then(Commands.argument("index", (ArgumentType)IntegerArgumentType.integer()).then(paramDataManipulatorDecorator.create(())))).then(Commands.literal("prepend").then(paramDataManipulatorDecorator.create(()))).then(Commands.literal("append").then(paramDataManipulatorDecorator.create(()))).then(Commands.literal("set").then(paramDataManipulatorDecorator.create(()))).then(Commands.literal("merge").then(paramDataManipulatorDecorator.create(())))));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 166 */     paramCommandDispatcher.register(literalArgumentBuilder);
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
/*     */   private static String getAsText(Tag paramTag) throws CommandSyntaxException {
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
/*     */     //   16: lookupswitch default -> 80, 0 -> 44, 1 -> 64
/*     */     //   44: aload_1
/*     */     //   45: checkcast net/minecraft/nbt/StringTag
/*     */     //   48: astore_3
/*     */     //   49: aload_3
/*     */     //   50: invokevirtual value : ()Ljava/lang/String;
/*     */     //   53: astore #5
/*     */     //   55: aload #5
/*     */     //   57: astore #4
/*     */     //   59: aload #4
/*     */     //   61: goto -> 88
/*     */     //   64: aload_1
/*     */     //   65: checkcast net/minecraft/nbt/PrimitiveTag
/*     */     //   68: astore #5
/*     */     //   70: aload #5
/*     */     //   72: invokeinterface toString : ()Ljava/lang/String;
/*     */     //   77: goto -> 88
/*     */     //   80: getstatic net/minecraft/server/commands/data/DataCommands.ERROR_EXPECTED_VALUE : Lcom/mojang/brigadier/exceptions/DynamicCommandExceptionType;
/*     */     //   83: aload_0
/*     */     //   84: invokevirtual create : (Ljava/lang/Object;)Lcom/mojang/brigadier/exceptions/CommandSyntaxException;
/*     */     //   87: athrow
/*     */     //   88: areturn
/*     */     //   89: astore_1
/*     */     //   90: new java/lang/MatchException
/*     */     //   93: dup
/*     */     //   94: aload_1
/*     */     //   95: invokevirtual toString : ()Ljava/lang/String;
/*     */     //   98: aload_1
/*     */     //   99: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   102: athrow
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #180	-> 0
/*     */     //   #181	-> 44
/*     */     //   #182	-> 64
/*     */     //   #183	-> 80
/*     */     //   #180	-> 88
/*     */     //   #182	-> 89
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   50	53	89	java/lang/Throwable
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
/*     */   private static List<Tag> stringifyTagList(List<Tag> paramList, StringProcessor paramStringProcessor) throws CommandSyntaxException {
/* 193 */     ArrayList<StringTag> arrayList = new ArrayList(paramList.size());
/* 194 */     for (Tag tag : paramList) {
/* 195 */       String str = getAsText(tag);
/* 196 */       arrayList.add(StringTag.valueOf(paramStringProcessor.process(str)));
/*     */     } 
/* 198 */     return (List)arrayList;
/*     */   }
/*     */   
/*     */   private static ArgumentBuilder<CommandSourceStack, ?> decorateModification(BiConsumer<ArgumentBuilder<CommandSourceStack, ?>, DataManipulatorDecorator> paramBiConsumer) {
/* 202 */     LiteralArgumentBuilder literalArgumentBuilder = Commands.literal("modify");
/*     */     
/* 204 */     for (Iterator<DataProvider> iterator = TARGET_PROVIDERS.iterator(); iterator.hasNext(); ) { DataProvider dataProvider = iterator.next();
/* 205 */       dataProvider.wrap((ArgumentBuilder<CommandSourceStack, ?>)literalArgumentBuilder, paramArgumentBuilder -> {
/*     */             RequiredArgumentBuilder requiredArgumentBuilder = Commands.argument("targetPath", (ArgumentType)NbtPathArgument.nbtPath());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             for (DataProvider dataProvider : SOURCE_PROVIDERS) {
/*     */               paramBiConsumer.accept(requiredArgumentBuilder, ());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               paramBiConsumer.accept(requiredArgumentBuilder, ());
/*     */             } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             paramBiConsumer.accept(requiredArgumentBuilder, ());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             return paramArgumentBuilder.then((ArgumentBuilder)requiredArgumentBuilder);
/*     */           }); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 249 */     return (ArgumentBuilder<CommandSourceStack, ?>)literalArgumentBuilder;
/*     */   }
/*     */   
/*     */   private static String validatedSubstring(String paramString, int paramInt1, int paramInt2) throws CommandSyntaxException {
/* 253 */     if (paramInt1 < 0 || paramInt2 > paramString.length() || paramInt1 > paramInt2) {
/* 254 */       throw ERROR_INVALID_SUBSTRING.create(Integer.valueOf(paramInt1), Integer.valueOf(paramInt2));
/*     */     }
/* 256 */     return paramString.substring(paramInt1, paramInt2);
/*     */   }
/*     */   
/*     */   private static String substring(String paramString, int paramInt1, int paramInt2) throws CommandSyntaxException {
/* 260 */     int i = paramString.length();
/* 261 */     int j = getOffset(paramInt1, i);
/* 262 */     int k = getOffset(paramInt2, i);
/* 263 */     return validatedSubstring(paramString, j, k);
/*     */   }
/*     */   
/*     */   private static String substring(String paramString, int paramInt) throws CommandSyntaxException {
/* 267 */     int i = paramString.length();
/* 268 */     return validatedSubstring(paramString, getOffset(paramInt, i), i);
/*     */   }
/*     */   
/*     */   private static int getOffset(int paramInt1, int paramInt2) {
/* 272 */     return (paramInt1 >= 0) ? paramInt1 : (paramInt2 + paramInt1);
/*     */   }
/*     */   
/*     */   private static List<Tag> getSingletonSource(CommandContext<CommandSourceStack> paramCommandContext, DataProvider paramDataProvider) throws CommandSyntaxException {
/* 276 */     DataAccessor dataAccessor = paramDataProvider.access(paramCommandContext);
/* 277 */     return (List)Collections.singletonList(dataAccessor.getData());
/*     */   }
/*     */   
/*     */   private static List<Tag> resolveSourcePath(CommandContext<CommandSourceStack> paramCommandContext, DataProvider paramDataProvider) throws CommandSyntaxException {
/* 281 */     DataAccessor dataAccessor = paramDataProvider.access(paramCommandContext);
/* 282 */     NbtPathArgument.NbtPath nbtPath = NbtPathArgument.getPath(paramCommandContext, "sourcePath");
/* 283 */     return nbtPath.get((Tag)dataAccessor.getData());
/*     */   }
/*     */   
/*     */   private static int manipulateData(CommandContext<CommandSourceStack> paramCommandContext, DataProvider paramDataProvider, DataManipulator paramDataManipulator, List<Tag> paramList) throws CommandSyntaxException {
/* 287 */     DataAccessor dataAccessor = paramDataProvider.access(paramCommandContext);
/* 288 */     NbtPathArgument.NbtPath nbtPath = NbtPathArgument.getPath(paramCommandContext, "targetPath");
/*     */     
/* 290 */     CompoundTag compoundTag = dataAccessor.getData();
/*     */     
/* 292 */     int i = paramDataManipulator.modify(paramCommandContext, compoundTag, nbtPath, paramList);
/*     */     
/* 294 */     if (i == 0) {
/* 295 */       throw ERROR_MERGE_UNCHANGED.create();
/*     */     }
/*     */     
/* 298 */     dataAccessor.setData(compoundTag);
/* 299 */     ((CommandSourceStack)paramCommandContext.getSource()).sendSuccess(() -> paramDataAccessor.getModifiedSuccess(), true);
/*     */     
/* 301 */     return i;
/*     */   }
/*     */   
/*     */   private static int removeData(CommandSourceStack paramCommandSourceStack, DataAccessor paramDataAccessor, NbtPathArgument.NbtPath paramNbtPath) throws CommandSyntaxException {
/* 305 */     CompoundTag compoundTag = paramDataAccessor.getData();
/*     */     
/* 307 */     int i = paramNbtPath.remove((Tag)compoundTag);
/*     */     
/* 309 */     if (i == 0) {
/* 310 */       throw ERROR_MERGE_UNCHANGED.create();
/*     */     }
/*     */     
/* 313 */     paramDataAccessor.setData(compoundTag);
/* 314 */     paramCommandSourceStack.sendSuccess(() -> paramDataAccessor.getModifiedSuccess(), true);
/* 315 */     return i;
/*     */   }
/*     */   
/*     */   public static Tag getSingleTag(NbtPathArgument.NbtPath paramNbtPath, DataAccessor paramDataAccessor) throws CommandSyntaxException {
/* 319 */     List list = paramNbtPath.get((Tag)paramDataAccessor.getData());
/* 320 */     Iterator<Tag> iterator = list.iterator();
/* 321 */     Tag tag = iterator.next();
/* 322 */     if (iterator.hasNext()) {
/* 323 */       throw ERROR_MULTIPLE_TAGS.create();
/*     */     }
/*     */     
/* 326 */     return tag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int getData(CommandSourceStack paramCommandSourceStack, DataAccessor paramDataAccessor, NbtPathArgument.NbtPath paramNbtPath) throws CommandSyntaxException {
/*     */     // Byte code:
/*     */     //   0: aload_2
/*     */     //   1: aload_1
/*     */     //   2: invokestatic getSingleTag : (Lnet/minecraft/commands/arguments/NbtPathArgument$NbtPath;Lnet/minecraft/server/commands/data/DataAccessor;)Lnet/minecraft/nbt/Tag;
/*     */     //   5: astore_3
/*     */     //   6: aload_3
/*     */     //   7: dup
/*     */     //   8: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   11: pop
/*     */     //   12: astore #5
/*     */     //   14: iconst_0
/*     */     //   15: istore #6
/*     */     //   17: aload #5
/*     */     //   19: iload #6
/*     */     //   21: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */     //   26: tableswitch default -> 60, 0 -> 70, 1 -> 90, 2 -> 107, 3 -> 122, 4 -> 148
/*     */     //   60: new java/lang/MatchException
/*     */     //   63: dup
/*     */     //   64: aconst_null
/*     */     //   65: aconst_null
/*     */     //   66: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   69: athrow
/*     */     //   70: aload #5
/*     */     //   72: checkcast net/minecraft/nbt/NumericTag
/*     */     //   75: astore #7
/*     */     //   77: aload #7
/*     */     //   79: invokeinterface doubleValue : ()D
/*     */     //   84: invokestatic floor : (D)I
/*     */     //   87: goto -> 166
/*     */     //   90: aload #5
/*     */     //   92: checkcast net/minecraft/nbt/CollectionTag
/*     */     //   95: astore #8
/*     */     //   97: aload #8
/*     */     //   99: invokeinterface size : ()I
/*     */     //   104: goto -> 166
/*     */     //   107: aload #5
/*     */     //   109: checkcast net/minecraft/nbt/CompoundTag
/*     */     //   112: astore #9
/*     */     //   114: aload #9
/*     */     //   116: invokevirtual size : ()I
/*     */     //   119: goto -> 166
/*     */     //   122: aload #5
/*     */     //   124: checkcast net/minecraft/nbt/StringTag
/*     */     //   127: astore #10
/*     */     //   129: aload #10
/*     */     //   131: invokevirtual value : ()Ljava/lang/String;
/*     */     //   134: astore #12
/*     */     //   136: aload #12
/*     */     //   138: astore #11
/*     */     //   140: aload #11
/*     */     //   142: invokevirtual length : ()I
/*     */     //   145: goto -> 166
/*     */     //   148: aload #5
/*     */     //   150: checkcast net/minecraft/nbt/EndTag
/*     */     //   153: astore #12
/*     */     //   155: getstatic net/minecraft/server/commands/data/DataCommands.ERROR_GET_NON_EXISTENT : Lcom/mojang/brigadier/exceptions/DynamicCommandExceptionType;
/*     */     //   158: aload_2
/*     */     //   159: invokevirtual toString : ()Ljava/lang/String;
/*     */     //   162: invokevirtual create : (Ljava/lang/Object;)Lcom/mojang/brigadier/exceptions/CommandSyntaxException;
/*     */     //   165: athrow
/*     */     //   166: istore #4
/*     */     //   168: aload_0
/*     */     //   169: aload_1
/*     */     //   170: aload_3
/*     */     //   171: <illegal opcode> get : (Lnet/minecraft/server/commands/data/DataAccessor;Lnet/minecraft/nbt/Tag;)Ljava/util/function/Supplier;
/*     */     //   176: iconst_0
/*     */     //   177: invokevirtual sendSuccess : (Ljava/util/function/Supplier;Z)V
/*     */     //   180: iload #4
/*     */     //   182: ireturn
/*     */     //   183: astore #5
/*     */     //   185: new java/lang/MatchException
/*     */     //   188: dup
/*     */     //   189: aload #5
/*     */     //   191: invokevirtual toString : ()Ljava/lang/String;
/*     */     //   194: aload #5
/*     */     //   196: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   199: athrow
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #330	-> 0
/*     */     //   #331	-> 6
/*     */     //   #332	-> 70
/*     */     //   #333	-> 90
/*     */     //   #334	-> 107
/*     */     //   #335	-> 122
/*     */     //   #336	-> 148
/*     */     //   #338	-> 168
/*     */     //   #339	-> 180
/*     */     //   #336	-> 183
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   131	134	183	java/lang/Throwable
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int getNumeric(CommandSourceStack paramCommandSourceStack, DataAccessor paramDataAccessor, NbtPathArgument.NbtPath paramNbtPath, double paramDouble) throws CommandSyntaxException {
/* 343 */     Tag tag = getSingleTag(paramNbtPath, paramDataAccessor);
/* 344 */     if (!(tag instanceof NumericTag)) {
/* 345 */       throw ERROR_GET_NOT_NUMBER.create(paramNbtPath.toString());
/*     */     }
/* 347 */     int i = Mth.floor(((NumericTag)tag).doubleValue() * paramDouble);
/* 348 */     paramCommandSourceStack.sendSuccess(() -> paramDataAccessor.getPrintSuccess(paramNbtPath, paramDouble, paramInt), false);
/* 349 */     return i;
/*     */   }
/*     */   
/*     */   private static int getData(CommandSourceStack paramCommandSourceStack, DataAccessor paramDataAccessor) throws CommandSyntaxException {
/* 353 */     CompoundTag compoundTag = paramDataAccessor.getData();
/* 354 */     paramCommandSourceStack.sendSuccess(() -> paramDataAccessor.getPrintSuccess((Tag)paramCompoundTag), false);
/* 355 */     return 1;
/*     */   }
/*     */   
/*     */   private static int mergeData(CommandSourceStack paramCommandSourceStack, DataAccessor paramDataAccessor, CompoundTag paramCompoundTag) throws CommandSyntaxException {
/* 359 */     CompoundTag compoundTag1 = paramDataAccessor.getData();
/*     */     
/* 361 */     if (NbtPathArgument.NbtPath.isTooDeep((Tag)paramCompoundTag, 0)) {
/* 362 */       throw NbtPathArgument.ERROR_DATA_TOO_DEEP.create();
/*     */     }
/*     */     
/* 365 */     CompoundTag compoundTag2 = compoundTag1.copy().merge(paramCompoundTag);
/* 366 */     if (compoundTag1.equals(compoundTag2)) {
/* 367 */       throw ERROR_MERGE_UNCHANGED.create();
/*     */     }
/*     */     
/* 370 */     paramDataAccessor.setData(compoundTag2);
/*     */     
/* 372 */     paramCommandSourceStack.sendSuccess(() -> paramDataAccessor.getModifiedSuccess(), true);
/* 373 */     return 1;
/*     */   }
/*     */   
/*     */   public static interface DataProvider {
/*     */     DataAccessor access(CommandContext<CommandSourceStack> param1CommandContext) throws CommandSyntaxException;
/*     */     
/*     */     ArgumentBuilder<CommandSourceStack, ?> wrap(ArgumentBuilder<CommandSourceStack, ?> param1ArgumentBuilder, Function<ArgumentBuilder<CommandSourceStack, ?>, ArgumentBuilder<CommandSourceStack, ?>> param1Function);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface StringProcessor {
/*     */     String process(String param1String) throws CommandSyntaxException;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface DataManipulator {
/*     */     int modify(CommandContext<CommandSourceStack> param1CommandContext, CompoundTag param1CompoundTag, NbtPathArgument.NbtPath param1NbtPath, List<Tag> param1List) throws CommandSyntaxException;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface DataManipulatorDecorator {
/*     */     ArgumentBuilder<CommandSourceStack, ?> create(DataCommands.DataManipulator param1DataManipulator);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\data\DataCommands.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */