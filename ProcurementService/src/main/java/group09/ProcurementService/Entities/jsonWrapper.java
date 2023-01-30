package group09.ProcurementService.Entities;

import java.util.ArrayList;
import java.util.List;

//this class is for turning an array list to an object
public class jsonWrapper {
    List<BackOrderSale> backOrderList = new ArrayList<>();

    public jsonWrapper() {
    }
    public jsonWrapper(List<BackOrderSale> backOrderList) {
        this.backOrderList = backOrderList;
    }

    public List<BackOrderSale> getBackOrderList() {
        return backOrderList;
    }

    public void setBackOrderList(List<BackOrderSale> backOrderList) {
        this.backOrderList = backOrderList;
    }

    @Override
    public String toString() {
        return "BackOrder={" + backOrderList +'}';
    }
}

