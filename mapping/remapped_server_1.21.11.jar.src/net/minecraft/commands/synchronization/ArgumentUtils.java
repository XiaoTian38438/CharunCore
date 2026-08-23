/*     */ package net.minecraft.commands.synchronization;
/*     */ 
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.tree.ArgumentCommandNode;
/*     */ import com.mojang.brigadier.tree.CommandNode;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ArgumentUtils
/*     */ {
/*  25 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final byte NUMBER_FLAG_MIN = 1;
/*     */   private static final byte NUMBER_FLAG_MAX = 2;
/*     */   
/*     */   public static int createNumberFlags(boolean paramBoolean1, boolean paramBoolean2) {
/*  31 */     int i = 0;
/*  32 */     if (paramBoolean1) {
/*  33 */       i |= 0x1;
/*     */     }
/*  35 */     if (paramBoolean2) {
/*  36 */       i |= 0x2;
/*     */     }
/*  38 */     return i;
/*     */   }
/*     */   
/*     */   public static boolean numberHasMin(byte paramByte) {
/*  42 */     return ((paramByte & 0x1) != 0);
/*     */   }
/*     */   
/*     */   public static boolean numberHasMax(byte paramByte) {
/*  46 */     return ((paramByte & 0x2) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>> void serializeArgumentCap(JsonObject paramJsonObject, ArgumentTypeInfo<A, T> paramArgumentTypeInfo, ArgumentTypeInfo.Template<A> paramTemplate) {
/*  51 */     paramArgumentTypeInfo.serializeToJson((T)paramTemplate, paramJsonObject);
/*     */   }
/*     */   
/*     */   private static <T extends ArgumentType<?>> void serializeArgumentToJson(JsonObject paramJsonObject, T paramT) {
/*  55 */     ArgumentTypeInfo.Template<T> template = ArgumentTypeInfos.unpack(paramT);
/*     */     
/*  57 */     paramJsonObject.addProperty("type", "argument");
/*  58 */     paramJsonObject.addProperty("parser", String.valueOf(BuiltInRegistries.COMMAND_ARGUMENT_TYPE.getKey(template.type())));
/*     */     
/*  60 */     JsonObject jsonObject = new JsonObject();
/*  61 */     serializeArgumentCap(jsonObject, (ArgumentTypeInfo)template.type(), template);
/*  62 */     if (!jsonObject.isEmpty()) {
/*  63 */       paramJsonObject.add("properties", (JsonElement)jsonObject);
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
/*     */   
/*     */   public static <S> JsonObject serializeNodeToJson(CommandDispatcher<S> paramCommandDispatcher, CommandNode<S> paramCommandNode) {
/*     */     // Byte code:
/*     */     //   0: new com/google/gson/JsonObject
/*     */     //   3: dup
/*     */     //   4: invokespecial <init> : ()V
/*     */     //   7: astore_2
/*     */     //   8: aload_1
/*     */     //   9: dup
/*     */     //   10: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   13: pop
/*     */     //   14: astore_3
/*     */     //   15: iconst_0
/*     */     //   16: istore #4
/*     */     //   18: aload_3
/*     */     //   19: iload #4
/*     */     //   21: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */     //   26: tableswitch default -> 104, 0 -> 52, 1 -> 69, 2 -> 86
/*     */     //   52: aload_3
/*     */     //   53: checkcast com/mojang/brigadier/tree/RootCommandNode
/*     */     //   56: astore #5
/*     */     //   58: aload_2
/*     */     //   59: ldc 'type'
/*     */     //   61: ldc 'root'
/*     */     //   63: invokevirtual addProperty : (Ljava/lang/String;Ljava/lang/String;)V
/*     */     //   66: goto -> 127
/*     */     //   69: aload_3
/*     */     //   70: checkcast com/mojang/brigadier/tree/LiteralCommandNode
/*     */     //   73: astore #6
/*     */     //   75: aload_2
/*     */     //   76: ldc 'type'
/*     */     //   78: ldc 'literal'
/*     */     //   80: invokevirtual addProperty : (Ljava/lang/String;Ljava/lang/String;)V
/*     */     //   83: goto -> 127
/*     */     //   86: aload_3
/*     */     //   87: checkcast com/mojang/brigadier/tree/ArgumentCommandNode
/*     */     //   90: astore #7
/*     */     //   92: aload_2
/*     */     //   93: aload #7
/*     */     //   95: invokevirtual getType : ()Lcom/mojang/brigadier/arguments/ArgumentType;
/*     */     //   98: invokestatic serializeArgumentToJson : (Lcom/google/gson/JsonObject;Lcom/mojang/brigadier/arguments/ArgumentType;)V
/*     */     //   101: goto -> 127
/*     */     //   104: getstatic net/minecraft/commands/synchronization/ArgumentUtils.LOGGER : Lorg/slf4j/Logger;
/*     */     //   107: ldc 'Could not serialize node {} ({})!'
/*     */     //   109: aload_1
/*     */     //   110: aload_1
/*     */     //   111: invokevirtual getClass : ()Ljava/lang/Class;
/*     */     //   114: invokeinterface error : (Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V
/*     */     //   119: aload_2
/*     */     //   120: ldc 'type'
/*     */     //   122: ldc 'unknown'
/*     */     //   124: invokevirtual addProperty : (Ljava/lang/String;Ljava/lang/String;)V
/*     */     //   127: aload_1
/*     */     //   128: invokevirtual getChildren : ()Ljava/util/Collection;
/*     */     //   131: astore_3
/*     */     //   132: aload_3
/*     */     //   133: invokeinterface isEmpty : ()Z
/*     */     //   138: ifne -> 207
/*     */     //   141: new com/google/gson/JsonObject
/*     */     //   144: dup
/*     */     //   145: invokespecial <init> : ()V
/*     */     //   148: astore #4
/*     */     //   150: aload_3
/*     */     //   151: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */     //   156: astore #5
/*     */     //   158: aload #5
/*     */     //   160: invokeinterface hasNext : ()Z
/*     */     //   165: ifeq -> 199
/*     */     //   168: aload #5
/*     */     //   170: invokeinterface next : ()Ljava/lang/Object;
/*     */     //   175: checkcast com/mojang/brigadier/tree/CommandNode
/*     */     //   178: astore #6
/*     */     //   180: aload #4
/*     */     //   182: aload #6
/*     */     //   184: invokevirtual getName : ()Ljava/lang/String;
/*     */     //   187: aload_0
/*     */     //   188: aload #6
/*     */     //   190: invokestatic serializeNodeToJson : (Lcom/mojang/brigadier/CommandDispatcher;Lcom/mojang/brigadier/tree/CommandNode;)Lcom/google/gson/JsonObject;
/*     */     //   193: invokevirtual add : (Ljava/lang/String;Lcom/google/gson/JsonElement;)V
/*     */     //   196: goto -> 158
/*     */     //   199: aload_2
/*     */     //   200: ldc 'children'
/*     */     //   202: aload #4
/*     */     //   204: invokevirtual add : (Ljava/lang/String;Lcom/google/gson/JsonElement;)V
/*     */     //   207: aload_1
/*     */     //   208: invokevirtual getCommand : ()Lcom/mojang/brigadier/Command;
/*     */     //   211: ifnull -> 224
/*     */     //   214: aload_2
/*     */     //   215: ldc 'executable'
/*     */     //   217: iconst_1
/*     */     //   218: invokestatic valueOf : (Z)Ljava/lang/Boolean;
/*     */     //   221: invokevirtual addProperty : (Ljava/lang/String;Ljava/lang/Boolean;)V
/*     */     //   224: aload_1
/*     */     //   225: invokevirtual getRequirement : ()Ljava/util/function/Predicate;
/*     */     //   228: astore #5
/*     */     //   230: aload #5
/*     */     //   232: instanceof net/minecraft/server/permissions/PermissionProviderCheck
/*     */     //   235: ifeq -> 284
/*     */     //   238: aload #5
/*     */     //   240: checkcast net/minecraft/server/permissions/PermissionProviderCheck
/*     */     //   243: astore #4
/*     */     //   245: getstatic net/minecraft/server/permissions/PermissionCheck.CODEC : Lcom/mojang/serialization/Codec;
/*     */     //   248: getstatic com/mojang/serialization/JsonOps.INSTANCE : Lcom/mojang/serialization/JsonOps;
/*     */     //   251: aload #4
/*     */     //   253: invokevirtual test : ()Lnet/minecraft/server/permissions/PermissionCheck;
/*     */     //   256: invokeinterface encodeStart : (Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;
/*     */     //   261: <illegal opcode> apply : ()Ljava/util/function/Function;
/*     */     //   266: invokeinterface getOrThrow : (Ljava/util/function/Function;)Ljava/lang/Object;
/*     */     //   271: checkcast com/google/gson/JsonElement
/*     */     //   274: astore #5
/*     */     //   276: aload_2
/*     */     //   277: ldc 'permissions'
/*     */     //   279: aload #5
/*     */     //   281: invokevirtual add : (Ljava/lang/String;Lcom/google/gson/JsonElement;)V
/*     */     //   284: aload_1
/*     */     //   285: invokevirtual getRedirect : ()Lcom/mojang/brigadier/tree/CommandNode;
/*     */     //   288: ifnull -> 370
/*     */     //   291: aload_0
/*     */     //   292: aload_1
/*     */     //   293: invokevirtual getRedirect : ()Lcom/mojang/brigadier/tree/CommandNode;
/*     */     //   296: invokevirtual getPath : (Lcom/mojang/brigadier/tree/CommandNode;)Ljava/util/Collection;
/*     */     //   299: astore #4
/*     */     //   301: aload #4
/*     */     //   303: invokeinterface isEmpty : ()Z
/*     */     //   308: ifne -> 370
/*     */     //   311: new com/google/gson/JsonArray
/*     */     //   314: dup
/*     */     //   315: invokespecial <init> : ()V
/*     */     //   318: astore #5
/*     */     //   320: aload #4
/*     */     //   322: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */     //   327: astore #6
/*     */     //   329: aload #6
/*     */     //   331: invokeinterface hasNext : ()Z
/*     */     //   336: ifeq -> 361
/*     */     //   339: aload #6
/*     */     //   341: invokeinterface next : ()Ljava/lang/Object;
/*     */     //   346: checkcast java/lang/String
/*     */     //   349: astore #7
/*     */     //   351: aload #5
/*     */     //   353: aload #7
/*     */     //   355: invokevirtual add : (Ljava/lang/String;)V
/*     */     //   358: goto -> 329
/*     */     //   361: aload_2
/*     */     //   362: ldc_w 'redirect'
/*     */     //   365: aload #5
/*     */     //   367: invokevirtual add : (Ljava/lang/String;Lcom/google/gson/JsonElement;)V
/*     */     //   370: aload_2
/*     */     //   371: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #68	-> 0
/*     */     //   #70	-> 8
/*     */     //   #71	-> 52
/*     */     //   #72	-> 69
/*     */     //   #73	-> 86
/*     */     //   #74	-> 92
/*     */     //   #76	-> 104
/*     */     //   #77	-> 119
/*     */     //   #81	-> 127
/*     */     //   #82	-> 132
/*     */     //   #83	-> 141
/*     */     //   #84	-> 150
/*     */     //   #85	-> 180
/*     */     //   #86	-> 196
/*     */     //   #87	-> 199
/*     */     //   #90	-> 207
/*     */     //   #91	-> 214
/*     */     //   #94	-> 224
/*     */     //   #95	-> 245
/*     */     //   #96	-> 266
/*     */     //   #97	-> 276
/*     */     //   #100	-> 284
/*     */     //   #101	-> 291
/*     */     //   #102	-> 301
/*     */     //   #103	-> 311
/*     */     //   #104	-> 320
/*     */     //   #105	-> 351
/*     */     //   #106	-> 358
/*     */     //   #107	-> 361
/*     */     //   #111	-> 370
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
/*     */   public static <T> Set<ArgumentType<?>> findUsedArgumentTypes(CommandNode<T> paramCommandNode) {
/* 115 */     ReferenceOpenHashSet referenceOpenHashSet = new ReferenceOpenHashSet();
/* 116 */     HashSet<ArgumentType<?>> hashSet = new HashSet();
/* 117 */     findUsedArgumentTypes(paramCommandNode, hashSet, (Set<CommandNode<T>>)referenceOpenHashSet);
/* 118 */     return hashSet;
/*     */   }
/*     */   
/*     */   private static <T> void findUsedArgumentTypes(CommandNode<T> paramCommandNode, Set<ArgumentType<?>> paramSet, Set<CommandNode<T>> paramSet1) {
/* 122 */     if (!paramSet1.add(paramCommandNode)) {
/*     */       return;
/*     */     }
/*     */     
/* 126 */     if (paramCommandNode instanceof ArgumentCommandNode) { ArgumentCommandNode argumentCommandNode = (ArgumentCommandNode)paramCommandNode;
/* 127 */       paramSet.add(argumentCommandNode.getType()); }
/*     */ 
/*     */     
/* 130 */     paramCommandNode.getChildren().forEach(paramCommandNode -> findUsedArgumentTypes(paramCommandNode, paramSet1, paramSet2));
/* 131 */     CommandNode<T> commandNode = paramCommandNode.getRedirect();
/* 132 */     if (commandNode != null)
/* 133 */       findUsedArgumentTypes(commandNode, paramSet, paramSet1); 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\synchronization\ArgumentUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */