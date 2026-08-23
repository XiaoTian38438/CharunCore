/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public class PoiTypeRenameFix extends AbstractPoiSectionFix {
/*    */   private final Function<String, String> renamer;
/*    */   
/*    */   public PoiTypeRenameFix(Schema paramSchema, String paramString, Function<String, String> paramFunction) {
/* 14 */     super(paramSchema, paramString);
/* 15 */     this.renamer = paramFunction;
/*    */   }
/*    */ 
/*    */   
/*    */   protected <T> Stream<Dynamic<T>> processRecords(Stream<Dynamic<T>> paramStream) {
/* 20 */     return paramStream.map(paramDynamic -> paramDynamic.update("type", ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\PoiTypeRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */