import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        User user = UsersManager.login();
        if (user == null) {
            System.out.println("No se inició sesión.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== MENÚ PRINCIPAL ===");
            System.out.println("1. Listar personas");
            System.out.println("2. Crear persona");
            System.out.println("3. Editar persona");
            System.out.println("4. Eliminar persona");
            System.out.println("5. Informe por ciudad");
            System.out.println("6. Salir");
            System.out.print("Opción: ");

            String op = scanner.nextLine();

            switch (op) {
                case "1":
                    listarPersonas();
                    break;

                case "2":
                    crearPersona(user.getUsername());
                    break;

                case "3":
                    editarPersona(user.getUsername());
                    break;

                case "4":
                    eliminarPersona(user.getUsername());
                    break;

                case "5":
                    informePorCiudad();
                    break;

                case "6":
                    UsersManager.writeLog(user.getUsername(), "LOGOUT");
                    System.out.println("Hasta luego.");
                    running = false;
                    break;

                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    // ===================================================
    // LISTAR
    // ===================================================
    public static void listarPersonas() {
        Path path = Paths.get("persons.txt");

        if (!Files.exists(path)) {
            System.out.println("No existe persons.txt");
            return;
        }

        System.out.println("\n--- LISTADO DE PERSONAS ---");

        try {
            List<String> lines = Files.readAllLines(path);

            if (lines.isEmpty()) {
                System.out.println("No hay personas registradas.");
                return;
            }

            for (String line : lines) {
                String[] p = line.split(",");
                System.out.println(
                        p[0] + ": " + p[1] + " " + p[2] +
                        " | Ciudad: " + p[3] +
                        " | Tel: " + p[4] +
                        " | Saldo: " + p[5]
                );
            }

        } catch (IOException e) {
            System.out.println("Error leyendo archivo.");
        }
    }

    // ===================================================
    // CREAR
    // ===================================================
    public static void crearPersona(String username) {
        Scanner scanner = new Scanner(System.in);
        Path path = Paths.get("persons.txt");
        List<String> lines = new ArrayList<>();

        try {
            if (Files.exists(path)) {
                lines = Files.readAllLines(path);
            }
        } catch (Exception e) {
            System.out.println("Error leyendo persons.txt");
            return;
        }

        int newId = lines.size() + 1;

        System.out.println("\n--- CREAR PERSONA ---");

        System.out.print("Nombres: ");
        String nombres = scanner.nextLine();

        System.out.print("Apellidos: ");
        String apellidos = scanner.nextLine();

        System.out.print("Ciudad: ");
        String ciudad = scanner.nextLine();

        String telefono;
        while (true) {
            System.out.print("Teléfono (7-13 dígitos): ");
            telefono = scanner.nextLine();
            if (telefono.matches("\\d{7,13}")) break;
            System.out.println("Teléfono inválido.");
        }

        String saldo;
        while (true) {
            System.out.print("Saldo (>=0): ");
            saldo = scanner.nextLine();
            try {
                if (Double.parseDouble(saldo) >= 0) break;
            } catch (Exception ignored) {}
            System.out.println("Saldo inválido.");
        }

        String row = newId + "," + nombres + "," + apellidos + "," +
                     ciudad + "," + telefono + "," + saldo;

        lines.add(row);

        try {
            Files.write(path, lines,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("Persona creada.");
            UsersManager.writeLog(username, "CREAR_PERSONA ID=" + newId);

        } catch (Exception e) {
            System.out.println("Error guardando.");
        }
    }

    // ===================================================
    // EDITAR
    // ===================================================
    public static void editarPersona(String username) {
        Scanner scanner = new Scanner(System.in);
        Path path = Paths.get("persons.txt");

        List<String> lines;

        try {
            if (!Files.exists(path)) {
                System.out.println("No existe persons.txt");
                return;
            }
            lines = Files.readAllLines(path);

        } catch (Exception e) {
            System.out.println("Error leyendo archivo.");
            return;
        }

        System.out.print("ID a editar: ");
        int id;

        try {
            id = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("ID inválido.");
            return;
        }

        int index = -1;
        String[] datos = null;

        for (int i = 0; i < lines.size(); i++) {
            String[] p = lines.get(i).split(",");
            if (Integer.parseInt(p[0]) == id) {
                datos = p;
                index = i;
                break;
            }
        }

        if (index == -1) {
            System.out.println("No existe ese ID.");
            return;
        }

        System.out.println("\n--- EDITAR PERSONA ---");
        System.out.println("ENTER = conservar original");

        System.out.print("Nuevo nombre (" + datos[1] + "): ");
        String n = scanner.nextLine();
        if (n.isEmpty()) n = datos[1];

        System.out.print("Nuevos apellidos (" + datos[2] + "): ");
        String a = scanner.nextLine();
        if (a.isEmpty()) a = datos[2];

        System.out.print("Nueva ciudad (" + datos[3] + "): ");
        String c = scanner.nextLine();
        if (c.isEmpty()) c = datos[3];

        String t;
        while (true) {
            System.out.print("Nuevo teléfono (" + datos[4] + "): ");
            String temp = scanner.nextLine();
            if (temp.isEmpty()) {
                t = datos[4];
                break;
            }
            if (temp.matches("\\d{7,13}")) {
                t = temp;
                break;
            }
            System.out.println("Tel inválido.");
        }

        String s;
        while (true) {
            System.out.print("Nuevo saldo (" + datos[5] + "): ");
            String temp = scanner.nextLine();
            if (temp.isEmpty()) {
                s = datos[5];
                break;
            }
            try {
                if (Double.parseDouble(temp) >= 0) {
                    s = temp;
                    break;
                }
            } catch (Exception ignored) {}
            System.out.println("Saldo inválido.");
        }

        String newRow = datos[0] + "," + n + "," + a + "," + c + "," + t + "," + s;
        lines.set(index, newRow);

        try {
            Files.write(path, lines, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("Persona actualizada.");
            UsersManager.writeLog(username, "EDITAR_PERSONA ID=" + datos[0]);
        } catch (Exception e) {
            System.out.println("Error guardando cambios.");
        }
    }

    // ===================================================
    // ELIMINAR
    // ===================================================
    public static void eliminarPersona(String username) {
        Scanner scanner = new Scanner(System.in);
        Path path = Paths.get("persons.txt");

        List<String> lines;

        try {
            if (!Files.exists(path)) {
                System.out.println("No existe persons.txt");
                return;
            }
            lines = Files.readAllLines(path);

        } catch (Exception e) {
            System.out.println("Error leyendo archivo.");
            return;
        }

        System.out.print("ID a eliminar: ");
        int id;

        try {
            id = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("ID inválido.");
            return;
        }

        int index = -1;
        String[] datos = null;

        for (int i = 0; i < lines.size(); i++) {
            String[] p = lines.get(i).split(",");
            if (Integer.parseInt(p[0]) == id) {
                datos = p;
                index = i;
                break;
            }
        }

        if (index == -1) {
            System.out.println("No existe ese ID.");
            return;
        }

        System.out.println("\n--- PERSONA A ELIMINAR ---");
        System.out.println("ID: " + datos[0]);
        System.out.println("Nombre: " + datos[1] + " " + datos[2]);
        System.out.println("Ciudad: " + datos[3]);
        System.out.println("Tel: " + datos[4]);
        System.out.println("Saldo: " + datos[5]);

        System.out.print("\n¿Eliminar? (S/N): ");
        String confirm = scanner.nextLine().toUpperCase();

        if (!confirm.equals("S")) {
            System.out.println("Cancelado.");
            return;
        }

        lines.remove(index);

        try {
            Files.write(path, lines, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("Persona eliminada.");
            UsersManager.writeLog(username, "ELIMINAR_PERSONA ID=" + datos[0]);

        } catch (Exception e) {
            System.out.println("Error guardando.");
        }
    }

    // ===================================================
    // INFORME POR CIUDAD
    // ===================================================
    public static void informePorCiudad() {

        Path path = Paths.get("persons.txt");

        if (!Files.exists(path)) {
            System.out.println("No existe persons.txt");
            return;
        }

        System.out.println("\n=== INFORME POR CIUDAD ===\n");

        try {
            List<String> lines = Files.readAllLines(path);

            if (lines.isEmpty()) {
                System.out.println("No hay personas registradas.");
                return;
            }

            Map<String, List<String[]>> mapa = new HashMap<>();

            for (String line : lines) {
                String[] p = line.split(",");
                String ciudad = p[3];

                mapa.putIfAbsent(ciudad, new ArrayList<>());
                mapa.get(ciudad).add(p);
            }

            int totalPersonas = 0;
            double totalSaldo = 0;

            for (String ciudad : mapa.keySet()) {

                List<String[]> lista = mapa.get(ciudad);

                System.out.println("Ciudad: " + ciudad);

                int count = lista.size();
                double saldoCiudad = 0;

                for (String[] p : lista) {
                    saldoCiudad += Double.parseDouble(p[5]);
                }

                System.out.println("  Personas: " + count);
                System.out.println("  Saldo total: " + saldoCiudad);
                System.out.println();

                totalPersonas += count;
                totalSaldo += saldoCiudad;
            }

            System.out.println("TOTAL PERSONAS: " + totalPersonas);
            System.out.println("TOTAL SALDOS: " + totalSaldo);

        } catch (Exception e) {
            System.out.println("Error leyendo archivo.");
        }
    }
}
