package view;

import controller.IAppController;

/**
 * App View Class.
 */
public class AppView implements IAppView {

    // Instance variable
    private final IAppController controller;

    /**
     * Constructor.
     *
     * @param controller The controller.
     */
    public AppView(IAppController controller) {
        this.controller = controller;
    }

    /**
     * Start the app.
     */
    @Override
    public void start() {
        new HomeScreen(controller);
    }

}
