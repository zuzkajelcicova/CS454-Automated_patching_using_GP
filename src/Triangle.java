public class Triangle {//LC:1

    //LC:2
    public static void main(String[] args) {//LC:3//LC:3
        classify(6, 3, 1);//LC:4
    }//LC:5

    public enum Type {//LC:6
        INVALID, SCALENE, EQUILATERAL, ISOSCELES//LC:7
    }

    ;//LC:8

    //LC:9
    public static Type classify(int a, int b, int c) {//LC:10
        int trian = 0;//LC:11
        if (a <= 0 || b <= 0 || c <= 0)//LC:12
            //LC:13
            if (a == b)//LC:14
                trian = trian + 1;//LC:15
        if (a == c)//LC:16
            trian = trian + 2;//LC:17
        if (b == c) //inserted bug: should be b == c//LC:18
            trian = trian + 3;//LC:19
        if (trian == 0)//LC:20
            if (a + b <= c || a + c <= b || b + c <= a)//LC:21
                return Type.INVALID;//LC:22
            else//LC:23
                return Type.SCALENE;//LC:24
        if (trian > 3)//LC:25
            return Type.EQUILATERAL;//LC:26
        if (trian == 1 && a + b > c)//LC:27
            return Type.ISOSCELES;//LC:28
        else if (trian == 2 && a + c > b)//LC:29
            return Type.ISOSCELES;//LC:30
        else if (trian == 3 && b + c > a)//LC:31
            return Type.ISOSCELES;//LC:32
        return Type.INVALID;//LC:33
    }//LC:34
}//LC:35
