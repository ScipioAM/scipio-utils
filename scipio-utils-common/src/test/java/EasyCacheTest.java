import com.github.ScipioAM.scipio_utils_common.data.cache.EasyCache;
import com.github.ScipioAM.scipio_utils_common.data.cache.IEasyCacheS;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @date 2021/9/28
 */
public class EasyCacheTest {

    @Test
    public void test0() {
        IEasyCacheS easyCache = EasyCache.newStrEasyCache();
        easyCache.putData("k0",123);
        easyCache.putData("k1","sdf");
        System.out.println(easyCache);
    }

}
