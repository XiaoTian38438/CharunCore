/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.common.base.Predicates;
/*     */ import com.google.common.collect.Iterators;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import net.minecraft.core.IdMap;
/*     */ 
/*     */ public class CrudeIncrementalIntIdentityHashBiMap<K>
/*     */   implements IdMap<K>
/*     */ {
/*     */   private static final int NOT_FOUND = -1;
/*  13 */   private static final Object EMPTY_SLOT = null;
/*     */   
/*     */   private static final float LOADFACTOR = 0.8F;
/*     */   
/*     */   private K[] keys;
/*     */   
/*     */   private int[] values;
/*     */   private K[] byId;
/*     */   private int nextId;
/*     */   private int size;
/*     */   
/*     */   private CrudeIncrementalIntIdentityHashBiMap(int paramInt) {
/*  25 */     this.keys = (K[])new Object[paramInt];
/*  26 */     this.values = new int[paramInt];
/*  27 */     this.byId = (K[])new Object[paramInt];
/*     */   }
/*     */   
/*     */   private CrudeIncrementalIntIdentityHashBiMap(K[] paramArrayOfK1, int[] paramArrayOfint, K[] paramArrayOfK2, int paramInt1, int paramInt2) {
/*  31 */     this.keys = paramArrayOfK1;
/*  32 */     this.values = paramArrayOfint;
/*  33 */     this.byId = paramArrayOfK2;
/*  34 */     this.nextId = paramInt1;
/*  35 */     this.size = paramInt2;
/*     */   }
/*     */   
/*     */   public static <A> CrudeIncrementalIntIdentityHashBiMap<A> create(int paramInt) {
/*  39 */     return new CrudeIncrementalIntIdentityHashBiMap<>((int)(paramInt / 0.8F));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getId(K paramK) {
/*  44 */     return getValue(indexOf(paramK, hash(paramK)));
/*     */   }
/*     */ 
/*     */   
/*     */   public K byId(int paramInt) {
/*  49 */     if (paramInt < 0 || paramInt >= this.byId.length) {
/*  50 */       return null;
/*     */     }
/*     */     
/*  53 */     return this.byId[paramInt];
/*     */   }
/*     */   
/*     */   private int getValue(int paramInt) {
/*  57 */     if (paramInt == -1) {
/*  58 */       return -1;
/*     */     }
/*  60 */     return this.values[paramInt];
/*     */   }
/*     */   
/*     */   public boolean contains(K paramK) {
/*  64 */     return (getId(paramK) != -1);
/*     */   }
/*     */   
/*     */   public boolean contains(int paramInt) {
/*  68 */     return (byId(paramInt) != null);
/*     */   }
/*     */   
/*     */   public int add(K paramK) {
/*  72 */     int i = nextId();
/*     */     
/*  74 */     addMapping(paramK, i);
/*     */     
/*  76 */     return i;
/*     */   }
/*     */   
/*     */   private int nextId() {
/*  80 */     while (this.nextId < this.byId.length && this.byId[this.nextId] != null) {
/*  81 */       this.nextId++;
/*     */     }
/*  83 */     return this.nextId;
/*     */   }
/*     */ 
/*     */   
/*     */   private void grow(int paramInt) {
/*  88 */     K[] arrayOfK = this.keys;
/*  89 */     int[] arrayOfInt = this.values;
/*     */     
/*  91 */     CrudeIncrementalIntIdentityHashBiMap<K> crudeIncrementalIntIdentityHashBiMap = new CrudeIncrementalIntIdentityHashBiMap(paramInt);
/*  92 */     for (byte b = 0; b < arrayOfK.length; b++) {
/*  93 */       if (arrayOfK[b] != null) {
/*  94 */         crudeIncrementalIntIdentityHashBiMap.addMapping(arrayOfK[b], arrayOfInt[b]);
/*     */       }
/*     */     } 
/*     */     
/*  98 */     this.keys = crudeIncrementalIntIdentityHashBiMap.keys;
/*  99 */     this.values = crudeIncrementalIntIdentityHashBiMap.values;
/* 100 */     this.byId = crudeIncrementalIntIdentityHashBiMap.byId;
/* 101 */     this.nextId = crudeIncrementalIntIdentityHashBiMap.nextId;
/* 102 */     this.size = crudeIncrementalIntIdentityHashBiMap.size;
/*     */   }
/*     */   
/*     */   public void addMapping(K paramK, int paramInt) {
/* 106 */     int i = Math.max(paramInt, this.size + 1);
/* 107 */     if (i >= this.keys.length * 0.8F) {
/* 108 */       int k = this.keys.length << 1;
/* 109 */       while (k < paramInt) {
/* 110 */         k <<= 1;
/*     */       }
/* 112 */       grow(k);
/*     */     } 
/*     */     
/* 115 */     int j = findEmpty(hash(paramK));
/* 116 */     this.keys[j] = paramK;
/* 117 */     this.values[j] = paramInt;
/* 118 */     this.byId[paramInt] = paramK;
/* 119 */     this.size++;
/*     */     
/* 121 */     if (paramInt == this.nextId) {
/* 122 */       this.nextId++;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int hash(K paramK) {
/* 138 */     return (Mth.murmurHash3Mixer(System.identityHashCode(paramK)) & Integer.MAX_VALUE) % this.keys.length;
/*     */   }
/*     */   private int indexOf(K paramK, int paramInt) {
/*     */     int i;
/* 142 */     for (i = paramInt; i < this.keys.length; i++) {
/* 143 */       if (this.keys[i] == paramK) {
/* 144 */         return i;
/*     */       }
/* 146 */       if (this.keys[i] == EMPTY_SLOT) {
/* 147 */         return -1;
/*     */       }
/*     */     } 
/*     */     
/* 151 */     for (i = 0; i < paramInt; i++) {
/* 152 */       if (this.keys[i] == paramK) {
/* 153 */         return i;
/*     */       }
/* 155 */       if (this.keys[i] == EMPTY_SLOT) {
/* 156 */         return -1;
/*     */       }
/*     */     } 
/*     */     
/* 160 */     return -1;
/*     */   }
/*     */   private int findEmpty(int paramInt) {
/*     */     int i;
/* 164 */     for (i = paramInt; i < this.keys.length; i++) {
/* 165 */       if (this.keys[i] == EMPTY_SLOT) {
/* 166 */         return i;
/*     */       }
/*     */     } 
/*     */     
/* 170 */     for (i = 0; i < paramInt; i++) {
/* 171 */       if (this.keys[i] == EMPTY_SLOT) {
/* 172 */         return i;
/*     */       }
/*     */     } 
/*     */     
/* 176 */     throw new RuntimeException("Overflowed :(");
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<K> iterator() {
/* 181 */     return (Iterator<K>)Iterators.filter((Iterator)Iterators.forArray((Object[])this.byId), Predicates.notNull());
/*     */   }
/*     */   
/*     */   public void clear() {
/* 185 */     Arrays.fill((Object[])this.keys, (Object)null);
/* 186 */     Arrays.fill((Object[])this.byId, (Object)null);
/* 187 */     this.nextId = 0;
/* 188 */     this.size = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 193 */     return this.size;
/*     */   }
/*     */   
/*     */   public CrudeIncrementalIntIdentityHashBiMap<K> copy() {
/* 197 */     return new CrudeIncrementalIntIdentityHashBiMap((K[])this.keys
/* 198 */         .clone(), (int[])this.values
/* 199 */         .clone(), (K[])this.byId
/* 200 */         .clone(), this.nextId, this.size);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\CrudeIncrementalIntIdentityHashBiMap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */