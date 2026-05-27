package via.pro2.vinyl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VinylLibraryTest {

    private VinylLibrary library;

    @BeforeEach
    void init(){
        library = new VinylLibrary();
    }

    @Test
    public void shelfEmptyWhenNoVinylAdded() throws Exception{
//        arrange
//        VinylLibrary library = new VinylLibrary();
//        act
        List<String> list = library.getVinyList();
//        assert
        assertTrue(list.isEmpty(), () -> "VinylLibrary should be empty");
    }

    @Test
    public void libraryContainsTwoVinylsWhenTwoVinylsAreAdded(){
//        VinylLibrary library = new VinylLibrary();
        library.add("A, a, 1961");
        library.add("B, b, 1921");
        List<String> list = library.getVinyList();
        assertEquals(2, list.size(), () -> "VinylLibrary should be 2");
    }
}
