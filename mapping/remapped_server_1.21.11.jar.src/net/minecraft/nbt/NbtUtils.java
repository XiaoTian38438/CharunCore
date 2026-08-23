/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.Comparators;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ public final class NbtUtils
/*     */ {
/*     */   private static final Comparator<ListTag> YXZ_LISTTAG_INT_COMPARATOR;
/*     */   private static final Comparator<ListTag> YXZ_LISTTAG_DOUBLE_COMPARATOR;
/*     */   
/*     */   static {
/*  44 */     YXZ_LISTTAG_INT_COMPARATOR = Comparator.<ListTag>comparingInt(paramListTag -> paramListTag.getIntOr(1, 0)).thenComparingInt(paramListTag -> paramListTag.getIntOr(0, 0)).thenComparingInt(paramListTag -> paramListTag.getIntOr(2, 0));
/*  45 */     YXZ_LISTTAG_DOUBLE_COMPARATOR = Comparator.<ListTag>comparingDouble(paramListTag -> paramListTag.getDoubleOr(1, 0.0D)).thenComparingDouble(paramListTag -> paramListTag.getDoubleOr(0, 0.0D)).thenComparingDouble(paramListTag -> paramListTag.getDoubleOr(2, 0.0D));
/*     */   }
/*  47 */   private static final Codec<ResourceKey<Block>> BLOCK_NAME_CODEC = ResourceKey.codec(Registries.BLOCK);
/*     */   
/*     */   public static final String SNBT_DATA_TAG = "data";
/*     */   
/*     */   private static final char PROPERTIES_START = '{';
/*     */   private static final char PROPERTIES_END = '}';
/*     */   private static final String ELEMENT_SEPARATOR = ",";
/*     */   private static final char KEY_VALUE_SEPARATOR = ':';
/*  55 */   private static final Splitter COMMA_SPLITTER = Splitter.on(",");
/*  56 */   private static final Splitter COLON_SPLITTER = Splitter.on(':').limit(2);
/*     */   
/*  58 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int INDENT = 2;
/*     */   
/*     */   private static final int NOT_FOUND = -1;
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   public static boolean compareNbt(Tag paramTag1, Tag paramTag2, boolean paramBoolean) {
/*  67 */     if (paramTag1 == paramTag2) {
/*  68 */       return true;
/*     */     }
/*  70 */     if (paramTag1 == null) {
/*  71 */       return true;
/*     */     }
/*  73 */     if (paramTag2 == null) {
/*  74 */       return false;
/*     */     }
/*  76 */     if (!paramTag1.getClass().equals(paramTag2.getClass())) {
/*  77 */       return false;
/*     */     }
/*     */     
/*  80 */     if (paramTag1 instanceof CompoundTag) { CompoundTag compoundTag1 = (CompoundTag)paramTag1;
/*  81 */       CompoundTag compoundTag2 = (CompoundTag)paramTag2;
/*     */       
/*  83 */       if (compoundTag2.size() < compoundTag1.size()) {
/*  84 */         return false;
/*     */       }
/*     */       
/*  87 */       for (Map.Entry<String, Tag> entry : compoundTag1.entrySet()) {
/*  88 */         Tag tag = (Tag)entry.getValue();
/*  89 */         if (!compareNbt(tag, compoundTag2.get((String)entry.getKey()), paramBoolean)) {
/*  90 */           return false;
/*     */         }
/*     */       } 
/*     */       
/*  94 */       return true; }
/*  95 */      if (paramTag1 instanceof ListTag) { ListTag listTag = (ListTag)paramTag1; if (paramBoolean) {
/*  96 */         ListTag listTag1 = (ListTag)paramTag2;
/*     */         
/*  98 */         if (listTag.isEmpty()) {
/*  99 */           return listTag1.isEmpty();
/*     */         }
/*     */         
/* 102 */         if (listTag1.size() < listTag.size()) {
/* 103 */           return false;
/*     */         }
/*     */         
/* 106 */         for (Tag tag : listTag) {
/* 107 */           boolean bool = false;
/* 108 */           for (Tag tag1 : listTag1) {
/* 109 */             if (compareNbt(tag, tag1, paramBoolean)) {
/* 110 */               bool = true;
/*     */               break;
/*     */             } 
/*     */           } 
/* 114 */           if (!bool) {
/* 115 */             return false;
/*     */           }
/*     */         } 
/*     */         
/* 119 */         return true;
/*     */       }  }
/* 121 */      return paramTag1.equals(paramTag2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static BlockState readBlockState(HolderGetter<Block> paramHolderGetter, CompoundTag paramCompoundTag) {
/* 126 */     Objects.requireNonNull(paramHolderGetter); Optional<Holder> optional = paramCompoundTag.<ResourceKey<Block>>read("Name", BLOCK_NAME_CODEC).flatMap(paramHolderGetter::get);
/* 127 */     if (optional.isEmpty()) {
/* 128 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/* 131 */     Block block = (Block)((Holder)optional.get()).value();
/* 132 */     BlockState blockState = block.defaultBlockState();
/*     */     
/* 134 */     Optional<CompoundTag> optional1 = paramCompoundTag.getCompound("Properties");
/* 135 */     if (optional1.isPresent()) {
/* 136 */       StateDefinition stateDefinition = block.getStateDefinition();
/* 137 */       for (String str : ((CompoundTag)optional1.get()).keySet()) {
/* 138 */         Property<Comparable> property = stateDefinition.getProperty(str);
/* 139 */         if (property != null) {
/* 140 */           blockState = setValueHelper(blockState, property, str, optional1.get(), paramCompoundTag);
/*     */         }
/*     */       } 
/*     */     } 
/* 144 */     return blockState;
/*     */   }
/*     */ 
/*     */   
/*     */   private static <S extends net.minecraft.world.level.block.state.StateHolder<?, S>, T extends Comparable<T>> S setValueHelper(S paramS, Property<T> paramProperty, String paramString, CompoundTag paramCompoundTag1, CompoundTag paramCompoundTag2) {
/* 149 */     Objects.requireNonNull(paramProperty); Optional<?> optional = paramCompoundTag1.getString(paramString).flatMap(paramProperty::getValue);
/* 150 */     if (optional.isPresent()) {
/* 151 */       return (S)paramS.setValue(paramProperty, (Comparable)optional.get());
/*     */     }
/*     */     
/* 154 */     LOGGER.warn("Unable to read property: {} with value: {} for blockstate: {}", new Object[] { paramString, paramCompoundTag1.get(paramString), paramCompoundTag2 });
/* 155 */     return paramS;
/*     */   }
/*     */   
/*     */   public static CompoundTag writeBlockState(BlockState paramBlockState) {
/* 159 */     CompoundTag compoundTag = new CompoundTag();
/* 160 */     compoundTag.putString("Name", BuiltInRegistries.BLOCK.getKey(paramBlockState.getBlock()).toString());
/*     */     
/* 162 */     Map map = paramBlockState.getValues();
/* 163 */     if (!map.isEmpty()) {
/* 164 */       CompoundTag compoundTag1 = new CompoundTag();
/*     */       
/* 166 */       for (Map.Entry entry : map.entrySet()) {
/* 167 */         Property<Comparable> property = (Property)entry.getKey();
/* 168 */         compoundTag1.putString(property.getName(), getName(property, (Comparable)entry.getValue()));
/*     */       } 
/* 170 */       compoundTag.put("Properties", compoundTag1);
/*     */     } 
/*     */     
/* 173 */     return compoundTag;
/*     */   }
/*     */   
/*     */   public static CompoundTag writeFluidState(FluidState paramFluidState) {
/* 177 */     CompoundTag compoundTag = new CompoundTag();
/* 178 */     compoundTag.putString("Name", BuiltInRegistries.FLUID.getKey(paramFluidState.getType()).toString());
/*     */     
/* 180 */     Map map = paramFluidState.getValues();
/* 181 */     if (!map.isEmpty()) {
/* 182 */       CompoundTag compoundTag1 = new CompoundTag();
/*     */       
/* 184 */       for (Map.Entry entry : map.entrySet()) {
/* 185 */         Property<Comparable> property = (Property)entry.getKey();
/* 186 */         compoundTag1.putString(property.getName(), getName(property, (Comparable)entry.getValue()));
/*     */       } 
/* 188 */       compoundTag.put("Properties", compoundTag1);
/*     */     } 
/*     */     
/* 191 */     return compoundTag;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T extends Comparable<T>> String getName(Property<T> paramProperty, Comparable<?> paramComparable) {
/* 197 */     return paramProperty.getName(paramComparable);
/*     */   }
/*     */   
/*     */   public static String prettyPrint(Tag paramTag) {
/* 201 */     return prettyPrint(paramTag, false);
/*     */   }
/*     */   
/*     */   public static String prettyPrint(Tag paramTag, boolean paramBoolean) {
/* 205 */     return prettyPrint(new StringBuilder(), paramTag, 0, paramBoolean).toString();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static StringBuilder prettyPrint(StringBuilder paramStringBuilder, Tag paramTag, int paramInt, boolean paramBoolean) {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: dup
/*     */     //   2: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   5: pop
/*     */     //   6: astore #4
/*     */     //   8: iconst_0
/*     */     //   9: istore #5
/*     */     //   11: aload #4
/*     */     //   13: iload #5
/*     */     //   15: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */     //   20: tableswitch default -> 64, 0 -> 74, 1 -> 90, 2 -> 101, 3 -> 310, 4 -> 445, 5 -> 720, 6 -> 968
/*     */     //   64: new java/lang/MatchException
/*     */     //   67: dup
/*     */     //   68: aconst_null
/*     */     //   69: aconst_null
/*     */     //   70: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   73: athrow
/*     */     //   74: aload #4
/*     */     //   76: checkcast net/minecraft/nbt/PrimitiveTag
/*     */     //   79: astore #6
/*     */     //   81: aload_0
/*     */     //   82: aload #6
/*     */     //   84: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*     */     //   87: goto -> 1245
/*     */     //   90: aload #4
/*     */     //   92: checkcast net/minecraft/nbt/EndTag
/*     */     //   95: astore #7
/*     */     //   97: aload_0
/*     */     //   98: goto -> 1245
/*     */     //   101: aload #4
/*     */     //   103: checkcast net/minecraft/nbt/ByteArrayTag
/*     */     //   106: astore #8
/*     */     //   108: aload #8
/*     */     //   110: invokevirtual getAsByteArray : ()[B
/*     */     //   113: astore #9
/*     */     //   115: aload #9
/*     */     //   117: arraylength
/*     */     //   118: istore #10
/*     */     //   120: iload_2
/*     */     //   121: aload_0
/*     */     //   122: invokestatic indent : (ILjava/lang/StringBuilder;)Ljava/lang/StringBuilder;
/*     */     //   125: ldc_w 'byte['
/*     */     //   128: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   131: iload #10
/*     */     //   133: invokevirtual append : (I)Ljava/lang/StringBuilder;
/*     */     //   136: ldc_w '] {\\n'
/*     */     //   139: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   142: pop
/*     */     //   143: iload_3
/*     */     //   144: ifeq -> 274
/*     */     //   147: iload_2
/*     */     //   148: iconst_1
/*     */     //   149: iadd
/*     */     //   150: aload_0
/*     */     //   151: invokestatic indent : (ILjava/lang/StringBuilder;)Ljava/lang/StringBuilder;
/*     */     //   154: pop
/*     */     //   155: iconst_0
/*     */     //   156: istore #11
/*     */     //   158: iload #11
/*     */     //   160: aload #9
/*     */     //   162: arraylength
/*     */     //   163: if_icmpge -> 271
/*     */     //   166: iload #11
/*     */     //   168: ifeq -> 178
/*     */     //   171: aload_0
/*     */     //   172: bipush #44
/*     */     //   174: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   177: pop
/*     */     //   178: iload #11
/*     */     //   180: bipush #16
/*     */     //   182: irem
/*     */     //   183: ifne -> 220
/*     */     //   186: iload #11
/*     */     //   188: bipush #16
/*     */     //   190: idiv
/*     */     //   191: ifle -> 220
/*     */     //   194: aload_0
/*     */     //   195: bipush #10
/*     */     //   197: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   200: pop
/*     */     //   201: iload #11
/*     */     //   203: aload #9
/*     */     //   205: arraylength
/*     */     //   206: if_icmpge -> 232
/*     */     //   209: iload_2
/*     */     //   210: iconst_1
/*     */     //   211: iadd
/*     */     //   212: aload_0
/*     */     //   213: invokestatic indent : (ILjava/lang/StringBuilder;)Ljava/lang/StringBuilder;
/*     */     //   216: pop
/*     */     //   217: goto -> 232
/*     */     //   220: iload #11
/*     */     //   222: ifeq -> 232
/*     */     //   225: aload_0
/*     */     //   226: bipush #32
/*     */     //   228: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   231: pop
/*     */     //   232: aload_0
/*     */     //   233: getstatic java/util/Locale.ROOT : Ljava/util/Locale;
/*     */     //   236: ldc_w '0x%02X'
/*     */     //   239: iconst_1
/*     */     //   240: anewarray java/lang/Object
/*     */     //   243: dup
/*     */     //   244: iconst_0
/*     */     //   245: aload #9
/*     */     //   247: iload #11
/*     */     //   249: baload
/*     */     //   250: sipush #255
/*     */     //   253: iand
/*     */     //   254: invokestatic valueOf : (I)Ljava/lang/Integer;
/*     */     //   257: aastore
/*     */     //   258: invokestatic format : (Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   261: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   264: pop
/*     */     //   265: iinc #11, 1
/*     */     //   268: goto -> 158
/*     */     //   271: goto -> 288
/*     */     //   274: iload_2
/*     */     //   275: iconst_1
/*     */     //   276: iadd
/*     */     //   277: aload_0
/*     */     //   278: invokestatic indent : (ILjava/lang/StringBuilder;)Ljava/lang/StringBuilder;
/*     */     //   281: ldc_w ' // Skipped, supply withBinaryBlobs true'
/*     */     //   284: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   287: pop
/*     */     //   288: aload_0
/*     */     //   289: bipush #10
/*     */     //   291: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   294: pop
/*     */     //   295: iload_2
/*     */     //   296: aload_0
/*     */     //   297: invokestatic indent : (ILjava/lang/StringBuilder;)Ljava/lang/StringBuilder;
/*     */     //   300: bipush #125
/*     */     //   302: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   305: pop
/*     */     //   306: aload_0
/*     */     //   307: goto -> 1245
/*     */     //   310: aload #4
/*     */     //   312: checkcast net/minecraft/nbt/ListTag
/*     */     //   315: astore #9
/*     */     //   317: aload #9
/*     */     //   319: invokevirtual size : ()I
/*     */     //   322: istore #10
/*     */     //   324: iload_2
/*     */     //   325: aload_0
/*     */     //   326: invokestatic indent : (ILjava/lang/StringBuilder;)Ljava/lang/StringBuilder;
/*     */     //   329: ldc_w 'list'
/*     */     //   332: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   335: ldc_w '['
/*     */     //   338: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   341: iload #10
/*     */     //   343: invokevirtual append : (I)Ljava/lang/StringBuilder;
/*     */     //   346: ldc_w '] ['
/*     */     //   349: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   352: pop
/*     */     //   353: iload #10
/*     */     //   355: ifeq -> 365
/*     */     //   358: aload_0
/*     */     //   359: bipush #10
/*     */     //   361: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   364: pop
/*     */     //   365: iconst_0
/*     */     //   366: istore #11
/*     */     //   368: iload #11
/*     */     //   370: iload #10
/*     */     //   372: if_icmpge -> 418
/*     */     //   375: iload #11
/*     */     //   377: ifeq -> 388
/*     */     //   380: aload_0
/*     */     //   381: ldc_w ',\\n'
/*     */     //   384: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   387: pop
/*     */     //   388: iload_2
/*     */     //   389: iconst_1
/*     */     //   390: iadd
/*     */     //   391: aload_0
/*     */     //   392: invokestatic indent : (ILjava/lang/StringBuilder;)Ljava/lang/StringBuilder;
/*     */     //   395: pop
/*     */     //   396: aload_0
/*     */     //   397: aload #9
/*     */     //   399: iload #11
/*     */     //   401: invokevirtual get : (I)Lnet/minecraft/nbt/Tag;
/*     */     //   404: iload_2
/*     */     //   405: iconst_1
/*     */     //   406: iadd
/*     */     //   407: iload_3
/*     */     //   408: invokestatic prettyPrint : (Ljava/lang/StringBuilder;Lnet/minecraft/nbt/Tag;IZ)Ljava/lang/StringBuilder;
/*     */     //   411: pop
/*     */     //   412: iinc #11, 1
/*     */     //   415: goto -> 368
/*     */     //   418: iload #10
/*     */     //   420: ifeq -> 430
/*     */     //   423: aload_0
/*     */     //   424: bipush #10
/*     */     //   426: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   429: pop
/*     */     //   430: iload_2
/*     */     //   431: aload_0
/*     */     //   432: invokestatic indent : (ILjava/lang/StringBuilder;)Ljava/lang/StringBuilder;
/*     */     //   435: bipush #93
/*     */     //   437: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   440: pop
/*     */     //   441: aload_0
/*     */     //   442: goto -> 1245
/*     */     //   445: aload #4
/*     */     //   447: checkcast net/minecraft/nbt/IntArrayTag
/*     */     //   450: astore #10
/*     */     //   452: aload #10
/*     */     //   454: invokevirtual getAsIntArray : ()[I
/*     */     //   457: astore #11
/*     */     //   459: iconst_0
/*     */     //   460: istore #12
/*     */     //   462: aload #11
/*     */     //   464: astore #13
/*     */     //   466: aload #13
/*     */     //   468: arraylength
/*     */     //   469: istore #14
/*     */     //   471: iconst_0
/*     */     //   472: istore #15
/*     */     //   474: iload #15
/*     */     //   476: iload #14
/*     */     //   478: if_icmpge -> 525
/*     */     //   481: aload #13
/*     */     //   483: iload #15
/*     */     //   485: iaload
/*     */     //   486: istore #16
/*     */     //   488: iload #12
/*     */     //   490: getstatic java/util/Locale.ROOT : Ljava/util/Locale;
/*     */     //   493: ldc_w '%X'
/*     */     //   496: iconst_1
/*     */     //   497: anewarray java/lang/Object
/*     */     //   500: dup
/*     */     //   501: iconst_0
/*     */     //   502: iload #16
/*     */     //   504: invokestatic valueOf : (I)Ljava/lang/Integer;
/*     */     //   507: aastore
/*     */     //   508: invokestatic format : (Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   511: invokevirtual length : ()I
/*     */     //   514: invokestatic max : (II)I
/*     */     //   517: istore #12
/*     */     //   519: iinc #15, 1
/*     */     //   522: goto -> 474
/*     */     //   525: aload #11
/*     */     //   527: arraylength
/*     */     //   528: istore #13
/*     */     //   530: iload_2
/*     */     //   531: aload_0
/*     */     //   532: invokestatic indent : (ILjava/lang/StringBuilder;)Ljava/lang/StringBuilder;
/*     */     //   535: ldc_w 'int['
/*     */     //   538: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   541: iload #13
/*     */     //   543: invokevirtual append : (I)Ljava/lang/StringBuilder;
/*     */     //   546: ldc_w '] {\\n'
/*     */     //   549: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   552: pop
/*     */     //   553: iload_3
/*     */     //   554: ifeq -> 684
/*     */     //   557: iload_2
/*     */     //   558: iconst_1
/*     */     //   559: iadd
/*     */     //   560: aload_0
/*     */     //   561: invokestatic indent : (ILjava/lang/StringBuilder;)Ljava/lang/StringBuilder;
/*     */     //   564: pop
/*     */     //   565: iconst_0
/*     */     //   566: istore #14
/*     */     //   568: iload #14
/*     */     //   570: aload #11
/*     */     //   572: arraylength
/*     */     //   573: if_icmpge -> 681
/*     */     //   576: iload #14
/*     */     //   578: ifeq -> 588
/*     */     //   581: aload_0
/*     */     //   582: bipush #44
/*     */     //   584: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   587: pop
/*     */     //   588: iload #14
/*     */     //   590: bipush #16
/*     */     //   592: irem
/*     */     //   593: ifne -> 630
/*     */     //   596: iload #14
/*     */     //   598: bipush #16
/*     */     //   600: idiv
/*     */     //   601: ifle -> 630
/*     */     //   604: aload_0
/*     */     //   605: bipush #10
/*     */     //   607: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   610: pop
/*     */     //   611: iload #14
/*     */     //   613: aload #11
/*     */     //   615: arraylength
/*     */     //   616: if_icmpge -> 642
/*     */     //   619: iload_2
/*     */     //   620: iconst_1
/*     */     //   621: iadd
/*     */     //   622: aload_0
/*     */     //   623: invokestatic indent : (ILjava/lang/StringBuilder;)Ljava/lang/StringBuilder;
/*     */     //   626: pop
/*     */     //   627: goto -> 642
/*     */     //   630: iload #14
/*     */     //   632: ifeq -> 642
/*     */     //   635: aload_0
/*     */     //   636: bipush #32
/*     */     //   638: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   641: pop
/*     */     //   642: aload_0
/*     */     //   643: getstatic java/util/Locale.ROOT : Ljava/util/Locale;
/*     */     //   646: iload #12
/*     */     //   648: <illegal opcode> makeConcatWithConstants : (I)Ljava/lang/String;
/*     */     //   653: iconst_1
/*     */     //   654: anewarray java/lang/Object
/*     */     //   657: dup
/*     */     //   658: iconst_0
/*     */     //   659: aload #11
/*     */     //   661: iload #14
/*     */     //   663: iaload
/*     */     //   664: invokestatic valueOf : (I)Ljava/lang/Integer;
/*     */     //   667: aastore
/*     */     //   668: invokestatic format : (Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   671: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   674: pop
/*     */     //   675: iinc #14, 1
/*     */     //   678: goto -> 568
/*     */     //   681: goto -> 698
/*     */     //   684: iload_2
/*     */     //   685: iconst_1
/*     */     //   686: iadd
/*     */     //   687: aload_0
/*     */     //   688: invokestatic indent : (ILjava/lang/StringBuilder;)Ljava/lang/StringBuilder;
/*     */     //   691: ldc_w ' // Skipped, supply withBinaryBlobs true'
/*     */     //   694: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   697: pop
/*     */     //   698: aload_0
/*     */     //   699: bipush #10
/*     */     //   701: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   704: pop
/*     */     //   705: iload_2
/*     */     //   706: aload_0
/*     */     //   707: invokestatic indent : (ILjava/lang/StringBuilder;)Ljava/lang/StringBuilder;
/*     */     //   710: bipush #125
/*     */     //   712: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   715: pop
/*     */     //   716: aload_0
/*     */     //   717: goto -> 1245
/*     */     //   720: aload #4
/*     */     //   722: checkcast net/minecraft/nbt/CompoundTag
/*     */     //   725: astore #11
/*     */     //   727: aload #11
/*     */     //   729: invokevirtual keySet : ()Ljava/util/Set;
/*     */     //   732: invokestatic newArrayList : (Ljava/lang/Iterable;)Ljava/util/ArrayList;
/*     */     //   735: astore #12
/*     */     //   737: aload #12
/*     */     //   739: invokestatic sort : (Ljava/util/List;)V
/*     */     //   742: iload_2
/*     */     //   743: aload_0
/*     */     //   744: invokestatic indent : (ILjava/lang/StringBuilder;)Ljava/lang/StringBuilder;
/*     */     //   747: bipush #123
/*     */     //   749: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   752: pop
/*     */     //   753: aload_0
/*     */     //   754: invokevirtual length : ()I
/*     */     //   757: aload_0
/*     */     //   758: ldc_w '\\n'
/*     */     //   761: invokevirtual lastIndexOf : (Ljava/lang/String;)I
/*     */     //   764: isub
/*     */     //   765: iconst_2
/*     */     //   766: iload_2
/*     */     //   767: iconst_1
/*     */     //   768: iadd
/*     */     //   769: imul
/*     */     //   770: if_icmple -> 788
/*     */     //   773: aload_0
/*     */     //   774: bipush #10
/*     */     //   776: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   779: pop
/*     */     //   780: iload_2
/*     */     //   781: iconst_1
/*     */     //   782: iadd
/*     */     //   783: aload_0
/*     */     //   784: invokestatic indent : (ILjava/lang/StringBuilder;)Ljava/lang/StringBuilder;
/*     */     //   787: pop
/*     */     //   788: aload #12
/*     */     //   790: invokeinterface stream : ()Ljava/util/stream/Stream;
/*     */     //   795: <illegal opcode> applyAsInt : ()Ljava/util/function/ToIntFunction;
/*     */     //   800: invokeinterface mapToInt : (Ljava/util/function/ToIntFunction;)Ljava/util/stream/IntStream;
/*     */     //   805: invokeinterface max : ()Ljava/util/OptionalInt;
/*     */     //   810: iconst_0
/*     */     //   811: invokevirtual orElse : (I)I
/*     */     //   814: istore #13
/*     */     //   816: ldc_w ' '
/*     */     //   819: iload #13
/*     */     //   821: invokestatic repeat : (Ljava/lang/String;I)Ljava/lang/String;
/*     */     //   824: astore #14
/*     */     //   826: iconst_0
/*     */     //   827: istore #15
/*     */     //   829: iload #15
/*     */     //   831: aload #12
/*     */     //   833: invokeinterface size : ()I
/*     */     //   838: if_icmpge -> 936
/*     */     //   841: iload #15
/*     */     //   843: ifeq -> 854
/*     */     //   846: aload_0
/*     */     //   847: ldc_w ',\\n'
/*     */     //   850: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   853: pop
/*     */     //   854: aload #12
/*     */     //   856: iload #15
/*     */     //   858: invokeinterface get : (I)Ljava/lang/Object;
/*     */     //   863: checkcast java/lang/String
/*     */     //   866: astore #16
/*     */     //   868: iload_2
/*     */     //   869: iconst_1
/*     */     //   870: iadd
/*     */     //   871: aload_0
/*     */     //   872: invokestatic indent : (ILjava/lang/StringBuilder;)Ljava/lang/StringBuilder;
/*     */     //   875: bipush #34
/*     */     //   877: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   880: aload #16
/*     */     //   882: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   885: bipush #34
/*     */     //   887: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   890: aload #14
/*     */     //   892: iconst_0
/*     */     //   893: aload #14
/*     */     //   895: invokevirtual length : ()I
/*     */     //   898: aload #16
/*     */     //   900: invokevirtual length : ()I
/*     */     //   903: isub
/*     */     //   904: invokevirtual append : (Ljava/lang/CharSequence;II)Ljava/lang/StringBuilder;
/*     */     //   907: ldc_w ': '
/*     */     //   910: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   913: pop
/*     */     //   914: aload_0
/*     */     //   915: aload #11
/*     */     //   917: aload #16
/*     */     //   919: invokevirtual get : (Ljava/lang/String;)Lnet/minecraft/nbt/Tag;
/*     */     //   922: iload_2
/*     */     //   923: iconst_1
/*     */     //   924: iadd
/*     */     //   925: iload_3
/*     */     //   926: invokestatic prettyPrint : (Ljava/lang/StringBuilder;Lnet/minecraft/nbt/Tag;IZ)Ljava/lang/StringBuilder;
/*     */     //   929: pop
/*     */     //   930: iinc #15, 1
/*     */     //   933: goto -> 829
/*     */     //   936: aload #12
/*     */     //   938: invokeinterface isEmpty : ()Z
/*     */     //   943: ifne -> 953
/*     */     //   946: aload_0
/*     */     //   947: bipush #10
/*     */     //   949: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   952: pop
/*     */     //   953: iload_2
/*     */     //   954: aload_0
/*     */     //   955: invokestatic indent : (ILjava/lang/StringBuilder;)Ljava/lang/StringBuilder;
/*     */     //   958: bipush #125
/*     */     //   960: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   963: pop
/*     */     //   964: aload_0
/*     */     //   965: goto -> 1245
/*     */     //   968: aload #4
/*     */     //   970: checkcast net/minecraft/nbt/LongArrayTag
/*     */     //   973: astore #12
/*     */     //   975: aload #12
/*     */     //   977: invokevirtual getAsLongArray : ()[J
/*     */     //   980: astore #13
/*     */     //   982: lconst_0
/*     */     //   983: lstore #14
/*     */     //   985: aload #13
/*     */     //   987: astore #16
/*     */     //   989: aload #16
/*     */     //   991: arraylength
/*     */     //   992: istore #17
/*     */     //   994: iconst_0
/*     */     //   995: istore #18
/*     */     //   997: iload #18
/*     */     //   999: iload #17
/*     */     //   1001: if_icmpge -> 1049
/*     */     //   1004: aload #16
/*     */     //   1006: iload #18
/*     */     //   1008: laload
/*     */     //   1009: lstore #19
/*     */     //   1011: lload #14
/*     */     //   1013: getstatic java/util/Locale.ROOT : Ljava/util/Locale;
/*     */     //   1016: ldc_w '%X'
/*     */     //   1019: iconst_1
/*     */     //   1020: anewarray java/lang/Object
/*     */     //   1023: dup
/*     */     //   1024: iconst_0
/*     */     //   1025: lload #19
/*     */     //   1027: invokestatic valueOf : (J)Ljava/lang/Long;
/*     */     //   1030: aastore
/*     */     //   1031: invokestatic format : (Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   1034: invokevirtual length : ()I
/*     */     //   1037: i2l
/*     */     //   1038: invokestatic max : (JJ)J
/*     */     //   1041: lstore #14
/*     */     //   1043: iinc #18, 1
/*     */     //   1046: goto -> 997
/*     */     //   1049: aload #13
/*     */     //   1051: arraylength
/*     */     //   1052: i2l
/*     */     //   1053: lstore #16
/*     */     //   1055: iload_2
/*     */     //   1056: aload_0
/*     */     //   1057: invokestatic indent : (ILjava/lang/StringBuilder;)Ljava/lang/StringBuilder;
/*     */     //   1060: ldc_w 'long['
/*     */     //   1063: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   1066: lload #16
/*     */     //   1068: invokevirtual append : (J)Ljava/lang/StringBuilder;
/*     */     //   1071: ldc_w '] {\\n'
/*     */     //   1074: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   1077: pop
/*     */     //   1078: iload_3
/*     */     //   1079: ifeq -> 1209
/*     */     //   1082: iload_2
/*     */     //   1083: iconst_1
/*     */     //   1084: iadd
/*     */     //   1085: aload_0
/*     */     //   1086: invokestatic indent : (ILjava/lang/StringBuilder;)Ljava/lang/StringBuilder;
/*     */     //   1089: pop
/*     */     //   1090: iconst_0
/*     */     //   1091: istore #18
/*     */     //   1093: iload #18
/*     */     //   1095: aload #13
/*     */     //   1097: arraylength
/*     */     //   1098: if_icmpge -> 1206
/*     */     //   1101: iload #18
/*     */     //   1103: ifeq -> 1113
/*     */     //   1106: aload_0
/*     */     //   1107: bipush #44
/*     */     //   1109: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   1112: pop
/*     */     //   1113: iload #18
/*     */     //   1115: bipush #16
/*     */     //   1117: irem
/*     */     //   1118: ifne -> 1155
/*     */     //   1121: iload #18
/*     */     //   1123: bipush #16
/*     */     //   1125: idiv
/*     */     //   1126: ifle -> 1155
/*     */     //   1129: aload_0
/*     */     //   1130: bipush #10
/*     */     //   1132: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   1135: pop
/*     */     //   1136: iload #18
/*     */     //   1138: aload #13
/*     */     //   1140: arraylength
/*     */     //   1141: if_icmpge -> 1167
/*     */     //   1144: iload_2
/*     */     //   1145: iconst_1
/*     */     //   1146: iadd
/*     */     //   1147: aload_0
/*     */     //   1148: invokestatic indent : (ILjava/lang/StringBuilder;)Ljava/lang/StringBuilder;
/*     */     //   1151: pop
/*     */     //   1152: goto -> 1167
/*     */     //   1155: iload #18
/*     */     //   1157: ifeq -> 1167
/*     */     //   1160: aload_0
/*     */     //   1161: bipush #32
/*     */     //   1163: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   1166: pop
/*     */     //   1167: aload_0
/*     */     //   1168: getstatic java/util/Locale.ROOT : Ljava/util/Locale;
/*     */     //   1171: lload #14
/*     */     //   1173: <illegal opcode> makeConcatWithConstants : (J)Ljava/lang/String;
/*     */     //   1178: iconst_1
/*     */     //   1179: anewarray java/lang/Object
/*     */     //   1182: dup
/*     */     //   1183: iconst_0
/*     */     //   1184: aload #13
/*     */     //   1186: iload #18
/*     */     //   1188: laload
/*     */     //   1189: invokestatic valueOf : (J)Ljava/lang/Long;
/*     */     //   1192: aastore
/*     */     //   1193: invokestatic format : (Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   1196: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   1199: pop
/*     */     //   1200: iinc #18, 1
/*     */     //   1203: goto -> 1093
/*     */     //   1206: goto -> 1223
/*     */     //   1209: iload_2
/*     */     //   1210: iconst_1
/*     */     //   1211: iadd
/*     */     //   1212: aload_0
/*     */     //   1213: invokestatic indent : (ILjava/lang/StringBuilder;)Ljava/lang/StringBuilder;
/*     */     //   1216: ldc_w ' // Skipped, supply withBinaryBlobs true'
/*     */     //   1219: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   1222: pop
/*     */     //   1223: aload_0
/*     */     //   1224: bipush #10
/*     */     //   1226: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   1229: pop
/*     */     //   1230: iload_2
/*     */     //   1231: aload_0
/*     */     //   1232: invokestatic indent : (ILjava/lang/StringBuilder;)Ljava/lang/StringBuilder;
/*     */     //   1235: bipush #125
/*     */     //   1237: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   1240: pop
/*     */     //   1241: aload_0
/*     */     //   1242: goto -> 1245
/*     */     //   1245: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #209	-> 0
/*     */     //   #210	-> 74
/*     */     //   #211	-> 90
/*     */     //   #212	-> 101
/*     */     //   #213	-> 108
/*     */     //   #215	-> 115
/*     */     //   #217	-> 120
/*     */     //   #218	-> 143
/*     */     //   #219	-> 147
/*     */     //   #220	-> 155
/*     */     //   #221	-> 166
/*     */     //   #222	-> 171
/*     */     //   #224	-> 178
/*     */     //   #225	-> 194
/*     */     //   #226	-> 201
/*     */     //   #227	-> 209
/*     */     //   #229	-> 220
/*     */     //   #230	-> 225
/*     */     //   #232	-> 232
/*     */     //   #220	-> 265
/*     */     //   #235	-> 274
/*     */     //   #237	-> 288
/*     */     //   #238	-> 295
/*     */     //   #239	-> 306
/*     */     //   #241	-> 310
/*     */     //   #242	-> 317
/*     */     //   #243	-> 324
/*     */     //   #244	-> 353
/*     */     //   #245	-> 358
/*     */     //   #248	-> 365
/*     */     //   #249	-> 375
/*     */     //   #250	-> 380
/*     */     //   #253	-> 388
/*     */     //   #254	-> 396
/*     */     //   #248	-> 412
/*     */     //   #256	-> 418
/*     */     //   #257	-> 423
/*     */     //   #259	-> 430
/*     */     //   #260	-> 441
/*     */     //   #262	-> 445
/*     */     //   #263	-> 452
/*     */     //   #265	-> 459
/*     */     //   #266	-> 462
/*     */     //   #267	-> 488
/*     */     //   #266	-> 519
/*     */     //   #270	-> 525
/*     */     //   #272	-> 530
/*     */     //   #274	-> 553
/*     */     //   #275	-> 557
/*     */     //   #276	-> 565
/*     */     //   #277	-> 576
/*     */     //   #278	-> 581
/*     */     //   #280	-> 588
/*     */     //   #281	-> 604
/*     */     //   #282	-> 611
/*     */     //   #283	-> 619
/*     */     //   #285	-> 630
/*     */     //   #286	-> 635
/*     */     //   #288	-> 642
/*     */     //   #276	-> 675
/*     */     //   #291	-> 684
/*     */     //   #294	-> 698
/*     */     //   #295	-> 705
/*     */     //   #296	-> 716
/*     */     //   #298	-> 720
/*     */     //   #299	-> 727
/*     */     //   #300	-> 737
/*     */     //   #302	-> 742
/*     */     //   #303	-> 753
/*     */     //   #304	-> 773
/*     */     //   #305	-> 780
/*     */     //   #308	-> 788
/*     */     //   #309	-> 816
/*     */     //   #311	-> 826
/*     */     //   #312	-> 841
/*     */     //   #313	-> 846
/*     */     //   #316	-> 854
/*     */     //   #317	-> 868
/*     */     //   #318	-> 914
/*     */     //   #311	-> 930
/*     */     //   #321	-> 936
/*     */     //   #322	-> 946
/*     */     //   #324	-> 953
/*     */     //   #325	-> 964
/*     */     //   #327	-> 968
/*     */     //   #328	-> 975
/*     */     //   #330	-> 982
/*     */     //   #331	-> 985
/*     */     //   #332	-> 1011
/*     */     //   #331	-> 1043
/*     */     //   #335	-> 1049
/*     */     //   #337	-> 1055
/*     */     //   #339	-> 1078
/*     */     //   #340	-> 1082
/*     */     //   #341	-> 1090
/*     */     //   #342	-> 1101
/*     */     //   #343	-> 1106
/*     */     //   #345	-> 1113
/*     */     //   #346	-> 1129
/*     */     //   #347	-> 1136
/*     */     //   #348	-> 1144
/*     */     //   #350	-> 1155
/*     */     //   #351	-> 1160
/*     */     //   #353	-> 1167
/*     */     //   #341	-> 1200
/*     */     //   #356	-> 1209
/*     */     //   #359	-> 1223
/*     */     //   #360	-> 1230
/*     */     //   #361	-> 1241
/*     */     //   #209	-> 1245
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static StringBuilder indent(int paramInt, StringBuilder paramStringBuilder) {
/* 367 */     int i = paramStringBuilder.lastIndexOf("\n") + 1;
/* 368 */     int j = paramStringBuilder.length() - i;
/*     */     
/* 370 */     for (byte b = 0; b < 2 * paramInt - j; b++) {
/* 371 */       paramStringBuilder.append(' ');
/*     */     }
/* 373 */     return paramStringBuilder;
/*     */   }
/*     */   
/*     */   public static Component toPrettyComponent(Tag paramTag) {
/* 377 */     return (new TextComponentTagVisitor("")).visit(paramTag);
/*     */   }
/*     */   
/*     */   public static String structureToSnbt(CompoundTag paramCompoundTag) {
/* 381 */     return (new SnbtPrinterTagVisitor()).visit(packStructureTemplate(paramCompoundTag));
/*     */   }
/*     */   
/*     */   public static CompoundTag snbtToStructure(String paramString) throws CommandSyntaxException {
/* 385 */     return unpackStructureTemplate(TagParser.parseCompoundFully(paramString));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static CompoundTag packStructureTemplate(CompoundTag paramCompoundTag) {
/*     */     ListTag listTag1;
/* 393 */     Optional<ListTag> optional1 = paramCompoundTag.getList("palettes");
/* 394 */     if (optional1.isPresent()) {
/* 395 */       listTag1 = ((ListTag)optional1.get()).getListOrEmpty(0);
/*     */     } else {
/* 397 */       listTag1 = paramCompoundTag.getListOrEmpty("palette");
/*     */     } 
/*     */     
/* 400 */     ListTag listTag2 = (ListTag)listTag1.compoundStream().map(NbtUtils::packBlockState).map(StringTag::valueOf).collect(Collectors.toCollection(ListTag::new));
/*     */     
/* 402 */     paramCompoundTag.put("palette", listTag2);
/*     */ 
/*     */     
/* 405 */     if (optional1.isPresent()) {
/* 406 */       ListTag listTag = new ListTag();
/* 407 */       ((ListTag)optional1.get()).stream().flatMap(paramTag -> paramTag.asList().stream()).forEach(paramListTag3 -> {
/*     */             CompoundTag compoundTag = new CompoundTag();
/*     */             
/*     */             for (byte b = 0; b < paramListTag3.size(); b++) {
/*     */               compoundTag.putString(paramListTag1.getString(b).orElseThrow(), packBlockState(paramListTag3.getCompound(b).orElseThrow()));
/*     */             }
/*     */             paramListTag2.add(compoundTag);
/*     */           });
/* 415 */       paramCompoundTag.put("palettes", listTag);
/*     */     } 
/*     */ 
/*     */     
/* 419 */     Optional<ListTag> optional2 = paramCompoundTag.getList("entities");
/* 420 */     if (optional2.isPresent()) {
/*     */ 
/*     */       
/* 423 */       ListTag listTag = (ListTag)((ListTag)optional2.get()).compoundStream().sorted(Comparator.comparing(paramCompoundTag -> paramCompoundTag.getList("pos"), Comparators.emptiesLast(YXZ_LISTTAG_DOUBLE_COMPARATOR))).collect(Collectors.toCollection(ListTag::new));
/* 424 */       paramCompoundTag.put("entities", listTag);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 431 */     ListTag listTag3 = (ListTag)paramCompoundTag.getList("blocks").stream().flatMap(ListTag::compoundStream).sorted(Comparator.comparing(paramCompoundTag -> paramCompoundTag.getList("pos"), Comparators.emptiesLast(YXZ_LISTTAG_INT_COMPARATOR))).peek(paramCompoundTag -> paramCompoundTag.putString("state", paramListTag.getString(paramCompoundTag.getIntOr("state", 0)).orElseThrow())).collect(Collectors.toCollection(ListTag::new));
/*     */     
/* 433 */     paramCompoundTag.put("data", listTag3);
/* 434 */     paramCompoundTag.remove("blocks");
/* 435 */     return paramCompoundTag;
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static CompoundTag unpackStructureTemplate(CompoundTag paramCompoundTag) {
/* 441 */     ListTag listTag = paramCompoundTag.getListOrEmpty("palette");
/*     */ 
/*     */     
/* 444 */     Map map = (Map)listTag.stream().flatMap(paramTag -> paramTag.asString().stream()).collect(ImmutableMap.toImmutableMap(Function.identity(), NbtUtils::unpackBlockState));
/*     */     
/* 446 */     Optional<ListTag> optional1 = paramCompoundTag.getList("palettes");
/* 447 */     if (optional1.isPresent()) {
/* 448 */       paramCompoundTag.put("palettes", (Tag)((ListTag)optional1.get()).compoundStream()
/* 449 */           .map(paramCompoundTag -> (ListTag)paramMap.keySet().stream().map(()).map(NbtUtils::unpackBlockState).collect(Collectors.toCollection(ListTag::new)))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 455 */           .collect(Collectors.toCollection(ListTag::new)));
/*     */       
/* 457 */       paramCompoundTag.remove("palette");
/*     */     } else {
/* 459 */       paramCompoundTag.put("palette", (Tag)map.values().stream().collect(Collectors.toCollection(ListTag::new)));
/*     */     } 
/*     */     
/* 462 */     Optional<ListTag> optional2 = paramCompoundTag.getList("data");
/* 463 */     if (optional2.isPresent()) {
/* 464 */       Object2IntOpenHashMap object2IntOpenHashMap = new Object2IntOpenHashMap();
/* 465 */       object2IntOpenHashMap.defaultReturnValue(-1);
/* 466 */       for (byte b1 = 0; b1 < listTag.size(); b1++) {
/* 467 */         object2IntOpenHashMap.put(listTag.getString(b1).orElseThrow(), b1);
/*     */       }
/*     */       
/* 470 */       ListTag listTag1 = optional2.get();
/* 471 */       for (byte b2 = 0; b2 < listTag1.size(); b2++) {
/* 472 */         CompoundTag compoundTag = listTag1.getCompound(b2).orElseThrow();
/* 473 */         String str = compoundTag.getString("state").orElseThrow();
/* 474 */         int i = object2IntOpenHashMap.getInt(str);
/* 475 */         if (i == -1) {
/* 476 */           throw new IllegalStateException("Entry " + str + " missing from palette");
/*     */         }
/* 478 */         compoundTag.putInt("state", i);
/*     */       } 
/*     */       
/* 481 */       paramCompoundTag.put("blocks", listTag1);
/* 482 */       paramCompoundTag.remove("data");
/*     */     } 
/*     */     
/* 485 */     return paramCompoundTag;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static String packBlockState(CompoundTag paramCompoundTag) {
/* 490 */     StringBuilder stringBuilder = new StringBuilder(paramCompoundTag.getString("Name").orElseThrow());
/* 491 */     paramCompoundTag.getCompound("Properties").ifPresent(paramCompoundTag -> {
/*     */           String str = paramCompoundTag.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(()).collect(Collectors.joining(","));
/*     */ 
/*     */           
/*     */           paramStringBuilder.append('{').append(str).append('}');
/*     */         });
/*     */ 
/*     */     
/* 499 */     return stringBuilder.toString();
/*     */   }
/*     */   @VisibleForTesting
/*     */   static CompoundTag unpackBlockState(String paramString) {
/*     */     String str;
/* 504 */     CompoundTag compoundTag = new CompoundTag();
/* 505 */     int i = paramString.indexOf('{');
/*     */ 
/*     */     
/* 508 */     if (i >= 0) {
/* 509 */       str = paramString.substring(0, i);
/* 510 */       CompoundTag compoundTag1 = new CompoundTag();
/* 511 */       if (i + 2 <= paramString.length()) {
/* 512 */         String str1 = paramString.substring(i + 1, paramString.indexOf('}', i));
/* 513 */         COMMA_SPLITTER.split(str1).forEach(paramString2 -> {
/*     */               List<String> list = COLON_SPLITTER.splitToList(paramString2);
/*     */               
/*     */               if (list.size() == 2) {
/*     */                 paramCompoundTag.putString(list.get(0), list.get(1));
/*     */               } else {
/*     */                 LOGGER.error("Something went wrong parsing: '{}' -- incorrect gamedata!", paramString1);
/*     */               } 
/*     */             });
/* 522 */         compoundTag.put("Properties", compoundTag1);
/*     */       } 
/*     */     } else {
/* 525 */       str = paramString;
/*     */     } 
/* 527 */     compoundTag.putString("Name", str);
/* 528 */     return compoundTag;
/*     */   }
/*     */   
/*     */   public static CompoundTag addCurrentDataVersion(CompoundTag paramCompoundTag) {
/* 532 */     int i = SharedConstants.getCurrentVersion().dataVersion().version();
/* 533 */     return addDataVersion(paramCompoundTag, i);
/*     */   }
/*     */   
/*     */   public static CompoundTag addDataVersion(CompoundTag paramCompoundTag, int paramInt) {
/* 537 */     paramCompoundTag.putInt("DataVersion", paramInt);
/* 538 */     return paramCompoundTag;
/*     */   }
/*     */   
/*     */   public static Dynamic<Tag> addCurrentDataVersion(Dynamic<Tag> paramDynamic) {
/* 542 */     int i = SharedConstants.getCurrentVersion().dataVersion().version();
/* 543 */     return addDataVersion(paramDynamic, i);
/*     */   }
/*     */   
/*     */   public static Dynamic<Tag> addDataVersion(Dynamic<Tag> paramDynamic, int paramInt) {
/* 547 */     return paramDynamic.set("DataVersion", paramDynamic.createInt(paramInt));
/*     */   }
/*     */   
/*     */   public static void addCurrentDataVersion(ValueOutput paramValueOutput) {
/* 551 */     int i = SharedConstants.getCurrentVersion().dataVersion().version();
/* 552 */     addDataVersion(paramValueOutput, i);
/*     */   }
/*     */   
/*     */   public static void addDataVersion(ValueOutput paramValueOutput, int paramInt) {
/* 556 */     paramValueOutput.putInt("DataVersion", paramInt);
/*     */   }
/*     */   
/*     */   public static int getDataVersion(CompoundTag paramCompoundTag) {
/* 560 */     return getDataVersion(paramCompoundTag, -1);
/*     */   }
/*     */   
/*     */   public static int getDataVersion(CompoundTag paramCompoundTag, int paramInt) {
/* 564 */     return paramCompoundTag.getIntOr("DataVersion", paramInt);
/*     */   }
/*     */   
/*     */   public static int getDataVersion(Dynamic<?> paramDynamic, int paramInt) {
/* 568 */     return paramDynamic.get("DataVersion").asInt(paramInt);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\nbt\NbtUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */