## Restaurant Simulation

This project simulates a restaurant scenario where a waiter serves multiple clients. It demonstrates the use of Java threads to manage concurrent actions of serving and dining, with synchronization to ensure the correct sequence of interactions between the waiter and clients.

### Repository Name
`RestaurantSim`

### Description
The Restaurant Simulation project uses Java's multithreading capabilities to simulate the interactions between a waiter and multiple clients in a restaurant setting. The main class, `Restaurante`, sets up the simulation by creating and managing threads for the waiter and each client, ensuring that the events occur in a synchronized and orderly manner.

### Features
- **Multithreading:** Utilizes Java's threading capabilities to manage concurrent tasks.
- **Synchronization:** Ensures proper coordination between the waiter's and clients' actions.
- **Simulation:** Creates a realistic scenario of a restaurant experience using print statements to represent the actions of the waiter and clients.

### File Structure


- **pom.xml**: Maven configuration file that manages project dependencies and build settings.
- **src/**: Contains the source code and test code for the project.
  - **main/**: Main application code.
    - **java/**: Java source code directory.
      - **com/mycompany/restaurante/**: Package directory containing all the Java classes for the project.
        - **Restaurante.java**: Entry point of the application; sets up and starts the threads for the waiter and clients.
  - **test/**: Placeholder for test code.
- **target/**: Directory generated by Maven during the build process.
  - **classes/**: Contains compiled bytecode of the Java classes.
    - **com/mycompany/restaurante/**: Compiled classes for the project.
      - **Cliente.class**: Compiled bytecode for the Cliente class.
      - **Mesero.class**: Compiled bytecode for the Mesero class.
      - **Restaurante.class**: Compiled bytecode for the Restaurante class.
  - **generated-sources/**: Directory for generated sources (if any).
    - **annotations/**: Placeholder for annotation processors.
  - **maven-status/**: Directory containing Maven build status files.
    - **maven-compiler-plugin/compile/default-compile/**: Contains lists of created and input files during compilation.
      - **createdFiles.lst**: List of files created during the build.
      - **inputFiles.lst**: List of files used as input during the build.
  - **test-classes/**: Directory for compiled test classes.

### Main Classes and Methods

#### Restaurante Class
This class is the entry point of the simulation. It creates threads for the waiter and each client, managing their interactions.

**Main Method:**
```java
public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    System.out.print("Ingrese la cantidad de clientes: ");
    int numClientes = scanner.nextInt();
    scanner.nextLine();

    Cliente[] clientes = new Cliente[numClientes];

    for (int i = 0; i < numClientes; i++) {
        System.out.print("Ingrese el nombre del cliente " + (i + 1) + ": ");
        String nombre = scanner.nextLine();
        System.out.print("Ingrese el platillo que pedirá el cliente " + (i + 1) + ": ");
        String platillo = scanner.nextLine();
        clientes[i] = new Cliente(nombre, platillo, Thread.NORM_PRIORITY); // Establecer prioridad normal
    }

    Mesero mesero = new Mesero(clientes);

    mesero.setPriority(Thread.MIN_PRIORITY); // Establecer prioridad mínima para el mesero

    mesero.start();
    for (Cliente cliente : clientes) {
        cliente.start();
    }
}
```

#### Mesero Class

This class represents the waiter. It uses synchronization to coordinate actions with each client.

**Run Method:**

```java
public void run() {
    for (Cliente cliente : clientes) {
        synchronized (cliente) {
            System.out.println("Mesero: Bienvenido, " + cliente.getNombre() + ". Por favor, tome asiento.");
        }
        try {
            sleep(1000); // Simula el tiempo que toma hacer el pedido.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        synchronized (cliente) {
            System.out.println("Mesero: Tomando la orden de " + cliente.getNombre() + " para " + cliente.getPlatillo());
            cliente.notify(); // Notificar al cliente que su comida está lista.
        }

        synchronized (cliente) {
            try {
                cliente.wait(); // Esperar a que el cliente termine de comer.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Mesero: Recogiendo los platos de " + cliente.getNombre());
        }
    }
}
```

#### Cliente Class

This class represents a client in the restaurant. Each client runs in its own thread and interacts with the waiter.

**Run Method:**

```java
public void run() {
    synchronized (this) {
        try {
            wait(); // Esperar a que el mesero le indique que tome asiento.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(nombre + ": Tomando asiento en una mesa.");
    }

    synchronized (this) {
        System.out.println(nombre + ": Disfrutando de " + platillo);
        try {
            sleep(2000); // Simula el tiempo que toma comer.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(nombre + ": Terminé de comer.");
        notify(); // Notificar al mesero que terminó de comer.
    }
}
```

### Getting Started

To run the simulation, compile the Java files and execute the `Restaurante` class.

### Requirements

-   Java Development Kit (JDK)
-   Maven for managing project dependencies
-   An IDE or text editor for Java development

### Usage

1.  Clone the repository.
2.  Navigate to the `src/main/java/com/mycompany/restaurante` directory.
3.  Compile the Java files using Maven: `mvn compile`.
4.  Run the simulation using: `mvn exec:java -Dexec.mainClass="com.mycompany.restaurante.Restaurante"`.