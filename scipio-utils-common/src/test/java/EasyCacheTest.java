import com.github.ScipioAM.scipio_utils_common.data.cache.EasyCacheS;
import com.github.ScipioAM.scipio_utils_common.data.cache.EasyCacheSApi;
import org.junit.jupiter.api.Test;

/**
 * @date 2021/9/28
 */
public class EasyCacheTest {

    @Test
    public void test0() {
        EasyCacheSApi easyCache = new EasyCacheS();
        easyCache.putData("k0",123);
        easyCache.putData("k1","sdf");
        System.out.println(easyCache);
    }

}
