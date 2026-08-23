/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class FluidTags
/*    */ {
/* 11 */   public static final TagKey<Fluid> WATER = create("water");
/* 12 */   public static final TagKey<Fluid> LAVA = create("lava");
/*    */   
/*    */   private static TagKey<Fluid> create(String paramString) {
/* 15 */     return TagKey.create(Registries.FLUID, Identifier.withDefaultNamespace(paramString));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\tags\FluidTags.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */