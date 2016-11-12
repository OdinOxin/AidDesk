package de.odinoxin.aiddesk;

import de.odinoxin.aidcloud.provider.Provider;
import de.odinoxin.aidcloud.provider.TranslatorProvider;
import de.odinoxin.aiddesk.controls.refbox.RefBox;
import de.odinoxin.aiddesk.controls.refbox.RefBoxListItem;
import de.odinoxin.aiddesk.controls.translateable.Button;
import de.odinoxin.aiddesk.dialogs.DecisionDialog;
import de.odinoxin.aiddesk.plugins.Plugin;
import de.odinoxin.aiddesk.plugins.RecordEditor;
import de.odinoxin.aiddesk.plugins.RecordItem;
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

public class MainMenu extends Plugin implements Provider<MainMenu.PluginItem> {

    private RefBox<PluginItem> refBoxPlugins;
    private Button btnLogot;
    private Button btnExit;

    private static final PluginItem[] PLUGIN_ITEMS =
            {
                    new PluginItem(0, "MainMenu"),
                    new PluginItem(1, "PersonEditor"),
                    new PluginItem(2, "AddressEditor"),
                    new PluginItem(3, "CountryEditor"),
                    new PluginItem(4, "ContactTypeEditor"),
                    new PluginItem(5, "ContactInformationEditor"),
            };
    private static List<Plugin> plugins = new ArrayList<>();

    public MainMenu() {
        super("/mainmenu.fxml", "Main menu");

        this.refBoxPlugins = (RefBox<PluginItem>) this.root.lookup("#refBoxPlugins");
        this.refBoxPlugins.setProvider(this);
        this.refBoxPlugins.objProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                switch (newValue.getId()) {
                    case 1:
                        new PersonEditor(null);
                        break;
                    case 2:
                        new AddressEditor(null);
                        break;
                    case 3:
                        new CountryEditor(null);
                        break;
                    case 4:
                        new ContactTypeEditor(null);
                        break;
                    case 5:
                        new ContactInformationEditor(null);
                        break;
                }
                this.refBoxPlugins.setObj(null);
            }
        });
        this.btnLogot = (Button) this.root.lookup("#btnLogout");
        this.btnExit = (Button) this.root.lookup("#btnExit");

        this.btnLogot.setOnAction(ev -> {
            DecisionDialog dialog = new DecisionDialog(this, "Log out?", "Log out and close all related windows?");
            Optional<ButtonType> res = dialog.showAndWait();
            if (ButtonType.OK.equals(res.get())) {
                for (Plugin plugin : plugins)
                    plugin.close();
                this.close();
                new Login();
            }
        });
        Plugin.setButtonEnter(this.btnLogot);
        this.setOnCloseRequest(ev -> {
            DecisionDialog dialog = new DecisionDialog(this, "Exit?", "Exit AidDesk?");
            Optional<ButtonType> res = dialog.showAndWait();
            if (ButtonType.OK.equals(res.get())) {
                for (Plugin plugin : plugins)
                    plugin.close();
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

    @Override
    public PluginItem get(int id) {
        return null;
    }

    @Override
    public PluginItem save(PluginItem item) {
        return null;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public RefBoxListItem<PluginItem> getRefBoxItem(PluginItem item) {
        if (item == null)
            return null;
        return new RefBoxListItem<>(item, item.getName(), "");
    }

    @Override
    public List<RefBoxListItem<PluginItem>> search(List<String> expr) {
        List<RefBoxListItem<PluginItem>> items = new ArrayList<>();
        for (PluginItem item : PLUGIN_ITEMS)
            items.add(getRefBoxItem(item));
        return items;
    }

    @Override
    public PluginItemEditor openEditor(PluginItem entity) {
        return null;
    }

    static class PluginItem extends RecordItem<Object> {
        private String name;

        public PluginItem(int id, String name) {
            this.setId(id);
            this.name = TranslatorProvider.getTranslation(name);
        }

        @Override
        protected Object clone() {
            return new PluginItem(this.getId(), this.getName());
        }

        public String getName() {
            return name;
        }

        @Override
        public Object toEntity() {
            return null;
        }
    }

    private static abstract class PluginItemEditor extends RecordEditor<PluginItem> {
        private PluginItemEditor() {
            super(null, null);
        }
    }
}
