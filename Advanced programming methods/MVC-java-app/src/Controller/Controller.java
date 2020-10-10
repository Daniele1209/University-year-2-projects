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
        IAquatic_animal[] animals = repository.get_all();

        for(IAquatic_animal animal: animals)
            if(animal.get_age() > 1) {
                animals[index] = animal;
                index++;
            }
        return animals;
    }
}
