package bhe.net.cn.utils;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum SortUtils {
    ;
    static Logger LOGGER = LoggerFactory.getLogger(SortUtils.class);

    /**
     *
     * @param elements
     * @param fieldName
     * @param order
     *            <ul>
     *            <li>1: ASC
     *            <li>-1: DESC
     *            </ul>
     * @return
     * @throws Exception
     */
    public static <X> List<X> byField(List<X> elements, String fieldName, int order) {
        for (int i = 0; i < elements.size() - 1; i++) {
            boolean discontinue = true;
            for (int j = 0; j < elements.size() - i - 1; j++) {
                boolean swap = false;
                X former = elements.get(j);
                X later = elements.get(j + 1);
                Object compareVal_former = BeanUtils.getFieldValue(former, fieldName);
                Object compareVal_later = BeanUtils.getFieldValue(later, fieldName);
                if (order == 1) {
                    if (compare(compareVal_former, compareVal_later) > 0) {
                        swap = true;
                    }
                } else if (order == -1) {
                    if (compare(compareVal_former, compareVal_later) < 0) {
                        swap = true;
                    }
                }
                if (swap) {
                    X temp = former;
                    elements.set(j, later);
                    elements.set(j + 1, temp);
                    discontinue = false;
                }
            }
            if (discontinue) {
                break;
            }
        }
        return elements;
    }

    private static int compare(Object former, Object later) {
        if (former instanceof Integer) {
            Integer f = (Integer) former;
            Integer l = (Integer) later;
            return f.equals(l) ? 0 : f > l ? 1 : -1;
        } else if (former instanceof Long) {
            Long f = (Long) former;
            Long l = (Long) later;
            return f.equals(l) ? 0 : f > l ? 1 : -1;
        } else if (former instanceof Float) {
            Float f = (Float) former;
            Float l = (Float) later;
            return f.equals(l) ? 0 : f > l ? 1 : -1;
        } else if (former instanceof Double) {
            Double f = (Double) former;
            Double l = (Double) later;
            return f.equals(l) ? 0 : f > l ? 1 : -1;
        } else if (former instanceof String) {
            String f = (String) former;
            String l = (String) later;
            if (f.equals(l)) {
                return 0;
            }
            return f.compareTo(l) > 0 ? 1 : -1;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static void main(String[] args) throws Exception {
        List<Test> tests = new ArrayList<Test>();
        tests.add(new Test("aaa", 20));
        tests.add(new Test("g", -1));
        tests.add(new Test("aa", 20));
        System.out.println(byField(tests, "integer", 1));
    }
}

class Test {

    private String string;
    private Integer integer;

    public Test(String string, Integer integer) {
        this.string = string;
        this.integer = integer;
    }

    public String getString() {
        return string;
    }

    public Integer getInteger() {
        return integer;
    }

    public void setString(String string) {
        this.string = string;
    }

    public void setInteger(Integer integer) {
        this.integer = integer;
    }

    @Override
    public String toString() {
        return string + " " + integer;
    }

}