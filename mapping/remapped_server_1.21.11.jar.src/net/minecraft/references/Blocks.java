/*    */ package net.minecraft.references;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ 
/*    */ public class Blocks {
/*  9 */   public static final ResourceKey<Block> PUMPKIN = createKey("pumpkin");
/* 10 */   public static final ResourceKey<Block> PUMPKIN_STEM = createKey("pumpkin_stem");
/* 11 */   public static final ResourceKey<Block> ATTACHED_PUMPKIN_STEM = createKey("attached_pumpkin_stem");
/* 12 */   public static final ResourceKey<Block> MELON = createKey("melon");
/* 13 */   public static final ResourceKey<Block> MELON_STEM = createKey("melon_stem");
/* 14 */   public static final ResourceKey<Block> ATTACHED_MELON_STEM = createKey("attached_melon_stem");
/*    */   
/*    */   private static ResourceKey<Block> createKey(String paramString) {
/* 17 */     return ResourceKey.create(Registries.BLOCK, Identifier.withDefaultNamespace(paramString));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\references\Blocks.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */