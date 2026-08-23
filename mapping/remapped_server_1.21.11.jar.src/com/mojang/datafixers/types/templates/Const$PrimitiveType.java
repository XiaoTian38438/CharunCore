/*     */ package com.mojang.datafixers.types.templates;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.serialization.Codec;
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
/*     */ public final class PrimitiveType<A>
/*     */   extends Type<A>
/*     */ {
/*     */   private final Codec<A> codec;
/*     */   
/*     */   public PrimitiveType(Codec<A> paramCodec) {
/*  82 */     this.codec = paramCodec;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject, boolean paramBoolean1, boolean paramBoolean2) {
/*  87 */     return (this == paramObject);
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeTemplate buildTemplate() {
/*  92 */     return DSL.constType(this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Codec<A> buildCodec() {
/*  97 */     return this.codec;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 102 */     return this.codec.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\types\templates\Const$PrimitiveType.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */