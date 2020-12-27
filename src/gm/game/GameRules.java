public interface GameRules {
    //判断是否连通的方法，并记录所以拐点，返回LinkResult
    public LinkRuselt isConnex(int[][] map, Point firstPoint, Point secondPoint);

    //获取地图数据（二维数组中的值），返回方块ID
    public int returnID(int[][] map, Point point);

    //清空指定两个点的方法
    public void remove(int[][] map, Point firstPoint, Point secondPoint);

    //判断并清空两个点的方法，返回布尔类型
    //public boolean isRemove(int[][] map, Point firstPoint, Point secondPoint);

    //重新随机地图的方法
    public void changeMap(int[][] map);

    //自动寻找两个可相消的点。成功返回LinkResult，失败抛出RuntimeException
    public LinkRuselt autoConnex(int[][] map);
}
