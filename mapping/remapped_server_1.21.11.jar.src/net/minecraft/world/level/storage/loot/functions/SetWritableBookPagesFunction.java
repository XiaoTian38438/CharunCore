/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.server.network.Filterable;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.WritableBookContent;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetWritableBookPagesFunction extends LootItemConditionalFunction {
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> commonFields(paramInstance).and(paramInstance.group((App)WritableBookContent.PAGES_CODEC.fieldOf("pages").forGetter(()), (App)ListOperation.codec(100).forGetter(()))).apply((Applicative)paramInstance, SetWritableBookPagesFunction::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<SetWritableBookPagesFunction> CODEC;
/*    */   private final List<Filterable<String>> pages;
/*    */   private final ListOperation pageOperation;
/*    */   
/*    */   protected SetWritableBookPagesFunction(List<LootItemCondition> paramList, List<Filterable<String>> paramList1, ListOperation paramListOperation) {
/* 25 */     super(paramList);
/* 26 */     this.pages = paramList1;
/* 27 */     this.pageOperation = paramListOperation;
/*    */   }
/*    */ 
/*    */   
/*    */   protected ItemStack run(ItemStack paramItemStack, LootContext paramLootContext) {
/* 32 */     paramItemStack.update(DataComponents.WRITABLE_BOOK_CONTENT, WritableBookContent.EMPTY, this::apply);
/* 33 */     return paramItemStack;
/*    */   }
/*    */   
/*    */   public WritableBookContent apply(WritableBookContent paramWritableBookContent) {
/* 37 */     List<Filterable<String>> list = this.pageOperation.apply(paramWritableBookContent.pages(), this.pages, 100);
/* 38 */     return paramWritableBookContent.withReplacedPages(list);
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<SetWritableBookPagesFunction> getType() {
/* 43 */     return LootItemFunctions.SET_WRITABLE_BOOK_PAGES;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\SetWritableBookPagesFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */