package via.pro2.mock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MockitoSetupTest {
    @Mock MyConcatt myConcatt;

    @Test
    void myMockitoIsWorking(){
//        arrange
        when(myConcatt.myConcat("IT", "WORKS")).thenReturn("YES");
//        act
        String result = myConcatt.myConcat("IT", "WORKS");
//        assert
        assertEquals("YES", result);

        System.out.println("go home");
    }
}
