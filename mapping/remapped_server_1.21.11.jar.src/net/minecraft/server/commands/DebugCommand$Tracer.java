/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import net.minecraft.commands.CommandSource;
/*     */ import net.minecraft.commands.execution.TraceCallbacks;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Tracer
/*     */   implements CommandSource, TraceCallbacks
/*     */ {
/*     */   public static final int INDENT_OFFSET = 1;
/*     */   private final PrintWriter output;
/*     */   private int lastIndent;
/*     */   private boolean waitingForResult;
/*     */   
/*     */   Tracer(PrintWriter paramPrintWriter) {
/* 164 */     this.output = paramPrintWriter;
/*     */   }
/*     */   
/*     */   private void indentAndSave(int paramInt) {
/* 168 */     printIndent(paramInt);
/* 169 */     this.lastIndent = paramInt;
/*     */   }
/*     */   
/*     */   private void printIndent(int paramInt) {
/* 173 */     for (byte b = 0; b < paramInt + 1; b++) {
/* 174 */       this.output.write("    ");
/*     */     }
/*     */   }
/*     */   
/*     */   private void newLine() {
/* 179 */     if (this.waitingForResult) {
/* 180 */       this.output.println();
/* 181 */       this.waitingForResult = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onCommand(int paramInt, String paramString) {
/* 187 */     newLine();
/* 188 */     indentAndSave(paramInt);
/* 189 */     this.output.print("[C] ");
/* 190 */     this.output.print(paramString);
/* 191 */     this.waitingForResult = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onReturn(int paramInt1, String paramString, int paramInt2) {
/* 196 */     if (this.waitingForResult) {
/* 197 */       this.output.print(" -> ");
/* 198 */       this.output.println(paramInt2);
/* 199 */       this.waitingForResult = false;
/*     */     } else {
/* 201 */       indentAndSave(paramInt1);
/* 202 */       this.output.print("[R = ");
/* 203 */       this.output.print(paramInt2);
/* 204 */       this.output.print("] ");
/* 205 */       this.output.println(paramString);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onCall(int paramInt1, Identifier paramIdentifier, int paramInt2) {
/* 211 */     newLine();
/* 212 */     indentAndSave(paramInt1);
/* 213 */     this.output.print("[F] ");
/* 214 */     this.output.print(paramIdentifier);
/* 215 */     this.output.print(" size=");
/* 216 */     this.output.println(paramInt2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onError(String paramString) {
/* 221 */     newLine();
/* 222 */     indentAndSave(this.lastIndent + 1);
/* 223 */     this.output.print("[E] ");
/* 224 */     this.output.print(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendSystemMessage(Component paramComponent) {
/* 229 */     newLine();
/* 230 */     printIndent(this.lastIndent + 1);
/* 231 */     this.output.print("[M] ");
/* 232 */     this.output.println(paramComponent.getString());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean acceptsSuccess() {
/* 237 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean acceptsFailure() {
/* 242 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldInformAdmins() {
/* 247 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean alwaysAccepts() {
/* 252 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 257 */     IOUtils.closeQuietly(this.output);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\DebugCommand$Tracer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */