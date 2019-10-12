package project;
import project.controller.DB;
import project.controller.GUIController;
import project.model.SalesAgency;
import project.repository.JDBCProductRepository;
import project.view.GUI;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;

public class SpringMain
{
    public static void main(String[] args)
    {
        PropertyConfigurator.configure(args[0]);
        System.out.println("Starting App");

        ApplicationContext factory = new ClassPathXmlApplicationContext("spring-library.xml");
        GUIController controller = (GUIController) factory.getBean("guiController");
        GUI.run(controller);
    }
}
