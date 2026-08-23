/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ 
/*    */ public class NopProcessor extends StructureProcessor {
/*  6 */   public static final MapCodec<NopProcessor> CODEC = MapCodec.unit(() -> INSTANCE);
/*    */   
/*  8 */   public static final NopProcessor INSTANCE = new NopProcessor();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected StructureProcessorType<?> getType() {
/* 15 */     return StructureProcessorType.NOP;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\NopProcessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */