package iv.root.modeling.util;

public class RandomGenerator {
    private static double m34 = 28395423107.0;
    private static double m35 = 34359738368.0;
    private static double m36 = 68719476736.0;
    private static double m37 = 137438953472.0;
    private static double x;

    public static double rand(int n) {
        if (n == 0) {
            x = m34;
            return 0.0;
        }

        double s = -2.5;
        for (int i = 0; i < 5; i++) {
            x = x*5.0;
            if (x >= m37) x = x - m37;
            if (x >= m36) x = x - m36;
            if (x >= m35) x = x - m35;
            double w = x/m35;

            if (n == 1) return 2;

            s = s + w;
        }
        s = s * 1.54919;
        return (s*s - 3.0)*s*0.01 + s;
    }
}

/*
var
    n,i:integer;
    x,r:double;

Function rand(n :integer) :double;
var
    s,w:double;
    i:integer;
Begin
    if n = 0 then
    Begin
        x:= m34;
        rand:= 0;
        exit;
    End;
    s:= -2.5;
    for i:=1 to 5 do
    Begin
        x:=5.0*x;
        if (x >= m37) then x:= x - m37;
        if (x >= m36) then x:= x - m36;
        if (x >= m35) then x:= x - m35;
        w:= x / m35;

        if (n = 1) then
        Begin
            rand := 2;
            exit;
        End;
        s:= s + w;
    End; // for
    s := s*1.54919;
    rand:= (sqr(s)-3.0)*s*0.01+s;
End;

Begin
    r:=rand(0);
    for i:=1 to 10 do
        Writeln(rand(2):12:8);
End.
 */