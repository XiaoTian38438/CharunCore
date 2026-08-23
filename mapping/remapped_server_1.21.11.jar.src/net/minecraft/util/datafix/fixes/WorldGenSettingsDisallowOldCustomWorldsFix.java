/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Map;
/*    */ import net.minecraft.nbt.NbtFormatException;
/*    */ 
/*    */ public class WorldGenSettingsDisallowOldCustomWorldsFix extends DataFix {
/*    */   public WorldGenSettingsDisallowOldCustomWorldsFix(Schema paramSchema) {
/* 12 */     super(paramSchema, false);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 17 */     Type type = getInputSchema().getType(References.WORLD_GEN_SETTINGS);
/* 18 */     OpticFinder opticFinder = type.findField("dimensions");
/*    */     
/* 20 */     return fixTypeEverywhereTyped("WorldGenSettingsDisallowOldCustomWorldsFix_" + getOutputSchema().getVersionKey(), type, paramTyped -> paramTyped.updateTyped(paramOpticFinder, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\WorldGenSettingsDisallowOldCustomWorldsFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */