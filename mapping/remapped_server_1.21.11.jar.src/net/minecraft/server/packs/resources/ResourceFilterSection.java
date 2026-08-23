/*    */ package net.minecraft.server.packs.resources;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.IdentifierPattern;
/*    */ 
/*    */ public class ResourceFilterSection {
/*    */   static {
/* 11 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)Codec.list(IdentifierPattern.CODEC).fieldOf("block").forGetter(())).apply((Applicative)paramInstance, ResourceFilterSection::new));
/*    */   }
/*    */   
/*    */   private static final Codec<ResourceFilterSection> CODEC;
/* 15 */   public static final MetadataSectionType<ResourceFilterSection> TYPE = new MetadataSectionType("filter", CODEC);
/*    */   
/*    */   private final List<IdentifierPattern> blockList;
/*    */   
/*    */   public ResourceFilterSection(List<IdentifierPattern> paramList) {
/* 20 */     this.blockList = List.copyOf(paramList);
/*    */   }
/*    */   
/*    */   public boolean isNamespaceFiltered(String paramString) {
/* 24 */     return this.blockList.stream().anyMatch(paramIdentifierPattern -> paramIdentifierPattern.namespacePredicate().test(paramString));
/*    */   }
/*    */   
/*    */   public boolean isPathFiltered(String paramString) {
/* 28 */     return this.blockList.stream().anyMatch(paramIdentifierPattern -> paramIdentifierPattern.pathPredicate().test(paramString));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\packs\resources\ResourceFilterSection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */