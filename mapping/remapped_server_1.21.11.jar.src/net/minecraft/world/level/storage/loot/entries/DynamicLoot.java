/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class DynamicLoot extends LootPoolSingletonContainer {
/*    */   static {
/* 15 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)Identifier.CODEC.fieldOf("name").forGetter(())).and(singletonFields(paramInstance)).apply((Applicative)paramInstance, DynamicLoot::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<DynamicLoot> CODEC;
/*    */   private final Identifier name;
/*    */   
/*    */   private DynamicLoot(Identifier paramIdentifier, int paramInt1, int paramInt2, List<LootItemCondition> paramList, List<LootItemFunction> paramList1) {
/* 22 */     super(paramInt1, paramInt2, paramList, paramList1);
/* 23 */     this.name = paramIdentifier;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootPoolEntryType getType() {
/* 28 */     return LootPoolEntries.DYNAMIC;
/*    */   }
/*    */ 
/*    */   
/*    */   public void createItemStack(Consumer<ItemStack> paramConsumer, LootContext paramLootContext) {
/* 33 */     paramLootContext.addDynamicDrops(this.name, paramConsumer);
/*    */   }
/*    */   
/*    */   public static LootPoolSingletonContainer.Builder<?> dynamicEntry(Identifier paramIdentifier) {
/* 37 */     return simpleBuilder((paramInt1, paramInt2, paramList1, paramList2) -> new DynamicLoot(paramIdentifier, paramInt1, paramInt2, paramList1, paramList2));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\entries\DynamicLoot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */