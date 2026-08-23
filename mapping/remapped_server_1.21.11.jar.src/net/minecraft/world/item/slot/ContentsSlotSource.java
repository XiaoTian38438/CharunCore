/*    */ package net.minecraft.world.item.slot;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.level.storage.loot.ContainerComponentManipulator;
/*    */ import net.minecraft.world.level.storage.loot.ContainerComponentManipulators;
/*    */ 
/*    */ public class ContentsSlotSource extends TransformedSlotSource {
/*    */   static {
/*  9 */     MAP_CODEC = RecordCodecBuilder.mapCodec(paramInstance -> commonFields(paramInstance).and((App)ContainerComponentManipulators.CODEC.fieldOf("component").forGetter(())).apply((Applicative)paramInstance, ContentsSlotSource::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<ContentsSlotSource> MAP_CODEC;
/*    */   private final ContainerComponentManipulator<?> component;
/*    */   
/*    */   private ContentsSlotSource(SlotSource paramSlotSource, ContainerComponentManipulator<?> paramContainerComponentManipulator) {
/* 16 */     super(paramSlotSource);
/* 17 */     this.component = paramContainerComponentManipulator;
/*    */   }
/*    */ 
/*    */   
/*    */   public MapCodec<ContentsSlotSource> codec() {
/* 22 */     return MAP_CODEC;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SlotCollection transform(SlotCollection paramSlotCollection) {
/* 27 */     Objects.requireNonNull(this.component); return paramSlotCollection.flatMap(this.component::getSlots);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\slot\ContentsSlotSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */