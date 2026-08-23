/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class JukeboxTicksSinceSongStartedFix extends NamedEntityFix {
/*    */   public JukeboxTicksSinceSongStartedFix(Schema paramSchema) {
/* 10 */     super(paramSchema, false, "JukeboxTicksSinceSongStartedFix", References.BLOCK_ENTITY, "minecraft:jukebox");
/*    */   }
/*    */   
/*    */   public Dynamic<?> fixTag(Dynamic<?> paramDynamic) {
/* 14 */     long l = paramDynamic.get("TickCount").asLong(0L) - paramDynamic.get("RecordStartTick").asLong(0L);
/* 15 */     Dynamic<?> dynamic = paramDynamic.remove("IsPlaying").remove("TickCount").remove("RecordStartTick");
/* 16 */     if (l > 0L) {
/* 17 */       return dynamic.set("ticks_since_song_started", paramDynamic.createLong(l));
/*    */     }
/* 19 */     return dynamic;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> paramTyped) {
/* 24 */     return paramTyped.update(DSL.remainderFinder(), this::fixTag);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\JukeboxTicksSinceSongStartedFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */