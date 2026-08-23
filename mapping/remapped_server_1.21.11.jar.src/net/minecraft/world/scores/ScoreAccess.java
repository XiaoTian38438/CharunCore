/*    */ package net.minecraft.world.scores;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.numbers.NumberFormat;
/*    */ 
/*    */ public interface ScoreAccess
/*    */ {
/*    */   int get();
/*    */   
/*    */   void set(int paramInt);
/*    */   
/*    */   default int add(int paramInt) {
/* 13 */     int i = get() + paramInt;
/* 14 */     set(i);
/* 15 */     return i;
/*    */   }
/*    */   
/*    */   default int increment() {
/* 19 */     return add(1);
/*    */   }
/*    */   
/*    */   default void reset() {
/* 23 */     set(0);
/*    */   }
/*    */   
/*    */   boolean locked();
/*    */   
/*    */   void unlock();
/*    */   
/*    */   void lock();
/*    */   
/*    */   Component display();
/*    */   
/*    */   void display(Component paramComponent);
/*    */   
/*    */   void numberFormatOverride(NumberFormat paramNumberFormat);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\scores\ScoreAccess.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */