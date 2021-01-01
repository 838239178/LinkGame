package gm.game;

import java.util.Iterator;

/**
 * @author 施嘉宏
 */
public interface GameMapItr extends Iterator<Integer> {
    @Override
    boolean hasNext();

    @Override
    Integer next();

    //返回最后一次遍历的元素对应的下标
    Point lastIndex();
}
