/*    */ package com.mojang.datafixers.types.families;
/*    */ 
/*    */ import com.mojang.datafixers.RewriteResult;
/*    */ import com.mojang.datafixers.functions.PointFree;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.stream.Collectors;
/*    */ 
/*    */ 
/*    */ public final class ListAlgebra
/*    */   implements Algebra
/*    */ {
/*    */   private final String name;
/*    */   private final List<RewriteResult<?, ?>> views;
/*    */   private int hashCode;
/*    */   
/*    */   public ListAlgebra(String paramString, List<RewriteResult<?, ?>> paramList) {
/* 18 */     this.name = paramString;
/* 19 */     this.views = paramList;
/*    */   }
/*    */ 
/*    */   
/*    */   public RewriteResult<?, ?> apply(int paramInt) {
/* 24 */     return this.views.get(paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 29 */     return toString(0);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString(int paramInt) {
/* 34 */     String str = "\n" + PointFree.indent(paramInt + 1);
/* 35 */     return "Algebra[" + this.name + str + (String)this.views.stream().map(paramRewriteResult -> paramRewriteResult.view().function().toString(paramInt + 1)).collect(Collectors.joining(str)) + "\n" + PointFree.indent(paramInt) + "]";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 40 */     if (this == paramObject) {
/* 41 */       return true;
/*    */     }
/* 43 */     if (!(paramObject instanceof ListAlgebra)) {
/* 44 */       return false;
/*    */     }
/* 46 */     ListAlgebra listAlgebra = (ListAlgebra)paramObject;
/* 47 */     return Objects.equals(this.views, listAlgebra.views);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 52 */     if (this.hashCode == 0) {
/* 53 */       this.hashCode = this.views.hashCode();
/*    */     }
/* 55 */     return this.hashCode;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\types\families\ListAlgebra.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */