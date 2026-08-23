/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.sun.jna.Memory;
/*     */ import com.sun.jna.Native;
/*     */ import com.sun.jna.Platform;
/*     */ import com.sun.jna.Pointer;
/*     */ import com.sun.jna.platform.win32.Kernel32;
/*     */ import com.sun.jna.platform.win32.Kernel32Util;
/*     */ import com.sun.jna.platform.win32.Tlhelp32;
/*     */ import com.sun.jna.platform.win32.Version;
/*     */ import com.sun.jna.platform.win32.Win32Exception;
/*     */ import com.sun.jna.ptr.IntByReference;
/*     */ import com.sun.jna.ptr.PointerByReference;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ public class NativeModuleLister
/*     */ {
/*  29 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int LANG_MASK = 65535;
/*     */   
/*     */   private static final int DEFAULT_LANG = 1033;
/*     */   private static final int CODEPAGE_MASK = -65536;
/*     */   private static final int DEFAULT_CODEPAGE = 78643200;
/*     */   
/*     */   public static List<NativeModuleInfo> listModules() {
/*  38 */     if (!Platform.isWindows()) {
/*  39 */       return (List<NativeModuleInfo>)ImmutableList.of();
/*     */     }
/*     */     
/*  42 */     int i = Kernel32.INSTANCE.GetCurrentProcessId();
/*     */     
/*  44 */     ImmutableList.Builder builder = ImmutableList.builder();
/*     */     
/*  46 */     List list = Kernel32Util.getModules(i);
/*     */     
/*  48 */     for (Tlhelp32.MODULEENTRY32W mODULEENTRY32W : list) {
/*  49 */       String str = mODULEENTRY32W.szModule();
/*  50 */       Optional<NativeModuleVersion> optional = tryGetVersion(mODULEENTRY32W.szExePath());
/*  51 */       builder.add(new NativeModuleInfo(str, optional));
/*     */     } 
/*     */     
/*  54 */     return (List<NativeModuleInfo>)builder.build();
/*     */   }
/*     */   
/*     */   private static Optional<NativeModuleVersion> tryGetVersion(String paramString) {
/*     */     try {
/*  59 */       IntByReference intByReference1 = new IntByReference();
/*     */       
/*  61 */       int i = Version.INSTANCE.GetFileVersionInfoSize(paramString, intByReference1);
/*     */       
/*  63 */       if (i == 0) {
/*  64 */         int n = Native.getLastError();
/*  65 */         if (n == 1813 || n == 1812) {
/*  66 */           return Optional.empty();
/*     */         }
/*  68 */         throw new Win32Exception(n);
/*     */       } 
/*     */       
/*  71 */       Memory memory = new Memory(i);
/*     */       
/*  73 */       if (!Version.INSTANCE.GetFileVersionInfo(paramString, 0, i, (Pointer)memory)) {
/*  74 */         throw new Win32Exception(Native.getLastError());
/*     */       }
/*     */       
/*  77 */       IntByReference intByReference2 = new IntByReference();
/*  78 */       Pointer pointer = queryVersionValue((Pointer)memory, "\\VarFileInfo\\Translation", intByReference2);
/*  79 */       int[] arrayOfInt = pointer.getIntArray(0L, intByReference2.getValue() / 4);
/*     */       
/*  81 */       OptionalInt optionalInt = findLangAndCodepage(arrayOfInt);
/*  82 */       if (optionalInt.isEmpty()) {
/*  83 */         return Optional.empty();
/*     */       }
/*     */       
/*  86 */       int j = optionalInt.getAsInt();
/*  87 */       int k = j & 0xFFFF;
/*  88 */       int m = (j & 0xFFFF0000) >> 16;
/*  89 */       String str1 = queryVersionString((Pointer)memory, langTableKey("FileDescription", k, m), intByReference2);
/*  90 */       String str2 = queryVersionString((Pointer)memory, langTableKey("CompanyName", k, m), intByReference2);
/*  91 */       String str3 = queryVersionString((Pointer)memory, langTableKey("FileVersion", k, m), intByReference2);
/*     */       
/*  93 */       return Optional.of(new NativeModuleVersion(str1, str3, str2));
/*  94 */     } catch (Exception exception) {
/*  95 */       LOGGER.info("Failed to find module info for {}", paramString, exception);
/*     */       
/*  97 */       return Optional.empty();
/*     */     } 
/*     */   }
/*     */   private static String langTableKey(String paramString, int paramInt1, int paramInt2) {
/* 101 */     return String.format(Locale.ROOT, "\\StringFileInfo\\%04x%04x\\%s", new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), paramString });
/*     */   }
/*     */   
/*     */   private static OptionalInt findLangAndCodepage(int[] paramArrayOfint) {
/* 105 */     OptionalInt optionalInt = OptionalInt.empty();
/* 106 */     for (int i : paramArrayOfint) {
/* 107 */       if ((i & 0xFFFF0000) == 78643200 && (
/* 108 */         i & 0xFFFF) == 1033) {
/* 109 */         return OptionalInt.of(i);
/*     */       }
/*     */       
/* 112 */       optionalInt = OptionalInt.of(i);
/*     */     } 
/* 114 */     return optionalInt;
/*     */   }
/*     */   
/*     */   private static Pointer queryVersionValue(Pointer paramPointer, String paramString, IntByReference paramIntByReference) {
/* 118 */     PointerByReference pointerByReference = new PointerByReference();
/* 119 */     if (!Version.INSTANCE.VerQueryValue(paramPointer, paramString, pointerByReference, paramIntByReference)) {
/* 120 */       throw new UnsupportedOperationException("Can't get version value " + paramString);
/*     */     }
/* 122 */     return pointerByReference.getValue();
/*     */   }
/*     */   
/*     */   private static String queryVersionString(Pointer paramPointer, String paramString, IntByReference paramIntByReference) {
/*     */     try {
/* 127 */       Pointer pointer = queryVersionValue(paramPointer, paramString, paramIntByReference);
/*     */       
/* 129 */       byte[] arrayOfByte = pointer.getByteArray(0L, (paramIntByReference.getValue() - 1) * 2);
/* 130 */       return new String(arrayOfByte, StandardCharsets.UTF_16LE);
/* 131 */     } catch (Exception exception) {
/* 132 */       return "";
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void addCrashSection(CrashReportCategory paramCrashReportCategory) {
/* 137 */     paramCrashReportCategory.setDetail("Modules", () -> (String)listModules().stream().sorted(Comparator.comparing(())).map(()).collect(Collectors.joining()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class NativeModuleVersion
/*     */   {
/*     */     public final String description;
/*     */     
/*     */     public final String version;
/*     */     
/*     */     public final String company;
/*     */ 
/*     */     
/*     */     public NativeModuleVersion(String param1String1, String param1String2, String param1String3) {
/* 152 */       this.description = param1String1;
/* 153 */       this.version = param1String2;
/* 154 */       this.company = param1String3;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 159 */       return this.description + ":" + this.description + ":" + this.version;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class NativeModuleInfo {
/*     */     public final String name;
/*     */     public final Optional<NativeModuleLister.NativeModuleVersion> version;
/*     */     
/*     */     public NativeModuleInfo(String param1String, Optional<NativeModuleLister.NativeModuleVersion> param1Optional) {
/* 168 */       this.name = param1String;
/* 169 */       this.version = param1Optional;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 174 */       return this.version.<String>map(param1NativeModuleVersion -> this.name + ":" + this.name).orElse(this.name);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\NativeModuleLister.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */