package net.qoopo.engine.renderer.raster;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import net.qoopo.engine.core.math.QVector3;

public class AdvancedTriangleRasterizer {

    static class Vertex {
        double x, y, z; // Posición en 3D
        double u, v; // Coordenadas UV
        Color color;

        public Vertex(double x, double y, double z, double u, double v, Color color) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.u = u;
            this.v = v;
            this.color = color;
        }
    }

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static BufferedImage canvas = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private static double[][] zBuffer = new double[WIDTH][HEIGHT];

    public static void main(String[] args) {
        // Inicializar el z-buffer
        for (double[] row : zBuffer)
            Arrays.fill(row, Double.NEGATIVE_INFINITY);

        // Crear un mapa de texturas
        BufferedImage texture = createTexture();

        // Definir un triángulo con coordenadas UV
        Vertex v1 = new Vertex(100, 100, 0, 0, 0, Color.WHITE);
        Vertex v2 = new Vertex(300, 400, 0, 1, 1, Color.WHITE);
        Vertex v3 = new Vertex(500, 150, 0, 1, 0, Color.WHITE);

        // Dibujar el triángulo
        drawTriangle(canvas, v1, v2, v3, texture);

        // Mostrar la imagen
        JFrame frame = new JFrame("Advanced Triangle Rasterizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.add(new JLabel(new ImageIcon(canvas)));
        frame.setVisible(true);
    }

    private static void drawTriangle(BufferedImage canvas, Vertex v1, Vertex v2, Vertex v3, BufferedImage texture) {
        // Ordenar vértices por la coordenada y
        if (v1.y > v2.y) {
            Vertex temp = v1;
            v1 = v2;
            v2 = temp;
        }
        if (v2.y > v3.y) {
            Vertex temp = v2;
            v2 = v3;
            v3 = temp;
        }
        if (v1.y > v2.y) {
            Vertex temp = v1;
            v1 = v2;
            v2 = temp;
        }

        // Dibujar triángulos planos
        if (v2.y == v3.y) {
            drawFlatBottomTriangle(canvas, v1, v2, v3, texture);
        } else if (v1.y == v2.y) {
            drawFlatTopTriangle(canvas, v1, v2, v3, texture);
        } else {
            // Dividir el triángulo
            double x4 = v1.x + (v2.y - v1.y) / (v3.y - v1.y) * (v3.x - v1.x);
            double z4 = v1.z + (v2.y - v1.y) / (v3.y - v1.y) * (v3.z - v1.z);
            double u4 = v1.u + (v2.y - v1.y) / (v3.y - v1.y) * (v3.u - v1.u);
            double v4 = v1.v + (v2.y - v1.y) / (v3.y - v1.y) * (v3.v - v1.v);

            Vertex ve4 = new Vertex(x4, v2.y, z4, u4, v4, Color.WHITE);

            drawFlatBottomTriangle(canvas, v1, v2, ve4, texture);
            drawFlatTopTriangle(canvas, v2, ve4, v3, texture);
        }
    }

    private static void drawFlatBottomTriangle(BufferedImage canvas, Vertex v1, Vertex v2, Vertex v3,
            BufferedImage texture) {
        double invSlope1 = (v2.x - v1.x) / (v2.y - v1.y);
        double invSlope2 = (v3.x - v1.x) / (v3.y - v1.y);

        double x1 = v1.x;
        double x2 = v1.x;

        for (int y = (int) v1.y; y <= (int) v2.y; y++) {
            drawScanline(canvas, y, (int) x1, (int) x2, v1, v2, v3, texture);
            x1 += invSlope1;
            x2 += invSlope2;
        }
    }

    private static void drawFlatTopTriangle(BufferedImage canvas, Vertex v1, Vertex v2, Vertex v3,
            BufferedImage texture) {
        double invSlope1 = (v3.x - v1.x) / (v3.y - v1.y);
        double invSlope2 = (v3.x - v2.x) / (v3.y - v2.y);

        double x1 = v3.x;
        double x2 = v3.x;

        for (int y = (int) v3.y; y >= (int) v1.y; y--) {
            drawScanline(canvas, y, (int) x1, (int) x2, v1, v2, v3, texture);
            x1 -= invSlope1;
            x2 -= invSlope2;
        }
    }

    private static void drawScanline(BufferedImage canvas, int y, int x1, int x2, Vertex v1, Vertex v2, Vertex v3,
            BufferedImage texture) {
        if (x1 > x2) {
            int temp = x1;
            x1 = x2;
            x2 = temp;
        }

        for (int x = x1; x <= x2; x++) {
            // Interpolar z
            double z = interpolate(v1.z, v2.z, v3.z, x1, x2, x);

            // Revisar el z-buffer
            if (z > zBuffer[x][y]) {
                zBuffer[x][y] = z;

                // Interpolar UV
                double u = interpolate(v1.u, v2.u, v3.u, x1, x2, x);
                double v = interpolate(v1.v, v2.v, v3.v, x1, x2, x);

                // Obtener color de la textura
                Color textureColor = sampleTexture(texture, u, v);

                // Aplicar iluminación
                Color shadedColor = applyLighting(textureColor,  QVector3.of(0f, 0f, -1.0f));

                // Dibujar píxel
                canvas.setRGB(x, y, shadedColor.getRGB());
            }
        }
    }

    private static Color applyLighting(Color color, QVector3 normal) {
        double ambient = 0.2;
        double diffuse = 0.8;

        QVector3 lightDir = QVector3.of(0, 0, -1).normalize();
        double intensity = Math.max(0, normal.dot(lightDir));

        int r = (int) (color.getRed() * (ambient + diffuse * intensity));
        int g = (int) (color.getGreen() * (ambient + diffuse * intensity));
        int b = (int) (color.getBlue() * (ambient + diffuse * intensity));

        return new Color(clamp(r, 0, 255), clamp(g, 0, 255), clamp(b, 0, 255));
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private static BufferedImage createTexture() {
        BufferedImage texture = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < 256; y++) {
            for (int x = 0; x < 256; x++) {
                texture.setRGB(x, y, new Color(x % 256, y % 256, (x + y) % 256).getRGB());
            }
        }
        return texture;
    }

    private static Color sampleTexture(BufferedImage texture, double u, double v) {
        int texX = (int) (u * (texture.getWidth() - 1));
        int texY = (int) (v * (texture.getHeight() - 1));
        return new Color(texture.getRGB(texX, texY));
    }

    private static double interpolate(double v1, double v2, double v3, int x1, int x2, int x) {
        return v1 + (v3 - v1) * (x - x1) / (double) (x2 - x1);
    }
}
