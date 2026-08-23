/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class ClipBlockStateContext
/*    */ {
/*    */   private final Vec3 from;
/*    */   private final Vec3 to;
/*    */   private final Predicate<BlockState> block;
/*    */   
/*    */   public ClipBlockStateContext(Vec3 paramVec31, Vec3 paramVec32, Predicate<BlockState> paramPredicate) {
/* 14 */     this.from = paramVec31;
/* 15 */     this.to = paramVec32;
/* 16 */     this.block = paramPredicate;
/*    */   }
/*    */   
/*    */   public Vec3 getTo() {
/* 20 */     return this.to;
/*    */   }
/*    */   
/*    */   public Vec3 getFrom() {
/* 24 */     return this.from;
/*    */   }
/*    */   
/*    */   public Predicate<BlockState> isTargetBlock() {
/* 28 */     return this.block;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\ClipBlockStateContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */