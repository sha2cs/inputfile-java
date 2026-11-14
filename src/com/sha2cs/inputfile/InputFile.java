package com.sha2cs.inputfile;

import java.awt.Component;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Selección y visualización de imágenes PNG y JPG en aplicaciones Swing.
 *
 * <p>Encapsula {@link JFileChooser}, {@link FileNameExtensionFilter},
 * {@link ImageIO} y {@link Image#getScaledInstance(int, int, int)}.</p>
 *
 * <pre>
 * File foto = InputFile.elegir(this);
 * if (foto != null) {
 *     InputFile.mostrar(lblFoto, foto);
 *     String ruta = foto.getAbsolutePath();
 * }
 * </pre>
 */
public final class InputFile {

    /** Última carpeta abierta; el selector vuelve a ella en la siguiente llamada. */
    private static File ultimaCarpeta;

    private InputFile() {
    }

    /**
     * Abre el selector de archivos restringido a imágenes PNG y JPG.
     *
     * @param padre componente sobre el que se centra el diálogo (puede ser {@code null})
     * @return el archivo elegido, o {@code null} si se cancela o el archivo no es PNG/JPG
     */
    public static File elegir(Component padre) {
        JFileChooser selector = new JFileChooser(ultimaCarpeta);
        selector.setDialogTitle("Seleccionar imagen");
        selector.setAcceptAllFileFilterUsed(false);
        selector.setFileFilter(new FileNameExtensionFilter("Imágenes PNG y JPG", "png", "jpg", "jpeg"));

        if (selector.showOpenDialog(padre) != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        File archivo = selector.getSelectedFile();
        ultimaCarpeta = archivo.getParentFile();
        return esImagenValida(archivo) ? archivo : null;
    }

    /**
     * Muestra la imagen dentro del {@link JLabel}, escalada al tamaño de la etiqueta
     * y conservando la proporción.
     *
     * @param etiqueta etiqueta destino
     * @param archivo  imagen PNG o JPG
     * @return {@code true} si la imagen se mostró; {@code false} si el archivo no existe o no es una imagen
     */
    public static boolean mostrar(JLabel etiqueta, File archivo) {
        if (etiqueta == null || !esImagenValida(archivo)) {
            return false;
        }
        BufferedImage original;
        try {
            original = ImageIO.read(archivo);
        } catch (IOException e) {
            return false;
        }
        if (original == null) {
            return false;
        }

        int ancho = etiqueta.getWidth() > 0 ? etiqueta.getWidth() : etiqueta.getPreferredSize().width;
        int alto = etiqueta.getHeight() > 0 ? etiqueta.getHeight() : etiqueta.getPreferredSize().height;

        Image imagen = original;
        if (ancho > 0 && alto > 0) {
            double escala = Math.min((double) ancho / original.getWidth(), (double) alto / original.getHeight());
            int nuevoAncho = Math.max(1, (int) (original.getWidth() * escala));
            int nuevoAlto = Math.max(1, (int) (original.getHeight() * escala));
            imagen = original.getScaledInstance(nuevoAncho, nuevoAlto, Image.SCALE_SMOOTH);
        }

        etiqueta.setText(null);
        etiqueta.setIcon(new ImageIcon(imagen));
        return true;
    }

    /**
     * Muestra la imagen ubicada en la ruta indicada. Útil para cargar una foto
     * cuya ruta se guardó previamente como texto.
     *
     * @param etiqueta etiqueta destino
     * @param ruta     ruta del archivo
     * @return {@code true} si la imagen se mostró
     */
    public static boolean mostrar(JLabel etiqueta, String ruta) {
        return ruta != null && mostrar(etiqueta, new File(ruta));
    }

    /**
     * Abre el selector y, si se elige una imagen válida, la muestra en la etiqueta.
     *
     * @param padre    componente sobre el que se centra el diálogo
     * @param etiqueta etiqueta destino
     * @return el archivo elegido, o {@code null} si se cancela o no es válido
     */
    public static File elegirYMostrar(Component padre, JLabel etiqueta) {
        File archivo = elegir(padre);
        if (archivo != null && mostrar(etiqueta, archivo)) {
            return archivo;
        }
        return null;
    }

    /**
     * Elimina la imagen de la etiqueta.
     *
     * @param etiqueta etiqueta a limpiar
     */
    public static void limpiar(JLabel etiqueta) {
        if (etiqueta != null) {
            etiqueta.setIcon(null);
        }
    }

    private static boolean esImagenValida(File archivo) {
        if (archivo == null || !archivo.isFile()) {
            return false;
        }
        String nombre = archivo.getName().toLowerCase();
        return nombre.endsWith(".png") || nombre.endsWith(".jpg") || nombre.endsWith(".jpeg");
    }
}
