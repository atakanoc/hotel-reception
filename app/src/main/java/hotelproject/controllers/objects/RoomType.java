package hotelproject.controllers.objects;

public class RoomType {
    private final String t_name;
    private final int r_size;
    private final int has_view;
    private final int has_kitchen;
    private final int has_bathroom;
    private final int has_workspace;
    private final int has_tv;
    private final int has_coffee_maker;
    private int beds;

    public RoomType(String t_name, int beds, int r_size, int has_view, int has_kitchen, int has_bathroom, int has_workspace, int has_tv, int has_coffee_maker) {
        this.t_name = t_name;
        this.beds = beds;
        this.r_size = r_size;
        this.has_view = has_view;
        this.has_kitchen = has_kitchen;
        this.has_bathroom = has_bathroom;
        this.has_workspace = has_workspace;
        this.has_tv = has_tv;
        this.has_coffee_maker = has_coffee_maker;
    }

    public String getT_name() {
        return t_name;
    }

    public int getBeds() {
        return beds;
    }

    public void setBeds(int beds) {
        this.beds = beds;
    }

    public int getR_size() {
        return r_size;
    }

    public int getHas_view() {
        return has_view;
    }

    public int getHas_kitchen() {
        return has_kitchen;
    }

    public int getHas_bathroom() {
        return has_bathroom;
    }

    public int getHas_workspace() {
        return has_workspace;
    }

    public int getHas_tv() {
        return has_tv;
    }

    public int getHas_coffee_maker() {
        return has_coffee_maker;
    }

    /**
     * Returns attribute information as a String
     *
     * @return attribute information as a String
     */
    @Override
    public String toString() {
        return "RoomType{" +
                "t_name='" + t_name + '\'' +
                ", beds=" + beds +
                ", r_size=" + r_size +
                ", has_view=" + has_view +
                ", has_kitchen=" + has_kitchen +
                ", has_bathroom=" + has_bathroom +
                ", has_workspace=" + has_workspace +
                ", has_tv=" + has_tv +
                ", has_coffee_maker=" + has_coffee_maker +
                '}';
    }
}
