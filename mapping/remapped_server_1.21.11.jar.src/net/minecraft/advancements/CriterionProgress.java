/*    */ package net.minecraft.advancements;
/*    */ 
/*    */ import java.time.Instant;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CriterionProgress
/*    */ {
/*    */   private Instant obtained;
/*    */   
/*    */   public CriterionProgress() {}
/*    */   
/*    */   public CriterionProgress(Instant paramInstant) {
/* 15 */     this.obtained = paramInstant;
/*    */   }
/*    */   
/*    */   public boolean isDone() {
/* 19 */     return (this.obtained != null);
/*    */   }
/*    */   
/*    */   public void grant() {
/* 23 */     this.obtained = Instant.now();
/*    */   }
/*    */   
/*    */   public void revoke() {
/* 27 */     this.obtained = null;
/*    */   }
/*    */   
/*    */   public Instant getObtained() {
/* 31 */     return this.obtained;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 37 */     return "CriterionProgress{obtained=" + String.valueOf((this.obtained == null) ? "false" : this.obtained) + "}";
/*    */   }
/*    */ 
/*    */   
/*    */   public void serializeToNetwork(FriendlyByteBuf paramFriendlyByteBuf) {
/* 42 */     paramFriendlyByteBuf.writeNullable(this.obtained, FriendlyByteBuf::writeInstant);
/*    */   }
/*    */   
/*    */   public static CriterionProgress fromNetwork(FriendlyByteBuf paramFriendlyByteBuf) {
/* 46 */     CriterionProgress criterionProgress = new CriterionProgress();
/* 47 */     criterionProgress.obtained = (Instant)paramFriendlyByteBuf.readNullable(FriendlyByteBuf::readInstant);
/* 48 */     return criterionProgress;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\advancements\CriterionProgress.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */