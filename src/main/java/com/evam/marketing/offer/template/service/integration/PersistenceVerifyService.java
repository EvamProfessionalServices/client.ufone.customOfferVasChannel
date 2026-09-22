package com.evam.marketing.offer.template.service.integration;

import com.evam.marketing.offer.template.configuration.VasConfig;
import com.evam.marketing.offer.template.service.stream.model.request.BoltOnVerificationRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * The {@code PersistenceVerifyService} class saves vas data
 *
 * @author Babu Khan
 * @since 8.0.2
 */
@Service
@Log4j2
public class PersistenceVerifyService extends AbstractPersistenceService<BoltOnVerificationRequest> {
    private static final String LOG_NAME = "BOLT_ON_VERIFY";

    public PersistenceVerifyService(JdbcTemplate jdbcTemplate, VasConfig config, PerformanceCounter counter) {
        super(jdbcTemplate, config.getPersistPoolSize(), config.getPersistJobBufferSize(), config.getSqlVerify(), counter);
    }

    @Override
    protected void setPs(PreparedStatement ps, BoltOnVerificationRequest request) throws SQLException {
        // debug_mode, part_number, virtual_number, action, response_code,
        // response_description, response_integrationId, response_installed_vas, response_xml
        ps.setString(1, request.getSilentMode());
        ps.setString(2, request.getPartNumber());
        ps.setString(3, request.getVirtualNumber());
        ps.setString(4, request.getAction());
        ps.setString(5, request.getCode());
        ps.setString(6, request.getDescription());
        ps.setString(7, request.getIntegrationId());
        ps.setString(8, request.getInstalledVas());
        ps.setString(9, request.getResponse());
    }

    @Override
    protected String getName() {
        return LOG_NAME;
    }
}
