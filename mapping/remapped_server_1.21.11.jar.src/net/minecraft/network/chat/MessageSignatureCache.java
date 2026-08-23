/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
/*    */ import java.util.ArrayDeque;
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
/*    */ public class MessageSignatureCache
/*    */ {
/*    */   public static final int NOT_FOUND = -1;
/*    */   private static final int DEFAULT_CAPACITY = 128;
/*    */   private final MessageSignature[] entries;
/*    */   
/*    */   public MessageSignatureCache(int paramInt) {
/* 29 */     this.entries = new MessageSignature[paramInt];
/*    */   }
/*    */   
/*    */   public static MessageSignatureCache createDefault() {
/* 33 */     return new MessageSignatureCache(128);
/*    */   }
/*    */   
/*    */   public int pack(MessageSignature paramMessageSignature) {
/* 37 */     for (byte b = 0; b < this.entries.length; b++) {
/* 38 */       if (paramMessageSignature.equals(this.entries[b])) {
/* 39 */         return b;
/*    */       }
/*    */     } 
/* 42 */     return -1;
/*    */   }
/*    */   
/*    */   public MessageSignature unpack(int paramInt) {
/* 46 */     return this.entries[paramInt];
/*    */   }
/*    */   
/*    */   public void push(SignedMessageBody paramSignedMessageBody, MessageSignature paramMessageSignature) {
/* 50 */     List<MessageSignature> list = paramSignedMessageBody.lastSeen().entries();
/*    */     
/* 52 */     ArrayDeque<MessageSignature> arrayDeque = new ArrayDeque(list.size() + 1);
/* 53 */     arrayDeque.addAll(list);
/* 54 */     if (paramMessageSignature != null) {
/* 55 */       arrayDeque.add(paramMessageSignature);
/*    */     }
/*    */     
/* 58 */     push(arrayDeque);
/*    */   }
/*    */   
/*    */   @VisibleForTesting
/*    */   void push(List<MessageSignature> paramList) {
/* 63 */     push(new ArrayDeque<>(paramList));
/*    */   }
/*    */   
/*    */   private void push(ArrayDeque<MessageSignature> paramArrayDeque) {
/* 67 */     ObjectOpenHashSet objectOpenHashSet = new ObjectOpenHashSet(paramArrayDeque);
/*    */     
/* 69 */     byte b = 0;
/* 70 */     while (!paramArrayDeque.isEmpty() && b < this.entries.length) {
/* 71 */       MessageSignature messageSignature = this.entries[b];
/* 72 */       this.entries[b] = paramArrayDeque.removeLast();
/* 73 */       if (messageSignature != null && !objectOpenHashSet.contains(messageSignature)) {
/* 74 */         paramArrayDeque.addFirst(messageSignature);
/*    */       }
/* 76 */       b++;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\chat\MessageSignatureCache.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */