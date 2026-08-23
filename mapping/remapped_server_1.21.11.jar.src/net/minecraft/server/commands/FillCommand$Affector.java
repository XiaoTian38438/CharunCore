/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ 
/*     */ @FunctionalInterface
/*     */ public interface Affector {
/*     */   public static final Affector NOOP = (paramServerLevel, paramBlockPos) -> false;
/*     */   
/*     */   boolean affect(ServerLevel paramServerLevel, BlockPos paramBlockPos);
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\FillCommand$Affector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */