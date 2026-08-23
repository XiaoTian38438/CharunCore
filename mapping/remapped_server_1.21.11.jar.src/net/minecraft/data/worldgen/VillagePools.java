/*    */ package net.minecraft.data.worldgen;
/*    */ 
/*    */ import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
/*    */ 
/*    */ public class VillagePools {
/*    */   public static void bootstrap(BootstrapContext<StructureTemplatePool> paramBootstrapContext) {
/*  7 */     PlainVillagePools.bootstrap(paramBootstrapContext);
/*  8 */     SnowyVillagePools.bootstrap(paramBootstrapContext);
/*  9 */     SavannaVillagePools.bootstrap(paramBootstrapContext);
/* 10 */     DesertVillagePools.bootstrap(paramBootstrapContext);
/* 11 */     TaigaVillagePools.bootstrap(paramBootstrapContext);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\worldgen\VillagePools.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */