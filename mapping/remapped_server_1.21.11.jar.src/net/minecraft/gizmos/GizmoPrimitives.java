package net.minecraft.gizmos;

import net.minecraft.world.phys.Vec3;

public interface GizmoPrimitives {
  void addPoint(Vec3 paramVec3, int paramInt, float paramFloat);
  
  void addLine(Vec3 paramVec31, Vec3 paramVec32, int paramInt, float paramFloat);
  
  void addTriangleFan(Vec3[] paramArrayOfVec3, int paramInt);
  
  void addQuad(Vec3 paramVec31, Vec3 paramVec32, Vec3 paramVec33, Vec3 paramVec34, int paramInt);
  
  void addText(Vec3 paramVec3, String paramString, TextGizmo.Style paramStyle);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\gizmos\GizmoPrimitives.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */