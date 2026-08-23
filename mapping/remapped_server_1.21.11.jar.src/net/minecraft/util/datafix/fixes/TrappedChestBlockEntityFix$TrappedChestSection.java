/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.ints.IntSet;
/*     */ import java.util.Objects;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class TrappedChestSection
/*     */   extends LeavesFix.Section
/*     */ {
/*     */   private IntSet chestIds;
/*     */   
/*     */   public TrappedChestSection(Typed<?> paramTyped, Schema paramSchema) {
/* 116 */     super(paramTyped, paramSchema);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean skippable() {
/* 121 */     this.chestIds = (IntSet)new IntOpenHashSet();
/*     */     
/* 123 */     for (byte b = 0; b < this.palette.size(); b++) {
/* 124 */       Dynamic dynamic = this.palette.get(b);
/* 125 */       String str = dynamic.get("Name").asString("");
/* 126 */       if (Objects.equals(str, "minecraft:trapped_chest")) {
/* 127 */         this.chestIds.add(b);
/*     */       }
/*     */     } 
/*     */     
/* 131 */     return this.chestIds.isEmpty();
/*     */   }
/*     */   
/*     */   public boolean isTrappedChest(int paramInt) {
/* 135 */     return this.chestIds.contains(paramInt);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\TrappedChestBlockEntityFix$TrappedChestSection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */