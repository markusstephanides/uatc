package com.digotsoft.uatc.ui;

import com.digotsoft.uatc.Game;
import com.digotsoft.uatc.radar.Radar;
import com.digotsoft.uatc.sim.Flight;
import com.digotsoft.uatc.sim.Simulator;
import com.digotsoft.uatc.util.ArrDepRenderingRunnable;
import org.newdawn.slick.*;

import java.util.concurrent.atomic.AtomicInteger;

public class ArrDepBox extends UIElement {

    private Simulator simulator;
    private Font titleFont;

    public ArrDepBox(Game game, float x, float y, float width, float height, Simulator simulator) {
        super(game, x, y, width, height);
        this.simulator = simulator;
        this.titleFont = new TrueTypeFont(new java.awt.Font("Arial", java.awt.Font.BOLD,18), true);
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        if(this.x == Integer.MAX_VALUE) this.x = container.getWidth() - this.getWidth() - 10;
        if(this.y == Integer.MAX_VALUE) this.y = container.getHeight() - this.getHeight() - 10;

        g.setColor(new Color(0x0, 0x0d, 0x33));
        g.fillRect(this.x, this.y, this.width, this.height);
        g.setColor(Color.white);
        g.setLineWidth(4f);
        g.drawRect( this.x, this.y, this.width, this.height);
        g.setLineWidth(2f);
        g.drawLine(this.x, this.y + 25, this.x + this.width, this.y + 25);
        this.titleFont.drawString(this.x + 4, this.y + 4, "Arrivals/Departures");

        AtomicInteger lastY = new AtomicInteger((int)this.y + 30);

        ArrDepRenderingRunnable renderIO = (flight, prefix) -> {
            String str = flight.getCallsign() + " " + flight.getFlightplan().getDestAirport().getIcao();
            g.drawString(prefix + " " + str + " " + flight.getClearedWaypoint() + " " + flight.getClearedFL(), ArrDepBox.this.x + 5, lastY.get());
            lastY.set(lastY.get() + 15);
        };

        for (Flight flight : this.simulator.getFlights()) {
            if(flight == null) {
                continue;
            }

            if(flight.getFlightplan().getDepAirport().equals(this.simulator.getControllingAirport()) &&
                    !flight.getFlightplan().getDestAirport().equals(this.simulator.getControllingAirport())) {
                // outbound
                renderIO.render(flight, "O");
            } else if(!flight.getFlightplan().getDepAirport().equals(this.simulator.getControllingAirport()) &&
                    flight.getFlightplan().getDestAirport().equals(this.simulator.getControllingAirport())) {
                // inbound
                renderIO.render(flight, "I");
            } else if(flight.getFlightplan().getDepAirport().equals(this.simulator.getControllingAirport()) &&
                    flight.getFlightplan().getDestAirport().equals(this.simulator.getControllingAirport())) {
                // vfr
                renderIO.render(flight, "VFR");
            }
        }
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {

    }

    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {

    }

    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {
        //TODO Enable Moving of ArrDepBox
//        System.out.println("hi");
//        if(this.x <= oldx && this.y <= oldy &&
//                oldx <= this.x + this.width && oldy <= this.y + 25) {
//            Radar.lockRadarScrolling = true;
//            if( (this.x + this.width + (newx-oldx) >= this.width && newx > oldx) ||
//                    (this.y + 25 + (newy-oldy) >= this.height && newy > oldy)) {
//                System.out.println("CANCEL");
//                return;
//            }
//
//            this.x += newx-oldx;
//            this.y += newy-oldy;
//            Radar.lockRadarScrolling = false;
//
//            System.out.println("Arrdepbox");
//        }
    }
}
