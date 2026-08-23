/*    */ package net.minecraft.server.packs;
/*    */ 
/*    */ import java.util.Map;
/*    */ import net.minecraft.server.packs.metadata.MetadataSectionType;
/*    */ 
/*    */ public class BuiltInMetadata
/*    */ {
/*  8 */   private static final BuiltInMetadata EMPTY = new BuiltInMetadata(Map.of());
/*    */   
/*    */   private final Map<MetadataSectionType<?>, ?> values;
/*    */   
/*    */   private BuiltInMetadata(Map<MetadataSectionType<?>, ?> paramMap) {
/* 13 */     this.values = paramMap;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> T get(MetadataSectionType<T> paramMetadataSectionType) {
/* 18 */     return (T)this.values.get(paramMetadataSectionType);
/*    */   }
/*    */   
/*    */   public static BuiltInMetadata of() {
/* 22 */     return EMPTY;
/*    */   }
/*    */   
/*    */   public static <T> BuiltInMetadata of(MetadataSectionType<T> paramMetadataSectionType, T paramT) {
/* 26 */     return new BuiltInMetadata(Map.of(paramMetadataSectionType, paramT));
/*    */   }
/*    */   
/*    */   public static <T1, T2> BuiltInMetadata of(MetadataSectionType<T1> paramMetadataSectionType, T1 paramT1, MetadataSectionType<T2> paramMetadataSectionType1, T2 paramT2) {
/* 30 */     return new BuiltInMetadata(Map.of(paramMetadataSectionType, paramT1, paramMetadataSectionType1, paramT2));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\packs\BuiltInMetadata.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */