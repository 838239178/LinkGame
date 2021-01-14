package useless;

import gm.game.*;

import java.util.*;

/**
 * 尝试用广搜法重写autoConnex方法。
 *
 * @deprecated 其中重写的方法效率较低，因此弃用改类，请使用父类{@link GameMap}替代。
 */
public class GMapModify extends GameMap {

    private final int[][] dir = {
            {0, 1,},
            {0, -1,},
            {1, 0,},
            {-1, 0,},
    };

    /**
     * @deprecated 其中重写的方法效率较低，因此弃用改类，请使用父类{@link GameMap}替代。
     */
    public GMapModify(int mapSize) {
        super(mapSize);
    }

    private LinkResult searchBFS(Point begin, final Map<Point, Boolean> noLinkMap) {
        Queue<Point> queue = new LinkedList<>();
        Map<Point, Boolean> isVisited = new HashMap<>();
        queue.add(begin);
        isVisited.put(begin, true);
        while (!queue.isEmpty()) {
            Point now = queue.poll();

            if(now != begin && !noLinkMap.getOrDefault(now, false) && returnID(now) == returnID(begin)) {
                LinkResult res = isConnex(begin, now);
                if (res.getLinkType() != LinkType.NO_LINK) {
                    return res;
                }
            }

            for (int d = 0; d < 4; d++) {
                Point next = new Point(now.x, now.y);
                next.translate(dir[d][0],dir[d][1]);
                if(!isVisited.getOrDefault(next, false)){
                    if(next.x >=1 && next.x<=mapSize && next.y>=1 && next.y<=mapSize){
                        queue.add(next);
                        isVisited.put(next, true);
                    }
                }
            }
        }
        return null;
    }


    /**
     * 已弃用
     *
     * 广搜版，经测试，三个难度下的平均搜索时间均超出ID启发式搜索的两倍！！！
     * 原因：广搜版因为广度优先的原因，ID相同的点并不会优先进行访问，而是向其他路径扩张，导致无用次数增多。
     * @return LinkResult
     * @deprecated 效率较低
     */
    @Override
    public LinkResult autoConnex() {
        //test
        //long t = System.nanoTime();
        Map<Point, Boolean> isNoLink = new HashMap<>();
        GameMapItr it = super.Iterator();
        while (it.hasNext()){
            if(it.next() == BLANK_BLOCK) continue;
            Point p = it.lastIndex();
            LinkResult res = searchBFS(p, isNoLink);
            if(res != null) {
                //test
                //System.out.println((System.nanoTime() - t)/1000000.0);
                return res;
            }
            else{
                isNoLink.put(p, true);
            }
        }
        throw new NoSuchElementException("couldn't find result");
    }
}
