package com.georgefitzpatrick.trading.crypto.ui;

import com.georgefitzpatrick.trading.crypto.CryptoTradingUiProperties;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author George Fitzpatrick
 */
@Component
public class StageInitializer {

    /* ----- Fields ----- */

    private final ApplicationContext context;
    private final CryptoTradingUiProperties properties;

    /* ----- Constructors ----- */

    @Autowired
    public StageInitializer(ApplicationContext context, CryptoTradingUiProperties properties) {
        this.context = context;
        this.properties = properties;
    }

    /* ----- Methods ----- */

    @EventListener
    public void stageReady(Stage stage) throws IOException {
        String title = properties.getTitle();
        Resource view = properties.getPrimaryView();

        // load fxml layout and assign controller instance from spring application context
        FXMLLoader loader = new FXMLLoader(view.getURL());
        loader.setControllerFactory(context::getBean);
        Parent root = loader.load();

        // create scene that fills primary screen
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(root, bounds.getWidth(), bounds.getHeight());

        // add all style sheets to scene
        for (Resource styleSheet : properties.getStyleSheets()) {
            String externalForm = styleSheet.getURL().toExternalForm();
            scene.getStylesheets().add(externalForm);
        }

        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

}
