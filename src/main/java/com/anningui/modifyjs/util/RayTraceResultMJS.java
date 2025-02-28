package com.anningui.modifyjs.util;

import net.minecraft.world.phys.Vec3;

public record RayTraceResultMJS(
        double startX, double startY, double startZ,
        double endX, double endY, double endZ,
        Vec3 start, Vec3 end, Vec3 direction
) {}