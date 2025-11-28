# Taller #6 – Archivos Planos en Java

Este proyecto corresponde al Taller #6 de Programación, cuyo objetivo es implementar un sistema básico de manejo de información utilizando archivos planos en Java.  
El sistema permite gestionar usuarios para iniciar sesión y administrar un registro de personas guardado en un archivo de texto.

---

## Descripción general

El programa realiza lectura, escritura, actualización y eliminación de datos almacenados en archivos `.txt`.  
Incluye un proceso de login con validación y bloqueo, manejo de logs, y operaciones CRUD sobre un archivo de personas.

---

## Funcionalidades implementadas

### 1. Login con validación y bloqueo  
El archivo `Users.txt` contiene los usuarios con el formato:


El sistema:
- Permite tres intentos de contraseña.
- Bloquea automáticamente al usuario si falla los tres intentos.
- Guarda los eventos en el archivo `log.txt`.

---

### 2. Gestión de personas (persons.txt)

El sistema permite:

- Listar personas registradas.
- Crear nuevas personas (validando teléfono y saldo).
- Editar personas conservando valores si se presiona ENTER.
- Eliminar personas con confirmación.
- Generar un informe agrupado por ciudad mostrando:
  - Número de personas por ciudad.
  - Suma total de saldos por ciudad.
  - Totales generales.

---

## Estructura del proyecto


---

## Autor

Trabajo realizado por Andrés Upegui  
Taller #6 – Archivos Planos  
Asignatura: Programación

---

## Licencia

Uso académico. Libre para estudiar y modificar.


El archivo `persons.txt` utiliza el siguiente formato:

