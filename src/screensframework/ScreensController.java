/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License"). You
 * may not use this file except in compliance with the License. You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package screensframework;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 *
 * @author Angie
 */
public class ScreensController extends StackPane {
    //Holds the screens to be displayed

    private Map<String, Node> screens = new HashMap<>();
    private Map<String, ControlledScreen> controllers = new HashMap<>();
    private String visibleScreen = "";
    private boolean fadingEnabled=true;

    public ScreensController() {
        super();
    }

    
    public void enableFading(){
        fadingEnabled=true;
    }
    public void disableFading(){
        fadingEnabled=false;
    }
    //Add the screen to the collection
    public void addScreen(String name, Node screen) {
        screens.put(name, screen);
    }

    public void addController(String name, ControlledScreen controller) {
        controllers.put(name, controller);
    }

    //Returns the Node with the appropriate name
    public Node getScreen(String name) {
        return screens.get(name);
    }

    //Loads the fxml file, add the screen to the screens collection and
    //finally injects the screenPane to the controller.
    public boolean loadScreen(String name, String resource,ResourceBundle resourceBundle) {
        try {
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource(resource));
            myLoader.setResources(resourceBundle);
            Parent loadScreen = (Parent) myLoader.load();
            ControlledScreen myScreenControler = ((ControlledScreen) myLoader.getController());
            myScreenControler.setScreenParent(this);
            addController(name, myScreenControler);
            addScreen(name, loadScreen);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    //This method tries to displayed the screen with a predefined name.
    //First it makes sure the screen has been already loaded.  Then if there is more than
    //one screen the new screen is been added second, and then the current screen is removed.
    // If there isn't any screen being displayed, the new screen is just added to the root.
    public boolean setScreen(final String name) {
        final Node screen = screens.get(name);
        if (screen != null) {   //screen loaded
            final DoubleProperty opacity = opacityProperty();

            if (!getChildren().isEmpty()) {    //if there is more than one screen
                boolean ok = controllers.get(name).onLoad();
                if (!ok) {
                    return false;
                }
                if(fadingEnabled){
                    Timeline fade = buildFader(opacity,screen);
                    fade.play();
                }else{
                changeScreen(screen);
                }

            } else {
                setOpacity(0.0);
                controllers.get(name).onLoad();
                getChildren().add(screen);       //no one else been displayed, then just show
                Timeline fadeIn = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                        new KeyFrame(new Duration(500), new KeyValue(opacity, 1.0)));
                fadeIn.play();
            }
            visibleScreen = name;
            return true;
        } else {
            System.out.println("screen hasn't been loaded!!! \n");
            return false;
        }



    }
 private void changeScreen(Node screen) {
                        controllers.get(visibleScreen).onUnload();
                        getChildren().remove(0);                    //remove the displayed screen
                        getChildren().add(0, screen);     //add the screen
                    }
    //This method will remove the screen with the given name from the collection of screens
    public boolean unloadScreen(String name) {
        if (screens.remove(name) == null) {
            System.out.println("Screen didn't exist");
            return false;
        } else {
            return true;
        }
    }

    private Timeline buildFader(final DoubleProperty opacity,final Node screen) {
        Timeline fade = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
                new KeyFrame(new Duration(100), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                changeScreen(screen);     
                Timeline fadeIn = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                        new KeyFrame(new Duration(800), new KeyValue(opacity, 1.0)));
                fadeIn.play();
            }

           
        }, new KeyValue(opacity, 0.0)));
        return fade;
    }

    boolean isFadingEnabled() {
        return fadingEnabled;
    }
}
