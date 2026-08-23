/*    */ package net.minecraft.world.flag;
/*    */ 
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface FeatureElement
/*    */ {
/* 18 */   public static final Set<ResourceKey<? extends Registry<? extends FeatureElement>>> FILTERED_REGISTRIES = Set.of(Registries.ITEM, Registries.BLOCK, Registries.ENTITY_TYPE, Registries.GAME_RULE, Registries.MENU, Registries.POTION, Registries.MOB_EFFECT);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   FeatureFlagSet requiredFeatures();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default boolean isEnabled(FeatureFlagSet paramFeatureFlagSet) {
/* 31 */     return requiredFeatures().isSubsetOf(paramFeatureFlagSet);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\flag\FeatureElement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */