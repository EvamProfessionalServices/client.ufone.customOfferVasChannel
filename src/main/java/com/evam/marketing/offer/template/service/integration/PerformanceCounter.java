package com.evam.marketing.offer.template.service.integration;

import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Log4j2
@Service
public class PerformanceCounter {

    private final AtomicLong eventCountTotal = new AtomicLong(0);

    private final AtomicLong eventCountSilent = new AtomicLong(0);
    private final AtomicLong eventCountTimeConstraint = new AtomicLong(0);

    private final AtomicLong eventCountSuccess = new AtomicLong(0);
    private final AtomicLong eventCountFail = new AtomicLong(0);

    private final AtomicLong wscallCount = new AtomicLong(0);
    private final AtomicLong wscallTimeAvarage = new AtomicLong(0);

    private final AtomicLong appLifetimeTotalWSCallCount = new AtomicLong(0);
    private final AtomicLong appLifeTimeWSCallAverage = new AtomicLong(0);

    private final AtomicLong ts = new AtomicLong(System.currentTimeMillis());

    private final AtomicLong batchCountTotal = new AtomicLong(0);
    private final AtomicLong batchCountSuccess = new AtomicLong(0);
    private final AtomicLong batchCountError = new AtomicLong(0);
    private final AtomicLong batchCountDuplicate = new AtomicLong(0);
    private final AtomicLong eventCountDuplicate = new AtomicLong(0);

    private final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private final AtomicLong dbTimeForSave = new AtomicLong();
    private final AtomicLong dbCountForSave = new AtomicLong();

    public PerformanceCounter() {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(
                new CustomizableThreadFactory("StatWorker-" + getClass().getName()));
        scheduledExecutorService.scheduleAtFixedRate(this::report, 30, 30, TimeUnit.SECONDS);
    }

    public void incrementBatchCountSuccess() {
        this.batchCountSuccess.incrementAndGet();
        this.batchCountTotal.incrementAndGet();
    }

    public void incrementBatchCountError() {
        this.batchCountError.incrementAndGet();
        this.batchCountTotal.incrementAndGet();
    }

    public void incrementEventCountDuplicate(long size) {
        this.eventCountDuplicate.addAndGet(size);
        this.eventCountTotal.addAndGet(size);
    }

    public void incrementEventCountSilent() {
        this.eventCountSilent.incrementAndGet();
        this.eventCountTotal.incrementAndGet();
    }

    public void incrementWSCallCount(long totalCallTime) {
        this.wscallCount.incrementAndGet();
        this.appLifetimeTotalWSCallCount.incrementAndGet();
        this.wscallTimeAvarage.addAndGet(totalCallTime);
        this.appLifeTimeWSCallAverage.addAndGet(totalCallTime);
    }

    public void incrementEventCountSuccess() {
        this.eventCountSuccess.incrementAndGet();
        this.eventCountTotal.incrementAndGet();
    }

    public void incrementEventCountFail() {
        this.eventCountFail.incrementAndGet();
        this.eventCountTotal.incrementAndGet();
    }

    public synchronized void report() {

        long eCountSilent = this.eventCountSilent.getAndSet(0);
        long eCountTimeConstraint = this.eventCountTimeConstraint.getAndSet(0);
        long eCountSuccess = this.eventCountSuccess.getAndSet(0);
        long eCountFail = this.eventCountFail.getAndSet(0);
        long eCountTotal = this.eventCountTotal.getAndSet(0);

        long bCountSuccess = this.batchCountSuccess.getAndSet(0);
        long bCountError = this.batchCountError.getAndSet(0);
        long bCountDuplicate = this.batchCountDuplicate.getAndSet(0);
        long bCountTotal = this.batchCountTotal.getAndSet(0);

        long eCountDuplicate = this.eventCountDuplicate.getAndSet(0);

        long appLifetimeWSCallCount = this.appLifetimeTotalWSCallCount.get();
        long appLifeTimeWSCallAverageMillis = this.appLifeTimeWSCallAverage.get();
        long totalWSCallCount = this.wscallCount.getAndSet(0);
        long periodicWSCallAverageMillis = this.wscallTimeAvarage.getAndSet(0);

        if(periodicWSCallAverageMillis == 0 || totalWSCallCount == 0) {
            periodicWSCallAverageMillis = 1;
            totalWSCallCount = 1;
        }
        if(appLifeTimeWSCallAverageMillis == 0 || appLifetimeWSCallCount == 0){
            appLifeTimeWSCallAverageMillis = 1;
            appLifetimeWSCallCount = 1;
        }

        long now = System.currentTimeMillis();
        long diff = now - ts.getAndSet(now);
        double multiplier = 1000d / diff;
        log.info(
                "batch(success={},error={},duplicate={},total={},tps={}), event(duplicate={},silent={},timeConstraint={},success={},fail={},total={},tps={}), WebServiceCall(PeriodicTotalWSCallCount={},PeriodicWSCallAverageMillis={},AppLifeTimeWSCallAverage={})",
                new Object[]{
                        bCountSuccess,
                        bCountError,
                        bCountDuplicate,
                        bCountTotal,
                        decimalFormat.format(bCountTotal * multiplier),
                        eCountDuplicate,
                        eCountSilent,
                        eCountTimeConstraint,
                        eCountSuccess,
                        eCountFail,
                        eCountTotal,
                        decimalFormat.format(eCountTotal * multiplier),
                        totalWSCallCount,
                        decimalFormat.format(periodicWSCallAverageMillis / totalWSCallCount),
                        decimalFormat.format(appLifeTimeWSCallAverageMillis / appLifetimeWSCallCount)
                }
        );
    }

    public void addDbInsertDuration(long duration) {
        dbCountForSave.incrementAndGet();
        dbTimeForSave.addAndGet(duration);
    }

    public void addDbInsertDuration(long duration, int size) {
        dbCountForSave.addAndGet(size);
        dbTimeForSave.addAndGet(duration);
    }
}