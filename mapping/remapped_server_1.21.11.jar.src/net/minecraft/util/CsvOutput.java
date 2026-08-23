/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.io.IOException;
/*    */ import java.io.Writer;
/*    */ import java.util.List;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ import org.apache.commons.lang3.StringEscapeUtils;
/*    */ 
/*    */ 
/*    */ public class CsvOutput
/*    */ {
/*    */   private static final String LINE_SEPARATOR = "\r\n";
/*    */   private static final String FIELD_SEPARATOR = ",";
/*    */   private final Writer output;
/*    */   private final int columnCount;
/*    */   
/*    */   CsvOutput(Writer paramWriter, List<String> paramList) throws IOException {
/* 20 */     this.output = paramWriter;
/* 21 */     this.columnCount = paramList.size();
/* 22 */     writeLine(paramList.stream());
/*    */   }
/*    */   
/*    */   public static Builder builder() {
/* 26 */     return new Builder();
/*    */   }
/*    */   
/*    */   public void writeRow(Object... paramVarArgs) throws IOException {
/* 30 */     if (paramVarArgs.length != this.columnCount) {
/* 31 */       throw new IllegalArgumentException("Invalid number of columns, expected " + this.columnCount + ", but got " + paramVarArgs.length);
/*    */     }
/*    */     
/* 34 */     writeLine(Stream.of(paramVarArgs));
/*    */   }
/*    */   
/*    */   private void writeLine(Stream<? extends Object> paramStream) throws IOException {
/* 38 */     this.output.write((String)paramStream.<CharSequence>map(CsvOutput::getStringValue).collect(Collectors.joining(",")) + "\r\n");
/*    */   }
/*    */   
/*    */   private static String getStringValue(Object paramObject) {
/* 42 */     return StringEscapeUtils.escapeCsv((paramObject != null) ? paramObject.toString() : "[null]");
/*    */   }
/*    */   
/*    */   public static class Builder {
/* 46 */     private final List<String> headers = Lists.newArrayList();
/*    */     
/*    */     public Builder addColumn(String param1String) {
/* 49 */       this.headers.add(param1String);
/* 50 */       return this;
/*    */     }
/*    */     
/*    */     public CsvOutput build(Writer param1Writer) throws IOException {
/* 54 */       return new CsvOutput(param1Writer, this.headers);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\CsvOutput.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */