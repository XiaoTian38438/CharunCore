/*    */ package net.minecraft.server.packs;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.HashSet;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Set;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.packs.metadata.MetadataSectionType;
/*    */ import net.minecraft.server.packs.resources.IoSupplier;
/*    */ 
/*    */ 
/*    */ public class CompositePackResources
/*    */   implements PackResources
/*    */ {
/*    */   private final PackResources primaryPackResources;
/*    */   private final List<PackResources> packResourcesStack;
/*    */   
/*    */   public CompositePackResources(PackResources paramPackResources, List<PackResources> paramList) {
/* 24 */     this.primaryPackResources = paramPackResources;
/*    */     
/* 26 */     ArrayList<PackResources> arrayList = new ArrayList(paramList.size() + 1);
/* 27 */     arrayList.addAll(Lists.reverse(paramList));
/* 28 */     arrayList.add(paramPackResources);
/* 29 */     this.packResourcesStack = List.copyOf(arrayList);
/*    */   }
/*    */ 
/*    */   
/*    */   public IoSupplier<InputStream> getRootResource(String... paramVarArgs) {
/* 34 */     return this.primaryPackResources.getRootResource(paramVarArgs);
/*    */   }
/*    */ 
/*    */   
/*    */   public IoSupplier<InputStream> getResource(PackType paramPackType, Identifier paramIdentifier) {
/* 39 */     for (PackResources packResources : this.packResourcesStack) {
/* 40 */       IoSupplier<InputStream> ioSupplier = packResources.getResource(paramPackType, paramIdentifier);
/* 41 */       if (ioSupplier != null) {
/* 42 */         return ioSupplier;
/*    */       }
/*    */     } 
/*    */     
/* 46 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void listResources(PackType paramPackType, String paramString1, String paramString2, PackResources.ResourceOutput paramResourceOutput) {
/* 51 */     HashMap<Object, Object> hashMap = new HashMap<>();
/* 52 */     for (PackResources packResources : this.packResourcesStack) {
/* 53 */       Objects.requireNonNull(hashMap); packResources.listResources(paramPackType, paramString1, paramString2, hashMap::putIfAbsent);
/*    */     } 
/* 55 */     hashMap.forEach(paramResourceOutput);
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<String> getNamespaces(PackType paramPackType) {
/* 60 */     HashSet<String> hashSet = new HashSet();
/* 61 */     for (PackResources packResources : this.packResourcesStack) {
/* 62 */       hashSet.addAll(packResources.getNamespaces(paramPackType));
/*    */     }
/* 64 */     return hashSet;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> T getMetadataSection(MetadataSectionType<T> paramMetadataSectionType) throws IOException {
/* 69 */     return this.primaryPackResources.getMetadataSection(paramMetadataSectionType);
/*    */   }
/*    */ 
/*    */   
/*    */   public PackLocationInfo location() {
/* 74 */     return this.primaryPackResources.location();
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 79 */     this.packResourcesStack.forEach(PackResources::close);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\packs\CompositePackResources.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */