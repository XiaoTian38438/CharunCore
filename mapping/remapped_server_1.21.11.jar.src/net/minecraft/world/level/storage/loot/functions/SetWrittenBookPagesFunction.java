/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.network.Filterable;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.WrittenBookContent;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetWrittenBookPagesFunction extends LootItemConditionalFunction {
/*    */   static {
/* 18 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> commonFields(paramInstance).and(paramInstance.group((App)WrittenBookContent.PAGES_CODEC.fieldOf("pages").forGetter(()), (App)ListOperation.UNLIMITED_CODEC.forGetter(()))).apply((Applicative)paramInstance, SetWrittenBookPagesFunction::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<SetWrittenBookPagesFunction> CODEC;
/*    */   private final List<Filterable<Component>> pages;
/*    */   private final ListOperation pageOperation;
/*    */   
/*    */   protected SetWrittenBookPagesFunction(List<LootItemCondition> paramList, List<Filterable<Component>> paramList1, ListOperation paramListOperation) {
/* 27 */     super(paramList);
/* 28 */     this.pages = paramList1;
/* 29 */     this.pageOperation = paramListOperation;
/*    */   }
/*    */ 
/*    */   
/*    */   protected ItemStack run(ItemStack paramItemStack, LootContext paramLootContext) {
/* 34 */     paramItemStack.update(DataComponents.WRITTEN_BOOK_CONTENT, WrittenBookContent.EMPTY, this::apply);
/* 35 */     return paramItemStack;
/*    */   }
/*    */   
/*    */   @VisibleForTesting
/*    */   public WrittenBookContent apply(WrittenBookContent paramWrittenBookContent) {
/* 40 */     List<Filterable<Component>> list = this.pageOperation.apply(paramWrittenBookContent.pages(), this.pages);
/* 41 */     return paramWrittenBookContent.withReplacedPages(list);
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<SetWrittenBookPagesFunction> getType() {
/* 46 */     return LootItemFunctions.SET_WRITTEN_BOOK_PAGES;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\SetWrittenBookPagesFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */