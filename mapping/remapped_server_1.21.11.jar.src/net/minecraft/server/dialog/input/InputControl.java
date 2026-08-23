/*   */ package net.minecraft.server.dialog.input;
/*   */ 
/*   */ import com.mojang.serialization.MapCodec;
/*   */ import net.minecraft.core.registries.BuiltInRegistries;
/*   */ 
/*   */ public interface InputControl {
/*   */   static {
/* 8 */     MAP_CODEC = BuiltInRegistries.INPUT_CONTROL_TYPE.byNameCodec().dispatchMap(InputControl::mapCodec, paramMapCodec -> paramMapCodec);
/*   */   }
/*   */   
/*   */   public static final MapCodec<InputControl> MAP_CODEC;
/*   */   
/*   */   MapCodec<? extends InputControl> mapCodec();
/*   */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\dialog\input\InputControl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */