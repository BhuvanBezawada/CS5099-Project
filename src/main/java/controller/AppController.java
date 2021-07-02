package controller;

import model.AppModel;
import model.IAppModel;
import view.AppView;
import view.IAppView;

public class AppController {

    IAppView appView;
    IAppModel appModel;

    public AppController(IAppView appView, IAppModel appModel) {
        this.appView = appView;
        this.appModel = appModel;
    }

}
