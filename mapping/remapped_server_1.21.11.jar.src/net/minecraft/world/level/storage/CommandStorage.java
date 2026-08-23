/*     */ package net.minecraft.world.level.storage;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.level.saveddata.SavedDataType;
/*     */ 
/*     */ public class CommandStorage {
/*     */   private static final String ID_PREFIX = "command_storage_";
/*     */   
/*     */   private static class Container extends SavedData {
/*     */     static {
/*  19 */       CODEC = RecordCodecBuilder.create(param1Instance -> param1Instance.group((App)Codec.unboundedMap(ExtraCodecs.RESOURCE_PATH_CODEC, CompoundTag.CODEC).fieldOf("contents").forGetter(())).apply((Applicative)param1Instance, Container::new));
/*     */     }
/*     */     
/*     */     public static final Codec<Container> CODEC;
/*     */     private final Map<String, CompoundTag> storage;
/*     */     
/*     */     private Container(Map<String, CompoundTag> param1Map) {
/*  26 */       this.storage = new HashMap<>(param1Map);
/*     */     }
/*     */     
/*     */     private Container() {
/*  30 */       this(new HashMap<>());
/*     */     }
/*     */     
/*     */     public static SavedDataType<Container> type(String param1String) {
/*  34 */       return new SavedDataType(CommandStorage.createId(param1String), Container::new, CODEC, DataFixTypes.SAVED_DATA_COMMAND_STORAGE);
/*     */     }
/*     */     
/*     */     public CompoundTag get(String param1String) {
/*  38 */       CompoundTag compoundTag = this.storage.get(param1String);
/*  39 */       return (compoundTag != null) ? compoundTag : new CompoundTag();
/*     */     }
/*     */     
/*     */     public void put(String param1String, CompoundTag param1CompoundTag) {
/*  43 */       if (param1CompoundTag.isEmpty()) {
/*  44 */         this.storage.remove(param1String);
/*     */       } else {
/*  46 */         this.storage.put(param1String, param1CompoundTag);
/*     */       } 
/*  48 */       setDirty();
/*     */     }
/*     */     
/*     */     public Stream<Identifier> getKeys(String param1String) {
/*  52 */       return this.storage.keySet().stream().map(param1String2 -> Identifier.fromNamespaceAndPath(param1String1, param1String2));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*  57 */   private final Map<String, Container> namespaces = new HashMap<>();
/*     */   private final DimensionDataStorage storage;
/*     */   
/*     */   public CommandStorage(DimensionDataStorage paramDimensionDataStorage) {
/*  61 */     this.storage = paramDimensionDataStorage;
/*     */   }
/*     */   
/*     */   public CompoundTag get(Identifier paramIdentifier) {
/*  65 */     Container container = getContainer(paramIdentifier.getNamespace());
/*  66 */     if (container != null) {
/*  67 */       return container.get(paramIdentifier.getPath());
/*     */     }
/*  69 */     return new CompoundTag();
/*     */   }
/*     */   
/*     */   private Container getContainer(String paramString) {
/*  73 */     Container container1 = this.namespaces.get(paramString);
/*  74 */     if (container1 != null) {
/*  75 */       return container1;
/*     */     }
/*  77 */     Container container2 = this.storage.<Container>get(Container.type(paramString));
/*  78 */     if (container2 != null) {
/*  79 */       this.namespaces.put(paramString, container2);
/*     */     }
/*  81 */     return container2;
/*     */   }
/*     */   
/*     */   private Container getOrCreateContainer(String paramString) {
/*  85 */     Container container1 = this.namespaces.get(paramString);
/*  86 */     if (container1 != null) {
/*  87 */       return container1;
/*     */     }
/*  89 */     Container container2 = this.storage.<Container>computeIfAbsent(Container.type(paramString));
/*  90 */     this.namespaces.put(paramString, container2);
/*  91 */     return container2;
/*     */   }
/*     */   
/*     */   public void set(Identifier paramIdentifier, CompoundTag paramCompoundTag) {
/*  95 */     getOrCreateContainer(paramIdentifier.getNamespace()).put(paramIdentifier.getPath(), paramCompoundTag);
/*     */   }
/*     */   
/*     */   public Stream<Identifier> keys() {
/*  99 */     return this.namespaces.entrySet().stream().flatMap(paramEntry -> ((Container)paramEntry.getValue()).getKeys((String)paramEntry.getKey()));
/*     */   }
/*     */   
/*     */   static String createId(String paramString) {
/* 103 */     return "command_storage_" + paramString;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\CommandStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */