import com.github.ScipioAM.scipio_utils_common.data.mount.CacheField;
import com.github.ScipioAM.scipio_utils_common.data.mount.MountedCache;
import com.github.ScipioAM.scipio_utils_common.data.mount.MountedCacheWrapper;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @date 2021/9/30
 */
public class MountedCacheTest {

    @Test
    public void test0() {
        TestBean testBean = new TestBean();
        testBean.setList("qwe");

        MountedCacheWrapper wrapper = new MountedCacheWrapper(testBean);
        try {
            wrapper.putData("zxc");
            System.out.println("putData succeed!");
            String data = wrapper.getData(1);
            System.out.println("getData: " + data);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class TestBean implements MountedCache {
        @CacheField
        private List<String> list;

        public List<String> getList() {
            return list;
        }

        public void setList(List<String> list) {
            this.list = list;
        }

        public void setList(String data) {
            if(this.list == null) {
                this.list = new ArrayList<>();
            }
            this.list.add(data);
        }
    }

}
