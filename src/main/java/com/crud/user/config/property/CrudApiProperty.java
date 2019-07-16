package com.crud.user.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("crud")
public class CrudApiProperty {

    private final Security security = new Security();
    private final Mail mail = new Mail();
    private String originAllowed = "http://localhost:8000";

    @Getter
    @Setter
    public static class Security {

        private boolean enableHttps;
    }

    @Getter
    @Setter
    public static class Mail {

        private String host;
        private Integer port;
        private String username;
        private String password;

    }
}
