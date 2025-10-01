package org.firstinspires.ftc.teamcode.util.voyager;

import xyz.devmello.voyager.Voyager;
import xyz.devmello.voyager.math.geometry.Shape;
import xyz.devmello.voyager.pathgen.zones.Zone;

public class ZoneShape extends Zone {
    public ZoneShape(Shape<?> shape) {
        super(shape);
    }

    @Override
    public void onEnter(Voyager voyager) {}
}
