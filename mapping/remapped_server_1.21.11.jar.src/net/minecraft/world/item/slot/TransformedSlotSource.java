/*    */ package net.minecraft.world.item.slot;
/*    */ 
/*    */ import com.mojang.datafixers.Products;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ 
/*    */ public abstract class TransformedSlotSource
/*    */   implements SlotSource {
/*    */   protected TransformedSlotSource(SlotSource paramSlotSource) {
/* 14 */     this.slotSource = paramSlotSource;
/*    */   }
/*    */ 
/*    */   
/*    */   protected final SlotSource slotSource;
/*    */   
/*    */   protected static <T extends TransformedSlotSource> Products.P1<RecordCodecBuilder.Mu<T>, SlotSource> commonFields(RecordCodecBuilder.Instance<T> paramInstance) {
/* 21 */     return paramInstance.group((App)SlotSources.CODEC
/* 22 */         .fieldOf("slot_source").forGetter(paramTransformedSlotSource -> paramTransformedSlotSource.slotSource));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final SlotCollection provide(LootContext paramLootContext) {
/* 30 */     return transform(this.slotSource.provide(paramLootContext));
/*    */   }
/*    */ 
/*    */   
/*    */   public void validate(ValidationContext paramValidationContext) {
/* 35 */     super.validate(paramValidationContext);
/* 36 */     this.slotSource.validate(paramValidationContext.forChild((ProblemReporter.PathElement)new ProblemReporter.FieldPathElement("slot_source")));
/*    */   }
/*    */   
/*    */   public abstract MapCodec<? extends TransformedSlotSource> codec();
/*    */   
/*    */   protected abstract SlotCollection transform(SlotCollection paramSlotCollection);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\slot\TransformedSlotSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */