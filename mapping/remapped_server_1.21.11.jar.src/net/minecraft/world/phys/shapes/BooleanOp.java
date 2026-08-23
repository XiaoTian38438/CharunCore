/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ public interface BooleanOp {
/*    */   static {
/*  5 */     NOT_OR = ((paramBoolean1, paramBoolean2) -> (!paramBoolean1 && !paramBoolean2));
/*  6 */     ONLY_SECOND = ((paramBoolean1, paramBoolean2) -> (paramBoolean2 && !paramBoolean1));
/*  7 */     NOT_FIRST = ((paramBoolean1, paramBoolean2) -> !paramBoolean1);
/*  8 */     ONLY_FIRST = ((paramBoolean1, paramBoolean2) -> (paramBoolean1 && !paramBoolean2));
/*  9 */     NOT_SECOND = ((paramBoolean1, paramBoolean2) -> !paramBoolean2);
/* 10 */     NOT_SAME = ((paramBoolean1, paramBoolean2) -> (paramBoolean1 != paramBoolean2));
/* 11 */     NOT_AND = ((paramBoolean1, paramBoolean2) -> (!paramBoolean1 || !paramBoolean2));
/* 12 */     AND = ((paramBoolean1, paramBoolean2) -> (paramBoolean1 && paramBoolean2));
/* 13 */     SAME = ((paramBoolean1, paramBoolean2) -> (paramBoolean1 == paramBoolean2));
/* 14 */     SECOND = ((paramBoolean1, paramBoolean2) -> paramBoolean2);
/* 15 */     CAUSES = ((paramBoolean1, paramBoolean2) -> (!paramBoolean1 || paramBoolean2));
/* 16 */     FIRST = ((paramBoolean1, paramBoolean2) -> paramBoolean1);
/* 17 */     CAUSED_BY = ((paramBoolean1, paramBoolean2) -> (paramBoolean1 || !paramBoolean2));
/* 18 */     OR = ((paramBoolean1, paramBoolean2) -> (paramBoolean1 || paramBoolean2));
/*    */   }
/*    */   
/*    */   public static final BooleanOp FALSE = (paramBoolean1, paramBoolean2) -> false;
/*    */   public static final BooleanOp NOT_OR;
/*    */   public static final BooleanOp ONLY_SECOND;
/*    */   public static final BooleanOp NOT_FIRST;
/*    */   public static final BooleanOp ONLY_FIRST;
/*    */   public static final BooleanOp NOT_SECOND;
/*    */   public static final BooleanOp NOT_SAME;
/*    */   public static final BooleanOp NOT_AND;
/*    */   public static final BooleanOp AND;
/*    */   public static final BooleanOp SAME;
/*    */   public static final BooleanOp SECOND;
/*    */   public static final BooleanOp CAUSES;
/*    */   public static final BooleanOp FIRST;
/*    */   public static final BooleanOp CAUSED_BY;
/*    */   public static final BooleanOp OR;
/*    */   public static final BooleanOp TRUE = (paramBoolean1, paramBoolean2) -> true;
/*    */   
/*    */   boolean apply(boolean paramBoolean1, boolean paramBoolean2);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\phys\shapes\BooleanOp.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */