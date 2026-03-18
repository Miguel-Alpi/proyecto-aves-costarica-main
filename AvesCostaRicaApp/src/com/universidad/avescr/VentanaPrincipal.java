package com.universidad.avescr;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import javax.imageio.ImageIO;

public class VentanaPrincipal extends JFrame {
    // --- Componentes de la Interfaz ---
    private JTextField campoNombre;
    private JButton botonBuscarProlog, botonCargarImagen;
    private JTextArea areaResultados;
    private JLabel etiquetaImagen;

    public VentanaPrincipal() {
        setTitle("Sistema de Búsqueda de Aves de Costa Rica");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // --- Panel de Búsqueda (Norte) ---
        JPanel panelBusqueda = new JPanel(new GridLayout(2, 2, 6, 6));
        panelBusqueda.setBorder(BorderFactory.createTitledBorder("Criterio de búsqueda"));
        panelBusqueda.add(new JLabel("Nombre del ave:"));
        campoNombre = new JTextField();
        panelBusqueda.add(campoNombre);

        botonBuscarProlog = new JButton("Buscar en Base de Conocimiento");
        panelBusqueda.add(new JLabel()); // relleno para cuadrar 2x2
        panelBusqueda.add(botonBuscarProlog);

        // --- Panel de Imagen (Centro) ---
        JPanel panelImagen = new JPanel(new BorderLayout());
        panelImagen.setBorder(BorderFactory.createTitledBorder("Imagen (detección de color principal)"));
        etiquetaImagen = new JLabel("Cargue una imagen...", SwingConstants.CENTER);
        etiquetaImagen.setPreferredSize(new Dimension(560, 420));
        botonCargarImagen = new JButton("Cargar Imagen");
        panelImagen.add(etiquetaImagen, BorderLayout.CENTER);
        panelImagen.add(botonCargarImagen, BorderLayout.SOUTH);

        // --- Panel de Resultados (Este) ---
        areaResultados = new JTextArea("Resultados aparecerán aquí...");
        areaResultados.setEditable(false);
        areaResultados.setLineWrap(true);        // <-- ajuste de línea
        areaResultados.setWrapStyleWord(true);   // <-- no corta palabras
        areaResultados.setFont(new Font("Consolas", Font.PLAIN, 13));

        JScrollPane scrollResultados = new JScrollPane(areaResultados);
        scrollResultados.setPreferredSize(new Dimension(280, 0));
        scrollResultados.setBorder(BorderFactory.createTitledBorder("Resultados"));

        // --- Añadir paneles a la ventana ---
        add(panelBusqueda, BorderLayout.NORTH);
        add(panelImagen, BorderLayout.CENTER);
        add(scrollResultados, BorderLayout.EAST);

        // --- Acciones de botones ---
        botonBuscarProlog.addActionListener(e -> buscarPorNombreAction());
        botonCargarImagen.addActionListener(e -> cargarImagenAction());

        // --- Prolog listo ---
        PrologConnector.inicializarProlog();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    /** Busca en Prolog SOLO por nombre y muestra color/alimento/hábitat devueltos. */
    private void buscarPorNombreAction() {
        String nombre = campoNombre.getText().trim().toLowerCase();
        areaResultados.setText("");

        if (nombre.isBlank()) {
            areaResultados.append("Por favor, ingrese el nombre del ave.\n");
            return;
        }

        List<PrologConnector.AveResultado> resultados =
                PrologConnector.buscarPorNombre(nombre);

        if (resultados.isEmpty()) {
            areaResultados.append("No se encontraron aves con ese nombre.\n");
        } else {
            areaResultados.append("Resultados:\n\n");
            for (PrologConnector.AveResultado r : resultados) {
                areaResultados.append(r.toString());
                areaResultados.append("\n\n");
            }
        }
    }

    /** Carga imagen y detecta su color principal analizando píxeles (Java puro). */
    private void cargarImagenAction() {
        JFileChooser selector = new JFileChooser();
        int opcion = selector.showOpenDialog(this);
        if (opcion == JFileChooser.APPROVE_OPTION) {
            File archivo = selector.getSelectedFile();

            // Mostrar la imagen escalada en el label
            ImageIcon icon = new ImageIcon(archivo.getAbsolutePath());
            Image imgEscalada = icon.getImage().getScaledInstance(
                    etiquetaImagen.getWidth(),
                    etiquetaImagen.getHeight(),
                    Image.SCALE_SMOOTH
            );
            etiquetaImagen.setIcon(new ImageIcon(imgEscalada));
            etiquetaImagen.setText("");

            // Detectar color principal
            try {
                BufferedImage bi = ImageIO.read(archivo);
                String colorDominante = detectarColorPrincipal(bi);
                JOptionPane.showMessageDialog(this,
                        "Color principal detectado: " + colorDominante,
                        "Detección de color",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "No se pudo analizar la imagen: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Detecta un color "principal" mapeándolo a categorías (rojo, verde, azul, amarillo,
     * anaranjado, café, negro, blanco, gris). Muestrea y clasifica en HSV.
     */
    private String detectarColorPrincipal(BufferedImage img) {
        int stepX = Math.max(1, img.getWidth()  / 200);
        int stepY = Math.max(1, img.getHeight() / 200);

        int rojo=0, verde=0, azul=0, amarillo=0, naranja=0, cafe=0, negro=0, blanco=0, gris=0;

        for (int y = 0; y < img.getHeight(); y += stepY) {
            for (int x = 0; x < img.getWidth(); x += stepX) {
                int rgb = img.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8)  & 0xFF;
                int b = (rgb)       & 0xFF;

                float[] hsv = Color.RGBtoHSB(r, g, b, null);
                float h = hsv[0] * 360f; // 0..360
                float s = hsv[1];        // 0..1
                float v = hsv[2];        // 0..1

                if (v > 0.92 && s < 0.10) { blanco++; continue; }
                if (v < 0.15) { negro++; continue; }
                if (s < 0.12) { gris++; continue; }

                if (h < 15 || h >= 345) rojo++;
                else if (h < 40)  naranja++;
                else if (h < 70)  amarillo++;
                else if (h < 170) verde++;
                else if (h < 255) azul++;
                else if (h < 345) cafe++;
            }
        }

        String[] nombres = {"rojo","verde","azul","amarillo","anaranjado","café","negro","blanco","gris"};
        int[] valores     = { rojo,  verde,  azul,  amarillo,  naranja,   cafe,  negro,  blanco,  gris };

        int max = -1; String ganador = "indefinido";
        for (int i = 0; i < valores.length; i++) {
            if (valores[i] > max) { max = valores[i]; ganador = nombres[i]; }
        }
        return ganador;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(VentanaPrincipal::new);
    }
}
