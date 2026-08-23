/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiConsumer;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public interface ContainerLevelAccess
/*    */ {
/* 11 */   public static final ContainerLevelAccess NULL = new ContainerLevelAccess()
/*    */     {
/*    */       public <T> Optional<T> evaluate(BiFunction<Level, BlockPos, T> param1BiFunction) {
/* 14 */         return Optional.empty();
/*    */       }
/*    */     };
/*    */   
/*    */   static ContainerLevelAccess create(final Level level, final BlockPos pos) {
/* 19 */     return new ContainerLevelAccess()
/*    */       {
/*    */         public <T> Optional<T> evaluate(BiFunction<Level, BlockPos, T> param1BiFunction) {
/* 22 */           return Optional.of(param1BiFunction.apply(level, pos));
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   <T> Optional<T> evaluate(BiFunction<Level, BlockPos, T> paramBiFunction);
/*    */   
/*    */   default <T> T evaluate(BiFunction<Level, BlockPos, T> paramBiFunction, T paramT) {
/* 30 */     return evaluate(paramBiFunction).orElse(paramT);
/*    */   }
/*    */   
/*    */   default void execute(BiConsumer<Level, BlockPos> paramBiConsumer) {
/* 34 */     evaluate((paramLevel, paramBlockPos) -> {
/*    */           paramBiConsumer.accept(paramLevel, paramBlockPos);
/*    */           return Optional.empty();
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\inventory\ContainerLevelAccess.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */