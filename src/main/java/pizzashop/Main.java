package pizzashop;

import javafx.application.Application;
import javafx.stage.Stage;
import pizzashop.gui.KitchenGUIInitializer;
import pizzashop.gui.MainGUIInitializer;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;
import pizzashop.service.OrdersService;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        MenuRepository repoMenu=new MenuRepository();
        PaymentRepository payRepo= new PaymentRepository("data/payments.txt");
        OrdersService service = new OrdersService(repoMenu, payRepo);

        System.out.println("Opening main stage");

        MainGUIInitializer mainGUI = new MainGUIInitializer();
        mainGUI.InitGui(primaryStage, service);

        KitchenGUIInitializer kitchenGUI = new KitchenGUIInitializer();
        kitchenGUI.InitGui();
    }

    public static void main(String[] args) {
        launch(args);
    }
}