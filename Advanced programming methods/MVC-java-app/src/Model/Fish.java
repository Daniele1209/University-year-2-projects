package Model;

public class Fish implements IAquatic_animal{
    int age;
    String name;

    public Fish(String name, int age) {
        this.age = age;
        this.name = name;
    }
    @Override
    public void set_name(String name) {
        this.name = name;
    }
    @Override
    public String get_name() {
        return name;
    }

    @Override
    public void set_age(int age){
        this.age = age;
    }

    @Override
    public int get_age() {
        return age;
    }
}
