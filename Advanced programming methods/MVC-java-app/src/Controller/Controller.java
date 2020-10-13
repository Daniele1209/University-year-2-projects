package Controller;

import Custom_exception.Custom_exception;
import Model.IAquatic_animal;
import Repository.*;

public class Controller {

    IRepo repository;

    public Controller(IRepo repo){
        repository = repo;
    }

    public void add(IAquatic_animal elem) throws Custom_exception
    {
        repository.add(elem);
    }

    public void remove(String name) throws Custom_exception
    {
        repository.remove(name);
    }

    public IAquatic_animal[] show_over_1()
    {
        int index = 0;
        int size = 0;
        IAquatic_animal[] animals = repository.get_all();


        //see how many animals over 1 year old
        for(IAquatic_animal animal: animals) {
            if (animal.get_age() > 1) {
                size++;
            }
        }
        IAquatic_animal[] animals_over_1 = new IAquatic_animal[size];
        //put the animals in the new list
        for(IAquatic_animal animal: animals)
            if(animal.get_age() > 1) {
                animals_over_1[index++] = animal;
            }

        return animals_over_1;
    }
}
