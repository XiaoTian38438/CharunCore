/*    */ package net.minecraft.world.entity.variant;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ public class SpawnConditions
/*    */ {
/*    */   public static MapCodec<? extends SpawnCondition> bootstrap(Registry<MapCodec<? extends SpawnCondition>> paramRegistry) {
/*  9 */     Registry.register(paramRegistry, "structure", StructureCheck.MAP_CODEC);
/* 10 */     Registry.register(paramRegistry, "moon_brightness", MoonBrightnessCheck.MAP_CODEC);
/* 11 */     return (MapCodec<? extends SpawnCondition>)Registry.register(paramRegistry, "biome", BiomeCheck.MAP_CODEC);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\variant\SpawnConditions.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */