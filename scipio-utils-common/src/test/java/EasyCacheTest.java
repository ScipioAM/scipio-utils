import com.github.ScipioAM.scipio_utils_common.data.cache.EasyCache;
import org.junit.jupiter.api.Test;

/**
 * @date 2021/9/28
 */
public class EasyCacheTest implements EasyCache<String,Integer> {

    @Test
    public void test0() {
        setData(0);
    }

}
