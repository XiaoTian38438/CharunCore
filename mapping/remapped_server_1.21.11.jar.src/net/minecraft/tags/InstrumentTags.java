/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.item.Instrument;
/*    */ 
/*    */ public interface InstrumentTags {
/*  8 */   public static final TagKey<Instrument> REGULAR_GOAT_HORNS = create("regular_goat_horns");
/*  9 */   public static final TagKey<Instrument> SCREAMING_GOAT_HORNS = create("screaming_goat_horns");
/* 10 */   public static final TagKey<Instrument> GOAT_HORNS = create("goat_horns");
/*    */   
/*    */   private static TagKey<Instrument> create(String paramString) {
/* 13 */     return TagKey.create(Registries.INSTRUMENT, Identifier.withDefaultNamespace(paramString));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\tags\InstrumentTags.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */