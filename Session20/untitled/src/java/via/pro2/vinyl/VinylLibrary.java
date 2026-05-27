package via.pro2.vinyl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VinylLibrary {
    private final List<String> vinylList = new ArrayList<String>();

    public List<String> getVinyList() {
        return vinylList;
    }

    public void add(String s) {
        vinylList.add(s);
    }
}
