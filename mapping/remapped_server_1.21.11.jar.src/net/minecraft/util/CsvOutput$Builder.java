/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.io.IOException;
/*    */ import java.io.Writer;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Builder
/*    */ {
/* 46 */   private final List<String> headers = Lists.newArrayList();
/*    */   
/*    */   public Builder addColumn(String paramString) {
/* 49 */     this.headers.add(paramString);
/* 50 */     return this;
/*    */   }
/*    */   
/*    */   public CsvOutput build(Writer paramWriter) throws IOException {
/* 54 */     return new CsvOutput(paramWriter, this.headers);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\CsvOutput$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */