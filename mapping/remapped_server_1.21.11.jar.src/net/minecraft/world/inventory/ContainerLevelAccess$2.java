/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.Level;
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
/*    */ class null
/*    */   implements ContainerLevelAccess
/*    */ {
/*    */   public <T> Optional<T> evaluate(BiFunction<Level, BlockPos, T> paramBiFunction) {
/* 22 */     return Optional.of(paramBiFunction.apply(level, pos));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\inventory\ContainerLevelAccess$2.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */