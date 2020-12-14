/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

/**
 *
 * @author ASUS
 
public class BankAdvisor {
    
}
*/
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Scanner;

public class BankAdvisor implements ActionResultClientDelegate {
    Scanner scanner = new Scanner(System.in);
    Client socketClient;

    public void initialScreen(){
        System.out.println("Bienvenido al banco .... ");
        System.out.println(" escoja accion ");
        System.out.println("1. registrarse ");
        System.out.println("2.  Retiro ");
        System.out.println("3.  Consulta Saldo ");
        System.out.println("4.  Deposito ");
        System.out.println("5.  salir ");

        int selectedOption = this.scanner.nextInt();
        this.scanner.nextLine(); // breakline

        if (selectedOption == 1) {
            this.showRegisterScreen();
        }

        if (selectedOption == 2) {
            this.withdrawal();
        }

        if (selectedOption == 3) {
            this.balance();
        }

        if (selectedOption == 4) {
            this.deposit();
        }

        if (selectedOption == 5) {
            this.exit();
        }
    }

    private void showRegisterScreen(){
        System.out.println("Ingresa el nuevo usuario: Nombre , Apellido , Cedula , Telefono, Contraseña");
        System.out.println("Siempre debes usar comas: ' , ' para separar. ");
        String inputUser = this.scanner.nextLine();

        String[] userInfo = inputUser.split(",");
        String name = userInfo[0];
        String lastName = userInfo[1];
        String personId = userInfo[2];
        String phone = userInfo[3];
        String password = userInfo[4];
        this.createUserInBank(name, lastName, phone, personId, password);
    }

    public void deposit(){
        System.out.println("Ingresa tu cedula, clave y monto a depositar separado por ','");

        String info = this.scanner.nextLine();
        String[] depositData = info.split(",");
        String personId = depositData[0];
        String password = depositData[1];
        String quantity = depositData[2];

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode action = mapper.createObjectNode();
        action.put("action", "execute_deposit");

        ObjectNode depositInfo = mapper.createObjectNode();
        depositInfo.put("personId", personId.trim());
        depositInfo.put("password", password.trim());
        depositInfo.put("amount", Integer.parseInt(quantity.trim()));
        action.set("data", depositInfo);

        try {
            this.socketClient = new Client();
            this.socketClient.resultDelegate = this;
            this.socketClient.startConnectionWithInfo(action.toString());
        } catch (Exception e) {
            System.out.println("Excepcion al levantar conexion: " +  e.getMessage());
        }
    }

    public void withdrawal(){

        System.out.println("Ingresa tu cedula, clave y monto a retirar separado por ','");

        String info = this.scanner.nextLine();
        String[] withdrawalData = info.split(",");
        String personId = withdrawalData[0];
        String password = withdrawalData[1];
        String quantity = withdrawalData[2];
        
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode action = mapper.createObjectNode();
        action.put("action", "execute_withdrawal");

        ObjectNode withdrawalInfo = mapper.createObjectNode();
        withdrawalInfo.put("personId", personId.trim());
        withdrawalInfo.put("password", password.trim());
        withdrawalInfo.put("amount", Integer.parseInt(quantity.trim()));

        action.set("data", withdrawalInfo);

        try {
            this.socketClient = new Client();
            this.socketClient.resultDelegate = this;
            this.socketClient.startConnectionWithInfo(action.toString());
        } catch (Exception e) {
            System.out.println("Excepcion al levantar conexion: " +  e.getMessage());
        }
    }

    public void balance(){
        System.out.println("Para solicitar su saldo");
        System.out.println("Ingresa tu cedula y clave separado por ','");

        String info = this.scanner.nextLine();
        String[] withdrawalData = info.split(",");
        String personId = withdrawalData[0];
        String password = withdrawalData[1];

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode action = mapper.createObjectNode();
        action.put("action", "get_balance");

        ObjectNode balanceInfo = mapper.createObjectNode();
        balanceInfo.put("personId", personId.trim());
        balanceInfo.put("password", password.trim());
        action.set("data", balanceInfo);

        try {
            this.socketClient = new Client();
            this.socketClient.resultDelegate = this;
            this.socketClient.startConnectionWithInfo(action.toString());
        } catch (Exception e) {
            System.out.println("Excepcion al levantar conexion: " +  e.getMessage());
        }
    }

    private void exit(){
        System.out.println("Gracias por usar nuestro banco.");
    }

    public void createUserInBank(String name, String lastname, String phone, String personId, String password){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode action = mapper.createObjectNode();
        action.put("action", "create_client");

        ObjectNode user = mapper.createObjectNode();
        user.put("name", name.trim());
        user.put("lastName", lastname.trim());
        user.put("phone", phone.trim());
        user.put("personId", personId.trim());
        user.put("branchId", 1);
        user.put("password", password.trim());
        action.set("data", user);

        try {
            this.socketClient = new Client();
            this.socketClient.resultDelegate = this;
            this.socketClient.startConnectionWithInfo(action.toString());
        }catch (Exception e){
            System.out.println("Excepción al levantar conexión: " + e.getMessage());
        }
    }

    @Override
    public void actionExecuted(boolean completed, String message) {
        //System.out.println("re-start");
        this.initialScreen();
    }
}
