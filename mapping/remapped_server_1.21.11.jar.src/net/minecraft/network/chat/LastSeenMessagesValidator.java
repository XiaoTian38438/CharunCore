/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectList;
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
/*    */ public class LastSeenMessagesValidator
/*    */ {
/*    */   private final int lastSeenCount;
/* 19 */   private final ObjectList<LastSeenTrackedEntry> trackedMessages = (ObjectList<LastSeenTrackedEntry>)new ObjectArrayList();
/*    */   
/*    */   private MessageSignature lastPendingMessage;
/*    */   
/*    */   public LastSeenMessagesValidator(int paramInt) {
/* 24 */     this.lastSeenCount = paramInt;
/* 25 */     for (byte b = 0; b < paramInt; b++) {
/* 26 */       this.trackedMessages.add(null);
/*    */     }
/*    */   }
/*    */   
/*    */   public void addPending(MessageSignature paramMessageSignature) {
/* 31 */     if (!paramMessageSignature.equals(this.lastPendingMessage)) {
/* 32 */       this.trackedMessages.add(new LastSeenTrackedEntry(paramMessageSignature, true));
/* 33 */       this.lastPendingMessage = paramMessageSignature;
/*    */     } 
/*    */   }
/*    */   
/*    */   public int trackedMessagesCount() {
/* 38 */     return this.trackedMessages.size();
/*    */   }
/*    */   
/*    */   public void applyOffset(int paramInt) throws ValidationException {
/* 42 */     int i = this.trackedMessages.size() - this.lastSeenCount;
/* 43 */     if (paramInt < 0 || paramInt > i) {
/* 44 */       throw new ValidationException("Advanced last seen window by " + paramInt + " messages, but expected at most " + i);
/*    */     }
/* 46 */     this.trackedMessages.removeElements(0, paramInt);
/*    */   }
/*    */   
/*    */   public LastSeenMessages applyUpdate(LastSeenMessages.Update paramUpdate) throws ValidationException {
/* 50 */     applyOffset(paramUpdate.offset());
/*    */     
/* 52 */     ObjectArrayList objectArrayList = new ObjectArrayList(paramUpdate.acknowledged().cardinality());
/* 53 */     if (paramUpdate.acknowledged().length() > this.lastSeenCount) {
/* 54 */       throw new ValidationException("Last seen update contained " + paramUpdate.acknowledged().length() + " messages, but maximum window size is " + this.lastSeenCount);
/*    */     }
/*    */     
/* 57 */     for (byte b = 0; b < this.lastSeenCount; b++) {
/* 58 */       boolean bool = paramUpdate.acknowledged().get(b);
/* 59 */       LastSeenTrackedEntry lastSeenTrackedEntry = (LastSeenTrackedEntry)this.trackedMessages.get(b);
/* 60 */       if (bool) {
/* 61 */         if (lastSeenTrackedEntry == null) {
/* 62 */           throw new ValidationException("Last seen update acknowledged unknown or previously ignored message at index " + b);
/*    */         }
/* 64 */         this.trackedMessages.set(b, lastSeenTrackedEntry.acknowledge());
/* 65 */         objectArrayList.add(lastSeenTrackedEntry.signature());
/*    */       } else {
/* 67 */         if (lastSeenTrackedEntry != null && !lastSeenTrackedEntry.pending()) {
/* 68 */           throw new ValidationException("Last seen update ignored previously acknowledged message at index " + b + " and signature " + String.valueOf(lastSeenTrackedEntry.signature()));
/*    */         }
/* 70 */         this.trackedMessages.set(b, null);
/*    */       } 
/*    */     } 
/*    */     
/* 74 */     LastSeenMessages lastSeenMessages = new LastSeenMessages((List<MessageSignature>)objectArrayList);
/* 75 */     if (!paramUpdate.verifyChecksum(lastSeenMessages)) {
/* 76 */       throw new ValidationException("Checksum mismatch on last seen update: the client and server must have desynced");
/*    */     }
/*    */     
/* 79 */     return lastSeenMessages;
/*    */   }
/*    */   
/*    */   public static class ValidationException extends Exception {
/*    */     public ValidationException(String param1String) {
/* 84 */       super(param1String);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\chat\LastSeenMessagesValidator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */