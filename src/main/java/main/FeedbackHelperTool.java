package main;

import controller.AppController;
import model.AppModel;
import model.IAppModel;
import view.AppView;
import view.IAppView;

public class FeedbackHelperTool {

    public static void main(String[] args) {
        IAppView view = new AppView();
        IAppModel model = new AppModel();
        AppController controller = new AppController(view, model);
    }
}
