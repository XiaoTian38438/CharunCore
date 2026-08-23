/*     */ package net.minecraft;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.nio.file.FileStore;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.InvalidPathException;
/*     */ import java.nio.file.Path;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import org.slf4j.Logger;
/*     */ import oshi.SystemInfo;
/*     */ import oshi.hardware.CentralProcessor;
/*     */ import oshi.hardware.GlobalMemory;
/*     */ import oshi.hardware.GraphicsCard;
/*     */ import oshi.hardware.HardwareAbstractionLayer;
/*     */ import oshi.hardware.PhysicalMemory;
/*     */ import oshi.hardware.VirtualMemory;
/*     */ 
/*     */ public class SystemReport
/*     */ {
/*     */   public static final long BYTES_PER_MEBIBYTE = 1048576L;
/*     */   private static final long ONE_GIGA = 1000000000L;
/*  30 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  32 */   private static final String OPERATING_SYSTEM = System.getProperty("os.name") + " (" + System.getProperty("os.name") + ") version " + System.getProperty("os.arch");
/*  33 */   private static final String JAVA_VERSION = System.getProperty("java.version") + ", " + System.getProperty("java.version");
/*  34 */   private static final String JAVA_VM_VERSION = System.getProperty("java.vm.name") + " (" + System.getProperty("java.vm.name") + "), " + System.getProperty("java.vm.info");
/*     */   
/*  36 */   private final Map<String, String> entries = Maps.newLinkedHashMap();
/*     */   
/*     */   public SystemReport() {
/*  39 */     setDetail("Minecraft Version", SharedConstants.getCurrentVersion().name());
/*  40 */     setDetail("Minecraft Version ID", SharedConstants.getCurrentVersion().id());
/*  41 */     setDetail("Operating System", OPERATING_SYSTEM);
/*  42 */     setDetail("Java Version", JAVA_VERSION);
/*  43 */     setDetail("Java VM Version", JAVA_VM_VERSION);
/*     */     
/*  45 */     setDetail("Memory", () -> {
/*     */           Runtime runtime = Runtime.getRuntime();
/*     */           
/*     */           long l1 = runtime.maxMemory();
/*     */           
/*     */           long l2 = runtime.totalMemory();
/*     */           long l3 = runtime.freeMemory();
/*     */           long l4 = l1 / 1048576L;
/*     */           long l5 = l2 / 1048576L;
/*     */           long l6 = l3 / 1048576L;
/*     */           return "" + l3 + " bytes (" + l3 + " MiB) / " + l6 + " bytes (" + l2 + " MiB) up to " + l5 + " bytes (" + l1 + " MiB)";
/*     */         });
/*  57 */     setDetail("CPUs", () -> String.valueOf(Runtime.getRuntime().availableProcessors()));
/*     */     
/*  59 */     ignoreErrors("hardware", () -> putHardware(new SystemInfo()));
/*     */     
/*  61 */     setDetail("JVM Flags", () -> printJvmFlags(()));
/*     */     
/*  63 */     setDetail("Debug Flags", () -> printJvmFlags(()));
/*     */   }
/*     */   
/*     */   private static String printJvmFlags(Predicate<String> paramPredicate) {
/*  67 */     List<String> list = ManagementFactory.getRuntimeMXBean().getInputArguments();
/*  68 */     List<? extends CharSequence> list1 = list.stream().filter(paramPredicate).toList();
/*  69 */     return String.format(Locale.ROOT, "%d total; %s", new Object[] { Integer.valueOf(list1.size()), String.join(" ", list1) });
/*     */   }
/*     */   
/*     */   public void setDetail(String paramString1, String paramString2) {
/*  73 */     this.entries.put(paramString1, paramString2);
/*     */   }
/*     */   
/*     */   public void setDetail(String paramString, Supplier<String> paramSupplier) {
/*     */     try {
/*  78 */       setDetail(paramString, paramSupplier.get());
/*  79 */     } catch (Exception exception) {
/*  80 */       LOGGER.warn("Failed to get system info for {}", paramString, exception);
/*  81 */       setDetail(paramString, "ERR");
/*     */     } 
/*     */   }
/*     */   
/*     */   private void putHardware(SystemInfo paramSystemInfo) {
/*  86 */     HardwareAbstractionLayer hardwareAbstractionLayer = paramSystemInfo.getHardware();
/*  87 */     ignoreErrors("processor", () -> putProcessor(paramHardwareAbstractionLayer.getProcessor()));
/*  88 */     ignoreErrors("graphics", () -> putGraphics(paramHardwareAbstractionLayer.getGraphicsCards()));
/*  89 */     ignoreErrors("memory", () -> putMemory(paramHardwareAbstractionLayer.getMemory()));
/*  90 */     ignoreErrors("storage", this::putStorage);
/*     */   }
/*     */   
/*     */   private void ignoreErrors(String paramString, Runnable paramRunnable) {
/*     */     try {
/*  95 */       paramRunnable.run();
/*  96 */     } catch (Throwable throwable) {
/*  97 */       LOGGER.warn("Failed retrieving info for group {}", paramString, throwable);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static float sizeInMiB(long paramLong) {
/* 102 */     return (float)paramLong / 1048576.0F;
/*     */   }
/*     */   
/*     */   private void putPhysicalMemory(List<PhysicalMemory> paramList) {
/* 106 */     byte b = 0;
/* 107 */     for (PhysicalMemory physicalMemory : paramList) {
/* 108 */       String str = String.format(Locale.ROOT, "Memory slot #%d ", new Object[] { Integer.valueOf(b++) });
/* 109 */       setDetail(str + "capacity (MiB)", () -> String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf(sizeInMiB(paramPhysicalMemory.getCapacity())) }));
/* 110 */       setDetail(str + "clockSpeed (GHz)", () -> String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf((float)paramPhysicalMemory.getClockSpeed() / 1.0E9F) }));
/* 111 */       Objects.requireNonNull(physicalMemory); setDetail(str + "type", physicalMemory::getMemoryType);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void putVirtualMemory(VirtualMemory paramVirtualMemory) {
/* 116 */     setDetail("Virtual memory max (MiB)", () -> String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf(sizeInMiB(paramVirtualMemory.getVirtualMax())) }));
/* 117 */     setDetail("Virtual memory used (MiB)", () -> String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf(sizeInMiB(paramVirtualMemory.getVirtualInUse())) }));
/* 118 */     setDetail("Swap memory total (MiB)", () -> String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf(sizeInMiB(paramVirtualMemory.getSwapTotal())) }));
/* 119 */     setDetail("Swap memory used (MiB)", () -> String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf(sizeInMiB(paramVirtualMemory.getSwapUsed())) }));
/*     */   }
/*     */   
/*     */   private void putMemory(GlobalMemory paramGlobalMemory) {
/* 123 */     ignoreErrors("physical memory", () -> putPhysicalMemory(paramGlobalMemory.getPhysicalMemory()));
/* 124 */     ignoreErrors("virtual memory", () -> putVirtualMemory(paramGlobalMemory.getVirtualMemory()));
/*     */   }
/*     */   
/*     */   private void putGraphics(List<GraphicsCard> paramList) {
/* 128 */     byte b = 0;
/* 129 */     for (GraphicsCard graphicsCard : paramList) {
/* 130 */       String str = String.format(Locale.ROOT, "Graphics card #%d ", new Object[] { Integer.valueOf(b++) });
/* 131 */       Objects.requireNonNull(graphicsCard); setDetail(str + "name", graphicsCard::getName);
/* 132 */       Objects.requireNonNull(graphicsCard); setDetail(str + "vendor", graphicsCard::getVendor);
/* 133 */       setDetail(str + "VRAM (MiB)", () -> String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf(sizeInMiB(paramGraphicsCard.getVRam())) }));
/* 134 */       Objects.requireNonNull(graphicsCard); setDetail(str + "deviceId", graphicsCard::getDeviceId);
/* 135 */       Objects.requireNonNull(graphicsCard); setDetail(str + "versionInfo", graphicsCard::getVersionInfo);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void putProcessor(CentralProcessor paramCentralProcessor) {
/* 140 */     CentralProcessor.ProcessorIdentifier processorIdentifier = paramCentralProcessor.getProcessorIdentifier();
/*     */     
/* 142 */     Objects.requireNonNull(processorIdentifier); setDetail("Processor Vendor", processorIdentifier::getVendor);
/* 143 */     Objects.requireNonNull(processorIdentifier); setDetail("Processor Name", processorIdentifier::getName);
/* 144 */     Objects.requireNonNull(processorIdentifier); setDetail("Identifier", processorIdentifier::getIdentifier);
/* 145 */     Objects.requireNonNull(processorIdentifier); setDetail("Microarchitecture", processorIdentifier::getMicroarchitecture);
/* 146 */     setDetail("Frequency (GHz)", () -> String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf((float)paramProcessorIdentifier.getVendorFreq() / 1.0E9F) }));
/*     */     
/* 148 */     setDetail("Number of physical packages", () -> String.valueOf(paramCentralProcessor.getPhysicalPackageCount()));
/* 149 */     setDetail("Number of physical CPUs", () -> String.valueOf(paramCentralProcessor.getPhysicalProcessorCount()));
/* 150 */     setDetail("Number of logical CPUs", () -> String.valueOf(paramCentralProcessor.getLogicalProcessorCount()));
/*     */   }
/*     */   
/*     */   private void putStorage() {
/* 154 */     putSpaceForProperty("jna.tmpdir");
/* 155 */     putSpaceForProperty("org.lwjgl.system.SharedLibraryExtractPath");
/* 156 */     putSpaceForProperty("io.netty.native.workdir");
/* 157 */     putSpaceForProperty("java.io.tmpdir");
/* 158 */     putSpaceForPath("workdir", () -> "");
/*     */   }
/*     */   
/*     */   private void putSpaceForProperty(String paramString) {
/* 162 */     putSpaceForPath(paramString, () -> System.getProperty(paramString));
/*     */   }
/*     */   
/*     */   private void putSpaceForPath(String paramString, Supplier<String> paramSupplier) {
/* 166 */     String str = "Space in storage for " + paramString + " (MiB)";
/*     */     
/*     */     try {
/* 169 */       String str1 = paramSupplier.get();
/* 170 */       if (str1 == null) {
/* 171 */         setDetail(str, "<path not set>");
/*     */         
/*     */         return;
/*     */       } 
/* 175 */       FileStore fileStore = Files.getFileStore(Path.of(str1, new String[0]));
/* 176 */       setDetail(str, String.format(Locale.ROOT, "available: %.2f, total: %.2f", new Object[] { Float.valueOf(sizeInMiB(fileStore.getUsableSpace())), Float.valueOf(sizeInMiB(fileStore.getTotalSpace())) }));
/* 177 */     } catch (InvalidPathException invalidPathException) {
/* 178 */       LOGGER.warn("{} is not a path", paramString, invalidPathException);
/* 179 */       setDetail(str, "<invalid path>");
/* 180 */     } catch (Exception exception) {
/* 181 */       LOGGER.warn("Failed retrieving storage space for {}", paramString, exception);
/* 182 */       setDetail(str, "ERR");
/*     */     } 
/*     */   }
/*     */   
/*     */   public void appendToCrashReportString(StringBuilder paramStringBuilder) {
/* 187 */     paramStringBuilder.append("-- ").append("System Details").append(" --\n");
/* 188 */     paramStringBuilder.append("Details:");
/* 189 */     this.entries.forEach((paramString1, paramString2) -> {
/*     */           paramStringBuilder.append("\n\t");
/*     */           paramStringBuilder.append(paramString1);
/*     */           paramStringBuilder.append(": ");
/*     */           paramStringBuilder.append(paramString2);
/*     */         });
/*     */   }
/*     */   
/*     */   public String toLineSeparatedString() {
/* 198 */     return this.entries.entrySet().stream()
/* 199 */       .map(paramEntry -> (String)paramEntry.getKey() + ": " + (String)paramEntry.getKey())
/* 200 */       .collect(Collectors.joining(System.lineSeparator()));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\SystemReport.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */