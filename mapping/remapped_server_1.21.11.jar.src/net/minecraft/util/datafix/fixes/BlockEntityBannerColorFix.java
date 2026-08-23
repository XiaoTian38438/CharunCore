/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public class BlockEntityBannerColorFix extends NamedEntityFix {
/*    */   public BlockEntityBannerColorFix(Schema paramSchema, boolean paramBoolean) {
/* 11 */     super(paramSchema, paramBoolean, "BlockEntityBannerColorFix", References.BLOCK_ENTITY, "minecraft:banner");
/*    */   }
/*    */   
/*    */   public Dynamic<?> fixTag(Dynamic<?> paramDynamic) {
/* 15 */     paramDynamic = paramDynamic.update("Base", paramDynamic -> paramDynamic.createInt(15 - paramDynamic.asInt(0)));
/*    */     
/* 17 */     paramDynamic = paramDynamic.update("Patterns", paramDynamic -> {
/*    */           Objects.requireNonNull(paramDynamic);
/*    */           
/*    */           return (Dynamic)DataFixUtils.orElse(paramDynamic.asStreamOpt().map(()).map(paramDynamic::createList).result(), paramDynamic);
/*    */         });
/*    */     
/* 23 */     return paramDynamic;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> paramTyped) {
/* 28 */     return paramTyped.update(DSL.remainderFinder(), this::fixTag);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\BlockEntityBannerColorFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */