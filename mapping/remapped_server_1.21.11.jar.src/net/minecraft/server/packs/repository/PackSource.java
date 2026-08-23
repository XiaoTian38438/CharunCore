/*    */ package net.minecraft.server.packs.repository;
/*    */ 
/*    */ import java.util.function.UnaryOperator;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ 
/*    */ public interface PackSource {
/*  9 */   public static final UnaryOperator<Component> NO_DECORATION = UnaryOperator.identity();
/*    */ 
/*    */   
/* 12 */   public static final PackSource DEFAULT = create(NO_DECORATION, true);
/* 13 */   public static final PackSource BUILT_IN = create(decorateWithSource("pack.source.builtin"), true);
/* 14 */   public static final PackSource FEATURE = create(decorateWithSource("pack.source.feature"), false);
/* 15 */   public static final PackSource WORLD = create(decorateWithSource("pack.source.world"), true);
/*    */ 
/*    */   
/* 18 */   public static final PackSource SERVER = create(decorateWithSource("pack.source.server"), true);
/*    */   
/*    */   Component decorate(Component paramComponent);
/*    */   
/*    */   boolean shouldAddAutomatically();
/*    */   
/*    */   static PackSource create(final UnaryOperator<Component> decorator, final boolean addAutomatically) {
/* 25 */     return new PackSource()
/*    */       {
/*    */         public Component decorate(Component param1Component) {
/* 28 */           return decorator.apply(param1Component);
/*    */         }
/*    */ 
/*    */         
/*    */         public boolean shouldAddAutomatically() {
/* 33 */           return addAutomatically;
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   private static UnaryOperator<Component> decorateWithSource(String paramString) {
/* 39 */     MutableComponent mutableComponent = Component.translatable(paramString);
/* 40 */     return paramComponent2 -> Component.translatable("pack.nameAndSource", new Object[] { paramComponent2, paramComponent1 }).withStyle(ChatFormatting.GRAY);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\packs\repository\PackSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */