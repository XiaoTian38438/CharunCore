/*    */ package net.minecraft.data.tags;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ 
/*    */ public class FluidTagsProvider extends IntrinsicHolderTagsProvider<Fluid> {
/*    */   public FluidTagsProvider(PackOutput paramPackOutput, CompletableFuture<HolderLookup.Provider> paramCompletableFuture) {
/* 14 */     super(paramPackOutput, Registries.FLUID, paramCompletableFuture, paramFluid -> paramFluid.builtInRegistryHolder().key());
/*    */   }
/*    */ 
/*    */   
/*    */   protected void addTags(HolderLookup.Provider paramProvider) {
/* 19 */     tag(FluidTags.WATER).add(new Fluid[] { (Fluid)Fluids.WATER, (Fluid)Fluids.FLOWING_WATER });
/* 20 */     tag(FluidTags.LAVA).add(new Fluid[] { (Fluid)Fluids.LAVA, (Fluid)Fluids.FLOWING_LAVA });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\tags\FluidTagsProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */