/*    */ package net.minecraft.world.entity.vehicle.boat;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.world.entity.EntityDimensions;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class ChestRaft
/*    */   extends AbstractChestBoat {
/*    */   public ChestRaft(EntityType<? extends ChestRaft> paramEntityType, Level paramLevel, Supplier<Item> paramSupplier) {
/* 12 */     super((EntityType)paramEntityType, paramLevel, paramSupplier);
/*    */   }
/*    */ 
/*    */   
/*    */   protected double rideHeight(EntityDimensions paramEntityDimensions) {
/* 17 */     return (paramEntityDimensions.height() * 0.8888889F);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\vehicle\boat\ChestRaft.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */