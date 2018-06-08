import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

import static java.lang.Math.cos;
import static java.lang.Math.toRadians;
import static java.lang.StrictMath.sin;

public class practica extends JFrame {
    private static int vertices;
    private static double[][] coor = new double[1100][1100];
    static Scanner sc = new Scanner(System.in);
    static double[][] vec = new double[1100][1100];

    public static void main(String[] args) {
        double x, y,z;
        char e;
        System.out.println("Deme el numero de puntos de la base:");
        int n = sc.nextInt();

        int inc=360/n, A = 0;
        for (int i = 0; i < n ; i++) {
            x = 50 * Math.cos(Math.toRadians(A)) + 50;
            y = 50 * Math.sin(toRadians(A)) + 50;
            vec[i][0] = x;
            vec[i][1] = y;
            vec[i][2] = 1;
            A += inc;
        }

        x=50;
        y=50;
        z=-200;

        int i = n, N=0;
        while (N<=n){
            vec[i]=vec[N];
            vec[i+1][0]=x;
            vec[i+1][1]=y;
            vec[i+1][2]=z;
            i+=2;
            N++;
        }

        vertices=i-1;

        vec[i][0]=50;
        vec[i][1]=50;
        vec[i][2]=-200;

        practica p = new practica(vec);
        System.out.println("Deme el factor de trsalacion (x y z)");
        x=sc.nextInt();
        y=sc.nextInt();
        z=sc.nextInt();
        p.traslacion(x,y,z);
        System.out.println("Deme el factor de escalacion ");
        x=sc.nextDouble();
        p.escalacion(x);
        System.out.println("Deme el factor de escalacion con centro");
        x=sc.nextDouble();
        p.escalacioncentro(x,x,2-x);
        System.out.println("Deme el angulo de rotacion");
        x=sc.nextDouble();
        System.out.println("Deme el eje de rotacion ('x' 'y' 'z')");
        e=sc.next().charAt(0);
        p.rotacion(360-x ,e);
        System.out.println("Deme el angulo de rotacion con centro");
        x=sc.nextDouble();
        System.out.println("Deme el eje de rotacion ('x' 'y' 'z')");
        e=sc.next().charAt(0);
        p.rotacioncentro(360-x ,e);


    }

    private practica(double[][] vectores) {
        coor = vectores;
        this.setVisible(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle("Original");
        this.setBounds(200, 200, 250, 250);

    }

    public void paint(Graphics g) {
        g.setColor(Color.PINK);
        int[][] p = new int[vertices + 1][2];

        int xcentro = this.getWidth() / 2;
        int ycentro = this.getHeight() / 2;

        for (int i = 0; i < vertices; i++) {
            double alpha = coor[vertices+1][2] / (coor[i][2] + coor[vertices+1][2]);
            p[i][0] = (int) ((alpha * coor[i][0]) + ((1 - alpha) * coor[vertices+1][0]));
            p[i][1] = (int) ((alpha * coor[i][1]) + ((1 - alpha) * coor[vertices+1][1]));
        }

        g.drawLine(this.getWidth() / 2, 0, this.getWidth() / 2, this.getHeight());
        g.drawLine(0, this.getHeight() / 2, this.getWidth(), this.getHeight() / 2);
        g.setColor(Color.RED);
        for (int i = 1; i < vertices; i++) {
            g.drawLine(xcentro + p[i - 1][0], ycentro - p[i - 1][1], xcentro + p[i][0], ycentro - p[i][1]);
        }
        g.drawLine(xcentro + p[0][0], ycentro - p[0][1], xcentro + p[vertices - 1][0], ycentro - p[vertices - 1][1]);
    }

    private void traslacion(double xc, double yc, double zc) {
        double[][] tras = new double[1100][1100];
        for (int i = 0; i < 1100 ; i++) {
            System.arraycopy(vec[i], 0, tras[i], 0, 1100);
        }
        for (int i = 0; i < vertices +1; i++) {
            tras[i][0] = vec[i][0] + xc;
            tras[i][1] = vec[i][1] + yc;
            tras[i][2] = vec[i][2] + zc;
        }
        practica p = new practica(tras);
        p.setTitle("Traslacion xc = " + xc + " yc = " + yc + " zc = " + zc);
        p.setBounds(200, 450, 250, 250);
    }

    private void escalacion(double fac) {
        double[][] tras = new double[1100][1100];
        for (int i = 0; i < 1100 ; i++) {
            System.arraycopy(vec[i], 0, tras[i], 0, 1100);
        }

        for (int i = 0; i < vertices + 1; i++) {
            tras[i][0] *= fac;
            tras[i][1] *= fac;
            tras[i][2] *= fac;
        }

        practica p = new practica(tras);
        p.setTitle("Escalacion por " + fac);
        p.setBounds(450, 200, 250, 250);

    }


    private double min(double... a) {
        double min = a[0];
        for (double i : a)
            min = (i < min) ? i : min;
        return min;
    }

    private double max(double... a) {
        double max = a[0];
        for (double i : a)
            max = (i > max) ? i : max;
        return max;
    }

    private double[] getcentro() {
        double[][] a = new double[vertices + 1][vertices + 1];
        double[] centro = new double[vertices + 1];
        for (int i = 0; i < vertices - 1; i++) {
            for (int j = 0; j < vertices - 1; j++) {
                a[i][j] = vec[j][i];
            }
            centro[i] = (min(a[i]) + max(a[i])) / 2;
        }
        return centro;
    }

    private int escalar(double x, double xc, double sx) {
        return (int) ((x - xc) * sx + xc);
    }

    private void escalacioncentro(double xc, double yc, double zc) {
        double[] centro = getcentro();
        double[][] tras = new double[1100][1100];
        for (int i = 0; i < 1100 ; i++) {
            System.arraycopy(vec[i], 0, tras[i], 0, 1100);
        }
        for (int i = 0; i < vertices; i++) {
            tras[i][0] = escalar(vec[i][0], centro[0], xc);
            tras[i][1] = escalar(vec[i][1], centro[1], yc);
            tras[i][2] = escalar(vec[i][2], centro[2], zc);
        }
        practica p = new practica(tras);
        p.setBounds(450, 450, 250, 250);
        p.setTitle("Escalacion con respecto al centro por " + xc);
    }

    private void rotacion(double angulo, char eje) {
        double[][] tras = new double[1100][1100];
        for (int i = 0; i < 1100 ; i++) {
            System.arraycopy(vec[i], 0, tras[i], 0, 1100);
        }

        double rad = Math.toRadians(angulo);
        practica p = null;
        switch (eje) {
            case 'x':
                for (int i = 0; i < vertices; i++) {
                    tras[i][1] = (int) (vec[i][1] * cos(rad) - vec[i][2] * sin(rad));
                    tras[i][2] = (int) (vec[i][1] * sin(rad) + vec[i][2] * cos(rad));
                }
                p = new practica(tras);
                break;
            case 'y':
                for (int i = 0; i < vertices; i++) {
                    tras[i][0] = (int) (vec[i][0] * cos(rad) - vec[i][2] * sin(rad));
                    tras[i][2] = (int) (vec[i][0] * sin(rad) + vec[i][2] * cos(rad));
                }
                p = new practica(tras);
                break;
            case 'z':
                for (int i = 0; i < vertices + 1; i++) {
                    tras[i][0] = (int) (vec[i][0] * cos(rad) - vec[i][1] * sin(rad));
                    tras[i][1] = (int) (vec[i][0] * sin(rad) + vec[i][1] * cos(rad));
                }
                p = new practica(tras);
                break;
        }
        if (p != null) {
            p.setBounds(700, 200, 250, 250);
            p.setTitle(angulo + " grados sobre el eje " + eje);
        }
    }

    private void rotacioncentro(double angulo, char eje) {
        double[][] tras = new double[1100][1100];
        for (int i = 0; i < 1100 ; i++) {
            System.arraycopy(vec[i], 0, tras[i], 0, 1100);
        }

        double rad = Math.toRadians(angulo);
        double[] centro = getcentro();
        practica p = null;
        switch (eje) {
            case 'x':
                for (int i = 0; i < vertices + 1; i++) {
                    tras[i][1] = (int) ((vec[i][1] - centro[1]) * cos(rad) - (vec[i][2] - centro[2]) * sin(rad));
                    tras[i][1] += centro[1];
                    tras[i][2] = (int) ((vec[i][1] - centro[1]) * sin(rad) + (vec[i][2] - centro[2]) * cos(rad));
                    tras[i][2] += centro[2];
                }
                p = new practica(tras);
                break;
            case 'y':
                for (int i = 0; i < vertices + 1; i++) {
                    tras[i][0] = (int) (vec[i][0] - centro[1]) * cos(rad) - (vec[i][2] - centro[2]) * sin(rad);
                    tras[i][0] += centro[0];
                    tras[i][2] = (int) (vec[i][0] - centro[1]) * sin(rad) + (vec[i][2] - centro[2]) * cos(rad);
                    tras[i][2] += centro[2];

                }
                p = new practica(tras);
                break;
            case 'z':
                for (int i = 0; i < vertices + 1; i++) {
                    tras[i][0] = (int) (vec[i][0] - centro[1]) * cos(rad) - (vec[i][1] - centro[2]) * sin(rad);
                    tras[i][0]+=centro[0];
                    tras[i][1] = (int) (vec[i][0] - centro[1]) * sin(rad) + (vec[i][1] - centro[2]) * cos(rad);
                    tras[i][1]+=centro[1];
                }
                p = new practica(tras);
                break;
        }
        if (p != null) {
            p.setBounds(700, 450, 250, 250);
            p.setTitle("rotacion respecto al centro de " + angulo + " grados sobre el eje " + eje);
        }
    }
}
