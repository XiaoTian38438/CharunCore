/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ 
/*    */ public class V702
/*    */   extends Schema
/*    */ {
/*    */   public V702(int paramInt, Schema paramSchema) {
/* 15 */     super(paramInt, paramSchema);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema paramSchema) {
/* 20 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(paramSchema);
/*    */     
/* 22 */     paramSchema.register(map, "ZombieVillager", paramString -> DSL.optionalFields("Offers", DSL.optionalFields("Recipes", DSL.list(References.VILLAGER_TRADE.in(paramSchema)))));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 28 */     paramSchema.registerSimple(map, "Husk");
/*    */     
/* 30 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V702.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */