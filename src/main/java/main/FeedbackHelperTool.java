package main;

import controller.AppController;
import model.AppModel;
import model.IAppModel;
import view.AppView;
import view.IAppView;
import view.LoadingScreens;

import javax.swing.*;
import java.awt.*;

public class FeedbackHelperTool {

    public static void main(String[] args) {

        FeedbackHelperTool fht = new FeedbackHelperTool();
        fht.start();



    }

    public void start() {

        new Thread(LoadingScreens::showSplashScreen).start();

        AppModel model = new AppModel();
        AppController controller = new AppController(model);
        IAppView view = new AppView(controller);

        ((AppView) view).start();
    }
}
