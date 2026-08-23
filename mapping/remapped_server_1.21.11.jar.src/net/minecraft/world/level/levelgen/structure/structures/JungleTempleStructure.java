/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.levelgen.structure.SinglePieceStructure;
/*    */ import net.minecraft.world.level.levelgen.structure.Structure;
/*    */ import net.minecraft.world.level.levelgen.structure.StructureType;
/*    */ 
/*    */ public class JungleTempleStructure extends SinglePieceStructure {
/*  8 */   public static final MapCodec<JungleTempleStructure> CODEC = simpleCodec(JungleTempleStructure::new);
/*    */   
/*    */   public JungleTempleStructure(Structure.StructureSettings paramStructureSettings) {
/* 11 */     super(JungleTemplePiece::new, 12, 15, paramStructureSettings);
/*    */   }
/*    */ 
/*    */   
/*    */   public StructureType<?> type() {
/* 16 */     return StructureType.JUNGLE_TEMPLE;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\JungleTempleStructure.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */