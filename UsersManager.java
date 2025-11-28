import java.nio.file.*;
import java.util.*;
import java.io.IOException;

public class UsersManager {

    private static final Path USERS_PATH = Paths.get("Users.txt");

    public static User login() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Login ===");
        System.out.print("Usuario: ");
        String username = scanner.nextLine().trim();

        try {
            if (!Files.exists(USERS_PATH)) {
                System.out.println("No existe Users.txt");
                return null;
            }

            List<String> users = Files.readAllLines(USERS_PATH);
            int index = -1;
            String[] datos = null;

            for (int i = 0; i < users.size(); i++) {
                String[] p = users.get(i).split(",", 3);
                if (p[0].equals(username)) {
                    index = i;
                    datos = p;
                    break;
                }
            }

            if (index == -1) {
                System.out.println("Usuario no encontrado.");
                return null;
            }

            boolean active = Boolean.parseBoolean(datos[2]);
            if (!active) {
                System.out.println("Usuario bloqueado.");
                return null;
            }

            int attempts = 3;
            while (attempts > 0) {
                System.out.print("Contraseña: ");
                String pass = scanner.nextLine();

                if (pass.equals(datos[1])) {
                    writeLog(username, "LOGIN_OK");
                    return new User(datos[0], datos[1], true);
                }

                attempts--;
                System.out.println("Contraseña incorrecta. Intentos restantes: " + attempts);
            }

            System.out.println("Intentos agotados. Usuario bloqueado.");
            datos[2] = "false";
            users.set(index, datos[0] + "," + datos[1] + ",false");
            Files.write(USERS_PATH, users, StandardOpenOption.TRUNCATE_EXISTING);

            writeLog(username, "USER_BLOCKED");
            return null;

        } catch (Exception e) {
            System.out.println("Error leyendo Users.txt");
            return null;
        }
    }

    public static void writeLog(String username, String action) {
        String time = java.time.LocalDateTime.now().toString().replace("T", " ");
        String line = "[" + time + "] usuario:" + username + " - " + action;
        Path log = Paths.get("log.txt");

        try {
            Files.write(log,
                        Collections.singletonList(line),
                        StandardOpenOption.CREATE,
                        StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Error escribiendo log.");
        }
    }
}
