/*     */ package net.minecraft.util.monitoring.jmx;
/*     */ 
/*     */ import java.util.function.Supplier;
/*     */ import javax.management.MBeanAttributeInfo;
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
/*     */ final class AttributeDescription
/*     */ {
/*     */   final String name;
/*     */   final Supplier<Object> getter;
/*     */   private final String description;
/*     */   private final Class<?> type;
/*     */   
/*     */   AttributeDescription(String paramString1, Supplier<Object> paramSupplier, String paramString2, Class<?> paramClass) {
/* 115 */     this.name = paramString1;
/* 116 */     this.getter = paramSupplier;
/* 117 */     this.description = paramString2;
/* 118 */     this.type = paramClass;
/*     */   }
/*     */   
/*     */   private MBeanAttributeInfo asMBeanAttributeInfo() {
/* 122 */     return new MBeanAttributeInfo(this.name, this.type.getSimpleName(), this.description, true, false, false);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\monitoring\jmx\MinecraftServerStatistics$AttributeDescription.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */