/*    */ package net.minecraft.world.level.storage;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.datafix.DataFixTypes;
/*    */ import net.minecraft.world.level.saveddata.SavedDataType;
/*    */ 
/*    */ class Container extends SavedData {
/*    */   static {
/* 19 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)Codec.unboundedMap(ExtraCodecs.RESOURCE_PATH_CODEC, CompoundTag.CODEC).fieldOf("contents").forGetter(())).apply((Applicative)paramInstance, Container::new));
/*    */   }
/*    */   
/*    */   public static final Codec<Container> CODEC;
/*    */   private final Map<String, CompoundTag> storage;
/*    */   
/*    */   private Container(Map<String, CompoundTag> paramMap) {
/* 26 */     this.storage = new HashMap<>(paramMap);
/*    */   }
/*    */   
/*    */   private Container() {
/* 30 */     this(new HashMap<>());
/*    */   }
/*    */   
/*    */   public static SavedDataType<Container> type(String paramString) {
/* 34 */     return new SavedDataType(CommandStorage.createId(paramString), Container::new, CODEC, DataFixTypes.SAVED_DATA_COMMAND_STORAGE);
/*    */   }
/*    */   
/*    */   public CompoundTag get(String paramString) {
/* 38 */     CompoundTag compoundTag = this.storage.get(paramString);
/* 39 */     return (compoundTag != null) ? compoundTag : new CompoundTag();
/*    */   }
/*    */   
/*    */   public void put(String paramString, CompoundTag paramCompoundTag) {
/* 43 */     if (paramCompoundTag.isEmpty()) {
/* 44 */       this.storage.remove(paramString);
/*    */     } else {
/* 46 */       this.storage.put(paramString, paramCompoundTag);
/*    */     } 
/* 48 */     setDirty();
/*    */   }
/*    */   
/*    */   public Stream<Identifier> getKeys(String paramString) {
/* 52 */     return this.storage.keySet().stream().map(paramString2 -> Identifier.fromNamespaceAndPath(paramString1, paramString2));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\CommandStorage$Container.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */