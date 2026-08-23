/*    */ package net.minecraft.data.advancements;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.advancements.Advancement;
/*    */ import net.minecraft.advancements.AdvancementHolder;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public interface AdvancementSubProvider
/*    */ {
/*    */   void generate(HolderLookup.Provider paramProvider, Consumer<AdvancementHolder> paramConsumer);
/*    */   
/*    */   static AdvancementHolder createPlaceholder(String paramString) {
/* 14 */     return Advancement.Builder.advancement().build(Identifier.parse(paramString));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\advancements\AdvancementSubProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */