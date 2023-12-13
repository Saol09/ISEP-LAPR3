package lapr.project.ui;

import javafx.util.Pair;
import lapr.project.controller.SearchInformationController;
import lapr.project.model.Country;
import lapr.project.model.Port;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

import lapr.project.utils.portsCapitalsGraph.IPortsAndCapitals;

/**
 * The type Main.
 *
 * @author Nuno Bettencourt <nmb@isep.ipp.pt> on 24/05/16.
 */
class Main {

    /**
     * Logger class.
     */
    private static final Logger LOGGER = Logger.getLogger("MainLog");

    /**
     * The Scan.
     */
    static Scanner scan = new Scanner(System.in);

    /**
     * Private constructor to hide implicit public one.
     */
    private Main() {

    }

    /**
     * Application main method.
     *
     * @param args the command line arguments
     * @throws IOException the io exception
     */
    public static void main(String[] args) throws IOException {
        SearchInformationController controller = new SearchInformationController();
        int choice;
        String valor = "s";
        while (true) {
            choice = menuPrincipal();
            switch (choice) {
                case 1:

                    scan.nextLine();
                    while (valor.equalsIgnoreCase("s")) {
                        System.out.println("\n\nIntroduzir o código MMSI do navio pretendido:");
                        String input1 = scan.nextLine();
                        System.out.println("Movimentos do navio");
                        Pair<String, List<String>> pair = controller.searchInformationShip(input1);
                        if (pair != null) {
                            System.out.println("Codigo:" + pair.getKey());
                            System.out.println("Nome:" + pair.getValue().get(0));
                            System.out.println("VesselType:" + pair.getValue().get(1));
                            System.out.println("Data Partida:" + pair.getValue().get(2));
                            System.out.println("Data Chegada:" + pair.getValue().get(3));
                            System.out.println("Total Movimentos:" + pair.getValue().get(4));
                            System.out.println("Max Sog:" + pair.getValue().get(5));
                            System.out.println("Mean Sog:" + pair.getValue().get(6));
                            System.out.println("Max Cog:" + pair.getValue().get(7));
                            System.out.println("Mean Cog:" + pair.getValue().get(8));
                            System.out.println("Latitude Partida:" + pair.getValue().get(9));
                            System.out.println("Longitude Partida:" + pair.getValue().get(10));
                            System.out.println("Latitude Chegada:" + pair.getValue().get(11));
                            System.out.println("Longitude Chegada:" + pair.getValue().get(12));
                            System.out.println("Distancia Total (Travelled Distance):" + pair.getValue().get(13) + " Kilometros");
                            System.out.println("Intervalo de Distancia (Delta Distance):" + pair.getValue().get(14) + " Kilometros");
                        } else {
                            System.out.println("O codigo que passou não corresponde a nenhum codigo de navio");
                        }
                        System.out.println("Deseja procurar outro navio?(s/n)");
                        valor = scan.nextLine();

                    }
                    break;

                case 2:

                    scan.nextLine();
                    System.out.println("\n\nInformações sobre todos os navios:");
                    controller.shipsMovementsAndDistanceController();
                    break;
                case 3:

                    scan.nextLine();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

                    System.out.println("\n\nIntroduza o número de barcos pretendidos para análise:");
                    Integer inputN = scan.nextInt();

                    scan.nextLine();
                    System.out.println("\n\nIntroduzir uma data inicial do período de tempo pretendido (dd/MM/yyyy HH:mm):");
                    String inputDataIni = scan.nextLine();
                    LocalDateTime dateTimeIni = LocalDateTime.parse(inputDataIni, formatter);

                    System.out.println("\n\nIntroduzir uma data final do período de tempo pretendido (dd/MM/yyyy HH:mm):");
                    String inputDataFinal = scan.nextLine();
                    LocalDateTime dateTimeFinal = LocalDateTime.parse(inputDataFinal, formatter);


                    System.out.println("Top-N navios com mais quilómetros percorridos e respetiva velocidade média:");
                    controller.topNShipsController(inputN, dateTimeIni, dateTimeFinal);
                    break;
                case 4:

                    scan.nextLine();

                    System.out.println("\nPares de navios com rotas de coordenadas de partida/chegada próximas e com diferentes " +
                            "TraveledDistance");

                    controller.paresIguais();
                    break;

                case 5:

                    scan.nextLine();
                    DateTimeFormatter formatter_ = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

                    System.out.println("\n\nIntroduza o CallSign do barco pretendido para análise:");
                    String inputNV = scan.nextLine();

                    System.out.println("\n\nIntroduzir uma data do período de tempo pretendido (dd/MM/yyyy HH:mm):");
                    String inputData = scan.nextLine();
                    LocalDateTime dateTime = LocalDateTime.parse(inputData, formatter_);


                    System.out.println("Porto mais próximo do navio " + inputNV + ":\n");

                    Port port = controller.portoMaisProximo(inputNV, dateTime);

                    if (port == null)
                        System.out.println("O navio escolhido ainda não enviou nenhuma mensagem até à data escolhida.");

                    if (port != null)
                        System.out.println(port);

                    break;
                case 6:

                    scan.nextLine();
                    System.out.println("\nCriar grafo:");
                    System.out.println("\nN portos mais próximos:");
                    int inputG = scan.nextInt();
                    controller.createGraph(inputG);

                    break;

                case 7:

                    scan.nextLine();
                    System.out.println("\nColorir o mapa:");
                    controller.mapColour();

                    break;

                case 8:

                    scan.nextLine();
                    System.out.println("\n\nIntroduza o número de locais pretendidos para análise:");
                    int inputL = scan.nextInt();

                    System.out.println("\nTop N closeness places by Continent:");
                    controller.nClosestLocalsByContinent(inputL);
                    break;
                case 9:

                    scan.nextLine();
                    System.out.println("\nIntroduza o número de portos para verificar a sua maior centralidade:");
                    int inputC = scan.nextInt();

                    System.out.println("\nTop N de portos com maior Centralidade:\n");
                    controller.getNPortsGreaterCentrality(inputC);
                    break;

                case 10:

                    scan.nextLine();
                    System.out.println("\nDeseja que o seu local de partida seja um Porto ou uma Capital?");
                    System.out.println("\n1 - Porto" + "\n2 - Capital" + "\n\n0 - Voltar ao menu principal");
                    int num = scan.nextInt();
                    switch (num) {
                        case 1:


                            System.out.println("Insira o código do Porto");
                            scan.nextLine();
                            String inputPort = scan.nextLine();
                            

                            controller.getCircuitFromInicialLocPort(inputPort);

                            break;
                        case 2:

                            System.out.println("Insira o nome da capital");
                            scan.nextLine();
                            String inputCapital = scan.nextLine();

                            controller.getCircuitFromInicialLocCapital(inputCapital);


                            break;

                        case 0:
                            break;

                        default:
                            System.out.println("Escolha inválida, introduza um número que esteja no menu!");

                    }
                    break;
                case 11:
                    scan.nextLine();
                    System.out.println("Selecione o tipo de shortestPath que quer");
                    System.out.println("1-Shortest Path Maritime");
                    System.out.println("2-Shortest Path Land");
                    System.out.println("3-Shortest Path No Restrictions");
                    System.out.println("4-Shortest Path Passing in N");
                    String respostaCaminho = scan.nextLine();
                    if(respostaCaminho.equalsIgnoreCase("4")){
                        System.out.println("Local Incial?");
                        String strInicial = scan.nextLine();
                        List<String> listaPassingN = new ArrayList<>();
                        String parar = "";
                        while(!parar.equals("sim")){
                            System.out.println("Insira um porto que pretende passar");
                            listaPassingN.add(scan.nextLine());
                            System.out.println("Pretende parar de adicionar n?");
                            parar = scan.nextLine();
                        }
                        System.out.println("Local final?");
                        String strFinal = scan.nextLine();

                        System.out.println(controller.getShortestPathPassingN(strInicial, strFinal, listaPassingN));
                    }
                    break;

                case 0:
                    return;

                default:
                    System.out.println("Escolha inválida, introduza um número que esteja no menu!");

            }

        }


    }


    private static int menuPrincipal() {
        String header = "------------MENU------------"
                + "\nEscolha a funcionalidade:"
                + "\n1 - Sumário dos movimentos de um navio;"
                + "\n2 - Apresentar para todos os navios o MMSI, o número total de movimentos, TraveledDistance e DeltaDistance"
                + "\n3 - Apresentar os top-N navios com mais quilómetros percorridos e respetiva velocidade média " +
                "num período tempo agrupado por tipo de navio;"
                + "\n4 - Apresentar os pares de navios com rotas com coordenadas de partida/chegada próximas " +
                "e com diferentes TraveledDistance;"
                + "\n5 - Encontrar o porto mais próximo de um navio dado o seu CallSign para uma determinada data;"
                + "\n6 - Criar o Grafo;"
                + "\n7 - Colorir o mapa;"
                + "\n8 - Cidades e portos mais próximos de outros (closeness places);"
                + "\n9 - Portos com maior centralidade;"
                + "\n10 - Circuito mais eficiente a partir de um local de partida (sem visitar um local mais de uma " +
                "vez e voltando ao local de partida ao final);"
                + "\n11 - Shortest Path entre dois pontos;"
                + "\n\n0 - Sair.";
        System.out.printf("%n%s%n", header);
        return scan.nextInt();
    }



       /* Pair<String, List<String>> pair = controller.searchInformationShip("V2DD5");
        if(pair!= null){
            System.out.println("Codigo:"+pair.getKey());
            System.out.println("Nome:"+pair.getValue().get(0));
            System.out.println("VesselType:"+pair.getValue().get(1));
            System.out.println("Data Partida:"+pair.getValue().get(2));
            System.out.println("Data Chegada:"+pair.getValue().get(3));
            System.out.println("Total Movimentos:"+pair.getValue().get(4));
            System.out.println("Max Sog:"+ pair.getValue().get(5));
            System.out.println("Mean Sog:"+pair.getValue().get(6));
            System.out.println("Max Cog:"+pair.getValue().get(7));
            System.out.println("Mean Cog:"+pair.getValue().get(8));
            System.out.println("Latitude Partida:"+pair.getValue().get(9));
            System.out.println("Longitude Partida:"+pair.getValue().get(10));
            System.out.println("Latitude Chegada:"+pair.getValue().get(11));
            System.out.println("Longitude Chegada:"+ pair.getValue().get(12));
            System.out.println("Distancia Total (Travelled Distance):"+pair.getValue().get(13)+" Kilometros");
            System.out.println("Intervalo de Distancia (Delta Distance):"+pair.getValue().get(14)+" Kilometros");
        }else{
            System.out.println("O codigo que passou não corresponde a nenhum codigo de navio");
        }*/

    //}
}


