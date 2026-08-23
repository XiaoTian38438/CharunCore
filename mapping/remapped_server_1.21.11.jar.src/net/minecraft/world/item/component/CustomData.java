/*    */ package net.minecraft.world.item.component;
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.NbtUtils;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.nbt.TagParser;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public final class CustomData {
/* 16 */   public static final CustomData EMPTY = new CustomData(new CompoundTag());
/*    */   public static final Codec<CustomData> CODEC;
/* 18 */   public static final Codec<CompoundTag> COMPOUND_TAG_CODEC = Codec.withAlternative(CompoundTag.CODEC, TagParser.FLATTENED_CODEC); static {
/* 19 */     CODEC = COMPOUND_TAG_CODEC.xmap(CustomData::new, paramCustomData -> paramCustomData.tag);
/*    */ 
/*    */ 
/*    */     
/* 23 */     STREAM_CODEC = ByteBufCodecs.COMPOUND_TAG.map(CustomData::new, paramCustomData -> paramCustomData.tag);
/*    */   }
/*    */   @Deprecated
/*    */   public static final StreamCodec<ByteBuf, CustomData> STREAM_CODEC; private final CompoundTag tag;
/*    */   
/*    */   private CustomData(CompoundTag paramCompoundTag) {
/* 29 */     this.tag = paramCompoundTag;
/*    */   }
/*    */   
/*    */   public static CustomData of(CompoundTag paramCompoundTag) {
/* 33 */     return new CustomData(paramCompoundTag.copy());
/*    */   }
/*    */   
/*    */   public boolean matchedBy(CompoundTag paramCompoundTag) {
/* 37 */     return NbtUtils.compareNbt((Tag)paramCompoundTag, (Tag)this.tag, true);
/*    */   }
/*    */   
/*    */   public static void update(DataComponentType<CustomData> paramDataComponentType, ItemStack paramItemStack, Consumer<CompoundTag> paramConsumer) {
/* 41 */     CustomData customData = ((CustomData)paramItemStack.getOrDefault(paramDataComponentType, EMPTY)).update(paramConsumer);
/* 42 */     if (customData.tag.isEmpty()) {
/* 43 */       paramItemStack.remove(paramDataComponentType);
/*    */     } else {
/* 45 */       paramItemStack.set(paramDataComponentType, customData);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static void set(DataComponentType<CustomData> paramDataComponentType, ItemStack paramItemStack, CompoundTag paramCompoundTag) {
/* 50 */     if (!paramCompoundTag.isEmpty()) {
/* 51 */       paramItemStack.set(paramDataComponentType, of(paramCompoundTag));
/*    */     } else {
/* 53 */       paramItemStack.remove(paramDataComponentType);
/*    */     } 
/*    */   }
/*    */   
/*    */   public CustomData update(Consumer<CompoundTag> paramConsumer) {
/* 58 */     CompoundTag compoundTag = this.tag.copy();
/* 59 */     paramConsumer.accept(compoundTag);
/* 60 */     return new CustomData(compoundTag);
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 64 */     return this.tag.isEmpty();
/*    */   }
/*    */   
/*    */   public CompoundTag copyTag() {
/* 68 */     return this.tag.copy();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 73 */     if (paramObject == this) {
/* 74 */       return true;
/*    */     }
/* 76 */     if (paramObject instanceof CustomData) { CustomData customData = (CustomData)paramObject;
/* 77 */       return this.tag.equals(customData.tag); }
/*    */     
/* 79 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 84 */     return this.tag.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 89 */     return this.tag.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\component\CustomData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */