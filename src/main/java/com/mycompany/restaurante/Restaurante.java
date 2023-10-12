package com.mycompany.restaurante;

import java.util.Scanner;

class Restaurante {
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
            clientes[i] = new Cliente(nombre, platillo, Thread.NORM_PRIORITY); // Establecer prioridad mínima
        }

        Mesero mesero = new Mesero(clientes);

        mesero.setPriority(Thread.MIN_PRIORITY); // Establecer prioridad normal para el mesero

        mesero.start();
        for (Cliente cliente : clientes) {
            cliente.start();
        }
    }
}

class Mesero extends Thread {
    private Cliente[] clientes;

    public Mesero(Cliente[] clientes) {
        this.clientes = clientes;
    }

    public void run() {
        for (Cliente cliente : clientes) {
            synchronized (cliente) {
                System.out.println("Mesero: Bienvenido, " + cliente.getNombre() + ". Por favor, tome asiento.");
                //cliente.notify(); // Notificar al cliente que tome asiento.
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
}

class Cliente extends Thread {
    private String nombre;
    private String platillo;

    public Cliente(String nombre, String platillo, int prioridad) {
        this.nombre = nombre;
        this.platillo = platillo;
        setPriority(prioridad); // Establecer la prioridad del cliente
    }

    public String getNombre() {
        return nombre;
    }

    public String getPlatillo() {
        return platillo;
    }

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
}
