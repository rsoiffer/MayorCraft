package noise;

public class Noise {

    public double seed;

    public Noise(double seed) {
        this.seed = seed;
    }

    public double fbm(double x, double y, int octaves, double frequency) {
        return multi(x, y, octaves, frequency) + multi(x, y, octaves, frequency * 20) / 20;
    }

    public double fbmRidged(double x, double y, int octaves, double frequency) {
        return ridged(x, y, octaves, frequency) + ridged(x, y, octaves, frequency * 20) / 20;
    }

    public double multi(double x, double y, int octaves, double frequency) {
        double r = 0;
        for (int i = 0; i < octaves; i++) {
            r += perlin(x * Math.pow(2, i) * frequency, y * Math.pow(2, i) * frequency) / Math.pow(2, i);
        }
        return r;
    }

    public double offset(double x, double y, int octaves, double frequency, double offset) {
        double r = 0;
        for (int i = 0; i < octaves; i++) {
            r += SimplexNoise.noise(x * Math.pow(2, i) * frequency, y * Math.pow(2, i) * frequency, seed + offset) / Math.pow(2, i);
        }
        return r;
    }

    private double perlin(double x, double y) {
        return SimplexNoise.noise(x, y, seed);
    }

    public double random(double x, double y) {
        return multi(x, y, 1, 1000000);
    }

    public double ridged(double x, double y, int octaves, double frequency) {
        return 1 - 2 * Math.abs(multi(x, y, octaves, frequency));
    }
}
