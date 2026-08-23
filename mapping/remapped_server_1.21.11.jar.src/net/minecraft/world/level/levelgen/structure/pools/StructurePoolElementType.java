/*    */ package net.minecraft.world.level.levelgen.structure.pools;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public interface StructurePoolElementType<P extends StructurePoolElement>
/*    */ {
/*  9 */   public static final StructurePoolElementType<SinglePoolElement> SINGLE = register("single_pool_element", SinglePoolElement.CODEC);
/* 10 */   public static final StructurePoolElementType<ListPoolElement> LIST = register("list_pool_element", ListPoolElement.CODEC);
/* 11 */   public static final StructurePoolElementType<FeaturePoolElement> FEATURE = register("feature_pool_element", FeaturePoolElement.CODEC);
/* 12 */   public static final StructurePoolElementType<EmptyPoolElement> EMPTY = register("empty_pool_element", EmptyPoolElement.CODEC);
/* 13 */   public static final StructurePoolElementType<LegacySinglePoolElement> LEGACY = register("legacy_single_pool_element", LegacySinglePoolElement.CODEC);
/*    */ 
/*    */   
/*    */   MapCodec<P> codec();
/*    */   
/*    */   static <P extends StructurePoolElement> StructurePoolElementType<P> register(String paramString, MapCodec<P> paramMapCodec) {
/* 19 */     return (StructurePoolElementType<P>)Registry.register(BuiltInRegistries.STRUCTURE_POOL_ELEMENT, paramString, () -> paramMapCodec);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\pools\StructurePoolElementType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */