package utility;

import vo.AddressVO;

import java.util.Comparator;

/**
 * Created by 95016056 on 2017-06-02.
 */

public class VectorSort implements Comparator<AddressVO> {
    @Override
    public int compare(AddressVO o1, AddressVO o2) {
        return ((AddressVO)o1).getDist() > ((AddressVO)o2).getDist() ? 1 : 0;
    }
}