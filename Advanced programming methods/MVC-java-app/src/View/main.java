/*8. Intr-un acvariu traiesc pesti si broaste testoase.
     Sa se afiseze toate vietuitoarele din acvariu care sunt
     mai batrine de 1 an.
 */

package View;
import Custom_exception.Custom_exception;
import Repository.*;
import Controller.*;
import Model.*;

public class main {
    public static void main(String[] args) {

        IRepo repo = new Repo(5);
        Controller controller = new Controller(repo);

        //tests - adding and removing elements
        Turtle ninja_turtle_1 = new Turtle("Donatello", 0);
        Turtle ninja_turtle_2 = new Turtle("Raphael", 3);
        Turtle ninja_turtle_3 = new Turtle("Michelangelo", 4);
        Turtle ninja_turtle_4 = new Turtle("Leonardo", 0);
        Fish fish_1 = new Fish("Nemo", 3);
        Fish fish_2 = new Fish("Marlin", 4);

        try {
            controller.add(ninja_turtle_1);
            controller.add(ninja_turtle_2);
            controller.add(ninja_turtle_3);
            controller.add(ninja_turtle_4);
            controller.add(fish_1);
            //should not be able to add the last one
            controller.add(fish_2);
        }
        catch(Custom_exception exept) {
            System.out.println(exept.getMessage());
        }

        //function for the task of the problem
        IAquatic_animal[] animals_to_show = controller.show_over_1();
        for(IAquatic_animal animal : animals_to_show)
            System.out.println(animal.get_name());

        try {
            controller.remove("Raphael");
            //this should not work
            controller.remove("NO NAME");
        }
        catch(Custom_exception exept) {
            System.out.println(exept.getMessage());
        }

    }

}
