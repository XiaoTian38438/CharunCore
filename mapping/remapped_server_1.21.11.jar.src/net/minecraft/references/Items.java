/*    */ package net.minecraft.references;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.Item;
/*    */ 
/*    */ public class Items {
/*  9 */   public static final ResourceKey<Item> PUMPKIN_SEEDS = createKey("pumpkin_seeds");
/* 10 */   public static final ResourceKey<Item> MELON_SEEDS = createKey("melon_seeds");
/*    */   
/*    */   private static ResourceKey<Item> createKey(String paramString) {
/* 13 */     return ResourceKey.create(Registries.ITEM, Identifier.withDefaultNamespace(paramString));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\references\Items.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */