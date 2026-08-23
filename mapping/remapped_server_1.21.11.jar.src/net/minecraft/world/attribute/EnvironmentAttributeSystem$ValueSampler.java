/*     */ package net.minecraft.world.attribute;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ValueSampler<Value>
/*     */ {
/*     */   private final EnvironmentAttribute<Value> attribute;
/*     */   final Value baseValue;
/*     */   private final List<EnvironmentAttributeLayer<Value>> layers;
/*     */   final boolean isAffectedByPosition;
/*     */   private Value cachedTickValue;
/*     */   private int cacheTickId;
/*     */   
/*     */   ValueSampler(EnvironmentAttribute<Value> paramEnvironmentAttribute, Value paramValue, List<EnvironmentAttributeLayer<Value>> paramList, boolean paramBoolean) {
/* 204 */     this.attribute = paramEnvironmentAttribute;
/* 205 */     this.baseValue = paramValue;
/* 206 */     this.layers = paramList;
/* 207 */     this.isAffectedByPosition = paramBoolean;
/*     */   }
/*     */   
/*     */   public void invalidateTickCache() {
/* 211 */     this.cachedTickValue = null;
/* 212 */     this.cacheTickId++;
/*     */   }
/*     */   
/*     */   public Value getDimensionValue() {
/* 216 */     if (this.cachedTickValue != null) {
/* 217 */       return this.cachedTickValue;
/*     */     }
/* 219 */     Value value = computeValueNotPositional();
/* 220 */     this.cachedTickValue = value;
/* 221 */     return value;
/*     */   }
/*     */ 
/*     */   
/*     */   public Value getValue(Vec3 paramVec3, SpatialAttributeInterpolator paramSpatialAttributeInterpolator) {
/* 226 */     if (!this.isAffectedByPosition) {
/* 227 */       return getDimensionValue();
/*     */     }
/* 229 */     return computeValuePositional(paramVec3, paramSpatialAttributeInterpolator);
/*     */   }
/*     */   
/*     */   private Value computeValuePositional(Vec3 paramVec3, SpatialAttributeInterpolator paramSpatialAttributeInterpolator) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield baseValue : Ljava/lang/Object;
/*     */     //   4: astore_3
/*     */     //   5: aload_0
/*     */     //   6: getfield layers : Ljava/util/List;
/*     */     //   9: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */     //   14: astore #4
/*     */     //   16: aload #4
/*     */     //   18: invokeinterface hasNext : ()Z
/*     */     //   23: ifeq -> 170
/*     */     //   26: aload #4
/*     */     //   28: invokeinterface next : ()Ljava/lang/Object;
/*     */     //   33: checkcast net/minecraft/world/attribute/EnvironmentAttributeLayer
/*     */     //   36: astore #5
/*     */     //   38: aload #5
/*     */     //   40: dup
/*     */     //   41: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   44: pop
/*     */     //   45: astore #6
/*     */     //   47: iconst_0
/*     */     //   48: istore #7
/*     */     //   50: aload #6
/*     */     //   52: iload #7
/*     */     //   54: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */     //   59: tableswitch default -> 84, 0 -> 94, 1 -> 115, 2 -> 140
/*     */     //   84: new java/lang/MatchException
/*     */     //   87: dup
/*     */     //   88: aconst_null
/*     */     //   89: aconst_null
/*     */     //   90: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   93: athrow
/*     */     //   94: aload #6
/*     */     //   96: checkcast net/minecraft/world/attribute/EnvironmentAttributeLayer$Constant
/*     */     //   99: astore #8
/*     */     //   101: aload #8
/*     */     //   103: aload_3
/*     */     //   104: invokeinterface applyConstant : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   109: checkcast java/lang/Object
/*     */     //   112: goto -> 166
/*     */     //   115: aload #6
/*     */     //   117: checkcast net/minecraft/world/attribute/EnvironmentAttributeLayer$TimeBased
/*     */     //   120: astore #9
/*     */     //   122: aload #9
/*     */     //   124: aload_3
/*     */     //   125: aload_0
/*     */     //   126: getfield cacheTickId : I
/*     */     //   129: invokeinterface applyTimeBased : (Ljava/lang/Object;I)Ljava/lang/Object;
/*     */     //   134: checkcast java/lang/Object
/*     */     //   137: goto -> 166
/*     */     //   140: aload #6
/*     */     //   142: checkcast net/minecraft/world/attribute/EnvironmentAttributeLayer$Positional
/*     */     //   145: astore #10
/*     */     //   147: aload #10
/*     */     //   149: aload_3
/*     */     //   150: aload_1
/*     */     //   151: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   154: checkcast net/minecraft/world/phys/Vec3
/*     */     //   157: aload_2
/*     */     //   158: invokeinterface applyPositional : (Ljava/lang/Object;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/attribute/SpatialAttributeInterpolator;)Ljava/lang/Object;
/*     */     //   163: checkcast java/lang/Object
/*     */     //   166: astore_3
/*     */     //   167: goto -> 16
/*     */     //   170: aload_0
/*     */     //   171: getfield attribute : Lnet/minecraft/world/attribute/EnvironmentAttribute;
/*     */     //   174: aload_3
/*     */     //   175: invokevirtual sanitizeValue : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   178: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #233	-> 0
/*     */     //   #234	-> 5
/*     */     //   #235	-> 38
/*     */     //   #236	-> 94
/*     */     //   #237	-> 115
/*     */     //   #238	-> 140
/*     */     //   #240	-> 167
/*     */     //   #241	-> 170
/*     */   }
/*     */   
/*     */   private Value computeValueNotPositional() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield baseValue : Ljava/lang/Object;
/*     */     //   4: astore_1
/*     */     //   5: aload_0
/*     */     //   6: getfield layers : Ljava/util/List;
/*     */     //   9: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */     //   14: astore_2
/*     */     //   15: aload_2
/*     */     //   16: invokeinterface hasNext : ()Z
/*     */     //   21: ifeq -> 151
/*     */     //   24: aload_2
/*     */     //   25: invokeinterface next : ()Ljava/lang/Object;
/*     */     //   30: checkcast net/minecraft/world/attribute/EnvironmentAttributeLayer
/*     */     //   33: astore_3
/*     */     //   34: aload_3
/*     */     //   35: dup
/*     */     //   36: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   39: pop
/*     */     //   40: astore #4
/*     */     //   42: iconst_0
/*     */     //   43: istore #5
/*     */     //   45: aload #4
/*     */     //   47: iload #5
/*     */     //   49: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */     //   54: tableswitch default -> 80, 0 -> 90, 1 -> 111, 2 -> 136
/*     */     //   80: new java/lang/MatchException
/*     */     //   83: dup
/*     */     //   84: aconst_null
/*     */     //   85: aconst_null
/*     */     //   86: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   89: athrow
/*     */     //   90: aload #4
/*     */     //   92: checkcast net/minecraft/world/attribute/EnvironmentAttributeLayer$Constant
/*     */     //   95: astore #6
/*     */     //   97: aload #6
/*     */     //   99: aload_1
/*     */     //   100: invokeinterface applyConstant : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   105: checkcast java/lang/Object
/*     */     //   108: goto -> 147
/*     */     //   111: aload #4
/*     */     //   113: checkcast net/minecraft/world/attribute/EnvironmentAttributeLayer$TimeBased
/*     */     //   116: astore #7
/*     */     //   118: aload #7
/*     */     //   120: aload_1
/*     */     //   121: aload_0
/*     */     //   122: getfield cacheTickId : I
/*     */     //   125: invokeinterface applyTimeBased : (Ljava/lang/Object;I)Ljava/lang/Object;
/*     */     //   130: checkcast java/lang/Object
/*     */     //   133: goto -> 147
/*     */     //   136: aload #4
/*     */     //   138: checkcast net/minecraft/world/attribute/EnvironmentAttributeLayer$Positional
/*     */     //   141: astore #8
/*     */     //   143: aload_1
/*     */     //   144: checkcast java/lang/Object
/*     */     //   147: astore_1
/*     */     //   148: goto -> 15
/*     */     //   151: aload_0
/*     */     //   152: getfield attribute : Lnet/minecraft/world/attribute/EnvironmentAttribute;
/*     */     //   155: aload_1
/*     */     //   156: invokevirtual sanitizeValue : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   159: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #245	-> 0
/*     */     //   #246	-> 5
/*     */     //   #247	-> 34
/*     */     //   #248	-> 90
/*     */     //   #249	-> 111
/*     */     //   #251	-> 136
/*     */     //   #253	-> 148
/*     */     //   #254	-> 151
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\attribute\EnvironmentAttributeSystem$ValueSampler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */