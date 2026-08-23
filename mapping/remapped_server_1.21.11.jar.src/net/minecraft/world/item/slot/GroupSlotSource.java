/*    */ package net.minecraft.world.item.slot;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public class GroupSlotSource extends CompositeSlotSource {
/*  9 */   public static final MapCodec<GroupSlotSource> MAP_CODEC = createCodec(GroupSlotSource::new);
/* 10 */   public static final Codec<GroupSlotSource> INLINE_CODEC = createInlineCodec(GroupSlotSource::new);
/*    */   
/*    */   private GroupSlotSource(List<SlotSource> paramList) {
/* 13 */     super(paramList);
/*    */   }
/*    */ 
/*    */   
/*    */   public MapCodec<GroupSlotSource> codec() {
/* 18 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\slot\GroupSlotSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */