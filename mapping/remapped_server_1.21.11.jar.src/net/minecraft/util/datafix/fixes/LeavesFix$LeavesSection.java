/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import it.unimi.dsi.fastutil.ints.Int2IntMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.ints.IntSet;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.util.datafix.PackedBitStorage;
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
/*     */ public final class LeavesSection
/*     */   extends LeavesFix.Section
/*     */ {
/*     */   private static final String PERSISTENT = "persistent";
/*     */   private static final String DECAYABLE = "decayable";
/*     */   private static final String DISTANCE = "distance";
/*     */   private IntSet leaveIds;
/*     */   private IntSet logIds;
/*     */   private Int2IntMap stateToIdMap;
/*     */   
/*     */   public LeavesSection(Typed<?> paramTyped, Schema paramSchema) {
/* 264 */     super(paramTyped, paramSchema);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean skippable() {
/* 269 */     this.leaveIds = (IntSet)new IntOpenHashSet();
/* 270 */     this.logIds = (IntSet)new IntOpenHashSet();
/* 271 */     this.stateToIdMap = (Int2IntMap)new Int2IntOpenHashMap();
/*     */     
/* 273 */     for (byte b = 0; b < this.palette.size(); b++) {
/* 274 */       Dynamic<?> dynamic = this.palette.get(b);
/* 275 */       String str = dynamic.get("Name").asString("");
/* 276 */       if (LeavesFix.LEAVES.containsKey(str)) {
/* 277 */         boolean bool = Objects.equals(dynamic.get("Properties").get("decayable").asString(""), "false");
/* 278 */         this.leaveIds.add(b);
/* 279 */         this.stateToIdMap.put(getStateId(str, bool, 7), b);
/* 280 */         this.palette.set(b, makeLeafTag(dynamic, str, bool, 7));
/*     */       } 
/* 282 */       if (LeavesFix.LOGS.contains(str)) {
/* 283 */         this.logIds.add(b);
/*     */       }
/*     */     } 
/*     */     
/* 287 */     return (this.leaveIds.isEmpty() && this.logIds.isEmpty());
/*     */   }
/*     */   
/*     */   private Dynamic<?> makeLeafTag(Dynamic<?> paramDynamic, String paramString, boolean paramBoolean, int paramInt) {
/* 291 */     Dynamic dynamic = paramDynamic.emptyMap();
/* 292 */     dynamic = dynamic.set("persistent", dynamic.createString(paramBoolean ? "true" : "false"));
/* 293 */     dynamic = dynamic.set("distance", dynamic.createString(Integer.toString(paramInt)));
/*     */     
/* 295 */     Dynamic<?> dynamic1 = paramDynamic.emptyMap();
/* 296 */     dynamic1 = dynamic1.set("Properties", dynamic);
/* 297 */     dynamic1 = dynamic1.set("Name", dynamic1.createString(paramString));
/* 298 */     return dynamic1;
/*     */   }
/*     */   
/*     */   public boolean isLog(int paramInt) {
/* 302 */     return this.logIds.contains(paramInt);
/*     */   }
/*     */   
/*     */   public boolean isLeaf(int paramInt) {
/* 306 */     return this.leaveIds.contains(paramInt);
/*     */   }
/*     */   
/*     */   int getDistance(int paramInt) {
/* 310 */     if (isLog(paramInt)) {
/* 311 */       return 0;
/*     */     }
/* 313 */     return Integer.parseInt(((Dynamic)this.palette.get(paramInt)).get("Properties").get("distance").asString(""));
/*     */   }
/*     */   
/*     */   void setDistance(int paramInt1, int paramInt2, int paramInt3) {
/* 317 */     Dynamic<?> dynamic = this.palette.get(paramInt2);
/* 318 */     String str = dynamic.get("Name").asString("");
/* 319 */     boolean bool = Objects.equals(dynamic.get("Properties").get("persistent").asString(""), "true");
/* 320 */     int i = getStateId(str, bool, paramInt3);
/*     */     
/* 322 */     if (!this.stateToIdMap.containsKey(i)) {
/* 323 */       int k = this.palette.size();
/* 324 */       this.leaveIds.add(k);
/* 325 */       this.stateToIdMap.put(i, k);
/* 326 */       this.palette.add(makeLeafTag(dynamic, str, bool, paramInt3));
/*     */     } 
/*     */     
/* 329 */     int j = this.stateToIdMap.get(i);
/* 330 */     if (1 << this.storage.getBits() <= j) {
/* 331 */       PackedBitStorage packedBitStorage = new PackedBitStorage(this.storage.getBits() + 1, 4096);
/* 332 */       for (byte b = 0; b < 'က'; b++) {
/* 333 */         packedBitStorage.set(b, this.storage.get(b));
/*     */       }
/* 335 */       this.storage = packedBitStorage;
/*     */     } 
/* 337 */     this.storage.set(paramInt1, j);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\LeavesFix$LeavesSection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */