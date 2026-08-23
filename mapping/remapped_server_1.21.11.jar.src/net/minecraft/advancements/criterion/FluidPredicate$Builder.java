/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Builder
/*    */ {
/* 40 */   private Optional<HolderSet<Fluid>> fluids = Optional.empty();
/* 41 */   private Optional<StatePropertiesPredicate> properties = Optional.empty();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Builder fluid() {
/* 47 */     return new Builder();
/*    */   }
/*    */   
/*    */   public Builder of(Fluid paramFluid) {
/* 51 */     this.fluids = Optional.of(HolderSet.direct(new Holder[] { (Holder)paramFluid.builtInRegistryHolder() }));
/* 52 */     return this;
/*    */   }
/*    */   
/*    */   public Builder of(HolderSet<Fluid> paramHolderSet) {
/* 56 */     this.fluids = Optional.of(paramHolderSet);
/* 57 */     return this;
/*    */   }
/*    */   
/*    */   public Builder setProperties(StatePropertiesPredicate paramStatePropertiesPredicate) {
/* 61 */     this.properties = Optional.of(paramStatePropertiesPredicate);
/* 62 */     return this;
/*    */   }
/*    */   
/*    */   public FluidPredicate build() {
/* 66 */     return new FluidPredicate(this.fluids, this.properties);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\advancements\criterion\FluidPredicate$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */