/*   */ package net.minecraft.world.level.chunk;
/*   */ 
/*   */ public interface PaletteResize<T>
/*   */ {
/*   */   int onResize(int paramInt, T paramT);
/*   */   
/*   */   static <T> PaletteResize<T> noResizeExpected() {
/* 8 */     return (paramInt, paramObject) -> {
/*   */         throw new IllegalArgumentException("Unexpected palette resize, bits = " + paramInt + ", added value = " + String.valueOf(paramObject));
/*   */       };
/*   */   }
/*   */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\chunk\PaletteResize.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */