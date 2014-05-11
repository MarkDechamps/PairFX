/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package screensframework;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialogs;
import javafx.scene.control.Dialogs.DialogResponse;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Filter;
import model.I18n;
import model.Pairing;
import model.Player;
import model.Model;
import util.PlayerUtil;
import model.Screens;
import screensframework.cellfactory.PairingCellFactory;
import screensframework.cellfactory.PlayerCellFactory;
import screensframework.cellfactory.RankingCellFactory;
import util.PFXUtil;

/**
 *
 * @author mim
 */
public class ToernooiOverzichtController extends AbstractController {

    @FXML
    private CheckBox enkelOnafgewerktePartijen;
    @FXML
    private ListView<Player> spelers;
    @FXML
    private ListView<Pairing> paringen;
    @FXML
    private ListView<Player> ranking;
    @FXML
    private Text paringInfo;
    @FXML
    private Text paringPlayerInfo;
    @FXML
    private Label toernooiNaam;
    @FXML
    private TextField pairingFilter;
    
    
    private TextField vnaam = new TextField(I18n.get(Messages.voornaam));
    private TextField fnaam = new TextField(I18n.get(Messages.naam));
    private CheckBox afwezig = new CheckBox(I18n.get(Messages.afwezig));

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        paringInfo.setText("");
        paringPlayerInfo.setText("");
        vnaam.setPromptText(I18n.get(Messages.voornaam));
        fnaam.setPromptText(I18n.get(Messages.naam));

        spelers.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        spelers.setOnMouseClicked(maakSelectClickListener());
        spelers.setCellFactory(new PlayerCellFactory());
        spelers.setItems(FXCollections.observableList(Model.getInstance().getNietSpelendeSpelers()));
        paringen.setCellFactory(new PairingCellFactory());
        EventHandler<? super MouseEvent> paringClickedHandler = new MouseEventHandlerVoorParingInfo();
        paringen.setOnMouseClicked(paringClickedHandler);
        paringen.setItems(FXCollections.observableList(Model.getInstance().getAllePartijen()));

        ranking.setCellFactory(new RankingCellFactory());
        ranking.setOnMouseClicked(maakRankingClickedListener());
    }

    @FXML
    public void enkelOnafgewerkte() {
        refreshParingen();
    }

    @FXML
    public void maakParingen() {
        if (!Model.getInstance().hasSelectedPlayers()) {
            showInfoDialog(I18n.get(Messages.geenSpelersInSelectie));
            return;
        }
        

        List<Pairing> newPairings = Model.getInstance().pairSelectedPlayers();
        if (newPairings.isEmpty()) {
            showInfoDialog(I18n.get(Messages.geenParingMogelijkMetSpelers));

            PFX.log("Error:" + Model.getInstance().getLastError());
        } else {
            refreshAll();
        }
        save();
    }

    @FXML
    public void hoofdscherm() {
        controller().setScreen(Screens.startScreen);
    }

    @FXML
    public void witWint() {
        Pairing selectedPairing = selectedPairing();
        if (nietNull(selectedPairing)) {
            undoVorigeScoreIndienNodig(selectedPairing);
            selectedPairing.whiteWins();
            unselectPairing(selectedPairing);
            refreshAll();
            save();
        }

    }

    public void unselectPairing(Pairing pairing) {
        Model.getInstance().unselectPlayerForPairing(pairing.getBlack());
        Model.getInstance().unselectPlayerForPairing(pairing.getWhite());
        save();
    }

    @FXML
    public void zwartWint() {
        Pairing selectedPairing = selectedPairing();
        if (nietNull(selectedPairing)) {
            undoVorigeScoreIndienNodig(selectedPairing);
            selectedPairing.blackWins();
            unselectPairing(selectedPairing);
            refreshAll();
        }
        save();
    }

    @FXML
    public void remise() {
        Pairing selectedPairing = selectedPairing();
        if (nietNull(selectedPairing)) {
            undoVorigeScoreIndienNodig(selectedPairing);
            selectedPairing.draw();
            unselectPairing(selectedPairing);
            refreshAll();
        }
        save();
    }

    @FXML
    public void verwijderParing() {
        Pairing selectedPairing = selectedPairing();
        if (nietNull(selectedPairing)) {
            verwijderDezeParing(selectedPairing);
        } else {
            PFX.showInfoDialog(I18n.get(Messages.kiesEenParing), paringInfo);
        }
    }

    private Pairing selectedPairing() {
        Pairing paring = paringen.getSelectionModel().getSelectedItem();
        return paring;
    }

    private boolean nietNull(Object o) {
        return o != null;
    }

    private void refreshParingen() {
        ObservableList<Pairing> teTonen = teTonenParingen();
        String filterText = pairingFilter.getText();
        Filter filter = new Filter();
        List<Pairing> result = filter.filter(teTonen, filterText);
        
        
        paringen.setItems(FXCollections.observableList(result));
        if (paringen.getItems().size() > 0) {
            paringen.getSelectionModel().clearSelection();
        }
        forceListRefreshOn(paringen);
    }

    private void refreshPlayers() {
        Model.getInstance().unselectAllPlayersForPairing();
        spelers.getSelectionModel().clearSelection();
        spelers.setItems(null);
        final List<Player> nietSpelendeSpelers = Model.getInstance().getNietSpelendeSpelers();
        spelers.setItems(FXCollections.observableList(nietSpelendeSpelers));
        forceListRefreshOn(spelers);
    }

    private void refreshRanking() {
        ranking.getItems().clear();
        List<Player> itemsToAdd = Model.getInstance().getRanking();
        ranking.getItems().addAll(itemsToAdd);
        forceListRefreshOn(ranking);
    }

    private void refreshAll() {
        toernooiNaam.setText(Model.getInstance().getTitle());
        refreshPlayers();
        refreshParingen();
        refreshRanking();
        //refreshDisplays();
    }

    private List<Pairing> opponentsOfSelectedPlayer(Player selected) {
        return Model.getInstance().getAfgewerktePartijen(selected);
    }

    private Player getSelectedRankingPlayer() {
        ObservableList<Player> selected = ranking.getSelectionModel().getSelectedItems();
        if (selected.size() > 0) {
            return selected.get(0);
        } else {
            return null;
        }
    }

    @FXML
    public void showOpponentsOfSelectedPlayer() {
        Player selected = getSelectedRankingPlayer();
        if (selected == null) {
            showInfoDialog(I18n.get(Messages.kiesEenSpelerRanking));
        } else {
            Pane panel = createPaneWithOpponents(selected);
            Dialogs.showCustomDialog(stage(), panel, null, null, Dialogs.DialogOptions.OK, null);
        }
    }

    private Pane createNieuweSpelerPane() {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);

        Text title = new Text(I18n.get(Messages.nieuweSpeler));
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        final ObservableList<Node> children = vbox.getChildren();
        children.add(title);
        clearNewPlayerFields();
        children.add(vnaam);
        children.add(fnaam);
        return vbox;
    }

    private Pane createEditSpelerPane() {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);

        Text title = new Text(I18n.get(Messages.editSpeler));
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        final ObservableList<Node> children = vbox.getChildren();
        children.add(title);
        children.add(vnaam);
        children.add(fnaam);
        children.add(afwezig);
        return vbox;
    }

    private Pane createPaneWithOpponents(Player player) {
        final String TITLE = I18n.get(Messages.tegenstanders_title);

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);


        Text title = new Text(TITLE + player.getFirstname() + " " + player.getLastname());
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        final ObservableList<Node> children = vbox.getChildren();
        children.add(title);

        for (Pairing pairing : opponentsOfSelectedPlayer(player)) {

            String pairingString = PlayerUtil.buildPairingStringFor(pairing);
            Text txt = new Text(pairingString);
            txt.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
            children.add(txt);
        }

        return vbox;
    }

    private void undoVorigeScoreIndienNodig(Pairing selectedPairing) {
        if (selectedPairing.isPlayed()) {
            selectedPairing.undoResult();
        }
    }

    private <T> void forceListRefreshOn(ListView<T> lsv) {
        ObservableList<T> items = lsv.<T>getItems();
        lsv.<T>setItems(null);
        lsv.<T>setItems(items);
    }

    @FXML
    public void selectAll() {

        Model.getInstance().toggleSelectionOnAllAvailable();
        forceListRefreshOn(spelers);
        save();
    }

    @FXML
    public void addSpeler() {
        Pane panel = createNieuweSpelerPane();

        Dialogs.DialogResponse response = Dialogs.showCustomDialog(stage(), panel, "", I18n.get(Messages.nieuweSpeler), Dialogs.DialogOptions.OK_CANCEL, null);
        if (response == Dialogs.DialogResponse.OK) {
            Model model = Model.getInstance();
            if (model.getNrOfPlayersWithFirstnameLastname(vnaam.getText(), fnaam.getText()) == 0) {
                model.addPlayerWithFirstNameLastName(vnaam.getText(), fnaam.getText());
                refreshAll();
                save();
            } else {
                Dialogs.showInformationDialog(stage(), I18n.get(Messages.spelerBestaatAl));
            }
        }
    }

    public Stage stage() {
        return PFX.getStage(enkelOnafgewerktePartijen);
    }

    public void editSpeler(Player speler) {
        vnaam.setText(speler.getFirstname());
        fnaam.setText(speler.getLastname());
        afwezig.setSelected(speler.isAbsent());
        Pane panel = createEditSpelerPane();
        Dialogs.DialogResponse response = Dialogs.showCustomDialog(stage(), panel, "", I18n.get(Messages.editSpeler), Dialogs.DialogOptions.OK_CANCEL, null);
        if (response == Dialogs.DialogResponse.OK) {
            if (Model.getInstance().getNrOfPlayersWithFirstnameLastname(vnaam.getText(), fnaam.getText()) <= 1) {
                speler.setFirstname(vnaam.getText());
                speler.setLastname(fnaam.getText());
                speler.setAbsent(afwezig.isSelected());
            }
            refreshAll();
            save();
        }
    }

    @FXML
    public void removeSpeler() {
        ObservableList<Player> selected = ranking.getSelectionModel().getSelectedItems();
        if (selected.size() == 0) {
            return;
        }
        Player toRemove = selected.get(0);

        if (Model.getInstance().getTegenstandersVan(toRemove).size() > 0) {
            Dialogs.showWarningDialog(stage(), I18n.get(Messages.spelerVerwijderenKanNiet), "", I18n.get(Messages.spelersVerwijderenTitel), Dialogs.DialogOptions.OK);
        } else {
            DialogResponse response = Dialogs.showWarningDialog(stage(), toRemove.getFirstname() + " " + toRemove.getLastname() + " " + I18n.get(Messages.spelerVerwijderen), "", I18n.get(Messages.spelersVerwijderenTitel), Dialogs.DialogOptions.OK_CANCEL);
            if (response == DialogResponse.OK) {
                for (Player p : selected) {
                    if (Model.getInstance().playerHasNoGames(p)) {
                        Model.getInstance().removePlayer(p);
                    }
                }
                refreshAll();
                save();
            }
        }
    }

    public void save() {
        Model.getInstance().bewaarToernooi();
    }

    @Override
    public boolean onLoad() {

        refreshAll();
        return true;
    }

    @Override
    public void onUnload() {
        paringPlayerInfo.setText("");
        paringPlayerInfo.setText("");
    }

    private EventHandler<MouseEvent> maakSelectClickListener() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    int clicks = mouseEvent.getClickCount();

                    if (clicks > 1) {
                        Player speler = spelers.getSelectionModel().getSelectedItem();
                        Model.getInstance().toggleSelection(speler);
                        forceListRefreshOn(spelers);

                    }

                }
            }
        };
    }

    private EventHandler<? super MouseEvent> maakRankingClickedListener() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    int clicks = mouseEvent.getClickCount();

                    if (clicks > 1) {
                        editSpeler(ranking.getSelectionModel().getSelectedItem());
                        forceListRefreshOn(spelers);
                    }

                }
            }
        };
    }

    private void clearNewPlayerFields() {
        vnaam.clear();
        fnaam.clear();
        //startPunten.setValue(0);

    }

    private void showInfoDialog(String geenSpelersInSelectie) {
        PFX.showInfoDialog(geenSpelersInSelectie, enkelOnafgewerktePartijen);
    }

    private ObservableList<Pairing> teTonenParingen() {
        List<Pairing> toShow;
        if (enkelOnafgewerktePartijen.isSelected()) {
            toShow = Model.getInstance().getOnafgewerktePartijen();
        } else {
            toShow = Model.getInstance().getAllePartijen();
        }
        return FXCollections.observableList(toShow);
    }

    private void verwijderDezeParing(Pairing selectedPairing) {
        final String msg = I18n.get(Messages.zekerVanParingVerwijderen)+"\n"+PlayerUtil.buildPairingStringFor(selectedPairing);
        boolean remove = PFX.showOkCancelDialog(msg, paringInfo);
        if (remove) {
            unselectPairing(selectedPairing);
            Model.getInstance().removePairing(selectedPairing);
            refreshAll();
            save();
        }
    }

   

    private class MouseEventHandlerVoorParingInfo implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent t) {
            Pairing pairing = selectedPairing();
            if (pairing == null) {
                return;
            }
            StringBuilder pairingInfoText = new StringBuilder();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
            String creation = sdf.format(pairing.getCreationDate());
            String playedDate = "";
            if (pairing.isPlayed()) {
                playedDate = sdf.format(pairing.getPlayedDate());
            }
            pairingInfoText.append(I18n.get(Messages.aangemaakt));
            pairingInfoText.append(" ");
            pairingInfoText.append(creation);
            if (playedDate.length() > 0) {
                pairingInfoText.append(" ");
                pairingInfoText.append(I18n.get(Messages.gespeeld));
                pairingInfoText.append(" ");
                pairingInfoText.append(playedDate);
            }
            paringInfo.setText(pairingInfoText.toString());
            paringPlayerInfo.setText(maakDisplayString(pairing.getWhite()) + " VS " + maakDisplayString(pairing.getBlack()));
        }

        private String maakDisplayString(Player speler) {
            StringBuilder result = new StringBuilder();
            result.append(speler.getFirstname()).append(" ").append(speler.getLastname());


            return result.toString();
        }
    };

    @FXML
    public void export() {
        controller().setScreen(Screens.export);
    }

    @FXML
    public void pairingKeyPressed(KeyEvent event) {
        final Pairing selectedPairing = selectedPairing();
        if (PFXUtil.isNull(selectedPairing)) {
            return;
        }
        if (event.getCode() == KeyCode.DELETE) {
            verwijderParing();
        } else {
            String key = event.getCode().getName().toLowerCase();
            switch (key) {
                case "w":
                    selectedPairing.whiteWins();
                    break;
                case "z":
                    selectedPairing.blackWins();
                    break;
                case "r":
                    selectedPairing.draw();
                    break;
            }
            refreshAll();
        }

    }

    @FXML
    public void rankingKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.INSERT) {
            addSpeler();
        }
        if (event.getCode() == KeyCode.DELETE) {
            removeSpeler();
        }
        if (event.getCode() == KeyCode.ENTER) {
            final Player selectedRankingPlayer = getSelectedRankingPlayer();
            if (selectedRankingPlayer != null) {
                editSpeler(selectedRankingPlayer);
            }
        }
    }

    @FXML
    public void adjustFilter() {
       refreshParingen();     
    }
    
    @FXML
    public void undoLastPairing(){
        Model model =Model.getInstance();
        List<Pairing> lastRun = model.getOnafgewerktePartijenLaatsteRun();
       
        for(Pairing p:lastRun){
            //if(!p.isPlayed()){
                verwijderDezeParing(p);
            //}
        }
        
    }
}
