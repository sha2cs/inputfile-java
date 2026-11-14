# inputfile-java

Librería Java tipo `<input type="file">` para seleccionar imágenes **PNG y JPG** y mostrarlas dentro de un `JLabel` en aplicaciones Swing.

Encapsula `JFileChooser`, `FileNameExtensionFilter`, `ImageIO` y `getScaledInstance()` en una sola clase: `com.sha2cs.inputfile.InputFile`.

## Requisitos

- JDK 11 o superior.
- Proyecto Java con interfaz Swing.
- Sistema operativo: Windows o Linux. Las instrucciones indican la diferencia donde la hay.

Versión del JDK instalada:

| Sistema | Dónde | Comando |
|---|---|---|
| Windows | **Símbolo del sistema** o **PowerShell** (tecla Windows → escribir `cmd`) | `java -version` |
| Linux | **Terminal** (`Ctrl+Alt+T`) | `java -version` |

La primera línea de la salida debe indicar versión 11 o superior (por ejemplo `openjdk version "21.0.2"`).

## 1. Descarga del `.jar`

1. Abrir el archivo [`dist/inputfile-java.jar`](dist/inputfile-java.jar) en este repositorio.
2. Pulsar el botón **Download raw file** (ícono de descarga, a la derecha).

Ubicación del archivo descargado:

| Sistema | Carpeta |
|---|---|
| Windows | `C:\Users\<usuario>\Downloads` (en el **Explorador de archivos**: **Descargas**) |
| Linux | `~/Descargas` o `~/Downloads`, según el idioma del sistema |

## 2. Instalación en IntelliJ IDEA

### 2.1. Carpeta `lib`

1. En el panel **Project**, clic derecho sobre la carpeta raíz del proyecto → **New → Directory** → nombre `lib`.
2. Copiar `inputfile-java.jar` dentro de `lib/`. Cualquiera de estas formas sirve:
   - **Arrastrar** el archivo desde la carpeta de descargas hasta la carpeta `lib` en el panel **Project** de IntelliJ.
   - **Copiar y pegar:** `Ctrl+C` sobre el archivo en la carpeta de descargas, clic sobre `lib` en IntelliJ y `Ctrl+V`.
   - **Desde el gestor de archivos del sistema:** clic derecho sobre `lib` en IntelliJ → **Open In** → **Explorer** (Windows) o **Files** / **Directory** (Linux). Se abre la carpeta `lib` y el `.jar` se pega allí.

Los atajos de teclado de IntelliJ indicados en esta guía son los mismos en Windows y en Linux.

### 2.2. Registro de la librería

**Opción A (la más corta):**

1. Clic derecho sobre `lib/inputfile-java.jar` → **Add as Library…**
2. En **Level** dejar **Project Library** y en **Add to module** el módulo del proyecto → **OK**.

**Opción B (desde Project Structure):**

1. **File → Project Structure…** (`Ctrl+Alt+Shift+S`).
2. **Libraries → + → Java**.
3. Seleccionar `lib/inputfile-java.jar` → **OK**.
4. Confirmar el módulo al que se agrega → **OK** → **Apply** → **OK**.

### 2.3. Verificación

La librería queda instalada si el siguiente `import` no se marca en rojo:

```java
import com.sha2cs.inputfile.InputFile;
```

La librería aparece además en el panel **Project → External Libraries → inputfile-java**.

### 2.4. Repositorio del proyecto

La carpeta `lib/` con el `.jar` **se sube al repositorio** del proyecto. Sin ella, el proyecto no compila en otro computador.

## 3. Métodos

| Método | Resultado |
|---|---|
| `InputFile.elegir(padre)` | Abre el selector filtrado a PNG/JPG. Devuelve el `File` elegido, o `null` si se cancela. |
| `InputFile.mostrar(etiqueta, archivo)` | Muestra la imagen en el `JLabel`, escalada al tamaño de la etiqueta y con la proporción original. Devuelve `true` si la imagen se mostró, `false` si el archivo no existe o no es una imagen. |
| `InputFile.mostrar(etiqueta, ruta)` | Igual que el anterior, a partir de una ruta en texto. |
| `InputFile.elegirYMostrar(padre, etiqueta)` | Abre el selector y muestra la imagen elegida. Devuelve el `File`, o `null` si se cancela. |
| `InputFile.limpiar(etiqueta)` | Quita la imagen de la etiqueta. |

El parámetro `padre` es la ventana sobre la que se centra el diálogo; dentro de un `JFrame` corresponde a `this`.

El selector recuerda la última carpeta abierta durante la ejecución del programa.

## 4. Ejemplo de uso

Formulario con un `JLabel` llamado `lblFoto` y un botón **Cargar foto**:

```java
import com.sha2cs.inputfile.InputFile;
import java.io.File;

public class FormularioUsuario extends javax.swing.JFrame {

    private String rutaFoto;

    private void btnCargarFotoActionPerformed(java.awt.event.ActionEvent evt) {
        File foto = InputFile.elegirYMostrar(this, lblFoto);
        if (foto != null) {
            rutaFoto = foto.getAbsolutePath();   // ruta que se guarda junto al usuario
        }
    }

    private void cargarUsuario(Usuario usuario) {
        if (!InputFile.mostrar(lblFoto, usuario.getRutaFoto())) {
            InputFile.limpiar(lblFoto);
            lblFoto.setText("Sin foto");
        }
    }
}
```

## 5. Tamaño del `JLabel`

La imagen se escala al tamaño que tiene el `JLabel` en ese momento. Condiciones:

- El `JLabel` requiere un tamaño definido en el diseñador (por ejemplo 150 × 150) o mediante `setPreferredSize(new java.awt.Dimension(150, 150))`.
- Sin tamaño definido, la imagen se muestra en su tamaño original.
- Para centrar la imagen: `lblFoto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER)`.

## 6. Errores frecuentes

| Síntoma | Causa | Solución |
|---|---|---|
| `package com.sha2cs.inputfile does not exist` | La librería no está registrada en el módulo. | Repetir el paso 2.2. |
| El proyecto compila en un equipo y no en otro | La carpeta `lib/` no se subió al repositorio. | Subir `lib/inputfile-java.jar`. |
| La imagen aparece enorme o desbordada | El `JLabel` no tiene tamaño definido. | Ver la sección 5. |
| `mostrar(...)` devuelve `false` | La ruta guardada ya no existe, o el archivo no es una imagen real. | Verificar la ruta; mostrar un texto alternativo. |

## 7. Compilación desde el código fuente

| Sistema | Dónde | Comando |
|---|---|---|
| Windows | **Símbolo del sistema** abierto en la carpeta del repositorio | `build.bat` |
| Linux | **Terminal** abierta en la carpeta del repositorio | `./build.sh` |

Genera `dist/inputfile-java.jar`, que incluye las clases compiladas y el código fuente (IntelliJ muestra la documentación de cada método al usarlo).
