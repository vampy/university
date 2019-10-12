package com.aop;

import com.aop.controller.GUIController;
import com.aop.model.Library;
import com.aop.repository.BookRepository;
import com.aop.view.GUI;
import org.apache.log4j.PropertyConfigurator;

// Use multiple terminals
public class Main
{
    public static void main(String[] args)
    {
        PropertyConfigurator.configure(args[0]);
        System.out.println("Starting App");
        Library library = new Library(new BookRepository());
        GUIController controller = new GUIController(library);

        GUI.run(controller);
    }
}
