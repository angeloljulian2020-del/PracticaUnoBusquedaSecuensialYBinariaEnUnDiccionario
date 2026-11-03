/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package ec.edu.ups.pracunobusquedasecuencialybinaria;

import ec.edu.ups.pracunobusquedasecuencialybinaria.Modelo.MPalabra;
import java.util.Arrays;
import java.util.Random;

public class PracUnoBusquedaSecuencialYBinaria {

    // Contadores independientes para cada método (como se vio en clases)
    public static int contadorSecuencial = 0;
    public static int contadorBinaria = 0;

    public static void main(String[] args) {
        // 1.arreglo de 8 objetos Palabra en orden alfabético ascendente
        MPalabra[] diccionario = {
            new MPalabra("arbol", "Planta perenne de tronco lenoso."),
            new MPalabra("barco", "Vehiculo flotante para navegar por el agua."),
            new MPalabra("casa", "Edificio para habitar."),
            new MPalabra("dado", "Pieza cubica usada en juegos de mesa."),
            new MPalabra("escudo", "Arma defensiva para proteger el cuerpo."),
            new MPalabra("fuego", "Emision de luz y calor producida por una combustión."),
            new MPalabra("gato", "Mamifero felino doméstico."),
            new MPalabra("hiel", "Secrecion gastrica amarga / bilis.")
        };

        // 2.arreglo paralelo String[] con los términos manteniendo el orden
        String[] terminos = new String[diccionario.length];
        for (int i = 0; i < diccionario.length; i++) {
            terminos[i] = diccionario[i].getTermino();
        }

        System.out.println(" 3. COMPROBACION FUNCIONAL ");
        String[] consultasPrueba = {"arbol", "escudo", "hiel", "pelota"};

        for (int i = 0; i < consultasPrueba.length; i++) {
            String consulta = consultasPrueba[i];
            System.out.println("\n Buscando : \"" + consulta + "\" .");

            // Búsqueda secuencial propia
            contadorSecuencial = 0;
            int idxSec = busquedaSecuencial(terminos, consulta);
            System.out.println("Con Busqueda secuencial propia");
            System.out.println("Indice: " + idxSec + ", Comparaciones: " + contadorSecuencial);
            if (idxSec >= 0) {
                System.out.println("Significado: " + diccionario[idxSec].getSignificado());
            }

            // Búsqueda binaria iterativa propia
            contadorBinaria = 0;
            int idxBin = busquedaBinariaPropia(terminos, consulta);
            System.out.println("Con Busqueda binaria iterativa propia");
            System.out.println(" Indice: " + idxBin + ", Comparaciones: " + contadorBinaria);

            // Búsqueda binaria de la biblioteca de Java
            int idxLib = Arrays.binarySearch(terminos, consulta);
            System.out.println("Con Busqueda binaria de la biblioteca de Java");
            System.out.println("Arrays.binarySearch -> Resultado : " + idxLib);
            if (idxLib >= 0) {
                System.out.println("Significado encontrado: " + diccionario[idxLib].getSignificado());
            } else {
                int puntoInsercion = -idxLib - 1;
                System.out.println("Termino ausente. Punto de insercion: " + puntoInsercion);
            }
        }

        // 4. EXPERIMENTOS Y MEDICIÓN DE TIEMPOS
        System.out.println("\n 4. EJECUTANDO EXPERIMENTOS DE TIEMPOS ");
        int[] tamanios = {100, 1000, 10000};

        for (int n : tamanios) {
            String[] dataset = generarDatosSinteticos(n);
            String consultaEjemplo = dataset[n / 2]; // Término central como ejemplo de prueba

            ejecutarExperimentos(dataset, consultaEjemplo, n);
        }
    }

    // --- MÉTODOS DE BÚSQUEDA ---
    public static int busquedaSecuencial(String[] arreglo, String objetivo) {
        for (int i = 0; i < arreglo.length; i++) {
            contadorSecuencial++;
            if (arreglo[i].equals(objetivo)) {
                return i;
            }
        }
        return -1;
    }

    public static int busquedaBinariaPropia(String[] arreglo, String objetivo) {
        int izq = 0;
        int der = arreglo.length - 1;

        while (izq <= der) {
            int medio = izq + (der - izq) / 2;
            contadorBinaria++;
            int resultadoComparacion = arreglo[medio].compareTo(objetivo);

            if (resultadoComparacion == 0) {
                return medio;
            } else if (resultadoComparacion < 0) {
                izq = medio + 1;
            } else {
                der = medio - 1;
            }
        }
        return -1;
    }

    // MÉTODO PARA GENERAR DATOS SINTETICOS ORDENADOS 
    public static String[] generarDatosSinteticos(int n) {
        String[] datos = new String[n];
        for (int i = 0; i < n; i++) {
            char c1 = (char) ('a' + (i / 676) % 26);
            char c2 = (char) ('a' + (i / 26) % 26);
            char c3 = (char) ('a' + i % 26);
            datos[i] = "" + c1 + c2 + c3;
        }
        Arrays.sort(datos); // Ordena alfabéticamente
        return datos;
    }

    // MEDICIÓN DE TIEMPOS 
    public static void ejecutarExperimentos(String[] dataset, String consulta, int n) {
        // 1000 ejecuciones
        for (int k = 0; k < 1000; k++) {
            busquedaSecuencial(dataset, consulta);
            busquedaBinariaPropia(dataset, consulta);
        }

        double[] tiemposSec = new double[5];
        double[] tiemposBin = new double[5];

        // 5 rondas de medición
        for (int ronda = 0; ronda < 5; ronda++) {

            // Medición Secuencial 
            double inicioSec = System.currentTimeMillis();
            for (int k = 0; k < 10000; k++) {
                busquedaSecuencial(dataset, consulta);
            }
            double finSec = System.currentTimeMillis();
            tiemposSec[ronda] = (finSec - inicioSec) / 10000.0;

            // --- Medición Binaria Propia ---
            double inicioBin = System.currentTimeMillis();
            for (int k = 0; k < 10000; k++) {
                busquedaBinariaPropia(dataset, consulta);
            }
            double finBin = System.currentTimeMillis();
            tiemposBin[ronda] = (finBin - inicioBin) / 10000.0;
        }

        // Ordenar los arreglos para obtener la mediana de las 5 rondas
        Arrays.sort(tiemposSec);
        Arrays.sort(tiemposBin);

        System.out.println("Para n = " + n + " -> Mediana Secuencial: " + tiemposSec[2] + " ms | Mediana Binaria: " + tiemposBin[2] + " ms");
    }
}
