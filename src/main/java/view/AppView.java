package view;

import controller.AppController;

public class AppView implements IAppView {

    private AppController controller;

    public AppView(AppController controller) {
        this.controller = controller;
    }

    public void start() {
        HomeScreen homeScreen = new HomeScreen(controller);
    }
}
