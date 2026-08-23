/*    */ package net.minecraft.world.level.levelgen.feature.rootplacers;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public class RootPlacerType<P extends RootPlacer>
/*    */ {
/*  9 */   public static final RootPlacerType<MangroveRootPlacer> MANGROVE_ROOT_PLACER = register("mangrove_root_placer", MangroveRootPlacer.CODEC);
/*    */   
/*    */   private static <P extends RootPlacer> RootPlacerType<P> register(String paramString, MapCodec<P> paramMapCodec) {
/* 12 */     return (RootPlacerType<P>)Registry.register(BuiltInRegistries.ROOT_PLACER_TYPE, paramString, new RootPlacerType<>(paramMapCodec));
/*    */   }
/*    */   
/*    */   private final MapCodec<P> codec;
/*    */   
/*    */   private RootPlacerType(MapCodec<P> paramMapCodec) {
/* 18 */     this.codec = paramMapCodec;
/*    */   }
/*    */   
/*    */   public MapCodec<P> codec() {
/* 22 */     return this.codec;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\rootplacers\RootPlacerType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */