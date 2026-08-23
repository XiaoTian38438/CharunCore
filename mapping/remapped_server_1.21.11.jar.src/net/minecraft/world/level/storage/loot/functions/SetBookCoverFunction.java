/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.server.network.Filterable;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.WrittenBookContent;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetBookCoverFunction extends LootItemConditionalFunction {
/*    */   static {
/* 18 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> commonFields(paramInstance).and(paramInstance.group((App)Filterable.codec(Codec.string(0, 32)).optionalFieldOf("title").forGetter(()), (App)Codec.STRING.optionalFieldOf("author").forGetter(()), (App)ExtraCodecs.intRange(0, 3).optionalFieldOf("generation").forGetter(()))).apply((Applicative)paramInstance, SetBookCoverFunction::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<SetBookCoverFunction> CODEC;
/*    */   
/*    */   private final Optional<String> author;
/*    */   private final Optional<Filterable<String>> title;
/*    */   private final Optional<Integer> generation;
/*    */   
/*    */   public SetBookCoverFunction(List<LootItemCondition> paramList, Optional<Filterable<String>> paramOptional, Optional<String> paramOptional1, Optional<Integer> paramOptional2) {
/* 29 */     super(paramList);
/* 30 */     this.author = paramOptional1;
/* 31 */     this.title = paramOptional;
/* 32 */     this.generation = paramOptional2;
/*    */   }
/*    */ 
/*    */   
/*    */   protected ItemStack run(ItemStack paramItemStack, LootContext paramLootContext) {
/* 37 */     paramItemStack.update(DataComponents.WRITTEN_BOOK_CONTENT, WrittenBookContent.EMPTY, this::apply);
/* 38 */     return paramItemStack;
/*    */   }
/*    */ 
/*    */   
/*    */   private WrittenBookContent apply(WrittenBookContent paramWrittenBookContent) {
/* 43 */     Objects.requireNonNull(paramWrittenBookContent);
/* 44 */     Objects.requireNonNull(paramWrittenBookContent);
/* 45 */     Objects.requireNonNull(paramWrittenBookContent); return new WrittenBookContent(this.title.orElseGet(paramWrittenBookContent::title), this.author.orElseGet(paramWrittenBookContent::author), ((Integer)this.generation.orElseGet(paramWrittenBookContent::generation)).intValue(), paramWrittenBookContent
/* 46 */         .pages(), paramWrittenBookContent
/* 47 */         .resolved());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<SetBookCoverFunction> getType() {
/* 53 */     return LootItemFunctions.SET_BOOK_COVER;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\SetBookCoverFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */