/*     */ package net.minecraft.network.chat.contents;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.arguments.NbtPathArgument;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.StringTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentContents;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.contents.data.DataSource;
/*     */ import net.minecraft.network.chat.contents.data.DataSources;
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class NbtContents implements ComponentContents {
/*  32 */   private static final Logger LOGGER = LogUtils.getLogger(); public static final MapCodec<NbtContents> MAP_CODEC;
/*     */   static {
/*  34 */     MAP_CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)Codec.STRING.fieldOf("nbt").forGetter(NbtContents::getNbtPath), (App)Codec.BOOL.lenientOptionalFieldOf("interpret", Boolean.valueOf(false)).forGetter(NbtContents::isInterpreting), (App)ComponentSerialization.CODEC.lenientOptionalFieldOf("separator").forGetter(NbtContents::getSeparator), (App)DataSources.CODEC.forGetter(NbtContents::getDataSource)).apply((Applicative)paramInstance, NbtContents::new));
/*     */   }
/*     */ 
/*     */   
/*     */   private final boolean interpreting;
/*     */   
/*     */   private final Optional<Component> separator;
/*     */   
/*     */   private final String nbtPathPattern;
/*     */   
/*     */   private final DataSource dataSource;
/*     */   
/*     */   protected final NbtPathArgument.NbtPath compiledNbtPath;
/*     */   
/*     */   public NbtContents(String paramString, boolean paramBoolean, Optional<Component> paramOptional, DataSource paramDataSource) {
/*  49 */     this(paramString, compileNbtPath(paramString), paramBoolean, paramOptional, paramDataSource);
/*     */   }
/*     */   
/*     */   private NbtContents(String paramString, NbtPathArgument.NbtPath paramNbtPath, boolean paramBoolean, Optional<Component> paramOptional, DataSource paramDataSource) {
/*  53 */     this.nbtPathPattern = paramString;
/*  54 */     this.compiledNbtPath = paramNbtPath;
/*  55 */     this.interpreting = paramBoolean;
/*  56 */     this.separator = paramOptional;
/*  57 */     this.dataSource = paramDataSource;
/*     */   }
/*     */   
/*     */   private static NbtPathArgument.NbtPath compileNbtPath(String paramString) {
/*     */     try {
/*  62 */       return (new NbtPathArgument()).parse(new StringReader(paramString));
/*  63 */     } catch (CommandSyntaxException commandSyntaxException) {
/*  64 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getNbtPath() {
/*  69 */     return this.nbtPathPattern;
/*     */   }
/*     */   
/*     */   public boolean isInterpreting() {
/*  73 */     return this.interpreting;
/*     */   }
/*     */   
/*     */   public Optional<Component> getSeparator() {
/*  77 */     return this.separator;
/*     */   }
/*     */   
/*     */   public DataSource getDataSource() {
/*  81 */     return this.dataSource;
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
/*     */     //   8: instanceof net/minecraft/network/chat/contents/NbtContents
/*     */     //   11: ifeq -> 78
/*     */     //   14: aload_1
/*     */     //   15: checkcast net/minecraft/network/chat/contents/NbtContents
/*     */     //   18: astore_2
/*     */     //   19: aload_0
/*     */     //   20: getfield dataSource : Lnet/minecraft/network/chat/contents/data/DataSource;
/*     */     //   23: aload_2
/*     */     //   24: getfield dataSource : Lnet/minecraft/network/chat/contents/data/DataSource;
/*     */     //   27: invokeinterface equals : (Ljava/lang/Object;)Z
/*     */     //   32: ifeq -> 78
/*     */     //   35: aload_0
/*     */     //   36: getfield separator : Ljava/util/Optional;
/*     */     //   39: aload_2
/*     */     //   40: getfield separator : Ljava/util/Optional;
/*     */     //   43: invokevirtual equals : (Ljava/lang/Object;)Z
/*     */     //   46: ifeq -> 78
/*     */     //   49: aload_0
/*     */     //   50: getfield interpreting : Z
/*     */     //   53: aload_2
/*     */     //   54: getfield interpreting : Z
/*     */     //   57: if_icmpne -> 78
/*     */     //   60: aload_0
/*     */     //   61: getfield nbtPathPattern : Ljava/lang/String;
/*     */     //   64: aload_2
/*     */     //   65: getfield nbtPathPattern : Ljava/lang/String;
/*     */     //   68: invokevirtual equals : (Ljava/lang/Object;)Z
/*     */     //   71: ifeq -> 78
/*     */     //   74: iconst_1
/*     */     //   75: goto -> 79
/*     */     //   78: iconst_0
/*     */     //   79: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #86	-> 0
/*     */     //   #87	-> 5
/*     */     //   #93	-> 7
/*     */     //   #89	-> 14
/*     */     //   #90	-> 27
/*     */     //   #91	-> 43
/*     */     //   #93	-> 68
/*     */     //   #89	-> 79
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  98 */     int i = this.interpreting ? 1 : 0;
/*  99 */     i = 31 * i + this.separator.hashCode();
/* 100 */     i = 31 * i + this.nbtPathPattern.hashCode();
/* 101 */     i = 31 * i + this.dataSource.hashCode();
/* 102 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 107 */     return "nbt{" + String.valueOf(this.dataSource) + ", interpreting=" + this.interpreting + ", separator=" + String.valueOf(this.separator) + "}";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MutableComponent resolve(CommandSourceStack paramCommandSourceStack, Entity paramEntity, int paramInt) throws CommandSyntaxException {
/* 115 */     if (paramCommandSourceStack == null || this.compiledNbtPath == null) {
/* 116 */       return Component.empty();
/*     */     }
/*     */     
/* 119 */     Stream stream1 = this.dataSource.getData(paramCommandSourceStack).flatMap(paramCompoundTag -> {
/*     */           try {
/*     */             return this.compiledNbtPath.get((Tag)paramCompoundTag).stream();
/* 122 */           } catch (CommandSyntaxException commandSyntaxException) {
/*     */             return Stream.empty();
/*     */           } 
/*     */         });
/*     */     
/* 127 */     if (this.interpreting) {
/* 128 */       RegistryOps registryOps = paramCommandSourceStack.registryAccess().createSerializationContext((DynamicOps)NbtOps.INSTANCE);
/* 129 */       Component component = (Component)DataFixUtils.orElse(ComponentUtils.updateForEntity(paramCommandSourceStack, this.separator, paramEntity, paramInt), ComponentUtils.DEFAULT_NO_STYLE_SEPARATOR);
/* 130 */       return stream1.flatMap(paramTag -> {
/*     */             try {
/*     */               Component component = (Component)ComponentSerialization.CODEC.parse((DynamicOps)paramRegistryOps, paramTag).getOrThrow();
/*     */               return Stream.of(ComponentUtils.updateForEntity(paramCommandSourceStack, component, paramEntity, paramInt));
/* 134 */             } catch (Exception exception) {
/*     */               LOGGER.warn("Failed to parse component: {}", paramTag, exception);
/*     */               
/*     */               return Stream.of(new MutableComponent[0]);
/*     */             } 
/* 139 */           }).reduce((paramMutableComponent1, paramMutableComponent2) -> paramMutableComponent1.append(paramComponent).append((Component)paramMutableComponent2))
/* 140 */         .orElseGet(Component::empty);
/*     */     } 
/* 142 */     Stream stream2 = stream1.map(NbtContents::asString);
/* 143 */     return ComponentUtils.updateForEntity(paramCommandSourceStack, this.separator, paramEntity, paramInt)
/* 144 */       .map(paramMutableComponent -> (MutableComponent)paramStream.map(Component::literal).reduce(()).orElseGet(Component::empty))
/*     */       
/* 146 */       .orElseGet(() -> Component.literal(paramStream.collect(Collectors.joining(", "))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String asString(Tag paramTag) {
/* 154 */     if (paramTag instanceof StringTag) { StringTag stringTag = (StringTag)paramTag; try { String str; return str = stringTag.value(); } catch (Throwable throwable) { throw new MatchException(throwable.toString(), throwable); }
/*     */        }
/*     */     
/* 157 */     return paramTag.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public MapCodec<NbtContents> codec() {
/* 162 */     return MAP_CODEC;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\chat\contents\NbtContents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */