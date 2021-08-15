package main;

import controller.AppController;
import controller.IAppController;
import model.AppModel;
import view.AppView;
import view.IAppView;
import view.LoadingScreens;

public class FeedbackHelperTool {

    public static void main(String[] args) {

        FeedbackHelperTool fht = new FeedbackHelperTool();
        fht.start();



    }

    public void start() {

        new Thread(LoadingScreens::showSplashScreen).start();

        AppModel model = new AppModel();
        IAppController controller = new AppController(model);
        IAppView view = new AppView(controller);

        view.start();
    }
}
