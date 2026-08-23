/*    */ package com.mojang.datafixers.types.templates;
/*    */ 
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.families.TypeFamily;
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
/*    */ class null
/*    */   implements TypeFamily
/*    */ {
/*    */   public Type<?> apply(int paramInt) {
/* 35 */     if (paramInt < 0) {
/* 36 */       throw new IndexOutOfBoundsException();
/*    */     }
/* 38 */     return new Check.CheckType(Check.this.name, paramInt, Check.this.index, Check.this.element.apply(family).apply(paramInt));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\types\templates\Check$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */