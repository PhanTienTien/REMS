package com.rems.property.job;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.util.Timer;
import java.util.TimerTask;

@WebListener
public class PropertyReservationScheduler implements ServletContextListener {

    private Timer timer;
    private PropertyReservationExpirationJob job;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        timer = new Timer("PropertyReservationTimer", true);
        job = new PropertyReservationExpirationJob();
        
        // Run the job every 6 hours (6 * 60 * 60 * 1000 milliseconds)
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    job.expireLongReservations();
                } catch (Exception e) {
                    System.err.println("Error in property reservation expiration job: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }, 0, 6 * 60 * 60 * 1000); // Start immediately, then every 6 hours
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (timer != null) {
            timer.cancel();
        }
    }
}
