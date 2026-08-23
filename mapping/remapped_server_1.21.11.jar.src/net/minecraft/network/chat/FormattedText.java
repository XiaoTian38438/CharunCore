/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.Unit;
/*    */ 
/*    */ public interface FormattedText
/*    */ {
/* 10 */   public static final Optional<Unit> STOP_ITERATION = Optional.of(Unit.INSTANCE);
/*    */   
/* 12 */   public static final FormattedText EMPTY = new FormattedText()
/*    */     {
/*    */       public <T> Optional<T> visit(FormattedText.ContentConsumer<T> param1ContentConsumer) {
/* 15 */         return Optional.empty();
/*    */       }
/*    */ 
/*    */       
/*    */       public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> param1StyledContentConsumer, Style param1Style) {
/* 20 */         return Optional.empty();
/*    */       }
/*    */     };
/*    */   
/*    */   <T> Optional<T> visit(ContentConsumer<T> paramContentConsumer);
/*    */   
/*    */   <T> Optional<T> visit(StyledContentConsumer<T> paramStyledContentConsumer, Style paramStyle);
/*    */   
/*    */   static FormattedText of(final String text) {
/* 29 */     return new FormattedText()
/*    */       {
/*    */         public <T> Optional<T> visit(FormattedText.ContentConsumer<T> param1ContentConsumer) {
/* 32 */           return param1ContentConsumer.accept(text);
/*    */         }
/*    */ 
/*    */         
/*    */         public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> param1StyledContentConsumer, Style param1Style) {
/* 37 */           return param1StyledContentConsumer.accept(param1Style, text);
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   static FormattedText of(final String text, final Style style) {
/* 43 */     return new FormattedText()
/*    */       {
/*    */         public <T> Optional<T> visit(FormattedText.ContentConsumer<T> param1ContentConsumer) {
/* 46 */           return param1ContentConsumer.accept(text);
/*    */         }
/*    */ 
/*    */         
/*    */         public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> param1StyledContentConsumer, Style param1Style) {
/* 51 */           return param1StyledContentConsumer.accept(style.applyTo(param1Style), text);
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   static FormattedText composite(FormattedText... paramVarArgs) {
/* 57 */     return composite((List<? extends FormattedText>)ImmutableList.copyOf((Object[])paramVarArgs));
/*    */   }
/*    */   
/*    */   static FormattedText composite(final List<? extends FormattedText> parts) {
/* 61 */     return new FormattedText()
/*    */       {
/*    */         public <T> Optional<T> visit(FormattedText.ContentConsumer<T> param1ContentConsumer) {
/* 64 */           for (FormattedText formattedText : parts) {
/* 65 */             Optional<T> optional = formattedText.visit(param1ContentConsumer);
/* 66 */             if (optional.isPresent()) {
/* 67 */               return optional;
/*    */             }
/*    */           } 
/*    */           
/* 71 */           return Optional.empty();
/*    */         }
/*    */ 
/*    */         
/*    */         public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> param1StyledContentConsumer, Style param1Style) {
/* 76 */           for (FormattedText formattedText : parts) {
/* 77 */             Optional<T> optional = formattedText.visit(param1StyledContentConsumer, param1Style);
/* 78 */             if (optional.isPresent()) {
/* 79 */               return optional;
/*    */             }
/*    */           } 
/*    */           
/* 83 */           return Optional.empty();
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   default String getString() {
/* 89 */     StringBuilder stringBuilder = new StringBuilder();
/*    */     
/* 91 */     visit(paramString -> {
/*    */           paramStringBuilder.append(paramString);
/*    */           
/*    */           return Optional.empty();
/*    */         });
/* 96 */     return stringBuilder.toString();
/*    */   }
/*    */   
/*    */   public static interface ContentConsumer<T> {
/*    */     Optional<T> accept(String param1String);
/*    */   }
/*    */   
/*    */   public static interface StyledContentConsumer<T> {
/*    */     Optional<T> accept(Style param1Style, String param1String);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\chat\FormattedText.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */