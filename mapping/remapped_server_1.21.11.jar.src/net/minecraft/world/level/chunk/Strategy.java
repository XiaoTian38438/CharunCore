/*    */ package net.minecraft.world.level.chunk;
/*    */ 
/*    */ import net.minecraft.core.IdMap;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public abstract class Strategy<T>
/*    */ {
/*  8 */   private static final Palette.Factory SINGLE_VALUE_PALETTE_FACTORY = SingleValuePalette::create;
/*  9 */   private static final Palette.Factory LINEAR_PALETTE_FACTORY = LinearPalette::create;
/* 10 */   private static final Palette.Factory HASHMAP_PALETTE_FACTORY = HashMapPalette::create;
/*    */   
/* 12 */   static final Configuration ZERO_BITS = new Configuration.Simple(SINGLE_VALUE_PALETTE_FACTORY, 0);
/*    */   
/* 14 */   static final Configuration ONE_BIT_LINEAR = new Configuration.Simple(LINEAR_PALETTE_FACTORY, 1);
/* 15 */   static final Configuration TWO_BITS_LINEAR = new Configuration.Simple(LINEAR_PALETTE_FACTORY, 2);
/* 16 */   static final Configuration THREE_BITS_LINEAR = new Configuration.Simple(LINEAR_PALETTE_FACTORY, 3);
/* 17 */   static final Configuration FOUR_BITS_LINEAR = new Configuration.Simple(LINEAR_PALETTE_FACTORY, 4);
/*    */   
/* 19 */   static final Configuration FIVE_BITS_HASHMAP = new Configuration.Simple(HASHMAP_PALETTE_FACTORY, 5);
/* 20 */   static final Configuration SIX_BITS_HASHMAP = new Configuration.Simple(HASHMAP_PALETTE_FACTORY, 6);
/* 21 */   static final Configuration SEVEN_BITS_HASHMAP = new Configuration.Simple(HASHMAP_PALETTE_FACTORY, 7);
/* 22 */   static final Configuration EIGHT_BITS_HASHMAP = new Configuration.Simple(HASHMAP_PALETTE_FACTORY, 8);
/*    */   
/*    */   private final IdMap<T> globalMap;
/*    */   private final GlobalPalette<T> globalPalette;
/*    */   protected final int globalPaletteBitsInMemory;
/*    */   private final int bitsPerAxis;
/*    */   private final int entryCount;
/*    */   
/*    */   Strategy(IdMap<T> paramIdMap, int paramInt) {
/* 31 */     this.globalMap = paramIdMap;
/* 32 */     this.globalPalette = new GlobalPalette<>(paramIdMap);
/* 33 */     this.globalPaletteBitsInMemory = minimumBitsRequiredForDistinctValues(paramIdMap.size());
/* 34 */     this.bitsPerAxis = paramInt;
/* 35 */     this.entryCount = 1 << paramInt * 3;
/*    */   }
/*    */   
/*    */   public static <T> Strategy<T> createForBlockStates(IdMap<T> paramIdMap) {
/* 39 */     return new Strategy<T>(paramIdMap, 4)
/*    */       {
/*    */         public Configuration getConfigurationForBitCount(int param1Int) {
/* 42 */           switch (param1Int) { case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8:  }  return 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */             
/* 50 */             new Configuration.Global(this.globalPaletteBitsInMemory, param1Int);
/*    */         }
/*    */       };
/*    */   }
/*    */ 
/*    */   
/*    */   public static <T> Strategy<T> createForBiomes(IdMap<T> paramIdMap) {
/* 57 */     return new Strategy<T>(paramIdMap, 2)
/*    */       {
/*    */         public Configuration getConfigurationForBitCount(int param1Int) {
/* 60 */           switch (param1Int) { case 0: case 1: case 2: case 3:  }  return 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */             
/* 66 */             new Configuration.Global(this.globalPaletteBitsInMemory, param1Int);
/*    */         }
/*    */       };
/*    */   }
/*    */ 
/*    */   
/*    */   public int entryCount() {
/* 73 */     return this.entryCount;
/*    */   }
/*    */   
/*    */   public int getIndex(int paramInt1, int paramInt2, int paramInt3) {
/* 77 */     return (paramInt2 << this.bitsPerAxis | paramInt3) << this.bitsPerAxis | paramInt1;
/*    */   }
/*    */   
/*    */   public IdMap<T> globalMap() {
/* 81 */     return this.globalMap;
/*    */   }
/*    */   
/*    */   public GlobalPalette<T> globalPalette() {
/* 85 */     return this.globalPalette;
/*    */   }
/*    */   
/*    */   protected abstract Configuration getConfigurationForBitCount(int paramInt);
/*    */   
/*    */   protected Configuration getConfigurationForPaletteSize(int paramInt) {
/* 91 */     int i = minimumBitsRequiredForDistinctValues(paramInt);
/* 92 */     return getConfigurationForBitCount(i);
/*    */   }
/*    */   
/*    */   private static int minimumBitsRequiredForDistinctValues(int paramInt) {
/* 96 */     return Mth.ceillog2(paramInt);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\chunk\Strategy.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */