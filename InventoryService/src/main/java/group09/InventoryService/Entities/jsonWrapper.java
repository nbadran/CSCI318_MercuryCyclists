package group09.InventoryService.Entities;

import java.util.ArrayList;
import java.util.List;

//this class is for turning an array list to an object
public class jsonWrapper {
    List<BackOrder> backOrderList = new ArrayList<>();

    public jsonWrapper() {
    }
    public jsonWrapper(List<BackOrder> backOrderList) {
        this.backOrderList = backOrderList;
    }

    public List<BackOrder> getBackOrderList() {
        return backOrderList;
    }

    public void setBackOrderList(List<BackOrder> backOrderList) {
        this.backOrderList = backOrderList;
    }

    @Override
    public String toString() {
        return "BackOrder={" + backOrderList +'}';
    }
}
