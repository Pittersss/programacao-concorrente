import java.io.File;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class RichardsonLucy extends Thread {
    private float[][] image;
    private float[][] psf;
    private float[][] psfFlipped;
    private float[][] resultado;
    private int iterations;

    public RichardsonLucy(float[][] image, float[][] psf, float[][] psfFlipped, int iterations) {
        this.image = image;
        this.psf = psf;
        this.psfFlipped = psfFlipped;
        this.iterations = iterations;
    }

    @Override
    public void run() {
        richardsonLucyMirror(image, psf, psfFlipped, iterations);
    }

    public float[][] getResultado() {
        return this.resultado;
    }

    public void richardsonLucyMirror(float[][] image, float[][] psf, float[][] psfFlipped, int iterations) {
        this.resultado = richardsonLucy(image, psf, psfFlipped, iterations);
    }

    // Richardson-Lucy Iterativo
    public static float[][] richardsonLucy(float[][] image, float[][] psf, float[][] psfFlipped, int iterations) {
        int h = image.length;
        int w = image[0].length;
        float[][] estimate = new float[h][w];

        // Inicializa com valor constante (pode ser a imagem borrada)
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++)
                estimate[y][x] = 0.5f;

        for (int it = 0; it < iterations; it++) {
            float[][] estimateBlurred = convolve(estimate, psf);
            float[][] ratio = new float[h][w];

            for (int y = 0; y < h; y++)
                for (int x = 0; x < w; x++) {
                    float eb = estimateBlurred[y][x];
                    ratio[y][x] = (eb > 1e-6f) ? image[y][x] / eb : 0f;
                }

            float[][] correction = convolve(ratio, psfFlipped);

            for (int y = 0; y < h; y++)
                for (int x = 0; x < w; x++)
                    estimate[y][x] *= correction[y][x];
        }

        return estimate;
    }

    // Convolução 2D
    public static float[][] convolve(float[][] image, float[][] kernel) {
        int h = image.length, w = image[0].length;
        int kh = kernel.length, kw = kernel[0].length;
        int kyc = kh / 2, kxc = kw / 2;

        float[][] result = new float[h][w];

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                float sum = 0f;
                for (int ky = 0; ky < kh; ky++) {
                    for (int kx = 0; kx < kw; kx++) {
                        int iy = y + ky - kyc;
                        int ix = x + kx - kxc;
                        if (iy >= 0 && iy < h && ix >= 0 && ix < w) {
                            sum += image[iy][ix] * kernel[ky][kx];
                        }
                    }
                }
                result[y][x] = sum;
            }
        }
        return result;
    }

    // Espelha o kernel horizontal e verticalmente
    public static float[][] invertKernel(float[][] kernel) {
        int h = kernel.length, w = kernel[0].length;
        float[][] result = new float[h][w];
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++)
                result[y][x] = kernel[h - y - 1][w - x - 1];
        return result;
    }

    // Kernel Gaussiano normalizado
    public static float[][] gaussianKernel(int size, float sigma) {
        float[][] kernel = new float[size][size];
        float mean = size / 2f;
        float sum = 0f;

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                float val = (float) Math.exp(-0.5 * (Math.pow((x - mean) / sigma, 2) +
                        Math.pow((y - mean) / sigma, 2)));
                kernel[y][x] = val;
                sum += val;
            }
        }

        for (int y = 0; y < size; y++)
            for (int x = 0; x < size; x++)
                kernel[y][x] /= sum;

        return kernel;
    }

    // Salva a imagem RGB em PNG
    public static void saveColorImage(float[][] r, float[][] g, float[][] b, String filename) throws Exception {
        int h = r.length, w = r[0].length;
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int red = clampToByte(r[y][x] * 255f);
                int green = clampToByte(g[y][x] * 255f);
                int blue = clampToByte(b[y][x] * 255f);
                Color color = new Color(red, green, blue);
                out.setRGB(x, y, color.getRGB());
            }
        }

        ImageIO.write(out, "png", new File(filename));
    }

    private static int clampToByte(float val) {
        return Math.min(255, Math.max(0, Math.round(val)));
    }
}