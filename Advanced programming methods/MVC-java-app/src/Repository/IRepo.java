package Repository;
import Model.IAquatic_animal;
import Custom_exception.Custom_exception;

public interface IRepo {
    public void add(IAquatic_animal elem) throws Custom_exception;
    public void remove(String elem) throws Custom_exception;
    public IAquatic_animal[] get_all();
}
