/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import java.util.OptionalInt;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.core.dispenser.BlockSource;
/*    */ import net.minecraft.world.level.block.DispenserBlock;
/*    */ import net.minecraft.world.phys.Vec3;
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
/*    */   private ProjectileItem.PositionFunction positionFunction;
/*    */   private float uncertainty;
/*    */   private float power;
/*    */   private OptionalInt overrideDispenseEvent;
/*    */   
/*    */   public Builder() {
/* 37 */     this.positionFunction = ((paramBlockSource, paramDirection) -> DispenserBlock.getDispensePosition(paramBlockSource, 0.7D, new Vec3(0.0D, 0.1D, 0.0D)));
/* 38 */     this.uncertainty = 6.0F;
/* 39 */     this.power = 1.1F;
/* 40 */     this.overrideDispenseEvent = OptionalInt.empty();
/*    */   }
/*    */   public Builder positionFunction(ProjectileItem.PositionFunction paramPositionFunction) {
/* 43 */     this.positionFunction = paramPositionFunction;
/* 44 */     return this;
/*    */   }
/*    */   
/*    */   public Builder uncertainty(float paramFloat) {
/* 48 */     this.uncertainty = paramFloat;
/* 49 */     return this;
/*    */   }
/*    */   
/*    */   public Builder power(float paramFloat) {
/* 53 */     this.power = paramFloat;
/* 54 */     return this;
/*    */   }
/*    */   
/*    */   public Builder overrideDispenseEvent(int paramInt) {
/* 58 */     this.overrideDispenseEvent = OptionalInt.of(paramInt);
/* 59 */     return this;
/*    */   }
/*    */   
/*    */   public ProjectileItem.DispenseConfig build() {
/* 63 */     return new ProjectileItem.DispenseConfig(this.positionFunction, this.uncertainty, this.power, this.overrideDispenseEvent);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\ProjectileItem$DispenseConfig$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */