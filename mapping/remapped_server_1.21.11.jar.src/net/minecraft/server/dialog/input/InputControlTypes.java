/*    */ package net.minecraft.server.dialog.input;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class InputControlTypes {
/*    */   public static MapCodec<? extends InputControl> bootstrap(Registry<MapCodec<? extends InputControl>> paramRegistry) {
/*  9 */     Registry.register(paramRegistry, Identifier.withDefaultNamespace("boolean"), BooleanInput.MAP_CODEC);
/* 10 */     Registry.register(paramRegistry, Identifier.withDefaultNamespace("number_range"), NumberRangeInput.MAP_CODEC);
/* 11 */     Registry.register(paramRegistry, Identifier.withDefaultNamespace("single_option"), SingleOptionInput.MAP_CODEC);
/* 12 */     return (MapCodec<? extends InputControl>)Registry.register(paramRegistry, Identifier.withDefaultNamespace("text"), TextInput.MAP_CODEC);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\dialog\input\InputControlTypes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */