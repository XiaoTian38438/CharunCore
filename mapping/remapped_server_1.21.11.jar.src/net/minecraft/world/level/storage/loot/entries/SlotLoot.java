/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.slot.SlotSource;
/*    */ import net.minecraft.world.item.slot.SlotSources;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SlotLoot extends LootPoolSingletonContainer {
/*    */   static {
/* 18 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)SlotSources.CODEC.fieldOf("slot_source").forGetter(())).and(singletonFields(paramInstance)).apply((Applicative)paramInstance, SlotLoot::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<SlotLoot> CODEC;
/*    */   private final SlotSource slotSource;
/*    */   
/*    */   private SlotLoot(SlotSource paramSlotSource, int paramInt1, int paramInt2, List<LootItemCondition> paramList, List<LootItemFunction> paramList1) {
/* 25 */     super(paramInt1, paramInt2, paramList, paramList1);
/* 26 */     this.slotSource = paramSlotSource;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootPoolEntryType getType() {
/* 31 */     return LootPoolEntries.SLOTS;
/*    */   }
/*    */ 
/*    */   
/*    */   public void createItemStack(Consumer<ItemStack> paramConsumer, LootContext paramLootContext) {
/* 36 */     this.slotSource.provide(paramLootContext).itemCopies()
/* 37 */       .filter(paramItemStack -> !paramItemStack.isEmpty())
/* 38 */       .forEach(paramConsumer);
/*    */   }
/*    */ 
/*    */   
/*    */   public void validate(ValidationContext paramValidationContext) {
/* 43 */     super.validate(paramValidationContext);
/* 44 */     this.slotSource.validate(paramValidationContext.forChild((ProblemReporter.PathElement)new ProblemReporter.FieldPathElement("slot_source")));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\entries\SlotLoot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */