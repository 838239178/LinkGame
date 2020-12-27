import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameMap implements GameRules{
    private int mapSize;
    protected Point firstConner;
    protected Point secondConner;
    protected int kkk = 1;
    int[][] map = new int[mapSize + 2][mapSize + 2];


    public GameMap(int mapSize) {
        this.mapSize = mapSize;
    }

    public int getMapSize() {
        return mapSize;
    }

    public void setMapSize(int mapSize) {
        this.mapSize = mapSize;
    }

    //判断是否连通的方法，并记录所以拐点，返回LinkResult
    public LinkRuselt isConnex(int[][] map, Point firstPoint, Point secondPoint) {
        LinkRuselt linkRuselt = new LinkRuselt();
        if(isStraightLink(map, firstPoint, secondPoint)) {
            linkRuselt.setFirstPoint(firstPoint);
            linkRuselt.setSecondPoint(secondPoint);
            linkRuselt.setLinkType(LinkType.STRAIGHT_LINK);
            return linkRuselt;
        }
        if(isSingleConner(map, firstPoint, secondPoint)) {
            linkRuselt.setFirstPoint(firstPoint);
            linkRuselt.setSecondPoint(secondPoint);
            linkRuselt.setFirstCorner(firstConner);
            linkRuselt.setLinkType(LinkType.SINGLECORNER_LINK);
            return linkRuselt;
        }
        if(isDoubleConner(map, firstPoint, secondPoint)) {
            linkRuselt.setFirstPoint(firstPoint);
            linkRuselt.setSecondPoint(secondPoint);
            linkRuselt.setFirstCorner(firstConner);
            linkRuselt.setSecondCorner(secondConner);
            linkRuselt.setLinkType(LinkType.DOUBLECORNER_LINK);
            return linkRuselt;
        }
        return linkRuselt;
    }

    //获取地图数据（二维数组中的值），返回方块ID
    public int returnID(int[][] map, Point point){
        return map[point.getx()][point.gety()];
    }

    //清空指定两个点的方法
    public void remove(int[][] map, Point firstPoint, Point secondPoint) {
        LinkRuselt linkRuselt = isConnex(map, firstPoint, secondPoint);
        if(linkRuselt.getLinkType() != LinkType.NO_LINK) {
            map[firstPoint.getx()][firstPoint.gety()] = 0;
            map[secondPoint.getx()][secondPoint.gety()] = 0;
        }
    }

    //重新随机地图的方法
    public void changeMap(int[][] map){
        List list = new ArrayList();
        int k = 0;
        //循环找到所有不为0的点，并把它们放入list中
        for (int i = 0; i < map.length; i++) {
            for(int j = 0; j < map[i].length; j++) {
                if (map[i][j] != 0) {
                    list.add(map[i][j]);
                }
            }
        }
        //把放入的循序打乱
        Collections.shuffle(list);
        //覆盖掉原来不为0的位置
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j <map[i].length; j++) {
                if (map[i][j] != 0) {
                    map[i][j] = (int)list.get(k);
                    k++;
                }
            }
        }
    }

    //自动寻找两个可相消的点。成功返回LinkResult，失败抛出RuntimeException
    public LinkRuselt autoConnex(int[][] map){
        int n = 1;
        kkk--;
        LinkRuselt linkRuselt = new LinkRuselt();
        while(n <= kkk) {
            int k = 1;
            int i1 = 0;
            int j1 = 0;
            Point firstPoint = new Point();
            Point secondPoint = new Point();
            //共kkk组图片，所以先找到第一组图片的第一张图片所在的点
            for (int i = 1; i < map.length - 1 && k == 1; i++) {
                for(int j = 1; j < map[i].length - 1; j++) {
                    if (map[i][j] == n) {
                        i1 = i;
                        j1 = j;
                        k = 0;
                        firstPoint.setX(i);
                        firstPoint.setY(j);
                        break;
                    }
                }
            }
            //找到第一个ID为1的点后，去找它之后所以ID为1的点，并分别进行连通性判断
            for (int i = i1 ; i < map.length - 1; i++) {
                for (int j = j1 + 1; j < map[i].length - 1; j++) {
                    if (map[i][j] == n) {
                        secondPoint.setX(i);
                        secondPoint.setY(j);
                        linkRuselt = isConnex(map, firstPoint, secondPoint);
                        //若连通返回linkRuselt
                        if(linkRuselt.getLinkType() != LinkType.NO_LINK) return linkRuselt;
                        continue;
                    }
                }
            }
            //找不到n就加1，找下一组组图片
            n++;
        }
        //找不到也返回linkRuselt
        return linkRuselt;
    }

    //判断是否直接连通,用于isConnex
    public boolean isStraightLink(int[][] map, Point firstPoint, Point secondPoint) {
        //两个点id不相同，直接返回false
        if(map[firstPoint.getx()][firstPoint.gety()] != map[secondPoint.getx()][secondPoint.gety()] && map[firstPoint.getx()][firstPoint.gety()] != 0 && map[secondPoint.getx()][secondPoint.gety()] != 0) return false;
        //当两个点在同一行的时候，就去判断列这一路是否连通
        if(firstPoint.getx() == secondPoint.getx()) {
            //判断两个点谁在前面
            if(firstPoint.gety() < secondPoint.gety()) {
                int x = firstPoint.getx();
                int y = firstPoint.gety();
                while(map[x][y + 1] == 0 && y != secondPoint.gety() - 1) {
                    y++;
                }
                if(y == secondPoint.gety() - 1) return true;
                return false;
            }else if(firstPoint.gety() > secondPoint.gety()){
                int x = secondPoint.getx();
                int y = secondPoint.gety();
                while (map[x][y + 1] == 0 && y != firstPoint.gety() - 1) {
                    y++;
                }
                if(y == firstPoint.gety() - 1) return true;
                return false;
            }
            //当两个点在同一列时，判断行所在的那一路是否连通
        }else if(firstPoint.gety() == secondPoint.gety()) {
            //判断两个点谁在上面
            if(firstPoint.getx() < secondPoint.getx()) {
                int y = firstPoint.gety();
                int x = firstPoint.getx();
                while (map[x + 1][y] == 0 && x != secondPoint.getx() - 1) {
                    x++;
                }
                if(x == secondPoint.getx() - 1) return true;
                return false;
            }else if(firstPoint.getx() > secondPoint.getx()){
                int y = firstPoint.gety();
                int x = secondPoint.getx();
                while(map[x + 1][y] == 0 && x != firstPoint.getx() - 1) {
                    x++;
                }
                if(x == firstPoint.getx() - 1) return true;
                return false;
            }
        }
        return false;
    }

    public boolean isSingleConner(int[][] map, Point firstPoint, Point secondPoint) {
        //如果是单拐点连通的话，两个可能的拐点一定与这两个点同行或者同列
        Point mayConner1 = new Point();
        Point mayConner2 = new Point();
        mayConner1.setX(firstPoint.getx());
        mayConner1.setY(secondPoint.gety());
        mayConner2.setX(secondPoint.getx());
        mayConner2.setY(firstPoint.gety());
        //两个点id不相同，直接返回false
        if(map[firstPoint.getx()][firstPoint.gety()] != map[secondPoint.getx()][secondPoint.gety()] && map[firstPoint.getx()][firstPoint.gety()] != 0 && map[secondPoint.getx()][secondPoint.gety()] != 0) return false;
        //如果两个拐点均不为0，则不可能单拐点连通，返回false
        if(map[mayConner1.getx()][mayConner1.gety()] !=0 && map[mayConner2.getx()][mayConner2.gety()] != 0) return false;
        //判断可能的拐点1是否与两个点都连通
        if(isStraightLink(map, firstPoint, mayConner1)) {
            if(isStraightLink(map, secondPoint, mayConner1)) {
                firstConner = mayConner1;
                return true;
            }
        }
        //判断可能的拐点2是否与两个点都连通
        if(isStraightLink(map, firstPoint, mayConner2)) {
            if(isStraightLink(map, secondPoint, mayConner2)) {
                firstConner = mayConner2;
                return true;
            }
        }
        //都不连通返回false
        return false;
    }

    public boolean isDoubleConner(int[][] map, Point firstPoint, Point secondPoint) {
        //两个点id不相同，直接返回false
        if(map[firstPoint.getx()][firstPoint.gety()] != map[secondPoint.getx()][secondPoint.gety()] && map[firstPoint.getx()][firstPoint.gety()] != 0 && map[secondPoint.getx()][secondPoint.gety()] != 0) return false;
        //循环找与第一个点在同一行的点中是否有某个点，它分别与第一个点直接连通又与第二个点单拐点连通，有返回true
        for (int i = 0; i < map[0].length; i++) {
            Point mayConner = new Point();
            mayConner.setX(firstPoint.getx());
            mayConner.setY(i);
            if(map[mayConner.getx()][mayConner.gety()] == 0 && isStraightLink(map, mayConner, firstPoint) && isSingleConner(map, mayConner, secondPoint)) {
                //此时第二个拐点被第一个拐点所记录
                secondConner = firstConner;
                //再更新第一个拐点为真正的第一个拐点
                firstConner = mayConner;
                return true;
            }
        }
        //循环找与第一个点在同一列的点中是否有某个点，它分别与第一个点直接连通又与第二个点单拐点连通，有返回true
        for(int i = 0; i < map.length; i++) {
            Point mayConner = new Point();
            mayConner.setX(i);
            mayConner.setY(firstPoint.gety());
            if(map[mayConner.getx()][mayConner.gety()] == 0 && isStraightLink(map, mayConner, firstPoint) && isSingleConner(map, mayConner, secondPoint)) {
                //此时第二个拐点被第一个拐点所记录
                secondConner = firstConner;
                //再更新第一个拐点为真正的第一个拐点
                firstConner = mayConner;
                return  true;
            }
        }
        return false;
    }

    //初始化地图
    public void initMap() {
        int numI = mapSize / 2;
        int num = mapSize * mapSize / numI;
        int num1 = 0;
        int x = 0;
        List list = new ArrayList();
        //先按顺序初始化地图
        for (int i = 1; i < map.length - 1; i++) {
            for (int j = 1; j < map[i].length - 1; j++) {
                map[i][j] = kkk;
                num1++;
                if(num != 0 && num1 % num == 0) kkk++;
            }
        }
        //把有序的地图数据放入list
        for (int i = 1; i < map.length - 1; i++) {
            for (int j = 1; j < map[i].length - 1; j++) {
                list.add(map[i][j]);
            }
        }
        //打乱list
        Collections.shuffle(list);
        //放回地图，随机数据成功
        for (int i = 1; i < map.length - 1; i++) {
            for (int j = 1; j < map[i].length - 1; j++) {
                map[i][j] = (int) list.get(x);
                x++;
            }
        }
    }
}
