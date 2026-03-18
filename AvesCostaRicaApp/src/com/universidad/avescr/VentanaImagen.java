package com.universidad.avescr;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class VentanaImagen extends JFrame {
    private JLabel imagenLabel;
    private JButton botonCargar;

    public VentanaImagen() {
        setTitle("Carga de Imagen de Ave");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Componente donde se mostrará la imagen
        imagenLabel = new JLabel("Aquí se mostrará la imagen", SwingConstants.CENTER);
        imagenLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        imagenLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(imagenLabel, BorderLayout.CENTER);

        // Botón para cargar imagen
        botonCargar = new JButton("Cargar imagen");
        add(botonCargar, BorderLayout.SOUTH);

        // Acción del botón
        botonCargar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser selector = new JFileChooser();
                int opcion = selector.showOpenDialog(null);
                if (opcion == JFileChooser.APPROVE_OPTION) {
                    File archivo = selector.getSelectedFile();
                    ImageIcon imagen = new ImageIcon(archivo.getAbsolutePath());
                    // Escalar imagen para que no se salga del panel
                    Image imagenEscalada = imagen.getImage().getScaledInstance(
                            imagenLabel.getWidth(), imagenLabel.getHeight(), Image.SCALE_SMOOTH);
                    imagenLabel.setIcon(new ImageIcon(imagenEscalada));
                    imagenLabel.setText(""); // quitar texto por defecto
                }
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new VentanaImagen();
    }
}
