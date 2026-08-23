package com.mojang.jtracy;

import java.nio.ByteBuffer;

class TracyBindings {
  static native void startup();
  
  static native void shutdown();
  
  static native void markFrame(long paramLong);
  
  static native void markFrameStart(long paramLong);
  
  static native void markFrameEnd(long paramLong);
  
  static native int beginZone(String paramString1, String paramString2, String paramString3, int paramInt);
  
  static native int frameImage(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean);
  
  static native void endZone(int paramInt);
  
  static native void addZoneText(int paramInt, String paramString);
  
  static native void setZoneColor(int paramInt1, int paramInt2);
  
  static native void addZoneValue(int paramInt, long paramLong);
  
  static native long mallocNamed(long paramLong1, long paramLong2, int paramInt);
  
  static native long freeNamed(long paramLong1, long paramLong2);
  
  static native void setThreadName(String paramString, int paramInt);
  
  static native void plotValue(long paramLong, double paramDouble);
  
  static native long leakName(String paramString);
  
  static native void appInfo(String paramString);
  
  static native void message(String paramString);
  
  static native void messageColored(String paramString, int paramInt);
  
  static native void newGpuContext(int paramInt1, long paramLong, float paramFloat, int paramInt2, int paramInt3);
  
  static native void setGpuContextName(int paramInt, String paramString);
  
  static native int beginGpuZone(int paramInt1, int paramInt2, String paramString1, String paramString2, String paramString3, int paramInt3);
  
  static native int endGpuZone(int paramInt1, int paramInt2);
  
  static native int submitQueryTimestamp(int paramInt1, int paramInt2, long paramLong);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\jtracy\TracyBindings.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */