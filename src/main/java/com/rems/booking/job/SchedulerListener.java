package com.rems.booking.job;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
public class SchedulerListener
        implements ServletContextListener {

    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(
                () -> {
                    new BookingExpirationJob().run();
                },
                1,
                1,
                TimeUnit.HOURS
        );
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

        scheduler.shutdown();
    }
}
