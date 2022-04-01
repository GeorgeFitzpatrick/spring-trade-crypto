package com.georgefitzpatrick.trading.crypto.ui;

import com.georgefitzpatrick.trading.crypto.CryptoTradingUiProperties;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

import static javafx.scene.control.ButtonBar.ButtonData.OK_DONE;
import static javafx.scene.control.ButtonType.CANCEL;
import static javafx.scene.control.ButtonType.OK;

/**
 * @author George Fitzpatrick
 */
@Component
public class FormInitializer {

    /* ----- Fields ----- */

    private final ApplicationContext context;
    private final CryptoTradingUiProperties properties;
    private final I18n i18n;

    /* ----- Constructors ----- */

    @Autowired
    public FormInitializer(ApplicationContext context, CryptoTradingUiProperties properties, I18n i18n) {
        this.context = context;
        this.properties = properties;
        this.i18n = i18n;
    }

    /* ----- Methods ----- */

    public <R> Optional<R> showAndWait(Resource layout) throws IOException {
        return showAndWait(layout, null);
    }

    public <R> Optional<R> showAndWait(Resource layout, R editing) throws IOException {
        String title = properties.getTitle();

        // load fxml layout and assign controller instance from spring application context
        FXMLLoader loader = new FXMLLoader(layout.getURL());
        loader.setControllerFactory(context::getBean);
        Parent content = loader.load();

        // type cast controller to form and set value
        Form<R> form = loader.getController();
        form.setValue(editing);

        // create new dialog window and set its content
        Dialog<R> dialog = new Dialog<>();
        dialog.setTitle(title);
        DialogPane root = dialog.getDialogPane();
        root.setContent(content);

        // map dialog close action to object of type R
        dialog.setResultConverter(button -> {
            ButtonBar.ButtonData data = button == null ? null : button.getButtonData();
            return data == OK_DONE && form.validBinding().getValue() ? form.getValue() : null;
        });

        // add all style sheets to scene
        for (Resource styleSheet : properties.getStyleSheets()) {
            String externalForm = styleSheet.getURL().toExternalForm();
            root.getScene().getStylesheets().add(externalForm);
        }

        root.getButtonTypes().addAll(CANCEL, OK);

        // localise "cancel" button
        Button cancel = (Button) root.lookupButton(CANCEL);
        cancel.textProperty().bind(i18n.string("cancel"));

        // localise "ok" button and disable it while user input is invalid
        Button ok = (Button) root.lookupButton(OK);
        ok.textProperty().bind(i18n.string("ok"));
        ok.disableProperty().bind(Bindings.not(form.validBinding()));

        return dialog.showAndWait();
    }

}
