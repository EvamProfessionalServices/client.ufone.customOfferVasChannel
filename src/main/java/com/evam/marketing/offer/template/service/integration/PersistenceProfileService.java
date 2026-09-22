package com.evam.marketing.offer.template.service.integration;

import com.evam.marketing.offer.template.configuration.VasConfig;
import com.evam.marketing.offer.template.service.stream.model.request.BoltOnProfileRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * The {@code PersistenceProfileService} class saves vas data
 *
 * @author Babu Khan
 * @since 8.0.2
 */
@Service
@Log4j2
public class PersistenceProfileService extends AbstractPersistenceService<BoltOnProfileRequest> {
    private static final String LOG_NAME = "BOLT_ON_PROFILE";

    public PersistenceProfileService(JdbcTemplate jdbcTemplate, VasConfig config, PerformanceCounter counter) {
        super(jdbcTemplate, config.getPersistPoolSize(), config.getPersistJobBufferSize(), config.getSqlProfile(), counter);
    }

    @Override
    protected void setPs(PreparedStatement ps, BoltOnProfileRequest request) throws SQLException {
        // debug_mode, landline, response_code, response_description, response_productType,
        // response_dataRate, response_virtualNumber, response_integrationId,
        // response_mobileNumber, response_accountId, response_xml
        ps.setString(1, request.getSilentMode());
        ps.setString(2, request.getLandline());
        ps.setString(3, request.getCode());
        ps.setString(4, request.getDescription());
        ps.setString(5, request.getProductType());
        ps.setString(6, request.getDataRate());
        ps.setString(7, request.getVirtualNumber());
        ps.setString(8, request.getIntegrationId());
        ps.setString(9, request.getMobileNumber());
        ps.setString(10, request.getAccountId());
        ps.setString(11, request.getResponse());
    }

    @Override
    protected String getName() {
        return LOG_NAME;
    }
}
