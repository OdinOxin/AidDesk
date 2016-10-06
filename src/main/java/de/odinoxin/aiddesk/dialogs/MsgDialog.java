package de.odinoxin.aiddesk.dialogs;

import de.odinoxin.aiddesk.Login;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class MsgDialog
{
    public static void showMsg(Window owner, String title, String msg)
    {
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.getIcons().add(new Image(MsgDialog.class.getResource("/AidDesk.png").toString()));

        GridPane msgGrid = null;
        try
        {
            msgGrid = FXMLLoader.load(MsgDialog.class.getResource("/dialogs/msgdialog.fxml"));
            ((Text) msgGrid.lookup("#txtMsg")).setText(msg);
            ((Button) msgGrid.lookup("#btnOK")).setOnAction(ev -> stage.close());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        stage.setScene(new Scene(msgGrid));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(owner);
        stage.show();
    }
}
