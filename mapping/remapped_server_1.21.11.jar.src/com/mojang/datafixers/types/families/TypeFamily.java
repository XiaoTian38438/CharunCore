/*    */ package com.mojang.datafixers.types.families;
/*    */ 
/*    */ import com.mojang.datafixers.FamilyOptic;
/*    */ import com.mojang.datafixers.TypedOptic;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import java.util.Objects;
/*    */ import java.util.function.IntFunction;
/*    */ 
/*    */ 
/*    */ public interface TypeFamily
/*    */ {
/*    */   Type<?> apply(int paramInt);
/*    */   
/*    */   static <A, B> FamilyOptic<A, B> familyOptic(IntFunction<TypedOptic<?, ?, A, B>> paramIntFunction) {
/* 15 */     Objects.requireNonNull(paramIntFunction); return paramIntFunction::apply;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\types\families\TypeFamily.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */