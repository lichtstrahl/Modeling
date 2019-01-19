package iv.root.modeling.network.dto;

import java.util.List;

public class ContainerForList {
    public List<Integer> listLow;
    public List<Integer> listMiddle;
    public List<Integer> listHigh;

    public ContainerForList(List<Integer> listLow, List<Integer> listMiddle, List<Integer> listHigh) {
        this.listLow = listLow;
        this.listMiddle = listMiddle;
        this.listHigh = listHigh;
    }
}
