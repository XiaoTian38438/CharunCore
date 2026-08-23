/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ public class StructureProcessorList {
/*    */   private final List<StructureProcessor> list;
/*    */   
/*    */   public StructureProcessorList(List<StructureProcessor> paramList) {
/*  9 */     this.list = paramList;
/*    */   }
/*    */   
/*    */   public List<StructureProcessor> list() {
/* 13 */     return this.list;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 18 */     return "ProcessorList[" + String.valueOf(this.list) + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\StructureProcessorList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */