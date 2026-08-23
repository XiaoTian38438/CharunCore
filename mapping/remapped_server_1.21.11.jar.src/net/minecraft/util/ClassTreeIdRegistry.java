/*    */ package net.minecraft.util;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*    */ 
/*    */ public class ClassTreeIdRegistry {
/*    */   public static final int NO_ID_VALUE = -1;
/*    */   
/*    */   public ClassTreeIdRegistry() {
/*  9 */     this.classToLastIdCache = (Object2IntMap<Class<?>>)Util.make(new Object2IntOpenHashMap(), paramObject2IntOpenHashMap -> paramObject2IntOpenHashMap.defaultReturnValue(-1));
/*    */   } private final Object2IntMap<Class<?>> classToLastIdCache;
/*    */   public int getLastIdFor(Class<?> paramClass) {
/* 12 */     int i = this.classToLastIdCache.getInt(paramClass);
/* 13 */     if (i != -1) {
/* 14 */       return i;
/*    */     }
/* 16 */     Class<?> clazz = paramClass;
/* 17 */     while ((clazz = clazz.getSuperclass()) != Object.class) {
/* 18 */       int j = this.classToLastIdCache.getInt(clazz);
/* 19 */       if (j != -1) {
/* 20 */         return j;
/*    */       }
/*    */     } 
/* 23 */     return -1;
/*    */   }
/*    */   
/*    */   public int getCount(Class<?> paramClass) {
/* 27 */     return getLastIdFor(paramClass) + 1;
/*    */   }
/*    */   
/*    */   public int define(Class<?> paramClass) {
/* 31 */     int i = getLastIdFor(paramClass);
/* 32 */     boolean bool = (i == -1) ? false : (i + 1);
/* 33 */     this.classToLastIdCache.put(paramClass, bool);
/* 34 */     return bool;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\ClassTreeIdRegistry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */