package de.odinoxin.aiddesk;

import de.odinoxin.aiddesk.controls.refbox.RefBox;
import de.odinoxin.aiddesk.controls.translateable.Button;
import de.odinoxin.aiddesk.dialogs.DecisionDialog;
import de.odinoxin.aiddesk.plugins.Plugin;
import de.odinoxin.aiddesk.plugins.addresses.AddressEditor;
import de.odinoxin.aiddesk.plugins.contact.information.ContactInformationEditor;
import de.odinoxin.aiddesk.plugins.contact.types.ContactTypeEditor;
import de.odinoxin.aiddesk.plugins.countries.CountryEditor;
import de.odinoxin.aiddesk.plugins.people.PersonEditor;
import javafx.scene.control.ButtonType;
import javafx.stage.WindowEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainMenu extends Plugin {

    private RefBox refBoxPlugins;
    private Button btnLogot;
    private Button btnExit;

    private static List<Plugin> plugins = new ArrayList<>();

    public MainMenu() {
        super("/mainmenu.fxml", "Main menu");

        this.refBoxPlugins = (RefBox) this.root.lookup("#refBoxPlugins");
        this.refBoxPlugins.refProperty().addListener((observable, oldValue, newValue) -> {
            if ((int) newValue != 0) {
                switch (this.refBoxPlugins.getText()) {
                    case "People":
                        new PersonEditor(Login.getPerson().getId());
                        break;
                    case "Addresses":
                        new AddressEditor();
                        break;
                    case "Countries":
                        new CountryEditor();
                        break;
                    case "Contact types":
                        new ContactTypeEditor();
                        break;
                    case "Contact information":
                        new ContactInformationEditor();
                        break;
                }
                this.refBoxPlugins.setRef(0);
            }
        });
        this.btnLogot = (Button) this.root.lookup("#btnLogout");
        this.btnExit = (Button) this.root.lookup("#btnExit");

        this.btnLogot.setOnAction(ev -> {
            DecisionDialog dialog = new DecisionDialog(this, "Log out?", "Log out and close all related windows?");
            Optional<ButtonType> res = dialog.showAndWait();
            if (ButtonType.OK.equals(res.get())) {
                for (Plugin plugin : plugins) {
                    plugin.close();
                }
                this.close();
                new Login();
            }
        });
        Plugin.setButtonEnter(this.btnLogot);
        this.setOnCloseRequest(ev -> {
            DecisionDialog dialog = new DecisionDialog(this, "Exit?", "Exit AidDesk and close all related windows?");
            Optional<ButtonType> res = dialog.showAndWait();
            if (ButtonType.OK.equals(res.get())) {
                for (Plugin plugin : plugins) {
                    plugin.close();
                }
                this.close();
            }
            ev.consume();
        });
        this.btnExit.setOnAction(ev -> this.fireEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSE_REQUEST)));
        Plugin.setButtonEnter(this.btnExit);
        this.show();
        this.sizeToScene();
        this.centerOnScreen();
    }

    public static void addPlugin(Plugin plugin) {
        plugins.add(plugin);
    }

    public static void removePlugin(Plugin plugin) {
        plugins.remove(plugin);
    }
}
