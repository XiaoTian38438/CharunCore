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
/*    */ public class V703
/*    */   extends Schema
/*    */ {
/*    */   public V703(int paramInt, Schema paramSchema) {
/* 15 */     super(paramInt, paramSchema);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema paramSchema) {
/* 20 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(paramSchema);
/*    */     
/* 22 */     map.remove("EntityHorse");
/* 23 */     paramSchema.register(map, "Horse", () -> DSL.optionalFields("ArmorItem", References.ITEM_STACK.in(paramSchema), "SaddleItem", References.ITEM_STACK.in(paramSchema)));
/*    */ 
/*    */ 
/*    */     
/* 27 */     paramSchema.register(map, "Donkey", () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(paramSchema)), "SaddleItem", References.ITEM_STACK.in(paramSchema)));
/*    */ 
/*    */ 
/*    */     
/* 31 */     paramSchema.register(map, "Mule", () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(paramSchema)), "SaddleItem", References.ITEM_STACK.in(paramSchema)));
/*    */ 
/*    */ 
/*    */     
/* 35 */     paramSchema.register(map, "ZombieHorse", () -> DSL.optionalFields("SaddleItem", References.ITEM_STACK.in(paramSchema)));
/*    */ 
/*    */     
/* 38 */     paramSchema.register(map, "SkeletonHorse", () -> DSL.optionalFields("SaddleItem", References.ITEM_STACK.in(paramSchema)));
/*    */ 
/*    */ 
/*    */     
/* 42 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V703.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */