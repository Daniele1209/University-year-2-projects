package Repository;

import Model.IAquatic_animal;
import Custom_exception.Custom_exception;

public class Repo implements IRepo  {

    public IAquatic_animal animals[];
    private int size;
    private int current_index;

    public Repo(int size) {
        this.size = size;
        current_index = 0;
        animals = new IAquatic_animal[size];
    }

    @Override
    public void add(IAquatic_animal elem) throws Custom_exception {
        if(this.current_index != this.size) {
            animals[current_index] = elem;
            current_index ++;
        }
        else
            throw new Custom_exception("Array full :( \n");
    }

    @Override
    public void remove(String name) throws Custom_exception {
        boolean removed = false;
        for(int  i = 0; i < current_index; i++) {
            if(animals[i].get_name().equals(name)) {
                removed = true;
                for(int index = i; index < current_index-1; index++)
                    animals[index] = animals[index+1];
                size--;
            }
        }
        if(!removed)
            throw new Custom_exception("Error 404: Aquatic animal not found !\n");
    }

    @Override
    public IAquatic_animal[] get_all() {
        return animals;
    }
}
