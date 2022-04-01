package com.georgefitzpatrick.trading.crypto;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.Set;

/**
 * @author George FitzpatrickΩ
 */
@Configuration
@ConfigurationProperties(prefix = "trading.crypto.ui")
public class CryptoTradingUiProperties {

    /* ----- Fields ----- */

    private String title = "";
    private Resource primaryView;
    private String resourceBundleBaseName;
    private Set<Resource> styleSheets;

    /* ----- Methods ----- */

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Resource getPrimaryView() {
        return primaryView;
    }

    public void setPrimaryView(Resource primaryView) {
        this.primaryView = primaryView;
    }

    public String getResourceBundleBaseName() {
        return resourceBundleBaseName;
    }

    public void setResourceBundleBaseName(String resourceBundleBaseName) {
        this.resourceBundleBaseName = resourceBundleBaseName;
    }

    public Set<Resource> getStyleSheets() {
        return styleSheets;
    }

    public void setStyleSheets(Set<Resource> styleSheets) {
        this.styleSheets = styleSheets;
    }

}
