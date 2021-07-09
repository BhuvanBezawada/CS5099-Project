package main;

import controller.AppController;
import model.AppModel;
import model.IAppModel;
import view.AppView;
import view.IAppView;

public class FeedbackHelperTool {

    public static void main(String[] args) {
        AppModel model = new AppModel();
        AppController controller = new AppController(model);
        IAppView view = new AppView(controller);

        ((AppView) view).start();
    }
}
