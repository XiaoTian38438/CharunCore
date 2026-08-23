/*    */ package net.minecraft.world.item.slot;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public class LimitSlotSource extends TransformedSlotSource {
/*    */   static {
/*  8 */     MAP_CODEC = RecordCodecBuilder.mapCodec(paramInstance -> commonFields(paramInstance).and((App)ExtraCodecs.POSITIVE_INT.fieldOf("limit").forGetter(())).apply((Applicative)paramInstance, LimitSlotSource::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<LimitSlotSource> MAP_CODEC;
/*    */   private final int limit;
/*    */   
/*    */   private LimitSlotSource(SlotSource paramSlotSource, int paramInt) {
/* 15 */     super(paramSlotSource);
/* 16 */     this.limit = paramInt;
/*    */   }
/*    */ 
/*    */   
/*    */   public MapCodec<LimitSlotSource> codec() {
/* 21 */     return MAP_CODEC;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SlotCollection transform(SlotCollection paramSlotCollection) {
/* 26 */     return paramSlotCollection.limit(this.limit);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\slot\LimitSlotSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */