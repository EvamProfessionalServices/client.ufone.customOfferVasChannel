package com.evam.marketing.offer.template.service.integration;

import com.evam.marketing.offer.template.configuration.VasConfig;
import com.evam.marketing.offer.template.service.stream.model.request.BoltOnActionRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * The {@code PersistenceActionService} class saves vas data
 *
 * @author Babu Khan
 * @since 8.0.2
 */
@Service
@Log4j2
public class PersistenceActionService extends AbstractPersistenceService<BoltOnActionRequest> {
    private static final String LOG_NAME = "BOLT_ON_ACTION";

    public PersistenceActionService(JdbcTemplate jdbcTemplate, VasConfig config, PerformanceCounter counter) {
        super(jdbcTemplate, config.getPersistPoolSize(), config.getPersistJobBufferSize(), config.getSqlAction(), counter);
    }

    @Override
    protected void setPs(PreparedStatement ps, BoltOnActionRequest request) throws SQLException {
        // offer_code, offer_uuid, scenario_name, segment_code, insert_time,
        // silent_mode, actor_id, action, pstn, integration_id, service_id,
        // response_error_code, response_error_message, response_order_id, response_status
        // channel
        ps.setString(1, request.getCustomBenefitRequest().getCode());
        ps.setString(2, request.getCustomBenefitRequest().getOfferUUID());
        ps.setString(3, request.getCustomBenefitRequest().getScenarioName());
        ps.setString(4, request.getCustomBenefitRequest().getSegmentCode());
        ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(6, request.getSilentMode());
        ps.setString(7, request.getCustomBenefitRequest().getActorId());
        ps.setString(8, request.getAction());
        ps.setString(9, request.getLandline());
        ps.setString(10, request.getIntegrationId());
        ps.setString(11, request.getPartNumber());
        ps.setString(12, request.getErrorCode());
        ps.setString(13, request.getErrorMessage());
        ps.setString(14, request.getOrderId());
        ps.setString(15, request.getStatus());
        ps.setString(16, request.getResponse());
        ps.setString(17, request.getChannel());
    }

    @Override
    protected String getName() {
        return LOG_NAME;
    }
}
