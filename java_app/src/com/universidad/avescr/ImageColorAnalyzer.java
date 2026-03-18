package com.universidad.avescr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class ImageColorAnalyzer {
    public static String analizarColorDeImagen(BufferedImage imagen) {
        Map<String, Integer> colorCount = new HashMap<>();
        colorCount.put("rojo", 0);
        colorCount.put("verde", 0);
        colorCount.put("azul", 0);
        colorCount.put("amarillo", 0);
        colorCount.put("negro", 0);
        colorCount.put("blanco", 0);
        colorCount.put("marron", 0);

        int width = imagen.getWidth();
        int height = imagen.getHeight();

        for (int x = 0; x < width; x += 2) {
            for (int y = 0; y < height; y += 2) {
                Color color = new Color(imagen.getRGB(x, y));
                String nombreColor = obtenerNombreColor(color);
                if (colorCount.containsKey(nombreColor)) {
                    colorCount.put(nombreColor, colorCount.get(nombreColor) + 1);
                }
            }
        }

        String colorDominante = "desconocido";
        int max = 0;
        for (Map.Entry<String, Integer> entry : colorCount.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                colorDominante = entry.getKey();
            }
        }
        return colorDominante;
    }

    private static String obtenerNombreColor(Color color) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();

        if (r > 200 && g > 200 && b > 200) return "blanco";
        if (r < 50 && g < 50 && b < 50) return "negro";
        if (Math.abs(r - g) < 20 && Math.abs(g - b) < 20 && r > 50 && r < 200) return "gris";
        if (r > 200 && g > 200 && b < 100) return "amarillo";
        if (r > 200 && g > 100 && g < 200 && b < 100) return "naranja";
        if (r > 200 && b > 200 && g < 100) return "rosa";
        if (r > 150 && g > 100 && b < 80 && r > g && g > b) return "marron";
        if (g > 200 && r < 100 && b < 100) return "verde";
        if (g > 200 && b > 200 && r < 100) return "celeste";
        if (b > 200 && r < 100 && g < 100) return "azul";
        if (b > 150 && r > 100 && g < 100) return "violeta";
        if (r > 200 && g < 100 && b > 200) return "magenta";
        if (r > g && r > b) return "rojo";
        if (g > r && g > b) return "verde";
        if (b > r && b > g) return "azul";
        return "desconocido";
    }

    // --- Método main para probar directamente ---
    public static void main(String[] args) {
        try {
            File archivoImagen = new File("com/universidad/avescr/ejemplo.jpeg");
            BufferedImage imagen = ImageIO.read(archivoImagen);

            String colorDominante = analizarColorDeImagen(imagen);
            System.out.println("Color dominante: " + colorDominante);
        } catch (Exception e) {
            System.out.println("Error al analizar la imagen: " + e.getMessage());
            e.printStackTrace();
        }
    }
}