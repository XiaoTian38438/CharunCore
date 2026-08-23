/*     */ package net.minecraft.world.level.storage;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class TypedListWrapper<T>
/*     */   implements ValueOutput.TypedOutputList<T>
/*     */ {
/*     */   private final ProblemReporter problemReporter;
/*     */   private final String name;
/*     */   private final DynamicOps<Tag> ops;
/*     */   private final Codec<T> codec;
/*     */   private final ListTag output;
/*     */   
/*     */   TypedListWrapper(ProblemReporter paramProblemReporter, String paramString, DynamicOps<Tag> paramDynamicOps, Codec<T> paramCodec, ListTag paramListTag) {
/* 187 */     this.problemReporter = paramProblemReporter;
/* 188 */     this.name = paramString;
/* 189 */     this.ops = paramDynamicOps;
/* 190 */     this.codec = paramCodec;
/* 191 */     this.output = paramListTag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(T paramT) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield codec : Lcom/mojang/serialization/Codec;
/*     */     //   4: aload_0
/*     */     //   5: getfield ops : Lcom/mojang/serialization/DynamicOps;
/*     */     //   8: aload_1
/*     */     //   9: invokeinterface encodeStart : (Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;
/*     */     //   14: dup
/*     */     //   15: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   18: pop
/*     */     //   19: astore_2
/*     */     //   20: iconst_0
/*     */     //   21: istore_3
/*     */     //   22: aload_2
/*     */     //   23: iload_3
/*     */     //   24: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */     //   29: lookupswitch default -> 56, 0 -> 66, 1 -> 91
/*     */     //   56: new java/lang/MatchException
/*     */     //   59: dup
/*     */     //   60: aconst_null
/*     */     //   61: aconst_null
/*     */     //   62: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   65: athrow
/*     */     //   66: aload_2
/*     */     //   67: checkcast com/mojang/serialization/DataResult$Success
/*     */     //   70: astore #4
/*     */     //   72: aload_0
/*     */     //   73: getfield output : Lnet/minecraft/nbt/ListTag;
/*     */     //   76: aload #4
/*     */     //   78: invokevirtual value : ()Ljava/lang/Object;
/*     */     //   81: checkcast net/minecraft/nbt/Tag
/*     */     //   84: invokevirtual add : (Ljava/lang/Object;)Z
/*     */     //   87: pop
/*     */     //   88: goto -> 142
/*     */     //   91: aload_2
/*     */     //   92: checkcast com/mojang/serialization/DataResult$Error
/*     */     //   95: astore #5
/*     */     //   97: aload_0
/*     */     //   98: getfield problemReporter : Lnet/minecraft/util/ProblemReporter;
/*     */     //   101: new net/minecraft/world/level/storage/TagValueOutput$EncodeToListFailedProblem
/*     */     //   104: dup
/*     */     //   105: aload_0
/*     */     //   106: getfield name : Ljava/lang/String;
/*     */     //   109: aload_1
/*     */     //   110: aload #5
/*     */     //   112: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Object;Lcom/mojang/serialization/DataResult$Error;)V
/*     */     //   115: invokeinterface report : (Lnet/minecraft/util/ProblemReporter$Problem;)V
/*     */     //   120: aload #5
/*     */     //   122: invokevirtual partialValue : ()Ljava/util/Optional;
/*     */     //   125: aload_0
/*     */     //   126: getfield output : Lnet/minecraft/nbt/ListTag;
/*     */     //   129: dup
/*     */     //   130: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   133: pop
/*     */     //   134: <illegal opcode> accept : (Lnet/minecraft/nbt/ListTag;)Ljava/util/function/Consumer;
/*     */     //   139: invokevirtual ifPresent : (Ljava/util/function/Consumer;)V
/*     */     //   142: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #196	-> 0
/*     */     //   #197	-> 66
/*     */     //   #198	-> 91
/*     */     //   #199	-> 97
/*     */     //   #200	-> 120
/*     */     //   #203	-> 142
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 207 */     return this.output.isEmpty();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\TagValueOutput$TypedListWrapper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */