/*    */ package net.minecraft.world.item.crafting.display;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.context.ContextMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Empty
/*    */   implements SlotDisplay
/*    */ {
/* 69 */   public static final Empty INSTANCE = new Empty();
/*    */   
/* 71 */   public static final MapCodec<Empty> MAP_CODEC = MapCodec.unit(INSTANCE);
/*    */   
/* 73 */   public static final StreamCodec<RegistryFriendlyByteBuf, Empty> STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*    */   
/* 75 */   public static final SlotDisplay.Type<Empty> TYPE = new SlotDisplay.Type<>(MAP_CODEC, STREAM_CODEC);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SlotDisplay.Type<Empty> type() {
/* 82 */     return TYPE;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 87 */     return "<empty>";
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> Stream<T> resolve(ContextMap paramContextMap, DisplayContentsFactory<T> paramDisplayContentsFactory) {
/* 92 */     return Stream.empty();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\crafting\display\SlotDisplay$Empty.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */