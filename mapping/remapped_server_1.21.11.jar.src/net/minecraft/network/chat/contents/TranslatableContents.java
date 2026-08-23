/*     */ package net.minecraft.network.chat.contents;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.locale.Language;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentContents;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ 
/*     */ public class TranslatableContents implements ComponentContents {
/*  32 */   public static final Object[] NO_ARGS = new Object[0];
/*     */   
/*  34 */   private static final Codec<Object> PRIMITIVE_ARG_CODEC = ExtraCodecs.JAVA.validate(TranslatableContents::filterAllowedArguments); private static final Codec<Object> ARG_CODEC;
/*     */   
/*     */   private static DataResult<Object> filterAllowedArguments(Object paramObject) {
/*  37 */     if (!isAllowedPrimitiveArgument(paramObject)) {
/*  38 */       return DataResult.error(() -> "This value needs to be parsed as component");
/*     */     }
/*  40 */     return DataResult.success(paramObject);
/*     */   }
/*     */   public static final MapCodec<TranslatableContents> MAP_CODEC;
/*     */   public static boolean isAllowedPrimitiveArgument(Object paramObject) {
/*  44 */     return (paramObject instanceof Number || paramObject instanceof Boolean || paramObject instanceof String);
/*     */   }
/*     */   
/*     */   static {
/*  48 */     ARG_CODEC = Codec.either(PRIMITIVE_ARG_CODEC, ComponentSerialization.CODEC).xmap(paramEither -> paramEither.map((), ()), paramObject -> {
/*     */           Component component = (Component)paramObject;
/*     */ 
/*     */           
/*     */           return (paramObject instanceof Component) ? Either.right(component) : Either.left(paramObject);
/*     */         });
/*     */     
/*  55 */     MAP_CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)Codec.STRING.fieldOf("translate").forGetter(()), (App)Codec.STRING.lenientOptionalFieldOf("fallback").forGetter(()), (App)ARG_CODEC.listOf().optionalFieldOf("with").forGetter(())).apply((Applicative)paramInstance, TranslatableContents::create));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Optional<List<Object>> adjustArgs(Object[] paramArrayOfObject) {
/*  62 */     return (paramArrayOfObject.length == 0) ? Optional.<List<Object>>empty() : Optional.<List<Object>>of(Arrays.asList(paramArrayOfObject));
/*     */   }
/*     */   
/*     */   private static Object[] adjustArgs(Optional<List<Object>> paramOptional) {
/*  66 */     return paramOptional.<Object[]>map(paramList -> paramList.isEmpty() ? NO_ARGS : paramList.toArray()).orElse(NO_ARGS);
/*     */   }
/*     */   
/*     */   private static TranslatableContents create(String paramString, Optional<String> paramOptional, Optional<List<Object>> paramOptional1) {
/*  70 */     return new TranslatableContents(paramString, paramOptional.orElse(null), adjustArgs(paramOptional1));
/*     */   }
/*     */   
/*  73 */   private static final FormattedText TEXT_PERCENT = FormattedText.of("%");
/*  74 */   private static final FormattedText TEXT_NULL = FormattedText.of("null");
/*     */   
/*     */   private final String key;
/*     */   
/*     */   private final String fallback;
/*     */   private final Object[] args;
/*     */   private Language decomposedWith;
/*  81 */   private List<FormattedText> decomposedParts = (List<FormattedText>)ImmutableList.of();
/*     */   
/*  83 */   private static final Pattern FORMAT_PATTERN = Pattern.compile("%(?:(\\d+)\\$)?([A-Za-z%]|$)");
/*     */   
/*     */   public TranslatableContents(String paramString1, String paramString2, Object[] paramArrayOfObject) {
/*  86 */     this.key = paramString1;
/*  87 */     this.fallback = paramString2;
/*  88 */     this.args = paramArrayOfObject;
/*     */   }
/*     */ 
/*     */   
/*     */   public MapCodec<TranslatableContents> codec() {
/*  93 */     return MAP_CODEC;
/*     */   }
/*     */   
/*     */   private void decompose() {
/*  97 */     Language language = Language.getInstance();
/*  98 */     if (language == this.decomposedWith) {
/*     */       return;
/*     */     }
/* 101 */     this.decomposedWith = language;
/*     */ 
/*     */     
/* 104 */     String str = (this.fallback != null) ? language.getOrDefault(this.key, this.fallback) : language.getOrDefault(this.key);
/*     */     try {
/* 106 */       ImmutableList.Builder builder = ImmutableList.builder();
/* 107 */       Objects.requireNonNull(builder); decomposeTemplate(str, builder::add);
/* 108 */       this.decomposedParts = (List<FormattedText>)builder.build();
/* 109 */     } catch (TranslatableFormatException translatableFormatException) {
/* 110 */       this.decomposedParts = (List<FormattedText>)ImmutableList.of(FormattedText.of(str));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void decomposeTemplate(String paramString, Consumer<FormattedText> paramConsumer) {
/* 115 */     Matcher matcher = FORMAT_PATTERN.matcher(paramString);
/*     */     
/*     */     try {
/* 118 */       byte b = 0;
/* 119 */       int i = 0;
/*     */       
/* 121 */       while (matcher.find(i)) {
/* 122 */         int j = matcher.start();
/* 123 */         int k = matcher.end();
/*     */         
/* 125 */         if (j > i) {
/* 126 */           String str = paramString.substring(i, j);
/* 127 */           if (str.indexOf('%') != -1) {
/* 128 */             throw new IllegalArgumentException();
/*     */           }
/* 130 */           paramConsumer.accept(FormattedText.of(str));
/*     */         } 
/*     */         
/* 133 */         String str1 = matcher.group(2);
/* 134 */         String str2 = paramString.substring(j, k);
/*     */ 
/*     */         
/* 137 */         if ("%".equals(str1) && "%%".equals(str2)) {
/* 138 */           paramConsumer.accept(TEXT_PERCENT);
/* 139 */         } else if ("s".equals(str1)) {
/* 140 */           String str = matcher.group(1);
/* 141 */           boolean bool = (str != null) ? (Integer.parseInt(str) - 1) : b++;
/* 142 */           paramConsumer.accept(getArgument(bool));
/*     */         } else {
/* 144 */           throw new TranslatableFormatException(this, "Unsupported format: '" + str2 + "'");
/*     */         } 
/*     */         
/* 147 */         i = k;
/*     */       } 
/*     */       
/* 150 */       if (i < paramString.length()) {
/* 151 */         String str = paramString.substring(i);
/* 152 */         if (str.indexOf('%') != -1) {
/* 153 */           throw new IllegalArgumentException();
/*     */         }
/* 155 */         paramConsumer.accept(FormattedText.of(str));
/*     */       } 
/* 157 */     } catch (IllegalArgumentException illegalArgumentException) {
/* 158 */       throw new TranslatableFormatException(this, illegalArgumentException);
/*     */     } 
/*     */   }
/*     */   
/*     */   private FormattedText getArgument(int paramInt) {
/* 163 */     if (paramInt < 0 || paramInt >= this.args.length) {
/* 164 */       throw new TranslatableFormatException(this, paramInt);
/*     */     }
/*     */     
/* 167 */     Object object = this.args[paramInt];
/*     */     
/* 169 */     if (object instanceof Component) return (FormattedText)object;
/*     */ 
/*     */     
/* 172 */     return (object == null) ? TEXT_NULL : FormattedText.of(object.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> paramStyledContentConsumer, Style paramStyle) {
/* 178 */     decompose();
/*     */     
/* 180 */     for (FormattedText formattedText : this.decomposedParts) {
/* 181 */       Optional<T> optional = formattedText.visit(paramStyledContentConsumer, paramStyle);
/* 182 */       if (optional.isPresent()) {
/* 183 */         return optional;
/*     */       }
/*     */     } 
/*     */     
/* 187 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> Optional<T> visit(FormattedText.ContentConsumer<T> paramContentConsumer) {
/* 192 */     decompose();
/*     */     
/* 194 */     for (FormattedText formattedText : this.decomposedParts) {
/* 195 */       Optional<T> optional = formattedText.visit(paramContentConsumer);
/* 196 */       if (optional.isPresent()) {
/* 197 */         return optional;
/*     */       }
/*     */     } 
/*     */     
/* 201 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public MutableComponent resolve(CommandSourceStack paramCommandSourceStack, Entity paramEntity, int paramInt) throws CommandSyntaxException {
/* 206 */     Object[] arrayOfObject = new Object[this.args.length];
/*     */     
/* 208 */     for (byte b = 0; b < arrayOfObject.length; b++) {
/* 209 */       Object object = this.args[b];
/* 210 */       if (object instanceof Component) { Component component = (Component)object;
/* 211 */         arrayOfObject[b] = ComponentUtils.updateForEntity(paramCommandSourceStack, component, paramEntity, paramInt); }
/*     */       else
/* 213 */       { arrayOfObject[b] = object; }
/*     */     
/*     */     } 
/* 216 */     return MutableComponent.create(new TranslatableContents(this.key, this.fallback, arrayOfObject));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: if_acmpne -> 7
/*     */     //   5: iconst_1
/*     */     //   6: ireturn
/*     */     //   7: aload_1
/*     */     //   8: instanceof net/minecraft/network/chat/contents/TranslatableContents
/*     */     //   11: ifeq -> 65
/*     */     //   14: aload_1
/*     */     //   15: checkcast net/minecraft/network/chat/contents/TranslatableContents
/*     */     //   18: astore_2
/*     */     //   19: aload_0
/*     */     //   20: getfield key : Ljava/lang/String;
/*     */     //   23: aload_2
/*     */     //   24: getfield key : Ljava/lang/String;
/*     */     //   27: invokestatic equals : (Ljava/lang/Object;Ljava/lang/Object;)Z
/*     */     //   30: ifeq -> 65
/*     */     //   33: aload_0
/*     */     //   34: getfield fallback : Ljava/lang/String;
/*     */     //   37: aload_2
/*     */     //   38: getfield fallback : Ljava/lang/String;
/*     */     //   41: invokestatic equals : (Ljava/lang/Object;Ljava/lang/Object;)Z
/*     */     //   44: ifeq -> 65
/*     */     //   47: aload_0
/*     */     //   48: getfield args : [Ljava/lang/Object;
/*     */     //   51: aload_2
/*     */     //   52: getfield args : [Ljava/lang/Object;
/*     */     //   55: invokestatic equals : ([Ljava/lang/Object;[Ljava/lang/Object;)Z
/*     */     //   58: ifeq -> 65
/*     */     //   61: iconst_1
/*     */     //   62: goto -> 66
/*     */     //   65: iconst_0
/*     */     //   66: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #221	-> 0
/*     */     //   #222	-> 5
/*     */     //   #228	-> 7
/*     */     //   #225	-> 14
/*     */     //   #226	-> 27
/*     */     //   #227	-> 41
/*     */     //   #228	-> 55
/*     */     //   #225	-> 66
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 233 */     int i = Objects.hashCode(this.key);
/* 234 */     i = 31 * i + Objects.hashCode(this.fallback);
/* 235 */     i = 31 * i + Arrays.hashCode(this.args);
/* 236 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 241 */     return "translation{key='" + this.key + "'" + (
/*     */       
/* 243 */       (this.fallback != null) ? (", fallback='" + this.fallback + "'") : "") + ", args=" + 
/* 244 */       Arrays.toString(this.args) + "}";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getKey() {
/* 249 */     return this.key;
/*     */   }
/*     */   
/*     */   public String getFallback() {
/* 253 */     return this.fallback;
/*     */   }
/*     */   
/*     */   public Object[] getArgs() {
/* 257 */     return this.args;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\chat\contents\TranslatableContents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */