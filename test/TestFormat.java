
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.*;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mim
 */
public class TestFormat {

    @Test
    public void testFormat() {
        double points = 1;
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.0");

        System.out.printf("Floating-point with commas: %,f\n", 1234567.123);
        System.out.println(decimalFormat.format(points));
    }

    @Test
    public void testDateFormat() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String result = sdf.format(new Date());
        System.out.println("result:" + result);
    }
}
