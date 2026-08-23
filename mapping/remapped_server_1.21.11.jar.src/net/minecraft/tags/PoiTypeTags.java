/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PoiTypeTags
/*    */ {
/* 11 */   public static final TagKey<PoiType> ACQUIRABLE_JOB_SITE = create("acquirable_job_site");
/* 12 */   public static final TagKey<PoiType> VILLAGE = create("village");
/* 13 */   public static final TagKey<PoiType> BEE_HOME = create("bee_home");
/*    */   
/*    */   private static TagKey<PoiType> create(String paramString) {
/* 16 */     return TagKey.create(Registries.POINT_OF_INTEREST_TYPE, Identifier.withDefaultNamespace(paramString));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\tags\PoiTypeTags.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */