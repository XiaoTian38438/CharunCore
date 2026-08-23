/*    */ package com.mojang.datafixers.types.constant;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Optional;
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class EmptyPartPassthrough
/*    */   extends Type<Dynamic<?>>
/*    */ {
/*    */   public String toString() {
/* 17 */     return "EmptyPartPassthrough";
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<Dynamic<?>> point(DynamicOps<?> paramDynamicOps) {
/* 22 */     return Optional.of(new Dynamic(paramDynamicOps));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject, boolean paramBoolean1, boolean paramBoolean2) {
/* 27 */     return (this == paramObject);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeTemplate buildTemplate() {
/* 32 */     return DSL.constType(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public Codec<Dynamic<?>> buildCodec() {
/* 37 */     return Codec.PASSTHROUGH;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\types\constant\EmptyPartPassthrough.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */