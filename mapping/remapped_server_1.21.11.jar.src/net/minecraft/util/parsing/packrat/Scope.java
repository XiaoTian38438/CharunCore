/*     */ package net.minecraft.util.parsing.packrat;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Scope
/*     */ {
/*     */   private static final int NOT_FOUND = -1;
/*     */   
/*  18 */   private static final Object FRAME_START_MARKER = new Object()
/*     */     {
/*     */       public String toString() {
/*  21 */         return "frame";
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   private static final int ENTRY_STRIDE = 2;
/*  27 */   private Object[] stack = new Object[128];
/*  28 */   private int topEntryKeyIndex = 0;
/*  29 */   private int topMarkerKeyIndex = 0;
/*     */ 
/*     */   
/*     */   public Scope() {
/*  33 */     this.stack[0] = FRAME_START_MARKER;
/*     */     
/*  35 */     this.stack[1] = null;
/*     */   }
/*     */   
/*     */   private int valueIndex(Atom<?> paramAtom) {
/*  39 */     for (int i = this.topEntryKeyIndex; i > this.topMarkerKeyIndex; i -= 2) {
/*  40 */       Object object = this.stack[i];
/*  41 */       assert object instanceof Atom;
/*  42 */       if (object == paramAtom) {
/*  43 */         return i + 1;
/*     */       }
/*     */     } 
/*  46 */     return -1;
/*     */   }
/*     */   
/*     */   public int valueIndexForAny(Atom<?>... paramVarArgs) {
/*  50 */     for (int i = this.topEntryKeyIndex; i > this.topMarkerKeyIndex; i -= 2) {
/*  51 */       Object object = this.stack[i];
/*  52 */       assert object instanceof Atom;
/*  53 */       for (Atom<?> atom : paramVarArgs) {
/*  54 */         if (atom == object) {
/*  55 */           return i + 1;
/*     */         }
/*     */       } 
/*     */     } 
/*  59 */     return -1;
/*     */   }
/*     */   
/*     */   private void ensureCapacity(int paramInt) {
/*  63 */     int i = this.stack.length;
/*  64 */     int j = this.topEntryKeyIndex + 1;
/*  65 */     int k = j + paramInt * 2;
/*     */     
/*  67 */     if (k >= i) {
/*  68 */       int m = Util.growByHalf(i, k + 1);
/*  69 */       Object[] arrayOfObject = new Object[m];
/*  70 */       System.arraycopy(this.stack, 0, arrayOfObject, 0, i);
/*  71 */       this.stack = arrayOfObject;
/*     */     } 
/*  73 */     assert validateStructure();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setupNewFrame() {
/*  82 */     this.topEntryKeyIndex += 2;
/*  83 */     this.stack[this.topEntryKeyIndex] = FRAME_START_MARKER;
/*  84 */     this.stack[this.topEntryKeyIndex + 1] = Integer.valueOf(this.topMarkerKeyIndex);
/*  85 */     this.topMarkerKeyIndex = this.topEntryKeyIndex;
/*     */   }
/*     */   
/*     */   public void pushFrame() {
/*  89 */     ensureCapacity(1);
/*  90 */     setupNewFrame();
/*  91 */     assert validateStructure();
/*     */   }
/*     */   
/*     */   private int getPreviousMarkerIndex(int paramInt) {
/*  95 */     return ((Integer)this.stack[paramInt + 1]).intValue();
/*     */   }
/*     */   
/*     */   public void popFrame() {
/*  99 */     assert this.topMarkerKeyIndex != 0;
/*     */ 
/*     */ 
/*     */     
/* 103 */     this.topEntryKeyIndex = this.topMarkerKeyIndex - 2;
/* 104 */     this.topMarkerKeyIndex = getPreviousMarkerIndex(this.topMarkerKeyIndex);
/* 105 */     assert validateStructure();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void splitFrame() {
/* 113 */     int i = this.topMarkerKeyIndex;
/* 114 */     int j = (this.topEntryKeyIndex - this.topMarkerKeyIndex) / 2;
/* 115 */     ensureCapacity(j + 1);
/*     */     
/* 117 */     setupNewFrame();
/*     */     
/* 119 */     int k = i + 2;
/* 120 */     int m = this.topEntryKeyIndex;
/* 121 */     for (byte b = 0; b < j; b++) {
/* 122 */       m += 2;
/* 123 */       Object object = this.stack[k];
/* 124 */       assert object != null;
/* 125 */       this.stack[m] = object;
/* 126 */       this.stack[m + 1] = null;
/* 127 */       k += 2;
/*     */     } 
/* 129 */     this.topEntryKeyIndex = m;
/* 130 */     assert validateStructure();
/*     */   }
/*     */   
/*     */   public void clearFrameValues() {
/* 134 */     for (int i = this.topEntryKeyIndex; i > this.topMarkerKeyIndex; i -= 2) {
/* 135 */       assert this.stack[i] instanceof Atom;
/* 136 */       this.stack[i + 1] = null;
/*     */     } 
/* 138 */     assert validateStructure();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void mergeFrame() {
/* 146 */     int i = getPreviousMarkerIndex(this.topMarkerKeyIndex);
/* 147 */     int j = i;
/* 148 */     int k = this.topMarkerKeyIndex;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 154 */     while (k < this.topEntryKeyIndex) {
/* 155 */       j += 2;
/* 156 */       k += 2;
/* 157 */       Object object1 = this.stack[k];
/* 158 */       assert object1 instanceof Atom;
/* 159 */       Object object2 = this.stack[k + 1];
/*     */       
/* 161 */       Object object3 = this.stack[j];
/* 162 */       if (object3 != object1) {
/*     */         
/* 164 */         this.stack[j] = object1;
/* 165 */         this.stack[j + 1] = object2; continue;
/* 166 */       }  if (object2 != null) {
/* 167 */         this.stack[j + 1] = object2;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 172 */     this.topEntryKeyIndex = j;
/* 173 */     this.topMarkerKeyIndex = i;
/* 174 */     assert validateStructure();
/*     */   }
/*     */   
/*     */   public <T> void put(Atom<T> paramAtom, T paramT) {
/* 178 */     int i = valueIndex(paramAtom);
/* 179 */     if (i != -1) {
/* 180 */       this.stack[i] = paramT;
/*     */     } else {
/* 182 */       ensureCapacity(1);
/* 183 */       this.topEntryKeyIndex += 2;
/* 184 */       this.stack[this.topEntryKeyIndex] = paramAtom;
/* 185 */       this.stack[this.topEntryKeyIndex + 1] = paramT;
/*     */     } 
/* 187 */     assert validateStructure();
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T get(Atom<T> paramAtom) {
/* 192 */     int i = valueIndex(paramAtom);
/* 193 */     return (i != -1) ? (T)this.stack[i] : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T getOrThrow(Atom<T> paramAtom) {
/* 198 */     int i = valueIndex(paramAtom);
/* 199 */     if (i == -1) {
/* 200 */       throw new IllegalArgumentException("No value for atom " + String.valueOf(paramAtom));
/*     */     }
/* 202 */     return (T)this.stack[i];
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T getOrDefault(Atom<T> paramAtom, T paramT) {
/* 207 */     int i = valueIndex(paramAtom);
/* 208 */     return (i != -1) ? (T)this.stack[i] : paramT;
/*     */   }
/*     */ 
/*     */   
/*     */   @SafeVarargs
/*     */   public final <T> T getAny(Atom<? extends T>... paramVarArgs) {
/* 214 */     int i = valueIndexForAny((Atom<?>[])paramVarArgs);
/* 215 */     return (i != -1) ? (T)this.stack[i] : null;
/*     */   }
/*     */ 
/*     */   
/*     */   @SafeVarargs
/*     */   public final <T> T getAnyOrThrow(Atom<? extends T>... paramVarArgs) {
/* 221 */     int i = valueIndexForAny((Atom<?>[])paramVarArgs);
/* 222 */     if (i == -1) {
/* 223 */       throw new IllegalArgumentException("No value for atoms " + Arrays.toString(paramVarArgs));
/*     */     }
/* 225 */     return (T)this.stack[i];
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 230 */     StringBuilder stringBuilder = new StringBuilder();
/* 231 */     boolean bool = true;
/* 232 */     for (byte b = 0; b <= this.topEntryKeyIndex; b += 2) {
/* 233 */       Object object1 = this.stack[b];
/* 234 */       Object object2 = this.stack[b + 1];
/* 235 */       if (object1 == FRAME_START_MARKER) {
/* 236 */         stringBuilder.append('|');
/* 237 */         bool = true;
/*     */       } else {
/* 239 */         if (!bool) {
/* 240 */           stringBuilder.append(',');
/*     */         }
/* 242 */         bool = false;
/* 243 */         stringBuilder.append(object1).append(':').append(object2);
/*     */       } 
/*     */     } 
/* 246 */     return stringBuilder.toString();
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public Map<Atom<?>, ?> lastFrame() {
/* 251 */     HashMap<Object, Object> hashMap = new HashMap<>();
/*     */ 
/*     */     
/* 254 */     for (int i = this.topEntryKeyIndex; i > this.topMarkerKeyIndex; i -= 2) {
/* 255 */       Object object1 = this.stack[i];
/* 256 */       Object object2 = this.stack[i + 1];
/* 257 */       hashMap.put(object1, object2);
/*     */     } 
/* 259 */     return (Map)hashMap;
/*     */   }
/*     */   
/*     */   public boolean hasOnlySingleFrame() {
/* 263 */     for (int i = this.topEntryKeyIndex; i > 0; i--) {
/* 264 */       if (this.stack[i] == FRAME_START_MARKER) {
/* 265 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 269 */     if (this.stack[0] != FRAME_START_MARKER) {
/* 270 */       throw new IllegalStateException("Corrupted stack");
/*     */     }
/* 272 */     return true;
/*     */   }
/*     */   
/*     */   private boolean validateStructure() {
/* 276 */     assert this.topMarkerKeyIndex >= 0;
/* 277 */     assert this.topEntryKeyIndex >= this.topMarkerKeyIndex;
/*     */     int i;
/* 279 */     for (i = 0; i <= this.topEntryKeyIndex; i += 2) {
/* 280 */       Object object = this.stack[i];
/* 281 */       if (object != FRAME_START_MARKER && !(object instanceof Atom)) {
/* 282 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 286 */     i = this.topMarkerKeyIndex;
/* 287 */     while (i != 0) {
/* 288 */       Object object = this.stack[i];
/* 289 */       if (object != FRAME_START_MARKER) {
/* 290 */         return false;
/*     */       }
/* 292 */       i = getPreviousMarkerIndex(i);
/*     */     } 
/*     */     
/* 295 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\parsing\packrat\Scope.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */