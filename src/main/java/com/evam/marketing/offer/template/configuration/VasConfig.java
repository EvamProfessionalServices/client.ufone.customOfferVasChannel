package com.evam.marketing.offer.template.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.time.LocalTime;
import java.util.Map;

/**
 * The {@code VasConfig} class represents the configuration specific to VAS
 * of PTCL.
 *
 * @author Abdul Wadood
 * @since 8.0.2
 */
@Configuration
@ConfigurationProperties(prefix = "vas")
@Data
public class VasConfig {
    private String phase;
    private boolean timeRestricted;
    private String silentModeStart;
    private String silentModeEnd;
    private LocalTime silentModeStartTime;
    private LocalTime silentModeEndTime;

    private Map<String, Channel> channels;

    @Data
    public static class Channel {
        private String username;
        private String password;
        private String ip;
    }

    @Value("${vas.persist.jobBufferSize}")
    private int persistJobBufferSize = 10000;
    @Value("${vas.persist.poolSize}")
    private int persistPoolSize = 2;
    @Value("${vas.persist.enabled}")
    private boolean writeToDb;
    @Value("${vas.persist.sql.action:}")
    private String sqlAction;
    @Value("${vas.persist.sql.profile:}")
    private String sqlProfile;
    @Value("${vas.persist.sql.verify:}")
    private String sqlVerify;

    @Value("${vas.endpoints.action.add:}")
    private String urlActionAdd;
    @Value("${vas.endpoints.action.remove:}")
    private String urlActionRemove;
    @Value("${vas.endpoints.profile:}")
    private String urlProfile;
    @Value("${vas.endpoints.verify:}")
    private String urlVerify;

    @Value("${vas.payload.action.add:}")
    private String payloadAddVas;
    @Value("${vas.payload.action.remove:}")
    private String payloadRemoveVas;
    @Value("${vas.payload.profile:}")
    private String payloadProfile;
    @Value("${vas.payload.verify:}")
    private String payloadVerify;

    @Value("${vas.expr.action.code:}")
    private String exprActCode;
    @Value("${vas.expr.action.message:}")
    private String exprActMessage;
    @Value("${vas.expr.action.orderId:}")
    private String exprActOrderId;
    @Value("${vas.expr.action.status:}")
    private String exprActStatus;

    @Value("${vas.expr.profile.code:}")
    private String exprProCode;
    @Value("${vas.expr.profile.description:}")
    private String exprProDescription;
    @Value("${vas.expr.profile.productType:}")
    private String exprProProductType;
    @Value("${vas.expr.profile.dataRate:}")
    private String exprProDataRate;
    @Value("${vas.expr.profile.virtualNumber:}")
    private String exprProVirtualNumber;
    @Value("${vas.expr.profile.integrationId:}")
    private String exprProIntegrationId;
    @Value("${vas.expr.profile.mobileNumber:}")
    private String exprProMobileNumber;
    @Value("${vas.expr.profile.accountId:}")
    private String exprProAccountId;

    @Value("${vas.expr.verify.code:}")
    private String exprVerCode;
    @Value("${vas.expr.verify.desc:}")
    private String exprVerDesc;
    @Value("${vas.expr.verify.integrationId:}")
    private String exprVerIntegrationId;
    @Value("${vas.expr.verify.vasList:}")
    private String exprVerVasList;

    @PostConstruct
    public void init() {
        silentModeStartTime = LocalTime.parse(silentModeStart);
        silentModeEndTime = LocalTime.parse(silentModeEnd);
    }
}
